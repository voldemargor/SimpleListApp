package com.example.simplelistapp.presentation.folders

import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.simplelistapp.R
import com.example.simplelistapp.databinding.ActivityMainBinding
import com.example.simplelistapp.domain.AddFolderUseCase
import com.example.simplelistapp.domain.Repository
import com.example.simplelistapp.presentation.editfolder.EditFolderActivity
import com.example.simplelistapp.presentation.items.ItemsListActivity
import com.example.simplelistapp.presentation.startAnimation
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val viewModel by lazy { ViewModelProvider(this).get(FoldersScreenViewModel::class.java) }

    private lateinit var rvAdapter: FoldersListAdapter
    private lateinit var binding: ActivityMainBinding

    @Inject lateinit var repository1: Repository
    @Inject lateinit var repository2: Repository
    @Inject lateinit var addFolderUseCase: AddFolderUseCase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Log.d("mylog", "repository: $repository1")
        Log.d("mylog", "repository: $repository2")
        Log.d("mylog", "addFolderUseCase: $addFolderUseCase")

        viewModel.initFoldersLD()

        setupRecyclerView()
        observeViewModel()
        setupFab()
    }

    override fun onResume() {
        super.onResume()
        binding.stopFrame.isVisible = false
        binding.fabAddFolder.isVisible = true
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
            toggleEmptyState(it.isEmpty())
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
            FolderOptionsDialogFragment.newInstance(it).show(supportFragmentManager, "dialog")
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
        val animation = AnimationUtils.loadAnimation(this, R.anim.fab_explosion).apply {
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
        binding.stopFrame.isVisible = true
        binding.animCircle.isVisible = false
        startActivity(EditFolderActivity.newIntentAddFolder(this))
    }

    private fun toggleEmptyState(isEmpty: Boolean) {
        if (isEmpty) {
            binding.rvFoldersList.visibility = View.GONE
            binding.emptyFoldersLayout.visibility = View.VISIBLE
        } else {
            binding.rvFoldersList.visibility = View.VISIBLE
            binding.emptyFoldersLayout.visibility = View.GONE
        }
    }
}
