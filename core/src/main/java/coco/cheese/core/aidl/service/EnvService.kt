package coco.cheese.core.aidl.service

import coco.cheese.core.AIDL
import coco.cheese.core.Env
import coco.cheese.core.aidl.client.IEnvClient
import coco.cheese.core.run.checkUrl

import coco.cheese.core.run.debug.PCDebug
import coco.cheese.core.startServer
import coco.cheese.core.t
import coco.cheese.core.utils.ProcessUtils
import com.elvishew.xlog.XLog
import com.hjq.toast.Toaster

class EnvService : IEnvService.Stub() {
    override fun register(callback: IEnvClient) {
        Env.get().javaObjects[AIDL.CLIENT]!![IEnvClient::class.simpleName.toString()] = callback
    }
    override fun start() {
        var T = false
        ProcessUtils.get().setClassLoader()
        val myThread = Thread {
            if (Env.get().runTime.connect || !checkUrl("${Env.get().runTime.ip}/")) {
                if (Env.get().runTime.connect) {
                    Toaster.show("勿重复连接")
                    Env.get().runTime.connect = true
                    T=false
                } else {
                    Toaster.show("无法连接")
                    Env.get().runTime.connect = false
                    T=false
                }
                return@Thread
            }
            T=true
            Env.get().runTime.connect = true
        }
        myThread.start()
        myThread.join()
        if(T){
            Toaster.show("成功连接")
            startServer()
            PCDebug.get().start()
        }
    }
}