package fe.gson.compatlibredirect.extension.json.array

import com.google.gson.JsonArray
import fe.gson.compatlibredirect.extension.json.element.*
import java.math.BigDecimal
import java.math.BigInteger


@Throws(IllegalStateException::class, AssertionError::class)
fun JsonArray.strings(): List<String> = map { it.string() }
fun JsonArray.stringsOrNull(keepNulls: Boolean = true): List<String?> = mapWithNulls(keepNulls) { it.stringOrNull() }

@Throws(IllegalStateException::class, AssertionError::class)
fun JsonArray.booleans(): List<Boolean> = map { it.boolean() }
fun JsonArray.booleansOrNull(keepNulls: Boolean = true): List<Boolean?> = mapWithNulls(keepNulls) { it.booleanOrNull() }

@Throws(IllegalStateException::class, NumberFormatException::class)
fun JsonArray.ints(): List<Int> = map { it.int() }
fun JsonArray.intsOrNull(keepNulls: Boolean = true): List<Int?> = mapWithNulls(keepNulls) { it.intOrNull() }

@Throws(IllegalStateException::class, NumberFormatException::class)
fun JsonArray.longs(): List<Long> = map { it.long() }
fun JsonArray.longsOrNull(keepNulls: Boolean = true): List<Long?> = mapWithNulls(keepNulls) { it.longOrNull() }

@Throws(IllegalStateException::class, NumberFormatException::class)
fun JsonArray.doubles(): List<Double> = map { it.double() }
fun JsonArray.doublesOrNull(keepNulls: Boolean = true): List<Double?> = mapWithNulls(keepNulls) { it.doubleOrNull() }

@Throws(IllegalStateException::class, NumberFormatException::class)
fun JsonArray.floats(): List<Float> = map { it.float() }
fun JsonArray.floatsOrNull(keepNulls: Boolean = true): List<Float?> = mapWithNulls(keepNulls) { it.floatOrNull() }

@Throws(IllegalStateException::class, NumberFormatException::class)
fun JsonArray.shorts(): List<Short> = map { it.short() }
fun JsonArray.shortsOrNull(keepNulls: Boolean = true): List<Short?> = mapWithNulls(keepNulls) { it.shortOrNull() }

@Throws(IllegalStateException::class, NumberFormatException::class)
fun JsonArray.bytes(): List<Byte> = map { it.byte() }
fun JsonArray.bytesOrNull(keepNulls: Boolean = true): List<Byte?> = mapWithNulls(keepNulls) { it.byteOrNull() }

@Throws(IllegalStateException::class, NumberFormatException::class)
fun JsonArray.bigDecimals(): List<BigDecimal> = map { it.bigDecimal() }
fun JsonArray.bigDecimalsOrNull(keepNulls: Boolean = true): List<BigDecimal?> = mapWithNulls(keepNulls) { it.bigDecimalOrNull() }

@Throws(IllegalStateException::class, NumberFormatException::class)
fun JsonArray.bigInts(): List<BigInteger> = map { it.bigInt() }
fun JsonArray.bigIntsOrNull(keepNulls: Boolean = true): List<BigInteger?> = mapWithNulls(keepNulls) { it.bigIntOrNull() }
