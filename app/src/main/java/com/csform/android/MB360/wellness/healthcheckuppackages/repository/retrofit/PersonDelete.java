package com.csform.android.MB360.wellness.healthcheckuppackages.repository.retrofit;

import com.csform.android.MB360.wellness.healthcheckuppackages.repository.responseclass.Person;

public interface PersonDelete {
     void personDelete(String packagePosition,String personSrNo, int position, Person person);
}
