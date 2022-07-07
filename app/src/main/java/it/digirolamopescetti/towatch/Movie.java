package it.digirolamopescetti.towatch;

//USELESS CLASS

import android.graphics.Bitmap;

public class Movie {
    private String url;                 //movie's URL
    private String website;            //movie's Platform
    private String name;                //movie's name
    private Boolean favourite;          //if the movie has the star or not
    private Bitmap img;               //movie's image
    private int status;
    /*
    STATUS:
        0 -> RED -> to watch
        1 -> YELLOW -> watching
        2 -> GREEN -> watched
     */

    //class that describes how a movie is made (simple class lol)
    public Movie(String name, String url, String webSite, Boolean favourite, int status, Bitmap image) {
        this.url = url;
        this.website = webSite;
        this.img = image;
        this.favourite = favourite;
        this.status = status;
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String platform) {
        this.website = platform;
    }

    public Boolean getFavourite() {
        return favourite;
    }

    public void setFavourite(Boolean favourite) {
        this.favourite = favourite;
    }

    public Bitmap getImg() {
        return img;
    }

    public void setImg(Bitmap img) {
        this.img = img;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Movie{" +
                "URL='" + url + '\'' +
                ", webSite='" + website + '\'' +
                ", name='" + name + '\'' +
                ", isFavourite=" + favourite +
                ", image='" + img + '\'' +
                ", status=" + status +
                '}';
    }
}
