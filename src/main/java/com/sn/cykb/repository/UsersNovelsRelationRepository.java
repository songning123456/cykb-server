package com.sn.cykb.repository;

import com.sn.cykb.entity.UsersNovelsRelation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * @author: songning
 * @date: 2020/3/9 23:01
 */
public interface UsersNovelsRelationRepository extends JpaRepository<UsersNovelsRelation, String> {

    List<UsersNovelsRelation> findAllByUniqueIdOrderByUpdateTimeDesc(String uniqueId);


    @Query(value = "select novels_id from users_novels_relation where unique_id = ?1 order by update_time desc", nativeQuery = true)
    List<String> findByUniqueIdNative(String uniqueId);
}
