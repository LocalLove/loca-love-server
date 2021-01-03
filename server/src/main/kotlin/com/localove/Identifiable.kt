package com.localove

import org.springframework.data.util.ProxyUtils
import javax.persistence.*

@MappedSuperclass
abstract class Identifiable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Int? = null

    override fun equals(other: Any?): Boolean {
        other ?: return false

        if (this === other){
            return true
        }

        if (javaClass != ProxyUtils.getUserClass(other)) {
            return false
        }

        return this.id != null && this.id == (other as Identifiable).id
    }

    override fun hashCode() = 0xCAFE
}