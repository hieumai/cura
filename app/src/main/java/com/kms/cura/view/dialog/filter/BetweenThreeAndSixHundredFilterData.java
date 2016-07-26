package com.kms.cura.view.dialog.filter;

import com.kms.cura.R;
import com.kms.cura.entity.user.DoctorUserEntity;

/**
 * Created by duyhnguyen on 8/8/2016.
 */
public class BetweenThreeAndSixHundredFilterData extends FilterData{
    public static int SIX_HUNDRED = 600;

    public BetweenThreeAndSixHundredFilterData() {
        super(R.id.between_301_and_600);
    }

    @Override
    public boolean isDoctorMatched(DoctorUserEntity doctor) {
        return ((doctor.getMinPrice() > BetweenOneAndThreeHundredFilterData.THREE_HUNDRED) && (doctor.getMaxPrice() < SIX_HUNDRED));
    }
}
