package com.linjin.firebasemessagingtest;

import com.google.firebase.database.IgnoreExtraProperties;

/**
 * Created by evan on 28/09/16.
 */

@IgnoreExtraProperties
public class ChatMessage {
    public String uid;
    public String msg;
    public long epoch;

    public ChatMessage() {

    }

    public ChatMessage(String uid, String msg) {
        this.uid = uid;
        this.msg = msg;
        epoch = System.currentTimeMillis();
    }
}
