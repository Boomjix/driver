package de.hska.rest

import com.fasterxml.jackson.core.JsonParseException
import com.fasterxml.jackson.databind.exc.InvalidFormatException
import de.hska.Router.Companion.idPathVar
import de.hska.kunde.config.logger
import de.hska.entity.Driver
import de.hska.rest.constraints.DriverConstraintViolation
import de.hska.rest.hateoas.DriverModelAssembler
import de.hska.rest.hateoas.ListDriverModelAssembler
import de.hska.rest.patch.InvalidInteresseException
import de.hska.rest.patch.DriverPatcher
import de.hska.rest.patch.PatchOperation
import de.hska.service.DriverService
import java.net.URI
import javax.validation.ConstraintViolationException
import org.springframework.core.codec.DecodingException
import org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.ServerResponse.badRequest
import org.springframework.web.reactive.function.server.ServerResponse.created
import org.springframework.web.reactive.function.server.ServerResponse.noContent
import org.springframework.web.reactive.function.server.ServerResponse.notFound
import org.springframework.web.reactive.function.server.ServerResponse.ok
import org.springframework.web.reactive.function.server.ServerResponse.status
import org.springframework.web.reactive.function.server.body
import org.springframework.web.reactive.function.server.bodyToFlux
import org.springframework.web.reactive.function.server.bodyToMono
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.onErrorResume
import reactor.kotlin.core.publisher.toMono

