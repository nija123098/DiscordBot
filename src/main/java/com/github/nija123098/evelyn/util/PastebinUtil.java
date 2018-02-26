package com.github.nija123098.evelyn.util;

import com.github.kennedyoliveira.pastebin4j.*;
import com.github.nija123098.evelyn.botconfiguration.ConfigProvider;

/**
 * @author nija123098
 */
public class PastebinUtil {

    private final static String devKey = ConfigProvider.PASTE_BIN_SETTINGS.devKey();
    private final static String userName = ConfigProvider.PASTE_BIN_SETTINGS.userName();
    private final static String password = ConfigProvider.PASTE_BIN_SETTINGS.password();

    private final static PasteBin pasteBin = new PasteBin(new AccountCredentials(devKey, userName, password));

    //  Basic creation
    private final static Paste paste = new Paste();

    /**
     * Sends a string to be hosted on hastebin.com.
     *
     * @param message the text to send to hastebind.
     * @return the key to find the post.
     */
    public static String postToPastebin(String title, String message) {

        paste.setTitle(title);
        paste.setExpiration(PasteExpiration.ONE_DAY);
        paste.setHighLight(PasteHighLight.Java);
        paste.setVisibility(PasteVisibility.UNLISTED);
        paste.setContent(message);

        try {
            return pasteBin.createPaste(paste);
        } catch (Exception e) {
            return "pastebin failed us";
        }
    }
}
