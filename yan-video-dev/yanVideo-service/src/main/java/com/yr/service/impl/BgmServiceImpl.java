package com.yr.service.impl;

import com.yr.mapper.BgmMapper;
import com.yr.pojo.Bgm;
import com.yr.service.BgmService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BgmServiceImpl implements BgmService {
    @Autowired
    private BgmMapper bgmMapper;

    @Override
    public List<Bgm> queryBgmList() {
        List<Bgm> bgmList = bgmMapper.selectAll();
        return bgmList;
    }

    @Override
    public Bgm selectBgmById(String bgmId) {
        return bgmMapper.selectByPrimaryKey(bgmId);
    }
}
