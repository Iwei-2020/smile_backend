package com.smile.backend.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.smile.backend.entity.Image;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author passion
 * @since 2022-03-29
 */
public interface ImageService extends IService<Image> {
    List<List<Image>> getImages(List<Integer> lbIds, boolean getAll);
    List<Image> getImage(Integer lbId);
}
