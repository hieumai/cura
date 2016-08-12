package com.kms.cura.view.dialog.filter;

import com.kms.cura.R;
import com.kms.cura.entity.user.DoctorUserEntity;

/**
 * Created by duyhnguyen on 8/8/2016.
 */
public class BetweenOneAndThreeHundredFilterData extends FilterData {
    public static int THREE_HUNDRED = 300;

    public BetweenOneAndThreeHundredFilterData() {
        super(R.id.between_100_and_300);
    }

    @Override
    public boolean isDoctorMatched(DoctorUserEntity doctor) {
        return ((doctor.getMinPrice() > LessThanOneHundredFilterData.ONE_HUNDRED) && (doctor.getMaxPrice() < THREE_HUNDRED));
    }
}

