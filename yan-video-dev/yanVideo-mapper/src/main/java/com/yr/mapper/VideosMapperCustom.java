package com.yr.mapper;

import com.yr.pojo.VO.VideosVO;
import com.yr.pojo.Videos;
import com.yr.utils.MyMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface VideosMapperCustom extends MyMapper<Videos> {

    //以热搜词为搜索对象查询所以相关的视频
     List<VideosVO> queryAllVideosVO(@Param("videoDesc") String videoDesc);

    // 对视频喜欢的数量进行累加
    void addVideoLikeCount(String videoId);

    //对视频的喜欢数进行减少
    void reduceVideoLikeCount(String video);

    //查询用户关注的视频
    List<VideosVO> showMyFollow(String userId);

    //查询我喜欢的视频
    List<VideosVO> showMyLike(String userId);
}
