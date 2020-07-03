package com.example.walkwithme

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.StrictMode
import android.preference.PreferenceManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.walkwithme.view.challenges.ChallengesFragment
import com.example.walkwithme.view.map.MapFragment
import com.example.walkwithme.view.settings.SettingsFragment
import com.example.walkwithme.view.stats.StatsFragment
import kotlinx.android.synthetic.main.activity_main.*
import org.osmdroid.config.Configuration
import java.util.*

class MainActivity :
    AppCompatActivity() {

    private val permissionRequestCode = 1

    public override fun onCreate(
        savedInstanceState: Bundle?
    ) {
        StrictMode.setThreadPolicy(
            StrictMode.ThreadPolicy.Builder()
                .permitAll()
                .build()
        )

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        loadFragment(MapFragment())

        NavigationView.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.action_map -> {
                    loadFragment(MapFragment())
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.action_challenges -> {
                    loadFragment(ChallengesFragment())
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.action_stats -> {
                    loadFragment(StatsFragment())
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.action_settings -> {
                    loadFragment(SettingsFragment())
                    return@setOnNavigationItemSelectedListener true
                }
            }
            false
        }

        Configuration.getInstance().apply {
            userAgentValue = "OBP_Tuto/1.0"
            load(
                applicationContext,
                PreferenceManager.getDefaultSharedPreferences(applicationContext)
            )
        }

        requestPermissionsIfNecessary(
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.INTERNET,
                Manifest.permission.ACCESS_NETWORK_STATE,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
        )
    }

    private fun loadFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.Container, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }

    private fun requestPermissionsIfNecessary(
        permissions: Array<String>
    ) {
        val permissionsToRequest = ArrayList<String>()
        for (permission in permissions) {
            if (ContextCompat.checkSelfPermission(this, permission)
                != PackageManager.PERMISSION_GRANTED
            ) {
                permissionsToRequest.add(permission)
            }
        }

        if (permissionsToRequest.size > 0) {
            ActivityCompat.requestPermissions(
                this,
                permissionsToRequest.toTypedArray(),
                permissionRequestCode
            )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        val permissionsToRequest = ArrayList<String>()
        for (i in grantResults.indices) {
            permissionsToRequest.add(permissions[i])
        }

        if (permissionsToRequest.size > 0) {
            ActivityCompat.requestPermissions(
                this,
                permissionsToRequest.toTypedArray(),
                permissionRequestCode
            )
        }
    }

}