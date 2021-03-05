package com.offcn.project.service;

import com.offcn.dycommon.enums.ProjectStatusEnume;
import com.offcn.project.vo.req.ProjectRedisStorageVo;

public interface ProjectCreateService {
    /**
     * 初始化项目
     * @param memberId
     * @return
     */
    String initCreateProject(Integer memberId);
    void saveProjectInfo(ProjectStatusEnume auth, ProjectRedisStorageVo project);
}
