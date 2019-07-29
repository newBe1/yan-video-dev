package com.yr.controller;

import com.yr.pojo.Users;
import com.yr.pojo.UsersReport;
import com.yr.pojo.VO.PublisherVideo;
import com.yr.pojo.VO.UsersVO;
import com.yr.service.UserService;
import com.yr.utils.IMoocJSONResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

@RestController
@Api(value = "用户相关业务接口", tags = "用户相关业务controller")
@RequestMapping("/user")
public class UserController extends BasicControll {

    @Autowired
    private UserService userService;

    @ApiOperation(value = "用户上传头像", notes = "用户上传头像的接口")
    @ApiImplicitParam(name = "userId", value = "用户id", required = true,
            dataType = "String", paramType = "query")
    @PostMapping("/uploadFace")
    public IMoocJSONResult uploadFace(String userId,
                                      @RequestParam("file") MultipartFile[] files) throws Exception {

        if (StringUtils.isBlank(userId)) {
            return IMoocJSONResult.errorMsg("用户id不能为空...");
        }

        // 文件保存的命名空间
        String fileSpace = FILE_SPACE;
        // 保存到数据库中的相对路径
        String uploadPathDB = "/" + userId + "/face";

        FileOutputStream fileOutputStream = null;
        InputStream inputStream = null;
        try {
            if (files != null && files.length > 0) {

                String fileName = files[0].getOriginalFilename();
                System.out.println(fileName);
                if (StringUtils.isNotBlank(fileName)) {
                    // 文件上传的最终保存路径
                    String finalFacePath = fileSpace + uploadPathDB + "/" + fileName;
                    // 设置数据库保存的路径
                    uploadPathDB += ("/" + fileName);

                    File outFile = new File(finalFacePath);
                    if (outFile.getParentFile() != null || !outFile.getParentFile().isDirectory()) {
                        // 创建父文件夹
                        outFile.getParentFile().mkdirs();
                    }

                    fileOutputStream = new FileOutputStream(outFile);
                    inputStream = files[0].getInputStream();
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

        Users user = new Users();
        user.setId(userId);
        user.setFaceImage(uploadPathDB);
        userService.updateUserInfo(user);

        return IMoocJSONResult.ok(uploadPathDB);
    }

    @PostMapping(value = "userInfo")
    @ApiOperation(value = "查询用户信息", notes = "查询用户信息接口")
    @ApiImplicitParam(name = "userId", value = "用户id", dataType = "String", required = true, paramType = "query")
    public IMoocJSONResult userInfo(String userId,String fanId) {
        if (StringUtils.isBlank(userId)) {
            return IMoocJSONResult.errorMsg("用户id不能为空...");
        }

        //查询用户信息
        Users user = userService.userInfo(userId);
        UsersVO usersVO = new UsersVO();
        BeanUtils.copyProperties(user, usersVO);
        usersVO.setFollow(userService.queryIfFollow(userId,fanId));
        return IMoocJSONResult.ok(usersVO);
    }

    @PostMapping(value = "changeName")
    @ApiOperation(value = "修改用户昵称",notes = "修改用户昵称接口")
    @ApiImplicitParam(name = "userId",value = "用户Id",dataType = "String",required = true,paramType = "query")
    public IMoocJSONResult changeName(String userId,String nikeName){
        if (StringUtils.isBlank(userId)) {
            return IMoocJSONResult.errorMsg("用户id不能为空...");
        }

        //修改昵称
        Users user = new Users();
        user.setId(userId);
        user.setNickname(nikeName);
        userService.updateUserInfo(user);

        return IMoocJSONResult.ok(nikeName);
    }

    @ApiOperation(value ="查询视频作者信息",notes = "视频作者的id不能为空")
    @PostMapping(value = "queryPublisher")
    public IMoocJSONResult queryPublisher(String loginUserId,String videoId,String publishUserId){
        if(StringUtils.isBlank(publishUserId)){
            return IMoocJSONResult.errorMsg("视频作者的id不能为空");
        }

        //查询视频作者的信息
        Users userInfo = userService.userInfo(publishUserId);
        UsersVO publisher = new UsersVO();
        BeanUtils.copyProperties(userInfo,publisher);

        //查询登录者是否喜欢此视频
        boolean userLikeVideo = userService.isUserLikeVideo(loginUserId,videoId);

        PublisherVideo bean = new PublisherVideo();
        bean.setPublisher(publisher);
        bean.setUserLikeVideo(userLikeVideo);
        return IMoocJSONResult.ok(bean);
    }

    @ApiOperation(value = "成为粉丝",notes = "用户的粉丝数增加 粉丝的关注数增加")
    @PostMapping(value = "beYourFans")
    public IMoocJSONResult beYourFans(String userId , String fanId){
        if(StringUtils.isBlank(userId) || StringUtils.isBlank(fanId)){
            return IMoocJSONResult.errorMsg("用户id和粉丝id不能为空");
        }

        userService.saveUserFanRelation(userId,fanId);
        return IMoocJSONResult.ok("关注成功");
    }


    @ApiOperation(value = "取消关注",notes = "用户的粉丝数减少 粉丝的关注数减少")
    @PostMapping(value = "dontBeYourFans")
    public IMoocJSONResult dontBeYourFans(String userId , String fanId){
        if(StringUtils.isBlank(userId) || StringUtils.isBlank(fanId)){
            return IMoocJSONResult.errorMsg("用户id和粉丝id不能为空");
        }

        userService.deleteUserFanRelation(userId,fanId);
        return IMoocJSONResult.ok("取消关注成功");
    }

    @ApiOperation(value = "举报用户视频")
    @PostMapping(value = "reportUser")
    public IMoocJSONResult reportUser(@RequestBody UsersReport usersReport){

        //保存举报信息
        userService.reportUser(usersReport);

        return IMoocJSONResult.ok("举报成功");
    }
}
