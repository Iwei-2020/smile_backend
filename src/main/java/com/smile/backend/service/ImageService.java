package com.smile.backend.service;

import com.smile.backend.entity.Image;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.ArrayList;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author passion
 * @since 2022-03-29
 */
public interface ImageService extends IService<Image> {
    ArrayList<ArrayList<String>> getImages(ArrayList<Integer> libraryIds, boolean getAll);
    ArrayList<String> getImage(Integer id);
}
