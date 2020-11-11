package com.android.custom.customimagerecyclerview.view;

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;

/**
 * @ProjectName: ImageRecyclerView
 * @Package: com.android.custom.customimagerecyclerview.view
 * @ClassName: CustomImageView
 * @Author: 1984629668@qq.com
 * @CreateDate: 2020/11/11 15:59
 */
public class CustomImageView extends AppCompatImageView {
    public CustomImageView(@NonNull Context context) {
        super(context);
    }

    public CustomImageView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomImageView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, widthMeasureSpec);
    }
}
