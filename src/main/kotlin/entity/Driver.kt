package de.hska.entity

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonPropertyOrder
import de.hska.entity.Driver.Companion.ID_PATTERN
import de.hska.entity.Driver.Companion.NACHNAME_PATTERN
import java.net.URL
import java.time.LocalDate
import javax.validation.Valid
import javax.validation.constraints.Email
import javax.validation.constraints.Max
import javax.validation.constraints.Min
import javax.validation.constraints.NotEmpty
import javax.validation.constraints.Past
import javax.validation.constraints.Pattern
import org.hibernate.validator.constraints.UniqueElements

@JsonPropertyOrder(
    "name", "email", "category", "newsletter", "birthdate",
    "sales", "homepage", "sex", "maritialstatus", "interests",
    "adress"
)

data class Driver(
    @get:Pattern(regexp = ID_PATTERN, message = "{driver.id.pattern}")
    @JsonIgnore
    val id: String?,

    @get:NotEmpty(message = "{driver.name.notEmpty}")
    @get:Pattern(
        regexp = NACHNAME_PATTERN,
        message = "{driver.name.pattern}"
    )
    val nachname: String,

    @get:Email(message = "{driver.email.pattern}")
    val email: String,

    @get:Min(value = MIN_KATEGORIE, message = "{driver.kategorie.min}")
    @get:Max(value = MAX_KATEGORIE, message = "{driver.kategorie.min}")
    val kategorie: Int = 0,

    val newsletter: Boolean = false,

    @get:Past(message = "{driver.birth-date.past}")
    // In einer "Data Class": keine Aufbereitung der Konstruktor-Argumente
    // @JsonFormat(shape = STRING)
    // @field:JsonDeserialize(using = DateDeserializer.class)
    val birthdate: LocalDate?,

    val sales: Sales?,

    val homepage: URL?,

    val sex: SexType?,

    val maritalstatus: MaritalStatusType?,

    @get:UniqueElements(message = "{driver.interests.uniqueElements}")
    val interests: List<InterestType>?,

    @get:Valid
    val adress: Adress
) {

@Suppress("ReturnCount")
override fun equals(other: Any?): Boolean {
    if (this === other) return true
    if (javaClass != other?.javaClass) return false

    other as Driver
    return email == other.email
}

    override fun hashCode() = email.hashCode()

    companion object {
        private const val HEX_PATTERN = "[\\dA-Fa-f]"

        /**
         * Muster für eine UUID.
         */
        const val ID_PATTERN =
            "$HEX_PATTERN{8}-$HEX_PATTERN{4}-$HEX_PATTERN{4}-" +
                "$HEX_PATTERN{4}-$HEX_PATTERN{12}"

        private const val NACHNAME_PREFIX = "o'|von|von der|von und zu|van"

        private const val NAME_PATTERN = "[A-ZÄÖÜ][a-zäöüß]+"

        /**
         * Muster für einen Nachnamen
         */
        const val NACHNAME_PATTERN =
            "($NACHNAME_PREFIX)?$NAME_PATTERN(-$NAME_PATTERN)?"

        /**
         * Maximaler Wert für eine Kategorie
         */
        const val MIN_KATEGORIE = 0L

        /**
         * Minimaler Wert für eine Kategorie
         */
        const val MAX_KATEGORIE = 9L
    }
}




