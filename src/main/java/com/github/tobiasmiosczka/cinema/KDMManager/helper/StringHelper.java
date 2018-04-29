package com.github.tobiasmiosczka.cinema.KDMManager.helper;

import org.apache.commons.net.util.Charsets;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class StringHelper {
    public static boolean isBlank(final CharSequence cs) {
        int strLen;
        if (cs == null || (strLen = cs.length()) == 0) {
            return true;
        }
        for (int i = 0; i < strLen; i++) {
            if (!Character.isWhitespace(cs.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    public static InputStream toInputStream(String input) {
        byte[] bytes = input.getBytes(Charsets.toCharset(StandardCharsets.UTF_8.name()));
        return new ByteArrayInputStream(bytes);
    }
}