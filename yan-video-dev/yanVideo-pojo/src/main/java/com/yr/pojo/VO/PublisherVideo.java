package com.yr.pojo.VO;

/**
 * @author 程序鬼才
 * @version 1.0
 * @date 2019/7/26 15:01
 * 视频作者和视频信息对象  是否喜欢视频
 */
public class PublisherVideo {
    public UsersVO publisher;
    public boolean userLikeVideo;
    public UsersVO getPublisher() {
        return publisher;
    }
    public void setPublisher(UsersVO publisher) {
        this.publisher = publisher;
    }
    public boolean isUserLikeVideo() {
        return userLikeVideo;
    }
    public void setUserLikeVideo(boolean userLikeVideo) {
        this.userLikeVideo = userLikeVideo;
    }
}
