package com.github.kaaz.emily.util;

import java.io.*;

/**
 * @author Kaaz
 */
public class StreamGobler extends Thread {
    private InputStream stream;
    private PrintStream printStream;
    public StreamGobler(InputStream stream, PrintStream printStream) {
        this.stream = stream;
        this.printStream = printStream;
    }
    @Override
    public void run(){
        BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
        String in;
        try {
            while ((in = reader.readLine()) != null){
                this.printStream.println(in);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
