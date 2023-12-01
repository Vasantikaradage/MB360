package com.csform.android.MB360.wellness.healthcheckup;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.csform.android.MB360.utilities.LogMyBenefits;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.csform.android.MB360.R;
import com.csform.android.MB360.databinding.FragmentSummaryBinding;
import com.csform.android.MB360.insurance.DashBoardActivity;
import com.csform.android.MB360.insurance.repository.LoadSessionViewModel;
import com.csform.android.MB360.utilities.LoadingWellnessDialogue;
import com.csform.android.MB360.utilities.LogTags;
import com.csform.android.MB360.utilities.UtilMethods;
import com.csform.android.MB360.wellness.PaymentActivity;
import com.csform.android.MB360.wellness.dashboardwellness.repository.DashboardWellnessViewModel;
import com.csform.android.MB360.wellness.healthcheckuppackages.repository.PackagesViewModel;
import com.csform.android.MB360.wellness.healthcheckuppackages.repository.requestclass.ScheduleRequest;
import com.csform.android.MB360.wellness.healthcheckuppackages.repository.ui.DiagnosticCenterAdapter;
import com.csform.android.MB360.wellness.healthcheckuppackages.repository.ui.SummaryPersonAdapter;

import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

import org.json.JSONObject;


public class SummaryFragment extends Fragment {

    FragmentSummaryBinding binding;
    View view;
    NavController navController;

    //PackageLT ViewModel
    PackagesViewModel packagesViewModel;
    LoadSessionViewModel loadSessionViewModel;
    DashboardWellnessViewModel dashboardWellnessViewModel;

    //Adapter
    SummaryPersonAdapter adapter;

    String familySrNo;
    String groupSrNo;
    String empIdNo;

    String groupCode;

    LoadingWellnessDialogue loadingWellnessDialogue;

    public SummaryFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentSummaryBinding.inflate(inflater, container, false);
        view = binding.getRoot();

        //loader
        loadingWellnessDialogue = new LoadingWellnessDialogue(requireContext(), requireActivity());

