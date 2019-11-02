/*
 * Copyright (C) 2016 - present Juergen Zimmermann, Hochschule Karlsruhe
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

//  Aufrufe
//  1) Microservice uebersetzen und starten
//        .\gradlew -t [bootRun]
//        .\gradlew compileKotlin
//        .\gradlew compileTestKotlin
//
//  2) Microservice als selbstausfuehrendes JAR erstellen und ausfuehren
//        .\gradlew bootJar
//        java -jar build/libs/....jar --spring.profiles.active=dev
//
//  3) Tests und QS
//        .\gradlew test [--rerun-tasks] [--fail-fast] jacocoTestReport
//        .\gradlew ktlint detekt
//        .\gradlew sonarqube -x test
//
//  4) Sicherheitsueberpruefung durch OWASP Dependency Check
//        .\gradlew dependencyCheckAnalyze --info [--warning-mode=all]
//
//  5) "Dependencies Updates"
//        .\gradlew versions
//        .\gradlew dependencyUpdates
//
//  6) API-Dokumentation erstellen (funktioniert NICHT mit proxy.hs-karlruhe.de, sondern nur mit proxyads)
//        .\gradlew dokka
//
//  7) Entwicklerhandbuch in "Software Engineering" erstellen
//        .\gradlew asciidoctor asciidoctorPdf
//
//  8) Projektreport erstellen
//        .\gradlew projectReport
//        .\gradlew -q dependencyInsight --dependency spring-kafka
//        .\gradlew dependencies
//        .\gradlew dependencies --configuration runtimeOnly
//        .\gradlew buildEnvironment
//        .\gradlew htmlDependencyReport
//
//  9) Report ueber die Lizenzen der eingesetzten Fremdsoftware
//        .\gradlew generateLicenseReport
//
//  10)Docker-Image
//        .\gradlew jib
//
//  11) pom.xml generieren
//        .\gradlew generatePomFileForHskaPublication
//
//  12) Daemon abfragen und stoppen
//        .\gradlew --status
//        .\gradlew --stop
//
//  13) Verfuegbare Tasks auflisten
//        .\gradlew tasks
//
//  14) Properties auflisten
//        .\gradlew properties
//        .\gradlew dependencyManagementProperties
//
//  15) Hilfe einschl. Typinformation
//        .\gradlew help --task bootRun
//
//  16) Initialisierung des Gradle Wrappers in der richtigen Version
//      (dazu ist ggf. eine Internetverbindung erforderlich)
//        gradle wrapper --gradle-version 5.6.3
//        gradle wrapper --gradle-version 6.0-rc-1
//          --distribution-type=all

// mvn package [-DskipTests]
// java -jar target\kunde-1.0-exec.jar --illegal-access=deny -Dspring.profiles.active=dev
// mvn dependency:tree

// https://github.com/gradle/kotlin-dsl/tree/master/samples
// https://docs.gradle.org/current/userguide/kotlin_dsl.html
// https://docs.gradle.org/current/userguide/task_configuration_avoidance.html
// https://guides.gradle.org/migrating-build-logic-from-groovy-to-kotlin

import org.asciidoctor.gradle.jvm.pdf.AsciidoctorPdfTask
import org.asciidoctor.gradle.jvm.AsciidoctorTask
import org.jetbrains.dokka.gradle.DokkaTask
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.springframework.boot.gradle.tasks.bundling.BootJar
import org.springframework.boot.gradle.tasks.run.BootRun

buildscript {
    dependencies {
        //classpath(kotlin("gradle-plugin", Versions.Plugins.kotlin))
        //classpath(kotlin("allopen", Versions.Plugins.allOpen))
        //classpath(kotlin("noarg", Versions.Plugins.noArg))

        classpath("org.springframework.boot:spring-boot-gradle-plugin:${Versions.Plugins.springBoot}")

        //classpath("org.owasp:dependency-check-gradle:${Versions.Plugins.owaspDependencyCheck}")
    }
}

plugins {
    idea
    jacoco
    `project-report`
    `maven-publish`

    // FIXME https://youtrack.jetbrains.com/issue/KT-30227
    // https://dl.bintray.com/kotlin/kotlin-dev/org/jetbrains/kotlin/kotlin-compiler
    kotlin("jvm") version Versions.Plugins.kotlin
    // fuer Spring Beans
    kotlin("plugin.allopen") version Versions.Plugins.allOpen
    // fuer @ConfigurationProperties mit "data class"
    kotlin("plugin.noarg") version Versions.Plugins.noArg

    //id("org.springframework.boot") version Versions.Plugins.springBoot

    id("com.adarshr.test-logger") version Versions.Plugins.testLogger

    // https://github.com/arturbosch/detekt
    // FIXME https://github.com/arturbosch/detekt/issues/2040
    id("io.gitlab.arturbosch.detekt") version Versions.Plugins.detekt

    // http://redirect.sonarsource.com/doc/gradle.html
    id("org.sonarqube") version Versions.Plugins.sonarqube

    // https://github.com/Kotlin/dokka
    // FIXME https://github.com/Kotlin/dokka/issues/515
    id("org.jetbrains.dokka") version Versions.Plugins.dokka

    // https://github.com/nwillc/vplugin
    id("com.github.nwillc.vplugin") version Versions.Plugins.vplugin

    // https://github.com/ben-manes/gradle-versions-plugin
    // FIXME https://github.com/ben-manes/gradle-versions-plugin/issues/357
    id("com.github.ben-manes.versions") version Versions.Plugins.versions

    // https://github.com/GoogleContainerTools/jib/tree/master/jib-gradle-plugin
    // FIXME https://github.com/GoogleContainerTools/jib/issues/2086
    id("com.google.cloud.tools.jib") version Versions.Plugins.jib

    id("com.fizzpod.sweeney") version Versions.Plugins.sweeney

    // https://github.com/jeremylong/dependency-check-gradle
    // FIXME https://github.com/jeremylong/dependency-check-gradle/issues/155
    id("org.owasp.dependencycheck") version Versions.Plugins.owaspDependencyCheck

    id("org.asciidoctor.jvm.convert") version Versions.Plugins.asciidoctorConvert
    id("org.asciidoctor.jvm.pdf") version Versions.Plugins.asciidoctorPdf

    // https://github.com/intergamma/gradle-zap
    //id("net.intergamma.gradle.gradle-zap-plugin") version Versions.Plugins.zap

    // https://github.com/jk1/Gradle-License-Report
    // FIXME https://github.com/jk1/Gradle-License-Report/issues/162
    id("com.github.jk1.dependency-license-report") version Versions.Plugins.jk1DependencyLicenseReport

    // https://github.com/jaredsburrows/gradle-license-plugin
    //id("com.jaredsburrows.license") version Versions.Plugins.jaredsBurrowsLicense
}

apply(plugin = "org.springframework.boot")

defaultTasks = mutableListOf("bootRun")
group = "de.hska"
version = "1.0"

repositories {
    mavenCentral()
    maven("https://dl.bintray.com/kotlin/kotlin-eap")
    // https://github.com/spring-projects/spring-framework/wiki/Spring-repository-FAQ
    // https://github.com/spring-projects/spring-framework/wiki/Release-Process
    maven("https://repo.spring.io/libs-milestone") {
        // reactor-kotlin-extensions hat keine pom-Datei
        // https://github.com/reactor/reactor-kotlin-extensions/issues/2
        metadataSources {
            mavenPom()
            artifact()
            //ignoreGradleMetadataRedirection()
        }
    }
    //maven("http://repo.spring.io/libs-milestone-local")
    maven("https://repo.spring.io/release")

    mavenCentral()
    //jcenter()
    //google()

    // Snapshots von Spring Framework, Spring Boot, Spring Data, Spring Security und Spring Cloud
    maven("https://repo.spring.io/libs-snapshot")
    // Snapshots von JaCoCo
    //maven("https://oss.sonatype.org/content/repositories/snapshots")
}

/**
 * Configuration-Objekt für PlantUML, um Artifakte zu definieren, die in der ANT-Task benötigt werden
 */
