package it.digirolamopescetti.towatch;

public class Movie {
    private String URL;                 //movie's URL
    private String platform;            //movie's Platform
    private Boolean isFavourite;          //if the movie has the star or not
    private String image;               //movie's image
    private int status;
    /*
    STATUS:
        0 -> RED -> to watch
        1 -> YELLOW -> watching
        2 -> GREEN -> watched
     */

    public Movie(String URL, String platform, String image) {
        this.URL = URL;
        this.platform = platform;
        this.image = image;
        this.isFavourite = false;
        this.status = 0;
    }

    public String getURL() {
        return URL;
    }

    public void setURL(String URL) {
        this.URL = URL;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
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

    @Override
    public String toString() {
        return "Movie{" +
                "URL='" + URL + '\'' +
                ", platform='" + platform + '\'' +
                ", isFavourite=" + isFavourite +
                ", image='" + image + '\'' +
                ", status=" + status +
                '}';
    }
}
