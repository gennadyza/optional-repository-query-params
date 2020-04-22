package com.papaya.optionalrepositoryqueryparams.repository;

import com.papaya.optionalrepositoryqueryparams.model.Form;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface FormRepository extends JpaRepository<Form, Long>,
        JpaSpecificationExecutor<Form> {
}