val plantumlCfg: Configuration by configurations.creating

/**
 * Configuration-Objekt für ktlint, um Artifakte zu definieren, die in der JavaExec-Task benötigt werden
 */
val ktlintCfg: Configuration by configurations.creating {
    exclude(module = "ktlint-test")
    exclude(group = "org.apache.maven")
}

// https://docs.gradle.org/current/userguide/java_library_plugin.html#sec:java_library_separation
dependencies {
    // https://docs.gradle.org/current/userguide/managing_transitive_dependencies.html#sec:bom_import
    // https://github.com/spring-cloud/spring-cloud-release/blob/master/docs/src/main/asciidoc/spring-cloud-starters.adoc#using-spring-cloud-dependencies-with-spring-io-platform
    // https://github.com/JetBrains/kotlin/blob/master/libraries/tools/kotlin-bom/pom.xml
    implementation(platform("org.jetbrains.kotlin:kotlin-bom:${Versions.kotlin}"))
    //implementation(platform("io.projectreactor:reactor-bom:${Versions.reactorBom}"))
    //implementation(platform("org.springframework:spring-framework-bom:${Versions.springBom}"))

    //implementation(platform("io.r2dbc:r2dbc-bom:${Versions.r2dbcBom}"))
    implementation(platform("org.springframework.boot.experimental:spring-boot-bom-r2dbc:${Versions.springBootR2dbcBom}"))
    //implementation(platform("org.springframework.data:spring-data-releasetrain:${Versions.springDataReleasetrain}"))

    //implementation(platform("org.springframework.security:spring-security-bom:${Versions.springSecurityBom}"))
    //implementation(platform("org.springframework.integration:spring-integration-bom:${Versions.springIntegrationBom}"))
    implementation(platform("org.junit:junit-bom:${Versions.junitJupiterBom}"))
    implementation(platform("org.springframework.boot:spring-boot-starter-parent:${Versions.springBoot}"))
    //implementation(platform("org.springframework.cloud:spring-cloud-function-dependencies:${Versions.springCloudFunctionBom}"))
    //implementation(platform("org.springframework.cloud:spring-cloud-config-dependencies:${Versions.springCloudConfigBom}"))
    //implementation(platform("org.springframework.cloud:spring-cloud-consul-dependencies:${Versions.springCloudConsulBom}"))
    //implementation(platform("org.springframework.cloud:spring-cloud-circuitbreaker-dependencies:${Versions.springCloudCircuitbreakerBom}"))
    //implementation(platform("org.springframework.cloud:spring-cloud-stream-dependencies:${Versions.springCloudStreamBom}"))
    // https://github.com/apache/incubator-zipkin-brave
    implementation(platform("io.zipkin.brave:brave-bom:${Versions.braveBom}"))
    //implementation(platform("org.springframework.cloud:spring-cloud-sleuth-dependencies:${Versions.springCloudSleuthBom}"))
    implementation(platform("org.springframework.cloud:spring-cloud-dependencies:${Versions.springCloud}"))

    implementation(kotlin("stdlib-jdk8"))
    implementation(kotlin("reflect"))
    implementation("io.projectreactor.kotlin:reactor-kotlin-extensions")
    implementation("io.projectreactor:reactor-tools")
    implementation("io.projectreactor.tools:blockhound:${Versions.blockhound}")
    implementation("com.fasterxml.jackson.dataformat:jackson-dataformat-yaml:${Versions.jackson}")
    // Validierung von Methodenargumenten
    implementation("com.thoughtworks.paranamer:paranamer:${Versions.paranamer}")

    implementation("org.springframework.boot:spring-boot-starter-webflux")
    // Tomcat statt Netty (Default "Web Engine" in spring-boot-starter-webflux)
    implementation("org.springframework.boot:spring-boot-starter-tomcat") {
        exclude(group = "org.apache.tomcat.embed", module = "tomcat-embed-el")
        exclude(group = "org.apache.tomcat.embed", module = "tomcat-embed-websocket")
    }
    implementation("org.springframework.boot:spring-boot-starter-json")
    implementation("org.springframework.boot:spring-boot-starter-validation") {
        exclude(group = "org.apache.tomcat.embed", module = "tomcat-embed-el")
    }
    implementation("org.springframework.boot.experimental:spring-boot-starter-data-r2dbc")
    //implementation("org.springframework:spring-jdbc")
    implementation("io.r2dbc:r2dbc-postgresql")
    implementation("org.springframework.boot:spring-boot-starter-data-mongodb-reactive")

    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.boot:spring-boot-starter-mail")
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springframework.hateoas:spring-hateoas")

    implementation("org.springframework.cloud:spring-cloud-starter-config")
    // FIXME https://github.com/spring-cloud/spring-cloud-consul/issues/562
    implementation("org.springframework.cloud:spring-cloud-starter-consul-discovery") {
        exclude(group = "org.springframework.cloud", module = "spring-cloud-netflix-hystrix")
        exclude(group = "org.springframework.cloud", module = "spring-cloud-starter-netflix-ribbon")
    }
    implementation("org.springframework.cloud:spring-cloud-starter-circuitbreaker-reactor-resilience4j")
    implementation("org.springframework.cloud:spring-cloud-starter-stream-kafka")
    //implementation("org.springframework.cloud:spring-cloud-starter-stream-rabbit")
    implementation("org.springframework.cloud:spring-cloud-starter-zipkin") {
        exclude(group = "io.zipkin.brave", module = "brave-instrumentation-spring-rabbit")
        exclude(group = "io.zipkin.brave", module = "brave-instrumentation-spring-webmvc")
        exclude(group = "io.zipkin.brave", module = "brave-instrumentation-jms")
    }

    implementation("org.springframework.boot:spring-boot-starter-thymeleaf")

    // https://docs.spring.io/spring/docs/current/spring-framework-reference/core.html#beans-scanning-index
    // generiert Index-Datei META-INF/spring.components fuer 500+ Klassen statt Scanning des Classpath
    // in IntelliJ: spring-context-indexer muss als "annotation processor" registriert werden
    //compileOnly("org.springframework:spring-context-indexer")

    // https://www.vojtechruzicka.com/spring-boot-devtools
    runtimeOnly("org.springframework.boot:spring-boot-devtools:${Versions.springBoot}")
    runtimeOnly("com.fasterxml.jackson.module:jackson-module-kotlin")

    testImplementation("org.junit.jupiter:junit-jupiter")
    testImplementation("org.assertj:assertj-core")
    testImplementation("io.mockk:mockk:${Versions.mockk}")
    testImplementation("org.springframework.boot:spring-boot-starter-test") {
        exclude(group = "org.junit.vintage", module = "junit-vintage-engine")
        exclude(group = "junit", module = "junit")
        exclude(group = "org.hamcrest", module = "hamcrest")
        exclude(group = "org.mockito", module = "mockito-core")
        exclude(group = "org.skyscreamer", module = "jsonassert")
        exclude(group = "org.xmlunit", module = "xmlunit-core")
    }
    testImplementation("org.springframework.security:spring-security-test")

    ktlintCfg("com.pinterest:ktlint:${Versions.ktlint}") {

    }
    // https://youtrack.jetbrains.net/issue/KT-27463
    constraints {
        ktlintCfg("org.apache.httpcomponents:httpclient:${Versions.httpClientKtlint}")
    }

    plantumlCfg("net.sourceforge.plantuml:plantuml:${Versions.plantuml}")
    plantumlCfg("org.apache.ant:ant-junit:${Versions.antJunit}")
}

