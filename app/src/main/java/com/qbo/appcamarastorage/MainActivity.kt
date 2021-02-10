package com.qbo.appcamarastorage

import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private val CAMARA_REQUEST = 1888;
    var rutalActual = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        btncamara.setOnClickListener {
            if(permisoEscrituraAlmacenamiento()){
                Snackbar.make(it, "SI tiene permiso", Snackbar.LENGTH_LONG).show()
            }else{
                solicitarPermiso()
                //Snackbar.make(it, "No tiene permiso", Snackbar.LENGTH_LONG).show()
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if(requestCode == 0){
            if(grantResults.isNotEmpty() && grantResults[0] ==
                    PackageManager.PERMISSION_GRANTED){
                Toast.makeText(applicationContext, "Llamar a la cámara",
                    Toast.LENGTH_LONG).show()
            }else{
                Toast.makeText(applicationContext, "Permiso denegado",
                    Toast.LENGTH_LONG).show()
            }
        }
    }
    private fun solicitarPermiso(){
        ActivityCompat.requestPermissions(
            this,
            arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE),
            0
        )
    }
    private fun permisoEscrituraAlmacenamiento(): Boolean{
        val result = ContextCompat.checkSelfPermission(
            applicationContext, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
        var permiso = false
        if (result == PackageManager.PERMISSION_GRANTED) permiso = true
        return permiso
    }
}