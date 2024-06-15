package org.app.quarkus.tromsnes.service

import io.quarkus.hibernate.reactive.panache.common.WithTransaction
import io.smallrye.mutiny.Uni
import jakarta.enterprise.context.ApplicationScoped
import jakarta.inject.Inject
import java.util.UUID
import org.app.quarkus.tromsnes.dto.author.RqCreateAuthorDto
import org.app.quarkus.tromsnes.dto.author.RqUpdateAuthorDto
import org.app.quarkus.tromsnes.dto.author.RsAuthorDto
import org.app.quarkus.tromsnes.exception.ResourceNotFoundException
import org.app.quarkus.tromsnes.mapper.AuthorMapper
import org.app.quarkus.tromsnes.repository.AuthorRepository

@ApplicationScoped
class AuthorService {

    @Inject
    lateinit var repository: AuthorRepository

    @Inject
    lateinit var mapper: AuthorMapper

    @WithTransaction
    fun create(rq: RqCreateAuthorDto): Uni<RsAuthorDto>? {
        val entity = mapper.toCreateEntity(rq)
        return repository.persist(entity)
            .map { mapper.toDto(it) }
    }

    @WithTransaction
    fun update(id: UUID, rq: RqUpdateAuthorDto): Uni<RsAuthorDto>? = repository.findById(id)
        .onItem().ifNotNull().transformToUni { entity ->
            mapper.toUpdateEntity(entity, rq)
            repository.persist(entity)
                .map { mapper.toDto(it) }
        }.onItem().ifNull().failWith {
            ResourceNotFoundException("Record with ID $id not found")
        }

    @WithTransaction
    fun list(): Uni<List<RsAuthorDto>>? = repository.listAll().map { mapper.toListDto(it) }

    @WithTransaction
    fun getById(id: UUID): Uni<RsAuthorDto>? = repository.findById(id)
        .onItem().ifNotNull().transform { mapper.toDto(it) }
        .onItem().ifNull().failWith {
            ResourceNotFoundException("Record with ID $id not found")
        }

}

