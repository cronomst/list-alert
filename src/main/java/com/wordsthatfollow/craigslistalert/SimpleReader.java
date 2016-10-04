package com.wordsthatfollow.craigslistalert;

import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;

/**
 *
 * @author kshook
 */
public class SimpleReader {
    public static String read(InputStream in) throws IOException
    {
        String content;
        try (Scanner scanner = new Scanner(in).useDelimiter("\\Z")) {
            content = scanner.next();
        }
        return content;
    }
}
