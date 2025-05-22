package com.abrahamfavela.todolistsql

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.abrahamfavela.todolistsql.databinding.ActivityActualizarNotaBinding

class RegistroActivity : AppCompatActivity() {

    private lateinit var binding: ActivityActualizarNotaBinding

    private lateinit var dbHelper: NotasDBHelper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registro)

        dbHelper = NotasDBHelper(this)

        val nombresEditText = findViewById<EditText>(R.id.et_Nombres)
        val emailEditText = findViewById<EditText>(R.id.et_Email)
        val passwordEditText = findViewById<EditText>(R.id.et_Password)
        val repeatPasswordEditText = findViewById<EditText>(R.id.et_R_Password)
        val registerButton = findViewById<Button>(R.id.btn_Registrar)

        registerButton.setOnClickListener {
            val nombres = nombresEditText.text.toString()
            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()
            val repeatPassword = repeatPasswordEditText.text.toString()
            binding
            if (password != repeatPassword) {
                Toast.makeText(this, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (dbHelper.addUser (email, password, nombres)) {
                Toast.makeText(this, "Registro exitoso", Toast.LENGTH_SHORT).show()
                // Redirigir a la actividad de login o a otra parte de la app
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish() // Cerrar RegistroActivity
            } else {
                Toast.makeText(this, "El usuario ya existe", Toast.LENGTH_SHORT).show()
            }
        }
    }
}