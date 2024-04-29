package com.ssafy.authorization.link.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ssafy.authorization.link.entity.SubscribeEntity;
import com.ssafy.authorization.link.entity.SubscribePK;

@Repository
public interface SubscribeRepository extends JpaRepository<SubscribeEntity, SubscribePK> {
	List<SubscribeEntity> findAllByMemberSeqAndIsDeleteFalse(Integer memberSeq);
}
