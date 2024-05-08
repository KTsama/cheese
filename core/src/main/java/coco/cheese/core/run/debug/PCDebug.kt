package coco.cheese.core.run.debug

import coco.cheese.core.Env
import coco.cheese.core.activity.StubEnv
import coco.cheese.core.callback.IActivity
import coco.cheese.core.engine.javet.ConsoleLogger
import coco.cheese.core.engine.javet.node
import coco.cheese.core.interfaces.IBase
import coco.cheese.core.run.Utils
import coco.cheese.core.utils.AppUtils
import coco.cheese.core.utils.HttpUtils
import coco.cheese.core.utils.OsUtils
import coco.cheese.core.utils.ProcessUtils
import coco.cheese.core.utils.UI_DIRECTORY
import coco.cheese.core.utils.ZipUtils
import coco.cheese.core.utils.ui.XmlUtils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.ref.WeakReference
import java.util.concurrent.Executors
import kotlin.coroutines.cancellation.CancellationException

class PCDebug(private val env: Env) {

    fun start() {
        val scope = CoroutineScope(Dispatchers.Default)
        scope.launch {

            while (true) {
                try {
                    delay(1000) // 非阻塞地延迟1秒
                } catch (e: CancellationException) {
                    throw RuntimeException(e)
                }
                HttpUtils.get().get("${env.runTime.ip}/heartbeat")
                val response = HttpUtils.get().get("${env.runTime.ip}/cmd")
                // 使用协程流处理不同的步骤
                when (response.toIntOrNull()) {
                    1 -> run()
                    2 -> ui()
                    3 -> exit()
                    else -> {}
                }
            }
        }
    }

    suspend fun run() {
        flow {
            Thread {
                Utils.get().run()

            }.start()
            emit("1 = >执行")
        }.collect {
            // 在这里处理步骤1的结果
            println(it)
        }
    }

    suspend fun ui() {
        flow {
            Thread {
                Utils.get().runUi()
            }.start()
            emit("2 = >执行")
        }.collect {
            // 在这里处理步骤2的结果
            println(it)
        }
    }

    suspend fun exit() {
        flow {
            // 在流中执行可能会阻塞的操作
            ProcessUtils.get().exit()
            // 发射步骤2的结果
            emit("3 = >执行")
        }.collect {
            // 在这里处理步骤2的结果
            println(it)
        }
    }

    suspend fun upload() {
        flow {
            // 在流中执行可能会阻塞的操作
            delay(1000) // 例如延迟1秒
            // 发射步骤2的结果
            emit("4 = >执行")
        }.collect {
            // 在这里处理步骤2的结果
            println(it)
        }
    }
    companion object : IBase {
        private var instanceWeak: WeakReference<PCDebug>? = null
        private var instance: PCDebug? = null
        private val lock = Any()
        override fun get(env: Env, examine: Boolean) : PCDebug {
            if (this.instance == null || !examine) {
                synchronized(this.lock) {
                    this.instance = PCDebug(env)
                }
            }
            return this.instance!!
        }

        override fun getWeak(env: Env, examine: Boolean): PCDebug {
            if (this.instanceWeak?.get() == null || !examine) {
                synchronized(this.lock) {
                    this.instanceWeak = WeakReference(PCDebug(env))
                }
            }
            return this.instanceWeak?.get()!!
        }

    }



}