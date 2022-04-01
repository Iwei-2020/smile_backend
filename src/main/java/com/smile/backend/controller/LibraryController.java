package com.smile.backend.controller;

import com.smile.backend.entity.Library;
import com.smile.backend.service.LibraryService;
import com.smile.backend.utils.Result;
import com.smile.backend.utils.ResultResponse;
import com.smile.backend.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author passion
 * @since 2022-03-29
 */
@RestController
@RequestMapping("/library")
public class LibraryController {
    private final LibraryService libraryService;

    @Autowired
    public LibraryController(LibraryService libraryService) {
        this.libraryService = libraryService;
    }

    @PostMapping("/add")
    public Result add(@RequestParam Map<String, String> map) {
        Library library = Utils.StringToObject(map.get("library"), Library.class);
        int userId = Integer.parseInt(map.get("id"));
        libraryService.addLibrary(library, userId);
        return ResultResponse.getSuccessResult();
    }
}
