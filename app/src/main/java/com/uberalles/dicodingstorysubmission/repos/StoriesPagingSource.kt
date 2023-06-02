package com.uberalles.dicodingstorysubmission.repos

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.uberalles.dicodingstorysubmission.api.ApiService

class StoriesPagingSource(private val apiService: ApiService) : PagingSource<Int, Story>() {
    override fun getRefreshKey(state: PagingState<Int, Story>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Story> {
        return try {
            val page = params.key ?: INITIAL_PAGE_INDEX
            val responseData = apiService.getStories(page, params.loadSize)

            LoadResult.Page(
                data = responseData.listStory ?: emptyList(),
                prevKey = if (page == 1) null else page - 1,
                nextKey = if (responseData.listStory?.isEmpty() == true) null else page + 1
            )
        } catch (exception: Exception) {
            LoadResult.Error(exception)
        }
    }


    private companion object {
        const val INITIAL_PAGE_INDEX = 1
    }
}
