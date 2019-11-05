package de.hska.rest.hateoas

import de.hska.entity.Driver
import org.springframework.hateoas.CollectionModel
import org.springframework.hateoas.EntityModel
import org.springframework.hateoas.Link
import org.springframework.hateoas.server.SimpleRepresentationModelAssembler
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.ServerRequest

@Component
class ListDriverModelAssembler : SimpleRepresentationModelAssembler<Driver> {
    // Konvertierung eines (gefundenen) Driver-Objektes in ein Model

    fun toModel(driver: Driver, request: ServerRequest): EntityModel<Driver> {
        val uri = request.uri().toString()
        val baseUri = uri.substringBefore('?').removeSuffix("/")
        val idUri = "$baseUri/${driver.id}"

        val selfLink = Link(idUri)
        return toModel(driver).add(selfLink)
    }

    // Konvertierung eines (gefundenen) Driver Objektes

    override fun addLinks(model: EntityModel<Driver>) = Unit

    override fun addLinks(model: CollectionModel<EntityModel<Driver>>) = Unit

}
