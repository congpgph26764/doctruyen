package com.example.assignmnet_networking.model;

public class BinhLuan {

    private String id_truyen;
    private String id_nguoidung;
    private String avatar;
    private String name;
    private String content;
    private String date;

    public BinhLuan() {
    }

    public BinhLuan(String id_truyen, String id_nguoidung, String avatar, String name, String content, String date) {
        this.id_truyen = id_truyen;
        this.id_nguoidung = id_nguoidung;
        this.avatar = avatar;
        this.name = name;
        this.content = content;
        this.date = date;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId_truyen() {
        return id_truyen;
    }

    public void setId_truyen(String id_truyen) {
        this.id_truyen = id_truyen;
    }

    public String getId_nguoidung() {
        return id_nguoidung;
    }

    public void setId_nguoidung(String id_nguoidung) {
        this.id_nguoidung = id_nguoidung;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
