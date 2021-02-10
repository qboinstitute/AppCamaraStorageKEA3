package com.qbo.appcamarastorage

import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.content.ContextCompat

class MainActivity : AppCompatActivity() {

    private val CAMARA_REQUEST = 1888;
    var rutalActual = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    private fun permisoEscrituraAlmacenamiento(): Boolean{
        val result = ContextCompat.checkSelfPermission(
            applicationContext, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
        var permiso = false
        if (result == PackageManager.PERMISSION_GRANTED) permiso = true
        return permiso
    }
}