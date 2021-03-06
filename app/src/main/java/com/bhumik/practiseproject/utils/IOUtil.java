package com.bhumik.practiseproject.utils;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by bhumik on 18/5/16.
 */
public class IOUtil {

    public static BitmapDrawable toBitmapDrawable(InputStream is)
            throws IOException {
        @SuppressWarnings("deprecation")
        BitmapDrawable bitmapDrawable = new BitmapDrawable(is);
        is.close();
        return bitmapDrawable;
    }

    public static Bitmap toBitmap(InputStream is) throws IOException {
        if (null == is)
            return null;
        return toBitmapDrawable(is).getBitmap();
    }

    public static StringBuilder toStringBuffer(InputStream is)
            throws IOException {
        if (null == is)
            return null;
        BufferedReader in = new BufferedReader(new InputStreamReader(is));
        StringBuilder buffer = new StringBuilder();
        String line = null;
        while ((line = in.readLine()) != null) {
            buffer.append(line).append("\n");
        }
        is.close();
        return buffer;
    }

    /**
     * stream to string
     *
     * @param inputStream InputStream
     * @return Returns string
     * @throws IOException ioexception
     */
    public static String streamToString(InputStream inputStream) throws IOException {
        StringBuilder sBuilder = new StringBuilder();
        String line;
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        while ((line = bufferedReader.readLine()) != null) {
            sBuilder.append(line).append("\n");
        }
        return sBuilder.toString();
    }


    public static String toString(InputStream is) throws IOException {
        if (null == is)
            return null;
        return toStringBuffer(is).toString();
    }

    public static String convertToString(InputStream is, String encoding)
            throws IOException {
        if (null == is)
            return null;
        BufferedReader reader = new BufferedReader(new InputStreamReader(is,
                encoding));
        char cache[] = new char[1024];
        int cacheSize = -1;
        StringBuilder buffer = new StringBuilder();
        while ((cacheSize = reader.read(cache)) != -1) {
            buffer.append(new String(cache, 0, cacheSize));
            cacheSize = reader.read(cache);
        }
        is.close();
        return buffer.toString();
    }

    public static byte[] toByteArray(InputStream is) throws IOException {
        if (null == is)
            return null;
        byte[] cache = new byte[2 * 1024];
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        for (int length; (length = is.read(cache)) != -1; ) {
            buffer.write(cache, 0, length);
        }
        is.close();
        return buffer.toByteArray();
    }

    /**
     * Enter transfer byte []
     *
     * @param inStream InputStream
     * @return Byte array
     */
    public static final byte[] input2byte(InputStream inStream) {
        if (inStream == null)
            return null;
        ByteArrayOutputStream swapStream = new ByteArrayOutputStream();
        byte[] buff = new byte[100];
        int rc = 0;
        try {
            while ((rc = inStream.read(buff, 0, 100)) > 0) {
                swapStream.write(buff, 0, rc);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return swapStream.toByteArray();
    }

    public static String getStreamEncoding(InputStream is) throws IOException {
        BufferedInputStream bis = new BufferedInputStream(is);
        bis.mark(2);
        byte[] first3bytes = new byte[3];
        bis.read(first3bytes);
        bis.reset();
        String encoding = null;
        if (first3bytes[0] == (byte) 0xEF && first3bytes[1] == (byte) 0xBB
                && first3bytes[2] == (byte) 0xBF) {
            encoding = "utf-8";
        } else if (first3bytes[0] == (byte) 0xFF
                && first3bytes[1] == (byte) 0xFE) {
            encoding = "unicode";
        } else if (first3bytes[0] == (byte) 0xFE
                && first3bytes[1] == (byte) 0xFF) {
            encoding = "utf-16be";
        } else if (first3bytes[0] == (byte) 0xFF
                && first3bytes[1] == (byte) 0xFF) {
            encoding = "utf-16le";
        } else {
            encoding = "GBK";
        }
        return encoding;
    }

    public static String getEncodingFromHTML(InputStream is) throws IOException {
        final int FIND_CHARSET_CACHE_SIZE = 4 * 1024;
        BufferedInputStream bis = new BufferedInputStream(is);
        bis.mark(FIND_CHARSET_CACHE_SIZE);
        byte[] cache = new byte[FIND_CHARSET_CACHE_SIZE];
        bis.read(cache);
        bis.reset();
        return getHtmlCharset(new String(cache));
    }

    public static String getHtmlCharset(String content) {
        String encoding = null;
        final String CHARSET_REGX = "<meta.*charset=\"?([a-zA-Z0-9-_/]+)\"?";
        Matcher m = Pattern.compile(CHARSET_REGX).matcher(content);
        if (m.find()) {
            encoding = m.group(1);
        }
        return encoding;
    }
}
