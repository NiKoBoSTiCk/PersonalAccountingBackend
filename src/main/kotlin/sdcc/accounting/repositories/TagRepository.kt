package sdcc.accounting.repositories;

import org.springframework.data.jpa.repository.JpaRepository
import sdcc.accounting.entities.ETag
import sdcc.accounting.entities.Tag

interface TagRepository : JpaRepository<Tag, Int> {

    fun findTagByTagEquals(tag: ETag): Tag?
}