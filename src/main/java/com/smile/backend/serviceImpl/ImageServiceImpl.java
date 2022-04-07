package com.smile.backend.serviceImpl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.smile.backend.entity.Image;
import com.smile.backend.entity.LbImage;
import com.smile.backend.mapper.ImageMapper;
import com.smile.backend.mapper.LbImageMapper;
import com.smile.backend.service.ImageService;
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
    public List<List<Image>> getImages(List<Integer> lbIds, boolean getAll) {
        List<List<Image>> libraryImagesList = new ArrayList<>();
        for (Integer libraryId : lbIds) {
            libraryImagesList.add(getImageList(libraryId, getAll));
        }
        return libraryImagesList;
    }

    @Override
    public List<Image> getImage(Integer lbId) {
        return getImageList(lbId, false);
    }

    private List<Image> getImageList(Integer lbId, boolean getAll) {
        QueryWrapper<LbImage> lbImageQueryWrapper = new QueryWrapper<>();
        lbImageQueryWrapper.eq("lb_id", lbId);
        List<LbImage> lbImages = lbImageMapper.selectList(lbImageQueryWrapper);
        List<Image> images = new ArrayList<>();
        if (lbImages.size() > 0) {
            ArrayList<Integer> imageIdList = new ArrayList<>();
            for (LbImage lbImage : lbImages) {
                imageIdList.add(lbImage.getImageId());
            }
            if (getAll) {
                images = imageMapper.selectBatchIds(imageIdList);
            } else {
                System.out.println("67!!!!!!!!");
                IPage<Image> imagePage = new Page<>(0, 6);
                QueryWrapper<Image> imageQueryWrapper = new QueryWrapper<>();
                imageQueryWrapper.in("id", imageIdList);
                images = imageMapper.selectPage(imagePage, imageQueryWrapper).getRecords();
            }
        }
        return images;
    }
}
