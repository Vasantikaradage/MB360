package com.csform.android.MB360.wellness.healthcheckuppackages.repository.ui;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.text.Html;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.util.Base64;
import com.csform.android.MB360.utilities.LogMyBenefits;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.core.widget.CompoundButtonCompat;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.RecyclerView;

import com.csform.android.MB360.R;
import com.csform.android.MB360.utilities.LogMyBenefits;
import com.csform.android.MB360.utilities.LogTags;
import com.csform.android.MB360.wellness.fragment.WellnessHomeFragmentDirections;
import com.csform.android.MB360.wellness.healthcheckup.repository.requestclass.DependentRequest;
import com.csform.android.MB360.wellness.healthcheckuppackages.repository.PackagesViewModel;
import com.csform.android.MB360.wellness.healthcheckuppackages.repository.responseclass.Packages;
import com.csform.android.MB360.wellness.healthcheckuppackages.repository.responseclass.Person;
import com.csform.android.MB360.wellness.healthcheckuppackages.repository.responseclass.VerifyNoRequest;
import com.csform.android.MB360.wellness.healthcheckuppackages.repository.retrofit.PackageInterface;
import com.csform.android.MB360.wellness.healthcheckuppackages.repository.retrofit.PersonDelete;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.http.DELETE;

public class PersonAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Context mContext;
    List<Person> personList = new ArrayList<>();
    NavController navController;
    View view;
    PackageInterface packageInterface;
    Packages packages;
    PackagesViewModel packagesViewModel;
    VerifyNoRequest verifyNoRequest = new VerifyNoRequest();
    PersonDelete personDelete;

    public PersonAdapter(Context mContext, List<Person> personList, Packages packages, PackageInterface packageInterface, PersonDelete personDelete) {
        this.personList = personList;
        this.mContext = mContext;
        this.packageInterface = packageInterface;
        this.packages = packages;
        this.personDelete = personDelete;
        packagesViewModel = new ViewModelProvider((ViewModelStoreOwner) mContext).get(PackagesViewModel.class);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_person, parent, false);

        return new PackagesViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {


        try {


            final Person lstPerson = personList.get(holder.getAdapterPosition());

            ((PackagesViewHolder) holder).personName.setText(lstPerson.getPersonName());
//            ((PackagesViewHolder) holder).packagePrice.setText(lstPackages.getPackagePrice());

            ((PackagesViewHolder) holder).setIsRecyclable(false);

            ((PackagesViewHolder) holder).scheduleIV.setVisibility(View.VISIBLE);

            ((PackagesViewHolder) holder).personName.setAlpha(0.5F);

            if (lstPerson.getIsChbChecked()) {
                ((PackagesViewHolder) holder).cbpkgopt.setChecked(true);
              /*  ((PackagesViewHolder) holder).scheduleIV.setVisibility(View.GONE);*/
                ((PackagesViewHolder) holder).cbpkgopt.setButtonTintList(ColorStateList.valueOf(ContextCompat.getColor(mContext, R.color.light_grey)));
            }

            if (!lstPerson.getIsDisabled()) {

                ((PackagesViewHolder) holder).scheduleIV.setVisibility(View.GONE);
                ((PackagesViewHolder) holder).cbpkgopt.setVisibility(View.VISIBLE);

                ((PackagesViewHolder) holder).deleteIV.setVisibility(View.VISIBLE);
                ((PackagesViewHolder) holder).personName.setAlpha(1);

                if (lstPerson.isSelected()) {

                    ((PackagesViewHolder) holder).cbpkgopt.setChecked(true);

                    ((PackagesViewHolder) holder).llscheduleapp.setVisibility(View.VISIBLE);

                    ((PackagesViewHolder) holder).llscheduleapp.setOnClickListener(view1 -> {

                        if (personList.get(holder.getAdapterPosition()).getIsMobEmailConf() == 1) {

                            if (((PackagesViewHolder) holder).llscheduleapp.getVisibility() == View.VISIBLE) {
                                packageInterface.scheduleAppointment(packages, lstPerson);
                            } else {
                                ((PackagesViewHolder) holder).llscheduleapp.setVisibility(View.VISIBLE);
                            }
                        } else {

                            final BottomSheetDialog dialog = new BottomSheetDialog(mContext);
                            dialog.setContentView(R.layout.dialog_verify_mobile_no);
                            dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
                            dialog.setCanceledOnTouchOutside(false);
                            ImageView iv_close = dialog.findViewById(R.id.iv_close);
                            EditText etmobile = dialog.findViewById(R.id.etmobile);
                            CheckBox cbtermsofuse = dialog.findViewById(R.id.cktermsofuse);
                            TextView btnVerifyMobNo = dialog.findViewById(R.id.btnVerifyMobNo);
                            iv_close.setOnClickListener(v -> dialog.cancel());
                            btnVerifyMobNo.setBackgroundColor(ContextCompat.getColor(mContext, R.color.greenlightbg2));

                            String html = " I agree to <a href=\"http://www.google.com\">Terms of Use</a>";
                            Spanned result;
                            result = Html.fromHtml(html, Html.FROM_HTML_MODE_LEGACY);
                            cbtermsofuse.setText(result);
                            cbtermsofuse.setMovementMethod(LinkMovementMethod.getInstance());

                            cbtermsofuse.setButtonTintList(ColorStateList.valueOf(mContext.getResources().getColor(R.color.grey1)));//setButtonTintList is accessible directly on API>19

                            cbtermsofuse.setOnCheckedChangeListener((buttonView, isChecked) -> {
                                if (isChecked) {
                                    if (Build.VERSION.SDK_INT < 21)
                                        CompoundButtonCompat.setButtonTintList(cbtermsofuse, ColorStateList.valueOf(mContext.getResources().getColor(R.color.greenlightbg2)));//Use android.support.v4.widget.CompoundButtonCompat when necessary else
                                    else
                                        cbtermsofuse.setButtonTintList(ColorStateList.valueOf(mContext.getResources().getColor(R.color.greenlightbg2)));//setButtonTintList is accessible directly on API>19
                                } else {
                                    if (Build.VERSION.SDK_INT < 21)
                                        CompoundButtonCompat.setButtonTintList(cbtermsofuse, ColorStateList.valueOf(mContext.getResources().getColor(R.color.grey1)));//Use android.support.v4.widget.CompoundButtonCompat when necessary else
                                    else
                                        cbtermsofuse.setButtonTintList(ColorStateList.valueOf(mContext.getResources().getColor(R.color.grey1)));//setButtonTintList is accessible directly on API>19
                                }
                            });

                            btnVerifyMobNo.setOnClickListener(v -> {
                                if (etmobile.getText().length() != 10) {
                                    Toast.makeText(mContext, "Please insert correct mobile number", Toast.LENGTH_SHORT).show();
                                } else if (!cbtermsofuse.isChecked()) {
                                    cbtermsofuse.setButtonTintList(ColorStateList.valueOf(ContextCompat.getColor(mContext, R.color.bg_row_background)));//setButtonTintList is accessible directly on API>19
                                } else {
                                    verifymobile("" + personList.get(holder.getAdapterPosition()).getPersonSRNo(),
                                            "" + etmobile.getText().toString().trim(), dialog);
                                    packageInterface.scheduleAppointment(packages, lstPerson);
                                    dialog.cancel();
                                }
                            });

                            dialog.show();

                        }
                    });

                } else {
                    ((PackagesViewHolder) holder).llscheduleapp.setVisibility(View.GONE);
                    ((PackagesViewHolder) holder).cbpkgopt.setChecked(false);
                }
            }

            ((PackagesViewHolder) holder).cbpkgopt.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    setSelected(holder.getAdapterPosition(), b);
                }
            });

            ((PackagesViewHolder) holder).deleteIV.setOnClickListener(view -> {
                personDelete.personDelete(packages.getPackageSrNo(), "" + lstPerson.getPersonSRNo(), holder.getAdapterPosition(), lstPerson);
            });


        } catch (Exception e) {
            LogMyBenefits.e(LogTags.HEALTH_CHECKUP_PACKAGE_ACTIVITY, "Error ", e);
        }

    }

    @Override
    public int getItemCount() {
        if (personList != null) {
            return personList.size();
        } else {
            return 0;
        }
    }


    public void personRemoved(int position, Person person) {
        //here we inform the adapter that person is removed.
        try {
            LogMyBenefits.d(LogTags.HEALTH_CHECKUP_ACTIVITY, "onPersonRemoved: @" + position);
            personList.remove(person);
            notifyItemRemoved(position - 1);
        } catch (ArrayIndexOutOfBoundsException e) {
            LogMyBenefits.e(LogTags.HEALTH_CHECKUP_ACTIVITY, "onPersonRemoved: @" + position, e);
        }


    }

    public static class PackagesViewHolder extends RecyclerView.ViewHolder {

        TextView personName;
        ImageView scheduleIV, deleteIV;
        View v1;
        RelativeLayout person_item;
        LinearLayout llscheduleapp;
        CheckBox cbpkgopt;

        public PackagesViewHolder(@NonNull View itemView) {
            super(itemView);

            personName = itemView.findViewById(R.id.txtName);
            scheduleIV = itemView.findViewById(R.id.ivappschedule);
            deleteIV = itemView.findViewById(R.id.iv_delete);
            person_item = itemView.findViewById(R.id.person_item);
            llscheduleapp = itemView.findViewById(R.id.llscheduleapp);
            cbpkgopt = itemView.findViewById(R.id.cbpkgopt);

            v1 = itemView.findViewById(R.id.v1);

        }
    }

    public void verifymobile(String personSrNo, String mobileNo, BottomSheetDialog dialog) {

        verifyNoRequest.setPersonSrNo("" + personSrNo);
        verifyNoRequest.setEmailId("");
        verifyNoRequest.setMobileNumber("" + mobileNo);

        packagesViewModel.verifyNo(verifyNoRequest).observe((LifecycleOwner) mContext, messageResponseVerifyNo -> {
            LogMyBenefits.d(LogTags.HEALTH_CHECKUP_ACTIVITY, "VerifyNo: " + messageResponseVerifyNo.toString());

        });

//        packageInterface.scheduleAppointment(packages, (Person) personList);

    }

    private void setSelected(int pos, boolean check) {
//        data.getData().get(this.pos).getServicesName().get(pos).setSelected(check);
        personList.get(pos).setSelected(check);
        notifyDataSetChanged();
    }


}
