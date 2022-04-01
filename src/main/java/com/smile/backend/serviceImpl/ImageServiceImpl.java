package com.smile.backend.serviceImpl;

import com.smile.backend.entity.Image;
import com.smile.backend.mapper.ImageMapper;
import com.smile.backend.service.ImageService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author passion
 * @since 2022-03-29
 */
@Service
public class ImageServiceImpl extends ServiceImpl<ImageMapper, Image> implements ImageService {

}
