package com.papaya.optionalrepositoryqueryparams;

import com.papaya.optionalrepositoryqueryparams.model.Form;
import com.papaya.optionalrepositoryqueryparams.model.FormUsage;
import com.papaya.optionalrepositoryqueryparams.model.Role;
import com.papaya.optionalrepositoryqueryparams.repository.FormRepository;
import com.papaya.optionalrepositoryqueryparams.repository.FormSpecifications;
import com.papaya.optionalrepositoryqueryparams.repository.FormUsageRepository;
import org.jboss.jandex.TypeTarget;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Collections;
import java.util.List;

@SpringBootTest
public class FormRepositoryTest {
    @Autowired
    FormRepository formRepository;
    @Autowired
    FormUsageRepository formUsageRepository;

    @Test
    void searchTest() {
        formRepository.deleteAll();
        formUsageRepository.deleteAll();
        Role admin = Role.builder().code("admin-123").title("admin").build();
        Role admin2 = Role.builder().code("admin-us4").title("admin").build();
        Role user = Role.builder().code("super-user").title("user").build();
        FormUsage isr = FormUsage.builder().countryIso("ISR").organisationId(23L).projectId(9L).roles(List.of(admin, user)).build();
        FormUsage us = FormUsage.builder().countryIso("US").roles(List.of(admin2)).build();
        Form firstForm = Form.builder().label("form-1").formUsages(List.of(isr, us)).build();

        Role manager = Role.builder().code("manager-700").title("manager").build();
        FormUsage isr2 = FormUsage.builder().countryIso(null).roles(List.of(manager)).build();
        FormUsage us2 = FormUsage.builder().countryIso("US").organisationId(17L).build();
        Form secondForm = Form.builder().label("form-2").formUsages(List.of(isr2, us2)).build();
        formRepository.save(firstForm);
        formRepository.save(secondForm);
        List<Form> forms = formRepository.findAll(FormSpecifications.formsByUsageLevel("ISR", 23L, 9L, List.of(manager)));
        forms.forEach(System.out::println);
    }
    @Test
    void severalValuesForFieldSearchTest(){
        //List<FormUsage> usages = formUsageRepository.findByOrganisationIdOrOrganisationIdIsNull(23L);
        List<FormUsage> usages = formUsageRepository.findByCountryIsoOrCountryIsoIsNullAndOrganisationIdOrOrganisationIdIsNullAndProjectIdOrProjectIdIsNull("ISR", 23L, 9L);
        usages.forEach(System.out::println);
    }
}
