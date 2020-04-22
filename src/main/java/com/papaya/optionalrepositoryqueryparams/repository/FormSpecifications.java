package com.papaya.optionalrepositoryqueryparams.repository;

import com.papaya.optionalrepositoryqueryparams.model.Form;
import com.papaya.optionalrepositoryqueryparams.model.FormUsage;
import com.papaya.optionalrepositoryqueryparams.model.Role;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.*;
import java.util.List;
import java.util.stream.Stream;

public class FormSpecifications {
    public static Specification<Form> formsByUsageLevel(String countryIso, Long organisationId, Long projectId, List<Role> roleList) {
        return new Specification<Form>() {
            @Override
            public Predicate toPredicate(Root<Form> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                ListJoin<Form, FormUsage> formUsageJoin = root.joinList(Form.Fields.formUsages);
                ListJoin<FormUsage, Role> roleJoin = formUsageJoin.joinList(FormUsage.Fields.roles);

                Predicate countryIsoValue = criteriaBuilder.equal(formUsageJoin.get(FormUsage.Fields.countryIso), countryIso);
                Predicate countryIsoNull = criteriaBuilder.isNull(formUsageJoin.get(FormUsage.Fields.countryIso));
                Predicate countryIsoOrStatement = criteriaBuilder.or(countryIsoValue, countryIsoNull);

                Predicate organisationIdValue = criteriaBuilder.equal(formUsageJoin.get(FormUsage.Fields.organisationId), organisationId);
                Predicate organisationIdNull = criteriaBuilder.isNull(formUsageJoin.get(FormUsage.Fields.organisationId));
                Predicate organisationIdOrStatement = criteriaBuilder.or(organisationIdValue, organisationIdNull);

                Predicate projectIdValue = criteriaBuilder.equal(formUsageJoin.get(FormUsage.Fields.projectId), projectId);
                Predicate projectIdNull = criteriaBuilder.isNull(formUsageJoin.get(FormUsage.Fields.projectId));
                Predicate projectIdOrStatement = criteriaBuilder.or(projectIdValue, projectIdNull);

                Predicate result;

                if (roleList.isEmpty()) {
                    result = criteriaBuilder.and(countryIsoOrStatement, organisationIdOrStatement, projectIdOrStatement);
                } else {
                    Stream<Predicate> roleValues = roleList.stream()
                            .map(role -> {
                                Predicate id = criteriaBuilder.equal(roleJoin.get(Role.Fields.id), role.getId());
                                Predicate code = criteriaBuilder.equal(roleJoin.get(Role.Fields.code), role.getCode());
                                Predicate title = criteriaBuilder.equal(roleJoin.get(Role.Fields.title), role.getTitle());
                                return criteriaBuilder.and(id, code, title);
                            });
                    Predicate roleEmpty = criteriaBuilder.isEmpty(formUsageJoin.get(FormUsage.Fields.roles));

                    Predicate rolesOrStatement = criteriaBuilder.or(Stream.concat(Stream.of(roleEmpty), roleValues).toArray(Predicate[]::new));

                    result = criteriaBuilder.and(countryIsoOrStatement, organisationIdOrStatement, projectIdOrStatement, rolesOrStatement);
                }
                return result;
            }
        };
    }
}
