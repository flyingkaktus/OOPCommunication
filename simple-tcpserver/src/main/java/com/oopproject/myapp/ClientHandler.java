/*
 * Author:  Maciej Suchowski
 * Date:    03/03/2023
 * Version: 1.0
 */

package com.oopproject.myapp;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;

import java.net.SocketTimeoutException;
import java.util.LinkedList;

public class ClientHandler implements Runnable {

    public final ObjectInputStream objectInputStream;
    public final ObjectOutputStream objectOutputStream;
    private String name = "UNIDENTIFIED";
    LinkedList<Message> messageStoragePending = new LinkedList<Message>();

    public ClientHandler(Socket clientSocket) throws IOException {
        this.objectInputStream = new ObjectInputStream(clientSocket.getInputStream());
        this.objectOutputStream = new ObjectOutputStream(clientSocket.getOutputStream());
        System.out.println("ClientHandler created.");
    }

    private void requestIdentification() throws IOException {
        objectOutputStream.writeObject(new Message("IDENTIFICATION_REQUEST", "SERVER", "CLIENT"));
        objectOutputStream.flush();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public void run() {
        try {

            Message messageFromClient;
            requestIdentification();
            while ((messageFromClient = (Message) objectInputStream.readObject()) != null) {

                if (messageFromClient.getReceiverName().equals("SERVER_RESPONSE")) {
                    setName(messageFromClient.getSenderName());
                    System.out.println("Client " + getName() + " identified.");
                } else {
                    System.out.println(
                            "Message from: " + messageFromClient.getSenderName() + " with message: "
                                    + messageFromClient.toString() + " to " + messageFromClient.getReceiverName()
                                    + " received.");
                    messageStoragePending.add(messageFromClient);
                }
            }

        } catch (SocketTimeoutException e) {
            System.out.println("Socket Timeout");
        } catch (EOFException e) {
            System.out.println("End of File");
        } catch (SocketException e) {
            if (e.getMessage().equals("Connection reset")) {
                System.err.println("Verbindung zum Server wurde zurückgesetzt.");
            } else {
                System.err.println("SocketException aufgetreten: " + e.getMessage());
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
