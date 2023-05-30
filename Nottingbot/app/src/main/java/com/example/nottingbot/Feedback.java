package com.example.nottingbot;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Feedback class of one feedback query
 *
 * @author Junhan LIU
 */
public class Feedback {
    /**
     * Constructor function of feedback
     *
     * @param content the content of the feedback
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    public Feedback(String content) {
        Date date = new Date();
        this.content = content;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        this.date = simpleDateFormat.format(date);
    }

    /**
     * Feedback content
     */
    private String content;
    /**
     * creation date, in the format of dd/MM/yyyy HH:mm:ss Z
     * with time zone
     */
    private String date;

    /**
     * getter function of content of the feedback
     * @return content of the feedback
     */
    public String getContent() {
        return content;
    }

    /**
     * set the content of the feedback
     * @param content content of the feedback
     */
    public void setContent(String content) {
        this.content = content;
    }

    /**
     * get the creation date of the feedback
     * @return date
     */
    public String getDate() {
        return date;
    }
    /**
     * set the creation date of the feedback
     * @param date creation date
     */
    public void setDate(String date) {
        this.date = date;
    }
}
