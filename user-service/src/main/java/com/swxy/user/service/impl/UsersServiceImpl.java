package com.swxy.user.service.impl;

import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.swxy.user.domain.Users;
import com.swxy.user.mapper.UsersMapper;
import com.swxy.user.service.UsersService;
@Service
public class UsersServiceImpl extends ServiceImpl<UsersMapper, Users> implements UsersService{

}