@Component
@Suppress("TooManyFunctions")
class DriverHandler(
    private val service: DriverService,
    private val modelAssembler: DriverModelAssembler,
    private val listModelAssembler: DriverModelAssembler
) {
    /**
     * Suche anhand der Kunde-ID
     * @param request Der eingehende Request
     * @return Ein Mono-Objekt mit dem Statuscode 200 und dem gefundenen
     *      Kunden einschließlich HATEOAS-Links, oder aber Statuscode 204.
     */
    fun findById(request: ServerRequest): Mono<ServerResponse> {
        val id = request.pathVariable(idPathVar)
        return service.findById(id)
            .doOnNext { driver -> logger.debug("findById: {}", driver) }
            .map { modelAssembler.toModel(it, request.exchange()) }
            .flatMap { ok().body(it.toMono()) }
            .switchIfEmpty(notFound().build())
    }

    /**
     * Suche mit diversen Suchkriterien als Query-Parameter. Es wird
     * `Mono<List<Kunde>>` statt `Flux<Kunde>` zurückgeliefert, damit
     * auch der Statuscode 204 möglich ist.
     * @param request Der eingehende Request mit den Query-Parametern.
     * @return Ein Mono-Objekt mit dem Statuscode 200 und einer Liste mit den
     *      gefundenen Kunden einschließlich HATEOAS-Links, oder aber
     *      Statuscode 204.
     */
    fun find(request: ServerRequest): Mono<ServerResponse> {
        val queryParams = request.queryParams()

        // https://stackoverflow.com/questions/45903813/...
        //     ...webflux-functional-how-to-detect-an-empty-flux-and-return-404
        return service.find(queryParams)
            .doOnNext { driver -> logger.debug("find: {}", driver) }
            .collectList()
            .flatMap { drivers ->
                // genau 1 Treffer bei der Suche anhand der Emailadresse
                if (queryParams.keys.contains("email")) {
                    val driverModel = modelAssembler.toModel(drivers[0], request.exchange())
                    ok().body(driverModel)
                } else {
                    val driversModel = drivers.map { driver -> listModelAssembler.toModel(driver, request) }
                    ok().body(driversModel.toMono())
                }
            }
            .switchIfEmpty(notFound().build())
    }

    /**
     * Einen neuen Kunde-Datensatz anlegen.
     * @param request Der eingehende Request mit dem Kunde-Datensatz im Body.
     * @return Response mit Statuscode 201 einschließlich Location-Header oder
     *      Statuscode 400 falls Constraints verletzt sind oder der
     *      JSON-Datensatz syntaktisch nicht korrekt ist.
     */
    fun create(request: ServerRequest) =
        request.bodyToMono<Driver>()
            .flatMap { service.create(it) }
            .flatMap { driver ->
                logger.debug("create: {}", driver)
                val location = URI("${request.uri()}${driver.id}")
                created(location).build()
            }
            .onErrorResume(ConstraintViolationException::class) {
                // Service-Funktion "create" und Parameter "kunde"
                handleConstraintViolation(it, "create.kunde.")
            }
            .onErrorResume(DecodingException::class) { handleDecodingException(it) }

    // z.B. Service-Funktion "create|update" mit Parameter "kunde" hat dann Meldungen mit "create.kunde.nachname:"
    private fun handleConstraintViolation(exception: ConstraintViolationException, deleteStr: String):
        Mono<ServerResponse> {
        val violations = exception.constraintViolations
        if (violations.isEmpty()) {
            return badRequest().build()
        }

        val driverViolations = violations.map { violation ->
            DriverConstraintViolation(
                property = violation.propertyPath.toString().replace(deleteStr, ""),
                message = violation.message
            )
        }
        logger.debug("handleConstraintViolation(): {}", driverViolations)
        return badRequest().body(driverViolations.toMono())
    }

    private fun handleDecodingException(e: DecodingException) = when (val exception = e.cause) {
        is JsonParseException -> {
            logger.debug("handleDecodingException(): JsonParseException={}", exception.message)
            val msg = exception.message ?: ""
            badRequest().body(msg.toMono())
        }
        is InvalidFormatException -> {
            logger.debug("handleDecodingException(): InvalidFormatException={}", exception.message)
            val msg = exception.message ?: ""
            badRequest().body(msg.toMono())
        }
        else -> status(INTERNAL_SERVER_ERROR).build()
    }

    /**
     * Einen vorhandenen Kunde-Datensatz überschreiben.
     * @param request Der eingehende Request mit dem neuen Kunde-Datensatz im
     *      Body.
     * @return Response mit Statuscode 204 oder Statuscode 400, falls
     *      Constraints verletzt sind oder der JSON-Datensatz syntaktisch nicht
     *      korrekt ist.
     */
    fun update(request: ServerRequest): Mono<ServerResponse> {
        val id = request.pathVariable(idPathVar)
        return request.bodyToMono<Driver>()
            .flatMap { service.update(it, id) }
            .flatMap { noContent().build() }
            .switchIfEmpty(notFound().build())
            .onErrorResume(ConstraintViolationException::class) {
                // Service-Funktion "update" und Parameter "driver"
                handleConstraintViolation(it, "update.driver.")
            }
            .onErrorResume(DecodingException::class) { handleDecodingException(it) }
    }

    /**
     * Einen vorhandenen Kunde-Datensatz durch PATCH aktualisieren.
     * @param request Der eingehende Request mit dem PATCH-Datensatz im Body.
     * @return Response mit Statuscode 204 oder Statuscode 400, falls
     *      Constraints verletzt sind oder der JSON-Datensatz syntaktisch nicht
     *      korrekt ist.
     */
    @Suppress("LongMethod")
    fun patch(request: ServerRequest): Mono<ServerResponse> {
        val id = request.pathVariable(idPathVar)

        return request.bodyToFlux<PatchOperation>()
            // Die einzelnen Patch-Operationen als Liste in einem Mono
            .collectList()
            .flatMap { patchOps ->
                service.findById(id)
                    .flatMap {
                        val driverPatched = DriverPatcher.patch(it, patchOps)
                        logger.debug("patch(): {}", driverPatched)
                        service.update(driverPatched, id)
                    }
                    .flatMap { noContent().build() }
                    .switchIfEmpty(notFound().build())
            }
            .onErrorResume(ConstraintViolationException::class) {
                // Service-Funktion "update" und Parameter "driver"
                handleConstraintViolation(it, "update.driver.")
            }
            .onErrorResume(InvalidInteresseException::class) {
                val msg = it.message
                if (msg == null) {
                    badRequest().build()
                } else {
                    badRequest().body(msg.toMono())
                }
            }
            .onErrorResume(DecodingException::class) { handleDecodingException(it) }
    }

    /**
     * Einen vorhandenen Kunden anhand seiner ID löschen.
     * @param request Der eingehende Request mit der ID als Pfad-Parameter.
     * @return Response mit Statuscode 204.
     */
    fun deleteById(request: ServerRequest): Mono<ServerResponse> {
        val id = request.pathVariable(idPathVar)
        return service.deleteById(id).flatMap { noContent().build() }
    }

    /**
     * Einen vorhandenen Kunden anhand seiner Emailadresse löschen.
     * @param request Der eingehende Request mit der Emailadresse als
     *      Query-Parameter.
     * @return Response mit Statuscode 204.
     */
    fun deleteByEmail(request: ServerRequest): Mono<ServerResponse> {
        val email = request.queryParam("email")
        return if (email.isPresent) {
            return service.deleteByEmail(email.get())
                .flatMap { noContent().build() }
        } else {
            notFound().build()
        }
    }

    private companion object {
        val logger by lazy { logger() }
    }
}
