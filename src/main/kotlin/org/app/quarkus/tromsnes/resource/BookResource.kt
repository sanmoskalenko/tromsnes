package org.app.quarkus.tromsnes.resource

import jakarta.inject.Inject
import jakarta.ws.rs.Consumes
import jakarta.ws.rs.GET
import jakarta.ws.rs.POST
import jakarta.ws.rs.PUT
import jakarta.ws.rs.Path
import jakarta.ws.rs.PathParam
import jakarta.ws.rs.Produces
import jakarta.ws.rs.core.MediaType
import java.util.UUID
import org.jboss.resteasy.reactive.RestResponse
import org.jboss.resteasy.reactive.RestResponse.ResponseBuilder
import org.app.quarkus.tromsnes.dto.book.RqCreateBookDto
import org.app.quarkus.tromsnes.dto.book.RqUpdateBookDto
import org.app.quarkus.tromsnes.service.BookService

@Path("/api/v1/books")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
class BookResource {

    @Inject
    lateinit var service: BookService

    @POST
    fun create(data: RqCreateBookDto) = service.create(data)
        ?.map { ResponseBuilder.create(RestResponse.Status.CREATED, it).build() }

    @PUT
    @Path("/{id}")
    fun update(@PathParam("id") id: UUID, data: RqUpdateBookDto) =
        service.update(id, data)

    @GET
    @Path("/list")
    fun list() = service.list()

    @GET
    @Path("/{id}")
    fun get(@PathParam("id") id: UUID) = service.getById(id)

}
