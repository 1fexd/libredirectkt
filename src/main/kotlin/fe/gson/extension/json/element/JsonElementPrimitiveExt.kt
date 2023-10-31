package fe.gson.extension.json.element

import com.google.gson.JsonElement
import java.math.BigDecimal
import java.math.BigInteger


@Throws(IllegalStateException::class, AssertionError::class)
fun JsonElement.string(): String = primitive().asString
fun JsonElement.stringOrNull(): String? = primitiveMappedOrNull { it.asString }

@Throws(IllegalStateException::class, AssertionError::class)
fun JsonElement.boolean(): Boolean = primitive().asBoolean
fun JsonElement.booleanOrNull(): Boolean? = primitiveMappedOrNull { it.asBoolean }

@Throws(IllegalStateException::class, NumberFormatException::class)
fun JsonElement.int(): Int = primitive().asInt
fun JsonElement.intOrNull(): Int? = primitiveMappedOrNull { it.asInt }

@Throws(IllegalStateException::class, NumberFormatException::class)
fun JsonElement.long(): Long = primitive().asLong
fun JsonElement.longOrNull(): Long? = primitiveMappedOrNull { it.asLong }

@Throws(IllegalStateException::class, NumberFormatException::class)
fun JsonElement.double(): Double = primitive().asDouble
fun JsonElement.doubleOrNull(): Double? = primitiveMappedOrNull { it.asDouble }

@Throws(IllegalStateException::class, NumberFormatException::class)
fun JsonElement.float(): Float = primitive().asFloat
fun JsonElement.floatOrNull(): Float? = primitiveMappedOrNull { it.asFloat }

@Throws(IllegalStateException::class, NumberFormatException::class)
fun JsonElement.short(): Short = primitive().asShort
fun JsonElement.shortOrNull(): Short? = primitiveMappedOrNull { it.asShort }

@Throws(IllegalStateException::class, NumberFormatException::class)
fun JsonElement.byte(): Byte = primitive().asByte
fun JsonElement.byteOrNull(): Byte? = primitiveMappedOrNull { it.asByte }

@Throws(IllegalStateException::class, NumberFormatException::class)
fun JsonElement.bigDecimal(): BigDecimal = primitive().asBigDecimal
fun JsonElement.bigDecimalOrNull(): BigDecimal? = primitiveMappedOrNull { it.asBigDecimal }

@Throws(IllegalStateException::class, NumberFormatException::class)
fun JsonElement.bigInt(): BigInteger = primitive().asBigInteger
fun JsonElement.bigIntOrNull(): BigInteger? = primitiveMappedOrNull { it.asBigInteger }
