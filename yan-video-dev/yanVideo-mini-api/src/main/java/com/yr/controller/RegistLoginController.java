package com.yr.controller;

import com.yr.pojo.Users;
import com.yr.pojo.VO.UsersVO;
import com.yr.service.UserService;
import com.yr.utils.IMoocJSONResult;
import com.yr.utils.MD5Utils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.thymeleaf.util.StringUtils;

import java.util.UUID;

@RestController
@Api(value = "用户注册登录的接口", tags = {"注册和登录的controller"})
public class RegistLoginController extends Basic {
    @Autowired
    private UserService userService;

    /**
     * 用户注册
     *
     * @param user
     * @return
     * @throws Exception
     */
    @ApiOperation(value = "用户注册", notes = "用户注册的接口")
    @PostMapping(value = "/regist")
    public IMoocJSONResult regist(@RequestBody Users user) throws Exception {
        //判断用户名和密码不为空
        if (StringUtils.isEmpty(user.getUsername()) || StringUtils.isEmpty(user.getPassword())) {
            return IMoocJSONResult.errorMsg("用户名密码不能为空");
        }
        //判断用户名是否存在
        boolean usernameIsExist = userService.queryUsernameIsExist(user.getUsername());

        //保存用户 进行注册
        if (!usernameIsExist) {
            user.setNickname("用户X");
            user.setPassword(MD5Utils.getMD5Str(user.getPassword()));
            user.setFansCounts(0);
            user.setReceiveLikeCounts(0);
            user.setFollowCounts(0);
            userService.saveUser(user);
        } else {
            return IMoocJSONResult.errorMsg("用户名已存在");
        }
        user.setPassword("");
        UsersVO usersVO = setUserRedisSessionToken(user);
        return IMoocJSONResult.ok(usersVO);
    }

    /**
     * 对user设置token 时间为30分钟
     *
     * @param userModel
     * @return
     */
    public UsersVO setUserRedisSessionToken(Users userModel) {
        String uniqueToken = UUID.randomUUID().toString();   //生成唯一的字符串作为token
        redis.set(USER_REDIS_SESSION + ":" + userModel.getId(), uniqueToken, 1000 * 60 * 30);
        System.out.println(USER_REDIS_SESSION + ":" + userModel.getId());
        UsersVO usersVO = new UsersVO();
        BeanUtils.copyProperties(userModel, usersVO);         //将userModel的属性复制给usersVO
        usersVO.setUserToken(uniqueToken);
        return usersVO;
    }

    /**
     * 用户登陆
     *
     * @param user
     * @return
     * @throws Exception
     */
    @ApiOperation(value = "用户登录", notes = "用户登录的接口")
    @PostMapping("/login")
    public IMoocJSONResult login(@RequestBody Users user) throws Exception {
        String username = user.getUsername();
        String password = user.getPassword();
        //判断用户名 密码不为空
        if (StringUtils.isEmpty(user.getUsername()) || StringUtils.isEmpty(user.getPassword())) {
            return IMoocJSONResult.errorMsg("用户名密码不能为空");
        }

        //判断用户是否存在
        Users userResult = userService.queryUserForLogin(username, MD5Utils.getMD5Str(password));

        //进行登陆
        if (userResult != null) {
            userResult.setPassword("");
            UsersVO usersVO = setUserRedisSessionToken(userResult);
            return IMoocJSONResult.ok(usersVO);
        } else {
            return IMoocJSONResult.errorMsg("用户名或密码不对");
        }
    }

    /**
     * 用户注销
     *
     * @param userId
     * @return
     * @throws Exception
     */
    @ApiOperation(value = "用户注销", notes = "用户注销的接口")
    @ApiImplicitParam(name = "userId", value = "用户id", required = true, dataType = "String", paramType = "query")
    @PostMapping("/logout")
    public IMoocJSONResult logout(String userId) throws Exception {
        redis.del(USER_REDIS_SESSION + ":" + userId);
        return IMoocJSONResult.ok();
    }
}
