package org.originmc.hub.util;

public final class NumUtils {
    public static int parseInt(String string) {
        try {
            return Integer.parseInt(string);
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    public static byte parseByte(String string) {
        try {
            return Byte.parseByte(string);
        } catch (NumberFormatException e) {
            return 0;
        }
    }
}
