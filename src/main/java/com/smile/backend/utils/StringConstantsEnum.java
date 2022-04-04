package com.smile.backend.utils;

import lombok.Getter;

@Getter
public enum StringConstantsEnum {
    BASE_FILE_PATH("E:\\iResource\\images\\"),
    FILE_AVATAR_PATH_LOCAL("E:\\iResource\\images\\avatar\\user\\"),
    FILE_LIBRARY_PATH_LOCAL("E:\\iResource\\images\\Library\\"),
    DEFAULT_AVATAR("http://localhost:3333/images/avatar/default/default-0.png"),
    DEFAULT_AVATAR_LOCAL("http://localhost:3333/images/avatar/default/default-");

    private final String constant;

    StringConstantsEnum(String constant) {
        this.constant = constant;
    }

}
