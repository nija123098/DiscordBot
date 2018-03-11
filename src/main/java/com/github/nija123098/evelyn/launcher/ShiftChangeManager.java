package com.github.nija123098.evelyn.launcher;

import com.github.nija123098.evelyn.util.CareLess;
import com.github.nija123098.evelyn.util.Log;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.concurrent.atomic.AtomicReference;

public class ShiftChangeManager {
    private static final Integer PORT = 13123;
    private static final AtomicReference<ServerSocket> SERVER_SOCKET = new AtomicReference<>();
    private static final AtomicReference<SocketChannel> CONNECTION = new AtomicReference<>();
    private static void setupConnection() throws IOException {
        if (CONNECTION.get() != null && CONNECTION.get().isConnected()) return;
        CONNECTION.set(SocketChannel.open(InetSocketAddress.createUnresolved("127.0.0.1", PORT)));
    }
    static {
        Thread allowBootUp = new Thread(() -> {
            try {
                setupConnection();
                if (CONNECTION.get() != null) {
                    CONNECTION.get().write(ByteBuffer.wrap(new byte[]{1}));
                }
            } catch (IOException e) {

            }
        });
        Runtime.getRuntime().addShutdownHook(allowBootUp);
    }
    public static void waitForPredicesorShutdown() {
        try {
            setupConnection();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static boolean waitForReplacement(long timeout){
        try {
            setupConnection();
            ByteBuffer byteBuffer = ByteBuffer.allocate(1);
            int length;
            while (true) {
                length = CONNECTION.get().read(byteBuffer);
                if (length != 0) return true;
                timeout -= 500;
                CareLess.lessSleep(500);
            }
        } catch (IOException e) {
            Log.log("Unexpected read exception from ");
            return false;
        }
    }
}
