package com.tiger.quicknews.view;

import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.tiger.quicknews.R;
import com.tiger.quicknews.utils.Options;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

@EViewGroup(R.layout.item_audio)
public class AudioItemView extends RelativeLayout {

	@ViewById(R.id.audio_icon)
	protected ImageView leftImage;
	
	@ViewById(R.id.audio_title)
	protected TextView audioTitle;
	
	@ViewById(R.id.audio_content)
	protected TextView audioContent;
	
	@ViewById(R.id.audio_source)
	protected TextView audioSource;
	
	private ImageLoader imageLoader = ImageLoader.getInstance();
	
	public AudioItemView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public void setText(String title, String content, String source, String imageUrl)
	{
		audioTitle.setText(title);
		audioContent.setText(content);
		audioSource.setText(source);
		if(!"".equals(imageUrl))
		{
			leftImage.setVisibility(View.VISIBLE);
			imageLoader.displayImage(imageUrl, leftImage, Options.getListOptions());
		}
	}
}
