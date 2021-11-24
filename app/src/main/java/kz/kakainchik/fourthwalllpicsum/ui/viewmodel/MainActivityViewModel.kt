package kz.kakainchik.fourthwalllpicsum.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kz.kakainchik.fourthwalllpicsum.entities.Picture
import kz.kakainchik.fourthwalllpicsum.models.PictureApi
import kz.kakainchik.fourthwalllpicsum.models.PicturePagingSource
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(private val repository: PictureApi): ViewModel() {

    val pics: Flow<PagingData<Picture>> = Pager<Int, Picture>(
        PagingConfig(pageSize = 10)
    ) {
        Log.d(TAG, "Picture PS created")
        PicturePagingSource(repository)
    }.flow.cachedIn(viewModelScope)

    companion object {
        const val TAG: String = "MAIN_ACTIVITY_VM"
    }
}