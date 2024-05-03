package com.ssafy.resourceserver.link.repository;

import java.util.List;

import com.ssafy.resourceserver.link.entity.CustomerEntity;
import com.ssafy.resourceserver.link.entity.CustomerPK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends JpaRepository<CustomerEntity, CustomerPK> {
	List<CustomerEntity> findAllByTeamSeqAndIsDeleteFalse(Integer teamSeq);

	List<CustomerEntity> findAllByNameContainsAndIsDeleteFalse(String keyword);

	List<CustomerEntity> findAllByEmailContainsAndIsDeleteFalse(String keyword);

	List<CustomerEntity> findAllByStudentIdContainsAndIsDeleteFalse(String keyword);

	List<CustomerEntity> findAllByPhoneNumberContainsAndIsDeleteFalse(String keyword);
}
