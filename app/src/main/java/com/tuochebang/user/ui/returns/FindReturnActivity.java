package com.tuochebang.user.ui.returns;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import com.framework.app.component.widget.DataLoadingView;
import com.framework.app.component.widget.XListEmptyView;
import com.framework.app.component.widget.XListView;
import com.tuochebang.user.R;
import com.tuochebang.user.adapter.FindReturnListAdapter;
import com.tuochebang.user.base.BaseActivity;
import com.tuochebang.user.request.base.ServerUrl;
import com.tuochebang.user.request.entity.ReturnCarInfo;
import com.tuochebang.user.request.entity.TuocheRequestInfo;
import com.tuochebang.user.request.task.GetUserReturnsCarRequest;
import com.tuochebang.user.view.component.ReturnsItemView;
import com.yanzhenjie.nohttp.NoHttp;
import com.yanzhenjie.nohttp.RequestMethod;
import com.yanzhenjie.nohttp.rest.OnResponseListener;
import com.yanzhenjie.nohttp.rest.RequestQueue;
import com.yanzhenjie.nohttp.rest.Response;

import java.util.List;

public class FindReturnActivity extends BaseActivity {
    private FindReturnListAdapter adapter;
    private int mCurrentPage = 1;
    private DataLoadingView mDataLoadingView;
    private XListView mListRequest;
    private SwipeRefreshLayout mSwipRefreshList;
    private Toolbar mToolBar;

    /* renamed from: com.tuochebang.user.ui.returns.FindReturnActivity$1 */
    class C09891 implements OnClickListener {
        C09891() {
        }

        public void onClick(View v) {
            FindReturnActivity.this.finish();
        }
    }

    /* renamed from: com.tuochebang.user.ui.returns.FindReturnActivity$2 */
    class C09902 implements OnRefreshListener {
        C09902() {
        }

        public void onRefresh() {
            FindReturnActivity.this.mCurrentPage = 1;
            FindReturnActivity.this.httpGetFindReturnRequest(XListView.XListRefreshType.ON_PULL_REFRESH, false);
        }
    }

    /* renamed from: com.tuochebang.user.ui.returns.FindReturnActivity$3 */
    class C09913 implements OnItemClickListener {
        C09913() {
        }

        public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
        }
    }

    /* renamed from: com.tuochebang.user.ui.returns.FindReturnActivity$6 */
    class C09956 extends Handler {
        C09956() {
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_return);
        initToolBar();
        initView();
    }

    private void initToolBar() {
        this.mToolBar = (Toolbar) findViewById(R.id.toolbar);
        this.mToolBar.setNavigationOnClickListener(new C09891());
    }

    private void initView() {
        this.mListRequest = (XListView) findViewById(R.id.request_list);
        this.mDataLoadingView = (DataLoadingView) findViewById(R.id.data_loadingview);
        XListEmptyView xListEmptyView = (XListEmptyView) findViewById(R.id.xlist_empty_view);
        xListEmptyView.setEmptyInfo(R.mipmap.ic_list_empty_view, "暂时没有请求");
        this.mListRequest.setEmptyView(xListEmptyView);
        this.mSwipRefreshList = (SwipeRefreshLayout) findViewById(R.id.swip_refresh);
        this.mSwipRefreshList.setOnRefreshListener(new C09902());
        this.mListRequest.setPullLoadEnable(false);
        this.mListRequest.setAutoLoadMoreEnable(true);
        this.mListRequest.setPullRefreshEnable(false);
        this.mListRequest.setOnItemClickListener(new C09913());
        this.adapter = new FindReturnListAdapter(this.mContext);
        this.mListRequest.setAdapter(this.adapter);
        httpGetFindReturnRequest(XListView.XListRefreshType.ON_PULL_REFRESH, true);
    }

    private void httpGetFindReturnRequest(final XListView.XListRefreshType xListRefreshType, final boolean showLoadingView) {
        final RequestQueue queue = NoHttp.newRequestQueue();
        GetUserReturnsCarRequest request = new GetUserReturnsCarRequest(ServerUrl.getInst().GET_TUOCHE_RETURNS(), RequestMethod.POST, ReturnsItemView.TUOCHE_RETURN_WAIT_STATUS, this.mCurrentPage);
        request.setmBeanClass(TuocheRequestInfo.class, -1);
        queue.add(0, request, new OnResponseListener<List<ReturnCarInfo>>() {

            /* renamed from: com.tuochebang.user.ui.returns.FindReturnActivity$4$1 */
            class C09921 implements OnClickListener {
                C09921() {
                }

                public void onClick(View v) {
                    FindReturnActivity.this.httpGetFindReturnRequest(XListView.XListRefreshType.ON_PULL_REFRESH, true);
                }
            }

            public void onSucceed(int what, Response<List<ReturnCarInfo>> response) {
                if (showLoadingView) {
                    FindReturnActivity.this.mDataLoadingView.showDataLoadSuccess();
                }
                List<ReturnCarInfo> tuocheReturnInfos = (List) response.get();
                if (tuocheReturnInfos != null) {
                    Log.e("RequestItemView: ", tuocheReturnInfos.toString());
                    if (XListView.XListRefreshType.ON_PULL_REFRESH == xListRefreshType) {
                        FindReturnActivity.this.adapter.setList(tuocheReturnInfos);
                    } else {
                        FindReturnActivity.this.adapter.addList(tuocheReturnInfos);
                    }
                }
                if (tuocheReturnInfos == null || tuocheReturnInfos.size() < 10) {
                    FindReturnActivity.this.mListRequest.setPullLoadEnable(false);
                } else {
                    FindReturnActivity.this.mListRequest.setPullLoadEnable(true);
                }
            }

            public void onFailed(int what, Response<List<ReturnCarInfo>> response) {
                Exception exception = response.getException();
                FindReturnActivity.this.mCurrentPage = FindReturnActivity.this.mCurrentPage - 1;
                FindReturnActivity.this.mDataLoadingView.showDataLoadFailed("网络开小差了...");
                FindReturnActivity.this.mDataLoadingView.setOnReloadClickListener(new C09921());
            }

            public void onStart(int what) {
            }

            public void onFinish(int what) {
                if (XListView.XListRefreshType.ON_PULL_REFRESH == xListRefreshType) {
                    FindReturnActivity.this.mSwipRefreshList.setRefreshing(false);
                } else {
                    FindReturnActivity.this.mListRequest.onLoadMoreComplete();
                }
            }
        });
        if (showLoadingView) {
            this.mDataLoadingView.showDataLoading();
            new C09956().postDelayed(new Runnable() {
                public void run() {

                }
            }, 1000);
            return;
        }

    }
}
