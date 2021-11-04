package tk.atna.shortlyapp.presentation.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import org.koin.androidx.viewmodel.ext.android.viewModel
import tk.atna.shortlyapp.databinding.MainFrBinding

class MainFragment : Fragment() {

    private val binding by lazy { MainFrBinding.inflate(layoutInflater) }

    private val viewModel: MainViewModel by viewModel()

    private val adapter by lazy {
        HistoryAdapter(requireContext(), viewModel::copyUrl, viewModel::deleteUrl)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.rvHistory.adapter = adapter
    }
}