package com.github.nija123098.evelyn.launcher;

import com.github.nija123098.evelyn.util.CareLess;
import com.github.nija123098.evelyn.util.Log;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ShiftChangeManager {
    private static final Integer PORT = 13123;
    public static void waitForPredecessorShutdown() {
        try {
            Socket socket = new Socket("127.0.0.1", PORT);
            try {
                socket.getInputStream().read();
            } catch (IOException e) {
                Log.log("Predecessor shut down, preceding with startup");
            }
        } catch (IOException e) {
            Log.log("No predecessor found, booting up uninterrupted");
        }
        Thread allowBootUp = new Thread(() -> {
            ServerSocket serverSocket;
            try {
                serverSocket = new ServerSocket(PORT);
                Socket sock = null;
                try {
                    sock = serverSocket.accept();
                } catch (IOException e) {
                    Log.log("IOException reading predecessor detection socket", e);
                }
                Launcher.shutdown(0, 1_000, true);
                CareLess.lessSleep(10_000);
                System.exit(0);
                if (sock != null) CareLess.something(sock::close);// Should never be reached
            } catch (IOException e) {
                Log.log("Issue with shift change", e);
            }
        }, "Shift-Change-Monitor-Thread");
        allowBootUp.setDaemon(false);
        allowBootUp.start();
    }
}
