package fe.gson.extension.json.`object`

import com.google.gson.JsonObject
import fe.gson.exception.NoSuchKeyInElementFoundException
import java.math.BigDecimal
import java.math.BigInteger


@Throws(ClassCastException::class, NoSuchKeyInElementFoundException::class, AssertionError::class)
fun JsonObject.asString(key: String): String = asPrimitive(key).asString
fun JsonObject.asStringOrNull(key: String): String? = asPrimitiveMappedOrNull(key) { it.asString }

@Throws(ClassCastException::class, NoSuchKeyInElementFoundException::class, AssertionError::class)
fun JsonObject.asBoolean(key: String): Boolean = asPrimitive(key).asBoolean
fun JsonObject.asBooleanOrNull(key: String): Boolean? = asPrimitiveMappedOrNull(key) { it.asBoolean }

@Throws(ClassCastException::class, NoSuchKeyInElementFoundException::class, NumberFormatException::class)
fun JsonObject.asInt(key: String): Int = asPrimitive(key).asInt
fun JsonObject.asIntOrNull(key: String): Int? = asPrimitiveMappedOrNull(key) { it.asInt }

@Throws(ClassCastException::class, NoSuchKeyInElementFoundException::class, NumberFormatException::class)
fun JsonObject.asLong(key: String): Long = asPrimitive(key).asLong
fun JsonObject.asLongOrNull(key: String): Long? = asPrimitiveMappedOrNull(key) { it.asLong }

@Throws(ClassCastException::class, NoSuchKeyInElementFoundException::class, NumberFormatException::class)
fun JsonObject.asDouble(key: String): Double = asPrimitive(key).asDouble
fun JsonObject.asDoubleOrNull(key: String): Double? = asPrimitiveMappedOrNull(key) { it.asDouble }

@Throws(ClassCastException::class, NoSuchKeyInElementFoundException::class, NumberFormatException::class)
fun JsonObject.asFloat(key: String): Float = asPrimitive(key).asFloat
fun JsonObject.asFloatOrNull(key: String): Float? = asPrimitiveMappedOrNull(key) { it.asFloat }

@Throws(ClassCastException::class, NoSuchKeyInElementFoundException::class, NumberFormatException::class)
fun JsonObject.asShort(key: String): Short = asPrimitive(key).asShort
fun JsonObject.asShortOrNull(key: String): Short? = asPrimitiveMappedOrNull(key) { it.asShort }

@Throws(ClassCastException::class, NoSuchKeyInElementFoundException::class, NumberFormatException::class)
fun JsonObject.asByte(key: String): Byte = asPrimitive(key).asByte
fun JsonObject.asByteOrNull(key: String): Byte? = asPrimitiveMappedOrNull(key) { it.asByte }

@Throws(ClassCastException::class, NoSuchKeyInElementFoundException::class, NumberFormatException::class)
fun JsonObject.asBigDecimal(key: String): BigDecimal = asPrimitive(key).asBigDecimal
fun JsonObject.asBigDecimalOrNull(key: String): BigDecimal? = asPrimitiveMappedOrNull(key) { it.asBigDecimal }

@Throws(ClassCastException::class, NoSuchKeyInElementFoundException::class, NumberFormatException::class)
fun JsonObject.asBigInteger(key: String): BigInteger = asPrimitive(key).asBigInteger
fun JsonObject.asBigIntegerOrNull(key: String): BigInteger? = asPrimitiveMappedOrNull(key) { it.asBigInteger }
