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
package de.hska.kunde.config

import org.springframework.boot.actuate.autoconfigure.security.reactive.EndpointRequest
import org.springframework.context.annotation.Bean
import org.springframework.http.HttpMethod.DELETE
import org.springframework.http.HttpMethod.GET
import org.springframework.http.HttpMethod.PATCH
import org.springframework.http.HttpMethod.POST
import org.springframework.http.HttpMethod.PUT
import org.springframework.security.config.web.server.ServerHttpSecurity
import org.springframework.security.core.userdetails.MapReactiveUserDetailsService
import org.springframework.security.core.userdetails.User
import org.springframework.security.crypto.factory.PasswordEncoderFactories

// https://github.com/spring-projects/spring-security/blob/5.0.2.RELEASE/samples/...
//       ...javaconfig/hellowebflux/src/main/java/sample/SecurityConfig.java

/**
 * Security-Konfiguration.
 *
 * @author [Jürgen Zimmermann](mailto:Juergen.Zimmermann@HS-Karlsruhe.de)
 */
interface SecurityConfig {
    /**
     * Bean-Definition, um den Zugriffsschutz an der REST-Schnittstelle zu
     * konfigurieren.
     *
     * @param http Injiziertes Objekt von `ServerHttpSecurity` als
     *      Ausgangspunkt für die Konfiguration.
     * @return Objekt von `SecurityWebFilterChain`
     */
    @Bean
    @Suppress("HasPlatformType")
    fun securityWebFilterChain(http: ServerHttpSecurity) = http.authorizeExchange { exchanges ->
        exchanges
            .pathMatchers(POST, kundePath).permitAll()
            .pathMatchers(GET, kundePath, kundeIdPath).hasRole(adminRolle)
            .pathMatchers(PUT, kundePath).hasRole(adminRolle)
            .pathMatchers(PATCH, kundeIdPath).hasRole(adminRolle)
            .pathMatchers(DELETE, kundeIdPath).hasRole(adminRolle)

            .matchers(EndpointRequest.to("health")).permitAll()
            .matchers(EndpointRequest.toAnyEndpoint()).hasRole(endpointAdminRolle)

            .anyExchange().authenticated()
    }
        .httpBasic {}
        .formLogin { form -> form.disable() }
        .csrf { csrf -> csrf.disable() }
        .build()

    /**
     * Bean, um Test-User anzulegen. Dazu gehören jeweils ein Benutzername, ein
     * Passwort und diverse Rollen. Das wird in Beispiel 2 verbessert werden.
     *
     * @return Ein Objekt, mit dem diese Test-User verwaltet werden, z.B. für
     * die künftige Suche.
     */
    @Bean
    fun userDetailsService(): MapReactiveUserDetailsService {
        val passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder()
        val password = passwordEncoder.encode("p")

        val admin = User.withUsername("admin")
            .password(password)
            .roles(adminRolle, kundeRolle, endpointAdminRolle)
            .build()
        val alpha = User.withUsername("alpha")
            .password(password)
            .roles(kundeRolle)
            .build()
        return MapReactiveUserDetailsService(admin, alpha)
    }

    companion object {
        private const val adminRolle = "ADMIN"
        private const val kundeRolle = "KUNDE"
        private const val endpointAdminRolle = "ENDPOINT_ADMIN"
        private const val kundePath = "/"
        private const val kundeIdPath = "/*"
    }
}
