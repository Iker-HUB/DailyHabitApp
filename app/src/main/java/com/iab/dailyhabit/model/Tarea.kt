package com.iab.dailyhabit.model

data class Tarea(
    val titulo: String,
    val numeroVeces: Int,
    val progresoActual: Int = 0,
    val hora: String,
    val idUsuario: String,
) {
    fun toMap(): Map<String, Any> {
        return mapOf(
            "titulo" to titulo,
            "numeroVeces" to numeroVeces,
            "progresoActual" to progresoActual,
            "hora" to hora,
            "idUsuario" to idUsuario,
        )
    }
}