package de.hska.entity

import com.fasterxml.jackson.annotation.JsonValue

enum class InterestType(val value: String) {

    SPORTS("S"),
    LECTURE("L"),
    TRAVEL("T");


    @JsonValue
    override fun toString() = value

    companion object {
        private val nameCache = HashMap<String, InterestType>().apply{
            enumValues<InterestType>().forEach{
                put(it.value, it)
                put(it.value.toLowerCase(), it)
                put(it.name, it)
                put(it.name.toLowerCase(), it)
            }
        }

        fun build(value: String?) = nameCache[value]
    }
}
