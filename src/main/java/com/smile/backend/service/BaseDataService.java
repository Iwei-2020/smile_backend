package com.smile.backend.service;

import com.fasterxml.jackson.databind.node.ObjectNode;


public interface BaseDataService {
    ObjectNode getBaseData(Integer userId);
}
