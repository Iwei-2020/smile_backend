package com.smile.backend.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.smile.backend.entity.Library;
import com.smile.backend.vo.LibraryImageVo;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author passion
 * @since 2022-03-29
 */
public interface LibraryService extends IService<Library> {
    void addLibrary(Library lib, Integer userId);

    List<Library> getLibrary(Integer id);

    void updateLibrary(MultipartFile[] files, Library library, Integer id, String url);

    Map<String, List<LibraryImageVo>> getSpecific(String userId);

    void watchPlus(Integer lbId);

    void likeOrStar(Integer lbId, Integer user_id, String type, Integer ops);
}
