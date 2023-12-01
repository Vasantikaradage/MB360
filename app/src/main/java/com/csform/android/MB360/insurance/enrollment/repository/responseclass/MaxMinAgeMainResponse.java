package com.csform.android.MB360.insurance.enrollment.repository.responseclass;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;


import com.csform.android.MB360.database.converters.MaxMinAgeConverter;
import com.csform.android.MB360.database.converters.ServiceNamesConverter;

import java.util.ArrayList;
import java.util.List;
@Entity(tableName = "AGE", indices = @Index(value = {"oeGrpBasInfoSrNo"}, unique = true))
@TypeConverters(MaxMinAgeConverter.class)
public class MaxMinAgeMainResponse {
    private List<MaxMinAgeResponse> maxMinAgeResponseList = new ArrayList<>();
    @PrimaryKey(autoGenerate = false)
    @NonNull
    private String oeGrpBasInfoSrNo;

    public List<MaxMinAgeResponse> getMaxMinAgeResponseList() {
        return maxMinAgeResponseList;
    }

    public void setMaxMinAgeResponseList(List<MaxMinAgeResponse> maxMinAgeResponseList) {
        this.maxMinAgeResponseList = maxMinAgeResponseList;
    }

    @NonNull
    public String getOeGrpBasInfoSrNo() {
        return oeGrpBasInfoSrNo;
    }

    public void setOeGrpBasInfoSrNo(@NonNull String oeGrpBasInfoSrNo) {
        this.oeGrpBasInfoSrNo = oeGrpBasInfoSrNo;
    }

    @Override
    public String toString() {
        return "MaxMinAgeMainResponse{" +
                "maxMinAgeResponseList=" + maxMinAgeResponseList +
                ", oeGrpBasInfoSrNo='" + oeGrpBasInfoSrNo + '\'' +
                '}';
    }
}
