package com.csform.android.MB360.wellness.healthcheckuppackages.repository.retrofit;

import com.csform.android.MB360.wellness.healthcheckuppackages.repository.responseclass.Packages;
import com.csform.android.MB360.wellness.healthcheckuppackages.repository.responseclass.Person;

public interface PackageInterface {

    void getPackageDetails(Packages packages);

    void scheduleAppointment(Packages packages, Person person);

//    delete function

//    void deletePerson(String personSrNo);

}
