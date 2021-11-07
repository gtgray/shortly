package tk.atna.shortlyapp.presentation.base

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import tk.atna.shortlyapp.extension.collect

open class BaseFragment : Fragment() {

    protected val errorHandler: ErrorHandler by lazy { createErrorHandler() }

    protected open val viewModel: BaseViewModel? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel?.run {
            loading.collect(viewLifecycleOwner) { showLoading(it) }
            error.collect(viewLifecycleOwner) { errorHandler.handle(it) }
        }
    }

    protected open fun showLoading(loading: Boolean) = Unit

    protected open fun createErrorHandler(): ErrorHandler = ErrorHandler(createErrorView())

    protected open fun createErrorView(): ErrorView = object : ErrorView(requireContext()) {
        override fun showError(message: String) {
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        }
    }
}