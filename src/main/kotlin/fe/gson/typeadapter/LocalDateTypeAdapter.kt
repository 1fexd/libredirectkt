package fe.gson.typeadapter

import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter
import fe.gson.extension.nextStringOrNull
import java.io.IOException
import java.time.LocalDate
import java.time.format.DateTimeFormatter


class LocalDateTypeAdapter(
    private val formatter: DateTimeFormatter = DateTimeFormatter.ISO_LOCAL_DATE
) : TypeAdapter<LocalDate>() {
    @Throws(IOException::class)
    override fun write(writer: JsonWriter, date: LocalDate?) {
        writer.value(date?.format(formatter))
    }

    @Throws(IOException::class)
    override fun read(reader: JsonReader): LocalDate? {
        return reader.nextStringOrNull()?.let { LocalDate.parse(it, formatter) }
    }

    companion object {
        val localDateTypeAdapter = LocalDateTypeAdapter()
    }
}