configurations.all {
    resolutionStrategy.eachDependency {
        if (requested.group == "org.jetbrains" && requested.name == "annotations") {
            useVersion(Versions.annotations)
        }

        //if (requested.name == "spring-cloud-config-client" || requested.name == "spring-cloud-starter-config") {
        //    useVersion(Versions.springCloudConfigBom)
        //}
        //if (requested.name == "spring-cloud-consul-core" || requested.name == "spring-cloud-consul-discovery" ||
        //    requested.name == "spring-cloud-starter-consul" ||
        //    requested.name == "spring-cloud-starter-consul-discovery") {
        //    useVersion(Versions.springCloudConsulBom)
        //}
        //if (requested.name == "spring-cloud-loadbalancer") {
        //    useVersion(Versions.springCloudLoadBalancer)
        //}
        //if (requested.name == "spring-cloud-function-context") {
        //    useVersion(Versions.springCloudFunctionBom)
        //}
        //if (requested.name == "spring-cloud-stream" || requested.name == "spring-cloud-stream-binder-kafka" ||
        //    requested.name == "spring-cloud-stream-binder-kafka-core" ||
        //    requested.name == "spring-cloud-starter-stream-kafka") {
        //    useVersion(Versions.springCloudStream)
        //}
        //if (requested.name == "spring-cloud-starter-zipkin" || requested.name == "spring-cloud-sleuth-zipkin" ||
        //    requested.name == "spring-cloud-sleuth-core" || requested.name == "spring-cloud-starter-sleuth") {
        //    useVersion(Versions.springCloudSleuthBom)
        //}
        //if (requested.name == "spring-integration-core" || requested.name == "spring-integration-jmx") {
        //    useVersion(Versions.springIntegrationBom)
        //}
        //if (requested.name == "spring-integration-kafka") {
        //    useVersion(Versions.springIntegrationKafka)
        //}

        //if (requested.name == "r2dbc-postgresql") {
        //    useVersion(Versions.r2dbcPostgresql)
        //}
        if (requested.name == "mongodb-driver" || requested.name == "mongodb-driver-async") {
            useVersion(Versions.mongodb)
        }

        //if (requested.name == "reactive-streams") {
        //    useVersion(Versions.reactiveStreams)
        //}
        if (requested.name == "hibernate-validator") {
            useVersion(Versions.hibernateValidator)
        }
        //if (requested.group == "com.fasterxml.jackson.core" || requested.group == "com.fasterxml.jackson.datatype" ||
        //    requested.group == "com.fasterxml.jackson.dataformat" ||
        //    requested.group == "com.fasterxml.jackson.module") {
        //    useVersion(Versions.jackson)
        //}
        // jackson-module-kotlin
        if (requested.name == "kotlin-reflect") {
            useVersion(Versions.kotlin)
        }
        //if (requested.name == "jakarta.mail") {
        //    useVersion(Versions.jakartaMail)
        //}
        if (requested.name == "jakarta.validation-api") {
            useVersion(Versions.jakartaValidationApi)
        }
        //if (requested.name == "mongodb-driver-reactivestreams") {
        //    useVersion(Versions.mongoDriverReactivestreams)
        //}
        if (requested.name == "spring-security-rsa") {
            useVersion(Versions.springSecurityRsa)
        }
        if (requested.group == "org.bouncycastle") {
            useVersion(Versions.bouncycastle)
        }
        //if (requested.name == "spring-hateoas") {
        //    useVersion(Versions.springHateoas)
        //}
        //if (requested.name == "spring-kafka") {
        //    useVersion(Versions.springKafka)
        //}
        //if (requested.name == "kafka-clients") {
        //    useVersion(Versions.kafka)
        //}
        // org.springframework.cloud:pring-cloud-circuitbreaker-dependencies
        if (requested.group == "io.github.resilience4j") {
            useVersion(Versions.resilience4j)
        }
        if (requested.group == "org.apache.tomcat.embed") {
            useVersion(Versions.tomcat)
        }
        //if (requested.name == "thymeleaf-spring5" || requested.name == "thymeleaf") {
        //    useVersion(Versions.thymeleaf)
        //}
        //if (requested.name == "assertj-core") {
        //    useVersion(Versions.assertj)
        //}
    }
}

