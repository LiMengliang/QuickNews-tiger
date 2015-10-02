
package com.tiger.quicknews.http.json;

import android.content.Context;

import com.tiger.quicknews.bean.ImageDetailModel;
import com.tiger.quicknews.bean.ImagesModel;
import com.tiger.quicknews.bean.NewsDigestModel;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class NewListJson extends JsonPacket {

    public static NewListJson newListJson;

    public List<NewsDigestModel> newModles;

    public NewListJson(Context context) {
        super(context);
    }

    public static NewListJson instance(Context context) {
        if (newListJson == null) {
            newListJson = new NewListJson(context);
        }
        return newListJson;
    }

    public List<NewsDigestModel> readJsonNewModles(String res, String value) {
        newModles = new ArrayList<NewsDigestModel>();
        try {
            if (res == null || res.equals("")) {
                return null;
            }
            NewsDigestModel newModle = null;
            JSONObject jsonObject = new JSONObject(res);
            JSONArray jsonArray = jsonObject.getJSONArray(value);
            // if (isFirst) {
            // for (int i = 0; i < 4; i++) {
            // JSONObject js = jsonArray.getJSONObject(i);
            // newModle = readNewModle(js);
            // newModles.add(newModle);
            // }
            // }

            for (int i = 1; i < jsonArray.length(); i++) {
                newModle = new NewsDigestModel();
                JSONObject js = jsonArray.getJSONObject(i);
//                if (js.has("skipType") && js.getString("skipType").equals("special")) {
//                    continue;
//                }
//                if (js.has("TAGS") && !js.has("TAG")) {
//                    continue;
//                }
                if (js.has("imgextra")) {
                    newModle.setTitle(getString("title", js));
                    newModle.setDocid(getString("docid", js));
                    ImagesModel imagesModle = new ImagesModel();
                    List<ImageDetailModel> list;
                    list = readImgList(js.getJSONArray("imgextra"));
                    list.add(new ImageDetailModel(getString("imgsrc", js),
                    		getString("alt", js),
                    		getString("ref", js)));
                    imagesModle.setImgList(list);
                    newModle.setImagesModle(imagesModle);
                } else {
                    newModle = readNewModle(js);
                }
                newModles.add(newModle);
            }
        } catch (Exception e) {
        	System.out.println(e);
        } finally {
            System.gc();
        }
        return newModles;
    }

    /**
     * 解析图片集
     * 
     * @param jsonArray
     * @return
     * @throws Exception
     */
    public List<ImageDetailModel> readImgList(JSONArray jsonArray) throws Exception {
        List<ImageDetailModel> imgList = new ArrayList<ImageDetailModel>();

        for (int i = 0; i < jsonArray.length(); i++) {
            imgList.add(new ImageDetailModel(getString("imgsrc", jsonArray.getJSONObject(i)),
            		getString("alt", jsonArray.getJSONObject(i)),
            		getString("ref", jsonArray.getJSONObject(i))));
        }

        return imgList;
    }

    /**
     * 获取图文列表
     * 
     * @param jsonObject
     * @return
     * @throws Exception
     */
    public NewsDigestModel readNewModle(JSONObject jsonObject) throws Exception {
        NewsDigestModel newModle = null;

        String docid = "";
        String title = "";
        String digest = "";
        String imgsrc = "";
        String source = "";
        String ptime = "";
        String tag = "";

        docid = getString("docid", jsonObject);
        title = getString("title", jsonObject);
        digest = getString("digest", jsonObject);
        imgsrc = getString("imgsrc", jsonObject);
        source = getString("source", jsonObject);
        ptime = getString("ptime", jsonObject);
        tag = getString("TAG", jsonObject);

        newModle = new NewsDigestModel();

        newModle.setDigest(digest);
        newModle.setDocid(docid);
        newModle.setImgsrc(imgsrc);
        newModle.setTitle(title);
        newModle.setPtime(ptime);
        newModle.setSource(source);
        newModle.setTag(tag);

        return newModle;
    }

}
