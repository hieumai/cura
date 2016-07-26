package com.kms.cura.view.dialog.filter;

import com.kms.cura.R;
import com.kms.cura.entity.OpeningHour;
import com.kms.cura.entity.WorkingHourEntity;
import com.kms.cura.entity.user.DoctorUserEntity;

import java.util.Calendar;

/**
 * Created by nguyend25 on 07-Aug-16.
 */
public class AfterSixFilterData extends FilterData {
    public static int SIX_PM = 18;

    public AfterSixFilterData() {
        super(R.id.afterSix);
    }

    @Override
    public boolean isDoctorMatched(DoctorUserEntity doctor) {
        for (WorkingHourEntity hour : doctor.getWorkingTime()) {
            for (OpeningHour openingHour : hour.getWorkingTime()) {

                if (openingHour.getOpenTime().getHours() > SIX_PM || openingHour.getCloseTime().getHours() > SIX_PM) {
                    return true;
                }
            }
        }
        return false;
    }
}

