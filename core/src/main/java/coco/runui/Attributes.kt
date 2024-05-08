package coco.runui
import android.graphics.Color
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import org.w3c.dom.Node
import java.util.concurrent.ConcurrentHashMap
import kotlin.random.Random

var ID: MutableMap<String, Int> =
    ConcurrentHashMap<String, Int>().toMutableMap()
fun View.width(node: Node): View {
    val widthValue = node.attributes.getNamedItem("width")?.nodeValue ?: return this
    val layoutParams = this.layoutParams ?: ViewGroup.LayoutParams(
        ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT
    )
    val layoutParamsWidth = when (widthValue) {
        ParamType.MATCH_PARENT.value -> ViewGroup.LayoutParams.MATCH_PARENT
        ParamType.WRAP_CONTENT.value -> ViewGroup.LayoutParams.WRAP_CONTENT
        else -> parseSize(widthValue)
    }
    layoutParams.width = layoutParamsWidth
    this.layoutParams = layoutParams
    return this
}
fun View.height(node: Node): View {
    val heightValue = node.attributes.getNamedItem("height")?.nodeValue ?: return this
    val layoutParams = this.layoutParams ?: ViewGroup.LayoutParams(
        ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT
    )
    val layoutParamsHeight = when (heightValue) {
        ParamType.MATCH_PARENT.value -> ViewGroup.LayoutParams.MATCH_PARENT
        ParamType.WRAP_CONTENT.value -> ViewGroup.LayoutParams.WRAP_CONTENT
        else -> parseSize(heightValue)
    }
    layoutParams.height = layoutParamsHeight
    this.layoutParams = layoutParams
    return this
}
fun View.orientation(node: Node): View {
    val t = node.attributes.getNamedItem("orientation")?.nodeValue?: return this
    if (this is LinearLayout) {
        orientation = if (t == "horizontal") LinearLayout.HORIZONTAL else LinearLayout.VERTICAL
    }
    return this
}
fun View.text(node: Node): View {
    val t = node.attributes.getNamedItem("text")?.nodeValue ?: return this
    if ( this is TextView) {
        text = t
    }
    return this
}

fun View.id(node: Node): View {
    val t = node.attributes.getNamedItem("id")?.nodeValue ?: return this
    val randomNumber = (1..5).map { Random.nextInt(10) }.joinToString("").toInt()
    ID[t] = randomNumber
    this.id = randomNumber
    return this
}

fun View.background(node: Node): View {
    val t = node.attributes.getNamedItem("background")?.nodeValue ?: return this
        val color = Color.parseColor(t)
        setBackgroundColor(color)

    return this
}

fun View.hint(node: Node): View {
    val t = node.attributes.getNamedItem("hint")?.nodeValue ?: return this
    if (this is TextView) {
        setHint(t)
    }
    return this
}

fun View.gravity(node: Node): View {
    val t = node.attributes.getNamedItem("gravity")?.nodeValue ?: return this
    val alignment = when (t) {
        "center" -> Gravity.CENTER
        "left" -> Gravity.LEFT
        "right" -> Gravity.RIGHT
        else -> Gravity.NO_GRAVITY
    }
    when (this) {
        is LinearLayout -> gravity = alignment
        is TextView -> gravity = alignment
    }
    return this
}

fun View.all(node: Node){
    width(node)
    height(node)
    background(node)
    id(node)
    hint(node)
    text(node)
    gravity(node)
    orientation(node)
}
