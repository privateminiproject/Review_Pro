package com.sairaj.review_pro;

public class Model {

    String Email_id,Location,Product_id,Products_Description,Products_Image,Products_Name,TIme,id;

    public Model(String email_id, String location, String product_id, String products_Description, String products_Image, String products_Name, String TIme, String id) {
        Email_id = email_id;
        Location = location;
        Product_id = product_id;
        Products_Description = products_Description;
        Products_Image = products_Image;
        Products_Name = products_Name;
        this.TIme = TIme;
        this.id = id;
    }

    public Model(String email_id, String location, String product_id, String products_Description, String products_Image, String products_Name, String TIme) {
        Email_id = email_id;
        Location = location;
        Product_id = product_id;
        Products_Description = products_Description;
        Products_Image = products_Image;
        Products_Name = products_Name;
        this.TIme = TIme;
    }

    public String getEmail_id() {
        return Email_id;
    }

    public void setEmail_id(String email_id) {
        Email_id = email_id;
    }

    public String getLocation() {
        return Location;
    }

    public void setLocation(String location) {
        Location = location;
    }

    public String getProduct_id() {
        return Product_id;
    }

    public void setProduct_id(String product_id) {
        Product_id = product_id;
    }

    public String getProducts_Description() {
        return Products_Description;
    }

    public void setProducts_Description(String products_Description) {
        Products_Description = products_Description;
    }

    public String getProducts_Image() {
        return Products_Image;
    }

    public void setProducts_Image(String products_Image) {
        Products_Image = products_Image;
    }

    public String getProducts_Name() {
        return Products_Name;
    }

    public void setProducts_Name(String products_Name) {
        Products_Name = products_Name;
    }

    public String getTIme() {
        return TIme;
    }

    public void setTIme(String TIme) {
        this.TIme = TIme;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
