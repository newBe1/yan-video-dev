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

    public void convertor(String video, String bgm, double seconds, String finalFile) throws Exception {
        List<String> command = new ArrayList<>();

        command.add(ffmpegEXE);
        command.add("-i");
        command.add(video);
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

    public static void main(String[] args) {
        VideoBgmTest ffmpeg = new VideoBgmTest("D:\\tools\\ffmpeg\\bin\\ffmpeg.exe");
        try {
            ffmpeg.convertor("D:\\fileyr\\WeChat Files\\w18879098038\\FileStorage\\Video\\2019-07\\139953229973554f74fd89815ce5d352.mp4", "D:\\fileyr\\yan-video-file\\bgm\\HOYO-MiX - Gion.mp3", 7.1, "D:\\这是通过java生产的视频.mp4");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
