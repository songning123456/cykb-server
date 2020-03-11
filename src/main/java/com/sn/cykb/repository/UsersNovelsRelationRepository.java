package com.sn.cykb.repository;

import com.sn.cykb.entity.UsersNovelsRelation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author: songning
 * @date: 2020/3/9 23:01
 */
public interface UsersNovelsRelationRepository extends JpaRepository<UsersNovelsRelation, String> {

    List<UsersNovelsRelation> findAllByUniqueIdOrderByUpdateTimeDesc(String uniqueId);
}
