package com.csform.android.MB360.insurance.claimdataupload.views


import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatEditText
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.csform.android.MB360.R
import com.csform.android.MB360.databinding.FragmentFileUploadCDUBinding
import com.csform.android.MB360.insurance.claimdataupload.adapters.ClaimFileUploadAdapter
import com.csform.android.MB360.insurance.claimdataupload.base.ApiHelperImpl
import com.csform.android.MB360.insurance.claimdataupload.base.UiState
import com.csform.android.MB360.insurance.claimdataupload.base.ViewModelFactory
import com.csform.android.MB360.insurance.claimdataupload.interfaces.ExpandListerner
import com.csform.android.MB360.insurance.claimdataupload.interfaces.FileUploadListerner
import com.csform.android.MB360.insurance.claimdataupload.interfaces.ViewPagerNavigation
import com.csform.android.MB360.insurance.claimdataupload.repository.ClaimDocumentUploadViewModel
import com.csform.android.MB360.insurance.claimdataupload.responseclass.AaData
import com.csform.android.MB360.insurance.claimdataupload.responseclass.ClaimFileUpload
import com.csform.android.MB360.insurance.claimdataupload.responseclass.FilePathList
import com.csform.android.MB360.insurance.claimdataupload.responseclass.LoadRequiredDocumentDetails.Detail
import com.csform.android.MB360.insurance.claimdataupload.retrofit.ClaimIntimationRetrofitClient
import com.csform.android.MB360.insurance.repository.LoadSessionViewModel
import com.csform.android.MB360.insurance.repository.selectedPolicyRepo.SelectedPolicyViewModel
import com.csform.android.MB360.insurance.repository.selectedPolicyRepo.responseclass.GroupPolicyData
import com.csform.android.MB360.utilities.DefaultDispatcherProvider
import com.csform.android.MB360.utilities.EncryptionPreference
import com.csform.android.MB360.utilities.FileUtil
import com.csform.android.MB360.utilities.LogMyBenefits
import com.csform.android.MB360.utilities.LogTags
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL
import java.util.Objects


class FileUploadFragmentCDU() : Fragment(), FileUploadListerner, ExpandListerner {
    private var viewPagerNavigation: ViewPagerNavigation? = null



    var position: Int? = null
    var adapter: ClaimFileUploadAdapter? = null
    var fileLauncherActivity: ActivityResultLauncher<Intent>? = null
    private val binding get() = _binding!!
    private var _binding: FragmentFileUploadCDUBinding? = null
    private var responseList = ArrayList<ClaimFileUpload>()
    private var fileDataList = ArrayList<String>()
    var claimDocumentUploadViewModel: ClaimDocumentUploadViewModel? = null
    var loadSessionViewModel: LoadSessionViewModel? = null
    var selectedPolicyViewModel: SelectedPolicyViewModel? = null
    private val i = 0
    var file: File? = null
    lateinit var CLM_DOCS_UPLOAD_REQ_SR_NO: String

    private var docDetailList = ArrayList<Detail>()
    var aaData: AaData? = null
    private var filePathList = ArrayList<FilePathList>()
    private var filePathListFORAdditional = ArrayList<FilePathList>()


    override

    fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            viewPagerNavigation = it.getSerializable("viewPagerNavigation") as? ViewPagerNavigation
        }


        fileLauncherActivity = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            //queryViewModel.setLoadingFromFilePicker()

            if (result.resultCode == Activity.RESULT_OK) {
                // There are no request codes
                val data = result.data
                Log.d("FILE-PICKER", "filepicker: " + result.data!!.type)

                try {

                    val file2 = FileUtil.from(requireContext(), data!!.data)
                    // File file2 = new File(getPath(data.getData()));
                    val filePath = file2.path
                    var FileExists = false
                    val filenameArray = filePath.split("\\.".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                    val extension = filenameArray[filenameArray.size - 1]

                    val fileName = getFileNameFromPath(filePath)

                    file = File(filePath)
                    val file_size: String = java.lang.String.valueOf(file!!.length() / 1024).toInt().toString()

                    if (file_size.toInt() <= 5000) {

                        if (!fileDataList.contains(fileName)) {
                            responseList[position!!].fileName = fileName
                            responseList[position!!].status = true
                            responseList[position!!].fileSize = file_size
                            responseList[position!!].filePathInfo = filePath
                            adapter!!.notifyItemChanged(position!!)
                            adapter?.notifyDataSetChanged()
                            fileName.let { fileDataList.add(fileName) }

                        } else {
                            Toast.makeText(requireContext(), "File already uploaded", Toast.LENGTH_SHORT).show()
                        }

                    } else {
                        Toast.makeText(requireContext(), "File max size is 5mb", Toast.LENGTH_SHORT).show()
                    }
                    validate()
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }


    }


    fun getFileNameFromPath(filePath: String): String {
        val lastIndex = filePath.lastIndexOf('/')
        if (lastIndex != -1) {
            return filePath.substring(lastIndex + 1)
        }
        return filePath // Return the entire string if no slash is found
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        _binding = FragmentFileUploadCDUBinding.inflate(inflater, container, false)

        val view = binding.root
        claimDocumentUploadViewModel = ViewModelProvider(
                requireActivity(),
                ViewModelFactory(
                        ApiHelperImpl(ClaimIntimationRetrofitClient.apiService),
                        DefaultDispatcherProvider()
                )
        )[ClaimDocumentUploadViewModel::class.java]
        loadSessionViewModel = ViewModelProvider(requireActivity()).get(LoadSessionViewModel::class.java)
        selectedPolicyViewModel = ViewModelProvider(requireActivity()).get(SelectedPolicyViewModel::class.java)

        claimDocumentUploadViewModel!!.LoadRequiredClaimsDocDetails(loadSessionViewModel!!.loadSessionData.value!!.getGroupInfoData().getGroupchildsrno(),
                loadSessionViewModel!!.loadSessionData.value!!.groupPolicies.get(0).groupGMCPoliciesData[0].oeGrpBasInfSrNo)

        setupObserver()
        return view
    }


    private suspend fun getFilePathFromURL(fileURL: String, destination: String?): String? {
        return try {
            val url = URL(fileURL)
            val connection = url.openConnection() as HttpURLConnection
            connection.requestMethod = "GET"

            withContext(Dispatchers.IO) {
                connection.connect()

                if (connection.responseCode == HttpURLConnection.HTTP_OK) {
                    val inputStream: InputStream = connection.inputStream
                    val fileName = File(url.path).name
                    val localFilePath = File(destination, fileName)

                    localFilePath.outputStream().use { output ->
                        inputStream.copyTo(output)
                    }
                    localFilePath.absolutePath
                } else {
                    Log.e("Download Error", "HTTP Response Code: ${connection.responseCode}")
                    null // Handle HTTP error, e.g., connection.responseMessage
                }
            }
        } catch (e: Exception) {
            Log.e("Download Error", "Exception: ${e.toString()}")
            e.printStackTrace()
            null
        }

    }


    fun getFileExtension(filePath: String): String {
        val fileName = File(filePath).name
        val dotIndex = fileName.lastIndexOf(".")
        return if (dotIndex > 0) {
            fileName.substring(dotIndex + 1)
        } else {
            ""
        }
    }


    fun removeExtraStringFromFilePath(input: String): String {
        val fileExtensions = arrayOf(".jpeg", ".png", ".pdf", ".jpg")

        for (extension in fileExtensions) {
            val extensionIndex = input.lastIndexOf(extension)
            if (extensionIndex != -1) {
                return input.substring(0, extensionIndex + extension.length)
            }
        }

        // If no supported extension was found, return the original string
        return input


    }


    companion object {
        fun newInstance(viewPagerNavigation: ViewPagerNavigation): FileUploadFragmentCDU {
            val fragment = FileUploadFragmentCDU()
            val args = Bundle()

            args.putInt("pos", 2)
            args.putSerializable("viewPagerNavigation", viewPagerNavigation)
            fragment.arguments = args


            return fragment
        }
    }

    override fun fileOnClickListerner(pos: Int) {
        // openFilePicker()
        position = pos
        val item = responseList[position!!]
        if (item.status) {
            removeData()
        } else {
            val filePickerIntent = Intent(Intent.ACTION_GET_CONTENT)
            filePickerIntent.type = "*/*" // Allow all file types
            fileLauncherActivity?.launch(filePickerIntent)
            this.position = pos
        }


    }


    private fun removeData() {


        //e-card available
        val dialog = Dialog(requireContext())
        dialog.setContentView(R.layout.smslayout)
        Objects.requireNonNull(dialog.window)?.setBackgroundDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.nhborder))

        val lblSMS = dialog.findViewById<AppCompatTextView>(R.id.lblSMS)
        val smsContact = dialog.findViewById<AppCompatEditText>(R.id.smsContact)
        val btnSubmit = dialog.findViewById<AppCompatButton>(R.id.btnSubmit)
        val btnCancel = dialog.findViewById<AppCompatButton>(R.id.btnCancel)
        val llTitle = dialog.findViewById<LinearLayout>(R.id.llTitle)
        val lblSMSHeader = dialog.findViewById<AppCompatTextView>(R.id.lblSMSHeader)
        llTitle.visibility = View.VISIBLE
        lblSMS.text = "Alert"
        lblSMSHeader.text = "Are you sure you want to delete this file?"
        btnSubmit.text = "Yes"
        btnCancel.text = "No"
        smsContact.visibility = View.GONE
        btnSubmit.setOnClickListener { v1: View? ->

            fileDataList.remove(responseList[position!!].fileName)

            responseList[position!!].status = false
            responseList[position!!].fileName = ""
            responseList[position!!].fileSize = ""
            responseList[position!!].data = null

            adapter?.notifyDataSetChanged()
            validate()
            dialog.dismiss()

        }
        btnCancel.setOnClickListener { v: View? ->
            dialog.dismiss()
        }
        dialog.setCancelable(false)
        dialog.show()


    }


    override fun onAttach(context: Context) {
        super.onAttach(context)
        fileLauncherActivity = registerForActivityResult<Intent, ActivityResult>(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->

            if (result.resultCode == Activity.RESULT_OK) {
                val data = result.data
                Log.d("FILE-PICKER", "filepicker: " + result.data!!.type)
                try {
                    val file2 = FileUtil.from(context, data!!.data)
                    // File file2 = new File(getPath(data.getData()));
                    val filePath = file2.path
                    var FileExists = false
                    val filenameArray = filePath.split("\\.".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                    val extension = filenameArray[filenameArray.size - 1]

                } catch (e: java.lang.Exception) {
                    e.printStackTrace()
                }
            }
        }

    }


    private fun validate() {


        val allMandatoryFileUploaded = responseList.none { it.mandatory && !it.status }
        if (allMandatoryFileUploaded) {
            try {

                claimDocumentUploadViewModel = ViewModelProvider(
                        requireActivity(),
                        ViewModelFactory(
                                ApiHelperImpl(ClaimIntimationRetrofitClient.apiService),
                                DefaultDispatcherProvider()
                        )
                )[ClaimDocumentUploadViewModel::class.java]
                val groupPolicyData: GroupPolicyData = selectedPolicyViewModel!!.getSelectedPolicy().getValue()!!
                loadSessionViewModel = ViewModelProvider(requireActivity()).get(LoadSessionViewModel::class.java)

                val emp_sr_no: String = loadSessionViewModel!!.loadSessionData.value!!.getGroupPoliciesEmployees().get(0).getGroupGMCPolicyEmployeeData().get(0).getEmployeeSrNo()
                //
                claimDocumentUploadViewModel!!.viewModelScope.launch {
                    claimDocumentUploadViewModel!!.uiStateSelecetdData.collect {
                        when (it) {
                            is UiState.Success -> {
                                LogMyBenefits.d(LogTags.CDU_FILE_UPLOAD_FRAGMENTS, "SUCCESS")
                                CLM_DOCS_UPLOAD_REQ_SR_NO = it.data.CLMDOCSUPLOADREQSRNO.toString()
                            }

                            is UiState.Error -> {
                                LogMyBenefits.d(LogTags.CDU_FILE_UPLOAD_FRAGMENTS, it.message)
                            }

                            is UiState.Loading -> {
                                LogMyBenefits.d(LogTags.CDU_FILE_UPLOAD_FRAGMENTS, "LOADING")
                            }

                            else -> {
                                LogMyBenefits.d(LogTags.CDU_FILE_UPLOAD_FRAGMENTS, "FAILED")
                            }
                        }
                    }

                }

                val encryptionPreference = EncryptionPreference(requireActivity().getApplicationContext())
                var CLAIM_NO = encryptionPreference.getEncryptedDataString("CLAIM_INTIMATION_SR_NO")
                LogMyBenefits.d(LogTags.CDU_FILE_UPLOAD_FRAGMENTS, "$CLAIM_NO")
                viewPagerNavigation!!.uploadedDocs(responseList)


            } catch (e: Exception) {
                e.stackTrace
            }
            viewPagerNavigation?.enableNextLayout()
        } else {

            if((!claimDocumentUploadViewModel?.EditData?.value?.FILES.isNullOrEmpty())&& (!claimDocumentUploadViewModel?.EditData?.value?.FILES.equals("-")))
            {
                viewPagerNavigation?.enableNextLayout()
            }else {
                viewPagerNavigation?.disableNextLayout()
            }
        }
    }

    override fun onResume() {
        super.onResume()


        //   setupObserver()

        validate()
    }

    private fun setupObserver() {
        claimDocumentUploadViewModel = ViewModelProvider(
                requireActivity(),
                ViewModelFactory(
                        ApiHelperImpl(ClaimIntimationRetrofitClient.apiService),
                        DefaultDispatcherProvider()
                )
        )[ClaimDocumentUploadViewModel::class.java]
        loadSessionViewModel = ViewModelProvider(requireActivity()).get(LoadSessionViewModel::class.java)
        selectedPolicyViewModel = ViewModelProvider(requireActivity()).get(SelectedPolicyViewModel::class.java)

        claimDocumentUploadViewModel!!.LoadRequiredClaimsDocDetails(loadSessionViewModel!!.loadSessionData.value!!.getGroupInfoData().getGroupchildsrno(),
                loadSessionViewModel!!.loadSessionData.value!!.groupPolicies.get(0).groupGMCPoliciesData[0].oeGrpBasInfSrNo)

        //api call
        try {

            claimDocumentUploadViewModel!!.viewModelScope.launch {
                claimDocumentUploadViewModel!!.uiStateLoadRequiredClaimsDoc.collect {
                    when (it) {
                        is UiState.Success -> {
                            responseList.clear()

                            binding.progressBar.visibility = View.GONE
                            binding.errorLayout.visibility = View.GONE
                            if (!it.data.Detail.isNullOrEmpty()) {
                                LogMyBenefits.d(LogTags.CDU_FILE_UPLOAD_FRAGMENTS, "SUCCESS")
                                LogMyBenefits.d(LogTags.CDU_FILE_UPLOAD_FRAGMENTS, "${it.data.Detail}")

                                aaData = claimDocumentUploadViewModel?.EditData?.value
                                if (aaData != null) {
                                    aaData = claimDocumentUploadViewModel!!.EditData.value!!
                                    var filePath = aaData!!.FILES

                                    var filename = ""
                                    var newfilePath = ""
                                    if (!filePath.isNullOrEmpty()) {
                                        val splitStrings = filePath.split("~")

                                        for (part in splitStrings) {
                                            if (!part.isNullOrEmpty()) {

                                                if (part != "-") {

                                                    val claimFormString = part.substringAfterLast('(').substringBeforeLast(')')
                                                    println(claimFormString)
                                                    LogMyBenefits.d(LogTags.CDU_FILE_UPLOAD_FRAGMENTS, "claimform string" + claimFormString)
                                                    var response = ClaimFileUpload(claimFormString!!,
                                                            i + 1,
                                                            false,
                                                            "",
                                                            "",
                                                            false,
                                                            data = null,
                                                            false,
                                                            "",
                                                            "",
                                                            0, "", true

                                                    )
                                                    responseList.add(response)


                                                }
                                            }
                                        }

                                        docDetailList = it.data.Detail
                                        // for (i in 0..docDetailList.size) {

                                        for (user in docDetailList) {

                                            val response = ClaimFileUpload(user.CLMDOCNAME!!,
                                                    i + 1,
                                                    false,
                                                    "",
                                                    "",
                                                    true,
                                                    data = null,
                                                    false,
                                                    "",
                                                    "",
                                                    user.CLMREQDOCSSRNO!!, "", false

                                            )
                                            responseList.add(response)


                                        }
                                        val extraResponse = ClaimFileUpload(items = "Additional document",
                                                int = docDetailList.size!! + 1,
                                                status = false,
                                                fileSize = "",
                                                fileName = "",
                                                mandatory = false,
                                                data = null,
                                                remark = "",
                                                typedtext = "",
                                                CLM_REQ_DOCS_SR_NO = 0,
                                                filePathInfo = "",
                                                fromEdit = false

                                        )
                                        responseList.add(extraResponse)
                                    }



                                    LogMyBenefits.d(LogTags.CDU_FILE_UPLOAD_FRAGMENTS, "$responseList")
                                    adapter = ClaimFileUploadAdapter(responseList, this@FileUploadFragmentCDU, this@FileUploadFragmentCDU, requireActivity())
                                    binding.rvFileUpload.adapter = adapter
                                    validate()

                                } else {

                                    docDetailList = it.data.Detail
                                    for (user in docDetailList) {

                                        var response = ClaimFileUpload(user.CLMDOCNAME!!,
                                                i + 1,
                                                false,
                                                "",
                                                "",
                                                true,
                                                data = null,
                                                false,
                                                "",
                                                "",
                                                user.CLMREQDOCSSRNO!!, "",
                                                false

                                        )
                                        responseList.add(response)


                                    }
                                    val extraResponse = ClaimFileUpload(items = "Additional document",
                                            int = docDetailList.size!! + 1,
                                            status = false,
                                            fileSize = "",
                                            fileName = "",
                                            mandatory = false,
                                            data = null,
                                            remark = "",
                                            typedtext = "",
                                            CLM_REQ_DOCS_SR_NO = 0,
                                            filePathInfo = "",
                                            fromEdit = false

                                    )
                                    responseList.add(extraResponse)


                                    LogMyBenefits.d(LogTags.CDU_FILE_UPLOAD_FRAGMENTS, "$responseList")
                                    adapter = ClaimFileUploadAdapter(responseList, this@FileUploadFragmentCDU, this@FileUploadFragmentCDU, requireActivity())
                                    binding.rvFileUpload.adapter = adapter

                                }
                            } else {
                                binding.progressBar.visibility = View.GONE
                                binding.errorLayout.visibility = View.VISIBLE
                            }
                        }

                        is UiState.Error -> {
                            binding.progressBar.visibility = View.GONE
                            binding.errorLayout.visibility = View.VISIBLE
                            LogMyBenefits.d(LogTags.CDU_FILE_UPLOAD_FRAGMENTS, it.message)
                            Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
                        }

                        is UiState.Loading -> {
                            binding.progressBar.visibility = View.VISIBLE
                            binding.errorLayout.visibility = View.GONE
                            LogMyBenefits.d(LogTags.CDU_FILE_UPLOAD_FRAGMENTS, "LOADING")
                        }

                        else -> {
                            LogMyBenefits.d(LogTags.CDU_FILE_UPLOAD_FRAGMENTS, "FAILED")
                        }
                    }
                }

            }
        } catch (e: Exception) {
            e.stackTrace
        }
    }


    override fun onExpandListerner(previousExpandedPosition: Int, position: Int) {
        adapter!!.notifyItemChanged(previousExpandedPosition)
        adapter!!.notifyItemChanged(position)

    }

}





