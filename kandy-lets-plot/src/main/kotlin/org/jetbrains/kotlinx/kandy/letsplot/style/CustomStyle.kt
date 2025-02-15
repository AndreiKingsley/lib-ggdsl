/*
* Copyright 2020-2023 JetBrains s.r.o. Use of this source code is governed by the Apache 2.0 license.
*/

package org.jetbrains.kotlinx.kandy.letsplot.style

import org.jetbrains.kotlinx.kandy.letsplot.settings.LineType
import org.jetbrains.kotlinx.kandy.letsplot.settings.font.FontFace
import org.jetbrains.kotlinx.kandy.letsplot.settings.font.FontFamily
import org.jetbrains.kotlinx.kandy.util.color.Color
import org.jetbrains.kotlinx.kandy.util.context.SelfInvocationContext

public sealed interface LayoutParameters {
    public companion object {
        public fun line(
            color: Color? = null,
            width: Double? = null,
            lineType: LineType? = null,
            blank: Boolean = false
        ): LineParameters = LineParameters(color, width, lineType, blank)

        public fun text(
            color: Color? = null,
            fontFamily: FontFamily? = null,
            fontFace: FontFace? = null,
            fontSize: Double? = null,
            angle: Double? = null,
            hJust: Double? = null,
            vJust: Double? = null,
            margin: Margin? = null,
            blank: Boolean = false
        ): TextParameters = TextParameters(color, fontFamily, fontFace, fontSize, angle, hJust, vJust, margin, blank)

        public fun background(
            fillColor: Color? = null,
            borderLineColor: Color? = null,
            borderLineWidth: Double? = null,
            borderLineType: LineType? = null,
            blank: Boolean = false
        ): BackgroundParameters = BackgroundParameters(
            fillColor, borderLineColor, borderLineWidth, borderLineType, blank
        )
    }
}

public data class Margin(val top: Double, val right: Double, val bottom: Double, val left: Double) {
    public constructor(all: Double) : this(all, all, all, all)

    public constructor(vertical: Double, horizontal: Double) : this(vertical, horizontal, vertical, horizontal)

    public constructor(top: Double, horizontal: Double, bottom: Double) : this(top, horizontal, bottom, horizontal)
}

public interface WithMargin {
    public var margin: Margin?
    public fun margin(all: Double) {
        margin = Margin(all, all, all, all)
    }

    public fun margin(vertical: Double, horizontal: Double) {
        margin = Margin(vertical, horizontal, vertical, horizontal)
    }

    public fun margin(top: Double, horizontal: Double, bottom: Double) {
        margin = Margin(top, horizontal, bottom, horizontal)
    }

    public fun margin(top: Double, right: Double, bottom: Double, left: Double) {
        margin = Margin(top, right, bottom, left)
    }
}

public typealias Inset = Margin

public interface WithInset {
    public var inset: Inset?
    public fun inset(all: Double) {
        inset = Inset(all, all, all, all)
    }

    public fun inset(vertical: Double, horizontal: Double) {
        inset = Inset(vertical, horizontal, vertical, horizontal)
    }

    public fun inset(top: Double, horizontal: Double, bottom: Double) {
        inset = Inset(top, horizontal, bottom, horizontal)
    }

    public fun inset(top: Double, right: Double, bottom: Double, left: Double) {
        inset = Inset(top, right, bottom, left)
    }
}

public data class LineParameters internal constructor(
    var color: Color? = null,
    var width: Double? = null,
    var lineType: LineType? = null,
    var blank: Boolean = false,
) : LayoutParameters

public data class TextParameters internal constructor(
    var color: Color? = null,
    var fontFamily: FontFamily? = null,
    var fontFace: FontFace? = null,
    var fontSize: Double? = null,
    var angle: Double? = null,
    var hJust: Double? = null,
    var vJust: Double? = null,
    override var margin: Margin? = null,
    var blank: Boolean = false,
) : LayoutParameters, WithMargin

public data class BackgroundParameters internal constructor(
    var fillColor: Color? = null,
    var borderLineColor: Color? = null,
    var borderLineWidth: Double? = null,
    var borderLineType: LineType? = null,
    var blank: Boolean = false
) : LayoutParameters

public interface WithBackground {
    public var background: BackgroundParameters?

    public fun background(block: BackgroundParameters.() -> Unit) {
        background = BackgroundParameters().apply(block)
    }

    public fun background(parameters: BackgroundParameters) {
        background = parameters
    }
}

public interface WithLine {
    public var line: LineParameters?

    public fun line(block: LineParameters.() -> Unit) {
        line = LineParameters().apply(block)
    }

    public fun line(parameters: LineParameters) {
        line = parameters
    }
}

public interface WithText {
    public var text: TextParameters?

    public fun text(block: TextParameters.() -> Unit) {
        text = TextParameters().apply(block)
    }

    public fun text(parameters: TextParameters) {
        text = parameters
    }
}

public interface WithTitle {
    public var title: TextParameters?

    public fun title(block: TextParameters.() -> Unit) {
        title = TextParameters().apply(block)
    }

    public fun title(parameters: TextParameters) {
        title = parameters
    }
}


public data class Global internal constructor(
    override var line: LineParameters? = null,
    override var background: BackgroundParameters? = null,
    override var text: TextParameters? = null,
    override var title: TextParameters? = null,
    // var axis: LineParameters? = null,
) : SelfInvocationContext, WithLine, WithBackground, WithText, WithTitle

public data class LayerTooltips internal constructor(
    override var background: BackgroundParameters? = null,
    override var title: TextParameters? = null,
    override var text: TextParameters? = null,
) : SelfInvocationContext, WithBackground, WithText, WithTitle

