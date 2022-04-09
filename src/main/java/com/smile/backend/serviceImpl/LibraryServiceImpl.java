package com.smile.backend.serviceImpl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.smile.backend.entity.*;
import com.smile.backend.exception.BizException;
import com.smile.backend.mapper.*;
import com.smile.backend.service.LibraryService;
import com.smile.backend.utils.ResultEnum;
import com.smile.backend.utils.StringConstantsEnum;
import com.smile.backend.utils.Utils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author passion
 * @since 2022-03-29
 */
@Service
@Transactional
public class LibraryServiceImpl extends ServiceImpl<LibraryMapper, Library> implements LibraryService {

    private final LibraryMapper libraryMapper;
    private final UserLibraryMapper userLibraryMapper;
    private final ImageMapper imageMapper;
    private final LbImageMapper lbImageMapper;
    private final SpecificLbMapper specificLibsMapper;
    private final UserMapper userMapper;

    @Autowired
    public LibraryServiceImpl(LibraryMapper libraryMapper, UserLibraryMapper userLibraryMapper, ImageMapper imageMapper, LbImageMapper lbImageMapper, SpecificLbMapper specificLibsMapper, UserMapper userMapper) {
        this.libraryMapper = libraryMapper;
        this.userLibraryMapper = userLibraryMapper;
        this.imageMapper = imageMapper;
        this.lbImageMapper = lbImageMapper;
        this.specificLibsMapper = specificLibsMapper;
        this.userMapper = userMapper;
    }

    @Override
    public void addLibrary(Library lib, Integer userId) {
        libraryMapper.insert(lib);
        UserLibrary userLibrary = new UserLibrary(userId, lib.getLbId());
        userLibraryMapper.insert(userLibrary);
    }

    @Override
    public List<Library> getLibrary(Integer id) {
        QueryWrapper<UserLibrary> userLibraryQueryWrapper = new QueryWrapper<>();
        userLibraryQueryWrapper.eq("user_id", id);
        List<UserLibrary> userLibraries = userLibraryMapper.selectList(userLibraryQueryWrapper);
        List<Integer> lbIdBatch = new ArrayList<>();
        for (UserLibrary userLibrary : userLibraries) {
            lbIdBatch.add(userLibrary.getLbId());
        }
        if (lbIdBatch.size() > 0) {
            return libraryMapper.selectBatchIds(lbIdBatch);
        }
        return new ArrayList<>();
    }

    @Override
    public void updateLibrary(MultipartFile[] files, Library library, Integer id, String url) {
        if (files != null && files.length > 0) {
            String folderName = "library" + library.getLbId() + "\\";
            String filePath = StringConstantsEnum.FILE_LIBRARY_PATH_LOCAL.getConstant() + folderName;
            File dir = new File(filePath);
            if (!dir.exists()) {
                if (!dir.mkdirs()) {
                    throw new BizException(ResultEnum.DIR_EXIST);
                }
            }
            StringBuilder urlBuilder = new StringBuilder(url);
            for (MultipartFile file : files) {
                if (file == null) {
                    continue;
                }
                String originalFileName = Objects.requireNonNull(file.getOriginalFilename());
                int lastIndex = file.getOriginalFilename().lastIndexOf(".");
                String suffix = originalFileName.substring(lastIndex);
                String newFileName = Utils.uuid() + suffix;
                File newFile = new File(filePath + newFileName);
                try {
                    file.transferTo(newFile);
                } catch (IOException e) {
                    throw new BizException(ResultEnum.SERVER_ERROR);
                }
                urlBuilder.append("/images").append("/library").append("/library").append(library.getLbId()).append("/").append(newFileName);
                Image image = new Image();
                image.setUrl(urlBuilder.toString());
                String fileName = originalFileName.substring(0, lastIndex);
                if (fileName.length() > 32) {
                    image.setName(fileName.substring(0, 32));
                } else {
                    image.setName(fileName);
                }
                imageMapper.insert(image);
                LbImage lbImage = new LbImage(library.getLbId(), image.getId());
                lbImageMapper.insert(lbImage);
                urlBuilder = new StringBuilder(url);
            }
            library.setLbCount(library.getLbCount() + files.length);
        }
        libraryMapper.updateById(library);
    }

