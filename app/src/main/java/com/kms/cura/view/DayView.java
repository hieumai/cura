package com.kms.cura.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;
import android.widget.TextView;

import com.kms.cura.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by linhtnvo on 7/20/2016.
 */

public class DayView extends TextView {
    private Date date;
    private boolean onDraw = false;
    private List<DayDecorator> decorators;
    private boolean onSelected;
    private Paint mCirclePaint;
    private int mColor;
    private static final int SELECTED_CIRCLE_ALPHA = 60;
    private static String PRIMARY_COLOR = "#009688";

    public DayView(Context context) {
        this(context, null, 0);
    }

    public DayView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
        onDraw = false;
        init();
    }

    public DayView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        onDraw = false;
        init();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.CUPCAKE && isInEditMode()) {
            return;
        }
    }

    private void init() {
        mCirclePaint = new Paint();
        mCirclePaint.setFakeBoldText(true);
        mCirclePaint.setAntiAlias(true);
        mCirclePaint.setTextAlign(Paint.Align.CENTER);
        mCirclePaint.setStyle(Paint.Style.STROKE);
        mCirclePaint.setStrokeWidth(3);
        mCirclePaint.setAlpha(SELECTED_CIRCLE_ALPHA);
    }

    public void bind(Date date, List<DayDecorator> decorators) {
        this.date = date;
        this.decorators = decorators;

        final SimpleDateFormat df = new SimpleDateFormat("d");
        int day = Integer.parseInt(df.format(date));
        setText(String.valueOf(day));
    }

    public void decorate() {
        //Set custom decorators
        if (null != decorators) {
            for (DayDecorator decorator : decorators) {
                decorator.decorate(this);
            }
        }
    }

    public void setmColor(int mColor) {
        this.mColor = mColor;
    }

    public void setOnDraw(boolean onDraw) {
        this.onDraw = onDraw;
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (!onDraw && !onSelected){
            return;
        }
        if (onSelected){
            colorTheSelectedDay(canvas);
            return;
        }
        colorTheCurrentDay(canvas);
    }

    private void colorTheCurrentDay(Canvas canvas){
        mCirclePaint.setColor(mColor);
        mCirclePaint.setStyle(Paint.Style.STROKE);
        int width = getWidth();
        int height = getHeight();
        int radius = Math.min(width, height) / 2;
        canvas.drawCircle(width / 2, height / 2, radius - 3, mCirclePaint);
    }

    private void colorTheSelectedDay(Canvas canvas){
        mCirclePaint.setStyle(Paint.Style.FILL);
        mCirclePaint.setColor(Color.WHITE);
        int width = getWidth();
        int height = getHeight();
        int radius = Math.min(width, height) / 2;
        canvas.drawCircle(width / 2, height / 2, radius, mCirclePaint);
        mCirclePaint.setColor(Color.parseColor(PRIMARY_COLOR));
        mCirclePaint.setTextSize(super.getTextSize());
        String text = super.getText().toString();
        int textInt = Integer.parseInt(text);
        if (textInt ==  10 || (textInt > 11 && textInt < 20)){
            canvas.drawText(super.getText().toString(), width / 2 - 3.5f , (height / 2 - ((mCirclePaint.descent() + mCirclePaint.ascent()) / 2)), mCirclePaint);
            return;
        }
        canvas.drawText(super.getText().toString(), width / 2 , (height / 2 - ((mCirclePaint.descent() + mCirclePaint.ascent()) / 2)), mCirclePaint);
    }

    public void setOnSelected(boolean onSelected) {
        this.onSelected = onSelected;
    }

    public void decorate(DayDecorator decorator) {
        decorator.decorate(this);
    }

    public Date getDate() {
        return date;
    }

    public boolean isOnDraw() {
        return onDraw;
    }

}