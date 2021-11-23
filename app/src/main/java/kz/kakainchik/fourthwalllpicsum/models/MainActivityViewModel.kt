package kz.kakainchik.fourthwalllpicsum.models

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import kotlinx.coroutines.flow.Flow
import kz.kakainchik.fourthwalllpicsum.entities.Picture

class MainActivityViewModel : ViewModel() {
    private val repository = PictureApi(ktorClient)

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