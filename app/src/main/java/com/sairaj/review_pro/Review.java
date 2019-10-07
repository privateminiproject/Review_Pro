package com.sairaj.review_pro;

public class Review {
    String Image,Rating,Review,user_name,location;

    public Review(String image, String rating, String review, String user_name) {
        Image = image;
        Rating = rating;
        Review = review;
        this.user_name = user_name;
    }

    public Review(String image, String rating, String review, String user_name, String location) {
        Image = image;
        Rating = rating;
        Review = review;
        this.user_name = user_name;
        this.location = location;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }

    public String getRating() {
        return Rating;
    }

    public void setRating(String rating) {
        Rating = rating;
    }

    public String getReview() {
        return Review;
    }

    public void setReview(String review) {
        Review = review;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }
}
