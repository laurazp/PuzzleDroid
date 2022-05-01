package com.ultimapieza.puzzledroid;

import java.util.UUID;

public class PhotoId {
    public String id;

    public void setId(String photo){
        this.id= UUID.randomUUID().toString();
    }
    public String getPhotoId(){
        return id;
    }

}
