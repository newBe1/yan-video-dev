package com.yr.controller;

import com.yr.enums.VideoStatusEnum;
import com.yr.pojo.Bgm;
import com.yr.pojo.Videos;
import com.yr.service.BgmService;
import com.yr.service.VideoService;
import com.yr.utils.FetchVideoCover;
import com.yr.utils.IMoocJSONResult;
import com.yr.utils.PagedResult;
import com.yr.utils.VideoBgmTest;
import io.swagger.annotations.*;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@RequestMapping(value = "/video")
@Api(value = "视频接口", tags = "视频的controller")
@RestController
public class VideoController extends BasicControll {
    @Autowired
    private BgmService bgmService;

    @Autowired
    private VideoService videoService;


    @ApiOperation(value = "上传视频", notes = "上传视频的接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", value = "用户id", required = true,
                    dataType = "String", paramType = "form"),
            @ApiImplicitParam(name = "bgmId", value = "背景音乐id", required = false,
                    dataType = "String", paramType = "form"),
            @ApiImplicitParam(name = "videoSeconds", value = "背景音乐播放长度", required = true,
                    dataType = "String", paramType = "form"),
            @ApiImplicitParam(name = "videoWidth", value = "视频宽度", required = true,
                    dataType = "String", paramType = "form"),
            @ApiImplicitParam(name = "videoHeight", value = "视频高度", required = true,
                    dataType = "String", paramType = "form"),
            @ApiImplicitParam(name = "desc", value = "视频描述", required = false,
                    dataType = "String", paramType = "form")
    })
    @PostMapping(value = "/upload", headers = "content-type=multipart/form-data")
    public IMoocJSONResult upload(String userId, String bgmId, double videoSeconds,
                                  int videoWidth, int videoHeight,
                                  String desc,
                                  @ApiParam(value = "短视频", required = true)
                                          MultipartFile file) throws Exception {

        if (StringUtils.isBlank(userId)) {
            return IMoocJSONResult.errorMsg("用户id不能为空...");
        }

        //保存在数据库中的路径
        String uploadPathDB = "/" + userId + "/video/";
        String coverPathDB = "/" + userId + "/video/";
        //文件最终路径
        String finalVideoPath = "";
        String finalCoverPath = "";

        InputStream inputStream = null;
        FileOutputStream fileOutputStream = null;

        try {
            if (file != null) {
                String fileName = file.getOriginalFilename();
                String arrayFilenameItem[] = fileName.split("\\.");
                String fileNamePerfix = "";
                for (int i = 0; i < arrayFilenameItem.length - 1; i++) {
                    fileNamePerfix += arrayFilenameItem[i];
                }

                if (StringUtils.isNoneBlank(fileName)) {
                    uploadPathDB += fileName;
                    coverPathDB += fileNamePerfix + ".jpg";
                    finalVideoPath = FILE_SPACE + uploadPathDB;
                    finalCoverPath = FILE_SPACE + coverPathDB;
                    File outFile = new File(finalVideoPath);
                    if (outFile.getParentFile() != null || !outFile.getParentFile().isDirectory()) {
                        // 创建父文件夹
                        outFile.getParentFile().mkdirs();
                    }
                    fileOutputStream = new FileOutputStream(outFile);
                    inputStream = file.getInputStream();
                    IOUtils.copy(inputStream, fileOutputStream);
                }
            } else {
                return IMoocJSONResult.errorMsg("上传出错");
            }
        } catch (IOException e) {
            e.printStackTrace();
            return IMoocJSONResult.errorMsg("上传出错");
        } finally {
            if (fileOutputStream != null) {
                fileOutputStream.flush();
                fileOutputStream.close();
            }
        }
        if (StringUtils.isNotBlank(bgmId)) {
            Bgm bgm = bgmService.selectBgmById(bgmId);

            String bgmPath = FILE_SPACE + bgm.getPath();

            String ffmpegEXE = FFMPEG_EXE;
            VideoBgmTest tool = new VideoBgmTest(ffmpegEXE);
            String videoInputPath = finalVideoPath;
            String videoOutputName = UUID.randomUUID().toString() + ".mp4";
            uploadPathDB = "/" + userId + "/video/" + videoOutputName;
            finalVideoPath = FILE_SPACE + uploadPathDB;
            tool.convertor(videoInputPath, bgmPath, videoSeconds, finalVideoPath);
        }
        System.out.println("uploadPathDB : " + uploadPathDB);
        System.out.println("finalVideoPath : " + finalVideoPath);

        // 对视频进行截图
        FetchVideoCover videoInfo = new FetchVideoCover(FFMPEG_EXE);
        videoInfo.getCover(finalVideoPath, finalCoverPath);

        //将video保存到数据库
        Videos video = new Videos();
        video.setAudioId(bgmId);
        video.setUserId(userId);
        video.setVideoPath(uploadPathDB);
        video.setVideoDesc(desc);
        video.setVideoHeight(videoHeight);
        video.setVideoWidth(videoWidth);
        video.setVideoSeconds((float) videoSeconds);
        video.setStatus(VideoStatusEnum.SUCCESS.value);
        video.setCreateTime(new Date());
        video.setCoverPath(coverPathDB);
        String videoId = videoService.saveVideo(video);

        return IMoocJSONResult.ok(videoId);
    }

    /*@ApiOperation(value="上传封面", notes="上传封面的接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name="userId", value="用户id", required=true,
                    dataType="String", paramType="form"),
            @ApiImplicitParam(name="videoId", value="视频主键id", required=true,
                    dataType="String", paramType="form")
    })
    @PostMapping(value="/uploadCover", headers="content-type=multipart/form-data")
    public IMoocJSONResult uploadCover(String userId,
                                       String videoId,
                                       @ApiParam(value="视频封面", required=true)
                                               MultipartFile file) throws Exception {

        if (StringUtils.isBlank(videoId) || StringUtils.isBlank(userId)) {
            return IMoocJSONResult.errorMsg("视频主键id和用户id不能为空...");
        }

        // 保存到数据库中的相对路径
        String uploadPathDB = "/" + userId + "/video";

        FileOutputStream fileOutputStream = null;
        InputStream inputStream = null;
        // 文件上传的最终保存路径
        String finalCoverPath = "";
        try {
            if (file != null) {

                String fileName = file.getOriginalFilename();
                if (StringUtils.isNotBlank(fileName)) {

                    finalCoverPath = FILE_SPACE + uploadPathDB + "/" + fileName;
                    // 设置数据库保存的路径
                    uploadPathDB += ("/" + fileName);

                    File outFile = new File(finalCoverPath);
                    if (outFile.getParentFile() != null || !outFile.getParentFile().isDirectory()) {
                        // 创建父文件夹
                        outFile.getParentFile().mkdirs();
                    }

                    fileOutputStream = new FileOutputStream(outFile);
                    inputStream = file.getInputStream();
                    IOUtils.copy(inputStream, fileOutputStream);
                }

            } else {
                return IMoocJSONResult.errorMsg("上传出错...");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return IMoocJSONResult.errorMsg("上传出错...");
        } finally {
            if (fileOutputStream != null) {
                fileOutputStream.flush();
                fileOutputStream.close();
            }
        }

        System.out.println("uploadPathDB : " + uploadPathDB);
        System.out.println("videoId : " + videoId);
        videoService.updateVideoCover(videoId, uploadPathDB);

        return IMoocJSONResult.ok();
    }*/

    @PostMapping(value = "/showAll")
    public IMoocJSONResult showAll(@RequestBody Videos videos, Integer isSaverRecord, Integer page) throws Exception {

        if (page == null) {
            page = 1;
        }
        PagedResult result = videoService.getAllVideos(videos, isSaverRecord, page, PAGE_SIZE);

        return IMoocJSONResult.ok(result);
    }

    @PostMapping(value = "/hot")
    public IMoocJSONResult hot() {
        List<String> list = videoService.getHotWords();
        return IMoocJSONResult.ok(list);
    }
}