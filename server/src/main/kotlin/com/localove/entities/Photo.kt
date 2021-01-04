package com.localove.entities

import com.localove.Identifiable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.time.LocalDateTime
import javax.persistence.*

@Entity
@Table(name = "photos")
class Photo(

    @ManyToOne
    @JoinColumn(name = "owner_id")
    val owner: Person,

    @Column(name = "last_update_time")
    val lastUpdateTime: LocalDateTime

) : Identifiable()

@Repository
interface PhotoRepository: JpaRepository<Photo, Long>