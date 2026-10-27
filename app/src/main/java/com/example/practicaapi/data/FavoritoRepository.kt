package com.example.practicaapi.data

import android.util.Log
import com.example.practicaapi.data.local.MealFavoritoDao
import com.example.practicaapi.data.local.MealFavoritoEntity
import com.example.practicaapi.data.remote.RetrofitClient
import com.example.practicaapi.data.remote.SQLiteCloudApiService
import com.example.practicaapi.data.remote.SqlQueryRequest
import kotlinx.coroutines.flow.Flow
import retrofit2.HttpException

class FavoritoRepository(
    private val dao: MealFavoritoDao,
    private val api: SQLiteCloudApiService = RetrofitClient.sqliteCloudApi
) {
    private val apiKey = RetrofitClient.SQLITE_CLOUD_API_KEY
    private val primaryDbName = RetrofitClient.SQLITE_CLOUD_DB_NAME // "meal_db.sqlite"
    private val alternateDbName = primaryDbName.removeSuffix(".sqlite") // "meal_db"

    fun observarFavoritos(): Flow<List<MealFavoritoEntity>> {
        return dao.obtenerFavoritos()
    }

    fun observarIdsFavoritos(): Flow<List<String>> {
        return dao.observarIdsFavoritos()
    }

    private suspend fun ejecutarComandoConFallback(sql: String) {
        for (db in listOf(primaryDbName, alternateDbName)) {
            // Intenta v2/weblink/sql
            try {
                Log.d("SQLiteCloud", "Ejecutando SQL v2 (weblink) en '$db': $sql")
                val response = api.ejecutarComandoV2(apiKey, SqlQueryRequest(db, sql))
                Log.d("SQLiteCloud", "Respuesta exitosa v2/weblink: $response")
                if (response.error == null) return
            } catch (e: HttpException) {
                val errorBody = e.response()?.errorBody()?.string()
                Log.e("SQLiteCloud", "Fallo v2/weblink/sql con db '$db' - HTTP ${e.code()}: $errorBody")
            } catch (e: Exception) {
                Log.e("SQLiteCloud", "Fallo v2/weblink/sql con db '$db': ${e.message}")
            }

            // Fallback a v2/sql
            try {
                Log.d("SQLiteCloud", "Ejecutando SQL v2 (sql) en '$db': $sql")
                val response = api.ejecutarComandoSql(apiKey, SqlQueryRequest(db, sql))
                Log.d("SQLiteCloud", "Respuesta exitosa v2/sql: $response")
                if (response.error == null) return
            } catch (e: HttpException) {
                val errorBody = e.response()?.errorBody()?.string()
                Log.e("SQLiteCloud", "Fallo v2/sql con db '$db' - HTTP ${e.code()}: $errorBody")
            } catch (e: Exception) {
                Log.e("SQLiteCloud", "Fallo v2/sql con db '$db': ${e.message}")
            }
        }
        Log.e("SQLiteCloud", "No se pudo ejecutar el comando en SQLiteCloud tras probar todas las rutas y nombres de BD.")
    }

    suspend fun agregar(favorito: MealFavoritoEntity) {
        // 1. Guardar en Room local primero
        dao.insertar(favorito)

        // 2. Sincronizar en SQLiteCloud
        try {
            val nombreEscape = favorito.nombre.replace("'", "''")
            val categoriaEscape = favorito.categoria.replace("'", "''")
            val sql = """
                INSERT INTO favoritos (id, nombre, categoria, agregadoEn)
                VALUES ('${favorito.id}', '$nombreEscape', '$categoriaEscape', ${favorito.agregadoEn})
                ON CONFLICT(id) DO UPDATE SET
                    nombre = excluded.nombre,
                    categoria = excluded.categoria,
                    agregadoEn = excluded.agregadoEn;
            """.trimIndent()

            ejecutarComandoConFallback(sql)
        } catch (e: Exception) {
            Log.e("SQLiteCloud", "Error en agregar favorito", e)
        }
    }

    suspend fun quitar(id: String) {
        // 1. Eliminar de Room local
        dao.eliminarPorId(id)

        // 2. Sincronizar eliminación con SQLiteCloud
        try {
            val sql = "DELETE FROM favoritos WHERE id = '$id';"
            ejecutarComandoConFallback(sql)
        } catch (e: Exception) {
            Log.e("SQLiteCloud", "Error en quitar favorito", e)
        }
    }

    suspend fun sincronizarConNube() {
        try {
            // DDL para asegurar que la tabla 'favoritos' exista
            val ddlSql = """
                CREATE TABLE IF NOT EXISTS favoritos (
                    id TEXT PRIMARY KEY NOT NULL,
                    nombre TEXT NOT NULL,
                    categoria TEXT NOT NULL,
                    agregadoEn INTEGER NOT NULL
                );
            """.trimIndent()

            ejecutarComandoConFallback(ddlSql)

            // Intentar leer los favoritos
            val selectSql = "SELECT id, nombre, categoria, agregadoEn FROM favoritos;"
            for (db in listOf(primaryDbName, alternateDbName)) {
                try {
                    val respuesta = api.obtenerFavoritosV2(apiKey, SqlQueryRequest(db, selectSql))
                    Log.d("SQLiteCloud", "Favoritos obtenidos de '$db': ${respuesta.data}")
                    respuesta.data?.forEach { favoritoRemoto ->
                        dao.insertar(favoritoRemoto)
                    }
                    if (respuesta.error == null) break
                } catch (e: HttpException) {
                    val errorBody = e.response()?.errorBody()?.string()
                    Log.e("SQLiteCloud", "Error leyendo favoritos de '$db' - HTTP ${e.code()}: $errorBody")
                } catch (e: Exception) {
                    Log.e("SQLiteCloud", "Error leyendo favoritos de '$db': ${e.message}")
                }
            }
        } catch (e: Exception) {
            Log.e("SQLiteCloud", "Error al sincronizar con nube", e)
        }
    }
}
