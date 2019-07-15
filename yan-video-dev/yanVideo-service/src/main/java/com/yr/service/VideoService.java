package com.yr.service;

import com.yr.pojo.Videos;
import com.yr.utils.PagedResult;

import java.util.List;

public interface VideoService {

    /**
     * 保存video
     *
     * @param video
     * @return
     */
    String saveVideo(Videos video);

    /**
     * 更新video封面
     *
     * @param videoId
     * @param coverPath
     */
    void updateVideoCover(String videoId, String coverPath);

    /**
     * 分页查询视频
     *
     * @param page
     * @param pageSize
     * @return
     */
    PagedResult getAllVideos(Videos videos, Integer isSaverRecord, Integer page, Integer pageSize);

    /**
     * 获取热搜词列表
     *
     * @return
     */
    List<String> getHotWords();
}
