
package com.tiger.quicknews.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.media.MediaPlayer;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.nhaarman.listviewanimations.swinginadapters.AnimationAdapter;
import com.tiger.quicknews.R;
import com.tiger.quicknews.adapter.AudioAdapter;
import com.tiger.quicknews.adapter.CardsAnimationAdapter;
import com.tiger.quicknews.bean.NewsDigestModel;
import com.tiger.quicknews.http.HttpUtil;
import com.tiger.quicknews.http.UrlUtils;
import com.tiger.quicknews.http.json.NewListJson;
import com.tiger.quicknews.initview.InitView;
import com.tiger.quicknews.utils.ACache;
import com.tiger.quicknews.utils.StringUtils;
import com.tiger.quicknews.view.AudioDigestView;
import com.tiger.quicknews.wedget.swiptlistview.SwipeListView;
import com.tiger.quicknews.wedget.viewimage.SliderTypes.BaseSliderView;
import com.tiger.quicknews.wedget.viewimage.SliderTypes.BaseSliderView.OnSliderClickListener;
import com.umeng.analytics.MobclickAgent;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ItemClick;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

@EActivity(R.layout.activity_audio)
public class AudioActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener,
OnSliderClickListener {
	@ViewById(R.id.title)
	protected TextView mTitle;
	@ViewById(R.id.swipe_container)
	protected  SwipeRefreshLayout swipeLayout;
	@ViewById(R.id.listview)
	protected SwipeListView mListView;
	@ViewById(R.id.progressBar)
	protected ProgressBar mProgressBar;	
	
	public MediaPlayer mediaPlayer;
		
	private AudioAdapter audioAdapter = new AudioAdapter(this);
	private List<NewsDigestModel> listsModles;	
	private int currentPage = 1;
	private int index = 0;
	private boolean isRefresh;
	private String cacheName = "audio";
	private HashMap<String, NewsDigestModel> newsModelsMap = new HashMap<String, NewsDigestModel>();
	private List<AudioDigestView> audioDigestViews = new ArrayList<AudioDigestView>();
	public AudioDigestView activeAudioDigestView = null;
	
	
	@AfterInject
	protected void init() {
		listsModles = new ArrayList<NewsDigestModel>();
		mediaPlayer = new MediaPlayer();
	}
	
	private void loadData(String url) {
        if (HttpUtil.isNetworkAvailable(this)) {
            loadNewList(url);
        } else {
            mListView.onBottomComplete();
            mProgressBar.setVisibility(View.GONE);
            // this.showShortToast(getString(R.string.not_network));
            String result = ACache.get(this).getAsString(cacheName + currentPage);
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
		 // TODO:
	        // setCacheStr(cacheName + currentPage, result);
		 	if (!StringUtils.isEmpty(result)) {
	            ACache.get(this).put(cacheName + currentPage, result);
	        }
	        if (isRefresh) {
	            isRefresh = false;
	            audioAdapter.clear();
	            listsModles.clear();
	        }
	        mProgressBar.setVisibility(View.GONE);
	        swipeLayout.setRefreshing(false);

	        List<NewsDigestModel> list = NewListJson.instance(this).readJsonNewModles(result, UrlUtils.DianTaiId);
	        audioAdapter.appendList(list);
	        updateUrlToNewsModelMap(newsModelsMap, list);
	        listsModles.addAll(list);
	        mListView.onBottomComplete();
	    }
	
	@AfterViews
	public void initView() {
		try{
			mTitle.setText("音频新闻");
			swipeLayout.setOnRefreshListener(this);
			InitView.instance().initSwipeRefreshLayout(swipeLayout);
			InitView.instance().initListView(mListView, this);
			AnimationAdapter animationAdapter = new CardsAnimationAdapter(audioAdapter);
			animationAdapter.setAbsListView(mListView);
			mListView.setAdapter(animationAdapter);
			// Load data
			loadData(getNewsListUrl(index + "", UrlUtils.DianTaiId));
			mListView.setOnBottomListener(new OnClickListener() {
	            @Override
	            public void onClick(View v) {
	            	currentPage++;
	                index = index + 10;
	                // Load Data
	                loadData(getNewsListUrl(index + "", UrlUtils.DianTaiId));
	            }
	        });
			
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	@ItemClick(R.id.listview)
	protected void onItemClick(int position)
	{
		NewsDigestModel audioModel = listsModles.get(position - 1);
		// Play audio
	}

	@Override
	public void onRefresh() {
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				currentPage = 1;
				isRefresh = true;
				// Load data
				loadData(getNewsListUrl(index + "", UrlUtils.DianTaiId));
			}
		}, 2000);
		
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

	@Override
	public void onSliderClick(BaseSliderView slider) {
		// TODO Auto-generated method stub
		
	}
	
	public void addAudioDigestView(AudioDigestView audioDigestView)
	{
		audioDigestViews.add(audioDigestView);
	}
	
	public void updateActiveAudioDigestView(AudioDigestView audioDigestView)
	{
		if(audioDigestView == null)
		{
			activeAudioDigestView = null;
			return;
		}
		if(activeAudioDigestView == null)
		{
			audioDigestView.activate();
			activeAudioDigestView = audioDigestView;
		}
		else if(activeAudioDigestView != null && activeAudioDigestView != audioDigestView)
		{
			activeAudioDigestView.deactivate();
			audioDigestView.activate();
			activeAudioDigestView = audioDigestView;
		}			
	}
	
	private void updateUrlToNewsModelMap(HashMap<String, NewsDigestModel> map, List<NewsDigestModel> newsModels)
	{
		if(map == null)
		{
			map = new HashMap<String, NewsDigestModel>();
		}
		for(NewsDigestModel newsModel : newsModels)
		{
			map.put(newsModel.getImgsrc(), newsModel);
		}
	}
	
	private void testGit()
	{
		
	}
}
