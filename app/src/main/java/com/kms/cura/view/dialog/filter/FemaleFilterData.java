package com.kms.cura.view.dialog.filter;

import com.kms.cura.R;
import com.kms.cura.entity.user.DoctorUserEntity;

/**
 * Created by duyhnguyen on 8/5/2016.
 */
public class FemaleFilterData extends FilterData {

    public FemaleFilterData() {
        super(R.id.female);
    }

    @Override
    public boolean isDoctorMatched(DoctorUserEntity doctor) {
        return !doctor.getGender().equals(DoctorUserEntity.GENDER_MALE);
    }
}
