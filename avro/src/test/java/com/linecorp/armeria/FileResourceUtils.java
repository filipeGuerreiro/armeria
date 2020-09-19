package com.linecorp.armeria;

import java.io.File;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;

public class FileResourceUtils {

    private static <T> InputStream getFileFromResourceAsStream(Class<T> tClass, String fileName) {
        ClassLoader classLoader = tClass.getClassLoader();
        InputStream inputStream = classLoader.getResourceAsStream(fileName);
        if (inputStream == null) {
            throw new IllegalArgumentException("file not found! " + fileName);
        } else {
            return inputStream;
        }
    }

    private static <T> File getFileFromResource(Class<T> tClass, String fileName) throws URISyntaxException {
        ClassLoader classLoader = tClass.getClassLoader();
        URL resource = classLoader.getResource(fileName);
        if (resource == null) {
            throw new IllegalArgumentException("file not found! " + fileName);
        } else {
            return new File(resource.toURI());
        }
    }
}
