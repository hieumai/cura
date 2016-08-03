package com.kms.cura.view;

import java.util.Date;

/**
 * Created by linhtnvo on 7/20/2016.
 */
public interface CalendarListener {
    void onDateSelected(Date date);

    void onMonthChanged(Date time);
}
