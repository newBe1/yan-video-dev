package com.yr.mapper;

import com.yr.pojo.Comments;
import com.yr.pojo.VO.CommentsVO;
import com.yr.utils.MyMapper;

import java.util.List;

/**
 * @author 程序鬼才
 * @version 1.0
 * @date 2019/7/27 9:53
 */
public interface CommentsMapperCustom extends MyMapper<Comments> {
    List<CommentsVO> queryComments(String videoId);
}
