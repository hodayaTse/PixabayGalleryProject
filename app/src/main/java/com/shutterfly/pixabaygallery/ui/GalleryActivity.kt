package com.shutterfly.pixabaygallery.ui

import android.content.res.Configuration
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.shutterfly.pixabaygallery.adapters.GalleryAdapter
import com.shutterfly.pixabaygallery.databinding.ActivityGalleryBinding
import com.shutterfly.pixabaygallery.repositories.DBRepositoryImpl
import com.shutterfly.pixabaygallery.repositories.GalleryRepository
import com.shutterfly.pixabaygallery.viewmodels.GalleryViewModel
import com.shutterfly.pixabaygallery.viewmodels.GalleryViewModelFactory
import kotlinx.coroutines.*

class GalleryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityGalleryBinding

    private var galleryAdapter: GalleryAdapter? = null

    private val dbRepositoryImpl = DBRepositoryImpl(this)

    private val viewModel by viewModels<GalleryViewModel> {
        GalleryViewModelFactory(GalleryRepository(), dbRepositoryImpl)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGalleryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        horizontalMode()
        setAdapter()
        setupUI()
        setupClickListeners()
        setupObservers()
    }

    private fun setAdapter() {
        galleryAdapter = GalleryAdapter(dbRepositoryImpl){
            //show big image activity
            startActivity(BigImageActivity.getIntent(this, it.previewUrl))

            //update favorite image om DB
            viewModel.updateDB(it.isFavorite, it.id)

            //show or hide favorite heart image
            it.isFavorite = !it.isFavorite
            galleryAdapter?.notifyItemChanged(it.position)
        }
    }

    private fun horizontalMode() {
        //show 4 images on row
        (binding.recycler.layoutManager as? GridLayoutManager)?.apply {
            spanCount = if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) 4 else 3
        }
    }

    private fun setupUI() {
        // disable search button since we start with an empty search text
        binding.searchButton.isEnabled = false
        // setup toolbar
        setSupportActionBar(binding.toolbar)
        // setup recycler view related stuff
        with(binding.recycler) {
            this.adapter = galleryAdapter
            this.setHasFixedSize(true)
        }
    }

    private fun setupClickListeners() {
        binding.searchButton.setOnClickListener {
            val searchTerm = binding.searchText.text.toString()
            viewModel.onSearchButtonClicked(searchTerm)
        }
    }

    private fun setupObservers() {
        // observe for search input changes so we can enable/disable search button when it's empty
        binding.searchText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                binding.searchButton.isEnabled = s.isNotBlank()
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        // observe for new images data
        viewModel.imageListObservable.observe(this) { data ->
            lifecycleScope.launch{
                galleryAdapter?.submitData(data)
            }
        }
    }
}