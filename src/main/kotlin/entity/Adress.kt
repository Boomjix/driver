package de.hska.entity

import javax.validation.constraints.NotEmpty
import javax.validation.constraints.Pattern

// Class for Domain and saving drivers to the Database
// @author [Boomjix]
//@property postalcode Code out of 5 Digits to identify a City

data class Adress(
    @get:NotEmpty(message = "{adress.plz.notEmpty}")
    @get:Pattern(regexp = "\\d{5}", message = "{adress.plz}")
    val postalcode: String,

    @get:NotEmpty(message = "{adress.city.notEmpty}")
    val city: String

)

