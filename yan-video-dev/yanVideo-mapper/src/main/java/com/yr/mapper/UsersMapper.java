package com.yr.mapper;

import com.yr.pojo.Users;
import com.yr.utils.MyMapper;

public interface UsersMapper extends MyMapper<Users> {

    //用户的喜欢数增加
    void addReceiveLikeCount(String userId);

    //用户的喜欢数减少
    void reduceReceiveLikeCount(String userId);
}