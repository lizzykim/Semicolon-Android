package com.example.stt

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
//추후에 완성
class SpeechToText : AppCompatActivity() {

    lateinit var mgotoTTS: Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.speechtotext)

        mgotoTTS = findViewById(R.id.gotoTTS) as Button

        //참고 https://blog.yena.io/studynote/2017/11/27/Android-Kotlin-Activity.html
        //intent로  STT로 화면 전환
        mgotoTTS.setOnClickListener(View.OnClickListener {
            val intent = Intent(this,TextToSpeech::class.java)
            startActivity(intent)
        })


    }


}