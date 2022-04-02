package com.smile.backend.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.smile.backend.entity.Library;

import java.util.List;

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
}
