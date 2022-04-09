package com.smile.backend.serviceImpl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.smile.backend.entity.Image;
import com.smile.backend.entity.LbImage;
import com.smile.backend.entity.User;
import com.smile.backend.mapper.ImageMapper;
import com.smile.backend.mapper.LbImageMapper;
import com.smile.backend.mapper.UserMapper;
import com.smile.backend.service.ImageService;
import com.smile.backend.utils.Utils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
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
    private final UserMapper userMapper;

    @Autowired
    public ImageServiceImpl(LbImageMapper lbImageMapper, ImageMapper imageMapper, UserMapper userMapper) {
        this.lbImageMapper = lbImageMapper;
        this.imageMapper = imageMapper;
        this.userMapper = userMapper;
    }

    @Override
    public ObjectNode getImages(List<Integer> lbIds, boolean getAll, Integer userId) {
        List<List<Image>> libraryImagesList = new ArrayList<>();
        HashMap<String, Object> map = new HashMap<>();
        for (Integer libraryId : lbIds) {
            libraryImagesList.add(getImageList(libraryId, getAll));
        }

        User user = userMapper.selectById(userId);
        // likeArray
        String sLikeLb = user.getLikeLb();
        ArrayList<Integer> iListLikeLb = new ArrayList<>();
        if (!StringUtils.isEmpty(sLikeLb)) {
            String[] sArrLikeLb = sLikeLb.split(",");
            ArrayList<String> sListLikeLb = new ArrayList<>(Arrays.asList(sArrLikeLb));
            for (String s : sListLikeLb) {
                iListLikeLb.add(Integer.parseInt(s));
            }
        }
        List<Integer> likeResult = new ArrayList<>(lbIds);
        // starArray
        String sStarLb = user.getStarLb();
        ArrayList<Integer> iListStarLb = new ArrayList<>();
        if (!StringUtils.isEmpty(sStarLb)) {
            String[] sArrStarLb = user.getStarLb().split(",");
            ArrayList<String> sListStarLb = new ArrayList<>(Arrays.asList(sArrStarLb));
            for (String s : sListStarLb) {
                iListStarLb.add(Integer.parseInt(s));
            }
        }
        List<Integer> starResult = new ArrayList<>(lbIds);
        // return
        likeResult.retainAll(iListLikeLb);
        starResult.retainAll(iListStarLb);
        ArrayList<Boolean> bArrLikeLb = new ArrayList<>();
        ArrayList<Boolean> bArrStarLb = new ArrayList<>();
        for (Integer lbId : lbIds) {
            bArrLikeLb.add(likeResult.contains(lbId));
            bArrStarLb.add(starResult.contains(lbId));
        }
        map.put("imagesArray", libraryImagesList);
        map.put("likeArray", bArrLikeLb);
        map.put("starArray", bArrStarLb);

        return Utils.objectNodeFormat(map);
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
                IPage<Image> imagePage = new Page<>(0, 6);
                QueryWrapper<Image> imageQueryWrapper = new QueryWrapper<>();
                imageQueryWrapper.in("id", imageIdList);
                images = imageMapper.selectPage(imagePage, imageQueryWrapper).getRecords();
            }
        }
        return images;
    }
}
