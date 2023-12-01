package com.csform.android.MB360.wellness;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProvider;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import com.csform.android.MB360.utilities.LogMyBenefits;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.csform.android.MB360.R;
import com.csform.android.MB360.insurance.repository.LoadSessionViewModel;
import com.csform.android.MB360.insurance.repository.responseclass.GroupGMCPolicyEmpDependantsDatum;
import com.csform.android.MB360.insurance.repository.responseclass.LoadSessionResponse;
import com.csform.android.MB360.utilities.LogTags;
import com.csform.android.MB360.utilities.UtilMethods;
import com.csform.android.MB360.wellness.dashboardwellness.repository.DashboardWellnessViewModel;
import com.csform.android.MB360.wellness.healthcheckuppackages.repository.PackagesViewModel;
import com.csform.android.MB360.wellness.healthcheckuppackages.repository.responseclass.HealthCheckupUpdatePaymentRequest;
import com.csform.android.MB360.wellness.healthcheckuppackages.repository.responseclass.VerifyNoRequest;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.razorpay.Checkout;
import com.razorpay.PaymentData;
import com.razorpay.PaymentResultListener;
import com.razorpay.PaymentResultWithDataListener;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class PaymentActivity extends AppCompatActivity implements PaymentResultWithDataListener {

    //Package ViewModel
    PackagesViewModel packagesViewModel;
    LoadSessionViewModel loadSessionViewModel;
    DashboardWellnessViewModel dashboardWellnessViewModel;

    HealthCheckupUpdatePaymentRequest healthCheckupUpdatePaymentRequest = new HealthCheckupUpdatePaymentRequest();

    String youpay = "";
    String familySrNo;
    String groupSrNo;
    String empIdNo;
    String orderReferenceNumber = "";
    String razorPayOrderId = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
        init();

    }

    public void init() {

        initViews();
        initValues();
    }

    public void initViews() {

        //viewModel scoped
        loadSessionViewModel = new ViewModelProvider(this).get(LoadSessionViewModel.class);
        dashboardWellnessViewModel = new ViewModelProvider(this).get(DashboardWellnessViewModel.class);
        packagesViewModel = new ViewModelProvider(this).get(PackagesViewModel.class);
    }

    public void initValues() {

        youpay = getIntent().getExtras().getString("youpay").replace("\u20B9 ", "");
        familySrNo = getIntent().getExtras().getString("familySrNo");
        groupSrNo = getIntent().getExtras().getString("groupSrNo");
        empIdNo = getIntent().getExtras().getString("empIdNo");
        razorPayOrderId = getIntent().getExtras().getString("razor_pay_order_id");
        orderReferenceNumber = getIntent().getExtras().getString("orderReferenceNumber");


        LogMyBenefits.e(LogTags.HEALTH_CHECKUP_ACTIVITY, "youpay : " + youpay);
        LogMyBenefits.e(LogTags.HEALTH_CHECKUP_ACTIVITY, "familySrNo : " + familySrNo);
        LogMyBenefits.e(LogTags.HEALTH_CHECKUP_ACTIVITY, "groupSrNo : " + groupSrNo);
        LogMyBenefits.e(LogTags.HEALTH_CHECKUP_ACTIVITY, "empIdNo : " + empIdNo);


        PaymentNow(Integer.parseInt(youpay));

    }

    public void PaymentNow(int youpay) {

//        final Activity activity = this;

        Checkout checkout = new Checkout();
        checkout.setKeyID("rzp_test_P85Qujlx0rs3IX");
        checkout.setImage(R.mipmap.ic_launcher_round);

//        double finalamount = Float.parseFloat(amount)*100;

        try {
            JSONObject options = new JSONObject();

            options.put("name", "MyBenefits");
            options.put("description", "Payment ");
            options.put("send_sms_hash", true);
            options.put("allow_rotation", true);
            //You can omit the image option to fetch the image from dashboard
            options.put("image", "https://wellness.mybenefits360.com/mybenefits/assets/img/logo-master-small.png");
            options.put("currency", "INR");
            options.put("amount", "" + (100 * youpay));
            options.put("order_id", razorPayOrderId);

            JSONObject preFill = new JSONObject();

            /* TODO change the params */
            LoadSessionResponse loadSessionResponse = loadSessionViewModel.getLoadSessionData().getValue();
            if (loadSessionResponse != null) {
                preFill.put("email", loadSessionResponse.getGroupPoliciesEmployees().get(0).getGroupGMCPolicyEmployeeData().get(0).getOfficialEMailId());

                List<GroupGMCPolicyEmpDependantsDatum> member_list = loadSessionResponse.getGroupPoliciesEmployeesDependants().get(0).getGroupGMCPolicyEmpDependantsData();
                for (GroupGMCPolicyEmpDependantsDatum data : member_list
                ) {
                    if (data.getRelationid().equalsIgnoreCase("17")) {
                        preFill.put("contact", data.getCellphoneNumber());
                    }
                }

                options.put("prefill", preFill);
            }


            checkout.open(this, options);

        } catch (Exception e) {
            LogMyBenefits.e(LogTags.HEALTH_CHECKUP_ACTIVITY, "Error in starting Razorpay Checkout : " + e);
            e.printStackTrace();
        }

    }

    @Override
    public void onPaymentSuccess(String s, PaymentData data) {

        LogMyBenefits.e(LogTags.HEALTH_CHECKUP_ACTIVITY, "onPaymentSuccess: " + s);
        Toast.makeText(this, "Payment Success", Toast.LENGTH_SHORT).show();

        String paymentId = data.getPaymentId();
        String signature = data.getSignature();
        String orderId = data.getOrderId();

        confirmApp(paymentId, signature, orderId);
    }

    @Override
    public void onPaymentError(int i, String s, PaymentData data) {

        LogMyBenefits.e(LogTags.HEALTH_CHECKUP_ACTIVITY, "onPaymentError: " + s);
        Toast.makeText(this, "Payment Failed", Toast.LENGTH_SHORT).show();
        finish();
    }

    private void confirmApp(String paymentId, String signature, String orderId) {

        healthCheckupUpdatePaymentRequest.setEmpIdNo(empIdNo);
        healthCheckupUpdatePaymentRequest.setExtGroupSrNo(groupSrNo);
        healthCheckupUpdatePaymentRequest.setFamilySrNo(familySrNo);
        healthCheckupUpdatePaymentRequest.setPaymentId("" + paymentId);
        healthCheckupUpdatePaymentRequest.setOrderId("" + orderId);
        healthCheckupUpdatePaymentRequest.setSignature("" + signature);
        healthCheckupUpdatePaymentRequest.setOrderReferenceNumber(orderReferenceNumber);

        packagesViewModel.putUpdatePayment(healthCheckupUpdatePaymentRequest).observe((LifecycleOwner) this, messageResponseUpdatePayment -> {

            if (messageResponseUpdatePayment != null) {

                LogMyBenefits.d(LogTags.HEALTH_CHECKUP_ACTIVITY, "Update Payment : " + messageResponseUpdatePayment.toString());
                if (messageResponseUpdatePayment.getMessage().getStatus()) {
                    showPaymentConfirmDialog(orderId, paymentId);
                } else {
                    Toast.makeText(this, "Something went wrong, Please try again later \nReason :- " + messageResponseUpdatePayment.getMessage().getMessage(), Toast.LENGTH_LONG).show();
                }
            }

        });

    }


    private void showPaymentConfirmDialog(String orderId, String paymentId) {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_appt_confirmed);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);

        TextView tvorder_no = dialog.findViewById(R.id.tvordernumber);
        TextView tvpaymentid = dialog.findViewById(R.id.tvpaymentid);
        TextView tvamount = dialog.findViewById(R.id.tvamount);

        tvorder_no.setText(orderId);
        tvpaymentid.setText(paymentId);
        tvamount.setText("\u20B9 " + UtilMethods.PriceFormat(youpay));

        Button btnok = dialog.findViewById(R.id.btn_ok);

        btnok.setOnClickListener(v -> {
            dialog.dismiss();
            finish();
        });
        dialog.show();

    }


}