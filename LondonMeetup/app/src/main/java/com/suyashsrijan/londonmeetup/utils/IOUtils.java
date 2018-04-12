package com.suyashsrijan.londonmeetup.utils;

import android.content.Context;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class IOUtils {

    public static void writeFileString(Context context, final String fileContents, String fileName) throws IOException {
            FileWriter out = new FileWriter(new File(context.getFilesDir(), fileName));
            out.write(fileContents);
            out.close();
    }

    public static String readFileString(Context context, String fileName) throws Exception {
        StringBuilder stringBuilder = new StringBuilder();
        String line;
        BufferedReader in;
        File file = new File(context.getFilesDir(), fileName);
        if (!file.exists()) {
            writeFileString(context, "[]", fileName);
        }
        in = new BufferedReader(new FileReader(file));
        while ((line = in.readLine()) != null) { stringBuilder.append(line); };
        return stringBuilder.toString();
    }
}
