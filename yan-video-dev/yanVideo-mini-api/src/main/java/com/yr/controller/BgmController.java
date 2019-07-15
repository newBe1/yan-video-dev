package com.yr.controller;

import com.yr.pojo.Bgm;
import com.yr.service.BgmService;
import com.yr.utils.IMoocJSONResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "bgm")
@Api(value = "背景音乐接口", tags = "背景音乐controller")
public class BgmController {
    @Autowired
    private BgmService bgmService;

    @PostMapping(value = "queryBgmList")
    @ApiOperation(value = "背景音乐列表", notes = "背景音乐列表接口")
    public IMoocJSONResult queryBgmList() {
        List<Bgm> bgmList = bgmService.queryBgmList();
        return IMoocJSONResult.ok(bgmList);
    }
}
