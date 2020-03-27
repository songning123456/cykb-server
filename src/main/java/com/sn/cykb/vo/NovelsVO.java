package com.sn.cykb.vo;

import lombok.Data;

import java.util.List;

/**
 * @author: songning
 * @date: 2020/3/9 23:03
 */
@Data
public class NovelsVO {

    private String sex;

    private String category;

    private Long createTime;

    private String author;

    /**
     * 模糊搜索 作者 || 小说名
     */
    private String authorOrTitle;

    /**
     * 搜索类型 全网搜索 || 本地搜索
     */
    private String searchType;

    /**
     * 全网搜索 来源
     */
    private List<String> source;
}
