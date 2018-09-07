package ru.innopolis.stc12.refactoring;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

public class Loader extends ClassLoader {
    public Loader(ClassLoader parent) {
        super(parent);
    }

    public Class<?> loadClass(String className, String equalsClassName, String filePath) throws ClassNotFoundException {
        if (className.equals(equalsClassName)) {
            byte[] classData = new byte[0];
            try {
                classData = getClassData(filePath);
            } catch (IOException e) {
                e.printStackTrace();
                return loadClass(className);
            }
            return defineClass(className, classData, 0, classData.length);
        }
        return loadClass(className);
    }

    private byte[] getClassData(String filePath) throws IOException {
        URL url = new URL(filePath);
        URLConnection urlConnection = url.openConnection();
        InputStream inputStream = urlConnection.getInputStream();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try {
            ReadData(inputStream, byteArrayOutputStream);
        } catch (IOException ex) {
            inputStream.close();
            throw ex;
        }
        return byteArrayOutputStream.toByteArray();
    }

    private void ReadData(InputStream inputStream, ByteArrayOutputStream byteArrayOutputStream) throws IOException {
        int data = inputStream.read();
        while (data != -1) {
            byteArrayOutputStream.write(data);
            data = inputStream.read();
        }
        inputStream.close();
    }
}
