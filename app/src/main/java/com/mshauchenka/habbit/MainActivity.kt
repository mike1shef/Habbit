package com.mshauchenka.habbit

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.mshauchenka.habbit.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding : ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val application = requireNotNull(this).application
        val dao = TaskDataBase.getInstance(application).taskDao
        val viewModelFactory = TasksViewModelFactory(dao)
        val vm = ViewModelProvider(this, viewModelFactory)[MainViewModel::class.java]

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController



        binding.bottomNav.setupWithNavController(navController)


        vm.tasks.observe(this, Observer {
            if (it.isNullOrEmpty()){
                Toast.makeText(this, "You need to add data first", Toast.LENGTH_LONG).show()
                navHostFragment.navController.navigate(R.id.action_mainElementFragment_to_addNoteFragment)
            } else if (it.isNotEmpty() && vm.currentTask.value == null) {
                vm.selectRandomTask()
            }
        })


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }


    override fun onDestroy() {
        super.onDestroy()
    }
}