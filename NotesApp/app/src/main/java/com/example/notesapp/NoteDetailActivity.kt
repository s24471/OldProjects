package com.example.notesapp

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.media.MediaPlayer
import android.media.MediaRecorder
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.example.myapplication.R
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date

class NoteDetailActivity : AppCompatActivity() {

    private val notesViewModel: NotesViewModel by viewModels()
    private var note: Note? = null
    private var photoPath: String? = null
    private var audioPath: String? = null
    private lateinit var imageView: ImageView
    private lateinit var takePictureLauncher: ActivityResultLauncher<Uri>
    private lateinit var editPhotoLauncher: ActivityResultLauncher<Intent>
    private lateinit var photoUri: Uri

    private lateinit var cameraPermissionLauncher: ActivityResultLauncher<String>
    private lateinit var audioPermissionLauncher: ActivityResultLauncher<String>

    private var mediaRecorder: MediaRecorder? = null
    private var mediaPlayer: MediaPlayer? = null

    private lateinit var addVoiceRecordingButton: Button
    private lateinit var removeVoiceRecordingButton: Button
    private lateinit var voiceRecordingStatus: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_note_detail)

        val noteId = intent.getIntExtra(NOTE_ID, -1)
        note = notesViewModel.getNoteById(noteId)

        val titleEditText = findViewById<EditText>(R.id.edit_note_title)
        val contentEditText = findViewById<EditText>(R.id.edit_note_content)
        val saveButton = findViewById<Button>(R.id.button_save)
        val deleteButton = findViewById<Button>(R.id.button_delete)
        val addPhotoButton = findViewById<Button>(R.id.button_add_photo)
        val removePhotoButton = findViewById<Button>(R.id.button_remove_photo)
        addVoiceRecordingButton = findViewById(R.id.button_add_voice_recording)
        removeVoiceRecordingButton = findViewById(R.id.button_remove_voice_recording)
        voiceRecordingStatus = findViewById(R.id.voice_recording_status)
        imageView = findViewById(R.id.note_photo)

        note?.let {
            titleEditText.setText(it.title)
            contentEditText.setText(it.content)
            it.photoPath?.let { path ->
                val bitmap = BitmapFactory.decodeFile(path)
                imageView.setImageBitmap(bitmap)
                imageView.visibility = ImageView.VISIBLE
                removePhotoButton.visibility = Button.VISIBLE
            }
            it.audioPath?.let { path ->
                audioPath = path
                voiceRecordingStatus.text = "Recording available"
                voiceRecordingStatus.visibility = TextView.VISIBLE
                removeVoiceRecordingButton.visibility = Button.VISIBLE
                setPlayRecordingButton()
            }
        }

        addPhotoButton.setOnClickListener {
            checkCameraPermission {
                dispatchTakePictureIntent()
            }
        }

        removePhotoButton.setOnClickListener {
            imageView.setImageBitmap(null)
            imageView.visibility = ImageView.GONE
            removePhotoButton.visibility = Button.GONE
            photoPath = null
        }

        if (audioPath == null) {
            setRecordButton()
        }

        removeVoiceRecordingButton.setOnClickListener {
            stopPlaying()
            audioPath = null
            voiceRecordingStatus.text = "No recording"
            voiceRecordingStatus.visibility = TextView.GONE
            removeVoiceRecordingButton.visibility = Button.GONE
            setRecordButton()
        }

        saveButton.setOnClickListener {
            val updatedNote = note?.copy(
                title = titleEditText.text.toString(),
                content = contentEditText.text.toString(),
                photoPath = photoPath,
                audioPath = audioPath
            ) ?: Note(
                id = 0, // Assuming 0 means new note
                title = titleEditText.text.toString(),
                content = contentEditText.text.toString(),
                photoPath = photoPath,
                audioPath = audioPath
            )
            if (updatedNote.id == 0) {
                notesViewModel.addNote(updatedNote)
            } else {
                notesViewModel.updateNote(updatedNote)
            }
            finish()
        }

        deleteButton.setOnClickListener {
            note?.let { notesViewModel.deleteNote(it) }
            finish()
        }

        takePictureLauncher = registerForActivityResult(ActivityResultContracts.TakePicture()) { success ->
            if (success) {
                val intent = Intent(this, DrawActivity::class.java).apply {
                    putExtra("photoPath", photoPath)
                }
                editPhotoLauncher.launch(intent)
            }
        }

        editPhotoLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                photoPath?.let {
                    val bitmap = BitmapFactory.decodeFile(it)
                    imageView.setImageBitmap(bitmap)
                    imageView.visibility = ImageView.VISIBLE
                    removePhotoButton.visibility = Button.VISIBLE
                }
            }
        }

        cameraPermissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                dispatchTakePictureIntent()
            }
        }

        audioPermissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                startRecording()
            }
        }
    }

    private fun setRecordButton() {
        addVoiceRecordingButton.text = "Add Voice Recording"
        addVoiceRecordingButton.setOnClickListener {
            checkAudioPermission {
                startRecording()
            }
        }
    }

    private fun setPlayRecordingButton() {
        addVoiceRecordingButton.text = "Play Recording"
        addVoiceRecordingButton.setOnClickListener {
            startPlaying()
        }
    }

    private fun dispatchTakePictureIntent() {
        val photoFile: File? = try {
            createImageFile()
        } catch (ex: IOException) {
            null
        }
        photoFile?.also {
            photoUri = FileProvider.getUriForFile(this, "com.example.myapplication.fileprovider", it)
            takePictureLauncher.launch(photoUri)
        }
    }

    @Throws(IOException::class)
    private fun createImageFile(): File {
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val storageDir: File = getExternalFilesDir(Environment.DIRECTORY_PICTURES)!!
        return File.createTempFile(
            "JPEG_${timeStamp}_",
            ".jpg",
            storageDir
        ).apply {
            photoPath = absolutePath
        }
    }

    private fun checkCameraPermission(onPermissionGranted: () -> Unit) {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            onPermissionGranted()
        } else {
            cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
        }
    }

    private fun checkAudioPermission(onPermissionGranted: () -> Unit) {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED) {
            onPermissionGranted()
        } else {
            audioPermissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
        }
    }

    private fun startRecording() {
        audioPath = createAudioFile().absolutePath
        mediaRecorder = MediaRecorder().apply {
            setAudioSource(MediaRecorder.AudioSource.MIC)
            setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP)
            setOutputFile(audioPath)
            setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB)
            try {
                prepare()
                start()
                voiceRecordingStatus.text = "Recording..."
                voiceRecordingStatus.visibility = TextView.VISIBLE
                addVoiceRecordingButton.text = "Stop Recording"
                addVoiceRecordingButton.setOnClickListener {
                    stopRecording()
                }
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    private fun stopRecording() {
        mediaRecorder?.apply {
            stop()
            release()
        }
        mediaRecorder = null
        voiceRecordingStatus.text = "Recording saved"
        setPlayRecordingButton()
        removeVoiceRecordingButton.visibility = Button.VISIBLE
    }

    private fun startPlaying() {
        mediaPlayer = MediaPlayer().apply {
            try {
                setDataSource(audioPath)
                prepare()
                start()
                voiceRecordingStatus.text = "Playing..."
                addVoiceRecordingButton.text = "Stop Playing"
                addVoiceRecordingButton.setOnClickListener {
                    stopPlaying()
                }
                setOnCompletionListener {
                    voiceRecordingStatus.text = "Recording available"
                    setPlayRecordingButton()
                }
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    private fun stopPlaying() {
        mediaPlayer?.release()
        mediaPlayer = null
        voiceRecordingStatus.text = "Recording available"
        setPlayRecordingButton()
    }

    @Throws(IOException::class)
    private fun createAudioFile(): File {
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val storageDir: File = getExternalFilesDir(Environment.DIRECTORY_MUSIC)!!
        return File.createTempFile(
            "3GP_${timeStamp}_",
            ".3gp",
            storageDir
        )
    }

    companion object {
        const val NOTE_ID = "note_id"
    }
}
