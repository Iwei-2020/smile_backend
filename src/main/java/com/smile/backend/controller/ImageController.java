package com.smile.backend.controller;

import com.smile.backend.service.ImageService;
import com.smile.backend.utils.Result;
import com.smile.backend.utils.ResultResponse;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author passion
 * @since 2022-03-29
 */
@RestController
@RequestMapping("/image")
@NoArgsConstructor
public class ImageController {

    private ImageService imageService;

    @Autowired
    public ImageController(ImageService imageService) {
        this.imageService = imageService;
    }

    @PostMapping("/get")
    public Result get(@RequestParam(required = false) List<Integer> lbIds,
                      @RequestParam boolean getAll,
                      @RequestParam Integer userId) {
        if (lbIds == null) {
            return ResultResponse.getSuccessResult(new ArrayList<>());
        }
        return ResultResponse.getSuccessResult(imageService.getImages(lbIds, getAll, userId));
    }

    @GetMapping("/get/{lbId}")
    public Result get(@PathVariable() Integer lbId) {
        return ResultResponse.getSuccessResult(imageService.getImage(lbId));
    }
}
