package com.example.nottingbot;

/**
 * @author Junhan LIU
 * @version 1.0
 *
 * Entity of the chat message in chatting page
 */
public class Message {
    public static final int TYPE_RECEIVED = 0;
    public static final int TYPE_SENT = 1;
    private String content;
    private int type;

    /**
     * Constructor without parameters of Message
     */
    public Message() {
    }

    /**
     * set the content of the message
     * @param content string
     */
    public void setContent(String content) {
        this.content = content;
    }

    /**
     * Set the type of the message
     * @param type type of the message
     */
    public void setType(int type) {
        this.type = type;
    }

    /**
     * get the content
     * @return content
     */
    public String getContent() {
        return content;
    }

    /**
     * get the type of the content
     * @return the type of the content
     */
    public int getType() {
        return type;
    }
}
