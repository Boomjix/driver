package de.hska.rest.hateoas

import de.hska.entity.Driver
import org.springframework.hateoas.EntityModel
import org.springframework.hateoas.Link
import org.springframework.hateoas.server.reactive.SimpleReactiveRepresentationModelAssembler
import org.springframework.stereotype.Component
import org.springframework.web.server.ServerWebExchange

@Component
class DriverModelAssembler : SimpleReactiveRepresentationModelAssembler<Driver> {

    override fun addLinks(driverModel : EntityModel<Driver>, exchange: ServerWebExchange): EntityModel<Driver> {
        val uri = exchange.request.uri.toString()
        val id = driverModel.content?.id

        val baseUri = uri.substringBefore('?')
            .removeSuffix("/")
            .removeSuffix("/$id")
        val idUri = "$baseUri/$id"

        val selfLink = Link(idUri)
        val listLink = Link(baseUri, "list")
        val addLink = Link(baseUri, "add")
        val updateLink = Link(idUri, "update")
        val removeLink = Link(idUri, "remove")
        return driverModel.add(selfLink, listLink, addLink, updateLink, removeLink)
    }
}
