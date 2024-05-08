package coco.cheese.core.utils

import coco.cheese.core.Env
import coco.cheese.core.interfaces.IBase
import java.io.File
import java.lang.ref.WeakReference
var ROOT_DIRECTORY = Env.get().context.getExternalFilesDir("")?.parentFile
var WORKING_DIRECTORY = File(ROOT_DIRECTORY,if (Env.get().runTime.runMode) "release" else "debug")
var LOG_DIRECTORY = File(ROOT_DIRECTORY,"log")
var MAIN_DIRECTORY = File(WORKING_DIRECTORY,"main")
var UI_DIRECTORY = File(MAIN_DIRECTORY,"ui")
var ASSETS_DIRECTORY = File(MAIN_DIRECTORY,"assets")
var JS_DIRECTORY = File(MAIN_DIRECTORY,"js")
var JAVA_MODULE_DIRECTORY = File(WORKING_DIRECTORY,"java")
var PYTHON_MODULE_DIRECTORY = File(WORKING_DIRECTORY,"python")
class OsUtils(private val env: Env) {
    companion object: IBase {
        private var instanceWeak: WeakReference<OsUtils>? = null
        private var instance: OsUtils? = null
        private val lock = Any()
        override fun get(env: Env, examine: Boolean) : OsUtils {
            if (this.instance == null || !examine) {
                synchronized(this.lock) {
                    this.instance = OsUtils(env)
                }
            }
            return this.instance!!
        }

        override fun getWeak(env: Env, examine: Boolean): OsUtils {
            if (this.instanceWeak?.get() == null || !examine) {
                synchronized(this.lock) {
                    this.instanceWeak = WeakReference(OsUtils(env))
                }
            }
            return this.instanceWeak?.get()!!
        }


    }
}