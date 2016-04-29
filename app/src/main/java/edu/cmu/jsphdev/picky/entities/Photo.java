package edu.cmu.jsphdev.picky.entities;

import edu.cmu.jsphdev.picky.ws.remote.service.BaseService;

/**
 * Entity to hold pictures uploaded by Users.
 */
public class Photo {

    private int id;
    private String url;
    private String base64Image;

    public Photo(String base64Image) {
        this.base64Image = base64Image;
    }

    public Photo() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUrl() {
        return BaseService.IP + "/uploads/" + url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getBase64Image() {
        return base64Image;
    }

    public void setBase64Image(String base64Image) {
        this.base64Image = base64Image;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Photo{");
        sb.append("id=").append(id);
        sb.append(", url='").append(url).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
