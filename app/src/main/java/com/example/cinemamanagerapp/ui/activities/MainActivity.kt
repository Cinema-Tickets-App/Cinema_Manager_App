package com.example.cinemamanagerapp.ui.activities

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.cinemamanagerapp.R
import com.example.cinemamanagerapp.databinding.ActivityMainBinding
import com.example.cinemamanagerapp.ui.fragment.FavouriteFragment
import com.example.cinemamanagerapp.ui.fragment.HomeFragment
import com.example.cinemamanagerapp.ui.fragment.HistoryFragment
import com.example.cinemamanagerapp.ui.fragment.SettingFragment


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    companion object {
        var userId: Int = 0 //  0 là chưa được gán. vì userId trong database luon luon > 0
        var userName: String = ""
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        userId = intent.getIntExtra("userId", -1)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Hiển thị fragment mặc định
        if (savedInstanceState == null) {
            replaceFragment(HomeFragment())
        }

        // Thiết lập BottomNavigationView
        binding.bottomNav.setOnItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.nav_home -> {
                    replaceFragment(HomeFragment())
                    true
                }

                R.id.nav_fav -> {
                    replaceFragment(FavouriteFragment())
                    true
                }

                R.id.nav_history -> {
                    replaceFragment(HistoryFragment())
                    true
                }

                R.id.nav_settting -> {
                    replaceFragment(SettingFragment())
                    true
                }

                else -> false
            }
        }
        // Nhận thông báo về trạng thái thanh toán
        val paymentStatus = intent.getStringExtra("payment_status")
        if (paymentStatus != null) {
            // Hiển thị thông báo đã thanh toán thành công
            Toast.makeText(this, paymentStatus, Toast.LENGTH_SHORT).show()
        }
    }

    private fun replaceFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.home_fragment_activiy, fragment)

        // Không gọi addToBackStack
        transaction.commit()
    }


}
