package com.yr.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.yr.mapper.*;
import com.yr.pojo.Comments;
import com.yr.pojo.SearchRecords;
import com.yr.pojo.UsersLikeVideos;
import com.yr.pojo.VO.CommentsVO;
import com.yr.pojo.VO.VideosVO;
import com.yr.pojo.Videos;
import com.yr.service.VideoService;
import com.yr.utils.PagedResult;
import com.yr.utils.TimeAgoUtils;
import org.n3r.idworker.Sid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.Date;
import java.util.List;

@Service
public class VideoServiceImpl implements VideoService {

    @Autowired
    private Sid sid;

    @Autowired
    private VideosMapper videosMapper;

    @Autowired
    private VideosMapperCustom videosMapperCustom;

    @Autowired
    private SearchRecordsMapper searchRecordsMapper;

    @Autowired
    private UsersLikeVideosMapper usersLikeVideosMapper;

    @Autowired
    private UsersMapper usersMapper;

    @Autowired
    private CommentsMapperCustom commentsMapperCustom;

    @Autowired
    private CommentsMapper commentsMapper;

    @Override
    public String saveVideo(Videos video) {
        String id = sid.nextShort();
        video.setId(id);
        videosMapper.insertSelective(video);
        return video.getId();
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void updateVideoCover(String videoId, String coverPath) {
        Videos video = new Videos();
        video.setId(videoId);
        video.setCoverPath(coverPath);
        videosMapper.updateByPrimaryKeySelective(video);
    }

    /**
     * 查询和保存热搜词
     * isSaverRecord 为1 时进行保存热搜词
     * @param isSaveRecord
     * @param page
     * @param pageSize
     * @return
     */
    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public PagedResult getAllVideos(Videos video, Integer isSaveRecord,
                                    Integer page, Integer pageSize) {

        // 保存热搜词
        String desc = video.getVideoDesc();
        String userId = video.getUserId();
        if (isSaveRecord != null && isSaveRecord == 1) {
            SearchRecords record = new SearchRecords();
            String recordId = sid.nextShort();
            record.setId(recordId);
            record.setContent(desc);
            searchRecordsMapper.insert(record);
        }

        PageHelper.startPage(page, pageSize);
        List<VideosVO> list = videosMapperCustom.queryAllVideosVO(desc);

        PageInfo<VideosVO> pageList = new PageInfo<>(list);

        PagedResult pagedResult = new PagedResult();
        pagedResult.setPage(page);
        pagedResult.setTotal(pageList.getPages());
        pagedResult.setRows(list);
        pagedResult.setRecords(pageList.getTotal());

        return pagedResult;
    }


    @Override
    public List<String> getHotWords() {
        return searchRecordsMapper.getHotWords();
    }

    @Override
    public void userLikeVideo(UsersLikeVideos usersLikeVideos, String videoCreaterId) {

        //建立用户和视频的喜欢的关系
        String likeId = sid.nextShort();
        usersLikeVideos.setId(likeId);
        usersLikeVideosMapper.insert(usersLikeVideos);

        //视频喜欢数量累加
        videosMapperCustom.addVideoLikeCount(usersLikeVideos.getVideoId());

        // 用户受喜欢数量的累加
        usersMapper.addReceiveLikeCount(videoCreaterId);
    }

    @Override
    public void userUnLikeVideo(UsersLikeVideos usersLikeVideos, String videoCreaterId) {
        //删除用户和视频的喜欢关系
        Example example = new Example(UsersLikeVideos.class);
        Example.Criteria criteria = example.createCriteria();

        criteria.andEqualTo("userId", usersLikeVideos.getUserId());
        criteria.andEqualTo("videoId", usersLikeVideos.getVideoId());

        usersLikeVideosMapper.deleteByExample(example);

        //视频的喜欢数减少
        videosMapperCustom.reduceVideoLikeCount(usersLikeVideos.getVideoId());

        //用户的喜欢数减少
        usersMapper.reduceReceiveLikeCount(videoCreaterId);
    }

    @Override
    public void saveCommon(Comments comment) {
        String id = sid.nextShort();
        comment.setId(id);
        comment.setCreateTime(new Date());
        commentsMapper.insert(comment);
    }

    @Override
    public PagedResult getAllComments(String videoId, Integer page, Integer pageSize) {

        PageHelper.startPage(page, pageSize);
        List<CommentsVO> list = commentsMapperCustom.queryComments(videoId);

        PageInfo<CommentsVO> pageList = new PageInfo<>(list);

        for (CommentsVO c : list) {
            String timeAgo = TimeAgoUtils.format(c.getCreateTime());
            c.setTimeAgoStr(timeAgo);
        }
        PagedResult pagedResult = new PagedResult();
        pagedResult.setPage(page);
        pagedResult.setTotal(pageList.getPages());
        pagedResult.setRows(list);
        pagedResult.setRecords(pageList.getTotal());
        return pagedResult;
    }

}
