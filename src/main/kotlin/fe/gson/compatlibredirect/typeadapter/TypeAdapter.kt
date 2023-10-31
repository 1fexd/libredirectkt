package fe.gson.compatlibredirect.typeadapter

import fe.gson.compatlibredirect.typeadapter.LocalDateTimeTypeAdapter.Companion.localDateTimeTypeAdapter
import fe.gson.compatlibredirect.typeadapter.LocalDateTypeAdapter.Companion.localDateTypeAdapter
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

val dateTimeAdapters = mapOf(
    LocalDateTime::class to localDateTimeTypeAdapter,
    LocalDate::class to localDateTypeAdapter,
    LocalTime::class to localDateTypeAdapter
)
