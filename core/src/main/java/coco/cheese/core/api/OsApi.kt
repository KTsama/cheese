package coco.cheese.core.api

import coco.cheese.core.Env
import coco.cheese.core.interfaces.IBase
import coco.cheese.core.interfaces.IEngineBase
import com.caoccao.javet.annotations.V8Function
import com.caoccao.javet.annotations.V8Property
import com.caoccao.javet.interop.NodeRuntime
import java.io.File
import java.lang.ref.WeakReference
import java.util.concurrent.ExecutorService

class OsApi(private val env: Env) : IEngineBase {
    override val executorService: ExecutorService = env.executorService
    override lateinit var nodeRuntime: NodeRuntime

    @V8Function
    override fun setNodeRuntime(key: String) {
        this.nodeRuntime = env.nodeRuntime[key]!!.nodeRuntime
    }

    var _ROOT_DIRECTORY: File
        get() {
            return coco.cheese.core.utils.ROOT_DIRECTORY!!
        }
        set(value) {
            coco.cheese.core.utils.ROOT_DIRECTORY = value
        }

    var _WORKING_DIRECTORY: File
        get() {
            return coco.cheese.core.utils.WORKING_DIRECTORY
        }
        set(value) {
            coco.cheese.core.utils.WORKING_DIRECTORY = value
        }

    var _LOG_DIRECTORY: File
        get() {
            return coco.cheese.core.utils.LOG_DIRECTORY
        }
        set(value) {
            coco.cheese.core.utils.LOG_DIRECTORY = value
        }

    var _MAIN_DIRECTORY: File
        get() {
            return coco.cheese.core.utils.MAIN_DIRECTORY
        }
        set(value) {
            coco.cheese.core.utils.MAIN_DIRECTORY = value
        }

    var _UI_DIRECTORY: File
        get() {
            return coco.cheese.core.utils.UI_DIRECTORY
        }
        set(value) {
            coco.cheese.core.utils.UI_DIRECTORY = value
        }

    var _ASSETS_DIRECTORY: File
        get() {
            return coco.cheese.core.utils.ASSETS_DIRECTORY
        }
        set(value) {
            coco.cheese.core.utils.ASSETS_DIRECTORY = value
        }

    var _JS_DIRECTORY: File
        get() {
            return coco.cheese.core.utils.JS_DIRECTORY
        }
        set(value) {
            coco.cheese.core.utils.JS_DIRECTORY = value
        }

    var _JAVA_MODULE_DIRECTORY: File
        get() {
            return coco.cheese.core.utils.JAVA_MODULE_DIRECTORY
        }
        set(value) {
            coco.cheese.core.utils.JAVA_MODULE_DIRECTORY = value
        }

    var _PYTHON_MODULE_DIRECTORY: File
        get() {
            return coco.cheese.core.utils.PYTHON_MODULE_DIRECTORY
        }
        set(value) {
            coco.cheese.core.utils.PYTHON_MODULE_DIRECTORY = value
        }

    companion object : IBase {
        private var instanceWeak: WeakReference<OsApi>? = null
        private var instance: OsApi? = null
        private val lock = Any()
        override fun get(env: Env, examine: Boolean): OsApi {
            if (this.instance == null || !examine) {
                synchronized(this.lock) {
                    this.instance = OsApi(env)
                }
            }
            return this.instance!!
        }

        override fun getWeak(env: Env, examine: Boolean): OsApi {
            if (this.instanceWeak?.get() == null || !examine) {
                synchronized(this.lock) {
                    this.instanceWeak = WeakReference(OsApi(env))
                }
            }
            return this.instanceWeak?.get()!!
        }

    }
}