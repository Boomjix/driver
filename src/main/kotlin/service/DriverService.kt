package de.hska.service

import de.hska.kunde.config.logger
import de.hska.entity.Adress
import de.hska.entity.MaritalStatusType.MARRIED
import de.hska.entity.SexType.FEMALE
import de.hska.entity.InterestType.LECTURE
import de.hska.entity.InterestType.TRAVEL
import de.hska.entity.Driver
import de.hska.entity.Sales
import java.math.BigDecimal.ONE
import java.net.URL
import java.time.LocalDate
import java.util.Currency.getInstance
import java.util.Locale.GERMANY
import java.util.UUID.randomUUID
import javax.validation.Valid
import kotlin.random.Random
import org.springframework.stereotype.Service
import org.springframework.util.MultiValueMap
import org.springframework.validation.annotation.Validated
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.toMono

//Anwendungslogik für Driver

@Service
@Validated
class DriverService {

    //Driver nach ID suchen

    fun findById(id: String) = if (id[0].toLowerCase() == 'f') {
        logger.debug("findByID: No Driver found")
        Mono.empty()
    } else {
        val driver = createDriver(id)
        logger.debug("findById: {}", driver)
        driver.toMono()
    }

    private fun findByEmail(email: String): Mono<Driver> {
        if (email[0].toLowerCase() == 'z') {
            return Mono.empty()
        }

        var id = randomUUID().toString()
        if (id[0] == 'f') {
            // damit findById nicht empty() liefert (s.u.)
            id = id.replaceFirst("f", "1")
        }

        return findById(id).flatMap {
            it.copy(email = email)
                .toMono()
                .doOnNext { driver -> logger.debug("findByEmail: {}", driver) }
        }
    }

    @Suppress("ReturnCount")
    fun find(queryParams: MultiValueMap<String, String>): Flux<Driver> {
        if (queryParams.isEmpty()) {
            return findAll()
        }

        for ((key, value) in queryParams) {
            // nicht mehrfach das gleiche Suchkriterium, z.B. nachname=Aaa&nachname=Bbb
            if (value.size != 1) {
                return Flux.empty()
            }

            val paramValue = value[0]
            when (key) {
                "email" -> return findByEmail(paramValue).flux()
                "nachname" -> return findByNachname(paramValue)
            }
        }

        return Flux.empty()
    }

    fun findAll() = Flux.range(1, maxDriver)
        .map {
            var id = randomUUID().toString()
            if (id[0] == 'f') {
                id = id.replaceFirst("f", "1")
            }
            createDriver(id)
        }
        .doOnNext { driver -> logger.debug("findByNachname: {}", driver) }

    @Suppress("ReturnCount", "LongMethod")
    private fun findByNachname(nachname: String): Flux<Driver> { // Alarm
        if (nachname == "") {
            return findAll()
        }

        if (nachname[0] == 'Z') {
            return Flux.empty()
        }

        return Flux.range(1, nachname.length)
            .map {
                var id = randomUUID().toString()
                if (id[0] == 'f') {
                    id = id.replaceFirst("f", "1")
                }
                createDriver(id, nachname)
            }
            .doOnNext { driver -> logger.debug("findByNachname: {}", driver) }
    }

    fun create(@Valid driver: Driver): Mono<Driver> {
        val newDriver = driver.copy(id = randomUUID().toString())
        logger.debug("create(): {}", newDriver)
        return newDriver.toMono()
    }

    fun update(@Valid driver: Driver, id: String) =
        findById(id)
            .flatMap {
                val driverWithId = driver.copy(id = id)
                logger.debug("update(): {}", driverWithId)
                driverWithId.toMono()
            }

    fun deleteById(driverId: String) = findById(driverId)

    fun deleteByEmail(email: String) = findByEmail(email)

    private fun createDriver(id: String) = createDriver(id, nachnamen.random())

    @Suppress("LongMethod")
    private fun createDriver(id: String, nachname: String): Driver {
        @Suppress("MagicNumber")
        val minusYears = Random.nextLong(1, 60)
        val birthdate = LocalDate.now().minusYears(minusYears)
        val homepage = URL("https://www.hska.de")
        val sales = Sales(ammount = ONE, currency = getInstance(GERMANY))
        val adress = Adress(postalcode = "12345", city = "Testort")

        return Driver(
            id = id,
            nachname = nachname,
            email = "$nachname@hska.de",
            newsletter = true,
            birthdate = birthdate,
            sales = sales,
            homepage = homepage,
            sex = FEMALE,
            maritalstatus = MARRIED,
            interests = listOf(LECTURE, TRAVEL),
            adress = adress
        )
    }
    private companion object {
        const val maxDriver = 8
        val nachnamen = listOf("Alpha", "Beta", "Gamma", "Delta", "Epsilon")
        val logger by lazy { logger() }
    }
}
