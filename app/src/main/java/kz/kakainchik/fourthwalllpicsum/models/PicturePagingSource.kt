package kz.kakainchik.fourthwalllpicsum.models

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.bumptech.glide.load.HttpException
import kz.kakainchik.fourthwalllpicsum.entities.Picture
import java.io.IOException

class PicturePagingSource constructor(private val apiService: PictureApi) :
    PagingSource<Int, Picture>() {

    override fun getRefreshKey(state: PagingState<Int, Picture>): Int? {
        //If both is null - return first page
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Picture> {
        val pageIndex: Int = params.key ?: 1

        return try {
            Log.d(TAG, "Start loading pictures")
            val response = apiService.makeGetPicturesListRequest(pageIndex)

            val nextKey = if(response.isEmpty()) null else pageIndex + 1
            val prevKey = if(pageIndex == 1) null else pageIndex - 1

            LoadResult.Page(response, prevKey, nextKey)
        } catch(ex: IOException) {
            return LoadResult.Error(ex)
        } catch(ex: HttpException) {
            return LoadResult.Error(ex)
        }
    }

    companion object {
        const val TAG: String = "PICTURE_PADDING_SOURCE"
    }
}