package com.swxy.user.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.math.BigDecimal;
import java.util.Date;
import lombok.Data;

/**
 * 用户表，存储用户的基本信息
 */
@ApiModel(description = "用户表，存储用户的基本信息")
@Data
@TableName(value = "USERS")
public class Users {
    /**
     * 用户ID，自增长
     */
    @TableId(value = "USER_ID", type = IdType.AUTO)
    @ApiModelProperty(value = "用户ID，自增长")
    private BigDecimal userId;

    /**
     * 用户名，唯一
     */
    @TableField(value = "USERNAME")
    @ApiModelProperty(value = "用户名，唯一")
    private String username;

    /**
     * 用户邮箱，唯一
     */
    @TableField(value = "EMAIL")
    @ApiModelProperty(value = "用户邮箱，唯一")
    private String email;

    /**
     * 用户密码（加密后的）
     */
    @TableField(value = "\"PASSWORD\"")
    @ApiModelProperty(value = "用户密码（加密后的）")
    private String password;

    /**
     * 用户手机号
     */
    @TableField(value = "PHONE")
    @ApiModelProperty(value = "用户手机号")
    private String phone;

    /**
     * 用户昵称
     */
    @TableField(value = "NICKNAME")
    @ApiModelProperty(value = "用户昵称")
    private String nickname;

    /**
     * 用户头像URL
     */
    @TableField(value = "AVATAR_URL")
    @ApiModelProperty(value = "用户头像URL")
    private String avatarUrl;

    /**
     * 用户性别（M: 男, F: 女, O: 其他）
     */
    @TableField(value = "GENDER")
    @ApiModelProperty(value = "用户性别（M: 男, F: 女, O: 其他）")
    private String gender;

    /**
     * 用户生日
     */
    @TableField(value = "BIRTHDAY")
    @ApiModelProperty(value = "用户生日")
    private Date birthday;

    /**
     * 用户状态（ACTIVE: 激活, INACTIVE: 未激活, BANNED: 禁用）
     */
    @TableField(value = "\"STATUS\"")
    @ApiModelProperty(value = "用户状态（ACTIVE: 激活, INACTIVE: 未激活, BANNED: 禁用）")
    private String status;

    /**
     * 用户最后登录时间
     */
    @TableField(value = "LAST_LOGIN_TIME")
    @ApiModelProperty(value = "用户最后登录时间")
    private Date lastLoginTime;

    /**
     * 用户注册时间
     */
    @TableField(value = "REGISTRATION_TIME")
    @ApiModelProperty(value = "用户注册时间")
    private Date registrationTime;

    /**
     * 用户登录失败次数
     */
    @TableField(value = "FAILED_LOGIN_ATTEMPTS")
    @ApiModelProperty(value = "用户登录失败次数")
    private BigDecimal failedLoginAttempts;

    /**
     * 用户锁定时间（如果登录失败次数过多）
     */
    @TableField(value = "LOCK_TIME")
    @ApiModelProperty(value = "用户锁定时间（如果登录失败次数过多）")
    private Date lockTime;

    /**
     * 邮箱是否验证（Y: 已验证, N: 未验证）
     */
    @TableField(value = "IS_VERIFIED")
    @ApiModelProperty(value = "邮箱是否验证（Y: 已验证, N: 未验证）")
    private String isVerified;
}