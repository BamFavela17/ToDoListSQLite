package com.abrahamfavela.todolistsql

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.abrahamfavela.todolistsql.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var ivSalir: ImageView
    private lateinit var binding: ActivityMainBinding
    private lateinit var db : NotasDBHelper
    private lateinit var notasAdaptador: NotasAdaptador

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = NotasDBHelper(this)
        notasAdaptador = NotasAdaptador(db.getAllNotas(), this)
        binding.notasRV.layoutManager = LinearLayoutManager(this)
        binding.notasRV.adapter = notasAdaptador
        binding.FABAgregarNota.setOnClickListener {
            startActivity(Intent(applicationContext, AgregarNotaActivity::class.java))
        }
        binding.ivSalir.setOnClickListener {
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Salir")
            builder.setMessage("¿Estás seguro de que deseas salir?")
            builder.setPositiveButton("Sí") { dialog, which ->
                // Cerrar sesión y redirigir al LoginActivity
                val sharedPreferences = getSharedPreferences("MyAppPrefs", MODE_PRIVATE)
                val editor = sharedPreferences.edit()
                editor.putBoolean("isLoggedIn", false)
                editor.apply()
                // Redirigir al LoginActivity
                startActivity(Intent(this, LoginActivity::class.java))
                finish() // Cerrar MainActivity
            }
            builder.setNegativeButton("No") { dialog, which -> dialog.dismiss() }
            builder.show()
        }
    }
    override fun onResume() {
        super.onResume()
        notasAdaptador.refrescarLista(db.getAllNotas())
    }
}