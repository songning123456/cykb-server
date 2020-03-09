package com.sn.cykb.repository;

import com.sn.cykb.entity.Comments;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author: songning
 * @date: 2020/3/9 23:00
 */
public interface CommentsRepository extends JpaRepository<Comments, String> {
}
