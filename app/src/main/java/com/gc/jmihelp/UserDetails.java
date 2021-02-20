package com.gc.jmihelp;

public class UserDetails{
    private String username;
    private String fullname;
    private String imgurl;

    public UserDetails(String fullname, String imgurl) {
        this.fullname = fullname;
        this.imgurl = imgurl;
    }

    public UserDetails(String username, String fullname, String imgurl) {
        this.username = username;
        this.fullname = fullname;
        this.imgurl = imgurl;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getImgurl() {
        return imgurl;
    }

    public void setImgurl(String imgurl) {
        this.imgurl = imgurl;
    }
}
