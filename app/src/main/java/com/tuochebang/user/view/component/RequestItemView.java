package com.tuochebang.user.view.component;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.RelativeLayout;
import com.framework.app.component.utils.ActivityUtil;
import com.framework.app.component.widget.DataLoadingView;
import com.framework.app.component.widget.XListEmptyView;
import com.framework.app.component.widget.XListView;
import com.tuochebang.user.R;
import com.tuochebang.user.adapter.TuocheRequestListAdapter;
import com.tuochebang.user.request.base.ServerUrl;
import com.tuochebang.user.request.entity.TuocheRequestInfo;
import com.tuochebang.user.request.task.GetUserTuocheRequest;
import com.tuochebang.user.ui.request.RequestDetailActivity;
import com.yanzhenjie.nohttp.NoHttp;
import com.yanzhenjie.nohttp.RequestMethod;
import com.yanzhenjie.nohttp.rest.OnResponseListener;
import com.yanzhenjie.nohttp.rest.RequestQueue;
import com.yanzhenjie.nohttp.rest.Response;

import java.util.List;

import static com.framework.app.component.widget.XListView.XListRefreshType.ON_PULL_REFRESH;


public class RequestItemView extends RelativeLayout {
    public static int TUOCHE_REQUEST_COMPLETE_STATUS = 2;
    public static int TUOCHE_REQUEST_DOING_STATUS = 1;
    public static int TUOCHE_REQUEST_WAIT_STATUS = 0;
    private TuocheRequestListAdapter adapter;
    private int mCurrentPage = 1;
    private DataLoadingView mDataLoadingView;
    private XListView mListRequest;
    private int mStatus;
    private SwipeRefreshLayout mSwipRefreshList;

    /* renamed from: com.tuochebang.user.view.component.RequestItemView$1 */
    class C10421 implements OnRefreshListener {
        C10421() {
        }

        public void onRefresh() {
            RequestItemView.this.mCurrentPage = 1;
            RequestItemView.this.httpGetTucheRequest(ON_PULL_REFRESH, false);
        }
    }

    /* renamed from: com.tuochebang.user.view.component.RequestItemView$5 */
    class C10475 extends Handler {
        C10475() {
        }
    }

    public RequestItemView(Context context) {
        super(context);
        initView(context);
    }

    public RequestItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public RequestItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(final Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.component_request_item_view, this);
        this.mListRequest = (XListView) view.findViewById(R.id.request_list);
        this.mDataLoadingView = (DataLoadingView) view.findViewById(R.id.data_loadingview);
        XListEmptyView xListEmptyView = (XListEmptyView) view.findViewById(R.id.xlist_empty_view);
        xListEmptyView.setEmptyInfo(R.mipmap.ic_list_empty_view, "暂时没有请求");
        this.mListRequest.setEmptyView(xListEmptyView);
        this.mSwipRefreshList = (SwipeRefreshLayout) view.findViewById(R.id.swip_refresh);
        this.mSwipRefreshList.setOnRefreshListener(new C10421());
        this.mListRequest.setPullLoadEnable(false);
        this.mListRequest.setAutoLoadMoreEnable(true);
        this.mListRequest.setPullRefreshEnable(false);
        this.mListRequest.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                TuocheRequestInfo info = (TuocheRequestInfo) RequestItemView.this.adapter.getItem(position - 1);
                if (info != null) {
                    Bundle bundle = new Bundle();
                    bundle.putSerializable(RequestDetailActivity.EXTRAS_REQUEST_INFO, info);
                    ActivityUtil.next((Activity) context, RequestDetailActivity.class, bundle);
                }
            }
        });
        this.adapter = new TuocheRequestListAdapter(context);
        this.mListRequest.setAdapter(this.adapter);
    }

    public void setData(int status) {
        this.mStatus = status;
        this.mCurrentPage = 1;
        httpGetTucheRequest(ON_PULL_REFRESH, true);
    }

    private void httpGetTucheRequest(final XListView.XListRefreshType xListRefreshType, final boolean showLoadingView) {
        final RequestQueue queue = NoHttp.newRequestQueue();
        GetUserTuocheRequest request = new GetUserTuocheRequest(ServerUrl.getInst().GET_TUOCHE_REQUEST(), RequestMethod.POST, this.mStatus, this.mCurrentPage);
        request.setmBeanClass(TuocheRequestInfo.class, -1);
        queue.add(0, request, new OnResponseListener<List<TuocheRequestInfo>>() {

            /* renamed from: com.tuochebang.user.view.component.RequestItemView$3$1 */
            class C10441 implements OnClickListener {
                C10441() {
                }

                public void onClick(View v) {
                    RequestItemView.this.httpGetTucheRequest(ON_PULL_REFRESH, true);
                }
            }

            public void onSucceed(int what, Response<List<TuocheRequestInfo>> response) {
                if (showLoadingView) {
                    RequestItemView.this.mDataLoadingView.showDataLoadSuccess();
                }
                List<TuocheRequestInfo> tuocheRequestInfos = (List) response.get();
                if (tuocheRequestInfos != null) {
                    Log.e("RequestItemView: ", tuocheRequestInfos.toString());
                    if (ON_PULL_REFRESH == xListRefreshType) {
                        RequestItemView.this.adapter.setList(tuocheRequestInfos);
                    } else {
                        RequestItemView.this.adapter.addList(tuocheRequestInfos);
                    }
                }
                if (tuocheRequestInfos == null || tuocheRequestInfos.size() < 10) {
                    RequestItemView.this.mListRequest.setPullLoadEnable(false);
                } else {
                    RequestItemView.this.mListRequest.setPullLoadEnable(true);
                }
            }

            public void onFailed(int what, Response<List<TuocheRequestInfo>> response) {
                Exception exception = response.getException();
                RequestItemView.this.mCurrentPage = RequestItemView.this.mCurrentPage - 1;
                RequestItemView.this.mDataLoadingView.showDataLoadFailed("网络开小差了...");
                RequestItemView.this.mDataLoadingView.setOnReloadClickListener(new C10441());
            }

            public void onStart(int what) {
            }

            public void onFinish(int what) {
                if (ON_PULL_REFRESH == xListRefreshType) {
                    RequestItemView.this.mSwipRefreshList.setRefreshing(false);
                } else {
                    RequestItemView.this.mListRequest.onLoadMoreComplete();
                }
            }
        });
        if (showLoadingView) {
            this.mDataLoadingView.showDataLoading();
            new C10475().postDelayed(new Runnable() {
                public void run() {

                }
            }, 1000);
            return;
        }

    }

    public int getmStatus() {
        return this.mStatus;
    }

    public void setmStatus(int mStatus) {
        this.mStatus = mStatus;
    }
}
