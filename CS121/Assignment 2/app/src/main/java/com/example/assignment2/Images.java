package com.example.assignment2;

public class Images {
    private int id;
    private byte[] image;
    private String title;

    public Images (){
        this.id = id;
        this.image = image;
        this.title = title;
    }

    public int getId(){
        return id;
    }

    public void setId(int id){
        this.id = id;
    }

    public byte[] getImage(){
        return image;
    }

    public void setImage(byte[] image){
        this.image = image;
    }

    public String getTitle(){
        return title;
    }

    public void setTitle(String title){
        this.title = title;
    }
}
