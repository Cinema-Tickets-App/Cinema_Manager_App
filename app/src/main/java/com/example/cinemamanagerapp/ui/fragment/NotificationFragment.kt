package com.example.cinemamanagerapp.ui.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.cinemamanagerapp.R
<<<<<<< HEAD:app/src/main/java/com/example/cinemamanagerapp/ui/fragment/NotificationFragment.kt
import com.example.cinemamanagerapp.ui.adapters.ADTNotification
=======
import com.example.cinemamanagerapp.api.RetrofitClient
import com.example.cinemamanagerapp.models.FoodDrink
import com.example.cinemamanagerapp.ui.adapters.FoodDrinkAdapter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.launch
import org.json.JSONArray
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
>>>>>>> 3e08c8bd1f001a295d56acb1d1c9b969d1f7d841:app/src/main/java/com/example/cinemamanagerapp/ui/fragment/StoreFragment.kt

class NotificationFragment  : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var view : View = inflater.inflate(R.layout.notification, container, false)
        var listView = view.findViewById<ListView>(R.id.rcv_Notificaton);
        listView.adapter = ADTNotification(null);
        return view;
    }
<<<<<<< HEAD:app/src/main/java/com/example/cinemamanagerapp/ui/fragment/NotificationFragment.kt
}
=======

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recyclerview = view.findViewById<RecyclerView>(R.id.rcvFoodDrinkList)

        // gọi dữ liệu từ server
        lifecycleScope.launch {
           try {
               RetrofitClient.apiService.getFoodDrinks().enqueue(object : Callback<List<FoodDrink>>{
                   override fun onResponse(
                       p0: Call<List<FoodDrink>>,
                       p1: Response<List<FoodDrink>>
                   ) {
                       if(p1.isSuccessful){
                           val responseBody = p1.body()!!.toList()
                            val adapter = FoodDrinkAdapter(responseBody)
                           recyclerview.layoutManager = GridLayoutManager(requireContext(), 2)
                           recyclerview.adapter = adapter
                       }
                   }

                   override fun onFailure(p0: Call<List<FoodDrink>>, p1: Throwable) {
                       TODO("Not yet implemented")
                   }
               })
           }catch (e: Exception){
               Log.e("TAG", "onViewCreated: $e", )
           }
        }
    }
}
>>>>>>> 3e08c8bd1f001a295d56acb1d1c9b969d1f7d841:app/src/main/java/com/example/cinemamanagerapp/ui/fragment/StoreFragment.kt
