package com.example.notesapp

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {

    private val notesViewModel: NotesViewModel by viewModels()
    private lateinit var adapter: NotesAdapter
    private lateinit var locationHelper: LocationHelper
    private lateinit var locationPermissionLauncher: ActivityResultLauncher<Array<String>>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        locationHelper = LocationHelper(this)

        val recyclerView = findViewById<RecyclerView>(R.id.recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = NotesAdapter { note ->
            val intent = Intent(this, NoteDetailActivity::class.java)
            intent.putExtra(NoteDetailActivity.NOTE_ID, note.id)
            startActivity(intent)
        }
        recyclerView.adapter = adapter

        notesViewModel.setAdapter(adapter)

        findViewById<FloatingActionButton>(R.id.fab_add_note).setOnClickListener {
            checkLocationPermission {
                locationHelper.getCurrentLocation { cityName ->
                    val newNote = Note(
                        id = 0,
                        title = "New Note",
                        content = "Note content",
                        location = cityName ?: "Unknown location"
                    )
                    notesViewModel.addNote(newNote)
                }
            }
        }

        locationPermissionLauncher = registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            if (permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true || permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true) {
                locationHelper.getCurrentLocation { cityName ->
                    val newNote = Note(
                        id = 0,
                        title = "New Note",
                        content = "Note content",
                        location = cityName ?: "Unknown location"
                    )
                    notesViewModel.addNote(newNote)
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        notesViewModel.loadNotes()
    }

    private fun checkLocationPermission(onPermissionGranted: () -> Unit) {
        val fineLocationPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
        val coarseLocationPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
        if (fineLocationPermission == PackageManager.PERMISSION_GRANTED || coarseLocationPermission == PackageManager.PERMISSION_GRANTED) {
            onPermissionGranted()
        } else {
            locationPermissionLauncher.launch(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION))
        }
    }
}
