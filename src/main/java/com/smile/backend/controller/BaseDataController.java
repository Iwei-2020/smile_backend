package com.smile.backend.controller;

import com.smile.backend.annotation.TokenRequired;
import com.smile.backend.service.BaseDataService;
import com.smile.backend.utils.Result;
import com.smile.backend.utils.ResultResponse;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/base")
@NoArgsConstructor
public class BaseDataController {

    private BaseDataService baseDataService;

    @Autowired
    public BaseDataController(BaseDataService baseDataService) {
        this.baseDataService = baseDataService;
    }

    @TokenRequired
    @GetMapping("/baseData/{userId}")
    public Result baseData(@PathVariable Integer userId) {
        return ResultResponse.getSuccessResult(baseDataService.getBaseData(userId));
    }
}
