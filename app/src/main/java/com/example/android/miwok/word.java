package com.example.android.miwok;

import android.widget.ImageView;

public class word {
    private String miworktranslation;
    private String defaulttranslation;
    private int Resourceid;
    private int musicid;
    private static final int NO_IMAGE_PROVIDED=-1;

    public word(String miworktranslation, String defaulttranslation,int musicid) {
        this.defaulttranslation = defaulttranslation;
        this.miworktranslation = miworktranslation;
        this.musicid=musicid;
        this.Resourceid=NO_IMAGE_PROVIDED;
    }

    public word(String miworktranslation, String defaulttranslation, int Resourceid,int musicid) {
        this.defaulttranslation = defaulttranslation;
        this.miworktranslation = miworktranslation;
        this.Resourceid = Resourceid;
        this.musicid=musicid;
    }

    public String getMiworktranslation() {
        return this.miworktranslation;
    }

    public String getDefaulttranslation() {
        return this.defaulttranslation;
    }

    public int getResourceid() {
        return this.Resourceid;
    }

    public Boolean hasImage(){
        if(Resourceid == NO_IMAGE_PROVIDED)
        {
            return false;
        }
        return true;
    }

    public int getMusicid(){
        return this.musicid;
    }
}
