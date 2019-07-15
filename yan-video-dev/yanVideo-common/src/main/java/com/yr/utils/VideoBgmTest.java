package com.yr.utils;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class VideoBgmTest {
    private String ffmpegEXE;

    public VideoBgmTest(String ffmpegEXE) {
        super();
        this.ffmpegEXE = ffmpegEXE;
    }

    public void convertor(String voide, String bgm, double seconds, String finalFile) throws Exception {
        List<String> command = new ArrayList<>();

        command.add(ffmpegEXE);
        command.add("-i");
        command.add(voide);
        command.add("-i");
        command.add(bgm);
        command.add("-t");
        command.add(String.valueOf(seconds));
        command.add("-y");
        command.add(finalFile);

        ProcessBuilder builder = new ProcessBuilder(command);
        Process process = builder.start();

        InputStream errorStream = process.getErrorStream();
        InputStreamReader inputStreamReader = new InputStreamReader(errorStream);
        BufferedReader br = new BufferedReader(inputStreamReader);

        String line = "";
        while ((line = br.readLine()) != null) {
        }

        if (br != null) {
            br.close();
        }
        if (inputStreamReader != null) {
            inputStreamReader.close();
        }
        if (errorStream != null) {
            errorStream.close();
        }
    }

}