allOpen {
    // FIXME Funktionale Bean Definition:
    // wegen @SpringBootApplication
    //annotation("org.springframework.context.annotation.Configuration")
    // Service-Klassen
    //annotation("org.springframework.validation.annotation.Validated")

    annotation("org.springframework.stereotype.Component")
    annotation("org.springframework.stereotype.Service")
    annotation("org.springframework.boot.context.properties.ConfigurationProperties")
}

noArg {
    annotation("org.springframework.boot.context.properties.ConfigurationProperties")
}

sweeney {
    enforce(mapOf("type" to "gradle", "expect" to "[5.6.2,)"))
    // FIXME https://youtrack.jetbrains.com/issue/IDEA-219433
    enforce(mapOf("type" to "jdk", "expect" to "[12.0.2,13)"))
    validate()
}

// Java 13
tasks.withType<JavaCompile> { options.compilerArgs.add("--enable-preview") }

tasks.withType<KotlinCompile> {
    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_12.majorVersion
        verbose = true
        freeCompilerArgs = listOf("-Xjsr305=strict")
        allWarningsAsErrors = true
        // ggf. wegen Kotlin-Daemon: %TEMP%\kotlin-daemon.* und %LOCALAPPDATA%\kotlin\daemon
        // https://youtrack.jetbrains.com/issue/KT-18300
        //  $env:LOCALAPPDATA\kotlin\daemon
        //  $env:TEMP\kotlin-daemon.<ZEITSTEMPEL>
    }
}

