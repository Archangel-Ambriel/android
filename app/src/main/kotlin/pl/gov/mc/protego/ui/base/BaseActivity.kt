package pl.gov.mc.protego.ui.base

import android.content.Context
import android.hardware.SensorManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.DialogFragment
import org.koin.android.ext.android.inject
import pl.gov.mc.protego.R
import pl.gov.mc.protego.ui.dialog.NoInternetConnectionDialog

abstract class BaseActivity : AppCompatActivity() {
    private val shakeDetector: CockpitShakeDetector by inject()

    private var noInternetConnectionDialog: DialogFragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initShakeDetection()
    }

    override fun setContentView(layoutResID: Int) {
        super.setContentView(layoutResID)
        findViewById<Toolbar>(R.id.toolbar)?.apply {
            setSupportActionBar(this)
            supportActionBar?.apply {
                setDisplayShowTitleEnabled(false)
            }
        }
    }

    override fun onStop() {
        super.onStop()
        shakeDetector.stopDetection()
    }

    private fun initShakeDetection() {
        val sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        shakeDetector.startDetection(sensorManager)
        shakeDetector.setListener { CockpitMenuLauncher.showCockpit(supportFragmentManager) }
    }

    fun showNoInternetConnectionDialog() {
        noInternetConnectionDialog =
            noInternetConnectionDialog ?: NoInternetConnectionDialog().apply {
                show(supportFragmentManager, null)
            }
    }

    fun hideNoInternetConnectionDialog() {
        noInternetConnectionDialog?.also {
            it.dismiss()
            noInternetConnectionDialog = null
        }
    }
}