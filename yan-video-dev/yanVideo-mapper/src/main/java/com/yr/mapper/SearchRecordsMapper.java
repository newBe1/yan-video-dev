package com.yr.mapper;

import com.yr.pojo.SearchRecords;
import com.yr.utils.MyMapper;

import java.util.List;

public interface SearchRecordsMapper extends MyMapper<SearchRecords> {
    List<String> getHotWords();
}