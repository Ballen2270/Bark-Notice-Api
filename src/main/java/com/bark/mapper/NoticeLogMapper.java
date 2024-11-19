package com.bark.mapper;

import com.bark.domain.NoticeLog;
import java.util.List;

public interface NoticeLogMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(NoticeLog record);

    NoticeLog selectByPrimaryKey(Integer id);

    List<NoticeLog> selectAll();

    int updateByPrimaryKey(NoticeLog record);
}