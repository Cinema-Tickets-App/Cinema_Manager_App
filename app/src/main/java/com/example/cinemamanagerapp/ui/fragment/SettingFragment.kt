package com.example.cinemamanagerapp.ui.fragment

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import com.example.cinemamanagerapp.R
import com.example.cinemamanagerapp.ui.activities.ChangePassword
import com.example.cinemamanagerapp.ui.activities.History
import com.example.cinemamanagerapp.ui.activities.LoginActivity
import com.example.cinemamanagerapp.ui.activities.MainActivity
import com.example.cinemamanagerapp.ui.activities.accounts.AccountActivity

class SettingFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate layout cho fragment
        val view = inflater.inflate(R.layout.fragment_setting, container, false)
        // Tìm LinearLayout và thiết lập OnClickListener
        val lnAccount: LinearLayout = view.findViewById(R.id.lnAccount)
        lnAccount.setOnClickListener {
            val intent = Intent(activity, AccountActivity::class.java)
            startActivity(intent)
        }
        val lnHistory : LinearLayout = view.findViewById(R.id.lnHistory);
        lnHistory.setOnClickListener({context?.startActivity(Intent(context,History::class.java))})

        val lnChangePassword : LinearLayout = view.findViewById(R.id.lnChangePassword);
        lnChangePassword.setOnClickListener({context?.startActivity(Intent(context,ChangePassword::class.java))})

        // Tìm nút Đăng xuất và thiết lập OnClickListener
        val btnLogout: Button = view.findViewById(R.id.btnLogout)
        btnLogout.setOnClickListener {
            logoutUser()
        }
        return view
    }
    private fun logoutUser() {
        // Xóa thông tin người dùng khỏi SharedPreferences
        val sharedPreferences =
            activity?.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
        sharedPreferences?.edit()?.clear()?.apply()
        // Chuyển hướng về màn hình đăng nhập
        (context as MainActivity).finish()
    }
}
