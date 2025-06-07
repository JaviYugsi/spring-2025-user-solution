package edu.uoc.epcsd.user.architecture;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.lang.ArchRule;
import org.junit.jupiter.api.Test;
import org.springframework.stereotype.Service;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;



public class ServicesNameTest {
    private static final String DOMAIN_SERVICE_PACKAGE = "edu.uoc.epcsd.user.domain.service";



    @Test
    void servicesNameShouldEndWithServiceImpl  () {

        JavaClasses serviceClasses = new ClassFileImporter().importPackages(DOMAIN_SERVICE_PACKAGE);
        ArchRule test = classes().that()
                .resideInAPackage(DOMAIN_SERVICE_PACKAGE)   // Analizamos clases dentro de este paquete
                .and().areAnnotatedWith(Service.class)  // Con esta anotación
                .should().haveSimpleNameEndingWith("ServiceImpl");


        test.check(serviceClasses);
    }

}
