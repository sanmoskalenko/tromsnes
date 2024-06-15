package org.app.quarkus.tromsnes.exception

import jakarta.ws.rs.core.Response
import jakarta.ws.rs.ext.ExceptionMapper
import jakarta.ws.rs.ext.Provider
import org.app.quarkus.tromsnes.dto.ErrorResponseDto

@Provider
class GlobalExceptionHandler : ExceptionMapper<Throwable> {

    override fun toResponse(exception: Throwable?): Response = Response
        .status(Response.Status.INTERNAL_SERVER_ERROR)
        .entity(exception?.message?.let { ErrorResponseDto(it) })
        .build()

}