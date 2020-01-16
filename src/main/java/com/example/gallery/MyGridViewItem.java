package com.example.gallery;

/**
 * Created by hasee-pc on 2017/07/06.
 */
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import static android.view.View.getDefaultSize;

/**
 * Created by hasee-pc on 2017/07/06.
 */

public class MyGridViewItem extends LinearLayout {

    public MyGridViewItem(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    public MyGridViewItem(Context context) {
        super(context);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        setMeasuredDimension(getDefaultSize(0, widthMeasureSpec),getDefaultSize(0, heightMeasureSpec));

        // childWidthSize是自定义布局的宽
        int childWidthSize = getMeasuredWidth();
        int childHeightSize = getMeasuredHeight();

        // 高度和宽度一样；最后面的是RelativeLayout的宽
        heightMeasureSpec = widthMeasureSpec = View.MeasureSpec.makeMeasureSpec(childWidthSize, View.MeasureSpec.EXACTLY);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

}