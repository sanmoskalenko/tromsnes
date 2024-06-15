package org.app.quarkus.tromsnes.service

import io.quarkus.hibernate.reactive.panache.common.WithSession
import io.quarkus.hibernate.reactive.panache.common.WithTransaction
import io.smallrye.mutiny.Uni
import jakarta.enterprise.context.ApplicationScoped
import jakarta.inject.Inject
import java.util.UUID
import org.app.quarkus.tromsnes.dto.book.RqCreateBookDto
import org.app.quarkus.tromsnes.dto.book.RqUpdateBookDto
import org.app.quarkus.tromsnes.dto.book.RsBookDto
import org.app.quarkus.tromsnes.exception.ResourceNotFoundException
import org.app.quarkus.tromsnes.mapper.BookMapper
import org.app.quarkus.tromsnes.repository.BookRepository

@ApplicationScoped
class BookService {

    @Inject
    lateinit var repository: BookRepository

    @Inject
    lateinit var mapper: BookMapper

    @WithTransaction
    fun create(rq: RqCreateBookDto): Uni<RsBookDto>? {
        return mapper.toCreateEntity(rq)
            ?.map { repository.persist(it) }
            ?.flatMap { item -> item.map { mapper.toDto(it) } }
    }

    @WithTransaction
    fun update(id: UUID, rq: RqUpdateBookDto): Uni<RsBookDto>? = repository.findById(id)
        .onItem().ifNotNull().transformToUni { entity ->
            mapper.toUpdateEntity(entity, rq)
            repository.persist(entity)
                .map { mapper.toDto(it) }
        }.onItem().ifNull().failWith {
            ResourceNotFoundException("Record with ID $id not found")
        }

    @WithSession
    fun list(): Uni<List<RsBookDto>>? = repository.listAll().map(mapper::toListDto)

    @WithSession
    fun getById(id: UUID): Uni<RsBookDto>? = repository.findById(id)
        .onItem().ifNotNull().transform { mapper.toDto(it) }
        .onItem().ifNull().failWith {
            ResourceNotFoundException("Record with ID $id not found")
        }

}
