package com.yr.service;

import com.yr.pojo.Users;
import com.yr.pojo.UsersReport;

public interface UserService {
    /**
     * 判断用户名是否存在
     *
     * @param username
     * @return
     */
    public boolean queryUsernameIsExist(String username);


    /**
     * 注册保存用户
     * @param user
     */
    public void saveUser(Users user);

    /**
     * 登陆时用户的判断
     * @param username
     * @param password
     * @return
     */
    public Users queryUserForLogin(String username, String password);

    /**
     * 更新用户信息
     * @param user
     * @return
     */
    public void updateUserInfo(Users user);

    /**
     * 获取用户信息
     * @param userId
     * @return
     */
    public Users userInfo(String userId);

    /**
     * 用户是否喜欢此视频
     * @param userId
     * @param videoId
     * @return
     */
    Boolean isUserLikeVideo(String userId, String videoId );

    /**
     * 查询用户是否关注别人
     * @param userId
     * @param fanId
     * @return
     */
    boolean queryIfFollow(String userId , String fanId);


    /**
     * 关注成为粉丝  保存关系
     * @param userId
     * @param fanId
     */
    void saveUserFanRelation(String userId , String fanId);

    /**
     *
     * @param userId
     * @param fanId
     */
    void deleteUserFanRelation(String userId , String fanId);

    /**
     *保存举报信息
     * @param usersReport
     */
    void reportUser(UsersReport usersReport);
}
