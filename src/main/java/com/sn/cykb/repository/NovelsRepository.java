package com.sn.cykb.repository;

import com.sn.cykb.entity.Novels;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * @author: songning
 * @date: 2020/3/9 23:01
 */
public interface NovelsRepository extends JpaRepository<Novels, String> {

    @Query(value = "select * from novels order by create_time desc limit ?1", nativeQuery = true)
    List<Novels> findFirstHomePageNative(int size);

    @Query(value = "select * from novels where create_time < ?1 order by create_time desc limit ?2", nativeQuery = true)
    List<Novels> findMoreHomePageNative(Long createTime, int size);

    @Query(value = "select * from novels where sex = ?1 and category = ?2 order by create_time desc limit ?3", nativeQuery = true)
    List<Novels> findFirstClassifyNative(String sex, String category, int size);

    @Query(value = "select * from novels where sex = ?1 and category = ?2 and create_time < ?3 order by create_time desc limit ?4", nativeQuery = true)
    List<Novels> findMoreClassifyNative(String sex, String category, Long createTime, int size);

    @Query(value = "select count(1) as total, category from novels where sex = ?1 group by category", nativeQuery = true)
    List<Map<String, Object>> countBySexNative(String sex);

    List<Novels> findAllByIdInOrderByUpdateTimeDesc(List<String> novelsIds);

    @Modifying
    @Transactional
    @Query(value = "update novels set create_time = ?1 where id = ?2", nativeQuery = true)
    void updateCreateTimeNative(Long createTime, String novelsId);
}
