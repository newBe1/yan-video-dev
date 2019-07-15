package com.yr.mapper;

import com.yr.pojo.VO.VideosVO;
import com.yr.pojo.Videos;
import com.yr.utils.MyMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface VideosMapperCustom extends MyMapper<Videos> {

    //已热搜词为搜索对象查询所以相关的视频
    List<VideosVO> queryAllVidoesVO(@Param("videoDesc") String videoDesc);
}
