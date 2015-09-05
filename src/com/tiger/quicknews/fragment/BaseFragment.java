
package com.tiger.quicknews.fragment;

import android.support.v4.app.Fragment;
import android.view.View;

import com.tiger.quicknews.activity.BaseActivity;
import com.tiger.quicknews.http.UrlUtils;
import com.tiger.quicknews.utils.StringUtils;

public class BaseFragment extends Fragment {
    public View mView;
    /**
     * 当前页
     */
    public int currentPagte = 1;

    public BaseActivity getMyActivity() {
        return (BaseActivity) getActivity();
    }

    public String getNewsUrl(String index) {
        String urlString = UrlUtils.TopUrl + UrlUtils.TopId + "/" + index + UrlUtils.endUrl;
        return urlString;
    }

    public String getCommonUrl(String index, String itemId) {
        String urlString = UrlUtils.CommonUrl + itemId + "/" + index + UrlUtils.endUrl;
        return urlString;
    }

    public String getLocalUrl(String index, String itemId) {
        String urlString = UrlUtils.Local + itemId + "/" + index + UrlUtils.endUrl;
        return urlString;
    }

    public String getFangUrl(String index, String itemId) {
        String urlString = UrlUtils.FangChan + itemId + "/" + index + UrlUtils.endUrl;
        return urlString;
    }

    public String getPhotosUrl(String index) {
        String urlString = UrlUtils.TuJi + index + UrlUtils.TuJiEnd;
        return urlString;
    }

    public String getReDianPicsUrl(String index) {
        String urlString = UrlUtils.TuPianReDian + index + UrlUtils.TuJiEnd;
        return urlString;
    }

    public String getDuJiaPicsUrl(String index) {
        String urlString = UrlUtils.TuPianDuJia + index + UrlUtils.TuJiEnd;
        return urlString;
    }

    public String getMingXingPicsUrl(String index) {
        String urlString = UrlUtils.TuPianMingXing + index + UrlUtils.TuJiEnd;
        return urlString;
    }

    public String getTiTanPicsUrl(String index) {
        String urlString = UrlUtils.TuPianTiTan + index + UrlUtils.TuJiEnd;
        return urlString;
    }

    public String getMeiTuPicsUrl(String index) {
        String urlString = UrlUtils.TuPianMeiTu + index + UrlUtils.TuJiEnd;
        return urlString;
    }

    public String getSinaJingXuan(String index) {
        String urlString = UrlUtils.JINGXUAN_ID + index;
        return urlString;
    }

    public String getSinaQuTu(String index) {
        String urlString = UrlUtils.QUTU_ID + index;
        return urlString;
    }

    public String getSinaMeiTu(String index) {
        String urlString = UrlUtils.MEITU_ID + index;
        return urlString;
    }

    public String getSinaGuShi(String index) {
        String urlString = UrlUtils.GUSHI_ID + index;
        return urlString;
    }

    // ��Ƶ http://c.3g.163.com/nc/video/list/V9LG4B3A0/n/10-10.html
    public String getVideoUrl(String index, String videoId) {
        String urlString = UrlUtils.Video + videoId + UrlUtils.VideoCenter + index + UrlUtils.videoEndUrl;
        return urlString;
    }

    public boolean isNullString(String imgUrl) {

        if (StringUtils.isEmpty(imgUrl)) {
            return true;
        }
        return false;
    }
}
