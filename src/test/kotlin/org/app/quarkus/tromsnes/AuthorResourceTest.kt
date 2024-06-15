package org.app.quarkus.tromsnes

import io.quarkus.test.InjectMock
import io.quarkus.test.TestTransaction
import io.quarkus.test.junit.QuarkusTest
import io.restassured.RestAssured.given
import io.smallrye.mutiny.Uni
import jakarta.ws.rs.core.MediaType
import java.time.LocalDate
import java.util.UUID
import net.javacrumbs.jsonunit.assertj.JsonAssertion
import net.javacrumbs.jsonunit.assertj.JsonAssertions.assertThatJson
import org.apache.http.HttpStatus
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.mockito.kotlin.anyVararg
import org.app.quarkus.tromsnes.dao.AuthorEntity
import org.app.quarkus.tromsnes.exception.ResourceNotFoundException
import org.app.quarkus.tromsnes.repository.AuthorRepository


@QuarkusTest
@TestTransaction
class AuthorResourceTest {

    @InjectMock
    lateinit var authorRepository: AuthorRepository

    val createAuthor: String = TestUtils.useFixture("createAuthor.json")
    val authorId: UUID = UUID.randomUUID()
    val testEntity = AuthorEntity().apply {
        id = authorId
        firstName = "Foo"
        middleName = "Bar"
        lastName = "Baz"
        bornPlace = "FooBar"
        bornDate = LocalDate.of(1800, 1, 1)
        deadDate = LocalDate.of(1900, 1, 1)
    }

    @Test
    fun `should create an author and return status 201`() {
        Mockito.`when`(authorRepository.persist(anyVararg(AuthorEntity::class)))
            .thenReturn(Uni.createFrom().item(testEntity))

        val response = given().`when`()
            .header("Content-Type", MediaType.APPLICATION_JSON)
            .body(createAuthor)
            .post("api/v1/authors").then()
            .assertThat().statusCode(HttpStatus.SC_CREATED)
            .assertThat().header("Content-Type", "application/json;charset=UTF-8")
            .extract().asPrettyString()

        assertThatJson(response).and(
            JsonAssertion { v ->
                v.node("id").isEqualTo(authorId)
                v.node("firstName").isEqualTo("Foo")
                v.node("middleName").isEqualTo("Bar")
                v.node("lastName").isEqualTo("Baz")
                v.node("bornPlace").isEqualTo("FooBar")
                v.node("bornDate").isEqualTo("1800-01-01")
                v.node("deadDate").isEqualTo("1900-01-01")
                v.node("createdAt").isNotNull
                v.node("updatedAt").isNotNull
            })
    }

    @Test
    fun `should return author with status 200`() {
        Mockito.`when`(authorRepository.findById(authorId))
            .thenReturn(Uni.createFrom().item(testEntity))

        val response = given().`when`()
            .header("Content-Type", MediaType.APPLICATION_JSON)
            .get("/api/v1/authors/${authorId}").then()
            .assertThat().statusCode(HttpStatus.SC_OK)
            .and().extract().asString()

        assertThatJson(response).and(
            JsonAssertion { v ->
                v.node("id").isEqualTo(authorId)
                v.node("firstName").isEqualTo("Foo")
                v.node("middleName").isEqualTo("Bar")
                v.node("lastName").isEqualTo("Baz")
                v.node("bornPlace").isEqualTo("FooBar")
                v.node("bornDate").isEqualTo("1800-01-01")
                v.node("deadDate").isEqualTo("1900-01-01")
                v.node("createdAt").isNotNull
                v.node("updatedAt").isNotNull
            })
    }

    @Test
    fun `should return list of authors with status 200`() {
        Mockito.`when`(authorRepository.listAll())
            .thenReturn(Uni.createFrom().item(listOf(testEntity, testEntity)))

        val response = given().`when`()
            .header("Content-Type", MediaType.APPLICATION_JSON)
            .get("api/v1/authors/list")
            .then().assertThat().statusCode(HttpStatus.SC_OK)
            .and().extract().asString()

        assertThatJson(response).and(
            JsonAssertion { v -> v.isArray.size().isEqualTo(2) }
        )
    }

    @Test
    fun `should return 404 when book is not found`() {
        Mockito.`when`(authorRepository.findById(authorId))
            .thenThrow(ResourceNotFoundException("Record with ID $authorId not found"))

        val response = given().`when`()
            .header("Content-Type", MediaType.APPLICATION_JSON)
            .get("/api/v1/authors/${authorId}")
            .then()
            .assertThat().statusCode(HttpStatus.SC_NOT_FOUND)
            .assertThat().header("Content-Type", "application/json;charset=UTF-8")
            .extract().asPrettyString()

        assertThatJson(response).and(
            JsonAssertion { v ->
                v.node("details").isEqualTo("Record with ID $authorId not found")
            })
    }

}
