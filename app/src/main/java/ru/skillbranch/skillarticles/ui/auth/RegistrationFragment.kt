package ru.skillbranch.skillarticles.ui.auth

import android.util.Patterns
import androidx.annotation.VisibleForTesting
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import androidx.savedstate.SavedStateRegistryOwner
import kotlinx.android.synthetic.main.fragment_registration.*
import ru.skillbranch.skillarticles.R
import ru.skillbranch.skillarticles.ui.RootActivity
import ru.skillbranch.skillarticles.ui.base.BaseFragment
import ru.skillbranch.skillarticles.viewmodels.auth.AuthViewModel
import ru.skillbranch.skillarticles.viewmodels.base.Notify

/**
 * Created by Anna Shabaeva on 05.11.2020
 */

class RegistrationFragment() : BaseFragment<AuthViewModel>() {

    //for testing
    var _mockFactory: ((SavedStateRegistryOwner) -> ViewModelProvider.Factory)? = null

    override val viewModel: AuthViewModel by viewModels {
        _mockFactory?.invoke(this) ?: defaultViewModelProviderFactory
    }

    private val args: RegistrationFragmentArgs by navArgs()
    override val layout: Int = R.layout.fragment_registration

    override fun setupViews() {

        btn_register.setOnClickListener {
            if (viewModel.isValid(
                    et_name.text.toString(),
                    et_login.text.toString(),
                    et_password.text.toString(),
                    et_repeat_password.text.toString()
                )
            ) {
                viewModel.handleRegister(
                    et_name.text.toString(),
                    et_login.text.toString(),
                    et_password.text.toString(),
                    if (args.privateDestination == -1) null else args.privateDestination
                )
            }
        }
    }
}