    @Override
    public Map<String, List<Library>> getSpecific(List<String> specificNameList) {
        HashMap<String, List<Library>> specificLibraryHashMap = new HashMap<>();
        QueryWrapper<SpecificLb> specificLibsQueryWrapper = new QueryWrapper<>();
        specificLibsQueryWrapper.in("specific_name", specificNameList);
        List<SpecificLb> specificLibs = specificLibsMapper.selectList(specificLibsQueryWrapper);
        for (SpecificLb specificLib : specificLibs) {
            String[] libraryIdStringArray = specificLib.getLibIds().split(",");
            ArrayList<Integer> LibIdList = new ArrayList<>();
            for (String s : libraryIdStringArray) {
                LibIdList.add(Integer.parseInt(s));
            }
            specificLibraryHashMap.put(specificLib.getSpecificName(), libraryMapper.selectBatchIds(LibIdList));
        }
        return specificLibraryHashMap;
    }

    @Override
    public void watchPlus(Integer lbId) {
        Library library = libraryMapper.selectById(lbId);
        Integer lbWatch = library.getLbWatch();
        library.setLbWatch(lbWatch + 1);
        libraryMapper.updateById(library);
    }

    @Override
    public void likeOrStar(Integer lbId, Integer user_id, String type, Integer ops) {
        User user = userMapper.selectById(user_id);
        int findIndex = -1;
        if (type.equals("star")) {
            if (ops == 1) {
                String starLb = user.getStarLb();
                if (StringUtils.isEmpty(starLb)) {
                    user.setStarLb(lbId + "");
                } else {
                    String[] starLbIdSplit = starLb.split(",");
                    String[] starLbArr = org.springframework.util.StringUtils.addStringToArray(starLbIdSplit, lbId + "");
                    user.setStarLb(StringUtils.join(starLbArr, ","));
                }
                userMapper.updateById(user);
            } else if (ops == 0) {
                String[] starLbIdSplit = user.getStarLb().split(",");
                likeOrStarHelper(lbId, user, findIndex, starLbIdSplit, type);
            }
        } else if (type.equals("like")) {
            if (ops == 1) {
                String likeLb = user.getLikeLb();
                if (StringUtils.isEmpty(likeLb)) {
                    user.setLikeLb(lbId + "");
                } else {
                    String[] likeLbIdSplit = likeLb.split(",");
                    String[] result = org.springframework.util.StringUtils.addStringToArray(likeLbIdSplit, lbId + "");
                    user.setLikeLb(StringUtils.join(result, ","));
                }
                userMapper.userAddLikeCount(lbId);
                userMapper.updateById(user);
            } else if (ops == 0) {
                userMapper.userReduceLikeCount(lbId);
                String[] likeLbIdSplit = user.getLikeLb().split(",");
                likeOrStarHelper(lbId, user, findIndex, likeLbIdSplit, type);
            }
        }
    }

    private void likeOrStarHelper(Integer lbId, User user, int findIndex, String[] starLbIdSplit, String type) {
        for (int i = 0; i < starLbIdSplit.length; i++) {
            if (starLbIdSplit[i].equals(lbId + "")) {
                findIndex = i;
                break;
            }
        }
        String[] remove = ArrayUtils.remove(starLbIdSplit, findIndex);
        if (type.equals("star")) {
            user.setStarLb(StringUtils.join(remove, ","));
        } else if (type.equals("like")) {
            user.setLikeLb(StringUtils.join(remove, ","));
        }
        userMapper.updateById(user);
    }
}
