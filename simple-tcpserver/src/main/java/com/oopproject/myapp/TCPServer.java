/*
 * Author:  Maciej Suchowski
 * Date:    03/03/2023
 * Version: 1.0
 */

package com.oopproject.myapp;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.Iterator;
import java.util.LinkedList;

public class TCPServer implements Runnable {

    private static TCPServer firstInstance = null;
    private LinkedList<ClientHandler> clientHandlers = new LinkedList<>();
    private ServerSocket serverSocket = null;
    private Socket clientSocket = null;

    private TCPServer(int port) throws IOException {
        System.out.println("Server " + this.hashCode() + " created.");
        serverSocket = new ServerSocket(port);
        serverSocket.setSoTimeout(1000);
    }

    // Server Instance is a Singleton to ensure that only one Server is running
    public static TCPServer getInstance(int port) throws IOException {
        synchronized (TCPServer.class) {
            if (firstInstance == null) {
                firstInstance = new TCPServer(port);
            }
        }
        return firstInstance;
    }

    public void sendPendingMessages2() {
        for (ClientHandler clientHandler : clientHandlers) {
            for (Message message : clientHandler.messageStoragePending) {
                for (ClientHandler clientHandler2 : clientHandlers) {
                    if (clientHandler2.getName().equals(message.getReceiverName())) {
                        try {
                            clientHandler2.objectOutputStream.writeObject(message);
                            clientHandler2.objectOutputStream.flush();
                            clientHandler.messageStoragePending.remove(message);
                        } catch (IOException e) {
                            e.printStackTrace();
                            System.out.println("Failed to send message to " + message.getReceiverName());
                        }
                    }
                }
            }
        }
    }

    public void sendPendingMessages() {
        for (ClientHandler clientHandler : clientHandlers) {
            Iterator<Message> messageStoragePending = clientHandler.messageStoragePending.iterator();
            while (messageStoragePending.hasNext()) {
                Message message = messageStoragePending.next();
                for (ClientHandler clientHandler2 : clientHandlers) {
                    if (clientHandler2.getName().equals(message.getReceiverName())) {
                        try {
                            clientHandler2.objectOutputStream.writeObject(message);
                            clientHandler2.objectOutputStream.flush();
                            messageStoragePending.remove();
                        } catch (IOException e) {
                            e.printStackTrace();
                            System.out.println("Failed to send message to " + message.getReceiverName());
                        }
                    }
                }
            }
        }
    }

    public void listen() {
        try {
            clientSocket = serverSocket.accept();
            ClientHandler clientHandler = new ClientHandler(clientSocket);
            clientHandlers.add(clientHandler);
            Thread thread = new Thread(clientHandler);
            thread.start();
        } catch (SocketTimeoutException e) {
            // System.out.println("Socket Timeout");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        while (true) {
            listen();
            sendPendingMessages();
        }

    }

}
