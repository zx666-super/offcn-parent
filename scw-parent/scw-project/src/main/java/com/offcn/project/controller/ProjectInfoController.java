package com.offcn.project.controller;

import com.offcn.dycommon.response.AppResponse;
import com.offcn.project.po.*;
import com.offcn.project.service.ProjectInfoService;
import com.offcn.project.vo.resp.ProjectDetailVo;
import com.offcn.project.vo.resp.ProjectVo;
import com.offcn.util.OssTemplate;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Api(tags="项目基本功能模块（文件上传、项目信息获取等）")
@Slf4j
@RequestMapping("/project")
@RestController
public class ProjectInfoController {
    @Autowired
    private OssTemplate ossTemplate;
    @Autowired
    private ProjectInfoService projectInfoService;

    @PostMapping("/upload")
    public AppResponse<Map<String,Object>> upload(@RequestParam("file") MultipartFile[] files)throws IOException {
        Map<String,Object> map = new HashMap<>();
        List<String> list= new ArrayList<>();
        if(files !=null && files.length>0){
            for (MultipartFile file : files) {
                if(!file.isEmpty()){
                    String upload = ossTemplate.upload(file.getInputStream(),file.getOriginalFilename());
                    list.add(upload);
                }
            }
        }
        map.put("urls",list);
        log.debug("ossTemplate信息：{},文件上传成功访问路径{}",ossTemplate,list);
        return AppResponse.ok(map);
    }

    @ApiOperation("获取项目回报列表")
    @GetMapping("/details/returns/{projectId}")
    public AppResponse<List<TReturn>> getReturnList(@PathVariable("projectId") Integer projectId) {
        List<TReturn> returns = projectInfoService.getReturnList(projectId);
        return AppResponse.ok(returns);
    }

    @ApiOperation("获取系统所有的项目")
    @GetMapping("/all")
    public AppResponse<List<ProjectVo>> findAllProject() {
        //创建集合存储所有的的项目vo
        List<ProjectVo> projectVos = new ArrayList<>();
        //2.查询项目
        List<TProject> projects = projectInfoService.findAllProject();
        //遍历集合
        for (TProject project : projects) {
            Integer projectId = project.getId();
            List<TProjectImages> projectImages = projectInfoService.getProjectImages(projectId);
            ProjectVo projectVo = new ProjectVo();
            BeanUtils.copyProperties(project,projectVo);
            //遍历图片集合
            for (TProjectImages image : projectImages) {
                if(image.getImgtype()==0){
                    projectVo.setHeaderImage(image.getImgurl());
                }
            }
            projectVos.add(projectVo);
        }
        return AppResponse.ok(projectVos);
    }

    @ApiOperation("获取项目信息详情")
    @GetMapping("/findProjectInfo/{projectId}")
    public AppResponse<ProjectDetailVo> findProjectInfo(@PathVariable("projectId") Integer projectId) {
        TProject projectInfo = projectInfoService.findProjectInfo(projectId);
        ProjectDetailVo detailVo = new ProjectDetailVo();
        //查出项目所有的图片
        List<TProjectImages> projectImages = projectInfoService.getProjectImages(projectInfo.getId());
        List<String> detailsImage = detailVo.getDetailsImage();
        if(detailsImage==null){
            detailsImage=new ArrayList<>();
        }
        for (TProjectImages projectImage : projectImages) {
            if(projectImage.getImgtype()==0){
                detailVo.setHeaderImage(projectImage.getImgurl());
            }else {
                detailsImage.add(projectImage.getImgurl());
            }
        }
        detailVo.setDetailsImage(detailsImage);
        //2.项目的所有支持回报
        List<TReturn> returns = projectInfoService.getReturnList(projectInfo.getId());
        detailVo.setProjectReturns(returns);
        BeanUtils.copyProperties(projectInfo, detailVo);
        return AppResponse.ok(detailVo);
    }

    @ApiOperation("获取系统所有的项目标签")
    @GetMapping("/findAllTag")
    public AppResponse<List<TTag>> findAllTag() {
        List<TTag> tags = projectInfoService. findAllTag ();
        return AppResponse.ok(tags);
    }

    @ApiOperation("获取系统所有的项目分类")
    @GetMapping("/findAllType")
    public AppResponse<List<TType>> findAllType() {
        List<TType> types = projectInfoService. findAllType();
        return AppResponse.ok(types);
    }

    @ApiOperation("获取回报信息")
    @GetMapping("/returns/info/{returnId}")
    public AppResponse<TReturn> findReturnInfo(@PathVariable("returnId") Integer returnId){
        TReturn tReturn = projectInfoService.findReturnInfo(returnId);
        return AppResponse.ok(tReturn);
    }
}
