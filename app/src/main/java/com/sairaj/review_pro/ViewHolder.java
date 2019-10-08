package com.sairaj.review_pro;

import android.net.Uri;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ViewHolder extends RecyclerView.ViewHolder {

    public LinearLayout root;
    public TextView product_name;
    public TextView desc;
    public ImageView img;
    public TextView location;
    public TextView userName;



    public ViewHolder(@NonNull View itemView) {
        super(itemView);

        root = itemView.findViewById(R.id.list_root);
        product_name = itemView.findViewById(R.id.list_title);
        desc = itemView.findViewById(R.id.list_desc);
        img=itemView.findViewById(R.id.imageView);
        location=itemView.findViewById(R.id.location);
//        userName=itemView.findViewById(R.id.userNames);

    }


    public void setImg(Uri uri) {
        setImg(uri);

    }

    public void setDesc(String string) {
       desc.setText(string);
    }

    public void setLocation(String string) {
        location.setText(string);
    }

    public void setProduct_name(String string) {
      product_name.setText(string);
    }

//    public void setUserName(String string) {
//        userName.setText(string);
//    }


}
