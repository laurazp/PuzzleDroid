package com.ultimapieza.puzzledroid;

import java.util.concurrent.atomic.AtomicInteger;

public class PhotoId {
    static AtomicInteger nextId = new AtomicInteger();
    public String id;

    public void setId(String id){
        this.id=id;
    }
    public String getPhotoId(String string) {
        id = String.valueOf(nextId.incrementAndGet());
        return id;
    }

}
