package com.restart.chapter3_secret_diary

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.NumberPicker
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.AppCompatButton
import androidx.core.content.edit

class MainActivity : AppCompatActivity() {

    private val numberPicker1: NumberPicker by lazy {
        findViewById<NumberPicker>(R.id.numberPicker1)
            .apply {
                minValue = 0
                maxValue = 9
            }
    }

    private val numberPicker2: NumberPicker by lazy {
        findViewById<NumberPicker>(R.id.numberPicker2)
            .apply {
                minValue = 0
                maxValue = 9
            }
    }

    private val numberPicker3: NumberPicker by lazy {
        findViewById<NumberPicker>(R.id.numberPicker3)
            .apply {
                minValue = 0
                maxValue = 9
            }
    }

    private val openButton: AppCompatButton by lazy {
        findViewById(R.id.openButton)
    }

    private val changePasswordButton: AppCompatButton by lazy {
        findViewById(R.id.changePasswordButton)
    }

    private var changePasswordMode = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //넘버피커를 직접적으로 사용할 경우가 없기 때문에 미리 호출하여 by laze내의 코드를 활성화시켜줌.
        numberPicker1
        numberPicker2
        numberPicker3

        openButton.setOnClickListener {

            if (changePasswordMode) {
                Toast.makeText(this, "비밀번호 변경 중입니다.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            //SharedPreferences에 비밀번호 저장.
            val passwordPreferences = getSharedPreferences("password", Context.MODE_PRIVATE)

            // 사용자가 입력한 넘버피커값 변수에 저장
            val passwordFromUser =
                "${numberPicker1.value}${numberPicker2.value}${numberPicker3.value}"

            //사용자 입력값과 sharedPreferences에 저장된 값을 비교
            if (passwordPreferences.getString("password", "000").equals(passwordFromUser)) {
                //패스워드가 같은 경우

                startActivity(Intent(this, DiaryActivity::class.java))
            } else {
                //패스워드가 다른 경우 얼럿창 띄우기
                showErrorAlertDialog()
            }

        }

        changePasswordButton.setOnClickListener {
          //번호를 저장하는 기능
            val passwordPreferences = getSharedPreferences("password", Context.MODE_PRIVATE)
            // 사용자가 입력한 넘버피커값 변수에 저장
            val passwordFromUser ="${numberPicker1.value}${numberPicker2.value}${numberPicker3.value}"

            if (changePasswordMode) {

                passwordPreferences.edit(true) {
                    putString("password", passwordFromUser)
                }

                changePasswordMode = false
                changePasswordButton.setBackgroundColor(Color.BLACK)

            } else {
                //changePasswordMode가 활성화 :: 비밀번호가 맞는지도 체크

                //사용자 입력값과 sharedPreferences에 저장된 값을 비교
                if (passwordPreferences.getString("password", "000").equals(passwordFromUser)) {

                    changePasswordMode = true
                    Toast.makeText(this, "변경할 패스워드를 입력해주세요.", Toast.LENGTH_SHORT).show()

                    changePasswordButton.setBackgroundColor(Color.RED)
                } else {

                    showErrorAlertDialog()

                }
            }

        }
    }
    private fun showErrorAlertDialog(){
        AlertDialog.Builder(this)
            .setTitle("실패!!")
            .setMessage("비밀번호가 잘못되었습니다.")
            .setPositiveButton("확인", DialogInterface.OnClickListener { _, _ ->

            })
            .create()
            .show()
    }
}