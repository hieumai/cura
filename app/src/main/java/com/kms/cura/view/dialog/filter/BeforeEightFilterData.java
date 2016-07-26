package com.kms.cura.view.dialog.filter;

import com.kms.cura.R;
import com.kms.cura.entity.OpeningHour;
import com.kms.cura.entity.WorkingHourEntity;
import com.kms.cura.entity.user.DoctorUserEntity;

/**
 * Created by nguyend25 on 07-Aug-16.
 */
public class BeforeEightFilterData extends FilterData {
    public static int EIGHT_AM = 8;

    public BeforeEightFilterData() {
        super(R.id.beforeEight);
    }

    @Override
    public boolean isDoctorMatched(DoctorUserEntity doctor) {
        for (WorkingHourEntity hour : doctor.getWorkingTime()) {
            for (OpeningHour openingHour : hour.getWorkingTime()) {

                if (openingHour.getOpenTime().getHours() < EIGHT_AM) {
                    return true;
                }
            }
        }
        return false;
    }
}
