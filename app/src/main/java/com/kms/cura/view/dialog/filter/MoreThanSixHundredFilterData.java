package com.kms.cura.view.dialog.filter;

import com.kms.cura.R;
import com.kms.cura.entity.user.DoctorUserEntity;

/**
 * Created by duyhnguyen on 8/8/2016.
 */
public class MoreThanSixHundredFilterData extends FilterData {

    public MoreThanSixHundredFilterData() {
        super(R.id.more_than_600);
    }

    @Override
    public boolean isDoctorMatched(DoctorUserEntity doctor) {
        return (doctor.getMinPrice() > BetweenThreeAndSixHundredFilterData.SIX_HUNDRED);
    }
}
