package com.kms.cura.view.dialog.filter;

import com.kms.cura.R;
import com.kms.cura.entity.OpeningHour;
import com.kms.cura.entity.WorkingHourEntity;
import com.kms.cura.entity.user.DoctorUserEntity;

/**
 * Created by nguyend25 on 07-Aug-16.
 */
public class EightToSixFilterData extends FilterData {

    public EightToSixFilterData() {
        super(R.id.eightToSix);
    }

    @Override
    public boolean isDoctorMatched(DoctorUserEntity doctor) {
        for (WorkingHourEntity hour : doctor.getWorkingTime()) {
            for (OpeningHour openingHour : hour.getWorkingTime()) {

                if (openingHour.getOpenTime().getHours() > BeforeEightFilterData.EIGHT_AM && openingHour.getCloseTime().getHours() < AfterSixFilterData.SIX_PM) {
                    return true;
                }
            }
        }
        return false;
    }
}
