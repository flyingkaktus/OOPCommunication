/*
 * Author:  Maciej Suchowski
 * Date:    03/03/2023
 * Version: 1.0
 */
package com.oopproject.myapp;

import java.io.Serializable;

public class Message implements Serializable {

    private static final long serialVersionUID = 1L;

    private String receiverName;
    private String senderName;
    private String message;

    public Message(String message, String senderName, String receiverName) {
        this.message = message;
        this.receiverName = receiverName;
        this.senderName = senderName;
    }

    public String getSenderName() {
        return senderName;
    }

    public String getReceiverName() {
        return receiverName;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return message;
    }
}
