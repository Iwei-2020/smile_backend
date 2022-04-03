package com.smile.backend.controller;

import com.smile.backend.entity.User;
import com.smile.backend.service.UserService;
import com.smile.backend.utils.Result;
import com.smile.backend.utils.ResultEnum;
import com.smile.backend.utils.ResultResponse;
import com.smile.backend.utils.StringConstantsEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;
import java.util.UUID;

@RestController
@RequestMapping("/upload")
public class UploadController {

    private final UserService userService;
    private final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd/");

    @Autowired
    public UploadController(UserService userService) {
        this.userService = userService;
    }

    @RequestMapping("/avatar/{id}")
    public Result uploadFile(@RequestParam("file") MultipartFile file, HttpServletRequest request, @PathVariable int id) {
        if (file == null) {
            return ResultResponse.getFailResult(ResultEnum.BAD_REQUEST);
        }
        String directory = simpleDateFormat.format(new Date());
        String filePath = StringConstantsEnum.FILE_PATH_LOCAL.getConstant();  //"D:\\Git\\";
        File dir = new File(filePath + directory);
        if (!dir.exists()) {
            if (!dir.mkdirs()) {
                return ResultResponse.getFailResult("创建目录失败");
            }
        }

        String suffix = Objects
                .requireNonNull(file.getOriginalFilename())
                .substring(file.getOriginalFilename().lastIndexOf("."));
        String newFileName = UUID.randomUUID().toString().replaceAll("-", "") + suffix;

        File newFile = new File(filePath + directory + newFileName);
        try {
            file.transferTo(newFile);
            String url = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + "/images/" + directory + newFileName;
            User user = new User();
            user.setId(id);
            user.setAvatarUrl(url);
            userService.updateById(user);
            return ResultResponse.getSuccessResult(url);
        } catch (IOException e) {
            return ResultResponse.getFailResult(ResultEnum.SERVER_ERROR);
        }
    }
}
