package tk.atna.shortlyapp.presentation.main

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.RecyclerView
import org.koin.androidx.viewmodel.ext.android.viewModel
import tk.atna.shortlyapp.R
import tk.atna.shortlyapp.databinding.MainFrBinding
import tk.atna.shortlyapp.domain.model.ShortenedUrl
import tk.atna.shortlyapp.extension.collect
import tk.atna.shortlyapp.extension.copyToClipboard
import tk.atna.shortlyapp.extension.isHitByMotionEvent
import tk.atna.shortlyapp.presentation.base.BaseFragment
import tk.atna.shortlyapp.presentation.base.ErrorHandler
import tk.atna.shortlyapp.presentation.base.ErrorView
import tk.atna.shortlyapp.presentation.model.InputException

class MainFragment : BaseFragment() {

    private val binding by lazy { MainFrBinding.inflate(layoutInflater) }

    override val viewModel: MainViewModel by viewModel()

    private val adapter by lazy {
        HistoryAdapter(requireContext(), ::copyUrl, viewModel::deleteUrl)
    }

    private val adapterDataObserver by lazy {
        object : RecyclerView.AdapterDataObserver() {
            override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
                showEmpty(adapter.itemCount == 0)
            }

            override fun onItemRangeRemoved(positionStart: Int, itemCount: Int) {
                showEmpty(adapter.itemCount == 0)
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return binding.root
    }

    @Suppress("ClickableViewAccessibility", "ResourceType")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {
            rvHistory.adapter = adapter
            (rvHistory.itemAnimator as DefaultItemAnimator).supportsChangeAnimations = false
            adapter.registerAdapterDataObserver(adapterDataObserver)

            vFocusDummy.setOnTouchListener { v, event ->
                if (!vgAddPlate.etUrl.isHitByMotionEvent(event)) v.requestFocus()
                false
            }
            vgAddPlate.btnShorten.setOnClickListener {
                viewModel.shortenUrl(binding.vgAddPlate.etUrl.text.toString().trim())
            }
        }

        viewModel.history.collect(viewLifecycleOwner) {
            adapter.submitList(it)
        }

        viewModel.submitSuccess.collect(viewLifecycleOwner) {
            binding.vgAddPlate.etUrl.text = null
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        adapter.unregisterAdapterDataObserver(adapterDataObserver)
    }

    override fun showLoading(loading: Boolean) {
        with(binding.vgAddPlate) {
            etUrl.isEnabled = !loading
            btnShorten.isEnabled = !loading
            btnShorten.text = if (loading) "" else getString(R.string.add_plate_btn)
            vProgress.isVisible = loading
        }
    }

    override fun createErrorHandler() = InputErrorHandler(requireContext(), createErrorView())

    override fun createErrorView() = InputErrorView(requireContext())

    private fun copyUrl(shortenedUrl: ShortenedUrl) {
        requireContext().copyToClipboard(shortenedUrl.shortLink)
        viewModel.copyUrl(shortenedUrl)
    }

    private fun showEmpty(show: Boolean) {
        with(binding) {
            vgEmpty.vgEmpty.isVisible = show
            vgHistory.isVisible = !show
        }
    }

    inner class InputErrorHandler(
        private val context: Context,
        errorView: InputErrorView
    ) : ErrorHandler(errorView) {

        override fun handle(t: Throwable?) {
            when (t) {
                is InputException -> (errorView as InputErrorView).showInputError(context.getString(t.errorMessage))
                else -> super.handle(t)
            }
        }
    }

    inner class InputErrorView(
        context: Context
    ) : ErrorView(context) {

        override fun showError(message: String) {
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
            binding.vgAddPlate.etUrl.showError()
        }

        fun showInputError(message: String) {
            binding.vgAddPlate.etUrl.showError(message)
        }
    }
}