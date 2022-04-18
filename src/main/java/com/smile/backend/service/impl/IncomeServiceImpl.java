package com.smile.backend.service.impl;

import com.smile.backend.entity.Income;
import com.smile.backend.mapper.IncomeMapper;
import com.smile.backend.service.IIncomeService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 作者
 * @since 2022-04-16
 */
@Service
public class IncomeServiceImpl extends ServiceImpl<IncomeMapper, Income> implements IIncomeService {

}
