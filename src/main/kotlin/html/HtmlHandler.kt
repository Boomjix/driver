package de.hska.html

import de.hska.service.DriverService
import org.springframework.http.MediaType.TEXT_HTML
import org.springframework.stereotype.Component
import org.springframework.ui.ConcurrentModel
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import org.thymeleaf.spring5.context.webflux.ReactiveDataDriverContextVariable
import reactor.core.publisher.Mono

@Component
class HtmlHandler(private val service: DriverService) {

    //Startseite anzeigen

    fun home(request: ServerRequest) =
        ServerResponse.ok().contentType(TEXT_HTML).render("index")

    // Alle Kunden anzeigen

    fun find(request:ServerRequest): Mono<ServerResponse> {
        val driver = ConcurrentModel()
            .addAttribute(
                "driver",
                ReactiveDataDriverContextVariable(service.findAll(),1)
                    )
        return ServerResponse.ok().contentType(TEXT_HTML).render("suche", driver)
    }

    //Kunde zu ID anzeigen

    fun details(request: ServerRequest): Mono<ServerResponse> {
        val driver = ConcurrentModel()
        request.queryParam("id").ifPresent {
            driver.addAttribute("kunde", service.findById(it))
        }

        return ServerResponse.ok().contentType(TEXT_HTML).render("details", driver)
    }
}
