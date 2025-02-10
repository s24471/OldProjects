package com.example.notesapp

data class Note(
    val id: Int,
    val title: String,
    val content: String,
    val photoPath: String? = null,
    val audioPath: String? = null,
    val location: String? = null // Assuming you want to keep location as well
)
