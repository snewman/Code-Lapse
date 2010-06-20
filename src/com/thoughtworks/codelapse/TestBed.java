package com.thoughtworks.codelapse;

import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.ExecuteWatchdog;
import org.apache.commons.exec.PumpStreamHandler;

import java.io.*;

/**
 * Created by IntelliJ IDEA.
 * User: admin
 * Date: Jun 20, 2010
 * Time: 3:30:37 PM
 * To change this template use File | Settings | File Templates.
 */
public class TestBed {

    public static void main(String[] args) throws IOException {
        String line = "/opt/local/bin/git --git-dir=/Users/admin/Development/thirdparty/clojure-contrib/.git --work-tree=/Users/admin/Development/thirdparty/clojure-contrib --no-pager log";
CommandLine commandLine = CommandLine.parse(line);

DefaultExecutor executor = new DefaultExecutor();
executor.setExitValue(0);

        OutputStream os = new ByteArrayOutputStream();
        OutputStream es = new ByteArrayOutputStream();
        PumpStreamHandler streamHandler = new PumpStreamHandler(os, es);
        executor.setStreamHandler(streamHandler);

        ExecuteWatchdog watchdog = new ExecuteWatchdog(20000);
executor.setWatchdog(watchdog);
int exitValue = executor.execute(commandLine);

        String output = os.toString();
        System.out.println("Got output " + output);
    }

    public static void main1(String[] args) {
        try {
            Process process = Runtime.getRuntime().exec("/opt/local/bin/git --git-dir=/Users/admin/Development/thirdparty/clojure-contrib/.git --work-tree=/Users/admin/Development/thirdparty/clojure-contrib --no-pager log");
            System.err.println("Waiting...");
            int i = process.waitFor();
            System.err.println("Exit " + i);
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (InterruptedException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

}
