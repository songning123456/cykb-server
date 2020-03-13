package com.sn.cykb.repository;

import com.sn.cykb.entity.Novels;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Map;

/**
 * @author: songning
 * @date: 2020/3/9 23:01
 */
public interface NovelsRepository extends JpaRepository<Novels, String> {

    @Query(value = "select * from novels", countQuery = "select count(1) from novels", nativeQuery = true)
    Page<Novels> findNovelsNative(Pageable pageable);

    @Query(value = "select count(1) as total, category from novels where sex = ?1 group by category", nativeQuery = true)
    List<Map<String, Object>> countBySexNative(String sex);

    Novels findByIdOrderByUpdateTimeDesc(String novelsId);

    List<Novels> findAllByIdInOrderByUpdateTimeDesc(List<String> novelsIds);
}
