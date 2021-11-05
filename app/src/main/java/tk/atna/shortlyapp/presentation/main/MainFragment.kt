package tk.atna.shortlyapp.presentation.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import org.koin.androidx.viewmodel.ext.android.viewModel
import tk.atna.shortlyapp.databinding.MainFrBinding
import tk.atna.shortlyapp.extension.hideKeyboard
import tk.atna.shortlyapp.extension.isHitByMotionEvent

class MainFragment : Fragment() {

    private val binding by lazy { MainFrBinding.inflate(layoutInflater) }

    private val viewModel: MainViewModel by viewModel()

    private val adapter by lazy {
        HistoryAdapter(requireContext(), viewModel::copyUrl, viewModel::deleteUrl)
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

    @Suppress("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {
            rvHistory.adapter = adapter
            adapter.registerAdapterDataObserver(adapterDataObserver)

            vFocusDummy.setOnTouchListener { v, event ->
                if (!vgAddPlate.etUrl.isHitByMotionEvent(event)) v.requestFocus()
                false
            }
            vgAddPlate.etUrl.setOnFocusChangeListener { v, hasFocus ->
                if (!hasFocus) vgAddPlate.etUrl.hideKeyboard()
            }

            vgAddPlate.btnShorten.setOnClickListener {
                viewModel.shortenUrl(binding.vgAddPlate.etUrl.text.toString().trim())
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        adapter.unregisterAdapterDataObserver(adapterDataObserver)
    }

    private fun showEmpty(show: Boolean) {
        with(binding) {
            vgEmpty.vgEmpty.isVisible = show
            vgHistory.isVisible = !show
        }
    }
}