tasks.test{
    useJUnitPlatform {
        includeEngines("junit-jupiter")

        includeTags("rest", "multimediaRest", "streamingRest", "service")
        //includeTags("rest")
        //includeTags("multimediaRest")
        //includeTags("streamingRest")
        //includeTags("service")

        //excludeTags("service")
    }

    // Java 13
    //jvmArgs = listOf("--enable-preview")

    //filter {
    //    includeTestsMatching(includeTests)
    //}

    systemProperty("javax.net.ssl.trustStore", "./src/main/resources/truststore.p12")
    systemProperty("javax.net.ssl.trustStorePassword", "zimmermann")
    systemProperty("junit.platform.output.capture.stdout", true)
    systemProperty("junit.platform.output.capture.stderr", true)

    // https://docs.gradle.org/current/userguide/java_testing.html#sec:debugging_java_tests
    // https://www.jetbrains.com/help/idea/run-debug-configuration-junit.html
    // https://docs.gradle.org/current/userguide/java_testing.html#sec:debugging_java_tests
    //debug = true

    // damit nach den Tests immer ein HTML-Report von JaCoCo erstellt wird
    finalizedBy(tasks.jacocoTestReport)
}

// https://docs.gradle.org/current/userguide/task_configuration_avoidance.html
tasks.named<BootRun>("bootRun") {
    jvmArgs = ArrayList(jvmArgs).apply {
        // FIXME https://github.com/spring-projects/spring-framework/issues/23288
        add("--illegal-access=deny")
        add("-Dspring.profiles.active=dev")
        add("-Dspring.config.location=classpath:/bootstrap.yml,classpath:/application.yml,classpath:/application-dev.yml")
        add("-Djavax.net.ssl.trustStore=${System.getProperty("user.dir")}/src/main/resources/truststore.p12")
        add("-Djavax.net.ssl.trustStorePassword=zimmermann")
        // Hotspot Compiler: aggressivere Optimierung als der Client-Compiler
        //add("-server")
        // GraalVM Compiler statt C2 (durch -server)
        // Beachte für Node oder Python: https://github.com/oracle/graal/releases
        //add("-XX:+UnlockExperimentalVMOptions")
        //add("-XX:+EnableJVMCI")
        //add("-XX:+UseJVMCICompiler")
        // Logging für den Garbage Collector
        //add("-Xlog:gc")
        // FIXME https://github.com/reactor/BlockHound/issues/33
        //add("-XX:+AllowRedefinitionToAddDeleteMethods")
        //add("-noverify")
        // Remote Debugger:   .\gradlew bootRun --debug-jvm
        //add("-verbose:class")
        //add("-agentlib:jdwp=transport=dt_socket,server=y,suspend=y,address=5005")
    }
}

tasks.named<BootJar>("bootJar") {
    doLast {
        println("")
        println("Aufruf der ausfuehrbaren JAR-Datei:")
        println("java -jar build/libs/${archiveFileName.get()} --spring.profiles.active=dev")
        println("")
    }
}

jacoco {
    //toolVersion = Versions.jacocoVersion
}

//jacocoTestReport {
//    // Default: nur HTML-Report im Verzeichnis $buildDir/reports/jacoco
//    // XML-Report fuer CI, z.B. Jenkins
//    reports {
//        xml.isEnabled = true
//        html.isEnabled = true
//    }
//}
tasks.getByName<JacocoReport>("jacocoTestReport") {
    reports {
        xml.isEnabled = true
        html.isEnabled = true
    }
    // afterEvaluate gibt es nur bei getByName<> ("eager"), nicht bei named<> ("lazy")
    // https://docs.gradle.org/5.0/release-notes.html#configuration-avoidance-api-disallows-common-configuration-errors
    afterEvaluate {
        classDirectories.setFrom(files(classDirectories.files.map {
            fileTree(it) { exclude("**/config/**", "**/entity/**") }
        }))
    }
}

// Docker-Image durch jib von Google
// https://github.com/GoogleContainerTools/jib/tree/master/jib-gradle-plugin#example
jib {
    from {
        image = "openjdk:alpine"
    }
    to {
        image = "my-docker-id/hska-${project.name}"
    }
}

