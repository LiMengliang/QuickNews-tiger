
package com.tiger.quicknews.activity;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.tiger.quicknews.R;
import com.tiger.quicknews.adapter.NewsDigestAdapter;
import com.tiger.quicknews.bean.NewsDigestModel;
import com.tiger.quicknews.http.HttpUtil;
import com.tiger.quicknews.http.UrlUtils;
import com.tiger.quicknews.http.json.NewListJson;
import com.tiger.quicknews.initview.InitView;
import com.tiger.quicknews.utils.StringUtils;
import com.tiger.quicknews.wedget.swiptlistview.SwipeListView;
import com.umeng.analytics.MobclickAgent;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ItemClick;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;

@EActivity(R.layout.activity_message)
public class MessageActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener,
        OnClickListener {
    @ViewById(R.id.title)
    protected TextView mTitle;
    @ViewById(R.id.swipe_container)
    protected SwipeRefreshLayout swipeLayout;
    @ViewById(R.id.listview)
    protected SwipeListView mListView;
    @ViewById(R.id.progressBar)
    protected ProgressBar mProgressBar;

    // 是否刷新操作
    private boolean isRefresh = false;
    @Bean
    protected NewsDigestAdapter newAdapter;
    protected List<NewsDigestModel> listsModles;
    private int index;

    @AfterViews
    public void initView() {
        try {
            listsModles = new ArrayList<NewsDigestModel>();
            mTitle.setText("消息列表");
            swipeLayout.setOnRefreshListener(this);
            InitView.instance().initSwipeRefreshLayout(swipeLayout);
            InitView.instance().initListView(mListView, this);
            loadData(UrlUtils.getMsgUrl(0 + "", UrlUtils.MsgId));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onRefresh() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                isRefresh = true;
                loadData(UrlUtils.getMsgUrl(0 + "", UrlUtils.MsgId));
            }
        }, 2000);
    }

    private void loadData(String url) {
        if (HttpUtil.isNetworkAvailable(this)) {
            loadNewList(url);
        } else {
            dismissProgressDialog();
            showShortToast(getString(R.string.not_network));
            String result = getCacheStr("MessageActivity");
            if (!StringUtils.isEmpty(result)) {
                getResult(result);
            }
        }
    }

    @Background
    void loadNewList(String url) {
        String result;
        try {
            result = HttpUtil.getByHttpClient(this, url);
            getResult(result);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @UiThread
    public void getResult(String result) {
        setCacheStr("MessageActivity", result);
        if (isRefresh) {
            isRefresh = false;
            newAdapter.clear();
            listsModles.clear();
        }
        mProgressBar.setVisibility(View.GONE);
        swipeLayout.setRefreshing(false);

        List<NewsDigestModel> list = NewListJson.instance(this).readJsonNewModles(result,
                UrlUtils.MsgId);
        newAdapter.appendList(list);
        listsModles.addAll(list);
        mListView.onBottomComplete();
    }

    @Override
    public void onClick(View v) {
        index = index + 40;
        loadData(UrlUtils.getMsgUrl(index + "", UrlUtils.MsgId));
    }

    @ItemClick(R.id.listview)
    protected void onItemClick(int position) {
        NewsDigestModel newModle = listsModles.get(position);
        enterDetailActivity(newModle);
    }

    public void enterDetailActivity(NewsDigestModel newModle) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("newModle", newModle);
        Class<?> class1;
        if (newModle.getImagesModle() != null && newModle.getImagesModle().getImgList().size() > 1) {
            class1 = ImageDetailActivity_.class;
        } else {
            class1 = DetailsActivity_.class;
        }
        openActivity(class1, bundle, 0);
    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

}
