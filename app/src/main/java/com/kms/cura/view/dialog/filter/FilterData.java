package com.kms.cura.view.dialog.filter;

import android.widget.LinearLayout;

import com.kms.cura.entity.user.DoctorUserEntity;

/**
 * Created by duyhnguyen on 8/5/2016.
 */
public abstract class FilterData {
    private boolean boo;
    private int id;

    public FilterData(int id) {
        this.boo = false;
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean getBoo() {
        return boo;
    }

    public void setBoo(boolean boo) {
        this.boo = boo;
    }

    public abstract boolean isDoctorMatched(DoctorUserEntity doctor);

}
