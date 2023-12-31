resources = [
    "//server/src/main/resources:server-resources",
]

common_deps = [
        "//core/src/main/java/codes/showme/domain/incident:incident",
        "//core/src/main/java/codes/showme/domain/team",
        "//core/src/main/java/codes/showme/domain/account",
        "//server/src/main/java/codes/showme/server/account:account_rest",
        "//server/src/main/java/codes/showme/server/schedule:schedule",
        "//server/src/main/java/codes/showme/server/team",
        "//server/src/main/java/codes/showme/server/springSecurity",
        "//server/src/main/java/codes/showme/server/account/exceptions",
        "//server/src/main/java/codes/showme/server/api:event2_request",
        "//tech-lib:ioc",
        "//spring-ioc-impl:ioc-instance-provider-spring-impl",
        "//tech-lib:email",
        "//tech-lib:cache",
        "//tech-lib:hash",
        "//tech-lib:json",
        "//tech-lib:validation",
        "//tech-lib-impl/src/main/java/codes/showme/techlib/validation:ValidationCodeGeneration",
        "//tech-lib-impl/src/main/java/codes/showme/techlib/json:json",
        "//tech-lib-impl/src/main/java/codes/showme/techlib/hash:HashServiceImpl",
        "//event-impl/src/main/java/codes/showme/domain/account:AccountSignUpEvent",
        "//repository-impl/src/main/java/codes/showme/domain/repository:ebean_config",
        "//repository-impl/src/main/java/codes/showme/domain/account:AccountRepositoryImpl",
        "//repository-impl/src/main/java/codes/showme/domain/team:TeamRepositoryImpl",
        "//repository-impl/src/main/java/codes/showme/domain/escalation:EscalationPolicyRepositoryImpl",
        "//repository-impl/src/main/java/codes/showme/domain/incident:IncidentRepositoryImpl",
        "//repository-impl/src/main/java/codes/showme/domain/schedule:ScheduleRuleRepositoryImpl",
        "@maven//:org_springframework_boot_spring_boot_starter_thymeleaf",
        "@maven//:org_springframework_boot_spring_boot",
        "@maven//:org_springframework_boot_spring_boot_autoconfigure",
        "@maven//:org_springframework_boot_spring_boot_starter_web",
        "@maven//:org_springframework_spring_context",
        "@maven//:org_springframework_spring_webmvc",
        "@maven//:org_springframework_boot_spring_boot_starter_validation",
        "@maven//:jakarta_servlet_jakarta_servlet_api",
        "@maven//:org_springframework_spring_web",
        "@maven//:ch_qos_logback_logback_classic",
        "@maven//:com_fasterxml_jackson_core_jackson_databind",
        "@maven//:com_fasterxml_jackson_datatype_jackson_datatype_jsr310",
        "@maven//:jakarta_persistence_jakarta_persistence_api",
        "@maven//:org_slf4j_slf4j_api",
]