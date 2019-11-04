/*
 * Copyright (C) 2017 - present Juergen Zimmermann, Hochschule Karlsruhe
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
package de.hska.rest.patch

import de.hska.entity.InterestType
import de.hska.entity.Driver

/**
 * Singleton-Klasse, um PATCH-Operationen auf Kunde-Objekte anzuwenden.
 *
 * @author [Jürgen Zimmermann](mailto:Juergen.Zimmermann@HS-Karlsruhe.de)
 */
object DriverPatcher {
    /**
     * PATCH-Operationen werden auf ein Kunde-Objekt angewandt.
     * @param kunde Das zu modifizierende Kunde-Objekt.
     * @param operations Die anzuwendenden Operationen.
     * @return Ein Kunde-Objekt mit den modifizierten Properties.
     */
    fun patch(driver: Driver, operations: List<PatchOperation>): Driver {
        val replaceOps = operations.filter { "replace" == it.op }
        var driverUpdated = replaceOps(driver, replaceOps)

        val addOps = operations.filter { "add" == it.op }
        driverUpdated = addInteressen(driverUpdated, addOps)

        val removeOps = operations.filter { "remove" == it.op }
        return removeInteressen(driverUpdated, removeOps)
    }

    private fun replaceOps(driver: Driver, ops: Collection<PatchOperation>): Driver {
        var driverUpdated = driver
        ops.forEach { (_, path, value) ->
            when (path) {
                "/nachname" -> {
                    driverUpdated = replaceNachname(driverUpdated, value)
                }
                "/email" -> {
                    driverUpdated = replaceEmail(driverUpdated, value)
                }
            }
        }
        return driverUpdated
    }

    private fun replaceNachname(driver: Driver, nachname: String) = driver.copy(nachname = nachname)

    private fun replaceEmail(driver: Driver, email: String) = driver.copy(email = email)

    private fun addInteressen(driver: Driver, ops: Collection<PatchOperation>): Driver {
        if (ops.isEmpty()) {
            return driver
        }
        var driverUpdated = driver
        ops.filter { "/interessen" == it.path }
            .forEach { driverUpdated = addInteresse(it, driverUpdated) }
        return driverUpdated
    }

    private fun addInteresse(op: PatchOperation, driver: Driver): Driver {
        val interesseStr = op.value
        val interesse = InterestType.build(interesseStr)
            ?: throw InvalidInteresseException(interesseStr)
        val interessen = if (driver.interests == null)
            mutableListOf()
        else driver.interests.toMutableList()
        interessen.add(interesse)
        return driver.copy(interests = interessen)
    }

    private fun removeInteressen(driver: Driver, ops: List<PatchOperation>): Driver {
        if (ops.isEmpty()) {
            return driver
        }
        var driverUpdated = driver
        ops.filter { "/interessen" == it.path }
            .forEach { driverUpdated = removeInteresse(it, driver) }
        return driverUpdated
    }

    private fun removeInteresse(op: PatchOperation, driver: Driver): Driver {
        val interesseStr = op.value
        val interesse = InterestType.build(interesseStr)
            ?: throw InvalidInteresseException(interesseStr)
        val interessen = driver.interests?.filter { it != interesse }
        return driver.copy(interests = interessen)
    }
}

/**
 * Exception, falls bei einer PATCH-Operation ein ungültiger Wert für ein
 * Interesse verwendet wird.
 * @author [Jürgen Zimmermann](mailto:Juergen.Zimmermann@HS-Karlsruhe.de)
 */
class InvalidInteresseException(value: String) : RuntimeException("$value ist kein gueltiges Interesse")
