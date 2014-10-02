package com.chriswallace.mobileprotolab3.Database;

/**
 * Created by cwallace on 9/15/14.
 * CLASS FOR INFO RECEIVED BACK FROM DATABASE
 */
public class ChatEntry {



    public String user;
    public String Message;
    public String Date;

    public ChatEntry(String user, String Message, String Date){
        this.user = user;
        this.Message = Message;
        this.Date = Date;
    }
}
