package fe.gson.compatlibredirect.typeadapter

import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter
import fe.gson.compatlibredirect.extension.nextStringOrNull
import java.io.IOException
import java.time.LocalTime
import java.time.format.DateTimeFormatter

class LocalTimeTypeAdapter(
    private val formatter: DateTimeFormatter = DateTimeFormatter.ISO_LOCAL_TIME
) : TypeAdapter<LocalTime>() {
    @Throws(IOException::class)
    override fun write(writer: JsonWriter, date: LocalTime?) {
        writer.value(date?.format(formatter))
    }

    @Throws(IOException::class)
    override fun read(reader: JsonReader): LocalTime? {
        return reader.nextStringOrNull()?.let { LocalTime.parse(it, formatter) }
    }

    companion object {
        val localDateTypeAdapter = LocalDateTypeAdapter()
    }
}


