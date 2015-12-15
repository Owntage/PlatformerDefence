package com.example.owntage.es2.common;

/**
 * Created by Алексей on 15.12.2015.
 */
public final class User {
    public final String picUrl;
    public final String name;
    public User(String picUrl,String name)
    {
        this.name=name;
        this.picUrl=picUrl;
    }

    @Override
    public String toString()
    {
        return "CurrentUser[name=\"" + name + "\" picUrl=" + picUrl + "]";
    }
}
