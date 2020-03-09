package com.sn.cykb.entity;

import lombok.*;

import javax.persistence.*;

import org.hibernate.annotations.GenericGenerator;

import java.util.Date;

/**
 * @author songning
 * @date 2020/3/9
 * description
 */
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Data
@Entity
@Table(name = "users")
@GenericGenerator(name = "jpa-uuid", strategy = "uuid")
public class UsersEntity {
    @Id
    @GeneratedValue(generator = "jpa-uuid")
    @Column(length = 32)
    private String usersId;

    @Column(name = "uniqueId", columnDefinition = "VARCHAR(128) NOT NULL COMMENT '用户唯一标识'")
    private String uniqueId;

    @Column(name = "nickName", columnDefinition = "VARCHAR(64) COMMENT '昵称'")
    private String nickName;

    @Column(name = "avatar", columnDefinition = "VARCHAR(255) COMMENT '头像'")
    private String avatar;

    @Column(name = "updateTime", columnDefinition = "DATETIME NOT NULL COMMENT '更新时间'")
    private Date updateTime;
}
