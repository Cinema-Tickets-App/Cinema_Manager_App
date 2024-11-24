package com.example.cinemamanagerapp.ui.activities

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.cinemamanagerapp.R
import com.example.cinemamanagerapp.databinding.ActivityMainBinding
import com.example.cinemamanagerapp.ui.fragment.FavouriteFragment
import com.example.cinemamanagerapp.ui.fragment.HomeFragment
import com.example.cinemamanagerapp.ui.fragment.NotificationFragment
import com.example.cinemamanagerapp.ui.fragment.SettingFragment

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    // Tạo biến lưu trữ các Fragment
    private val homeFragment = HomeFragment()
    private val notificationFragment = NotificationFragment()
    private val favouriteFragment = FavouriteFragment()
    private val settingFragment = SettingFragment()

    companion object {
        var userId: Int = 0 // 0 là chưa được gán. vì userId trong database luôn luôn > 0
        var userName: String = ""
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        userId = intent.getIntExtra("userId", -1)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Hiển thị fragment mặc định
        if (savedInstanceState == null) {
            replaceFragment(homeFragment)
        }

        // Thiết lập BottomNavigationView
        binding.bottomNav.setOnItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.nav_home -> {
                    replaceFragment(homeFragment)
                    true
                }
                R.id.nav_notification -> {
                    replaceFragment(notificationFragment)
                    true
                }
                R.id.nav_fav -> {
                    replaceFragment(favouriteFragment)
                    true
                }
                R.id.nav_settting -> {
                    replaceFragment(settingFragment)
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

    // Hàm replaceFragment được tối ưu để không khởi tạo lại mỗi Fragment
    private fun replaceFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()

        // Ẩn tất cả các fragment
        supportFragmentManager.fragments.forEach {
            transaction.hide(it)
        }

        // Hiển thị fragment nếu đã tồn tại, nếu không, thêm vào
        if (fragment.isAdded) {
            transaction.show(fragment)
        } else {
            transaction.add(R.id.home_fragment_activiy, fragment)
        }

        transaction.commit()
    }

    // Xử lý khi nhấn nút back
    override fun onBackPressed() {
        if (binding.bottomNav.selectedItemId != R.id.nav_home) {
            // Chuyển về tab Home nếu không ở Home
            binding.bottomNav.selectedItemId = R.id.nav_home
        } else {
            // Nếu đang ở Home, thoát ứng dụng
            super.onBackPressed()
        }
    }
}
