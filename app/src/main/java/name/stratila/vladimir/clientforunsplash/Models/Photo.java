package name.stratila.vladimir.clientforunsplash.Models;

public class Photo {
    private Urls urls;

    public Photo(Urls urls) {
        this.urls = urls;
    }

    public Urls getUrls() {
        return urls;
    }

    public void setUrls(Urls urls) {
        this.urls = urls;
    }
}
