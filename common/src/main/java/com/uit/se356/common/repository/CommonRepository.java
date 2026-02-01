package com.uit.se356.common.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface CommonRepository<E, ID>
    extends JpaRepository<E, ID>, JpaSpecificationExecutor<E> {}
