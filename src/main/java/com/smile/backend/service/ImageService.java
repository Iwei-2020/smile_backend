package com.smile.backend.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.fasterxml.jackson.databind.node.ObjectNode;
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
    ObjectNode getImages(List<Integer> lbIds, boolean getAll, Integer userId);
    List<Image> getImage(Integer lbId);
}