// https://android.github.io/kotlin-guides/style.html
// https://kotlinlang.org/docs/reference/coding-conventions.html
// https://www.jetbrains.com/help/idea/code-style-kotlin.html
// https://github.com/android/kotlin-guides/issues/37
// https://github.com/shyiko/ktlint
val ktlint by tasks.registering(JavaExec::class) {
    group = "verification"

    classpath = ktlintCfg
    main = "com.pinterest.ktlint.Main"
    //https://github.com/pinterest/ktlint/blob/master/ktlint/src/main/kotlin/com/github/shyiko/ktlint/Main.kt
    args = listOf(
        "--verbose",
        "--reporter=plain",
        "--reporter=checkstyle,output=$buildDir/reports/ktlint.xml",
        "src/**/*.kt")
}
tasks.check { dependsOn(ktlint) }

detekt {
    buildUponDefaultConfig = true
    failFast = true
    config = files(project.rootDir.resolve("config/detekt.yml"))
    val reportsDir = "$buildDir/reports"
    reports {
        xml {
            destination = file("$reportsDir/detekt.xml")
        }
        html {
            destination = file("$reportsDir/detekt.html")
        }
    }
    idea {
        path = "${System.getProperty("user.home")}/.IntelliJIdea${Versions.intellij}"
        codeStyleScheme = "$projectDir/.idea/codestyles/Default.xml"
    }
}

// http://stackoverflow.com/questions/34143530/sonar-maven-analysis-class-not-found#answer-34151150
sonarqube {
    properties {
        property("sonar.scm.disabled", true)
        // https://docs.sonarqube.org/display/SONAR/Authentication
        property("sonar.login", "admin")
        property("sonar.password", "admin")
    }
}

// https://github.com/jeremylong/DependencyCheck/blob/master/src/site/markdown/dependency-check-gradle/configuration.md
// https://github.com/jeremylong/DependencyCheck/issues/1732
dependencyCheck {
    suppressionFile = "$projectDir/config/owasp.xml"
    skipConfigurations = mutableListOf("ktlintCfg", "detekt", "jacocoAgent", "jacocoAnt", "plantumlCfg")
    data {
        directory = "C:/Zimmermann/owasp-dependency-check"
        username = "dc"
        password = "p"
    }

    analyzedTypes = listOf("jar")
    analyzers {
        // nicht benutzte Analyzer
        nuspecEnabled = false
        assemblyEnabled = false
        golangDepEnabled = false
        golangModEnabled = false
        cocoapodsEnabled = false
        swiftEnabled = false
        bundleAuditEnabled = false
        pyDistributionEnabled = false
        pyPackageEnabled = false
        rubygemsEnabled = false
        cmakeEnabled = false
        autoconfEnabled = false
        composerEnabled = false
        nodeEnabled = false
        nodeAuditEnabled = false
        nugetconfEnabled = false
    }

    format = org.owasp.dependencycheck.reporting.ReportGenerator.Format.ALL
}

val plantuml by tasks.registering {
    doLast {
        //https://github.com/gradle/kotlin-dsl/blob/master/samples/ant/build.gradle.kts
        ant.withGroovyBuilder {
            "taskdef"(
                "name" to "plantuml",
                "classname" to "net.sourceforge.plantuml.ant.PlantUmlTask",
                "classpath" to plantumlCfg.asPath)

            // PNG-Bilder fuer HTML bei AsciiDoctor und Dokka
            mkdir("$buildDir/docs/images")
            "plantuml"(
                "output" to "$buildDir/docs/images",
                "graphvizDot" to "C:\\Zimmermann\\Graphviz\\bin\\dot.exe",
                "verbose" to true) {
                "fileset"("dir" to "$projectDir/src/main/kotlin") {
                    "include"("name" to "**/*.puml")
                }
            }

            // PNG-Bilder kopieren fuer AsciiDoctor mit dem IntelliJ-Plugin
            mkdir("$projectDir/config/images")
            "copy"("todir" to "$projectDir/config/images") {
                "fileset"("dir" to "$buildDir/docs/images") {
                    "include"("name" to "*.png")
                }
            }
        }
    }
}

tasks.named<DokkaTask>("dokka") {
    configuration {
        includes = listOf("Module.md")
        apiVersion = "1.3"
        languageVersion = apiVersion
        noStdlibLink = true
        noJdkLink = true
    }

    dependsOn(plantuml)
}

// https://github.com/asciidoctor/asciidoctor-gradle-plugin/tree/release_2_0_0_alpha_5
tasks.named<AsciidoctorTask>("asciidoctor") {
    asciidoctorj {
        setVersion(Versions.asciidoctorj)
        //requires("asciidoctor-diagram")
    }

    setSourceDir(file("config/docs"))
    //setOutputDir(file("$buildDir/docs/asciidoc"))
    logDocuments = true

    //attributes(mutableMapOf(
    //        "source-higlighter" to "coderay",
    //        "coderay-linenums-mode" to "table",
    //        "toc" to "left",
    //        "icon" to "font",
    //        "linkattrs" to true,
    //        "encoding" to "utf-8"))

    doLast {
        val separator = System.getProperty("file.separator")
        println("Das Entwicklerhandbuch ist in $buildDir${separator}docs${separator}asciidoc${separator}entwicklerhandbuch.html")
    }

    dependsOn(plantuml)
}

