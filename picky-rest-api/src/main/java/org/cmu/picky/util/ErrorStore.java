package org.cmu.picky.util;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class ErrorStore {
    private static final Map<String, String> ERRORS;

    static {
        Map<String, String> tempMap = new HashMap<String, String>();
        ERRORS = Collections.unmodifiableMap(tempMap);
    }

    public static String getMessage(String errorCode, String[] arguments) {
        String message = ERRORS.get(errorCode);
        if (message != null) {
            String formattedMsg = String.format(message, arguments).trim();
            if (formattedMsg.endsWith(":")) {
                formattedMsg.substring(0, formattedMsg.length() - 1);
            }
            return formattedMsg;
        }
        return "Unknown error";
    }
}
