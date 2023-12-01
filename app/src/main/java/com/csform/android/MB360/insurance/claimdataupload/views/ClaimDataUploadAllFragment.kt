package com.csform.android.MB360.insurance.claimdataupload.views

import android.Manifest
import android.content.ActivityNotFoundException
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.csform.android.MB360.databinding.FragmentClaimDataUploadedFragmentBinding
import com.csform.android.MB360.insurance.claimdataupload.adapters.UploadedAllFileAdapter
import com.csform.android.MB360.insurance.claimdataupload.base.ClaimFileDownloader
import com.csform.android.MB360.insurance.claimdataupload.interfaces.FileDownLoadHelper
import com.csform.android.MB360.insurance.claimdataupload.responseclass.AaData
import com.csform.android.MB360.insurance.claimdataupload.responseclass.FilePathList
import com.csform.android.MB360.insurance.utilities.ui.PermissionHelper
import com.csform.android.MB360.utilities.LogMyBenefits
import com.csform.android.MB360.utilities.LogTags
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.util.Locale

class ClaimDataUploadAllFragment() : Fragment(), FileDownLoadHelper {

    private var _binding: FragmentClaimDataUploadedFragmentBinding? = null
    private val binding get() = _binding!!
    private var filePathList = ArrayList<FilePathList>()
    var uploadedAllFileAdapter: UploadedAllFileAdapter? = null

    var selectedIndex = 0

    var position = 0
    var utility: FilePathList? = null

    // Declare a permission request launcher using the new API
    private var requestPermissionLauncher: ActivityResultLauncher<Array<String>>? = null


