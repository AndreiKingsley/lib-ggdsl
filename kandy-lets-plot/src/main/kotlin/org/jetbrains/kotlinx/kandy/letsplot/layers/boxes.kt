/*
* Copyright 2020-2023 JetBrains s.r.o. Use of this source code is governed by the Apache 2.0 license.
*/

package org.jetbrains.kotlinx.kandy.letsplot.layers

import org.jetbrains.kotlinx.kandy.dsl.internal.LayerCreatorScope
import org.jetbrains.kotlinx.kandy.letsplot.feature.Position
import org.jetbrains.kotlinx.kandy.letsplot.feature.position
import org.jetbrains.kotlinx.kandy.letsplot.layers.builders.BoxesBuilder

/**
 * Adds a new `boxes` layer to the plot.
 *
 * The `boxes` layer is responsible for constructing a boxplot representation,
 * which visualizes the distribution of a dataset by depicting its quartiles
 * and thereby provides insights into the data's spread and potential skewness.
 *
 * This function creates a context where you can set aesthetic mappings (`aes`) or aesthetic constants.
 * - Mappings are specified by calling methods that correspond to aesthetic names (`aes`).
 * - Constants are directly assigned using properties with the names corresponding to aesthetics.
 *   For positional aesthetics, you can use the `.constant()` method.
 *
 * ## Boxes Aesthetics
 * * **`x`** - The X-coordinate specifying the categories.
 * * **`yMin`** - The minimum value for the Y-coordinate (the lowest whisker).
 * * **`lower`** - The lower quartile value.
 * * **`middle`** - The median value.
 * * **`upper`** - The upper quartile value.
 * * **`yMax`** - The maximum value for the Y-coordinate (the highest whisker).
 * * **`fillColor`** - The fill color of the boxes.
 * * **`alpha`** - The transparency of the boxes.
 * * **`width`** - The width of the boxes.
 * * **`fatten`** - The factor by which to "fatten" the width of the notch relative to the body.
 * * **`borderLine.color`** - Color of the boxes borderline.
 * * **`borderLine.width`** - Width of the boxes borderline.
 * * **`borderLine.type`** - Type of the boxes borderline, such as dashed or dotted.
 *
 * ## Example
 *
 * ```kotlin
 * plot {
 *     boxes {
 *         // Positional mapping
 *         x(listOf("A", "B", "C", "D"))
 *         yMin(listOf(10, 20, 5, 12))
 *         lower(listOf(20, 30, 12, 22))
 *         middle(listOf(30, 40, 20, 35))
 *         upper(listOf(40, 50, 35, 45))
 *         yMax(listOf(50, 55, 40, 48))
 *
 *         // Adjust the Y-axis
 *         y.axis.limits = 0.0..60.0
 *
 *         // Non-positional settings
 *         fatten = 0.8
 *         width = 0.5
 *
 *         // BorderLine settings
 *         borderLine.width = .5
 *
 *         // Non-positional mapping
 *         fillColor = Color.BLUE
 *     }
 * }
 * ```
 */
public inline fun LayerCreatorScope.boxes(block: BoxesBuilder.() -> Unit) {
    createLayer(BoxesBuilder(this).apply {
        position = Position.dodge()
    }, block)
}
