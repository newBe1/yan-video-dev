package com.yr.service;

import com.yr.pojo.Users;

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

}
