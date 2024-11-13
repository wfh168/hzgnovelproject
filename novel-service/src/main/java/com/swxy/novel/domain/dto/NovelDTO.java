package com.swxy.novel.domain.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(description = "小说信息实体类")
public class NovelDTO {
    @ApiModelProperty(value= "主键，自增的小说ID")
    @TableId(type = IdType.AUTO)
    private Long id;
    @ApiModelProperty(value= "小说标题",required = true)
    private String title;
    @ApiModelProperty(value= "作者名称",required = true)
    private String author;
    @ApiModelProperty(value= "小说状态",required = true)
    private String status;
    @ApiModelProperty(value = "最后更新时间",required = true)
    private String updateTime;
    @ApiModelProperty(value= "小说简介",required = true)
    private String introduction;
    @ApiModelProperty(value= "小说封面图片路径",required = true)
    private String imagePath;
}
