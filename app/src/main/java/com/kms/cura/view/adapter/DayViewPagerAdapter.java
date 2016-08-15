package com.kms.cura.view.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;

import com.kms.cura.view.DoctorApptDayVIew;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by linhtnvo on 8/3/2016.
 */
public class DayViewPagerAdapter extends PagerAdapter {
    // This holds all the currently displayable views, in order from left to right.
    private List<View> views = new ArrayList<View>();
    private List<DoctorApptDayVIew> dayViewList = new ArrayList<>();
    private Context mContext;

    public DayViewPagerAdapter(Context mContext, List<DoctorApptDayVIew> views) {
        this.mContext = mContext;
        this.dayViewList.addAll(views);
        for (DoctorApptDayVIew dayView : dayViewList) {
            this.views.add(dayView.createView());
        }
    }

    //-----------------------------------------------------------------------------
    // Used by ViewPager.  "Object" represents the page; tell the ViewPager where the
    // page should be displayed, from left-to-right.  If the page no longer exists,
    // return POSITION_NONE.
    @Override
    public int getItemPosition(Object object) {
        int index = views.indexOf(object);
        if (index == -1)
            return POSITION_NONE;
        else
            return index;
    }

    //-----------------------------------------------------------------------------
    // Used by ViewPager.  Called when ViewPager needs a page to display; it is our job
    // to add the page to the container, which is normally the ViewPager itself.  Since
    // all our pages are persistent, we simply retrieve it from our "views" ArrayList.
    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View v = views.get(position);
        if (v.getParent() != null) {
            ((ViewGroup) v.getParent()).removeView(v);
        }
        container.addView(v);
        return v;
    }

    //-----------------------------------------------------------------------------
    // Used by ViewPager.  Called when ViewPager no longer needs a page to display; it
    // is our job to remove the page from the container, which is normally the
    // ViewPager itself.  Since all our pages are persistent, we do nothing to the
    // contents of our "views" ArrayList.
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(views.get(position));
    }

    //-----------------------------------------------------------------------------
    // Used by ViewPager; can be used by app as well.
    // Returns the total number of pages that the ViewPage can display.  This must
    // never be 0.
    @Override
    public int getCount() {
        return views.size();
    }

    //-----------------------------------------------------------------------------
    // Used by ViewPager.
    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    //-----------------------------------------------------------------------------
    // Add "view" to right end of "views".
    // Returns the position of the new view.
    // The app should call this to add pages; not used by ViewPager.
    public int addView(DoctorApptDayVIew v) {
        return addView(v, views.size());
    }

    //-----------------------------------------------------------------------------
    // Add "view" at "position" to "views".
    // Returns position of new view.
    // The app should call this to add pages; not used by ViewPager.
    public int addView(DoctorApptDayVIew v, int position) {
        dayViewList.add(position, v);
        views.add(position, v.createView());
        return position;
    }

    public void addView(List<DoctorApptDayVIew> viewList) {
        this.dayViewList.addAll(viewList);
        for (DoctorApptDayVIew view : viewList) {
            views.add(view.createView());
        }
    }

    //-----------------------------------------------------------------------------
    // Removes "view" from "views".
    // Retuns position of removed view.
    // The app should call this to remove pages; not used by ViewPager.
    public int removeView(ViewPager pager, View v) {
        return removeView(pager, views.indexOf(v));
    }

    //-----------------------------------------------------------------------------
    // Removes the "view" at "position" from "views".
    // Retuns position of removed view.
    // The app should call this to remove pages; not used by ViewPager.
    public int removeView(ViewPager pager, int position) {
        // ViewPager doesn't have a delete method; the closest is to set the adapter
        // again.  When doing so, it deletes all its views.  Then we can delete the view
        // from from the adapter and finally set the adapter to the pager again.  Note
        // that we set the adapter to null before removing the view from "views" - that's
        // because while ViewPager deletes all its views, it will call destroyItem which
        // will in turn cause a null pointer ref.
        pager.setAdapter(null);
        dayViewList.remove(position);
        views.remove(position);
        pager.setAdapter(this);

        return position;
    }


    //-----------------------------------------------------------------------------
    // Returns the "view" at "position".
    // The app should call this to retrieve a view; not used by ViewPager.
    public View getView(int position) {
        return views.get(position);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return dayViewList.get(position).getTitle();
    }

    public void clear() {
        this.dayViewList.clear();
        this.views.clear();
    }

}
