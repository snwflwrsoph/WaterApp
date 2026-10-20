package peralta.sophia.waterapp

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.content.ContextCompat
import peralta.sophia.waterapp.notificaciones.Notificador
import peralta.sophia.waterapp.ui.theme.WaterAppTheme
import peralta.sophia.waterapp.viewmodels.WaterViewModel
import peralta.sophia.waterapp.vistas.MainScreen

class MainActivity : ComponentActivity() {

    private val viewModel: WaterViewModel by viewModels()
    private lateinit var notificador: Notificador

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        notificador = Notificador(this)
        notificador.createChannel()

        enableEdgeToEdge()
        setContent {
            WaterAppTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    MainScreen(
                        viewModel = viewModel,
                        onButton = { checkPermissionAndSend() },
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }


    private val askPermission = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { given ->
        if (given) {
            sendReminder()
        } else {
            Toast.makeText(this, R.string.permiso_necesario, Toast.LENGTH_LONG).show()
        }
    }

    private fun hasPermission(): Boolean {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {
            return true
        }
        return ContextCompat.checkSelfPermission(
            this, Manifest.permission.POST_NOTIFICATIONS
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun checkPermissionAndSend() {
        if (hasPermission()) {
            sendReminder()
        } else {
            askPermission.launch(Manifest.permission.POST_NOTIFICATIONS)
        }
    }

    private fun sendReminder() {
        notificador.show()
        viewModel.addRegister()
    }
}