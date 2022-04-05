package com.smile.backend.serviceImpl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.smile.backend.entity.Image;
import com.smile.backend.entity.LbImage;
import com.smile.backend.mapper.ImageMapper;
import com.smile.backend.mapper.LbImageMapper;
import com.smile.backend.service.ImageService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author passion
 * @since 2022-03-29
 */
@Service
@Transactional
public class ImageServiceImpl extends ServiceImpl<ImageMapper, Image> implements ImageService {

    private final LbImageMapper lbImageMapper;
    private final ImageMapper imageMapper;

    @Autowired
    public ImageServiceImpl(LbImageMapper lbImageMapper, ImageMapper imageMapper) {
        this.lbImageMapper = lbImageMapper;
        this.imageMapper = imageMapper;
    }

    @Override
    public ArrayList<ArrayList<String>> getImages(ArrayList<Integer> libraryIds, boolean getAll) {
        ArrayList<ArrayList<String>> imagesList = new ArrayList<>();
        for (Integer libraryId : libraryIds) {
            QueryWrapper<LbImage> lbImageQueryWrapper = new QueryWrapper<>();
            lbImageQueryWrapper.eq("lb_id", libraryId);
            List<LbImage> lbImages = lbImageMapper.selectList(lbImageQueryWrapper);
            ArrayList<String> imageUrls = new ArrayList<>();
            if (lbImages.size() > 0) {
                ArrayList<Integer> imageIdList = new ArrayList<>();
                for (LbImage lbImage : lbImages) {
                    imageIdList.add(lbImage.getImageId());
                }
                List<Image> images = imageMapper.selectBatchIds(imageIdList);
                for (Image image : images) {
                    imageUrls.add(image.getUrl());
                    if (!getAll && imageUrls.size() >= 6) {
                        break;
                    }
                }
            }
            imagesList.add(imageUrls);
        }
        return imagesList;
    }

    @Override
    public ArrayList<String> getImage(Integer id) {
        QueryWrapper<LbImage> lbImageQueryWrapper = new QueryWrapper<>();
        lbImageQueryWrapper.eq("lb_id", id);
        List<LbImage> lbImages = lbImageMapper.selectList(lbImageQueryWrapper);
        ArrayList<String> imageUrls = new ArrayList<>();
        if (lbImages.size() > 0) {
            ArrayList<Integer> imageIdList = new ArrayList<>();
            for (LbImage lbImage : lbImages) {
                imageIdList.add(lbImage.getImageId());
            }
            List<Image> images = imageMapper.selectBatchIds(imageIdList);
            for (Image image : images) {
                imageUrls.add(image.getUrl());
                if (imageUrls.size() >= 6) {
                    break;
                }
            }
        }
        return imageUrls;
    }
}
