package ru.skillbranch.skillarticles.ui.custom.markdown

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.text.Layout
import android.text.Spanned
import androidx.annotation.VisibleForTesting
import androidx.core.graphics.ColorUtils
import androidx.core.text.getSpans
import ru.skillbranch.skillarticles.R
import ru.skillbranch.skillarticles.extensions.*
import ru.skillbranch.skillarticles.ui.custom.spans.HeaderSpan
import ru.skillbranch.skillarticles.ui.custom.spans.SearchFocusSpan
import ru.skillbranch.skillarticles.ui.custom.spans.SearchSpan

@VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
class SearchBgHelper(
    context: Context,
    mockDrawable: Drawable? = null,
    private val focusListener: ((Int, Int) -> Unit)? = null
) {

    private val padding = context.dpToIntPx(4)
    private val borderWidth = context.dpToIntPx(1)
    private val radius = context.dpToPx(8)
    private val secondaryColor = context.attrValue(R.attr.colorSecondary)
    private val alphaColor = ColorUtils.setAlphaComponent(secondaryColor, 160)

    val drawable: Drawable by lazy {
        mockDrawable ?: GradientDrawable().apply {
            shape = GradientDrawable.RECTANGLE
            cornerRadii = FloatArray(8){radius}
            color = ColorStateList.valueOf(alphaColor)
            setStroke(borderWidth, secondaryColor)
        }
    }
    val drawableLeft: Drawable by lazy {
        mockDrawable ?: GradientDrawable().apply {
            shape = GradientDrawable.RECTANGLE
            cornerRadii = floatArrayOf(
                radius, radius,
                0f, 0f,
                0f, 0f,
                radius, radius
            )
            color = ColorStateList.valueOf(alphaColor)
            setStroke(borderWidth, secondaryColor)
        }
    }
    val drawableMiddle by lazy {
        mockDrawable ?: GradientDrawable().apply {
            shape = GradientDrawable.RECTANGLE
            color = ColorStateList.valueOf(alphaColor)
            setStroke(borderWidth, secondaryColor)
        }
    }
    val drawableRight  by lazy {
        mockDrawable ?: GradientDrawable().apply {
            shape = GradientDrawable.RECTANGLE
            cornerRadii = floatArrayOf(
                0f, 0f,
                radius, radius,
                radius, radius,
                0f, 0f
            )
            color = ColorStateList.valueOf(alphaColor)
            setStroke(borderWidth, secondaryColor)
        }
    }

    private lateinit var spans: Array<out SearchSpan>
    private lateinit var headerSpans: Array<out HeaderSpan>
    private lateinit var render: SearchBgRender
    private val singleLineRender: SingleLineRender by lazy {
        SingleLineRender(padding, drawable)
    }
    private val multilineRender: MultilineRender by lazy {
        MultilineRender(padding, drawableLeft, drawableMiddle, drawableRight)
    }
    private var spanStart = 0
    private var spanEnd = 0
    private var startLine = 0
    private var endLine = 0
    private var startOffset = 0
    private var endOffset = 0
    private var topExtraPadding = 0
    private var bottomExtraPadding = 0


    fun draw(canvas: Canvas, text: Spanned, layout: Layout) {
        spans = text.getSpans()
        spans.forEach {
            spanStart = text.getSpanStart(it)
            spanEnd = text.getSpanEnd(it)
            startLine = layout.getLineForOffset(spanStart)
            endLine = layout.getLineForOffset(spanEnd)

            if (it is SearchFocusSpan) {
                focusListener?.invoke(layout.getLineTop(startLine), layout.getLineBottom(startLine))
            }
            headerSpans = text.getSpans(spanStart, spanEnd, HeaderSpan::class.java)

            if (headerSpans.isNotEmpty()) {
                topExtraPadding =
                    if (spanStart in headerSpans[0].firstLineBounds || spanEnd in headerSpans[0].firstLineBounds) headerSpans[0].topExtraPadding else 0
                bottomExtraPadding =
                    if (spanStart in headerSpans[0].lastLineBounds || spanEnd in headerSpans[0].lastLineBounds) headerSpans[0].bottomExtraPadding else 0
            } else {
                topExtraPadding = 0
                bottomExtraPadding = 0
            }

            startOffset = layout.getPrimaryHorizontal(spanStart).toInt()
            endOffset = layout.getPrimaryHorizontal(spanEnd).toInt()
            render = if (startLine == endLine) singleLineRender else multilineRender
            render.draw(
                canvas,
                layout,
                startLine,
                endLine,
                startOffset,
                endOffset,
                topExtraPadding,
                bottomExtraPadding
            )
        }
    }
}

