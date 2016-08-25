package com.kms.cura.view;

import com.kms.cura.entity.OpeningHour;

/**
 * Created by toanbnguyen on 8/18/2016.
 */
public interface ListViewRemoveItemListener {
    void removeItem(int position);
    void removeItem(String facility, OpeningHour openingHour);
}
