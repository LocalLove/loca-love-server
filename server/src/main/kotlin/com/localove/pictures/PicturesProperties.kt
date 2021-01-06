package com.localove.pictures

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component

@Component
@ConfigurationProperties("loca-love.pictures")
class PicturesProperties {
    lateinit var supportedTypes: List<String>
}