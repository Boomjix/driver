package de.hska

import de.hska.entity.Driver
import de.hska.html.HtmlHandler
import de.hska.rest.DriverHandler
import de.hska.rest.KundeStreamHandler
import org.springframework.context.annotation.Bean
import org.springframework.hateoas.MediaTypes.HAL_JSON
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.http.MediaType.TEXT_EVENT_STREAM
import org.springframework.http.MediaType.TEXT_HTML
import org.springframework.web.reactive.function.server.router


interface Router {
}
