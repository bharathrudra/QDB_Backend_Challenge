package com.bank.document.project.repository;

import com.bank.document.project.model.DocumentModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;

@Repository
@EnableJpaRepositories
public interface DocumentRepository extends JpaRepository<DocumentModel,String>{
}
