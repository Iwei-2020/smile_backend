package com.smile.backend.utils;

import lombok.Getter;

@Getter
public enum StringConstantsEnum {
    FILE_PATH_LOCAL("E:\\iResource\\images\\avatar\\"),
    DEFAULT_AVATAR("http://localhost:3333/images/2022/03/22/1fea566b862b4d5cbfb63b4fa73e484b.png"),
    DEFAULT_AVATAR_LOCAL("http://localhost:3333/images/default/default-");

    private final String constant;

    StringConstantsEnum(String constant) {
        this.constant = constant;
    }

}
