package net.marksheehan.githubsearch

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.findNavController

class MainNavigationHostActivity : AppCompatActivity(R.layout.main_activity) {

    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        navController = findNavController(R.id.navigation_host_fragment)
    }

}
