# ImageRecyclerView
Android 上传图片的RecyclerView

废话不多说，先上图！！！！！：

![image](https://github.com/gyadministrator/ImageRecyclerView/blob/master/images/spot.jpg)

工程的Gradle引入方式：

    repositories {
            google()
            jcenter()
            mavenCentral()
        }

    allprojects {
        repositories {
            google()
            jcenter()
            maven { url 'https://jitpack.io' }
            mavenCentral()
        }
    }

  dependencies {
		    implementation 'com.github.gyadministrator.ImageRecyclerView:customimagerecyclerview:2.4'
	}


	如何用：

	package com.android.custom.imagerecyclerview;

    import android.content.Intent;
    import android.os.Bundle;
    import android.view.View;
    import android.widget.TextView;
    import android.widget.Toast;

    import androidx.appcompat.app.AppCompatActivity;

    import com.android.custom.customimagerecyclerview.constant.Constants;
    import com.android.custom.customimagerecyclerview.view.ImageRecyclerView;
    import com.lzy.imagepicker.ImagePicker;
    import com.lzy.imagepicker.bean.ImageItem;
    import com.lzy.imagepicker.ui.ImageGridActivity;

    import java.util.ArrayList;

    public class MainActivity extends AppCompatActivity implements View.OnClickListener {
        private static final int IMAGE_PICKER = 1001;
        private TextView tvCamera;
        private TextView tvAlbum;
        private TextView tvResult;
        private ImageRecyclerView imageRecyclerView;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);
            initView();
        }

        private void initView() {
            tvCamera = findViewById(R.id.tv_camera);
            tvAlbum = findViewById(R.id.tv_album);
            tvResult = findViewById(R.id.tv_result);
            imageRecyclerView = findViewById(R.id.recycler);
            tvCamera.setOnClickListener(this);
            tvAlbum.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.tv_camera:
                    takePhoto();
                    break;
                case R.id.tv_album:
                    selectAlbum();
                    break;
            }
        }

        private void selectAlbum() {
            Intent intent = new Intent(this, ImageGridActivity.class);
            startActivityForResult(intent, IMAGE_PICKER);
        }

        private void takePhoto() {
            Intent intent = new Intent(this, ImageGridActivity.class);
            intent.putExtra(ImageGridActivity.EXTRAS_TAKE_PICKERS, true); // 是否是直接打开相机
            startActivityForResult(intent, IMAGE_PICKER);
        }

        @Override
        protected void onActivityResult(int requestCode, int resultCode, Intent data) {
            super.onActivityResult(requestCode, resultCode, data);
            if (resultCode == ImagePicker.RESULT_CODE_ITEMS) {
                if (data != null && requestCode == IMAGE_PICKER) {
                    ArrayList<ImageItem> images = (ArrayList<ImageItem>) data.getSerializableExtra(ImagePicker.EXTRA_RESULT_ITEMS);
                    showImages(images);
                } else if (data != null && requestCode == Constants.REQUEST_CODE) {
                    imageRecyclerView.onActivityResult(requestCode, resultCode, data);
                } else {
                    Toast.makeText(this, "没有数据", Toast.LENGTH_SHORT).show();
                }
            }
        }

        private void showImages(ArrayList<ImageItem> images) {
            StringBuilder stringBuilder = new StringBuilder();
            if (images != null && images.size() > 0) {
                for (ImageItem imageItem : images) {
                    if (imageItem != null) {
                        String path = imageItem.path;
                        stringBuilder.append(path + "  ");
                    }
                }
            }
            tvResult.setText(stringBuilder.toString());
        }
    }



