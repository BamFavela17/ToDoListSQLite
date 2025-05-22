package com.abrahamfavela.todolistsql

import android.R
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.abrahamfavela.todolistsql.databinding.ActivityAgregarNotaBinding
import com.abrahamfavela.todolistsql.databinding.ActivityMainBinding

class AgregarNotaActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAgregarNotaBinding
    private lateinit var db : NotasDBHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityAgregarNotaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = NotasDBHelper(this)

        binding.ivCuardarNota.setOnClickListener {
            // obtener datos
            val titulo = binding.etTitulo.text.toString()
            val descripcion = binding.etDescripcion.text.toString()
           if (!titulo.isEmpty() && !descripcion.isEmpty()){
               guardarNota(titulo, descripcion)
           } else{
               Toast.makeText(applicationContext, "No deje campos vacios", Toast.LENGTH_SHORT).show()
           }
        }
    }
    private fun guardarNota(titulo : String, descripcion : String){
        val nota = Nota(0, titulo, descripcion)
        db.insertNota(nota)
        startActivity(Intent(applicationContext, MainActivity::class.java))
        finishAffinity()
        Toast.makeText(applicationContext, "Se ha agregado la nota", Toast.LENGTH_SHORT).show()
    }
}