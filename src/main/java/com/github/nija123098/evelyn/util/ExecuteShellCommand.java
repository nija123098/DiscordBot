/**
 * @author Celestialdeath99
 * Sourced from https://www.mkyong.com/java/how-to-execute-shell-command-from-java/
 */

package com.github.nija123098.evelyn.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class ExecuteShellCommand {

    public static void commandToExecute(String command) {
        ExecuteShellCommand obj = new ExecuteShellCommand();
        obj.executeCommand(command);
    }

    private String executeCommand(String command) {
        StringBuffer output = new StringBuffer();
        Process p;
        try {
            p = Runtime.getRuntime().exec(command);
            p.waitFor();
            BufferedReader reader =
                    new BufferedReader(new InputStreamReader(p.getInputStream()));

            String line;
            while ((line = reader.readLine())!= null) {
                output.append(line + "\n");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return output.toString();
    }
}