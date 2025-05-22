package com.abrahamfavela.todolistsql

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class NotasDBHelper (context: Context) : SQLiteOpenHelper (
    context, DATABASE_NAME, null, DATABASE_VERSION
){
    // tabla de las nota
    override fun onCreate(db: SQLiteDatabase?) {
        val createTableQuery =
            "CREATE TABLE $TABLE_NAME ($COLUMN_ID INTEGER PRIMARY KEY, $COLUMN_TITLE TEXT, $COLUMN_DESCRIPTION TEXT)"
        db?.execSQL(createTableQuery)
        // tabla del login
        val createTableU = ("CREATE TABLE " + TABLE_USERS + "("
                + COLUMN_EMAIL + " TEXT PRIMARY KEY,"
                + COLUMN_PASSWORD + " TEXT,"
                + COLUMN_NOMBRES + " TEXT" + ")")
        db?.execSQL(createTableU)
    }

    override fun onUpgrade(
        db: SQLiteDatabase?,
        oldVersion: Int,
        newVersion: Int
    ) {
      val dropTableQuery = "DROP TABLE IF EXISTS $TABLE_NAME"
        db?.execSQL(dropTableQuery)
        onCreate(db)
    }

    companion object{
        private const val DATABASE_NAME = "notas.db"
        private const val DATABASE_VERSION = 1
        private const val TABLE_NAME ="notas"
        private const val COLUMN_ID = "id"
        private const val COLUMN_TITLE = "titulo"
        private const val COLUMN_DESCRIPTION = "descripcion"
        const val TABLE_USERS = "users"
        const val COLUMN_EMAIL = "email"
        const val COLUMN_PASSWORD = "password"
        private const val COLUMN_NOMBRES = "nombres"
    }    fun validateUser(email: String, password: String): Boolean {
        val db = this.readableDatabase
        val cursor = db.query(TABLE_USERS, null, "$COLUMN_EMAIL=? AND $COLUMN_PASSWORD=?", arrayOf(email, password), null, null, null)
        val isValid = cursor.count > 0
        cursor.close()
        db.close()
        return isValid
    }
    
    // funciones del usuario
    fun addUser(email: String, password: String, nombres: String): Boolean {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(COLUMN_EMAIL, email)
        contentValues.put(COLUMN_PASSWORD, password)
        contentValues.put(COLUMN_NOMBRES, nombres) // Usar la constante definida
        // Verificar si el usuario ya existe
        val cursor = db.query(TABLE_USERS, null, "$COLUMN_EMAIL=?", arrayOf(email), null, null, null)
        if (cursor.count > 0) {
            cursor.close()
            db.close()
            return false // Usuario ya existe
        }
        db.insert(TABLE_USERS, null, contentValues)
        cursor.close()
        db.close()
        return true // Registro exitoso
    }
     // funciones de notas
    fun insertNota(nota : Nota){
        val db = writableDatabase

        val values = ContentValues().apply {
            put(COLUMN_TITLE, nota.titulo)
            put(COLUMN_DESCRIPTION, nota.descripcion)
        }
        db.insert(TABLE_NAME, null, values)
        db.close()
    }

    fun getAllNotas() : List<Nota>{
        val listaNotas = mutableListOf<Nota>()
        val db = readableDatabase
        val query = "SELECT * FROM $TABLE_NAME"
        val cursor = db.rawQuery(query, null)

        while (cursor.moveToNext()){
            val id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID))
            val titulo = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TITLE))
            val descripcion = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DESCRIPTION))

            val nota = Nota (id, titulo, descripcion)
            listaNotas.add(nota)
        }
        cursor.close()
        db.close()
        return listaNotas
    }

    fun getIdNota(idNota : Int ) : Nota {
        val db = readableDatabase
        val query = "SELECT * FROM $TABLE_NAME WHERE $COLUMN_ID = $idNota"
        val cursor = db.rawQuery(query, null)
        cursor.moveToFirst()

        val id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID))
        val titulo = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TITLE))
        val descripcion = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DESCRIPTION))

        cursor.close()
        db.close()

        return Nota(id, titulo, descripcion)
    }

    fun updateNota (nota: Nota){
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_TITLE, nota.titulo)
            put(COLUMN_DESCRIPTION, nota.descripcion)
        }

        val whereClause = "$COLUMN_ID = ?"
        val whereArgs = arrayOf(nota.id.toString())
        db.update(TABLE_NAME, values, whereClause, whereArgs)
        db.close()
    }

    fun deleteNota (idNota : Int){
        val db = writableDatabase
        val whereClaus = "$COLUMN_ID = ?"
        val whereArgs = arrayOf(idNota.toString())
        db.delete(TABLE_NAME, whereClaus,whereArgs)
        db.close()
    }
}