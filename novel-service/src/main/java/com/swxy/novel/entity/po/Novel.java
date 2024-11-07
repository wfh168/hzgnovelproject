package com.swxy.novel.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.jsoup.nodes.Document;
@Data
@TableName("novels")
@ApiModel(description = "小说信息实体类")
public class Novel {
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

    public void setNovelDetails(Document document) {
        this.title = document.select("h1").text();
        this.author = document.select("div:nth-of-type(2) > div:nth-of-type(1) > div > div > div:nth-of-type(2) > p:nth-of-type(1) > a").text();
        this.status = document.select("p:nth-of-type(1) > span:nth-of-type(2)").text();
        this.updateTime = document.select("p:nth-of-type(1) > span:nth-of-type(3)").text();
        this.introduction = document.getElementById("bookIntro").text();
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    @Override
    public String toString() {
        return String.format("书名: %s, 作者: %s, 状态: %s, 更新时间: %s, 简介: %s,图片地址: %s",
                title, author, status, updateTime, introduction, imagePath);
    }
}

