@file:Suppress("INVISIBLE_REFERENCE", "INVISIBLE_MEMBER", "CANNOT_OVERRIDE_INVISIBLE_MEMBER")

package org.jetbrains.kotlinx.kandy.letsplot.geo

import org.jetbrains.kotlinx.kandy.ir.Plot
import org.jetbrains.kotlinx.kandy.letsplot.feature.CoordinatesTransformation
import org.jetbrains.kotlinx.kandy.letsplot.feature.CustomCoordinatesTransformation
import org.jetbrains.kotlinx.kandy.letsplot.internal.X
import org.jetbrains.kotlinx.kandy.letsplot.internal.Y
import org.jetbrains.kotlinx.kandy.letsplot.translator.axes
import org.jetbrains.kotlinx.kandy.letsplot.translator.limits
import org.jetbrains.letsPlot.coord.coordMap
import org.jetbrains.letsPlot.intern.FeatureList

internal class MercatorCoordinatesTransformation : CustomCoordinatesTransformation {
    override fun wrap(plot: Plot): FeatureList {
        val axes = plot.axes()
        val xLimits = axes[X]?.limits()
        val yLimits = axes[Y]?.limits()

        return FeatureList(listOf(coordMap(xlim = xLimits, ylim = yLimits, flip = false)))
    }
}

internal class MercatorFlippedCoordinates : CustomCoordinatesTransformation {
    override fun wrap(plot: Plot): FeatureList {
        val axes = plot.axes()
        val xLimits = axes[X]?.limits()
        val yLimits = axes[Y]?.limits()

        return FeatureList(listOf(coordMap(xlim = xLimits, ylim = yLimits, flip = true)))
    }
}

public fun CoordinatesTransformation.Companion.mercator(): CoordinatesTransformation =
    MercatorCoordinatesTransformation()

public fun CoordinatesTransformation.Companion.mercatorFlipped(): CoordinatesTransformation =
    MercatorFlippedCoordinates()
