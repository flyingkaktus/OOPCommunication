/*
 * Author:  Maciej Suchowski
 * Date:    03/03/2023
 * Version: 1.0
 */

package com.oopproject.myapp;

import java.io.IOException;

public class AppServer {
    public static void main(String[] args) throws IOException, InterruptedException {

        int port = 5020;

        TCPServer server1 = TCPServer.getInstance(port);

        Thread server1Thread = new Thread(server1);
        server1Thread.start();

        server1Thread.join();
        System.out.println("Server 1 Thread joined.");
    }

}
