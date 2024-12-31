package com.swxy.novel.domain.po;

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

    @ApiModelProperty(value = "小说分类",required = true)
    private String classify;
    @ApiModelProperty(value = "小说推荐默认为N")
    private String recommendation;

    public void setNovelDetails(Document document) {
        this.title = document.select("body > div.book > div.info > h1").text();
        this.author = document.select("body > div.book > div.info > div.small > span:nth-child(1)").text();
        this.status = document.select("body > div.book > div.info > div.small > span:nth-child(2)").text();
        this.updateTime = document.select("body > div.book > div.info > div.small > span:nth-child(3)").text();
        this.introduction = document.select("body > div.book > div.info > div.intro > dl > dd").text();
        String classifyText=document.select("body > div.book > div.path.wap_none").text();
        String[] parts = classifyText.split(">");  // 按照 ">" 分割字符串
        if (parts.length > 1) {
            String result = parts[1].trim();  // 获取第二部分并去除多余的空白字符
            this.classify = result;
        }
        this.recommendation="N";
    }
    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    @Override
    public String toString() {
        return String.format("书名: %s, 作者: %s, 状态: %s, 更新时间: %s, 简介: %s,图片地址: %s,分类：%s,是否推荐：%s",
                title, author, status, updateTime, introduction, imagePath, classify, recommendation);
    }
}

