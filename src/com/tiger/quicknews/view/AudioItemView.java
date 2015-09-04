package com.tiger.quicknews.view;

import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.tiger.quicknews.R;
import com.tiger.quicknews.bean.NewsDetailModel;
import com.tiger.quicknews.bean.NewsModel;
import com.tiger.quicknews.http.HttpUtil;
import com.tiger.quicknews.http.Url;
import com.tiger.quicknews.http.json.NewDetailJson;
import com.tiger.quicknews.utils.Options;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

@EViewGroup(R.layout.item_audio)
public class AudioItemView extends RelativeLayout implements ImageLoadingListener {

	@ViewById(R.id.audio_icon)
	protected ImageView leftImage;
	
	@ViewById(R.id.audio_title)
	protected TextView audioTitle;
	
	@ViewById(R.id.audio_content)
	protected TextView audioContent;
	
	@ViewById(R.id.audio_source)
	protected TextView audioSource;
	
	@ViewById(R.id.quick_play_audio)
	protected ImageView playButton;
	
	@ViewById(R.id.quick_qause_audio)
	protected ImageView pauseButton;
	
	private ImageLoader imageLoader = ImageLoader.getInstance();
	private MediaPlayer mediaPlayer = null;
	private String audioUrl;
	private NewsModel newsModel;
	private Context context;
	
	public AudioItemView(Context context) {
		super(context);
		this.context = context;
		// TODO Auto-generated constructor stub
	}

	public void updateNewsModel(NewsModel newsModel)
	{	
		this.newsModel = newsModel;
		audioTitle.setText(newsModel.getTitle());
		audioContent.setText(newsModel.getDigest());
		audioSource.setText(newsModel.getSource());
		if(!"".equals(newsModel.getImgsrc()))
		{
			leftImage.setVisibility(View.VISIBLE);
			imageLoader.displayImage(newsModel.getImgsrc(), leftImage, Options.getListOptions(), this);
		}
	}
	
	@Click(R.id.quick_play_audio)
	public void playAudio(View view)
	{
		if(mediaPlayer == null)
		{
			mediaPlayer = new MediaPlayer();
			setAudioUrlToMediaPlayer();
		}
		playButton.setVisibility(GONE);
		pauseButton.setVisibility(VISIBLE);
	}

	@Background
	protected void setAudioUrlToMediaPlayer() {
		String newsId = newsModel.getDocid();
		String newsUrl = Url.getUrl(newsId);
		String fullDetailContent;
		try {
			fullDetailContent = HttpUtil.getByHttpClient(context, newsUrl);
			NewsDetailModel newsDetailModel = NewDetailJson.instance(context).readJsonNewsDetailModel(fullDetailContent,
		        newsId);
			String audioUrl = newsDetailModel.getUrl_mp4();
			mediaPlayer.setDataSource(audioUrl);
			mediaPlayer.start();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Click(R.id.quick_qause_audio)
	public void pauseAudio(View view)
	{
		pauseButton.setVisibility(GONE);
		playButton.setVisibility(VISIBLE);
	}

	@Override
	public void onLoadingStarted(String imageUri, View view) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onLoadingFailed(String imageUri, View view,
			FailReason failReason) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
		// TODO Auto-generated method stub
		playButton.setVisibility(VISIBLE);
	}

	@Override
	public void onLoadingCancelled(String imageUri, View view) {
		// TODO Auto-generated method stub
		
	}
}
