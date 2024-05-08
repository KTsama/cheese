package coco.cheese.core.utils

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import coco.cheese.core.Env
import coco.cheese.core.interfaces.IBase
import com.elvishew.xlog.XLog
import com.tencent.yolov8ncnn.Yolov8Ncnn
import java.lang.ref.WeakReference
var yolov8ncnn = Yolov8Ncnn()
class YoloUtils (private val env: Env){

    fun loadYolo(path: String, list: Array<String>, cpugpu: Int): Boolean {
        XLog.i(list)
        return  if (!StringUtils.get().isPathInStorage(path)) {
            yolov8ncnn.loadModel(env.context.assets, path, list, 0, cpugpu)
        } else {
            yolov8ncnn.loadModel1(path, list, 0, cpugpu)
        }
    }
    fun detect(bitmap: Bitmap): Array<Yolov8Ncnn.Obj?> =
        yolov8ncnn.Detect(bitmap.copy(Bitmap.Config.ARGB_8888, true)) ?: arrayOfNulls(0)
    fun getSpeed(): Double =
        Yolov8Ncnn.speed
    fun draw(objects: Array<Yolov8Ncnn.Obj>?, b: Bitmap): Bitmap {
        if (objects == null) {
            return b
        }
        val rgba = b.copy(Bitmap.Config.ARGB_8888, true)
        val colors = intArrayOf(
            Color.rgb(54, 67, 244),
            Color.rgb(99, 30, 233),
            Color.rgb(176, 39, 156),
            Color.rgb(183, 58, 103),
            Color.rgb(181, 81, 63),
            Color.rgb(243, 150, 33),
            Color.rgb(244, 169, 3),
            Color.rgb(212, 188, 0),
            Color.rgb(136, 150, 0),
            Color.rgb(80, 175, 76),
            Color.rgb(74, 195, 139),
            Color.rgb(57, 220, 205),
            Color.rgb(59, 235, 255),
            Color.rgb(7, 193, 255),
            Color.rgb(0, 152, 255),
            Color.rgb(34, 87, 255),
            Color.rgb(72, 85, 121),
            Color.rgb(158, 158, 158),
            Color.rgb(139, 125, 96)
        )
        val canvas = Canvas(rgba)
        val paint = Paint().apply {
            style = Paint.Style.STROKE
            strokeWidth = 4f
        }
        val textbgpaint = Paint().apply {
            style = Paint.Style.FILL
        }
        val textpaint = Paint().apply {
            textSize = 28f
            textAlign = Paint.Align.LEFT
        }
        for (i in objects.indices) {
            paint.color = colors[i % 19]
            textbgpaint.color = colors[i % 19]
            textpaint.color = Color.WHITE
            canvas.drawRect(
                objects[i].x,
                objects[i].y,
                objects[i].x + objects[i].w,
                objects[i].y + objects[i].h,
                paint
            )

            val text = "${objects[i].label}  ${String.format("%.1f", objects[i].prob * 100)}%"
            val text_width = textpaint.measureText(text)
            val text_height = -textpaint.ascent() + textpaint.descent()
            var x = objects[i].x
            var y = objects[i].y - text_height
            if (y < 0) y = 0f
            if (x + text_width > rgba.width) x = rgba.width - text_width
            canvas.drawRect(x, y, x + text_width, y + text_height, textbgpaint)
            canvas.drawText(text, x, y - textpaint.ascent(), textpaint)
        }
        return rgba
    }

    companion object : IBase {
        private var instanceWeak: WeakReference<YoloUtils>? = null
        private var instance: YoloUtils? = null
        private val lock = Any()
        override fun get(env: Env, examine: Boolean): YoloUtils {
            if (this.instance == null || !examine) {
                synchronized(this.lock) {
                    this.instance = YoloUtils(env)
                }
            }
            return this.instance!!
        }


        override fun getWeak(env: Env, examine: Boolean): YoloUtils {
            if (this.instanceWeak?.get() == null || !examine) {
                synchronized(this.lock) {
                    this.instanceWeak = WeakReference(YoloUtils(env))
                }
            }
            return this.instanceWeak?.get()!!
        }


    }
}