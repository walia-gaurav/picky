package org.cmu.picky.model;

public class Picky {

    private int id;
    private String title;
    private Photo leftPhoto;
    private Photo rightPhoto;
    private User user;
    private Location location;
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

}
