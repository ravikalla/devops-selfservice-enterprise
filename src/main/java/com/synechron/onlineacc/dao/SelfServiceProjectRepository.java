package com.synechron.onlineacc.dao;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.synechron.onlineacc.domain.SelfServiceProjectEntity;

public interface SelfServiceProjectRepository extends CrudRepository<SelfServiceProjectEntity,Long> {
	List<SelfServiceProjectEntity> findByUserId (Long userId);
}
