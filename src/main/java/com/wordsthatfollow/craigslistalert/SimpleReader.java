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
        Scanner scanner = new Scanner(in).useDelimiter("\\Z");
        String content = scanner.next();
        scanner.close();
        in.close();
        return content;
    }
}
