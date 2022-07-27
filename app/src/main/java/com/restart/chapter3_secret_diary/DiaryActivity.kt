package com.restart.chapter3_secret_diary

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.edit
import androidx.core.widget.addTextChangedListener

class DiaryActivity: AppCompatActivity(){

    private val handler = Handler(Looper.getMainLooper()) //메인 쓰레드(ui쓰레드)와 생성한 쓰레드를 연결시켜주는 핸들러생성.

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_diary)

        //onCreate함수 밖에서 실행될 일이 없기 때문에 diaryEditText를 onCreate함수에서 지역변수로 지정해도 됨.
        val diaryEditText = findViewById<EditText>(R.id.diaryEditText)

        //SharedPreferences에 diary라는 이름으로 저장
        val detailPreferences = getSharedPreferences("diary", Context.MODE_PRIVATE)

        //에디트텍스트의 텍스트를 세팅 => sharedPreferences안에 detail로 저장된 키의 벨류값으로 세팅, 저장된 값이 없으면 ""으로 표현.
        diaryEditText.setText(detailPreferences.getString("detail",""))

        //쓰레드에서 할일 쓰레드에 넣는 Runnable 지정.
        val runnable = Runnable {
            getSharedPreferences("diary",Context.MODE_PRIVATE).edit {
                putString("detail", diaryEditText.text.toString())
            }

            Log.d("DiaryActivity","저장: ${diaryEditText.text.toString()}")
          }

        diaryEditText.addTextChangedListener {

            Log.d("DiaryActivity","텍스트체인지 :: $it")
            // 한글짜마다 저장하면 비효율적이므로 글 작성중 잠시멈추면(0.5) SharedPreferences에 저장.
            handler.removeCallbacks(runnable) // 0.5초 이전에 runnable이 있다면 삭제.
            handler.postDelayed(runnable, 500) //0.5초가 후에 runnable 실행.

        }

    }
}