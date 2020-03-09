package com.sn.cykb.repository;

import com.sn.cykb.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author songning
 * @date 2020/3/9
 * description
 */
public interface UsersRepository extends JpaRepository<Users, String> {

    Users findByUniqueId(String uniqueId);

    @Modifying
    @Transactional
    @Query(value = "update users set avatar=:#{#entity.avatar}, nick_name=:#{#entity.nickName}, update_time=:#{#entity.updateTime} where unique_id=:#{#entity.uniqueId}", nativeQuery = true)
    void updateNative(@Param("entity") Users users);
}
