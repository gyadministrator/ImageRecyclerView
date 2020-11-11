package com.android.custom.customimagerecyclerview.adapter;

import android.text.TextUtils;
import android.widget.ImageView;

import com.android.custom.customimagerecyclerview.R;
import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

/**
 * Created by ${wlh} on 2017/10/18.
 * 描述:
 */

public class NetPicAdapter extends BaseQuickAdapter<String, BaseViewHolder> {
    private boolean isDelete;

    public NetPicAdapter(boolean isDelete) {
        super(R.layout.item_image);
        this.isDelete = isDelete;
    }

    @Override
    protected void convert(BaseViewHolder helper, String path) {
        if (isDelete) {
            helper.addOnClickListener(R.id.iv_del)
                    .addOnClickListener(R.id.iv_icon);
            if (getData().size() == helper.getLayoutPosition()) {
                helper.setVisible(R.id.iv_del, false).setImageResource(R.id.iv_icon, R.drawable.tianjia);
            } else {
                helper.setVisible(R.id.iv_del, true);
                Glide.with(helper.getView(R.id.iv_icon).getContext()).load(path).into((ImageView) helper.getView(R.id.iv_icon));
            }
        } else {
            helper.setVisible(R.id.iv_del, false);
            if (!TextUtils.isEmpty(path)) {
                Glide.with(helper.getView(R.id.iv_icon).getContext()).load(path).into((ImageView) helper.getView(R.id.iv_icon));
            } else {
                helper.setVisible(R.id.fl_content, false);
            }
        }
    }

    @Override
    public int getItemCount() {
        int itemCount = super.getItemCount();
        if (isDelete) {
            if (itemCount == 6) {
                return 6;
            }
            return itemCount + 1;
        } else {
            return itemCount;
        }
    }

    @Override
    public int getItemViewType(int position) {
        return 0;
    }


    @Override
    public void onBindViewHolder(BaseViewHolder holder, int positions) {
        if (0 == getData().size()) {
            convert(holder, null);
        } else if (holder.getLayoutPosition() == getData().size()) {
            convert(holder, getData().get(holder.getLayoutPosition() - 1));
        } else {
            convert(holder, getData().get(holder.getLayoutPosition()));
        }
    }
}
