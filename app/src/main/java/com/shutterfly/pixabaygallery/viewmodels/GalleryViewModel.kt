package com.shutterfly.pixabaygallery.viewmodels

import androidx.lifecycle.*
import androidx.paging.*
import com.shutterfly.pixabaygallery.database.FavoritesImageData
import com.shutterfly.pixabaygallery.repositories.DBRepositoryImpl
import com.shutterfly.pixabaygallery.repositories.GalleryRepository
import kotlinx.coroutines.*

class GalleryViewModel(private val repository: GalleryRepository, private val dbRepository: DBRepositoryImpl) : ViewModel() {

    private companion object {
        private const val DEFAULT_SEARCH_KEYWORD = "android"
    }

    private val _currentKeyword = MutableLiveData(DEFAULT_SEARCH_KEYWORD)

    val imageListObservable = _currentKeyword.switchMap { keyword ->
        repository.searchImages(keyword)
    }.cachedIn(viewModelScope)

    fun onSearchButtonClicked(keyword: String) {
        if (keyword.isNotBlank()) {
            _currentKeyword.value = keyword
        }
    }

    fun updateDB(isFavorite: Boolean, id: Int) {
       viewModelScope.launch {
            if (isFavorite) {
                //delete favorite from DB
                dbRepository.delete(FavoritesImageData(id))
            } else {
                //insert favorite to DB
                dbRepository.insertAll(FavoritesImageData(id))
            }
        }
    }
}


class GalleryViewModelFactory(private val repository: GalleryRepository, private val dbRepository: DBRepositoryImpl) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(GalleryViewModel::class.java)) {
            GalleryViewModel(repository, dbRepository) as T
        } else {
            throw IllegalArgumentException("ViewModel not found")
        }
    }
}