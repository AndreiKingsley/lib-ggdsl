/*
* Copyright 2020-2023 JetBrains s.r.o. Use of this source code is governed by the Apache 2.0 license.
*/

package org.jetbrains.kotlinx.kandy.letsplot.translator

import org.jetbrains.kotlinx.dataframe.DataFrame
import org.jetbrains.kotlinx.kandy.dsl.internal.dataframe.GroupedData
import org.jetbrains.kotlinx.kandy.dsl.internal.dataframe.NamedData
import org.jetbrains.kotlinx.kandy.ir.Plot
import org.jetbrains.kotlinx.kandy.ir.aes.Aes
import org.jetbrains.kotlinx.kandy.ir.data.TableData
import org.jetbrains.kotlinx.kandy.letsplot.data.GeoSpatialData
import org.jetbrains.kotlinx.kandy.letsplot.internal.LetsPlotPositionalMappingParameters
import org.jetbrains.kotlinx.kandy.letsplot.scales.guide.model.Axis
import org.jetbrains.kotlinx.kandy.letsplot.settings.LineType
import org.jetbrains.kotlinx.kandy.letsplot.settings.Symbol
import org.jetbrains.kotlinx.kandy.letsplot.util.SimpleValueWrapper
import org.jetbrains.kotlinx.kandy.util.color.Color
import org.jetbrains.kotlinx.kandy.util.color.StandardColor

internal fun TableData.dataFrame(): DataFrame<*> {
    return when (this) {
        is NamedData -> dataFrame
        is GroupedData -> dataFrame
        is GeoSpatialData -> dataFrame
        else -> error("Unexpected data format: ${this::class}")
    }
}

internal fun Color.wrap(): String {
    return when (this) {
        is StandardColor.Hex -> hexString
        is StandardColor.Named -> name
        is StandardColor.RGB -> hexString
        // TODO("https://github.com/Kotlin/kandy/issues/421")
        is StandardColor.RGBA -> error("RGBA color not supported")
        else -> error("Unexpected color type: ${this::class}")
    }
}

internal fun wrapValue(value: Any?): Any? {
    if (value is Enum<*>) {
        return value.toString()
    }
    if (value is SimpleValueWrapper) {
        return value.value
    }
    if (value is Color) {
        return value.wrap()
    }
    if (value is Symbol) {
        return value.shape
    }
    if (value is LineType) {
        return value.description
    }
    return value
}

internal fun ClosedRange<*>?.wrap() = this?.let { (it.start as Number) to (it.endInclusive as Number) }
internal fun Pair<*, *>?.wrap() = this?.let { (it.first as? Number) to (it.second as? Number) }
internal fun Pair<Any?, Any?>.bothNull(): Boolean {
    return first == null && second == null
}

internal fun Pair<Any?, Any?>.computeRange(): Pair<Double, Double>? = when {
    first == null && second == null -> null
    second == null && first is Double -> (first as Double).let { it to it + 7.0 }
    first == null && second is Double -> (second as Double).let { (it - 7.0).coerceAtLeast(0.2) to it }
    else -> first as Double to second as Double
}

internal fun Plot.axes(): Map<Aes, Axis<*>> {
    return buildMap {
        layers.forEach { layer ->
            layer.mappings.forEach { (aes, mapping) ->
                (mapping.parameters as? LetsPlotPositionalMappingParameters<*>)?.axis?.let {
                    put(aes, it)
                }
            }
            layer.freeScales.forEach { (aes, freeScale) ->
                (freeScale.parameters as? LetsPlotPositionalMappingParameters<*>)?.axis?.let {
                    put(aes, it)
                }
            }
        }
        globalMappings.forEach { (aes, mapping) ->
            (mapping.parameters as? LetsPlotPositionalMappingParameters<*>)?.axis?.let {
                put(aes, it)
            }
        }
        freeScales.forEach { (aes, freeScale) ->
            (freeScale.parameters as? LetsPlotPositionalMappingParameters<*>)?.axis?.let {
                put(aes, it)
            }
        }
    }
}

internal fun Axis<*>.limits(): Pair<Number?, Number?>? {
    // Return null if both values are null for better handling in LP
    if (min == null && max == null) {
        return null
    }
    return (min as Number?) to (max as Number?)
}
