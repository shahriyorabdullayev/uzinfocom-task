package uz.shahriyor.uzinfocom_task.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.lifecycleScope
import com.yandex.mapkit.MapKitFactory
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import uz.shahriyor.uzinfocom_task.data.JsonDataSeeder
import uz.shahriyor.uzinfocom_task.presentation.theme.UzinfocomtaskTheme

class MainActivity : ComponentActivity() {

    private val jsonDataSeeder: JsonDataSeeder by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState == null) {
            lifecycleScope.launch { jsonDataSeeder.seed() }
        }
        enableEdgeToEdge()
        setContent {
            UzinfocomtaskTheme {
                MainScreen()
            }
        }
    }

    override fun onStart() {
        super.onStart()
        MapKitFactory.getInstance().onStart()
    }

    override fun onStop() {
        MapKitFactory.getInstance().onStop()
        super.onStop()
    }
}
