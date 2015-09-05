
package com.tiger.quicknews.dao;

import android.content.ContentValues;

import com.tiger.quicknews.bean.ChannelItem;

import java.util.List;
import java.util.Map;

public interface ChannelDaoInface {
    public boolean addChannel(ChannelItem item);

    public boolean deleteChannel(String whereClause, String[] whereArgs);

    public boolean updateChannel(ContentValues values, String whereClause,
            String[] whereArgs);

    public Map<String, String> viewChannels(String selection,
            String[] selectionArgs);

    public List<Map<String, String>> listChannels(String selection,
            String[] selectionArgs);

    public void clearFeedTable();
}
