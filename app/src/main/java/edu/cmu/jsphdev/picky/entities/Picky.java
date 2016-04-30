package edu.cmu.jsphdev.picky.entities;

/**
 * Entity to hold the actual PICKY fields.
 */
public class Picky {

    private int id;
    private String title;
    private Photo leftPhoto;
    private Photo rightPhoto;
    private User user;
    private Location location;
    private String expirationDate;
    private int leftVotes;
    private int rightVotes;

    public Picky() {}

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Photo getLeftPhoto() {
        return leftPhoto;
    }

    public void setLeftPhoto(Photo leftPhoto) {
        this.leftPhoto = leftPhoto;
    }

    public Photo getRightPhoto() {
        return rightPhoto;
    }

    public void setRightPhoto(Photo rightPhoto) {
        this.rightPhoto = rightPhoto;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public int getLeftVotes() {
        return leftVotes;
    }

    public void setLeftVotes(int leftVotes) {
        this.leftVotes = leftVotes;
    }

    public int getRightVotes() {
        return rightVotes;
    }

    public void setRightVotes(int rightVotes) {
        this.rightVotes = rightVotes;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(String expirationDate) {
        this.expirationDate = expirationDate;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Picky{");
        sb.append("id=").append(id);
        sb.append(", title='").append(title).append('\'');
        sb.append(", leftPhoto=").append(leftPhoto);
        sb.append(", rightPhoto=").append(rightPhoto);
        sb.append(", user=").append(user);
        sb.append(", location=").append(location);
        sb.append(", leftVotes=").append(leftVotes);
        sb.append(", rightVotes=").append(rightVotes);
        sb.append('}');
        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        return this.id == ((Picky) o).getId();
    }
}
