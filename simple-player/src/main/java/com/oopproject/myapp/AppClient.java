/*
 * Author:  Maciej Suchowski
 * Date:    03/03/2023
 * Version: 1.0
 */

package com.oopproject.myapp;

import java.io.IOException;
import java.util.Scanner;

public class AppClient {
    public static void main(String[] args) throws IOException, InterruptedException {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter Player name: ");
        String playerName = scanner.nextLine();

        int port = 5020;

        Player player = new Player(playerName, "localhost", port);
        Thread playerThread = new Thread(player);
        playerThread.start();

        try {
            while (true) {
                System.out.print("Put your Message: ");
                String message = scanner.nextLine();
                System.out.print("Put the Receiver: ");
                String receiver = scanner.nextLine();
                player.send(message, receiver);
            }
        } finally {
            scanner.close();
        }
    }
}
