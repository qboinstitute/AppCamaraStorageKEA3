package com.qbo.appcamarastorage

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_main.*
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {

    private val CAMARA_REQUEST = 1888;
    var rutalActual = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        btncamara.setOnClickListener {
            if(permisoEscrituraAlmacenamiento()){
                intencionTomarFoto()
                //Snackbar.make(it, "SI tiene permiso", Snackbar.LENGTH_LONG).show()
            }else{
                solicitarPermiso()
                //Snackbar.make(it, "No tiene permiso", Snackbar.LENGTH_LONG).show()
            }
        }
    }

    private fun grabarFoto(){
        val imagenintent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        val nuevoarchivo = File(rutalActual)
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
            val contenidouri = FileProvider.getUriForFile(
                applicationContext,
                "com.qbo.appcamarastorage.provider",
                nuevoarchivo
            )
            imagenintent.data = contenidouri
        }else{
            val contenidouri = Uri.fromFile(nuevoarchivo)
            imagenintent.data = contenidouri
        }
        this?.sendBroadcast(imagenintent)
    }
    private fun mostrarFoto(){

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if(requestCode == CAMARA_REQUEST){
            if(resultCode == Activity.RESULT_OK){
                grabarFoto()
                mostrarFoto()
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    @Throws(IOException::class)
    private fun crearArchivoImagen(): File?{
        val fechahora = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val nombrearchivo = "JPEG_${fechahora}_"
        val direccionstorage : File =
            this?.getExternalFilesDir(Environment.DIRECTORY_PICTURES)!!
        val imagen: File = File.createTempFile(nombrearchivo,".jpg",
            direccionstorage)
        rutalActual = imagen.absolutePath
        return imagen
    }
    @Throws(IOException::class)
    private fun intencionTomarFoto(){
        val tomarfotointent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if(tomarfotointent.resolveActivity(this?.packageManager!!) != null){
            val archivofoto = crearArchivoImagen()
            if(archivofoto != null){
                val urifoto : Uri = FileProvider.getUriForFile(
                    applicationContext,
                    "com.qbo.appcamarastorage.provider",
                    archivofoto
                )
                tomarfotointent.putExtra(MediaStore.EXTRA_OUTPUT, urifoto)
                startActivityForResult(tomarfotointent, CAMARA_REQUEST)
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
                intencionTomarFoto()
                //Toast.makeText(applicationContext, "Llamar a la cámara", Toast.LENGTH_LONG).show()
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