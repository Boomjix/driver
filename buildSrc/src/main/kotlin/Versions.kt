/*
 * Copyright (C) 2019 - present Juergen Zimmermann, Hochschule Karlsruhe
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

// https://docs.gradle.org/current/userguide/organizing_gradle_projects.html#sec:build_sources

@Suppress("unused", "KDocMissingDocumentation", "MemberVisibilityCanBePrivate")
object Versions {
    const val kotlin = "1.3.60-eap-76"
    const val springBoot = "2.2.0.RELEASE"

    object Plugins {
        const val kotlin = Versions.kotlin
        const val allOpen = Versions.kotlin
        const val noArg = Versions.kotlin
        //const val kapt = Versions.kotlin

        const val springBoot = Versions.springBoot
        const val testLogger = "2.0.0"

        const val vplugin = "3.0.1"
        const val versions = "0.27.0"
        const val detekt = "1.1.1"
        const val sonarqube = "2.8"
        const val dokka = "0.10.0"
        const val jib = "1.7.0"
        const val sweeney = "4.1.0"
        const val owaspDependencyCheck = "5.2.2"
        const val asciidoctorConvert = "3.0.0-alpha.3"
        const val asciidoctorPdf = asciidoctorConvert
        const val jk1DependencyLicenseReport = "1.11"
        const val jaredsBurrowsLicense = "0.8.42"
    }

    const val annotations = "17.0.0"
    const val paranamer = "2.8"
    const val springSecurityRsa = "1.0.8.RELEASE"
    const val bouncycastle = "1.63"

    // -------------------------------------------------------------------------------------------
    // Versionsnummern aus BOM-Dateien ueberschreiben
    // siehe org.springframework.boot:spring-boot-dependencies
    //    https://github.com/spring-projects/spring-boot/blob/master/spring-boot-dependencies/pom.xml
    // siehe org.springframework.cloud:spring-cloud-dependencies
    //    https://github.com/spring-cloud/spring-cloud-release/blob/master/spring-cloud-dependencies/pom.xml
    //    https://github.com/spring-cloud/spring-cloud-release/blob/master/pom.xml
    // siehe org.springframework.cloud:spring-cloud-commons-dependencies
    //    https://github.com/spring-cloud/spring-cloud-commons/blob/master/spring-cloud-commons-dependencies/pom.xml
    // siehe org.springframework.cloud:spring-cloud-build-dependencies
    //    https://github.com/spring-cloud/spring-cloud-build/blob/master/spring-cloud-build-dependencies/pom.xml
    // siehe org.springframework.cloud:spring-cloud-build
    //    https://github.com/spring-cloud/spring-cloud-build/blob/master/pom.xml
    // -------------------------------------------------------------------------------------------

    const val springCloud = "Hoxton.M3"
    //const val springCloudFunctionBom = "3.0.0.M3"
    //const val springCloudLoadBalancer = "2.2.0.M3"
    //const val springCloudCircuitbreakerBom = "1.0.0.M1"
    //const val springCloudConfigBom = "2.2.0.M3"
    //const val springCloudConsulBom = "2.2.0.M3"
    //const val springCloudStreamBom = "Horsham.M4"
    //const val springCloudStream = "3.0.0.M4"
    //const val springCloudSleuthBom = "2.2.0.M3"

    const val springBootR2dbcBom = "0.1.0.M2"
    //const val r2dbcBom = "Arabba-RC2"
    //const val r2dbcPostgresql = "0.8.0.RC2"
    const val braveBom = "5.8.0"
    const val resilience4j = "1.1.0"

    //const val assertj = "3.13.2"
    const val blockhound = "1.0.0.RELEASE"
    const val hibernateValidator = "6.1.0.Alpha6"
    const val jackson = "2.10.0"
    //const val jakartaMail = "1.6.4"
    const val jakartaValidationApi = "2.0.2"
    const val junitJupiter = "5.6.0-M1"
    const val junitJupiterBom = junitJupiter
    const val junitPlatform = "1.6.0-M1"
    //const val kafka = "2.3.0"
    //const val mongoDriverReactivestreams = "1.12.0"
    const val mongodb = "3.11.1"
    //const val reactiveStreams = "1.0.3"
    //const val reactorBom = "Dysprosium-RELEASE"
    //const val reactorKotlinExtension = "1.0.0.RELEASE"
    //const val springBom = "5.2.0.RELEASE"
    //const val springDataReleasetrain = "Moore-RELEASE"
    //const val springHateoas = "1.0.0.RELEASE"
    //const val springIntegrationBom = "5.2.0.RELEASE"
    //const val springIntegrationKafka = "3.2.0.RELEASE"
    //const val springKafka = "2.3.1.RELEASE"
    //const val springSecurityBom = "5.2.0.RELEASE"
    //const val thymeleaf = "3.0.11.RELEASE"
    const val tomcat = "9.0.27"

    const val mockk = "1.9.3"

    const val ktlint = "0.35.0"
    const val httpClientKtlint = "4.5.10"
    const val intellij = "2019.3"
    //const val jacocoVersion = "0.8.5"
    const val plantuml = "1.2019.11"
    const val antJunit = "1.10.7"
    const val asciidoctorj = "2.1.0"
    const val asciidoctorjPdf = "1.5.0-beta.6"
}
