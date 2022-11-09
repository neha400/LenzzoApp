package com.lenzzo.customviews;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.EditText;

import com.lenzzo.api.API;

public class CustomEditTextMedium extends EditText {

    public CustomEditTextMedium(Context context) {
        super(context);
        applyCustomFont(context);
    }

    public CustomEditTextMedium(Context context, AttributeSet attrs) {
        super(context, attrs);
        applyCustomFont(context);
    }

    public CustomEditTextMedium(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        applyCustomFont(context);
    }

    private void applyCustomFont(Context context) {
        Typeface customFont = FontCache.getTypeface(API.FONT_MEDIUM, context);
        setTypeface(customFont);
    }
}
