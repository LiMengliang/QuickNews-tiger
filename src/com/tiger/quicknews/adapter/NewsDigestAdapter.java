
package com.tiger.quicknews.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.tiger.quicknews.bean.NewsModel;

import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;

import com.tiger.quicknews.view.NewsDigestView;
import com.tiger.quicknews.view.NewsDigestView_;

import java.util.ArrayList;
import java.util.List;

@EBean
public class NewsDigestAdapter extends BaseAdapter {
    public List<NewsModel> lists = new ArrayList<NewsModel>();

    private String currentItem;

    public void appendList(List<NewsModel> list) {
        if (!lists.containsAll(list) && list != null && list.size() > 0) {
            lists.addAll(list);
        }
        notifyDataSetChanged();
    }

    @RootContext
    Context context;

    public void clear() {
        lists.clear();
        notifyDataSetChanged();
    }

    public void currentItem(String item) {
        this.currentItem = item;
    }

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

    	NewsDigestView newItemView;

        if (convertView == null) {
            newItemView = NewsDigestView_.build(context);
        } else {
            newItemView = (NewsDigestView) convertView;
        }

        NewsModel newModle = lists.get(position);
        if (newModle.getImagesModle() == null) {
            newItemView.setTexts(newModle.getTitle(), newModle.getDigest(), newModle.getSource(),
                    newModle.getImgsrc(), currentItem);
        } else {
            newItemView.setImages(newModle);
        }

        return newItemView;
    }
}
