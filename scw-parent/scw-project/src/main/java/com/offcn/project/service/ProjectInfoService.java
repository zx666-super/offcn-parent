package com.offcn.project.service;

import com.offcn.project.po.*;

import java.util.List;

public interface ProjectInfoService {
    /**
     * 获取项目回报列表
     * @param projectId
     * @return
     */
    List<TReturn> getReturnList(Integer projectId);
    List<TProject> findAllProject();
    List<TProjectImages> getProjectImages(Integer id);
    TProject findProjectInfo(Integer projectId);
    List<TTag> findAllTag();
    List<TType> findAllType();
    TReturn findReturnInfo (Integer returnId);
}
