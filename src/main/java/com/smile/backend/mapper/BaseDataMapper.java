package com.smile.backend.mapper;

import com.smile.backend.vo.BaseDataVo;
import org.springframework.stereotype.Component;

@Component
public interface BaseDataMapper {
    BaseDataVo getBaseData();
}
