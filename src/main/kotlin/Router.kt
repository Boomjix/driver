package de.hska

import de.hska.entity.Driver
import de.hska.html.HtmlHandler
import de.hska.rest.DriverHandler
import de.hska.rest.DriverStreamHandler
import org.springframework.context.annotation.Bean
import org.springframework.hateoas.MediaTypes.HAL_JSON
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.http.MediaType.TEXT_EVENT_STREAM
import org.springframework.http.MediaType.TEXT_HTML
import org.springframework.web.reactive.function.server.router


interface Router {
    @Bean
    @Suppress("SpringJavaInjectionPointsAutowiringInspection", "LongMethod")
    fun router(
        handler: DriverHandler,
        streamHandler: DriverStreamHandler,
        htmlHandler: HtmlHandler
    ) = router {
        // https://github.com/spring-projects/spring-framework/blob/master/...
        //       ..spring-webflux/src/main/kotlin/org/springframework/web/...
        //       ...reactive/function/server/RouterFunctionDsl.kt
        "/".nest {
            accept(HAL_JSON).nest {
                GET("/", handler::find)
                GET("/$idPathPattern", handler::findById)
            }

            contentType(APPLICATION_JSON).nest {
                POST("/", handler::create)
                PUT("/$idPathPattern", handler::update)
                PATCH("/$idPathPattern", handler::patch)
            }

            DELETE("/$idPathPattern", handler::deleteById)
            DELETE("/", handler::deleteByEmail)

            accept(TEXT_EVENT_STREAM).nest {
                GET("/", streamHandler::findAll)
            }

            accept(TEXT_HTML).nest {
                GET("/home", htmlHandler::home)
                GET("/suche", htmlHandler::find)
                GET("/details", htmlHandler::details)
            }
        }
    }

    companion object {
        /**
         * Name der Pfadvariablen für IDs.
         * const: "compile time constant"
         */
        const val idPathVar = "id"

        private const val idPathPattern = "{$idPathVar:${Driver.ID_PATTERN}}"
    }
}

