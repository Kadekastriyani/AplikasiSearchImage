package com.D121211035.SearchImage.ui.components

import com.D121211035.SearchImage.network.ApiService
import com.D121211035.SearchImage.network.model.PixabayResponse
import com.D121211035.SearchImage.util.Constant
import com.D121211035.SearchImage.util.Resource
import java.lang.Exception
import javax.inject.Inject

class MainRepository @Inject constructor(private val apiService:ApiService) {


    suspend fun getQueryItems(q:String):Resource<PixabayResponse>{
      return  try{

            val result = apiService.getQueryImages(query = q, apiKey = Constant.KEY, imageType = "photo")
            Resource.Success(data = result)
        }catch (e:Exception){
            Resource.Error(message = e.message.toString())
        }
    }


}