package com.sn.cykb.entity;

import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

/**
 * @author: songning
 * @date: 2020/3/9 22:44
 */
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Data
@Entity
@Table(name = "Comments")
@GenericGenerator(name = "jpa-uuid", strategy = "uuid")
public class Comments {
    @Id
    @GeneratedValue(generator = "jpa-uuid")
    @Column(length = 32)
    private String id;

    @Column(name = "content", columnDefinition = "TEXT NOT NULL COMMENT '文本内容'")
    private String content;

    @Column(name = "contact", columnDefinition = "VARCHAR(64) NOT NULL COMMENT '联系方式'")
    private String contact;

    @Column(name = "updateTime", columnDefinition = "DATETIME NOT NULL COMMENT '更新时间'")
    private Date updateTime;

}
