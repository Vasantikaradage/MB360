package com.csform.android.MB360.wellness.healthcheckuppackages.repository.cancellationHC;

import com.csform.android.MB360.wellness.healthcheckuppackages.repository.responseclass.AppointmentHealthCheckup;

public interface OnCancellationHealthCheckup {
    void cancelAppointmentHC(int position, AppointmentHealthCheckup healthCheckupPackage);
}
