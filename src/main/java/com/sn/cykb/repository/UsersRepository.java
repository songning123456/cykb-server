package com.sn.cykb.repository;

import com.sn.cykb.entity.UsersEntity;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author songning
 * @date 2020/3/9
 * description
 */
public interface UsersRepository extends JpaRepository<UsersEntity, String> {

    UsersEntity findByUniqueId(String uniqueId);
}
