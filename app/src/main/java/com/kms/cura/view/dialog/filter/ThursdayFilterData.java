package com.kms.cura.view.dialog.filter;

import com.kms.cura.R;
import com.kms.cura.entity.DayOfTheWeek;
import com.kms.cura.entity.user.DoctorUserEntity;

/**
 * Created by duyhnguyen on 8/5/2016.
 */
public class ThursdayFilterData extends FilterData {

    public ThursdayFilterData() {
        super(R.id.thursday);
    }

    @Override
    public boolean isDoctorMatched(DoctorUserEntity doctor) {
        return doctor.workOn(DayOfTheWeek.THURSDAY);
    }
}
