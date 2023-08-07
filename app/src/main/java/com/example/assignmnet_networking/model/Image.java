package com.example.assignmnet_networking.model;

public class Image {
    private String _id;
    private String image;
    private String id_truyen;

    public Image() {
    }

    public Image(String _id, String image, String id_truyen) {
        this._id = _id;
        this.image = image;
        this.id_truyen = id_truyen;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getId_truyen() {
        return id_truyen;
    }

    public void setId_truyen(String id_truyen) {
        this.id_truyen = id_truyen;
    }
}
