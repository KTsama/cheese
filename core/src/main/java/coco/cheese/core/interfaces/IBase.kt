package coco.cheese.core.interfaces

import android.content.Context
import coco.cheese.core.Env

interface IBase {
    fun get(env: Env = Env.get(), examine: Boolean=true) : Any
    fun getWeak(env: Env = Env.get(), examine: Boolean=true) : Any


}