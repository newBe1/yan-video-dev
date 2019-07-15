package com.yr.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.yr.mapper.SearchRecordsMapper;
import com.yr.mapper.VideosMapper;
import com.yr.mapper.VideosMapperCustom;
import com.yr.pojo.SearchRecords;
import com.yr.pojo.VO.VideosVO;
import com.yr.pojo.Videos;
import com.yr.service.VideoService;
import com.yr.utils.PagedResult;
import org.n3r.idworker.Sid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
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
     *
     * @param videos
     * @param isSaverRecord
     * @param page
     * @param pageSize
     * @return
     */
    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public PagedResult getAllVideos(Videos videos, Integer isSaverRecord, Integer page, Integer pageSize) {

        //将视频的描述作为热搜词保存
        String content = videos.getVideoDesc();
        if (isSaverRecord != null && isSaverRecord == 1) {
            SearchRecords searchRecords = new SearchRecords();
            searchRecords.setId(sid.nextShort());
            searchRecords.setContent(content);
            searchRecordsMapper.insert(searchRecords);
        }

        PageHelper.startPage(page, pageSize);
        List<VideosVO> list = videosMapperCustom.queryAllVidoesVO(content);
        PageInfo<VideosVO> pageInfo = new PageInfo<>(list);
        PagedResult result = new PagedResult();

        result.setPage(page);
        result.setRecords(pageInfo.getTotal());
        result.setRows(list);
        result.setTotal(pageInfo.getPages());
        return result;
    }

    @Override
    public List<String> getHotWords() {
        return searchRecordsMapper.getHotWords();
    }
}
