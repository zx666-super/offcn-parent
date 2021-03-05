package com.offcn.user.controller;

import com.offcn.user.entity.User;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "测试的控制层-swagger测试")
@RestController
public class MyController {

    @ApiOperation("这是一个hello的方法")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(value = "姓名",name = "name",required = true),
            @ApiImplicitParam(value = "年龄",name = "age")
    })
    @GetMapping("/hello")
    public String hello(String name){
        return "success"+name;
    }

    @ApiOperation("这是一个addUser的方法")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(value = "姓名",name = "name",required = true),
            @ApiImplicitParam(value = "性别",name = "sex",required = true)
    })
    @PostMapping("/addUser")
    public User addUser(String name,String sex){
        User user  = new User();
        user.setName(name);
        user.setSex(sex);
        return user;
    }
}
