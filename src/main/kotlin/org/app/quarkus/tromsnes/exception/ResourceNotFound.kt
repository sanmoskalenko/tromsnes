package org.app.quarkus.tromsnes.exception

import jakarta.ws.rs.core.Response
import jakarta.ws.rs.ext.ExceptionMapper
import jakarta.ws.rs.ext.Provider
import org.app.quarkus.tromsnes.dto.ErrorResponseDto

class ResourceNotFoundException(message: String?) : RuntimeException(message)

@Provider
class ResourceNotFoundExceptionHandler : ExceptionMapper<ResourceNotFoundException> {
    override fun toResponse(exception: ResourceNotFoundException): Response = Response
        .status(Response.Status.NOT_FOUND)
        .entity(exception.message?.let { ErrorResponseDto(it) })
        .build()
}
