package com.tharsol.endtb.ui.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.text.Editable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.tharsol.endtb.R;

import carbon.widget.LinearLayout;

public class RoundedFieldView extends LinearLayout
{
    private LinearLayout mLinearLayoutContainer;
    private final EditText mEditText;
    private TextView mTextViewPrefix;
    private String mPrefix;

    public RoundedFieldView(@NonNull Context context)
    {
        super(context);
        mEditText = new EditText(context);
        initializeView(null, context);
    }

    public RoundedFieldView(@NonNull Context context, @Nullable AttributeSet attrs)
    {
        super(context, null);
        mEditText = new EditText(context, attrs);
        initializeView(attrs, context);
    }

    public RoundedFieldView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr)
    {
        super(context, null, defStyleAttr);
        mEditText = new EditText(context, attrs, defStyleAttr);
        initializeView(attrs, context);
    }

    private void initializeView(AttributeSet attrs, Context context)
    {
        setOrientation(HORIZONTAL);
        int valueInPixels16 = (int) getResources().getDimension(R.dimen.dimen_size_16dp);
        int valueInPixels4 = (int) getResources().getDimension(R.dimen.dimen_size_4dp);
        setPaddingRelative(valueInPixels16, valueInPixels4, valueInPixels16, (int) getResources().getDimension(R.dimen.dimen_size_16dp));
        LayoutParams layoutRoot = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        setLayoutParams(layoutRoot);
        setId(mEditText.getId());
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.RoundedFieldView);
        int editId = a.getInt(R.styleable.RoundedFieldView_editId, 0);
        mPrefix = a.getString(R.styleable.RoundedFieldView_inputPrefix);
        int dimensionPixelSize = a.getDimensionPixelSize(R.styleable.RoundedFieldView_drawablePadding, 0);
        float dimensionElevation = a.getDimensionPixelSize(R.styleable.RoundedFieldView_elevation, 4);
        a.recycle();
        mEditText.setId(editId);

        mLinearLayoutContainer = new LinearLayout(context);

        LayoutParams layoutContainer = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int) getResources().getDimension(R.dimen.dimen_size_48dp));
        layoutContainer.gravity = Gravity.CENTER_VERTICAL;


        int valueInPixels24 = (int) getResources().getDimension(R.dimen.dimen_size_24dp);
        //int elevation = (int) getResources().getDimension(R.dimen.dimen_size_8dp);
        System.out.println("ELEVATION VALUE: " + dimensionElevation);
        mLinearLayoutContainer.setBackgroundColor(Color.parseColor("#ffffff"));
        mLinearLayoutContainer.setElevation(dimensionElevation);
        mLinearLayoutContainer.setCornerRadius(valueInPixels24);
        mLinearLayoutContainer.setElevationShadowColor(Color.RED);
        mLinearLayoutContainer.setOrientation(HORIZONTAL);
        mLinearLayoutContainer.setLayoutParams(layoutContainer);
        LayoutParams layout3 = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        layout3.gravity = Gravity.CENTER_VERTICAL;
        mEditText.setLayoutParams(layout3);

        mTextViewPrefix = new TextView(context);
        mTextViewPrefix.setTypeface(mEditText.getTypeface());
        mTextViewPrefix.setTextColor(mEditText.getTextColors());
        mTextViewPrefix.setTextSize(TypedValue.COMPLEX_UNIT_PX, mEditText.getTextSize());

        LayoutParams layoutPrefix = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutPrefix.gravity = Gravity.CENTER_VERTICAL;
        if (!TextUtils.isEmpty(mPrefix))
        {
            layoutPrefix.leftMargin = 20;
            mEditText.setCompoundDrawablePadding(dimensionPixelSize);
        }
        mTextViewPrefix.setLayoutParams(layoutPrefix);
        mTextViewPrefix.setText(mPrefix);
        mLinearLayoutContainer.addView(mTextViewPrefix);
        mLinearLayoutContainer.addView(mEditText);
        addView(mLinearLayoutContainer);
    }

    public String getPrefix()
    {
        return mPrefix;
    }

    public void setPrefix(String prefix)
    {
        mTextViewPrefix.setText(mPrefix);
        mPrefix = prefix;
    }

    public void setError(CharSequence error)
    {
        mEditText.setError(error);
    }

    public Editable getText()
    {
        return mEditText.getText();
    }

    public void setText(CharSequence text)
    {
        mEditText.setText(text);
    }

    public void setHint(CharSequence text)
    {
        mEditText.setHint(text);
    }

    public EditText getEditor()
    {
        return mEditText;
    }
}
