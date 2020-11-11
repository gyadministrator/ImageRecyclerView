package com.android.custom.customimagerecyclerview.view;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.os.Build;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.custom.customimagerecyclerview.R;
import com.android.custom.customimagerecyclerview.adapter.NetPicAdapter;
import com.android.custom.customimagerecyclerview.constant.Constants;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.bean.ImageItem;
import com.lzy.imagepicker.loader.PicassoImageLoader;
import com.lzy.imagepicker.ui.ImageGridActivity;
import com.lzy.imagepicker.view.CropImageView;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.OnCancelListener;
import com.orhanobut.dialogplus.OnDismissListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @ProjectName: Potential_Safety
 * @Package: com.gzinfo.wisdomsafe.view
 * @ClassName: ImageRecyclerView
 * @Author: 1984629668@qq.com
 * @CreateDate: 2020/9/28 8:04
 */
public class ImageRecyclerView extends RecyclerView {
    private NetPicAdapter mAdapter;
    private Activity mActivity;
    private ArrayList<String> mSelected = new ArrayList<>();
    private boolean isDelete;
    private ImagePicker imagePicker;
    private boolean isShowLocal = false;

    public ImageRecyclerView(@NonNull Context context) {
        this(context, null);
    }

    public ImageRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ImageRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ImageRecyclerView);
        isDelete = typedArray.getBoolean(R.styleable.ImageRecyclerView_isDelete, true);
        typedArray.recycle();
        mActivity = (Activity) context;
        initPicker();
        initRecycler();
    }

    private void initPicker() {
        if (imagePicker == null) {
            ImagePicker imagePicker = ImagePicker.getInstance();
            imagePicker.setImageLoader(new PicassoImageLoader());   //设置图片加载器
            imagePicker.setShowCamera(true);  //显示拍照按钮
            imagePicker.setCrop(true);        //允许裁剪（单选才有效）
            imagePicker.setSaveRectangle(true); //是否按矩形区域保存
            imagePicker.setSelectLimit(9);    //选中数量限制
            imagePicker.setStyle(CropImageView.Style.RECTANGLE);  //裁剪框的形状
            imagePicker.setFocusWidth(800);   //裁剪框的宽度。单位像素（圆形自动取宽高最小值）
            imagePicker.setFocusHeight(800);  //裁剪框的高度。单位像素（圆形自动取宽高最小值）
            imagePicker.setOutPutX(1000);//保存文件的宽度。单位像素
            imagePicker.setOutPutY(1000);//保存文件的高度。单位像素
        }
    }

    public void setImagePicker(ImagePicker imagePicker) {
        this.imagePicker = imagePicker;
    }

    public void setDelete(boolean delete) {
        isDelete = delete;
    }

    public String getImageUrl() {
        StringBuilder stringBuilder = new StringBuilder();
        if (mSelected != null && mSelected.size() > 0) {
            for (int i = 0; i < mSelected.size(); i++) {
                String s = mSelected.get(i);
                if (!TextUtils.isEmpty(s)) {
                    stringBuilder.append(s);
                    if (i != mSelected.size() - 1) {
                        stringBuilder.append(";");
                    }
                }
            }
        }
        return stringBuilder.toString();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        ArrayList<ImageItem> images = (ArrayList<ImageItem>) data.getSerializableExtra(ImagePicker.EXTRA_RESULT_ITEMS);
        if (images != null && images.size() > 0) {
            for (ImageItem imageItem : images) {
                if (imageItem != null) {
                    String path = imageItem.path;
                    mSelected.add(path);
                }
            }
        }
        if (uploadListener != null) {
            uploadListener.uploadFilesListener(mSelected);
        }
        if (isShowLocal) {
            initRecycler();
        }
    }

    public void setShowLocal(boolean showLocal) {
        isShowLocal = showLocal;
    }

    public void setImages(ArrayList<String> list) {
        if (!isShowLocal) {
            mSelected.clear();
            mSelected.addAll(list);
            initRecycler();
        }
    }

    private void initRecycler() {
        mAdapter = new NetPicAdapter(isDelete);
        this.setOverScrollMode(OVER_SCROLL_NEVER);
        this.setLayoutManager(new GridLayoutManager(mActivity, 4));
        this.setAdapter(mAdapter);
        mAdapter.setNewData(mSelected);
        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                if (mSelected == null || mSelected.size() == 0) return;
                if (itemClickListener != null) {
                    itemClickListener.onItemClick(mSelected);
                }
            }
        });
        mAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                int id = view.getId();
                if (id == R.id.iv_icon) {//点击照片
                    if (position == adapter.getData().size()) {//添加图片
                        hintKeyBoard();
                        showPhotoDialog(mActivity, new OnCancelListener() {
                            @Override
                            public void onCancel(DialogPlus dialog) {

                            }
                        }, new OnDismissListener() {
                            @Override
                            public void onDismiss(DialogPlus dialog) {

                            }
                        });
                    }
                } else if (id == R.id.iv_del) {//点击删除
                    adapter.getData().remove(position);
                    adapter.notifyItemRemoved(position);
                }
            }
        });
    }

    /**
     * 隐藏软键盘
     */
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void hintKeyBoard() {
        //拿到InputMethodManager
        InputMethodManager imm = (InputMethodManager) mActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
        //如果window上view获取焦点 && view不为空
        if (imm.isActive() && mActivity.getCurrentFocus() != null) {
            //拿到view的token 不为空
            if (Objects.requireNonNull(mActivity.getCurrentFocus()).getWindowToken() != null) {
                //表示软键盘窗口总是隐藏，除非开始时以SHOW_FORCED显示。
                imm.hideSoftInputFromWindow(Objects.requireNonNull(mActivity.getCurrentFocus()).getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }
    }

    /**
     * 展示图片选择 dialog
     */
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void showPhotoDialog(Activity activity, OnCancelListener onCancelListener, OnDismissListener onDismissListener) {
        hintKeyBoard();
        @SuppressLint("InflateParams") View view = LayoutInflater.from(mActivity).inflate(R.layout.dialog_photo_select, null);
        final DialogPlus dialogPlus = DialogPlus.newDialog(activity)
                .setContentHolder(new com.orhanobut.dialogplus.ViewHolder(view))
                .setGravity(Gravity.BOTTOM)
                .setOnCancelListener(onCancelListener)
                .setOnDismissListener(onDismissListener)
                .create();
        view.findViewById(R.id.dialog_cancle).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogPlus.dismiss();
            }
        });

        view.findViewById(R.id.dialog_takephoto).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogPlus.dismiss();
                takePhoto(mActivity);
            }
        });
        view.findViewById(R.id.dialog_takeselect).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogPlus.dismiss();
                selectAlbum(mActivity);
            }
        });
        dialogPlus.show();
    }

    private void selectAlbum(Activity activity) {
        Intent intent = new Intent(activity, ImageGridActivity.class);
        activity.startActivityForResult(intent, Constants.REQUEST_CODE);
    }

    private void takePhoto(Activity activity) {
        Intent intent = new Intent(activity, ImageGridActivity.class);
        intent.putExtra(ImageGridActivity.EXTRAS_TAKE_PICKERS, true); // 是否是直接打开相机
        activity.startActivityForResult(intent, Constants.REQUEST_CODE);
    }

    private OnUploadListener uploadListener;

    public void setUploadListener(OnUploadListener uploadListener) {
        this.uploadListener = uploadListener;
    }

    public interface OnUploadListener {
        void uploadFilesListener(List<String> list);

        void uploadFileListener(String path);
    }

    private OnItemClickListener itemClickListener;

    public interface OnItemClickListener {
        void onItemClick(ArrayList<String> mSelected);
    }
}