    var permissionHelper = PermissionHelper {
        try {

            lifecycleScope.launch {
                val intent = ClaimFileDownloader.downloadFile("https://employee.benefitsyou.com/" + utility!!.path, File(requireContext().filesDir, utility!!.fileName),
                        requireActivity(), requireContext())

                if (intent != null) {
                    context?.startActivity(intent)
                } else {
                    // Handle the case when the intent is null (e.g., an error occurred during download)
                }
            }
        } catch (e: ActivityNotFoundException) {
            e.printStackTrace()
            Toast.makeText(requireContext(), "No application found to view this file!", Toast.LENGTH_SHORT).show()
        }
        onFinishDownload(position)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        arguments?.let {

        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        _binding = FragmentClaimDataUploadedFragmentBinding.inflate(inflater, container, false)
        val view = binding.root
        getFilePaths()
        return view
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        val requestPermissionLauncher =
                registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
                    if (permissions[Manifest.permission.READ_EXTERNAL_STORAGE] == true &&
                            permissions[Manifest.permission.WRITE_EXTERNAL_STORAGE] == true
                    ) {
                        onStartDownload(position)
                        permissionHelper.granted()
                    } else {
                        Toast.makeText(requireContext(), "Permission denied", Toast.LENGTH_SHORT).show()
                        onFinishDownload(position)
                    }
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

    private fun getFilePaths() {

        if (requireArguments().getParcelable<AaData>("variableName") != null && (!requireArguments().getParcelable<AaData>("variableName")?.FILES.equals("-"))) {
            binding.errorLayoutCdu.visibility=View.GONE
            val allFileDataObject = requireArguments().getParcelable<AaData>("variableName")

            LogMyBenefits.d(LogTags.CLAIM_DATA_UPLOADALL_FRAGMENT, "files" + allFileDataObject!!.FILES)
            binding.progressBar.visibility = View.GONE
            // var filePath = "mybenefits/Claim_upload_documents/88159/881/316/Free_Test_Data_102KB_PDF.pdf(DULY FILLED THE CLAIM FORM)~mybenefits/Claim_upload_documents/88159/881/317/B48FE312-37DB-47E8-9FC5-DDC71AA9450B.jpeg(Claim Form)~"
            //   var filePath = "mybenefits/Claim_upload_documents/88159/881/316/Free_Test_Data_102KB_PDF.pdf~mybenefits/Claim_upload_documents/88159/881/317/B48FE312-37DB-47E8-9FC5-DDC71AA9450B.jpeg~"
            var filePath = allFileDataObject!!.FILES
            var filename = ""
            var filenameTemp = ""
            if (!filePath.isNullOrEmpty()) {
                val splitStrings = filePath.split("~")
                for (part in splitStrings) {
                    if (!part.isNullOrEmpty()) {
                        //var path = part
                        val path = removeExtraStringFromFilePath(part)
                        LogMyBenefits.d(LogTags.CLAIM_DATA_UPLOADALL_FRAGMENT, "path$path")
                        if (!path.isNullOrEmpty()) {

                            val fileExtension = getFileExtension(path)
                            var file = File(path)

                            filenameTemp = part.substringAfterLast('(').substringBeforeLast(')')


                            filename = file.name


                            val response = FilePathList(
                                    path = path,
                                    fileName = filename,
                                    fileNameTemp=filenameTemp,
                                    fileType = fileExtension)
                            filePathList.add(response)


                        }
                    }
                }

                uploadedAllFileAdapter = UploadedAllFileAdapter(requireContext(), filePathList, requireActivity(), this)
                binding.rvAllClaimsUploded.adapter = uploadedAllFileAdapter
                //utilitiesAdapter.notifyItemRangeChanged(0, utilitiesResponse.getUtilitiesData().size)

            } else {
                binding.errorLayoutCdu.visibility = View.VISIBLE

            }

        }else
        {
            binding.errorLayoutCdu.visibility=View.VISIBLE
            binding.progressBar.visibility=View.GONE

        }
    }

    fun removeExtraStringFromFilePath(input: String): String {

        val fileExtensions = arrayOf(".jpeg", ".png",".pdf",".jpg",".xlsx",".docx",".doc")

        for (extension in fileExtensions) {
            val extensionIndex = input.lastIndexOf(extension)
            if (extensionIndex != -1) {
                return input.substring(0, extensionIndex + extension.length)
            }
        }

        // If no supported extension was found, return the original string
        return input


    }

    override fun onUtilitiesClicked(position: Int) {

        val util = filePathList[position]
        val folder = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
        val    file = File(context?.filesDir, util.fileName)// Replace with your data source
        if (Build.VERSION.SDK_INT > 32) {
            LogMyBenefits.d(LogTags.CLAIM_DATA_UPLOADALL_FRAGMENT, "downloadFilePDF: created a new File " + file!!.absolutePath)
            try {

                lifecycleScope.launch {
                    val intent = ClaimFileDownloader.downloadFileWithoutPermission("https://employee.benefitsyou.com/${util.path}", file, requireActivity(), requireContext())

                    if (intent != null) {
                        context?.startActivity(intent)
                    } else {
                        // Handle the case when the intent is null (e.g., an error occurred during download)
                    }
                }            } catch (e: ActivityNotFoundException) {
                e.printStackTrace()

            }
        } else {
            try {
                lifecycleScope.launch {
                    val intent = ClaimFileDownloader.downloadFile("https://employee.benefitsyou.com/${util.path}", file, requireActivity(), requireContext())

                    if (intent != null) {
                        context?.startActivity(intent)
                    } else {
                        // Handle the case when the intent is null (e.g., an error occurred during download)
                    }
                }      } catch (e: ActivityNotFoundException) {
                e.printStackTrace()

            }
        }
    }

    override fun onStartDownload(position: Int) {
        uploadedAllFileAdapter!!.startDownloading(position)
        uploadedAllFileAdapter!!.notifyItemChanged(position)
    }

    override fun onFinishDownload(position: Int) {
        uploadedAllFileAdapter!!.finishDownloading(position)
        uploadedAllFileAdapter!!.notifyItemChanged(position)
    }

    override fun requestPermission(position: Int, utility: FilePathList?) {
        this.utility = utility
        this.position = position
        val permissionsArray = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
        requestPermissionLauncher!!.launch(permissionsArray)
    }


}


