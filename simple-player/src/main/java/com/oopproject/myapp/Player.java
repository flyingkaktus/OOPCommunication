/*
 * Author:  Maciej Suchowski
 * Date:    03/03/2023
 * Version: 1.0
 */

package com.oopproject.myapp;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.HashMap;

public class Player extends Client {

    HashMap<String, Integer> counterMap = new HashMap<>();

    public Player(String name, String address, int port) throws UnknownHostException, IOException {
        super(name, address, port);
    }

    public void modifyCounter(String name) {
        if (counterMap.containsKey(name)) {
            int currentCounter = counterMap.get(name);
            counterMap.put(name, currentCounter + 1);
        } else {
            counterMap.put(name, 1);
        }
    }

    @Override
    public void reaction(Message messageFromServer) {
        modifyCounter(messageFromServer.getSenderName());
        System.out.println("Player " + name + " received message: " + messageFromServer.getMessage() + " from "
                + messageFromServer.getSenderName() + ". Counter: "
                + counterMap.get(messageFromServer.getSenderName()));
    }

    @Override
    public void closeCondition() {
        for (String key : counterMap.keySet()) {
            if (counterMap.get(key) == 10) {
                System.out
                        .println("Player " + name + " has received 10 messages from " + key + ". Closing connection.");
                disconnect();
            }
        }
    }

}
