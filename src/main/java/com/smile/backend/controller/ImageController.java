package com.smile.backend.controller;

import com.smile.backend.service.ImageService;
import com.smile.backend.utils.Result;
import com.smile.backend.utils.ResultResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

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
public class ImageController {

    private final ImageService imageService;

    @Autowired
    public ImageController(ImageService imageService) {
        this.imageService = imageService;
    }

    @PostMapping("/get")
    public Result get(@RequestParam ArrayList<Integer> libraryIds, @RequestParam boolean getAll) {
        return ResultResponse.getSuccessResult(imageService.getImages(libraryIds, getAll));
    }

    @GetMapping("/get/{id}")
    public Result get(@PathVariable Integer id) {
        return ResultResponse.getSuccessResult(imageService.getImage(id));
    }
}
