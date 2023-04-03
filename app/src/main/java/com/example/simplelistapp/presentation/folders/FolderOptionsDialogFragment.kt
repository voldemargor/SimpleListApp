package com.example.simplelistapp.presentation.folders

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.example.simplelistapp.databinding.FragmentFolderOptionsBinding
import com.example.simplelistapp.domain.Folder
import com.example.simplelistapp.presentation.editfolder.EditFolderActivity
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class FolderOptionsDialogFragment : BottomSheetDialogFragment() {

    private var _binding: FragmentFolderOptionsBinding? = null
    private val binding get() = _binding!!

    private lateinit var folder: Folder
    private lateinit var viewModel: FolderOptionsDialogViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        parseParams()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFolderOptionsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this)[FolderOptionsDialogViewModel::class.java]
        binding.tvOptionRename.setOnClickListener() {
            this.dismiss()
            startActivity(EditFolderActivity.newIntentEditFolder(view.context, folder.id))
        }
        binding.tvOptionDelete.setOnClickListener() {
            viewModel.deleteFolder(folder)
            this.dismiss()
        }
        binding.tvCancel.setOnClickListener() {
            this.dismiss()
        }
    }

    companion object {
        private const val KEY_FOLDER = "folder_obj"
        fun newInstance(folder: Folder) = FolderOptionsDialogFragment()
            .apply {
                arguments = Bundle().apply {
                    putParcelable(KEY_FOLDER, folder)
                }
            }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun parseParams() {
        val args = requireArguments()
        if (!args.containsKey(KEY_FOLDER))
            throw RuntimeException("Param FOLDER is absent")
        args.getParcelable<Folder>(KEY_FOLDER)?.let { folder = it }
    }

}
