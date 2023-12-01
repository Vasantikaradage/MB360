package com.csform.android.MB360.insurance.enrollment.interfaces;

import com.csform.android.MB360.insurance.enrollment.repository.responseclass.TopSumInsuredsValue;
import com.csform.android.MB360.insurance.enrollment.repository.responseclass.topup.Si;

public interface TopUpSelected {
    void OnTopUpSelected(Si topUp, int position);
}
