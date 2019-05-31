package com.tuochebang.user.ui;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore.Images.Media;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;
import com.framework.app.component.utils.ActivityUtil;
import com.tuochebang.user.R;
import com.tuochebang.user.adapter.SelectedDataAdapter;
import com.tuochebang.user.base.BaseActivity;
import com.tuochebang.user.cache.FileUtil;
import com.tuochebang.user.util.ImageUtil;
import com.tuochebang.user.util.PictureUtil;
import com.tuochebang.user.widget.wxphotoselector.WxPhotoSelectorActivity;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SelectPhotoActivity extends BaseActivity {
    private static final int DEFALUT_ASPECT_X = 1;
    private static final int DEFALUT_ASPECT_Y = 1;
    private static final int DEFALUT_CROP_HEIGHT = 270;
    private static final int DEFALUT_CROP_WIDTH = 270;
    private static final int DEFALUT_PHOTO_NUMS = 4;
    private static final int DEFALUT_SELECT_TYPE = 0;
    public static final String EXTRA_DIALOG_TITLE = "dialog_title";
    public static final String EXTRA_PHOTO_ASPECT_X = "photo_aspect_x";
    public static final String EXTRA_PHOTO_ASPECT_Y = "photo_aspect_y";
    public static final String EXTRA_PHOTO_CROP_HEIGHT = "photo_crop_height";
    public static final String EXTRA_PHOTO_CROP_WIDTH = "photo_crop_width";
    public static final String EXTRA_PHOTO_NUMS = "photo_nums";
    public static final String EXTRA_PHOTO_SELECT_TYPE = "photo_select_type";
    public static final String FLAG_IMAGE_PATH = "flag_image_path";
    private static final String IMAGE_DEFAULT_NAME = "default.png";
    private static final int REQ_RESULT_PHOTO_CAPTURE = 500;
    private static final int REQ_RESULT_PHOTO_CROP = 700;
    private static final int REQ_RESULT_WXPHOTO = 100;
    private static final int WRITE_EXTERNAL_STORAGE_REQUEST_CODE = 20;
    private String IMAGE_DEFAULE_PATH = "/base//image/";
    private ArrayList<String> imageList;
    private Uri mCameraImageUri;
    private String mDialogTitle;
    private ListView mOptionsListView;
    private int mPhotoAspectX;
    private int mPhotoAspectY;
    private int mPhotoNums;
    private int mPhotoOutputX;
    private int mPhotoOutputY;
    private int mPhotoSelectType;
    private int mPosition;
    private TextView mTvTitle;

    /* renamed from: com.tuochebang.user.ui.SelectPhotoActivity$1 */
    class C09411 implements OnItemClickListener {
        C09411() {
        }

        public void onItemClick(AdapterView<?> adapterView, View view, int position, long arg3) {
            SelectPhotoActivity.this.mPosition = position;
            if (position == 0) {
                SelectPhotoActivity.this.openCamera();
            } else if (SelectPhotoActivity.this.mPhotoSelectType == 0) {
                SelectPhotoActivity.this.openMediaStore();
            } else {
                SelectPhotoActivity.this.openWxPhotoSelect();
            }
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_photo);
        getExtras();
        initViews();
    }

    private void getExtras() {
        this.mDialogTitle = getIntent().getStringExtra(EXTRA_DIALOG_TITLE);
        this.mPhotoOutputX = getIntent().getIntExtra(EXTRA_PHOTO_CROP_WIDTH, 270);
        this.mPhotoOutputY = getIntent().getIntExtra(EXTRA_PHOTO_CROP_HEIGHT, 270);
        this.mPhotoAspectX = getIntent().getIntExtra(EXTRA_PHOTO_ASPECT_X, 1);
        this.mPhotoAspectY = getIntent().getIntExtra(EXTRA_PHOTO_ASPECT_Y, 1);
        this.mPhotoSelectType = getIntent().getIntExtra(EXTRA_PHOTO_SELECT_TYPE, 0);
        this.mPhotoNums = getIntent().getIntExtra(EXTRA_PHOTO_NUMS, 4);
    }

    private void initViews() {
        this.imageList = new ArrayList();
        this.mTvTitle = (TextView) findViewById(R.id.tv_message);
        if (this.mDialogTitle != null) {
            this.mTvTitle.setText(this.mDialogTitle);
        }
        this.mOptionsListView = (ListView) findViewById(R.id.lv_options);
        this.mOptionsListView.setOnItemClickListener(new C09411());
        SelectedDataAdapter selectedDataAdapter = new SelectedDataAdapter(this.mContext);
        List<String> stringList = new ArrayList();
        stringList.add(getString(R.string.txt_take_info));
        stringList.add(getString(R.string.txt_select_from_album));
        selectedDataAdapter.setList(stringList);
        this.mOptionsListView.setAdapter(selectedDataAdapter);
    }

    public void onBgClick(View v) {
        finish();
    }

    private void openCamera() {
        try {
            if (Environment.getExternalStorageState().equals("mounted")) {
                try {
                    this.mCameraImageUri = Uri.fromFile(FileUtil.createCacheFile(this.IMAGE_DEFAULE_PATH, System.currentTimeMillis() + IMAGE_DEFAULT_NAME));
                    Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
                    intent.putExtra("output", this.mCameraImageUri);
                    intent.putExtra("return-data", true);
                    startActivityForResult(intent, 500);
                } catch (IOException e) {
                    showNoticeMsg("不能创建临时图片文件");
                    e.printStackTrace();
                }
            }
        } catch (Exception e2) {
            showNoticeMsg("没有合适的相机应用程序");
        }
    }

    private void openMediaStore() {
        if (Environment.getExternalStorageState().equals("mounted")) {
            try {
                this.mCameraImageUri = Uri.fromFile(FileUtil.createSDFile(this.IMAGE_DEFAULE_PATH, System.currentTimeMillis() + IMAGE_DEFAULT_NAME));
                Intent intent = new Intent("android.intent.action.PICK", Media.EXTERNAL_CONTENT_URI);
                intent.putExtra("noFaceDetection", false);
                setupCropIntent(intent);
                startActivityForResult(intent, REQ_RESULT_PHOTO_CROP);
            } catch (IOException e) {
                showNoticeMsg("不能创建临时图片文件");
                e.printStackTrace();
            }
        }
    }

    private void openWxPhotoSelect() {
        Bundle bundle = new Bundle();
        bundle.putInt(WxPhotoSelectorActivity.EXTRA_SELECT_LIMITED_COUNT, this.mPhotoNums);
        ActivityUtil.next(this, WxPhotoSelectorActivity.class, bundle, false, 100);
    }

    private void setupCropIntent(Intent intent) {
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", this.mPhotoAspectX);
        intent.putExtra("aspectY", this.mPhotoAspectY);
        intent.putExtra("outputX", this.mPhotoOutputX);
        intent.putExtra("outputY", this.mPhotoOutputY);
        intent.putExtra("scale", true);
        intent.putExtra("return-data", false);
        intent.putExtra("output", this.mCameraImageUri);
        intent.putExtra("outputFormat", CompressFormat.JPEG.toString());
    }

    private void cropImageUriByTakePhoto() {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(this.mCameraImageUri, "image/*");
        setupCropIntent(intent);
        startActivityForResult(intent, REQ_RESULT_PHOTO_CROP);
    }

    public static Bitmap toturn(Bitmap img) {
        Matrix matrix = new Matrix();
        matrix.postRotate(90.0f);
        return Bitmap.createBitmap(img, 0, 0, img.getWidth(), img.getHeight(), matrix, true);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == -1) {
            switch (requestCode) {
                case 100:
                    this.imageList = data.getStringArrayListExtra(WxPhotoSelectorActivity.EXTRA_RETURN_IMAGES);
                    Intent it = new Intent();
                    it.putStringArrayListExtra(WxPhotoSelectorActivity.EXTRA_RETURN_IMAGES, this.imageList);
                    setResult(-1, it);
                    finish();
                    return;
                case 500:
                    cropImageUriByTakePhoto();
                    return;
                case REQ_RESULT_PHOTO_CROP /*700*/:
                    if (this.mCameraImageUri != null) {
                        String path = this.mCameraImageUri.getPath();
                        Intent intent = new Intent();
                        if (this.mPhotoSelectType != 0) {
                            this.imageList.add(path);
                            intent.putStringArrayListExtra(WxPhotoSelectorActivity.EXTRA_RETURN_IMAGES, this.imageList);
                        } else {
                            checkPhoto();
                            intent.putExtra(FLAG_IMAGE_PATH, this.mCameraImageUri.getPath());
                        }
                        setResult(-1, intent);
                    } else {
                        showNoticeMsg("不能获取到图片");
                    }
                    finish();
                    return;
                default:
                    return;
            }
        }
        finish();
    }

    private void checkPhoto() {
        int degree = PictureUtil.readPictureDegree(this.mCameraImageUri.getPath());
        if (degree != 0) {
            try {
                ImageUtil.saveBitmap(this.mCameraImageUri.getPath(), PictureUtil.rotateBitmap(BitmapFactory.decodeFile(this.mCameraImageUri.getPath()), degree));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