tasks.named<AsciidoctorPdfTask>("asciidoctorPdf") {
    asciidoctorj {
        setVersion(Versions.asciidoctorj)
        modules.pdf.setVersion(Versions.asciidoctorjPdf)
    }

    setSourceDir(file("config/docs"))
    //outputDir file("${buildDir}/docs/asciidocPdf")
    attributes(mutableMapOf("imagesdir" to "$buildDir/docs/images"))
    logDocuments = true

    doLast {
        val separator = System.getProperty("file.separator")
        println("Das Entwicklerhandbuch ist in $buildDir${separator}docs${separator}asciidocPdf${separator}entwicklerhandbuch.pdf")
    }

    dependsOn(plantuml)
}

licenseReport {
    configurations = arrayOf("runtimeClasspath")
}

tasks.withType<GenerateMavenPom>().configureEach {
    destination = file("$buildDir/pom.xml")
}

// build\libs\kunde-1.0-sources.jar
val sourcesJar by tasks.registering(Jar::class) {
    from(sourceSets.main.get().allSource)
    archiveClassifier.set("sources")
}

// https://docs.gradle.org/current/userguide/publishing_maven.html
publishing {
    repositories {
        maven { url = uri("$buildDir/repo") }
    }
    publications {
        create<MavenPublication>("hska") {
            from (components["kotlin"])
            artifact(sourcesJar.get())

            pom {
                description.set("Beispiel fuer Softwarearchitektur")
                inceptionYear.set("2016")
                packaging = "jar"
                organization {
                    name.set("Hochschule Karlsruhe - Technik und Wirtschaft")
                    url.set("http://www.hs-karlsruhe.de")
                }
                licenses {
                    license {
                        name.set("GNU General Public License, Version 3 (GPLv3)")
                        url.set("https://www.gnu.org/licenses/gpl-3.0.de.html")
                    }
                }
                developers {
                    developer {
                        name.set("Juergen Zimmermann")
                        email.set("Juergen.Zimmermann@HS-Karlsruhe.de")
                    }
                }
                properties.set(mapOf(
                    "kotlin.version" to Versions.kotlin,
                    "spring-boot.version" to Versions.springBoot,
                    "spring-cloud.version" to Versions.springCloud,

                    "hibernate-validator.version" to Versions.hibernateValidator,
                    //"jackson.version" to Versions.jackson,
                    "jakarta-validation.version" to Versions.jakartaValidationApi
                ))

                withXml {
                    val root = asNode()

                    val parent = root.appendNode("parent")
                    parent.appendNode("groupId", "org.springframework.boot")
                    parent.appendNode("artifactId", "spring-boot-dependencies")
                    parent.appendNode("version", Versions.springBoot)

                    val repositories = root.appendNode("repositories")

                    val kotlinEapRepo = repositories.appendNode("repository")
                    kotlinEapRepo.appendNode("id", "kotlin-eap")
                    kotlinEapRepo.appendNode("url", "http://dl.bintray.com/kotlin/kotlin-eap")
                    kotlinEapRepo.appendNode("releases").appendNode("enabled", "false")
                    kotlinEapRepo.appendNode("snapshots").appendNode("enabled", "true")

                    val springMilestonesRepo = repositories.appendNode("repository")
                    springMilestonesRepo.appendNode("id", "spring-milestones")
                    springMilestonesRepo.appendNode("url", "https://repo.spring.io/libs-milestone")

                    val springSnapshotsRepo = repositories.appendNode("repository")
                    springSnapshotsRepo.appendNode("id", "spring-snapshots")
                    springSnapshotsRepo.appendNode("url", "https://repo.spring.io/libs-snapshot")
                    springSnapshotsRepo.appendNode("releases").appendNode("enabled", "false")
                    springSnapshotsRepo.appendNode("snapshots").appendNode("enabled", "true")

                    val pluginRepositories = root.appendNode("pluginRepositories")

                    val kotlinEapPluginRepository = pluginRepositories.appendNode("pluginRepository")
                    kotlinEapPluginRepository.appendNode("id", "kotlin-eap")
                    kotlinEapPluginRepository.appendNode("url", "http://dl.bintray.com/kotlin/kotlin-eap")
                    kotlinEapPluginRepository.appendNode("releases").appendNode("enabled", "false")
                    kotlinEapPluginRepository.appendNode("snapshots").appendNode("enabled", "true")

                    val springPluginRepository = pluginRepositories.appendNode("pluginRepository")
                    springPluginRepository.appendNode("id", "spring-milestones")
                    springPluginRepository.appendNode("url", "https://repo.spring.io/libs-milestone")

                    val build = root.appendNode("build")
                    build.appendNode("sourceDirectory", "\${project.basedir}/src/main/kotlin")
                    build.appendNode("testSourceDirectory", "\${project.basedir}/src/test/kotlin")

                    val plugins = build.appendNode("plugins")

                    val surefirePlugin = plugins.appendNode("plugin")
                    surefirePlugin.appendNode("groupId", "org.apache.maven.plugins")
                    surefirePlugin.appendNode("artifactId", "maven-surefire-plugin")
                    surefirePlugin.appendNode("version", "2.12.4")
                    surefirePlugin.appendNode("configuration")
                        .appendNode("skipTests", "true")

                    val kotlinPlugin = plugins.appendNode("plugin")
                    kotlinPlugin.appendNode("groupId", "org.jetbrains.kotlin")
                    kotlinPlugin.appendNode("artifactId", "kotlin-maven-plugin")
                    kotlinPlugin.appendNode("version", Versions.Plugins.kotlin)

                    val kotlinExecutions = kotlinPlugin.appendNode("executions")
                    val kotlinCompile = kotlinExecutions.appendNode("execution")
                    kotlinCompile.appendNode("id", "compile")
                    kotlinCompile.appendNode("goals")
                        .appendNode("goal", "compile")

                    val kotlinTestCompile = kotlinExecutions.appendNode("execution")
                    kotlinTestCompile.appendNode("id", "test-compile")
                    kotlinTestCompile.appendNode("goals")
                        .appendNode("goal", "test-compile")
                    kotlinTestCompile.appendNode("configuration")
                        .appendNode("skip","true")

                    val kotlinConfiguration = kotlinPlugin.appendNode("configuration")

                    kotlinConfiguration.appendNode("args")
                        .appendNode("arg", "-Xjsr305=strict")
                    kotlinConfiguration.appendNode("languageVersion", "1.3")
                    kotlinConfiguration.appendNode("apiVersion", "1.3")
                    kotlinConfiguration.appendNode("jvmTarget", "9")

                    val kotlinCompilerPlugin = kotlinConfiguration.appendNode("compilerPlugins")
                    kotlinCompilerPlugin.appendNode("plugin", "all-open")
                    kotlinCompilerPlugin.appendNode("plugin", "no-arg")

                    val kotlinPluginOptions = kotlinConfiguration.appendNode("pluginOptions")
                    kotlinPluginOptions.appendNode("option", "all-open:annotation=org.springframework.stereotype.Component")
                    kotlinPluginOptions.appendNode("option", "all-open:annotation=org.springframework.stereotype.Service")
                    kotlinPluginOptions.appendNode("option", "all-open:annotation=org.springframework.boot.context.properties.ConfigurationProperties")
                    kotlinPluginOptions.appendNode("option", "no-arg:annotation=org.springframework.boot.context.properties.ConfigurationProperties")

                    val kotlinPluginDeps = kotlinPlugin.appendNode("dependencies")
                    val allOpenPluginDependency = kotlinPluginDeps.appendNode("dependency")
                    allOpenPluginDependency.appendNode("groupId", "org.jetbrains.kotlin")
                    allOpenPluginDependency.appendNode("artifactId", "kotlin-maven-allopen")
                    allOpenPluginDependency.appendNode("version", Versions.Plugins.kotlin)
                    val noArgPluginDependency = kotlinPluginDeps.appendNode("dependency")
                    noArgPluginDependency.appendNode("groupId", "org.jetbrains.kotlin")
                    noArgPluginDependency.appendNode("artifactId", "kotlin-maven-noarg")
                    noArgPluginDependency.appendNode("version", Versions.Plugins.kotlin)

                    val springBootPlugin = plugins.appendNode("plugin")
                    springBootPlugin.appendNode("groupId", "org.springframework.boot")
                    springBootPlugin.appendNode("artifactId", "spring-boot-maven-plugin")
                    springBootPlugin.appendNode("version", Versions.Plugins.springBoot)
                    val springBootExecution = springBootPlugin.appendNode("executions")
                        .appendNode("execution")
                    springBootExecution.appendNode("id", "repackage")
                    springBootExecution.appendNode("goals")
                        .appendNode("goal", "repackage")
                    val springBootConfiguration = springBootExecution.appendNode("configuration")
                    springBootConfiguration.appendNode("classifier", "exec")
                    springBootConfiguration.appendNode("mainClass", "de.hska.kunde.ApplicationKt")

                    val dependenciesList = root.get("dependencies") as groovy.util.NodeList
                    if (dependenciesList.size != 0) {
                        val dependenciesNode = dependenciesList[0] as groovy.util.Node
                        val dependencies = dependenciesNode.children() as groovy.util.NodeList
                        dependencies.forEach { dependency ->
                            val dependencyNode = dependency as groovy.util.Node
                            val children = dependencyNode.children() as groovy.util.NodeList
                            var scopeNode: groovy.util.Node? = null
                            children.forEach { child ->
                                val childNode = child as groovy.util.Node
                                val qName = childNode.name() as groovy.xml.QName
                                if (qName.localPart == "scope") {
                                    scopeNode = childNode
                                }
                            }
                            dependencyNode.remove(scopeNode)
                        }
                    }
                }
            }
        }
    }
}

idea {
    module {
        isDownloadJavadoc = true
    }
}
