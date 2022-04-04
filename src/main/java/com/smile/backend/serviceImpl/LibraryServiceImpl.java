package com.smile.backend.serviceImpl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.smile.backend.entity.Image;
import com.smile.backend.entity.LbImage;
import com.smile.backend.entity.Library;
import com.smile.backend.entity.UserLibrary;
import com.smile.backend.exception.BizException;
import com.smile.backend.mapper.ImageMapper;
import com.smile.backend.mapper.LbImageMapper;
import com.smile.backend.mapper.LibraryMapper;
import com.smile.backend.mapper.UserLibraryMapper;
import com.smile.backend.service.LibraryService;
import com.smile.backend.utils.ResultEnum;
import com.smile.backend.utils.StringConstantsEnum;
import com.smile.backend.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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

    @Autowired
    public LibraryServiceImpl(LibraryMapper libraryMapper, UserLibraryMapper userLibraryMapper, ImageMapper imageMapper, LbImageMapper lbImageMapper) {
        this.libraryMapper = libraryMapper;
        this.userLibraryMapper = userLibraryMapper;
        this.imageMapper = imageMapper;
        this.lbImageMapper = lbImageMapper;
    }

    @Override
    public void addLibrary(Library lib, Integer userId) {
        libraryMapper.insert(lib);
        System.out.println("36: " + lib);
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
        return libraryMapper.selectBatchIds(lbIdBatch);
    }

    @Override
    public void updateLibrary(MultipartFile[] files, Library library, Integer id, String url) {
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
            String suffix = Objects
                    .requireNonNull(file.getOriginalFilename())
                    .substring(file.getOriginalFilename().lastIndexOf("."));
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
            imageMapper.insert(image);
            LbImage lbImage = new LbImage(library.getLbId(), image.getId());
            lbImageMapper.insert(lbImage);
            urlBuilder = new StringBuilder(url);
        }
        libraryMapper.updateById(library);
    }
}
