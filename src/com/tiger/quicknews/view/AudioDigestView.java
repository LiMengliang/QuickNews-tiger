package com.tiger.quicknews.view;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.tiger.quicknews.R;
import com.tiger.quicknews.bean.NewsDetailModel;
import com.tiger.quicknews.bean.NewsDigestModel;
import com.tiger.quicknews.http.HttpUtil;
import com.tiger.quicknews.http.UrlUtils;
import com.tiger.quicknews.http.json.NewDetailJson;
import com.tiger.quicknews.utils.ACache;
import com.tiger.quicknews.utils.Options;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tiger.quicknews.activity.AudioActivity_;

@EViewGroup(R.layout.item_audio)
public class AudioDigestView extends RelativeLayout implements ImageLoadingListener {

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
	
	@ViewById(R.id.audio_time)
	protected TextView audio_time;
	
	private ImageLoader imageLoader = ImageLoader.getInstance();
	private MediaPlayer mediaPlayer = null;
	private String audioUrl;
	private NewsDigestModel newsModel;
	private Context context;
	private Handler timingHandler = new Handler();
	private Runnable timingRunnable = new Runnable()
	{
		@Override
		public void run() {
			// TODO Auto-generated method stub
			if(mediaPlayer != null && mediaPlayer.isPlaying())
			{
				int currentProgress = mediaPlayer.getCurrentPosition();
				int duration = mediaPlayer.getDuration();
				setAudioTime(duration, currentProgress);
				timingHandler.postDelayed(timingRunnable, 1000);
			}
			else if(mediaPlayer != null && !mediaPlayer.isPlaying())
			{
				playButton.setVisibility(VISIBLE);
				pauseButton.setVisibility(GONE);
				audio_time.setText("");
				timingHandler.removeCallbacks(timingRunnable);
			}
		}		
	};
		
	public AudioDigestView(Context context) {
		super(context);
		this.context = context;
		((AudioActivity_)context).addAudioDigestView(this);
		mediaPlayer = ((AudioActivity_)context).mediaPlayer;
		// TODO Auto-generated constructor stub
	}

	public void updateNewsModel(NewsDigestModel newsModel)
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
		((AudioActivity_)context).updateActiveAudioDigestView(this);
		setAudioUrlToMediaPlayer();
		activate();
	}

	@Background
	protected void setAudioUrlToMediaPlayer() {
		String newsId = newsModel.getDocid();
		String newsUrl = UrlUtils.getNewsDetailUrl(newsId);
		String fullDetailContent;
		try {
			fullDetailContent = HttpUtil.getByHttpClient(context, newsUrl);
			NewsDetailModel newsDetailModel = NewDetailJson.instance(context).readJsonNewsDetailModel(fullDetailContent,
		        newsId);
			String progress = ACache.get(context).getAsString(audioUrl+"progress");
			int previousProgress = 0;
			if(progress != null)
			{
				previousProgress = Integer.parseInt(progress);
			}
			else
			{
				setAudioTimeToLoading();
			}
			if(mediaPlayer.isPlaying())
			{
				mediaPlayer.stop();
			}
			audioUrl = newsDetailModel.getUrl_mp4();			
			mediaPlayer.reset();
			mediaPlayer.setDataSource(audioUrl);			
			mediaPlayer.prepare();
			mediaPlayer.start();
			timingHandler.postDelayed(timingRunnable, 1000);
			int duration = mediaPlayer.getDuration();
			setAudioTime(duration, previousProgress);
			mediaPlayer.seekTo(previousProgress);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Click(R.id.quick_qause_audio)
	public void pauseAudio(View view)
	{
		((AudioActivity_)context).updateActiveAudioDigestView(null);
		deactivate();
		mediaPlayer.pause();
		int currentProcess = mediaPlayer.getCurrentPosition();
		ACache.get(context).put(audioUrl+"progress", Integer.toString(currentProcess));
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
	
	@UiThread
	protected void setAudioTime(int duration, int currentProcess)
	{
		StringBuilder sb = new StringBuilder(intToTime(currentProcess));
		sb.append("-");
		sb.append(intToTime(duration));
		audio_time.setText(sb);
	}
	
	@UiThread
	protected void setAudioTimeToLoading()
	{
		audio_time.setText("加载中...");
	}
	
	public void activate()
	{
		playButton.setVisibility(GONE);
		pauseButton.setVisibility(VISIBLE);
	}
	
	public void deactivate()
	{
		playButton.setVisibility(VISIBLE);
		pauseButton.setVisibility(GONE);	
		timingHandler.removeCallbacks(timingRunnable);
		audio_time.setText("");
	}
	
	private String intToTime(int time)
	{
		int second = time / 1000 % 60;
		int min = time / 1000 / 60;
		int hour = time / 1000 / 3600;
		return String.format("%02d:%02d:%02d", hour, min, second);
	}
}
