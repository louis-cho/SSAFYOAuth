package com.ssafy.resourceserver.team.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ssafy.resourceserver.team.entity.TeamListEntity;
import com.ssafy.resourceserver.team.entity.TeamListPK;

@Repository
public interface TeamListRepository extends JpaRepository<TeamListEntity, TeamListPK> {
	List<TeamListEntity> findByMemberSeq(Integer memberSeq);

	List<TeamListEntity> findByMemberSeqAndIsAcceptFalse(Integer memberSeq);
}
