package com.sairaj.review_pro;

import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ReviewHolder extends RecyclerView.ViewHolder {

    TextView user,review,location;
    ImageView userImage;
    RatingBar ratingBar;



    public ReviewHolder(@NonNull View itemView) {
        super(itemView);

        user=itemView.findViewById(R.id.user);
        review=itemView.findViewById(R.id.review);
        userImage=itemView.findViewById(R.id.userdp);
        ratingBar=itemView.findViewById(R.id.start);
        location=itemView.findViewById(R.id.location);

    }


    public void setUserImage(String string) {
        setUserImage(string);
    }

    public void setLocation(String string) {
        location.setText(string);
    }

    public void setRatingBar(String string) {
        ratingBar.setRating(Float.parseFloat(string));
    }

    public void setReview(String string) {
        review.setText(string);
    }

    public void setUser(String string) {
        user.setText(string);
    }
}
