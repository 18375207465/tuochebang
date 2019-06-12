package com.tuochebang.user.ui.user;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.alibaba.sdk.android.oss.ClientException;
import com.alibaba.sdk.android.oss.ServiceException;
import com.alibaba.sdk.android.oss.callback.OSSCompletedCallback;
import com.alibaba.sdk.android.oss.callback.OSSProgressCallback;
import com.alibaba.sdk.android.oss.internal.OSSAsyncTask;
import com.alibaba.sdk.android.oss.model.PutObjectRequest;
import com.alibaba.sdk.android.oss.model.PutObjectResult;
import com.framework.app.component.utils.BroadCastUtil;
import com.framework.app.component.utils.ImageLoaderUtil;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.tuochebang.user.R;
import com.tuochebang.user.app.MyApplication;
import com.tuochebang.user.base.BaseActivity;
import com.tuochebang.user.cache.FileUtil;
import com.tuochebang.user.constant.AppConstant;
import com.tuochebang.user.constant.AppConstant.BroadCastAction;
import com.tuochebang.user.request.base.ServerUrl;
import com.tuochebang.user.request.entity.LoginInfo;
import com.tuochebang.user.request.entity.UserInfo;
import com.tuochebang.user.request.task.EditUserRequest;
import com.tuochebang.user.ui.CommonEditActivity;
import com.tuochebang.user.ui.SelectItemActivity;
import com.tuochebang.user.view.CommonSelectDailog;
import com.tuochebang.user.view.CommonSelectDailog.OnItemClickedListener;
import com.yanzhenjie.nohttp.NoHttp;
import com.yanzhenjie.nohttp.RequestMethod;
import com.yanzhenjie.nohttp.rest.OnResponseListener;
import com.yanzhenjie.nohttp.rest.RequestQueue;
import com.yanzhenjie.nohttp.rest.Response;
import de.hdodenhof.circleimageview.CircleImageView;
import org.json.JSONException;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class UserInfoActivity extends BaseActivity {
    private String[] accountType = new String[]{"保险公司", "4s店", "维修厂"};
    private List<String> mAccountList;
    private String mHeaderUrl;
    CircleImageView mImgHeader;
    private RelativeLayout mRlAccount;
    private RelativeLayout mRlCompanyName;
    private RelativeLayout mRlHeader;
    private RelativeLayout mRlNickName;
    private RelativeLayout mRlPhone;
    private CommonSelectDailog mShowSelectDialog;
    private Toolbar mToolBar;
    private TextView mTxtAccount;
    private TextView mTxtCompanyFlag;
    private TextView mTxtCompanyName;
    private TextView mTxtNickName;
    private TextView mTxtPhone;
    private TextView mTxtSave;
    private UserInfo mUserInfo;

    /* renamed from: com.tuochebang.user.ui.user.UserInfoActivity$1 */
    class C10091 implements OnClickListener {
        C10091() {
        }

        public void onClick(View v) {
            UserInfoActivity.this.finish();
        }
    }

    /* renamed from: com.tuochebang.user.ui.user.UserInfoActivity$2 */
    class C10102 implements OnItemClickedListener {
        C10102() {
        }

        public void onItemClick(int id, String data) {
            if (id == 0) {
                PictureSelector.create(UserInfoActivity.this).openGallery(PictureMimeType.ofImage()).theme(R.style.picture_default_style).maxSelectNum(1).minSelectNum(1).imageSpanCount(4).selectionMode(1).previewImage(true).isCamera(true).enableCrop(true).compress(true).forResult(PictureConfig.CHOOSE_REQUEST);
            } else {
                PictureSelector.create(UserInfoActivity.this).openCamera(PictureMimeType.ofImage()).theme(R.style.picture_default_style).maxSelectNum(1).minSelectNum(1).selectionMode(1).previewImage(true).enableCrop(true).compress(true).forResult(PictureConfig.CHOOSE_REQUEST);
            }
            UserInfoActivity.this.mShowSelectDialog.dismiss();
        }
    }

    /* renamed from: com.tuochebang.user.ui.user.UserInfoActivity$3 */
    class C10113 implements OnClickListener {
        C10113() {
        }

        public void onClick(View v) {
            UserInfoActivity.this.onBtnUploadHeader();
        }
    }

    /* renamed from: com.tuochebang.user.ui.user.UserInfoActivity$4 */
    class C10124 implements OnClickListener {
        C10124() {
        }

        public void onClick(View v) {
            Bundle bundle = new Bundle();
            bundle.putString(CommonEditActivity.EXTRAS_INPUT_TYPE, "text");
            bundle.putString(CommonEditActivity.EXTRAS_TITLE, "昵称");
            bundle.putString(CommonEditActivity.EXTRAS_TYPE, "nickName");
            Intent intent = new Intent();
            intent.setClass(UserInfoActivity.this, CommonEditActivity.class);
            intent.putExtras(bundle);
            UserInfoActivity.this.startActivityForResult(intent, 100);
        }
    }

    /* renamed from: com.tuochebang.user.ui.user.UserInfoActivity$5 */
    class C10135 implements OnClickListener {
        C10135() {
        }

        public void onClick(View v) {
            Bundle bundle = new Bundle();
            bundle.putString(CommonEditActivity.EXTRAS_INPUT_TYPE, "text");
            bundle.putString(CommonEditActivity.EXTRAS_TITLE, "公司名称");
            bundle.putString(CommonEditActivity.EXTRAS_TYPE, "companyName");
            Intent intent = new Intent();
            intent.setClass(UserInfoActivity.this, CommonEditActivity.class);
            intent.putExtras(bundle);
            UserInfoActivity.this.startActivityForResult(intent, 100);
        }
    }

    /* renamed from: com.tuochebang.user.ui.user.UserInfoActivity$6 */
    class C10146 implements OnClickListener {
        C10146() {
        }

        public void onClick(View v) {
        }
    }

    /* renamed from: com.tuochebang.user.ui.user.UserInfoActivity$7 */
    class C10157 implements OnClickListener {
        C10157() {
        }

        public void onClick(View v) {
        }
    }

    /* renamed from: com.tuochebang.user.ui.user.UserInfoActivity$8 */
    class C10168 implements OnClickListener {
        C10168() {
        }

        public void onClick(View v) {
            UserInfoActivity.this.httpUpdateUserInfo();
        }
    }

    /* renamed from: com.tuochebang.user.ui.user.UserInfoActivity$9 */
    class C10179 implements OSSProgressCallback<PutObjectRequest> {
        C10179() {
        }

        public void onProgress(PutObjectRequest request, long currentSize, long totalSize) {
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);
        initToolBar();
        initView();
        initListener();
        initData();
    }

    private void initData() {
        this.mAccountList = Arrays.asList(this.accountType);
        this.mUserInfo = (UserInfo) FileUtil.readFile(this.mContext, AppConstant.FLAG_USER_INFO);
        if (this.mUserInfo != null) {
            if (!TextUtils.isEmpty(this.mUserInfo.getNickName())) {
                this.mTxtNickName.setText(this.mUserInfo.getNickName());
            }
            if (TextUtils.isEmpty(this.mUserInfo.getCorporate())) {
                this.mTxtCompanyFlag.setVisibility(View.VISIBLE);
            } else {
                this.mTxtCompanyName.setText(this.mUserInfo.getCorporate());
            }
            if (!TextUtils.isEmpty(this.mUserInfo.getUserType())) {
                this.mTxtAccount.setText(this.mUserInfo.getUserType());
            }
            if (!TextUtils.isEmpty(this.mUserInfo.getMobile())) {
                this.mTxtPhone.setText(this.mUserInfo.getMobile());
            }
            if (!TextUtils.isEmpty(this.mUserInfo.getPicture())) {
                this.mHeaderUrl = this.mUserInfo.getPicture();
                ImageLoader.getInstance().displayImage(this.mUserInfo.getPicture(), this.mImgHeader);
            }
        }
    }

    private void initToolBar() {
        this.mToolBar = (Toolbar) findViewById(R.id.toolbar);
        this.mToolBar.setNavigationOnClickListener(new C10091());
    }

    private void onBtnUploadHeader() {
        if (this.mShowSelectDialog == null) {
            List<String> list = new ArrayList();
            list.add(getString(R.string.txt_select_from_album));
            list.add(getString(R.string.txt_camera));
            this.mShowSelectDialog = new CommonSelectDailog(this.mContext, list);
            this.mShowSelectDialog.setMessage(getString(R.string.txt_upload_message));
            this.mShowSelectDialog.setOnItemClickedListener(new C10102());
            return;
        }
        this.mShowSelectDialog.show();
    }

    private void initListener() {
        this.mRlHeader.setOnClickListener(new C10113());
        this.mRlNickName.setOnClickListener(new C10124());
        this.mRlCompanyName.setOnClickListener(new C10135());
        this.mRlPhone.setOnClickListener(new C10146());
        this.mRlAccount.setOnClickListener(new C10157());
        this.mTxtSave.setOnClickListener(new C10168());
    }

    private void initView() {
        mImgHeader = (CircleImageView) findViewById(R.id.tcb_user_header_img);
        this.mRlHeader = (RelativeLayout) findViewById(R.id.tcb_user_avater_rl);
        this.mRlNickName = (RelativeLayout) findViewById(R.id.tcb_user_nickname_rl);
        this.mRlCompanyName = (RelativeLayout) findViewById(R.id.tcb_user_commpany_rl);
        this.mTxtNickName = (TextView) findViewById(R.id.tcb_user_nickname_txt);
        this.mTxtPhone = (TextView) findViewById(R.id.tcb_user_phone_ed);
        this.mTxtCompanyName = (TextView) findViewById(R.id.tcb_user_commpany_name_txt);
        this.mTxtCompanyFlag = (TextView) findViewById(R.id.tcb_user_company_flag_txt);
        this.mRlAccount = (RelativeLayout) findViewById(R.id.tcb_user_account_rl);
        this.mRlPhone = (RelativeLayout) findViewById(R.id.tcb_user_phone_rl);
        this.mTxtAccount = (TextView) findViewById(R.id.tcb_user_account_txt);
        this.mTxtSave = (TextView) findViewById(R.id.tcb_save_txt);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == -1) {
            switch (requestCode) {
                case PictureConfig.CHOOSE_REQUEST /*188*/:
                    List<LocalMedia> selectList = PictureSelector.obtainMultipleResult(data);
                    if (selectList != null) {
                        String path = "";
                        if (((LocalMedia) selectList.get(0)).isCut() && !((LocalMedia) selectList.get(0)).isCompressed()) {
                            path = ((LocalMedia) selectList.get(0)).getCutPath();
                        } else if (((LocalMedia) selectList.get(0)).isCompressed() || (((LocalMedia) selectList.get(0)).isCut() && ((LocalMedia) selectList.get(0)).isCompressed())) {
                            path = ((LocalMedia) selectList.get(0)).getCompressPath();
                        } else {
                            path = ((LocalMedia) selectList.get(0)).getPath();
                        }
                        ImageLoader.getInstance().displayImage(ImageLoaderUtil.getUriFromLocalFile(new File(path)), this.mImgHeader);
                        uploadImage(path);
                    }
                    return;
                default:
                    return;
            }
        } else if (resultCode == 2) {
            this.mTxtAccount.setText(data.getStringExtra(SelectItemActivity.EXTRAS_SELECT_NAME));
        } else if (resultCode == 3 && requestCode == 100) {
            String value = data.getStringExtra(CommonEditActivity.EXTRAS_VALUE);
            String type = data.getStringExtra(CommonEditActivity.EXTRAS_TYPE);
            if (type.equals("nickName")) {
                this.mTxtNickName.setText(value);
            } else if (type.equals("companyName")) {
                this.mTxtCompanyName.setText(value);
                this.mTxtCompanyFlag.setVisibility(View.GONE);
            } else if (type.equals("phone")) {
                this.mTxtPhone.setText(value);
            }
        }
    }

    private void uploadImage(final String filePath) {
        PutObjectRequest put = new PutObjectRequest(AppConstant.ALIYUN_OSS_BUCKET, AppConstant.ALIYUN_OSS_KEY + filePath, filePath);
        put.setProgressCallback(new C10179());
        OSSAsyncTask task = MyApplication.getInstance().getOssClient().asyncPutObject(put, new OSSCompletedCallback<PutObjectRequest, PutObjectResult>() {
            public void onSuccess(PutObjectRequest request, PutObjectResult result) {
                UserInfoActivity.this.mHeaderUrl = ServerUrl.URL_UPLOAD + "/" + AppConstant.ALIYUN_OSS_KEY + filePath;
            }

            public void onFailure(PutObjectRequest request, ClientException clientExcepion, ServiceException serviceException) {
                if (clientExcepion != null) {
                    clientExcepion.printStackTrace();
                }
                if (serviceException != null) {
                }
            }
        });
    }

    private void httpUpdateUserInfo() {
        RequestQueue queue = NoHttp.newRequestQueue();
        EditUserRequest request = null;
        try {
            request = new EditUserRequest(ServerUrl.getInst().UPDATE_USER_INFO_URL(), RequestMethod.POST, this.mHeaderUrl, this.mTxtNickName.getText().toString(), this.mTxtCompanyName.getText().toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        request.setmBeanClass(LoginInfo.class, 0);
        queue.add(0, request, new OnResponseListener<LoginInfo>() {
            public void onSucceed(int what, Response<LoginInfo> response) {
                LoginInfo loginInfo = (LoginInfo) response.get();
                if (loginInfo != null) {
                    Log.e(UserInfoActivity.this.TAG, loginInfo.toString());
                    MyApplication.getInstance().setUserInfo(loginInfo.getUser());
                    BroadCastUtil.sendBroadCast(UserInfoActivity.this.mContext, BroadCastAction.USER_INFO_CHANGE);
                    UserInfoActivity.this.finish();
                }
            }

            public void onFailed(int what, Response<LoginInfo> response) {
                Exception exception = response.getException();
            }

            public void onStart(int what) {
                UserInfoActivity.this.showCommonProgreessDialog("保存中..");
            }

            public void onFinish(int what) {
                UserInfoActivity.this.dismissCommonProgressDialog();
            }
        });

    }
}
