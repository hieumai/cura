package com.kms.cura.view.dialog.filter;

import com.kms.cura.R;
import com.kms.cura.entity.user.DoctorUserEntity;

/**
 * Created by duyhnguyen on 8/8/2016.
 */
public class LessThanOneHundredFilterData extends FilterData {
    public static int ONE_HUNDRED = 100;

    public LessThanOneHundredFilterData() {
        super(R.id.less_than_100);
    }

    @Override
    public boolean isDoctorMatched(DoctorUserEntity doctor) {
        return (doctor.getMinPrice() < ONE_HUNDRED);
    }
}