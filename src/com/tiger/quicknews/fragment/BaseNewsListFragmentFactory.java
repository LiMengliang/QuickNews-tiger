package com.tiger.quicknews.fragment;

import android.support.v4.app.Fragment;

public class BaseNewsListFragmentFactory {

	public static Fragment createBaseChannelListFragment(String channelId, String cacheName)
	{
		BaseChannelListFragment fragment = new BaseChannelListFragment_();
		fragment.channelId = channelId;
		fragment.cacheName = cacheName;
		return fragment;
	}
}
