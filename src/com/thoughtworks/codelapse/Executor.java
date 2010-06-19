package com.thoughtworks.codelapse;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class Executor {

    public static String execute(String command) {
        try {
            Process process = Runtime.getRuntime().exec(command);
            String errorString = streamToString(process.getErrorStream());
            String outputString = streamToString(process.getInputStream());

            int exitValue = process.exitValue();
            if (exitValue != 0) {
                String error = String.format("Exit code %d.\nOutput Stream:\n%s\nError Stream:\n%s\n", exitValue, errorString, outputString);
                throw new RuntimeException(error);
            }

            return outputString;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static String streamToString(InputStream errorStream) throws IOException {
        BufferedReader bis = new BufferedReader(new InputStreamReader(errorStream));
        String line;
        StringBuilder output;
        try {
            output = new StringBuilder();
            while ((line = bis.readLine()) != null) {
                output.append(line).append("\n");
            }
        } finally {
            bis.close();
        }
        return output.toString();
    }


}
