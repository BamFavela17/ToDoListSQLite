package com.abrahamfavela.todolistsql

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity

class LoginActivity : AppCompatActivity() {
    private lateinit var dbHelper: NotasDBHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        dbHelper = NotasDBHelper(this)

        val emailEditText = findViewById<EditText>(R.id.et_Email)
        val passwordEditText = findViewById<EditText>(R.id.et_Password)
        val loginButton = findViewById<Button>(R.id.btn_Ingresar)
        val registroButton = findViewById<TextView>(R.id.tv_registrarme)

        loginButton.setOnClickListener {
            val email = emailEditText.text.toString().trim()
            val password = passwordEditText.text.toString().trim()

            // Validar que los campos no estén vacíos
            if (email.isEmpty()) {
                Toast.makeText(this, "Por favor, ingresa tu correo electrónico", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (password.isEmpty()) {
                Toast.makeText(this, "Por favor, ingresa tu contraseña", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Validar usuario
            if (dbHelper.validateUser (email, password)) {
                Toast.makeText(this, "Login exitoso", Toast.LENGTH_SHORT).show()

                // Guardar el estado de inicio de sesión
                val sharedPreferences = getSharedPreferences("MyAppPrefs", MODE_PRIVATE)
                val editor = sharedPreferences.edit()
                editor.putBoolean("isLoggedIn", true)
                editor.apply()

                // Redirigir a MainActivity
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish() // Cerrar LoginActivity
            } else {
                Toast.makeText(this, "Credenciales incorrectas", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
