package com.bark.mapper;

import com.bark.domain.DeviceConf;
import java.util.List;

public interface DeviceConfMapper {
    int deleteByPrimaryKey(String deviceToken);

    int insert(DeviceConf record);

    DeviceConf selectByPrimaryKey(String deviceToken);

    List<DeviceConf> selectAll();
    List<DeviceConf> selectAllActive();

    int updateByPrimaryKey(DeviceConf record);
}