package com.it.yts_project.service;

import com.it.yts_project.dto.SystemContentUpdateRequest;
import com.it.yts_project.model.SystemContent;

public interface SystemContentService {

    SystemContent getByKey(String contentKey);

    SystemContent updateByKey(String contentKey, SystemContentUpdateRequest req, Integer operatorId);
}
