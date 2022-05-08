package com.smile.backend.vo;

import com.smile.backend.entity.Image;
import com.smile.backend.entity.Library;
import com.smile.backend.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LibraryImageVo{
    private Library library;
    private List<Image> images;
    private User author;
    private Boolean isLike;
    private Boolean isStar;
}
