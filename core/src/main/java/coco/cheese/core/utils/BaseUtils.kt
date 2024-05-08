package coco.cheese.core.utils

import android.content.ComponentName
import android.content.Intent
import android.os.Process
import coco.cheese.core.Env
import coco.cheese.core.callback.IJS
import coco.cheese.core.engine.javet.ConsoleLogger
import coco.cheese.core.interfaces.IBase
import coco.cheese.core.interfaces.JAction
import coco.cheese.core.runOnUi
import com.hjq.toast.Toaster
import java.lang.ref.WeakReference

class BaseUtils(private val env: Env) {

    fun sleep(tim: Long) {
        try {
            Thread.sleep(tim)
        } catch (e: InterruptedException) {
            throw RuntimeException(e)
        }
    }
    fun toast(message: String) {
        ConsoleLogger.i(message)
        runOnUi { Toaster.show(message) }
    }

    fun toast(format: String, vararg objects: Any) {
        val msg = String.format(format, *objects)
        ConsoleLogger.i(msg)
        runOnUi { Toaster.show(msg) }
    }

    fun exit() {
        ProcessUtils.get().exit()
    }
    fun runJS(nodeName: String,js:String): Any {
        return env.nodeRuntime[nodeName]!!.nodeRuntime.getExecutor(js).execute()
    }


    fun runOnUi(action: JAction) =
        runOnUi{
            action.invoke()
        }

    companion object : IBase {
        private var instanceWeak: WeakReference<BaseUtils>? = null
        private var instance: BaseUtils? = null
        private val lock = Any()
        override fun get(env: Env, examine: Boolean): BaseUtils {
            if (this.instance == null || !examine) {
                synchronized(this.lock) {
                    this.instance = BaseUtils(env)
                }
            }
            return this.instance!!
        }

        override fun getWeak(env: Env, examine: Boolean): BaseUtils {
            if (this.instanceWeak?.get() == null || !examine) {
                synchronized(this.lock) {
                    this.instanceWeak = WeakReference(BaseUtils(env))
                }
            }
            return this.instanceWeak?.get()!!
        }

    }
}