abstract class SearchBgRender(
    val padding: Int
) {
    abstract fun draw(
        canvas: Canvas,
        layout: Layout,
        startLine: Int,
        endLine: Int,
        startOffset: Int,
        endOffset: Int,
        topExtraPadding: Int,
        bottomExtraPadding: Int
    )

    fun getLineTop(layout: Layout, line: Int): Int {
        return layout.getLineTopWithoutPadding(line)
    }

    fun getLineBottom(layout: Layout, line: Int): Int {
        return layout.getLineBottomWithoutPadding(line)
    }
}

class SingleLineRender(
    padding: Int,
    val drawable: Drawable
) : SearchBgRender(padding) {
    private var lineBottom: Int = 0
    private var lineTop: Int = 0

    override fun draw(
        canvas: Canvas,
        layout: Layout,
        startLine: Int,
        endLine: Int,
        startOffset: Int,
        endOffset: Int,
        topExtraPadding: Int,
        bottomExtraPadding: Int
    ) {
        lineTop = layout.getLineTop(startLine) + topExtraPadding
        lineBottom = layout.getLineBottom(startLine) - bottomExtraPadding
        drawable.setBounds(startOffset - padding, lineTop, endOffset + padding, lineBottom)
        drawable.draw(canvas)
    }

}

class MultilineRender(
    padding: Int,
    val drawableLeft: Drawable,
    val drawableMiddle: Drawable,
    val drawableRight: Drawable
) : SearchBgRender(padding) {
    private var lineBottom: Int = 0
    private var lineTop: Int = 0
    private var lineEndOffset: Int = 0
    private var lineStartOffset: Int = 0

    override fun draw(
        canvas: Canvas,
        layout: Layout,
        startLine: Int,
        endLine: Int,
        startOffset: Int,
        endOffset: Int,
        topExtraPadding: Int,
        bottomExtraPadding: Int
    ) {

        //first line
        lineEndOffset = (layout.getLineRight(startLine) + padding).toInt()
        lineTop = layout.getLineTop(startLine) + topExtraPadding
        lineBottom = layout.getLineBottom(startLine)
        drawStart(canvas, startOffset - padding, lineTop, lineEndOffset, lineBottom)


        //middle line
        for (line in startLine.inc() until endLine) {
            lineTop = getLineTop(layout, line)
            lineBottom = getLineBottom(layout, line)
            drawableMiddle.setBounds(
                layout.getLineLeft(line).toInt() - padding,
                lineTop,
                layout.getLineRight(line).toInt() + padding,
                lineBottom
            )
            drawableMiddle.draw(canvas)
        }


        //last line
        lineStartOffset = (layout.getLineLeft(startLine) - padding).toInt()
        lineTop = layout.getLineTop(endLine)
        lineBottom = layout.getLineBottom(endLine) - bottomExtraPadding
        drawEnd(canvas, lineStartOffset, lineTop, endOffset + padding, lineBottom)

    }

    private fun drawStart(canvas: Canvas, start: Int, top: Int, end: Int, bottom: Int) {
        drawableLeft.setBounds(start, top, end, bottom)
        drawableLeft.draw(canvas)

    }

    private fun drawEnd(canvas: Canvas, start: Int, top: Int, end: Int, bottom: Int) {
        drawableRight.setBounds(start, top, end, bottom)
        drawableRight.draw(canvas)

    }

}