package com.test.chart.widget

//    private val textPaint: TextPaint
//        get() = TextPaint().apply {
//            isAntiAlias = true
//            bgColor = ContextCompat.getColor(context, R.color.neural_activity_unselected_color)
//            style = Paint.Style.FILL
//            typeface = ResourcesCompat.getFont(context, R.font.dm_sans_bold)
//            textSize = context.px(R.dimen.label_text_size)
//        }
//
//    private val textBackgroundPaint: Paint
//        get() = Paint().apply {
//            isAntiAlias = true
//            style = Paint.Style.FILL
//            color = ContextCompat.getColor(context, R.color.neural_activity_unselected_color)
//        }
//
//    override fun onDraw(canvas: Canvas) {
//        canvas.save()
//        val headerCellHeight = context.px(R.dimen.header_cell_height)
//        val itemCellHeight = context.px(R.dimen.item_cell_height)
//        val labelStartMargin = context.px(R.dimen.label_start_margin)
//        val labelTopMargin = context.px(R.dimen.label_top_margin)
//
//        with(canvas) {
//            val neuralText = context.getString(R.string.neural_activity)
//            val neuralActivityYPos = (headerCellHeight * 2) + labelTopMargin + textPaint.textSize
//            canvas.drawRect(
//                getTextBackgroundSize(labelStartMargin, neuralActivityYPos, neuralText, textPaint),
//                textBackgroundPaint
//            )
//            drawText(
//                neuralText,
//                labelStartMargin,
//                neuralActivityYPos,
//                textPaint.apply { color = ContextCompat.getColor(context, R.color.neural_activity_selected_color) }
//            )
//        }
//
//        canvas.restore()
//    }
//
//    private fun getTextBackgroundSize(x: Float, y: Float, text: String, paint: TextPaint): Rect {
//        val fontMetrics = paint.fontMetrics
//        val halfTextLength = paint.measureText(text) / 2 + 5
//        return Rect((x - halfTextLength).toInt(), (y + fontMetrics.top).toInt(), (x + halfTextLength).toInt(), (y + fontMetrics.bottom).toInt())
//    }