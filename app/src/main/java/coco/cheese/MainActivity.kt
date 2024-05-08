package coco.cheese

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import coco.cheese.core.Env
import coco.cheese.core.activity.StubEnv
import coco.cheese.core.aidl.ServiceManager
import coco.cheese.core.callback.IActivity
import coco.cheese.core.engine.javet.node
import coco.cheese.core.utils.UI_DIRECTORY
import coco.cheese.core.utils.ui.XmlUtils


class MainActivity : AppCompatActivity() {
    @SuppressLint("MissingInflatedId", "ResourceType")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        Env.get().context = this
        Env.get().activity = this
    }
}