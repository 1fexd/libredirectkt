package fe.gson.compatlibredirect.typeadapter

import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter
import fe.gson.compatlibredirect.extension.nextStringOrNull
import java.io.IOException
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class LocalDateTimeTypeAdapter(
    private val formatter: DateTimeFormatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME
) : TypeAdapter<LocalDateTime>() {
    @Throws(IOException::class)
    override fun write(writer: JsonWriter, date: LocalDateTime?) {
        writer.value(date?.format(formatter))
    }

    @Throws(IOException::class)
    override fun read(reader: JsonReader): LocalDateTime? {
        return reader.nextStringOrNull()?.let { LocalDateTime.parse(it, formatter) }
    }

    companion object {
        val localDateTimeTypeAdapter = LocalDateTimeTypeAdapter()
    }
}


