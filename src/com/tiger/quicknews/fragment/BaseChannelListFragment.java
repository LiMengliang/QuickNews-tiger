
package com.tiger.quicknews.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ProgressBar;

import com.nhaarman.listviewanimations.swinginadapters.AnimationAdapter;
import com.tiger.quicknews.R;
import com.tiger.quicknews.activity.*;
import com.tiger.quicknews.adapter.CardsAnimationAdapter;
import com.tiger.quicknews.adapter.NewsDigestAdapter;
import com.tiger.quicknews.bean.NewsDigestModel;
import com.tiger.quicknews.http.HttpUtil;
import com.tiger.quicknews.http.UrlUtils;
import com.tiger.quicknews.http.json.NewListJson;
import com.tiger.quicknews.initview.InitView;
import com.tiger.quicknews.utils.StringUtils;
import com.tiger.quicknews.wedget.swiptlistview.SwipeListView;
import com.tiger.quicknews.wedget.viewimage.Animations.DescriptionAnimation;
import com.tiger.quicknews.wedget.viewimage.Animations.SliderLayout;
import com.tiger.quicknews.wedget.viewimage.SliderTypes.BaseSliderView;
import com.tiger.quicknews.wedget.viewimage.SliderTypes.BaseSliderView.OnSliderClickListener;
import com.tiger.quicknews.wedget.viewimage.SliderTypes.TextSliderView;
import com.umeng.analytics.MobclickAgent;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ItemClick;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@EFragment(R.layout.activity_main)
public class BaseChannelListFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener,
        OnSliderClickListener {
    protected SliderLayout mDemoSlider;
    @ViewById(R.id.swipe_container)
    protected SwipeRefreshLayout swipeLayout;
    @ViewById(R.id.listview)
    protected SwipeListView mListView;
    @ViewById(R.id.progressBar)
    protected ProgressBar mProgressBar;
    protected HashMap<String, String> url_maps;

    protected HashMap<String, NewsDigestModel> imgUrlAndNewsMap;
    
    public String channelId;
    public String cacheName;

    @Bean
    protected NewsDigestAdapter newsDigestsAdapter;
    protected List<NewsDigestModel> newsDigests;
    private int index = 0;
    private boolean isRefresh = false;  

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @AfterInject
    protected void init() {
        newsDigests = new ArrayList<NewsDigestModel>();
        url_maps = new HashMap<String, String>();

        imgUrlAndNewsMap = new HashMap<String, NewsDigestModel>();
    }

    @AfterViews
    protected void initView() {

        swipeLayout.setOnRefreshListener(this);
        InitView.instance().initSwipeRefreshLayout(swipeLayout);
        InitView.instance().initListView(mListView, getActivity());
        View headView = LayoutInflater.from(getActivity()).inflate(R.layout.head_item, null);
        mDemoSlider = (SliderLayout) headView.findViewById(R.id.slider);
        mListView.addHeaderView(headView);
        AnimationAdapter animationAdapter = new CardsAnimationAdapter(newsDigestsAdapter);
        animationAdapter.setAbsListView(mListView);
        mListView.setAdapter(animationAdapter);
        loadData(getNewsListUrl(index + "", channelId));

        mListView.setOnBottomListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                currentPagte++;
                index = index + 20;
                loadData(getNewsListUrl(index + "", channelId));
            }
        });
    }

    private void loadData(String url) {
        if (HttpUtil.isNetworkAvailable(getMyActivity())) {
            loadNewsDigests(url);
        } else {
            mListView.onBottomComplete();
            mProgressBar.setVisibility(View.GONE);
            getMyActivity().showShortToast(getString(R.string.not_network));
            String result = getMyActivity().getCacheStr(cacheName + currentPagte);
            if (!StringUtils.isEmpty(result)) {
                getResult(result);
            }
        }
    }

    private void initSliderLayout(List<NewsDigestModel> newModles) {

        if (!isNullString(newModles.get(0).getImgsrc()))
            imgUrlAndNewsMap.put(newModles.get(0).getImgsrc(), newModles.get(0));
        if (!isNullString(newModles.get(1).getImgsrc()))
            imgUrlAndNewsMap.put(newModles.get(1).getImgsrc(), newModles.get(1));
        if (!isNullString(newModles.get(2).getImgsrc()))
            imgUrlAndNewsMap.put(newModles.get(2).getImgsrc(), newModles.get(2));
        if (!isNullString(newModles.get(3).getImgsrc()))
            imgUrlAndNewsMap.put(newModles.get(3).getImgsrc(), newModles.get(3));

        if (!isNullString(newModles.get(0).getImgsrc()))
            url_maps.put(newModles.get(0).getTitle(), newModles.get(0).getImgsrc());
        if (!isNullString(newModles.get(1).getImgsrc()))
            url_maps.put(newModles.get(1).getTitle(), newModles.get(1).getImgsrc());
        if (!isNullString(newModles.get(2).getImgsrc()))
            url_maps.put(newModles.get(2).getTitle(), newModles.get(2).getImgsrc());
        if (!isNullString(newModles.get(3).getImgsrc()))
            url_maps.put(newModles.get(3).getTitle(), newModles.get(3).getImgsrc());

        for (String name : url_maps.keySet()) {
            TextSliderView textSliderView = new TextSliderView(getActivity());
            textSliderView.setOnSliderClickListener(this);
            textSliderView
                    .description(name)
                    .image(url_maps.get(name));

            textSliderView.getBundle()
                    .putString("extra", name);
            mDemoSlider.addSlider(textSliderView);
        }

        mDemoSlider.setPresetTransformer(SliderLayout.Transformer.Default);
        mDemoSlider.setPresetIndicator(SliderLayout.PresetIndicators.Right_Bottom);
        mDemoSlider.setCustomAnimation(new DescriptionAnimation());
        newsDigestsAdapter.appendList(newModles);
    }

    @Override
    public void onRefresh() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                currentPagte = 1;
                isRefresh = true;
                loadData(getNewsListUrl(0 + "", channelId));
                url_maps.clear();
                mDemoSlider.removeAllSliders();
            }
        }, 2000);
    }

    @ItemClick(R.id.listview)
    protected void onItemClick(int position) {
        NewsDigestModel newModle = newsDigests.get(position - 1);
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
        ((BaseActivity) getActivity()).openActivity(class1,
                bundle, 0);
    }

    @Background
    void loadNewsDigests(String url) {
        String result;
        try {
            result = HttpUtil.getByHttpClient(getActivity(), url);
            getResult(result);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @UiThread
    public void getResult(String result) {
        getMyActivity().setCacheStr(cacheName + currentPagte, result);
        if (isRefresh) {
            isRefresh = false;
            newsDigestsAdapter.clear();
            newsDigests.clear();
        }
        mProgressBar.setVisibility(View.GONE);
        swipeLayout.setRefreshing(false);

        List<NewsDigestModel> list = NewListJson.instance(getActivity()).readJsonNewModles(result, channelId);
        if (index == 0 && list.size() >= 4) {
            initSliderLayout(list);
        } else {
            newsDigestsAdapter.appendList(list);
        }
        newsDigests.addAll(list);
        mListView.onBottomComplete();
    }

    @Override
    public void onSliderClick(BaseSliderView slider) {
        NewsDigestModel newModle = imgUrlAndNewsMap.get(slider.getUrl());
        enterDetailActivity(newModle);
    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("MainScreen"); // 统计页面
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("MainScreen");
    }
    
    protected String getNewsListUrl(String index, String itemId)
    {
    	String urlString = UrlUtils.CommonUrl + itemId + "/" + index + UrlUtils.endUrl;
        return urlString;
    }
}