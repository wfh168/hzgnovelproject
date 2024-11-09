package com.swxy.user.controller;
import com.swxy.user.domain.Users;
import com.swxy.user.service.impl.UsersServiceImpl;
import org.springframework.web.bind.annotation.*;

import org.springframework.beans.factory.annotation.Autowired;

/**
* 用户表，存储用户的基本信息(USERS)表控制层
*
* @author xxxxx
*/
@RestController
@RequestMapping("/USERS")
public class UsersController {
/**
* 服务对象
*/
    @Autowired
    private UsersServiceImpl usersServiceImpl;

    /**
    * 通过主键查询单条数据
    *
    * @param id 主键
    * @return 单条数据
    */

}
