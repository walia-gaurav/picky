package edu.cmu.jsphdev.picky.entities;

/**
 * Entity to hold pictures uploaded by Users.
 */
public class Photo {

    private int id;
    private String url;
    private byte[] binaryImage;

    public Photo(byte[] binaryImage) {
        this.binaryImage = binaryImage;
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
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
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
