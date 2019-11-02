package de.hska.entity

import com.fasterxml.jackson.annotation.JsonValue

enum class Sex(val value: String) {
    MALE("M"),
    FEMALE("F"),
    DIVERS("D"),


    @JsonValue

}
