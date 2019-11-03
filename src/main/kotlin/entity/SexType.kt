package de.hska.entity

import com.fasterxml.jackson.annotation.JsonValue

enum class SexType(val value: String) {
    MALE("M"),
    FEMALE("F"),
    DIVERS("D");


    @JsonValue
    override fun toString()= value

    companion object {
        private val nameCache = HashMap<String, SexType>().apply() {
            enumValues<SexType>().forEach {
                put(it.value, it)
                put(it.value.toLowerCase(), it)
                put(it.name, it)
                put(it.name.toLowerCase(), it)
            }
        }

        fun build(value: String?) = nameCache[value]
    }

}
