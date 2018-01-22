package com.mydomain.yyjp.myflickrfindr.services.apihelper;
/*
 * Description: Search photo properties
 * Created by jmonani on 17/1/18
 */

public class SearchPhoto {
    private String id;
    private String owner;
    private String secret;
    private String server;
    private Integer farm;
    private String title;
    private Integer isPublic;
    private Integer isFriend;
    private Integer isFamily;
    private int pageNumber;
    private String photoUrl;
    private int total;

    /*
     * get and set methods
     */
    public Integer getFarm() {
        return farm;
    }

    public String getId() {
        return id;
    }

    public String getSecret() {
        return secret;
    }

    public String getServer() {
        return server;
    }

    public String getOwner() {
        return owner;
    }

    public String getTitle() {
        return title;
    }

    public Integer getIsPublic() {
        return isPublic;
    }

    public void setPageNumber(int pageNumber) {
        this.pageNumber = pageNumber;
    }

    public int getPageNumber() {
        return pageNumber;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public void setServer(String server) {
        this.server = server;
    }

    public void setFarm(Integer farm) {
        this.farm = farm;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setIsPublic(Integer isPublic) {
        this.isPublic = isPublic;
    }

    public void setIsFriend(Integer isFriend) {
        this.isFriend = isFriend;
    }

    public void setIsFamily(Integer isFamily) {
        this.isFamily = isFamily;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public int getTotal() {
        return total;
    }
    public void setTotal(int total) {
        this.total = total;
    }
}
