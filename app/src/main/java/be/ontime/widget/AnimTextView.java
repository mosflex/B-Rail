package be.ontime.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.TextView;

import java.lang.ref.WeakReference;

import cn.dreamtobe.percentsmoothhandler.ISmoothTarget;
import cn.dreamtobe.percentsmoothhandler.SmoothHandler;

/**
 * Created by Jawad on 31-05-16.
 */
public class AnimTextView extends TextView implements ISmoothTarget {
    private SmoothHandler smoothHandler;

    public AnimTextView(Context context) {
        super(context);
    }

    public AnimTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AnimTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public AnimTextView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    private int progress;
    private int max = 100;

    public void setProgress(final int progress) {
        if (smoothHandler != null) {
            smoothHandler.commitPercent(progress / (float) getMax());
        }

        this.progress = progress;
        if(progress >= 60){
            setText(String.valueOf(((int)progress)/60) + " min.");
        }else {
            setText(String.valueOf(((int)progress)) + " sec.");
        }
    }

    public int getProgress() {
        return this.progress;
    }

    public int getMax() {
        return max;
    }

    public void setMax(int max) {
        this.max = max;
    }

    @Override
    public float getPercent() {
        return getProgress() / (float) getMax();
    }

    @Override
    public void setPercent(float percent) {
        setProgress((int) Math.ceil(percent * getMax()));
    }

    @Override
    public void setSmoothPercent(float percent) {
        getSmoothHandler().loopSmooth(percent);
    }

    @Override
    public void setSmoothPercent(float percent, long durationMillis) {
        getSmoothHandler().loopSmooth(percent, durationMillis);
    }

    private SmoothHandler getSmoothHandler() {
        if (smoothHandler == null) {
            smoothHandler = new SmoothHandler(new WeakReference<ISmoothTarget>(this));
        }
        return smoothHandler;
    }
}