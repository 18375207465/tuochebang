package com.tuochebang.user.ui.user;

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
import com.framework.app.component.utils.ActivityUtil;
import com.framework.app.component.widget.DataLoadingView;
import com.framework.app.component.widget.XListEmptyView;
import com.framework.app.component.widget.XListView;
import com.tuochebang.user.R;
import com.tuochebang.user.adapter.MessageListAdapter;
import com.tuochebang.user.base.BaseActivity;
import com.tuochebang.user.request.base.ServerUrl;
import com.tuochebang.user.request.entity.MessageInfo;
import com.tuochebang.user.request.task.GetUserMessageRequest;
import com.tuochebang.user.ui.message.MessageDetailActivity;
import com.yanzhenjie.nohttp.NoHttp;
import com.yanzhenjie.nohttp.RequestMethod;
import com.yanzhenjie.nohttp.rest.OnResponseListener;
import com.yanzhenjie.nohttp.rest.RequestQueue;
import com.yanzhenjie.nohttp.rest.Response;

import java.util.List;

public class UserMessageActivity extends BaseActivity {
    private MessageListAdapter adapter;
    private int mCurrentPage = 1;
    private DataLoadingView mDataLoadingView;
    private XListView mListRequest;
    private SwipeRefreshLayout mSwipRefreshList;
    private Toolbar mToolBar;

    /* renamed from: com.tuochebang.user.ui.user.UserMessageActivity$1 */
    class C10181 implements OnClickListener {
        C10181() {
        }

        public void onClick(View v) {
            UserMessageActivity.this.finish();
        }
    }

    /* renamed from: com.tuochebang.user.ui.user.UserMessageActivity$2 */
    class C10192 implements OnRefreshListener {
        C10192() {
        }

        public void onRefresh() {
            UserMessageActivity.this.mCurrentPage = 1;
            UserMessageActivity.this.httpGetMessageRequest(XListView.XListRefreshType.ON_PULL_REFRESH, false);
        }
    }

    /* renamed from: com.tuochebang.user.ui.user.UserMessageActivity$3 */
    class C10203 implements OnItemClickListener {
        C10203() {
        }

        public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
            MessageInfo info = (MessageInfo) UserMessageActivity.this.adapter.getItem(position - 1);
            if (info != null) {
                Bundle bundle = new Bundle();
                bundle.putSerializable(MessageDetailActivity.EXTRAS_MESSAGE_INFO, info);
                ActivityUtil.next(UserMessageActivity.this, MessageDetailActivity.class, bundle);
            }
        }
    }

    /* renamed from: com.tuochebang.user.ui.user.UserMessageActivity$6 */
    class C10246 extends Handler {
        C10246() {
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        initToolBar();
        initView();
        setData();
    }

    private void setData() {
        httpGetMessageRequest(XListView.XListRefreshType.ON_PULL_REFRESH, true);
    }

    private void initToolBar() {
        this.mToolBar = (Toolbar) findViewById(R.id.toolbar);
        this.mToolBar.setNavigationOnClickListener(new C10181());
    }

    private void initView() {
        this.mListRequest = (XListView) findViewById(R.id.request_list);
        this.mDataLoadingView = (DataLoadingView) findViewById(R.id.data_loadingview);
        XListEmptyView xListEmptyView = (XListEmptyView) findViewById(R.id.xlist_empty_view);
        xListEmptyView.setEmptyInfo(R.mipmap.ic_list_empty_view, "暂时没有拖车");
        this.mListRequest.setEmptyView(xListEmptyView);
        this.mSwipRefreshList = (SwipeRefreshLayout) findViewById(R.id.swip_refresh);
        this.mSwipRefreshList.setOnRefreshListener(new C10192());
        this.mListRequest.setPullLoadEnable(false);
        this.mListRequest.setAutoLoadMoreEnable(true);
        this.mListRequest.setPullRefreshEnable(false);
        this.adapter = new MessageListAdapter(this.mContext);
        this.mListRequest.setAdapter(this.adapter);
        this.mListRequest.setOnItemClickListener(new C10203());
    }

    private void httpGetMessageRequest(final XListView.XListRefreshType xListRefreshType, final boolean showLoadingView) {
        final RequestQueue queue = NoHttp.newRequestQueue();
        GetUserMessageRequest request = new GetUserMessageRequest(ServerUrl.getInst().GET_TUOCHE_MESSAGE(), RequestMethod.POST, this.mCurrentPage);
        request.setmBeanClass(MessageInfo.class, -1);
        queue.add(0, request, new OnResponseListener<List<MessageInfo>>() {

            /* renamed from: com.tuochebang.user.ui.user.UserMessageActivity$4$1 */
            class C10211 implements OnClickListener {
                C10211() {
                }

                public void onClick(View v) {
                    UserMessageActivity.this.httpGetMessageRequest(XListView.XListRefreshType.ON_PULL_REFRESH, true);
                }
            }

            public void onSucceed(int what, Response<List<MessageInfo>> response) {
                if (showLoadingView) {
                    UserMessageActivity.this.mDataLoadingView.showDataLoadSuccess();
                }
                List<MessageInfo> messageRequestInfos = (List) response.get();
                if (messageRequestInfos != null) {
                    Log.e(UserMessageActivity.this.TAG, messageRequestInfos.toString());
                    if (XListView.XListRefreshType.ON_PULL_REFRESH == xListRefreshType) {
                        UserMessageActivity.this.adapter.setList(messageRequestInfos);
                    } else {
                        UserMessageActivity.this.adapter.addList(messageRequestInfos);
                    }
                }
                if (messageRequestInfos == null || messageRequestInfos.size() < 10) {
                    UserMessageActivity.this.mListRequest.setPullLoadEnable(false);
                } else {
                    UserMessageActivity.this.mListRequest.setPullLoadEnable(true);
                }
            }

            public void onFailed(int what, Response<List<MessageInfo>> response) {
                Exception exception = response.getException();
                UserMessageActivity.this.mCurrentPage = UserMessageActivity.this.mCurrentPage - 1;
                UserMessageActivity.this.mDataLoadingView.showDataLoadFailed("网络开小差了...");
                UserMessageActivity.this.mDataLoadingView.setOnReloadClickListener(new C10211());
            }

            public void onStart(int what) {
            }

            public void onFinish(int what) {
                if (XListView.XListRefreshType.ON_PULL_REFRESH == xListRefreshType) {
                    UserMessageActivity.this.mSwipRefreshList.setRefreshing(false);
                } else {
                    UserMessageActivity.this.mListRequest.onLoadMoreComplete();
                }
            }
        });
        if (showLoadingView) {
            this.mDataLoadingView.showDataLoading();
            new C10246().postDelayed(new Runnable() {
                public void run() {

                }
            }, 1000);
            return;
        }

    }
}
