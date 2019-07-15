package com.yr.service;

import com.yr.pojo.Bgm;

import java.util.List;

public interface BgmService {
    /**
     * 获取bgm列表
     *
     * @return
     */
    List<Bgm> queryBgmList();

    /**
     * 通过id查询bgm
     *
     * @param bgmId
     * @return
     */
    Bgm selectBgmById(String bgmId);
}