public data class AxisTooltip internal constructor(
    override var background: BackgroundParameters? = null,
    override var text: TextParameters? = null,
) : SelfInvocationContext, WithBackground, WithText

public data class Axis internal constructor(
    var onTop: Boolean? = false,
    override var title: TextParameters? = null,
    override var text: TextParameters? = null,
    var ticks: LineParameters? = null,
    var ticksLength: Double? = null,
    override var line: LineParameters? = null,
    val tooltip: AxisTooltip = AxisTooltip(),
) : SelfInvocationContext, WithText, WithTitle, WithLine {
    internal var blank: Boolean? = null
    public fun ticks(block: LineParameters.() -> Unit) {
        ticks = LineParameters().apply(block)
    }

    public fun ticks(parameters: LineParameters) {
        ticks = parameters
    }
}

public sealed interface LegendPosition {
    public data object None : LegendPosition
    public data object Left : LegendPosition
    public data object Right : LegendPosition
    public data object Bottom : LegendPosition
    public data object Top : LegendPosition

    public data class Custom(val x: Double, val y: Double) : LegendPosition
}

public sealed interface LegendJustification {
    public data object Center : LegendJustification
    public data class Custom(val x: Double, val y: Double) : LegendJustification
}

public enum class LegendDirection {
    HORIZONTAL,
    VERTICAL
}

public data class Legend internal constructor(
    override var background: BackgroundParameters? = null,
    override var title: TextParameters? = null,
    override var text: TextParameters? = null,

    var position: LegendPosition? = null,
    var justification: LegendJustification? = null,
    var direction: LegendDirection? = null,
) : SelfInvocationContext, WithText, WithTitle, WithBackground {
    public fun justification(x: Double, y: Double) {
        justification = LegendJustification.Custom(x, y)
    }

    /*
    fun justification(value: LegendJustification) {
        justification = value
    }

     */
    public fun position(x: Double, y: Double) {
        position = LegendPosition.Custom(x, y)
    }
    /*
    fun position(value: LegendPosition) {
        position = value
    }
    fun direction(value: LegendDirection) {
        direction = value
    }

     */
}

public data class Grid internal constructor(
    var lineGlobal: LineParameters? = null,
    var majorLine: LineParameters? = null,
    var majorXLine: LineParameters? = null,
    var majorYLine: LineParameters? = null,
    var minorLine: LineParameters? = null,
    var minorXLine: LineParameters? = null,
    var minorYLine: LineParameters? = null,
) : SelfInvocationContext {
    public fun lineGlobal(block: LineParameters.() -> Unit) {
        lineGlobal = LineParameters().apply(block)
    }

    public fun lineGlobal(parameters: LineParameters) {
        lineGlobal = parameters
    }

    public fun majorLine(block: LineParameters.() -> Unit) {
        majorLine = LineParameters().apply(block)
    }

    public fun majorLine(parameters: LineParameters) {
        majorLine = parameters
    }

    public fun majorXLine(block: LineParameters.() -> Unit) {
        majorXLine = LineParameters().apply(block)
    }

    public fun majorXLine(parameters: LineParameters) {
        majorXLine = parameters
    }

    public fun majorYLine(block: LineParameters.() -> Unit) {
        majorYLine = LineParameters().apply(block)
    }

    public fun majorYLine(parameters: LineParameters) {
        majorYLine = parameters
    }

    public fun minorLine(block: LineParameters.() -> Unit) {
        minorLine = LineParameters().apply(block)
    }

    public fun minorLine(parameters: LineParameters) {
        minorLine = parameters
    }

    public fun minorXLine(block: LineParameters.() -> Unit) {
        majorLine = LineParameters().apply(block)
    }

    public fun minorXLine(parameters: LineParameters) {
        majorLine = parameters
    }

    public fun minorYLine(block: LineParameters.() -> Unit) {
        minorYLine = LineParameters().apply(block)
    }

    public fun minorYLine(parameters: LineParameters) {
        minorYLine = parameters
    }
}

public data class Panel internal constructor(
    override var background: BackgroundParameters? = null,
    var borderLine: LineParameters? = null,
    val grid: Grid = Grid()
) : SelfInvocationContext, WithBackground {
    public fun borderLine(block: LineParameters.() -> Unit) {
        borderLine = LineParameters().apply(block)
    }

    public fun borderLine(parameters: LineParameters) {
        borderLine = parameters
    }
}

public data class PlotCanvas internal constructor(
    override var background: BackgroundParameters? = null,
    override var title: TextParameters? = null,
    var subtitle: TextParameters? = null,
    var caption: TextParameters? = null,
    override var margin: Margin? = null,
    override var inset: Inset? = null,
) : SelfInvocationContext, WithBackground, WithTitle, WithMargin, WithInset {
    public fun subtitle(block: TextParameters.() -> Unit) {
        subtitle = TextParameters().apply(block)
    }

    public fun subtitle(parameters: TextParameters) {
        subtitle = parameters
    }

    public fun caption(block: TextParameters.() -> Unit) {
        caption = TextParameters().apply(block)
    }

    public fun caption(parameters: TextParameters) {
        caption = parameters
    }
}

public data class Strip internal constructor(
    override var background: BackgroundParameters? = null,
    override var text: TextParameters? = null
) : SelfInvocationContext, WithText, WithBackground

public data class CustomStyle @PublishedApi internal constructor(
    val global: Global = Global(),
    val axis: Axis = Axis(),
    val xAxis: Axis = Axis(),
    val yAxis: Axis = Axis(),
    val legend: Legend = Legend(),
    val panel: Panel = Panel(),
    val plotCanvas: PlotCanvas = PlotCanvas(),
    val strip: Strip = Strip(),
    val layerTooltips: LayerTooltips = LayerTooltips()
) : Style {
    public fun blankAxes() {
        axis.blank = true
    }
}
