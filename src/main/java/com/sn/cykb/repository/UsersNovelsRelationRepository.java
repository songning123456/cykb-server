package com.sn.cykb.repository;

import com.sn.cykb.entity.UsersNovelsRelation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * @author: songning
 * @date: 2020/3/9 23:01
 */
public interface UsersNovelsRelationRepository extends JpaRepository<UsersNovelsRelation, String> {

    @Query(value = "select novels_id from users_novels_relation where unique_id = ?1 order by update_time desc", nativeQuery = true)
    List<String> findByUniqueIdNative(String uniqueId);

    @Modifying
    @Transactional
    @Query(value = "update users_novels_relation set update_time = ?3 where unique_id = ?1 and novels_id = ?2", nativeQuery = true)
    int updateByRecentReadNative(String uniqueId, String novelsId, Date updateTime);

    @Modifying
    @Transactional
    int deleteByUniqueIdAndNovelsId(String uniqueId, String novelsId);

    UsersNovelsRelation findByUniqueIdAndAndNovelsId(String uniqueId, String novelsId);
}
