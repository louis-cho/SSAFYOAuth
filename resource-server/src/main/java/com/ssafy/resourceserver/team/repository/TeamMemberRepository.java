package com.ssafy.resourceserver.team.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ssafy.resourceserver.team.entity.TeamMemberEntity;
import com.ssafy.resourceserver.team.entity.TeamMemberPK;

@Repository
public interface TeamMemberRepository extends JpaRepository<TeamMemberEntity, TeamMemberPK> {
	@Query("select count(tm) from TeamMemberEntity tm where tm.teamSeq=:teamSeq")
	Integer countByTeamSeq(@Param("teamSeq") Integer teamSeq);
}
