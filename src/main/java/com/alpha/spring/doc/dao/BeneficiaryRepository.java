package com.alpha.spring.doc.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.alpha.spring.doc.model.Beneficiary;

public interface BeneficiaryRepository extends JpaRepository<Beneficiary, Long> {

}
