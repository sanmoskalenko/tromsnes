package org.app.quarkus.tromsnes.mapper

import jakarta.enterprise.context.ApplicationScoped
import org.app.quarkus.tromsnes.dao.AuthorEntity
import org.app.quarkus.tromsnes.dto.author.RqCreateAuthorDto
import org.app.quarkus.tromsnes.dto.author.RqUpdateAuthorDto
import org.app.quarkus.tromsnes.dto.author.RsAuthorDto

@ApplicationScoped
class AuthorMapper {

    fun toCreateEntity(rq: RqCreateAuthorDto): AuthorEntity {
        return AuthorEntity().apply {
            firstName = rq.firstName
            middleName = rq.middleName
            lastName = rq.lastName
            bornDate = rq.bornDate
            deadDate = rq.deadDate
            bornPlace = rq.bornPlace
        }
    }

    fun toUpdateEntity(entity: AuthorEntity, rq: RqUpdateAuthorDto): AuthorEntity {
        return AuthorEntity().apply {
            rq.firstName?.let { entity.firstName = it }
            rq.middleName?.let { entity.middleName = it }
            rq.lastName?.let { entity.lastName = it }
            rq.bornDate?.let { entity.bornDate = it }
            rq.deadDate?.let { entity.deadDate = it }
            rq.bornPlace?.let { entity.bornPlace = it }
        }
    }

    fun toDto(entity: AuthorEntity): RsAuthorDto {
        return RsAuthorDto(
            id = entity.id,
            firstName = entity.firstName,
            middleName = entity.middleName,
            lastName = entity.lastName,
            bornDate = entity.bornDate,
            deadDate = entity.deadDate,
            bornPlace = entity.bornPlace,
            createdAt = entity.createdAt,
            updatedAt = entity.updatedAt
        )
    }

    fun toListDto(list: List<AuthorEntity>): List<RsAuthorDto> {
        return list.map { toDto(it) }
    }

}