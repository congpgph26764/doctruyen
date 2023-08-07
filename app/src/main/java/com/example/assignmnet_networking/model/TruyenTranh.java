package com.example.assignmnet_networking.model;

public class TruyenTranh {

    private String _id;
    private String name;
    private String description;
    private String author;
    private String year;
    private String thumbnail;

    public TruyenTranh() {
    }

    public TruyenTranh(String _id, String name, String description, String author, String year, String thumbnail) {
        this._id = _id;
        this.name = name;
        this.description = description;
        this.author = author;
        this.year = year;
        this.thumbnail = thumbnail;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    @Override
    public String toString() {
        return "TruyenTranh{" +
                "_id='" + _id + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", author='" + author + '\'' +
                ", year='" + year + '\'' +
                ", thumbnail='" + thumbnail + '\'' +
                '}';
    }
}
