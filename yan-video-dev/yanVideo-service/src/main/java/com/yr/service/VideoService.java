package com.yr.service;

import com.yr.pojo.Comments;
import com.yr.pojo.UsersLikeVideos;
import com.yr.pojo.Videos;
import com.yr.utils.PagedResult;

import java.util.List;

public interface VideoService {

    /**
     * 保存video
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
    PagedResult getAllVideos(Videos videos ,Integer isSaveRecord, Integer page, Integer pageSize);

    /**
     * 获取热搜词列表
     * @return
     */
    List<String> getHotWords();

    /**
     * 用户喜欢视频
     * @return
     */
    void userLikeVideo(UsersLikeVideos usersLikeVideos, String videoCreaterId);

    /**
     * 用户取消喜欢视频
     * @param usersLikeVideos
     * @param videoCreaterId
     */
    void userUnLikeVideo(UsersLikeVideos usersLikeVideos, String videoCreaterId);

    /**
     * 保持评论
     * @param comment
     */
    void saveCommon(Comments comment);

    /**
     * 获取视频的全部评论
     * @param videoId
     * @param page
     * @param pageSize
     * @return
     */
    PagedResult getAllComments(String videoId, Integer page, Integer pageSize);

    /**
     * 查询我关注的人发的视频
     * @param userId
     * @param page
     * @param pageSize
     * @return
     */
    PagedResult showMyFollow(String userId , Integer page ,Integer pageSize);


    /**
     * 查询我喜欢的视频
     * @param userId
     * @param page
     * @param pageSize
     * @return
     */
    PagedResult showMyLike(String userId , Integer page , Integer pageSize);
}
