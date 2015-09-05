package com.tiger.quicknews.adapter;

import java.util.ArrayList;
import java.util.List;

import org.androidannotations.annotations.EBean;

import com.tiger.quicknews.bean.NewsDigestModel;
import com.tiger.quicknews.view.AudioDigestView;
import com.tiger.quicknews.view.AudioDigestView_;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

@EBean
public class AudioAdapter extends BaseAdapter {
	public AudioAdapter(Context context)
	{
		super();
		_context = context;
	}
	
	public List<NewsDigestModel> lists = new ArrayList<NewsDigestModel>();
	
	public void appendList(List<NewsDigestModel> list) {
		if(!lists.containsAll(list) && list != null && list.size() > 0)
		{
			lists.addAll(list);
		}
		notifyDataSetChanged();
	}

	private Context _context;
	
	@Override
	public int getCount() {
		return lists.size();
	}

	@Override
	public Object getItem(int position) {
		return lists.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		AudioDigestView audioItemView;
		if(convertView == null)
		{
			audioItemView = AudioDigestView_.build(_context);	
		}
		else
		{
			audioItemView = (AudioDigestView) convertView;
		}
		NewsDigestModel newsModel = lists.get(position);
		audioItemView.updateNewsModel(newsModel);
		return audioItemView;
	}
	
	public void clear()
	{
		lists.clear();
	}

}
