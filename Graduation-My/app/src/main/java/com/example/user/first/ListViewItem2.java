package com.example.user.first;

import android.graphics.drawable.Drawable;

/**
 * Created by 명윤 on 2017-12-27.
 */

public class ListViewItem2 {
    private Drawable iconDrawable;
    private String titleStr;
    private String descStr;

    public void setIcon(Drawable icon){
        iconDrawable = icon;
    }
    public void setTitle(String title){
        titleStr= title;
    }
    public void setDesc(String desc){
        descStr = desc;
    }
    public Drawable getIcon(){
        return this.iconDrawable;
    }
    public String getTitle(){
        return this.titleStr;
    }
    public String getDesc(){
        return this.descStr;
    }
}
