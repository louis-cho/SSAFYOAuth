package com.ssafy.resourceserver.team.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ssafy.resourceserver.team.entity.TeamMemberWithInfoEntity;
import com.ssafy.resourceserver.team.entity.TeamMemberWithInfoPK;

@Repository
public interface TeamMemberWithInfoRepository extends JpaRepository<TeamMemberWithInfoEntity, TeamMemberWithInfoPK> {
	List<TeamMemberWithInfoEntity> findAllByTeamSeq(Integer teamSeq);

	List<TeamMemberWithInfoEntity> findAllByTeamSeqAndEmail(Integer teamSeq, String email);
}
