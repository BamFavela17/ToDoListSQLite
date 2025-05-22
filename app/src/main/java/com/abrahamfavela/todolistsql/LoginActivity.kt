package com.abrahamfavela.todolistsql

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
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
        val recuperarCuentaButton = findViewById<TextView>(R.id.tv_recuperar_cuenta)

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
            if (dbHelper.validateUser(email, password)) {
                Toast.makeText(this, "Login exitoso", Toast.LENGTH_SHORT).show()

                // Guardar el estado de inicio de sesión
                val sharedPreferences = getSharedPreferences("MyAppPrefs", MODE_PRIVATE)
                val editor = sharedPreferences.edit()
                editor.putBoolean("isLoggedIn", true)
                editor.putString("userEmail", email) // Guardar el email del usuario actual
                editor.apply()

                // Redirigir a MainActivity
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish() // Cerrar LoginActivity
            } else {
                Toast.makeText(this, "Credenciales incorrectas", Toast.LENGTH_SHORT).show()
            }
        }

        // Configurar el botón de registro para navegar a la actividad de registro
        registroButton.setOnClickListener {
            val intent = Intent(this, RegistroActivity::class.java)
            startActivity(intent)
        }

        // Configurar el botón de recuperar cuenta
        recuperarCuentaButton.setOnClickListener {
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Recuperar cuenta")
            builder.setMessage("Por favor, ingresa tu correo electrónico para recuperar tu cuenta")

            // Configurar el input para el email
            val input = EditText(this)
            input.hint = "Correo electrónico"
            builder.setView(input)

            // Configurar los botones
            builder.setPositiveButton("Enviar") { dialog, which ->
                val email = input.text.toString().trim()
                if (email.isEmpty()) {
                    Toast.makeText(this, "Por favor ingrese un correo electrónico", Toast.LENGTH_SHORT).show()
                } else {
                    // Aquí implementarías la lógica de recuperación de contraseña
                    // Por ahora, solo mostraremos un mensaje
                    Toast.makeText(this, "Se ha enviado un correo de recuperación a $email", Toast.LENGTH_LONG).show()
                }
            }
            builder.setNegativeButton("Cancelar") { dialog, which -> dialog.dismiss() }
            builder.show()
        }
    }
}
