package it.digirolamopescetti.towatch;

public class Movie {
    private String URL;                 //movie's URL
    private String webSite;            //movie's Platform
    private String name;                //movie's name
    private Boolean isFavourite;          //if the movie has the star or not
    private String image;               //movie's image
    private int status;
    /*
    STATUS:
        0 -> RED -> to watch
        1 -> YELLOW -> watching
        2 -> GREEN -> watched
     */

    public Movie(String URL, String platform, String image, String name) {
        this.URL = URL;
        this.webSite = platform;
        this.image = image;
        this.isFavourite = false;
        this.status = 0;
        this.name = name;
    }

    public String getURL() {
        return URL;
    }

    public void setURL(String URL) {
        this.URL = URL;
    }

    public String getPlatform() {
        return webSite;
    }

    public void setPlatform(String platform) {
        this.webSite = platform;
    }

    public Boolean getFavourite() {
        return isFavourite;
    }

    public void setFavourite(Boolean favourite) {
        isFavourite = favourite;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
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
                "URL='" + URL + '\'' +
                ", webSite='" + webSite + '\'' +
                ", name='" + name + '\'' +
                ", isFavourite=" + isFavourite +
                ", image='" + image + '\'' +
                ", status=" + status +
                '}';
    }
}
