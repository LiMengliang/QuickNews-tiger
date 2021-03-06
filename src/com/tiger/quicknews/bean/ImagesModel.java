
package com.tiger.quicknews.bean;

import java.util.List;

public class ImagesModel extends BaseModel {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    /**
     * docid
     */
    private String docid;
    /**
     * title
     */
    private String title;
    /**
     * ͼƬ��
     */
    private List<ImageDetailModel> imgList;

    public String getDocid() {
        return docid;
    }

    public void setDocid(String docid) {
        this.docid = docid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<ImageDetailModel> getImgList() {
        return imgList;
    }

    public void setImgList(List<ImageDetailModel> imgList) {
        this.imgList = imgList;
    }
}
