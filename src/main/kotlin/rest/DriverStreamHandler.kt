package de.hska.rest

import de.hska.rest.hateoas.ListDriverModelAssembler
import de.hska.service.DriverService
import org.springframework.http.MediaType.TEXT_EVENT_STREAM
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.ServerResponse.ok
import org.springframework.web.reactive.function.server.body
import reactor.core.publisher.Mono

@Component
class DriverStreamHandler(private val service: DriverService, private val modelAssembler: ListDriverModelAssembler) {

    fun findAll(request: ServerRequest): Mono<ServerResponse> {
        val driver = service.findAll()
            .map { modelAssembler.toModel(it, request) }

        return ok().contentType(TEXT_EVENT_STREAM).body(driver)
    }
}
