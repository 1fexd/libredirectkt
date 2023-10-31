package fe.gson.compatlibredirect.extension.json.`object`

import com.google.gson.JsonObject
import fe.gson.compatlibredirect.extension.json.element.*
import java.math.BigDecimal
import java.math.BigInteger


@Throws(IllegalStateException::class, AssertionError::class)
fun JsonObject.stringMap(): Map<String, String> = map { it.string() }
fun JsonObject.stringOrNullMap(keepNulls: Boolean = true): Map<String, String?> = mapWithNulls(keepNulls) { it.stringOrNull() }

@Throws(IllegalStateException::class, AssertionError::class)
fun JsonObject.booleanMap(): Map<String, Boolean> = map { it.boolean() }
fun JsonObject.booleanOrNullMap(keepNulls: Boolean = true): Map<String, Boolean?> = mapWithNulls(keepNulls) { it.booleanOrNull() }

@Throws(IllegalStateException::class, NumberFormatException::class)
fun JsonObject.intMap(): Map<String, Int> = map { it.int() }
fun JsonObject.intOrNullMap(keepNulls: Boolean = true): Map<String, Int?> = mapWithNulls(keepNulls) { it.intOrNull() }

@Throws(IllegalStateException::class, NumberFormatException::class)
fun JsonObject.longMap(): Map<String, Long> = map { it.long() }
fun JsonObject.longOrNullMap(keepNulls: Boolean = true): Map<String, Long?> = mapWithNulls(keepNulls) { it.longOrNull() }

@Throws(IllegalStateException::class, NumberFormatException::class)
fun JsonObject.doubleMap(): Map<String, Double> = map { it.double() }
fun JsonObject.doubleOrNullMap(keepNulls: Boolean = true): Map<String, Double?> = mapWithNulls(keepNulls) { it.doubleOrNull() }

@Throws(IllegalStateException::class, NumberFormatException::class)
fun JsonObject.floatMap(): Map<String, Float> = map { it.float() }
fun JsonObject.floatOrNullMap(keepNulls: Boolean = true): Map<String, Float?> = mapWithNulls(keepNulls) { it.floatOrNull() }

@Throws(IllegalStateException::class, NumberFormatException::class)
fun JsonObject.shortMap(): Map<String, Short> = map { it.short() }
fun JsonObject.shortOrNullMap(keepNulls: Boolean = true): Map<String, Short?> = mapWithNulls(keepNulls) { it.shortOrNull() }

@Throws(IllegalStateException::class, NumberFormatException::class)
fun JsonObject.byteMap(): Map<String, Byte> = map { it.byte() }
fun JsonObject.byteOrNullMap(keepNulls: Boolean = true): Map<String, Byte?> = mapWithNulls(keepNulls) { it.byteOrNull() }

@Throws(IllegalStateException::class, NumberFormatException::class)
fun JsonObject.bigDecimalMap(): Map<String, BigDecimal> = map { it.bigDecimal() }
fun JsonObject.bigDecimalOrNullMap(keepNulls: Boolean = true): Map<String, BigDecimal?> = mapWithNulls(keepNulls) { it.bigDecimalOrNull() }

@Throws(IllegalStateException::class, NumberFormatException::class)
fun JsonObject.bigIntMap(): Map<String, BigInteger> = map { it.bigInt() }
fun JsonObject.bigIntOrNullMap(keepNulls: Boolean = true): Map<String, BigInteger?> = mapWithNulls(keepNulls) { it.bigIntOrNull() }
