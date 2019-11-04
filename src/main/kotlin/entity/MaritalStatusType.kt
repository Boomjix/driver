package de.hska.entity

import com.fasterxml.jackson.annotation.JsonValue

/**
 * Enum Class for Maritial Status. Posibillity to implement a Dropdown Menu
 * @author [Bene Bender]
 *
 * @property value The Internal value
 */

enum class MaritalStatusType(val value: String) {

    SINGLE("S"),
    MARRIED("M"),
    DIVORCED("D"),
    WIDOWED("W");

    @JsonValue
    override fun toString() = value

    companion object {
        private val nameCache = HashMap<String, MaritalStatusType>().apply {
            enumValues<MaritalStatusType>().forEach {
                put(it.value, it)
                put(it.value.toLowerCase(), it)
                put(it.name, it)
                put(it.name.toLowerCase(), it)
            }
        }

        // Convert String to Enum or Null

        fun build(value: String?) = nameCache[value]
    }
}
