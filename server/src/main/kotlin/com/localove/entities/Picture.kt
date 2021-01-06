package com.localove.entities

import com.localove.Identifiable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.time.LocalDateTime
import javax.persistence.*

@Entity
@Table(name = "photos")
class Picture(

    @ManyToOne
    @JoinColumn(name = "owner_id")
    val owner: Person,

    @Column(name = "last_update_time")
    var lastUpdateTime: LocalDateTime,

    @Column(name = "type")
    val type: String,

    @Column(name = "bytes")
    val bytes: ByteArray
) : Identifiable()

@Repository
interface PictureRepository : JpaRepository<Picture, Long>