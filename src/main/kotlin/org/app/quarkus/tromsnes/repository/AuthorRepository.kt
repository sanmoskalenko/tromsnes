package org.app.quarkus.tromsnes.repository

import io.quarkus.hibernate.reactive.panache.kotlin.PanacheRepositoryBase
import io.smallrye.mutiny.Uni
import jakarta.enterprise.context.ApplicationScoped
import java.util.UUID
import org.app.quarkus.tromsnes.dao.AuthorEntity

@ApplicationScoped
class AuthorRepository : PanacheRepositoryBase<AuthorEntity, UUID> {

    fun findByIds(ids: List<UUID>): Uni<List<AuthorEntity>> {
        return find("id in ?1", ids).list()
    }

}
