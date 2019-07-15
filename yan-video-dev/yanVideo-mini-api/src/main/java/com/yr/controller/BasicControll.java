package com.yr.controller;

import com.yr.utils.RedisOperator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BasicControll {

    @Autowired
    public RedisOperator redis;

    public static final String USER_REDIS_SESSION = "uesr-redis-session";
    // 文件保存的命名空间
    public static final String FILE_SPACE = "E:/yan-video-file";

    //ffmpge
    public static final String FFMPEG_EXE = "D:\\ffmpeg\\bin\\ffmpeg.exe";

    public static final Integer PAGE_SIZE = 5;
}
