package com.github.nija123098.evelyn.BotConfig;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import static com.github.nija123098.evelyn.util.PlatformDetector.PathEnding;

/**
 * @author Celestialdeath99
 */

public class VerifyConfig {

    public static void main() throws IOException {
        Properties Config = new Properties();
        String FILEPATH = System.getProperty("user.dir");

        try {
            Config.load(new FileInputStream(FILEPATH + PathEnding() + "Bot-Config.cfg"));
        } catch (IOException e) {
            System.out.println("There is not config present. Generating a new one now. Please re-run after filling out the config");
            CreateConfig.main();
            System.exit(-1);
        }
    }
}
