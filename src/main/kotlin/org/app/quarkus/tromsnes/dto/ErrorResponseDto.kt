package org.app.quarkus.tromsnes.dto

import kotlinx.serialization.Serializable

@Serializable
data class ErrorResponseDto(val details: String)
