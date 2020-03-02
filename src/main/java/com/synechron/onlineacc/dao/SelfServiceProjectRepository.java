package com.synechron.onlineacc.dao;

import org.springframework.data.repository.CrudRepository;

import com.synechron.onlineacc.domain.SelfServiceProjectEntity;

public interface SelfServiceProjectRepository extends CrudRepository<SelfServiceProjectEntity, Long> {
	Iterable<SelfServiceProjectEntity> findByUserId (Long userId);
}
