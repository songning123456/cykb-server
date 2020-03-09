package com.sn.cykb.entity;

import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

/**
 * @author: songning
 * @date: 2020/3/9 22:38
 */
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Data
@Entity
@Table(name = "UsersNovelsRelation", uniqueConstraints = {@UniqueConstraint(columnNames = {"usersId", "novelsId"})})
@GenericGenerator(name = "jpa-uuid", strategy = "uuid")
public class UsersNovelsRelation {

    @Id
    @GeneratedValue(generator = "jpa-uuid")
    @Column(length = 32)
    private String id;

    @Column(name = "usersId", columnDefinition = "VARCHAR(32) NOT NULL COMMENT 'users_id'")
    private String usersId;

    @Column(name = "novelsId", columnDefinition = "VARCHAR(32) NOT NULL COMMENT 'novels_id'")
    private String novelsId;

    @Column(name = "history", columnDefinition = "VARCHAR(128) NOT NULL COMMENT '当前阅读'")
    private String history;

    @Column(name = "updateTime", columnDefinition = "DATETIME NOT NULL COMMENT '更新时间'")
    private Date updateTime;
}
