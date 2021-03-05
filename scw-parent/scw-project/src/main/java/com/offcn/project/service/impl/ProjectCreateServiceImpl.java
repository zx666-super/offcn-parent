package com.offcn.project.service.impl;

import com.alibaba.fastjson.JSON;
import com.offcn.dycommon.enums.ProjectStatusEnume;
import com.offcn.project.contants.ProjectConstant;
import com.offcn.project.enums.ProjectImageTypeEnume;
import com.offcn.project.mapper.*;
import com.offcn.project.po.*;
import com.offcn.project.service.ProjectCreateService;
import com.offcn.project.vo.req.ProjectRedisStorageVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class ProjectCreateServiceImpl implements ProjectCreateService {
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    private TProjectMapper projectMapper;

    @Autowired
    private TProjectImagesMapper projectImagesMapper;

    @Autowired
    private TProjectTagMapper projectTagMapper;

    @Autowired
    private TProjectTypeMapper projectTypeMapper;

    @Autowired
    private TReturnMapper returnMapper;
    @Override
    public String initCreateProject(Integer memberId) {
        String token = UUID.randomUUID().toString().replace("-", "");
        ProjectRedisStorageVo vo = new ProjectRedisStorageVo();
        vo.setMemberid(memberId);
        vo.setProjectToken(token);
        String s = JSON.toJSONString(vo);
        stringRedisTemplate.opsForValue().set(ProjectConstant.TEMP_PROJECT_PREFIX+token,s);
        return token;
    }

    @Override
    public void saveProjectInfo(ProjectStatusEnume auth, ProjectRedisStorageVo projectVo) {
        //创建项目
        TProject project = new TProject();
        BeanUtils.copyProperties(projectVo,project);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        project.setCreatedate(sdf.format(new Date()));
        project.setStatus(auth.getCode()+"");
        projectMapper.insertSelective(project);
        //拿到id
        Integer projectId = project.getId();
        String headerImage = projectVo.getHeaderImage();
        TProjectImages tProjectImages = new TProjectImages(null, projectId, headerImage, ProjectImageTypeEnume.HEADER.getCode());
        projectImagesMapper.insertSelective(tProjectImages);
        List<String> detailsImage = projectVo.getDetailsImage();
        if(!CollectionUtils.isEmpty(detailsImage)){
            for (String image : detailsImage) {
                TProjectImages img = new TProjectImages(null,projectId,image, ProjectImageTypeEnume.DETAILS.getCode());
                projectImagesMapper.insertSelective(img);
            }
        }
        //3.保存项目的标签信息
        List<Integer> tagids = projectVo.getTagids();
        if(!CollectionUtils.isEmpty(tagids)){
            for (Integer tagId : tagids) {
                TProjectTag tProjectTag = new TProjectTag(null,projectId,tagId);
                projectTagMapper.insertSelective(tProjectTag);
            }
        }
        //4.保存分类信息
        List<Integer> typeids = projectVo.getTypeids();
        if(!CollectionUtils.isEmpty(typeids)){
            for (Integer typeid : typeids) {
                TProjectType tProjectType = new TProjectType(null,projectId,typeid);
                projectTypeMapper.insertSelective(tProjectType);
            }
        }
        //5.保存回报信息
        List<TReturn> returns = projectVo.getProjectReturns();
        if(!CollectionUtils.isEmpty(returns)){
            for(TReturn tReturn:returns){
                tReturn.setProjectid(projectId);
                returnMapper.insertSelective(tReturn);
            }
        }
        //6.删除临时数据
        if(auth.getStatus().equals("1")){
            stringRedisTemplate.delete(ProjectConstant.TEMP_PROJECT_PREFIX+projectVo.getProjectToken());
        }
    }
}
