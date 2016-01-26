package com.example.mortar.drawer.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.mortar.drawer.core.ObjectGraphService;
import com.example.mortar.drawer.screen.SlideShowScreen;

import javax.inject.Inject;

/**
 * Created by Lubenets Vladyslav on 1/26/16.
 */
public class SlideShowView extends LinearLayout {
    @Inject
    SlideShowScreen.Presenter presenter;

    public SlideShowView(Context context) {
        super(context);
        ObjectGraphService.inject(context, this);
    }

    public SlideShowView(Context context, AttributeSet attrs) {
        super(context, attrs);
        ObjectGraphService.inject(context, this);
    }

    public SlideShowView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        ObjectGraphService.inject(context, this);
    }

    public SlideShowView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        ObjectGraphService.inject(context, this);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        presenter.takeView(this);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        presenter.dropView(this);
    }


    public void showContent() {
        TextView textView = new TextView(getContext());
        setGravity(Gravity.CENTER);
        textView.setText("Slide show view has been loaded");
        addView(textView);
    }

}
