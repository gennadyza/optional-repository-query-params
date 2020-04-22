package com.papaya.optionalrepositoryqueryparams.repository;

import com.papaya.optionalrepositoryqueryparams.model.FormUsage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FormUsageRepository extends JpaRepository<FormUsage, Long> {
    List<FormUsage> findByOrganisationIdOrOrganisationIdIsNull(Long id);
    List<FormUsage> findByCountryIsoOrCountryIsoIsNullAndOrganisationIdOrOrganisationIdIsNullAndProjectIdOrProjectIdIsNull(String countryIso, Long organisationId, Long projectId);
}
