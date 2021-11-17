package com.atemis.customviewcryptowayne

import android.content.Context
import android.content.res.TypedArray
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.text.TextPaint
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View

/**
 * TODO: document your custom view class.
 */
class SlideVolume : View {

    private var touchY = 0f
    private var _value = 0

    var value : Int
        get() = _value
        set(newValue) {
            _value = newValue

            touchY = height - (newValue/100F * height)

            onValueChanged?.invoke(_value)
            invalidate()
        }

    private var onValueChanged : ((Int)->Unit)?=null

    fun setOnValueChanged (callback:(Int)->Unit) {
        onValueChanged = callback
    }

    constructor(context: Context) : super(context) {
        init(null, 0)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(attrs, 0)
    }

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(
        context,
        attrs,
        defStyle
    ) {
        init(attrs, defStyle)
    }

    private fun init(attrs: AttributeSet?, defStyle: Int) {

    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        // define proprieties of rectangule
        val paint = Paint()
        paint.color = Color.RED
        paint.style = Paint.Style.FILL

        // define proprieties of rectangule stroke
        val paintEdge = Paint()
        paintEdge.color = Color.BLUE
        paintEdge.style = Paint.Style.STROKE
        paintEdge.strokeWidth = 10F
        paintEdge.textSize = 80.0f

        // define proprieties for text of %
        val paintText = Paint()
        paintText.color = Color.YELLOW
        paintText.textSize = 80.0f

        // paint rectangule
        val rect = Rect(10, touchY.toInt() + 500, width-10,( touchY.toInt()-10))
        canvas.drawRect(rect, paint)

        // paint resctangule stroke
        val rectEdge = Rect(10, 10, width-10,height -10)
        canvas.drawRect(rectEdge, paintEdge)

        // paint text for %
        canvas.drawText("${_value}", width/2F, height-50F, paintText )
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        super.onTouchEvent(event)

        // val y of touch on axis y
        val y = event?.y
        when(event?.action){
            MotionEvent.ACTION_DOWN,
            MotionEvent.ACTION_MOVE ->{
                touchY = if (y?:0f>height) height.toFloat() else y?:0f
                touchY = if (touchY < 0) 0F else touchY
                _value = 100 - ((touchY/height.toFloat())*100F).toInt()

                onValueChanged?.invoke(_value)
                invalidate()
                Log.d("customview", "touchY value:$touchY")
                return true
            }
        }
        return false
    }
}