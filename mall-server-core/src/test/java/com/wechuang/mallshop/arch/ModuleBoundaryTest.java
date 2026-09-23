package com.wechuang.mallshop.arch;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.lang.ArchRule;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

/**
 * [healthmall-ext] 模块边界测试（《健康商城重构方案》Phase 0）
 * 规则：
 * 1. 存量模块不得依赖新域包（新能力一律放新域，存量只做最小增量改造）
 * 2. 新域不得调用存量模块的 ServiceImpl（走 Service 接口）
 * 3. 新域之间只允许通过 Service 接口通信，不得深入他域 repository/dao/service.impl/model
 * 4. 新域分层规范：*Controller → controller..，*ServiceImpl → service.impl..，*Repository → repository..
 */
class ModuleBoundaryTest {

    private static final String BASE = "com.wechuang.mallshop.";

    private static final List<String> EXISTING_MODULES = List.of(
            "account", "admin", "analytics", "cms", "invoicing",
            "marketing", "pay", "pt", "shop", "sys", "trade");

    private static final List<String> NEW_DOMAINS = List.of(
            "health", "family", "staff", "dispatch", "svc", "merchant", "chain",
            "settlement", "supplychain", "integration", "audit", "notify");

    private static JavaClasses classes;

    @BeforeAll
    static void importClasses() {
        classes = new ClassFileImporter()
                .withImportOption(new ImportOption.DoNotIncludeTests())
                .importPackages("com.wechuang.mallshop");
    }

    @Test
    @DisplayName("存量模块不得依赖新域包")
    void existingModulesMustNotDependOnNewDomains() {
        for (String existing : EXISTING_MODULES) {
            for (String domain : NEW_DOMAINS) {
                noClasses().that().resideInAPackage(BASE + existing + "..")
                        .should().dependOnClassesThat().resideInAPackage(BASE + domain + "..")
                        .because("新能力必须放新域包，存量模块只允许最小增量改造（见重构方案红线 #1/#3）")
                        .allowEmptyShould(true)
                        .check(classes);
            }
        }
    }

    @Test
    @DisplayName("新域不得调用存量模块的 ServiceImpl，只能走 Service 接口")
    void newDomainsMustNotUseExistingServiceImpl() {
        for (String domain : NEW_DOMAINS) {
            List<String> implPackages = new ArrayList<>();
            for (String existing : EXISTING_MODULES) {
                implPackages.add(BASE + existing + ".service.impl..");
            }
            noClasses().that().resideInAPackage(BASE + domain + "..")
                    .should().dependOnClassesThat().resideInAnyPackage(implPackages.toArray(new String[0]))
                    .because("跨模块调用必须面向 Service 接口，避免与存量实现耦合")
                    .allowEmptyShould(true)
                    .check(classes);
        }
    }

    @Test
    @DisplayName("新域之间只允许通过 Service 接口通信，不得深入他域内部实现")
    void newDomainsMustNotTouchOtherDomainInternals() {
        List<String> internalPatterns = new ArrayList<>();
        for (String source : NEW_DOMAINS) {
            for (String target : NEW_DOMAINS) {
                if (source.equals(target)) {
                    continue;
                }
                internalPatterns.add(BASE + target + ".repository..");
                internalPatterns.add(BASE + target + ".dao..");
                internalPatterns.add(BASE + target + ".service.impl..");
                internalPatterns.add(BASE + target + ".model..");
            }
            noClasses().that().resideInAPackage(BASE + source + "..")
                    .should().dependOnClassesThat().resideInAnyPackage(internalPatterns.toArray(new String[0]))
                    .because("跨域协作走 Service 接口或领域事件（EventPublisher），不得共享他域实体与数据访问层")
                    .allowEmptyShould(true)
                    .check(classes);
            internalPatterns.clear();
        }
    }

    @Test
    @DisplayName("core 自研包不得依赖业务模块（保持与原 jar 一致的自包含性）")
    void coreMustNotDependOnBusinessModules() {
        List<String> businessPackages = new ArrayList<>(EXISTING_MODULES);
        businessPackages.add("common");
        businessPackages.add("audit");
        for (String business : businessPackages) {
            noClasses().that().resideInAPackage(BASE + "core..")
                    .should().dependOnClassesThat().resideInAPackage(BASE + business + "..")
                    .because("core 是自包含基座（对齐原闭源 jar 的依赖边界），只准依赖 Spring/MyBatis-Plus/hutool/JDK")
                    .allowEmptyShould(true)
                    .check(classes);
        }
    }

    @Test
    @DisplayName("新域分层规范：Controller/ServiceImpl/Repository 的包位置")
    void newDomainLayering() {
        for (String domain : NEW_DOMAINS) {
            String domainPackage = BASE + domain + "..";

            classes().that().resideInAPackage(domainPackage)
                    .and().haveSimpleNameEndingWith("Controller")
                    .should().resideInAnyPackage(BASE + domain + ".controller.front..", BASE + domain + ".controller.manage..")
                    .orShould().resideInAPackage(BASE + domain + ".controller..")
                    .because("Controller 按 front/manage 划分（对齐存量惯例）")
                    .allowEmptyShould(true)
                    .check(classes);

            classes().that().resideInAPackage(domainPackage)
                    .and().haveSimpleNameEndingWith("ServiceImpl")
                    .should().resideInAPackage(BASE + domain + ".service.impl..")
                    .allowEmptyShould(true)
                    .check(classes);

            classes().that().resideInAPackage(domainPackage)
                    .and().haveSimpleNameEndingWith("Repository")
                    .should().resideInAPackage(BASE + domain + ".repository..")
                    .allowEmptyShould(true)
                    .check(classes);
        }
    }
}
