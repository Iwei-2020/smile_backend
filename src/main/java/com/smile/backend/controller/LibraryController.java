package com.smile.backend.controller;

import com.smile.backend.annotation.TokenRequired;
import com.smile.backend.entity.Library;
import com.smile.backend.service.LibraryService;
import com.smile.backend.utils.Result;
import com.smile.backend.utils.ResultResponse;
import com.smile.backend.utils.Utils;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
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
@NoArgsConstructor
public class LibraryController {

    private LibraryService libraryService;

    @Autowired
    public LibraryController(LibraryService libraryService) {
        this.libraryService = libraryService;
    }

    @PostMapping("/add")
    @TokenRequired
    public Result add(@RequestParam Map<String, String> map) {
        Library library = Utils.stringToObject(map.get("library"), Library.class);
        int userId = Integer.parseInt(map.get("id"));
        libraryService.addLibrary(library, userId);
        return ResultResponse.getSuccessResult();
    }

    @GetMapping("/get/{id}")
    @TokenRequired
    public Result get(@PathVariable Integer id) {
        return ResultResponse.getSuccessResult(libraryService.getLibrary(id));
    }


    @PostMapping("/update")
    @TokenRequired
    public Result update(
            @RequestParam(required = false) MultipartFile[] files,
            @RequestParam("library") String lbStr,
            @RequestParam Integer id,
            HttpServletRequest request) {
        Library library = Utils.stringToObject(lbStr, Library.class);
        String url = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort();
        libraryService.updateLibrary(files, library, id, url);
        return  ResultResponse.getSuccessResult();
    }

    @GetMapping("/specific/{userId}")
    public Result specific(@PathVariable(required = false) String userId) {
        return ResultResponse.getSuccessResult(libraryService.getSpecific(userId));
    }

    @PostMapping("/watch")
    public Result watch(@RequestParam Integer lbId) {
        libraryService.watchPlus(lbId);
        return ResultResponse.getSuccessResult();
    }

    /**
     * @param type 操作类型 in [like, star]
     * @param lbId 库Id
     * @param user_id 用户Id
     * @param ops 1：确定 0: 取消(点赞/收藏)
     * @return 结果
     */
    @PostMapping("/likeOrStar")
    public Result likeOrStar(@RequestParam Integer lbId,
                             @RequestParam Integer user_id,
                             @RequestParam String type,
                             @RequestParam Integer ops) {
        libraryService.likeOrStar(lbId, user_id, type, ops);
        return ResultResponse.getSuccessResult();
    }
}
