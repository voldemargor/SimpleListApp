package com.example.simplelistapp.presentation.folders

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Gravity
import android.view.ViewGroup
import android.view.Window
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.AnimationUtils
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.simplelistapp.R
import com.example.simplelistapp.databinding.ActivityMainBinding
import com.example.simplelistapp.domain.Folder
import com.example.simplelistapp.presentation.editfolder.EditFolderActivity
import com.example.simplelistapp.presentation.items.ItemsListActivity
import com.example.simplelistapp.presentation.startAnimation

class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: FoldersScreenViewModel
    private lateinit var rvAdapter: FoldersListAdapter
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this).get(FoldersScreenViewModel::class.java)

        setupRecyclerView()
        observeViewModel()
        setupFab()
    }

    override fun onResume() {
        super.onResume()
        binding.stopFrame.isVisible = false
    }

    private fun setupRecyclerView() {
        rvAdapter = FoldersListAdapter()
        binding.rvFoldersList.adapter = rvAdapter

        setOnClickListener()
        setOnLongClickListener()
        setOnFolderMenuClickListener()
        setSwipeListener()
    }

    private fun observeViewModel() {
        viewModel.foldersList.observe(this) {
            rvAdapter.submitList(it)
        }
    }

    private fun setOnClickListener() {
        rvAdapter.onFolderClickListener = {
            startActivity(ItemsListActivity.newIntentItemsList(this, it.id, it.name))
        }
    }

    private fun setOnLongClickListener() {
        rvAdapter.onFolderLongClickListener = {
            startActivity(EditFolderActivity.newIntentEditFolder(this, it.id))
        }
    }

    private fun setOnFolderMenuClickListener() {
        rvAdapter.onFolderMenuClickListener = {
            displayFolderMenu(it)
        }
    }

    private fun displayFolderMenu(folder: Folder) {
        val dialog = Dialog(this).apply {
            requestWindowFeature(Window.FEATURE_NO_TITLE)
            setContentView(R.layout.folder_menu)
        }

        dialog.findViewById<TextView>(R.id.tv1_cancel).setOnClickListener() {
            dialog.dismiss()
        }
        dialog.findViewById<TextView>(R.id.tv1_option_rename).setOnClickListener() {
            startActivity(EditFolderActivity.newIntentEditFolder(this, folder.id))
            dialog.dismiss()
        }
        dialog.findViewById<TextView>(R.id.tv1_option_delete).setOnClickListener() {
            viewModel.deleteFolder(folder)
            dialog.dismiss()
        }

        with(dialog) {
            show()
            window?.setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            window?.attributes?.windowAnimations = R.style.FolderMenuAnimation
            window?.setGravity(Gravity.BOTTOM)
        }
    }

    private fun setSwipeListener() {
        val callback = object : ItemTouchHelper
        .SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {

            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val folder = rvAdapter.currentList[viewHolder.adapterPosition]
                viewModel.deleteFolder(folder)
            }
        }

        val itemTouchHelper = ItemTouchHelper(callback)
        itemTouchHelper.attachToRecyclerView(binding.rvFoldersList)
    }

    private fun setupFab() {
        val animation = AnimationUtils.loadAnimation(this, R.anim.circle_explosion_anim).apply {
            duration = 360
            interpolator = AccelerateDecelerateInterpolator()
        }

        binding.fabAddFolder.setOnClickListener() {
            binding.fabAddFolder.isVisible = false
            binding.animCircle.isVisible = true
            binding.animCircle.startAnimation(animation) {
                onFabAnimationFinished()
            }
        }
    }

    private fun onFabAnimationFinished() {
        supportActionBar?.hide()
        binding.fabAddFolder.isVisible = true
        binding.stopFrame.isVisible = true
        binding.animCircle.isVisible = false
        startActivity(EditFolderActivity.newIntentAddFolder(this))
    }

}