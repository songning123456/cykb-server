package com.sn.cykb.repository;

import com.sn.cykb.entity.Novels;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author: songning
 * @date: 2020/3/9 23:01
 */
public interface NovelsRepository extends JpaRepository<Novels, String> {
}
