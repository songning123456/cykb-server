package com.sn.cykb.vo;

import lombok.Data;

/**
 * @author: songning
 * @date: 2020/3/9 23:03
 */
@Data
public class UsersNovelsRelationVO {

    private String uniqueId;

    private String novelsId;

    /**
     * recentRead; updateTime
     */
    private String sortType;
}
