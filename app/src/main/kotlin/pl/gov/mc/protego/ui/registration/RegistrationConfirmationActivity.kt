package pl.gov.mc.protego.ui.registration

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.core.widget.doOnTextChanged
import kotlinx.android.synthetic.main.registration_confirmation_view.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import pl.gov.mc.protego.R
import pl.gov.mc.protego.ui.base.BaseActivity
import pl.gov.mc.protego.ui.main.DashboardActivity
import pl.gov.mc.protego.ui.observeLiveData

class RegistrationConfirmationActivity : BaseActivity() {

    private val viewModel: RegistrationConfirmationViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.registration_confirmation_view)

        sms_code.doOnTextChanged { text, _, _, _ -> viewModel.onCodeChanged(text.toString()) }

        confirm_registration_button.setOnClickListener {
            viewModel.confirm(sms_code.text.toString())
        }

        with(viewModel) {
            observeLiveData(confirmationEnabled) { confirm_registration_button.isEnabled = it }

            observeLiveData(confirmationError) {
                Toast.makeText(
                    this@RegistrationConfirmationActivity,
                    "Problem z rejestracją: $it",
                    Toast.LENGTH_LONG
                ).show()
            }

            observeLiveData(confirmationSuccess) {
                navigateToMain()
            }
        }

    }

    private fun navigateToMain() {
        startActivity(Intent(this, DashboardActivity::class.java))
        finish()
    }
}