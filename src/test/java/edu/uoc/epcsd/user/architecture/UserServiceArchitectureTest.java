package edu.uoc.epcsd.user.architecture;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.lang.syntax.ArchRuleDefinition;
import org.junit.jupiter.api.Test;

public class UserServiceArchitectureTest {

    private static final String USER_SERVICE_IMPL_CLASS = "edu.uoc.epcsd.user.domain.service.UserServiceImpl";
    private static final String DOMAIN_PACKAGE = "edu.uoc.epcsd.user.domain..";
    private static final String INFRASTRUCTURE_PACKAGE = "edu.uoc.epcsd.user.infrastructure..";
    private static final String REST_PACKAGE = "edu.uoc.epcsd.user.application.rest..";

    private final JavaClasses importedClasses = new ClassFileImporter()
            .importPackages("edu.uoc.epcsd.user");

    @Test
    void userServiceImplShouldOnlyDependOnDomainLayer() {
        ArchRuleDefinition.classes()
                .that().haveFullyQualifiedName(USER_SERVICE_IMPL_CLASS) // Nombre de la clase UserService
                .should().onlyDependOnClassesThat()
                .resideInAnyPackage(
                        "java..",                      // Dependencia de objetos Java
                        "org.springframework..",       // Dependencia de objetos de Spring
                        DOMAIN_PACKAGE                 // Dependencia del paquete del dominio
                )
                .check(importedClasses);
    }

    @Test
    void userServiceImplShouldNotDependOnInfrastructureOrRest() {
        ArchRuleDefinition.noClasses()              //Ninguna clase
                .that().haveFullyQualifiedName(USER_SERVICE_IMPL_CLASS) // Con este nombre
                .should().dependOnClassesThat()
                .resideInAnyPackage(
                        INFRASTRUCTURE_PACKAGE,     // Depende del paquete de infraestructura
                        REST_PACKAGE                // Depende del paquete de Rest
                )
                .check(importedClasses);
    }
}
