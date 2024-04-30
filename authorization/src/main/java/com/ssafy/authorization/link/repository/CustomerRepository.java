package com.ssafy.authorization.link.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ssafy.authorization.link.entity.CustomerEntity;
import com.ssafy.authorization.link.entity.CustomerPK;

@Repository
public interface CustomerRepository extends JpaRepository<CustomerEntity, CustomerPK> {
	List<CustomerEntity> findAllByTeamSeqAndIsDeleteFalse(Integer teamSeq);

	List<CustomerEntity> findAllByNameContainsAndIsDeleteFalse(String keyword);

	List<CustomerEntity> findAllByEmailContainsAndIsDeleteFalse(String keyword);

	List<CustomerEntity> findAllByStudentIdContainsAndIsDeleteFalse(String keyword);

	List<CustomerEntity> findAllByPhoneNumberContainsAndIsDeleteFalse(String keyword);
}
