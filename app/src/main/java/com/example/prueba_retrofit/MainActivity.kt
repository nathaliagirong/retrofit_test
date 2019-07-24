package com.example.prueba_retrofit

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Environment
import com.example.e4app.models.Datos
import com.example.e4app.services.DatosService
import com.example.e4app.services.ServiceBuilder
import com.example.prueba_retrofit.models.Data
import com.example.prueba_retrofit.models.Respuesta
import com.jakewharton.threetenabp.AndroidThreeTen
import org.threeten.bp.LocalDateTime
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.toast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.FileOutputStream


class MainActivity : AppCompatActivity() {

    private var secondsRemaining = 10
    val timer = Counter(secondsRemaining.toLong()*1000, 1000)

    internal var test = ""

    //ESTA FECHA TENGO QUE VER DONDE UBICARLA BIEN

    var fecha = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        AndroidThreeTen.init(this);

        fecha = LocalDateTime.now().toString()


        setContentView(R.layout.activity_main)

        btn_getData.setOnClickListener {
            loadDatos()
        }

        btn_postData.setOnClickListener {
            postData()
        }

        btnStart.setOnClickListener {
            timer.start()
        }
    }

    private fun loadDatos(){

        val datosService = ServiceBuilder.buildService(DatosService::class.java)

        //val fecha_Actt = fecha.toString()
        val fecha_Act = "29-07-2019"
        val requestCall = datosService.getDatos(fecha_Act)

        requestCall.enqueue(object : Callback<Datos> {
            override fun onResponse(call: Call<Datos>, response: Response<Datos>) {
                if (response.isSuccessful) {
                    println("BUSCANDO: " + fecha_Act)
                    println(response.body()!!.response)
                }
            }

            override fun onFailure(call: Call<Datos>, t: Throwable) {
                println(t)
            }
        })
    }

    private fun postData(){


        val newRespuesta = Respuesta()
        val newData = Data()
        newData.name = edtNombre.text.toString()
        newData.comment = edtComent.text.toString()
        newData.date = fecha.toString()



        val DatosService = ServiceBuilder.buildService(DatosService::class.java)
        val requestCall = DatosService.uploadData(newData)

        requestCall.enqueue(object : Callback<Datos> {

            override fun onResponse(call: Call<Datos>, response: Response<Datos>) {

                if(response.isSuccessful){

                    println("SUCCESSFULL")

                    println(response.body()!!.response)

                }else{
                    println("FAIL ON RESPONSE")

                }
            }

            override fun onFailure(call: Call<Datos>, t: Throwable) {

            }


        })
    }

    inner class Counter(millisInFuture: Long, countDownTimer: Long) : CountDownTimer(millisInFuture, countDownTimer){
        override fun onFinish() {
            toast("Tiempo finalizado")
            println("Tiempo finalizado")
            val name = "ARCHIVO.csv"
            val textFile = File(Environment.getExternalStorageDirectory(),name)
            val fos = FileOutputStream(textFile)

            fos.write(test.toByteArray())
            fos.close()

            toast("Archivo guardado")

        }

        override fun onTick(millisUntilFinished: Long) {
            secondsRemaining = millisUntilFinished.toInt() / 1000
            println("Timer: " + millisUntilFinished/ 1000)

            test += secondsRemaining.toString()
            test += "\n"

        }
    }


}
