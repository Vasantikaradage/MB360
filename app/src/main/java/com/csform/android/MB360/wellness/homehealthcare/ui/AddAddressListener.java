package com.csform.android.MB360.wellness.homehealthcare.ui;

import com.csform.android.MB360.wellness.homehealthcare.responseclass.FamilyMember;

public interface AddAddressListener {
    void getMember(FamilyMember familyMember);

    void selectPackage(FamilyMember familyMember);

    void selectMember(FamilyMember familyMember,int position);
}