        //To Navigate
        NavHostFragment navHostFragment = (NavHostFragment) requireActivity().getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment_wellness);

        navController = navHostFragment.getNavController();

        //viewModel scoped in the fragment.
        loadSessionViewModel = new ViewModelProvider(requireActivity()).get(LoadSessionViewModel.class);
        dashboardWellnessViewModel = new ViewModelProvider(requireActivity()).get(DashboardWellnessViewModel.class);
        packagesViewModel = new ViewModelProvider(requireActivity()).get(PackagesViewModel.class);


        binding.btnconfirmnow.setOnClickListener(view -> {

            ConfirmNow("" + binding.tvYoupaidAmount.getText().toString());

        });

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getSummary();


        packagesViewModel.getPaymentLoadingState().observe(getViewLifecycleOwner(), loading -> {
            if (loading) {
                loadingWellnessDialogue.showLoading("");
            } else {
                loadingWellnessDialogue.hideLoading();
            }
        });
    }


    private void getSummary() {

        loadSessionViewModel.getLoadSessionData().observe(getViewLifecycleOwner(), loadSessionResponse -> {

            empIdNo = loadSessionResponse.getGroupPoliciesEmployees().get(0).getGroupGMCPolicyEmployeeData().get(0).getEmployeeIdentificationNo();

            dashboardWellnessViewModel.getEmployeeWellnessDetailsData().observe(getViewLifecycleOwner(), employeeCheckResponse -> {

                familySrNo = employeeCheckResponse.getExtFamilySrNo();
                groupSrNo = employeeCheckResponse.getExtGroupSrNo();
                groupCode = loadSessionResponse.getGroupInfoData().getGroupcode();

                packagesViewModel.getSummary(familySrNo, groupCode);
            });
        });


        packagesViewModel.getSummaryData().observe(getViewLifecycleOwner(), summaryResponse -> {


            if (summaryResponse != null) {
                if (summaryResponse.getMessage() != null) {
                    if (!summaryResponse.getMessage().getStatus()) {

                        binding.cartLL.setVisibility(View.GONE);
                        binding.noHistoryImage.setVisibility(View.VISIBLE);

                    } else {

                        binding.cartLL.setVisibility(View.VISIBLE);
                        binding.noHistoryImage.setVisibility(View.GONE);
                    }
                }


                if (summaryResponse.getSummary().getCompanySponseredPerson().isEmpty()) {
                    binding.llcompanysponsored.setVisibility(View.GONE);
                } else if (summaryResponse.getSummary().getTotal().equals("0")) {
                    binding.llselfsponsored.setVisibility(View.GONE);
                }

                adapter = new SummaryPersonAdapter(requireActivity(), summaryResponse.getSummary().getSelfSponseredPerson());
                binding.rvpaidmembers.setAdapter(adapter);
                adapter.notifyItemRangeChanged(0, summaryResponse.getSummary().getSelfSponseredPerson().size());

                if (!summaryResponse.getMessage().getStatus()) {
                    binding.messageTextView.setVisibility(View.VISIBLE);
                    binding.messageTextView.setText("" + summaryResponse.getMessage().getMessage());
                }

                binding.tvtotal.setText("\u20B9 " + UtilMethods.PriceFormat(summaryResponse.getSummary().getTotal()));
                binding.tvPaidAmount.setText("\u20B9 " + UtilMethods.PriceFormat(summaryResponse.getSummary().getPaid()));
                binding.tvYoupaidAmount.setText("\u20B9 " + UtilMethods.PriceFormat(summaryResponse.getSummary().getYoupay()));

                if (summaryResponse.getSummary().getTotal().equals("0")) {
                    binding.btnconfirmnow.setVisibility(View.GONE);
                } else {
                    //nothing
                }

                if (!summaryResponse.getSummary().getShowConfirmButton()) {
                    binding.btnconfirmnow.setVisibility(View.GONE);
                } else {
                    binding.btnconfirmnow.setVisibility(View.VISIBLE);
                }


                try {
                    if (!summaryResponse.getSummary().getYoupay().isEmpty()) {
                        int you_pay = Integer.parseInt(summaryResponse.getSummary().getYoupay());
                        if (you_pay <= 0) {
                            binding.btnconfirmnow.setVisibility(View.GONE);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }


            } else {
                binding.messageTextView.setVisibility(View.VISIBLE);
                binding.messageTextView.setText("Sorry, data not available");
            }


        });
    }


    private void ConfirmNow(String youpay) {

        final Dialog dialog = new Dialog(requireActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_appt_confirmation);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        int width = (int) (getResources().getDisplayMetrics().widthPixels * 0.90);
        int height = (int) (getResources().getDisplayMetrics().heightPixels * 0.90);

        dialog.getWindow().setLayout(width, height);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);

        TextView tvconfirmtext = dialog.findViewById(R.id.tvconfirmtext);
        TextView tvconfirmsubtext = dialog.findViewById(R.id.tvconfirmsubtext);

        ImageView iv_close = dialog.findViewById(R.id.iv_close);

        Button btnyes = dialog.findViewById(R.id.btnyes);
        Button btnno = dialog.findViewById(R.id.btnno);

        if (youpay.equals("0")) {
            tvconfirmtext.setText("Do you wish to confirm you appointment?");
            tvconfirmsubtext.setVisibility(View.GONE);
        } else
            tvconfirmtext.setText("You will be redirected to our secure payment gateway in order to process your payment of  " + youpay + " and schedule your Health Checkups. Do you wish to continue?");

        btnyes.setOnClickListener(view -> {
            fetchPaymentDetails(youpay);


            dialog.dismiss();

        });

        btnno.setOnClickListener(view -> dialog.dismiss());

        iv_close.setOnClickListener(view -> dialog.dismiss());

        dialog.show();
    }

    private void fetchPaymentDetails(String payment) {

        String extFamilySrNo = "" + dashboardWellnessViewModel.getEmployeeWellnessDetailsData().getValue().getExtFamilySrNo();
        String extGroupChild = "" + dashboardWellnessViewModel.getEmployeeWellnessDetailsData().getValue().getExtGroupSrNo();
        String youPay = payment.replace(",", "").replace("\u20B9 ", "");
        String employeeId = "" + loadSessionViewModel.getLoadSessionData().getValue().getGroupPoliciesEmployees().get(0).getGroupGMCPolicyEmployeeData().get(0).getEmployeeIdentificationNo();
        String groupCode = "" + loadSessionViewModel.getLoadSessionData().getValue().getGroupInfoData().getGroupcode();
        packagesViewModel.fetchPaymentDetails(extFamilySrNo, extGroupChild, employeeId, youPay, groupCode)
                .observe(getViewLifecycleOwner(), paymentInfo -> {
                    if (paymentInfo != null) {
                        if (paymentInfo.getMessage().getStatus()) {
                            if (paymentInfo.getPaymentDetails().getGoToPayment()) {
                                Intent paymentIntent = new Intent(getActivity(), PaymentActivity.class);
                                paymentIntent.putExtra("youpay", "" + payment.replace(",", "").replace("\u20B9 ", ""));
                                paymentIntent.putExtra("familySrNo", "" + familySrNo);
                                paymentIntent.putExtra("groupSrNo", "" + groupSrNo);
                                paymentIntent.putExtra("empIdNo", "" + empIdNo);
                                paymentIntent.putExtra("orderReferenceNumber", "" + paymentInfo.getPaymentDetails().getOrderReferenceNumber());
                                paymentIntent.putExtra("razor_pay_order_id", "" + paymentInfo.getPaymentDetails().getRazorPayOrderId());
                                startActivity(paymentIntent);

                            } else {
                                Toast.makeText(requireContext(), "Unfortunately you cant pay in-app, We know its not ideal.", Toast.LENGTH_LONG).show();
                            }

                        } else {
                            //something went wrong
                            Toast.makeText(requireContext(), "Unable to fetch Payment Details, please try again later", Toast.LENGTH_SHORT).show();
                        }

                    } else {
                        //error
                        //Something went wrong
                        /*Toast.makeText(requireContext(), "Something went wrong", Toast.LENGTH_LONG).show();*/
                    }
                });

    }


    @Override
    public void onResume() {
        super.onResume();
        packagesViewModel.getSummary(familySrNo, groupCode);
    }
}