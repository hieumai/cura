package com.kms.cura.view.dialog.filter;

import com.kms.cura.R;
import com.kms.cura.entity.DayOfTheWeek;
import com.kms.cura.entity.user.DoctorUserEntity;

/**
 * Created by duyhnguyen on 8/5/2016.
 */
public class WednesdayFilterData extends FilterData {

    public WednesdayFilterData() {
        super(R.id.wednesday);
    }

    @Override
    public boolean isDoctorMatched(DoctorUserEntity doctor) {
        return doctor.workOn(DayOfTheWeek.WEDNESDAY);
    }
}
