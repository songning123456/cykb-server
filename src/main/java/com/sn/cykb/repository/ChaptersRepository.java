package com.sn.cykb.repository;

import com.sn.cykb.entity.Chapters;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Map;

/**
 * @author: songning
 * @date: 2020/3/9 23:00
 */
public interface ChaptersRepository extends JpaRepository<Chapters, String> {

    @Query(value = "select * from chapters where novels_id = ?1 order by update_time asc limit 1", nativeQuery = true)
    Chapters findTopByNovelsIdNative(String novelsId);

    @Query(value = "select id, chapter from chapters order by update_time asc", nativeQuery = true)
    List<Map<String, Object>> findDirectoryNative(String novelsId);
}
