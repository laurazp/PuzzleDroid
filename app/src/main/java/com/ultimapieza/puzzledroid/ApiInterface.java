package com.ultimapieza.puzzledroid;

import com.ultimapieza.puzzledroid.entidades.Players;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ApiInterface {

    //@GET("/retrofit/getuser.php")
    @GET("Players")
        // API's endpoints
    Call<List<Players>> getUsersList();

// UserListResponse is POJO class to get the data from API,
// we use List<UserListResponse> in callback because the data in our API is starting from JSONArray

}
