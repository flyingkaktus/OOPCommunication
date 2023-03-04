/*
 * Author:  Maciej Suchowski
 * Date:    03/03/2023
 * Version: 1.0
 */

package com.oopproject.myapp;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public abstract class Client implements Runnable {

    protected int id;
    protected String name;
    protected String address;
    protected int port;
    protected Socket socket = null;
    protected PrintWriter out = null;
    private ObjectOutputStream objectOutputStream;
    private ObjectInputStream objectInputStream;

    public Client(String name, String address, int port) {
        this.name = name;
        this.address = address;
        this.port = port;
        id = hashCode();
        try {
            connect();
        } catch (IOException e) {
            System.out.println("Client " + id + " could not create objectOutputStream.");
        }
    }

    public void connect() throws UnknownHostException, IOException {
        socket = new Socket(address, port);
        objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
        objectInputStream = new ObjectInputStream(socket.getInputStream());
    }

    public void send(String messageStr, String receiver) throws UnknownHostException, IOException {
        try {
            if (socket == null || socket.isClosed()) {
                connect();
            }
            out = new PrintWriter(socket.getOutputStream(), true);
            Message message = new Message(messageStr, name, receiver);
            objectOutputStream.writeObject(message);
            objectOutputStream.flush();
        } catch (Exception e) {
            System.out.println("Client " + id + " could not connect to server.");
        }
    }

    public void disconnect() {
        try {
            if (socket != null && !socket.isClosed()) {
                socket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        socket = null;
    }

    public abstract void reaction(Message messageFromServer);

    public abstract void closeCondition();

    public void run() {
        try {
            Message messageFromServer;
            while ((messageFromServer = (Message) objectInputStream.readObject()) != null) {
                try {
                    if (messageFromServer != null) {
                        if (messageFromServer.getMessage().equals("IDENTIFICATION_REQUEST")) {
                            System.out.println("\nGot identification request from server.");
                            send(name, "SERVER_RESPONSE");
                        }
                        reaction(messageFromServer);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                closeCondition();
            }
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Client " + id + " could not read from server.");
        }
    }

}
