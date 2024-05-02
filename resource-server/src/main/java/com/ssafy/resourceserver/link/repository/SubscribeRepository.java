package com.ssafy.resourceserver.link.repository;

import java.util.List;

import com.ssafy.resourceserver.link.entity.SubscribeEntity;
import com.ssafy.resourceserver.link.entity.SubscribePK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SubscribeRepository extends JpaRepository<SubscribeEntity, SubscribePK> {
	List<SubscribeEntity> findAllByMemberSeqAndIsDeleteFalse(Integer memberSeq);
}
