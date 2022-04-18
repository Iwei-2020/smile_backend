package com.smile.backend.service.impl;

import com.smile.backend.entity.Goal;
import com.smile.backend.mapper.GoalMapper;
import com.smile.backend.service.IGoalService;
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
public class GoalServiceImpl extends ServiceImpl<GoalMapper, Goal> implements IGoalService {

}
