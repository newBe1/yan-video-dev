package com.yr.mapper;

import com.yr.pojo.Users;
import com.yr.utils.MyMapper;

public interface UsersMapper extends MyMapper<Users> {

    /**
     * 用户的喜欢数增加
     * @param userId
     */
    void addReceiveLikeCount(String userId);

    /**
     * 用户的喜欢数减少
     * @param userId
     */
    void reduceReceiveLikeCount(String userId);

    /**
     * 增加粉丝数
     * @param userId
     */
    void addFansCount(String userId);

    /**
     * 增加关注数
     * @param userId
     */
    void addFollersCount(String userId);

    /**
     * @Description: 减少粉丝数
     */
     void reduceFansCount(String userId);

    /**
     * @Description: 减少关注数
     */
     void reduceFollersCount(String userId);
}