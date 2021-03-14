
/*
해당 문서 참고 바랍니다.
https://developer.android.com/reference/android/speech/tts/TextToSpeech

참고 유튜브 2개
https://www.youtube.com/watch?v=mGHjqIjV-rw 코틀린
https://www.youtube.com/watch?v=DoYnz0GYN1w&t=146s 자바
둘이 섞어서 작성했어요.
 */


/*
코틀린 언어 특징
1.코틀린은 자료형이 var / val 로만 존재
  var(variable): 추후 변환 가능, val: 고정되는 값

2. non-nullable 언어이다. 즉 null을 가질수 없다.
    그러면 아무 값이 들어가지 않아야 되는 경우?

    <java>
    int a;  //a는 초기화 된 값이 아니므로 null 이 들어감!
    --->하지만 이것은 코틀린에서 불가능함

    하지만 코틀린에서 null값이 들어가야하는 경우도 있자나???
    그래서 코틀린은 null이 들어갈'수'도 있는 값에 ? 라는 부호를 넣어준다.
    그래서 위에 int a; 는
    a:int?   로 적어줄수 있다.


 3. 끝에 세미콜론은 안붙인다.

 4. lateinit 은 초기화를 해줄 때 쓰는 키워드!
 mEditText,mSeekBarPitch..등 변수를 앞쪽에 선언했는데 초기화되었다(즉 null값)
 근데 null 선언 --> ? 이거를 안써줌
 그런 경우 쓸수 있는 lateinit 이라는 초기화 값이 있다.
 이건 var 이랑만 쓸 수 있음~

 당연한게 val는 고정값이니깐 더 이상 바뀔수 없는데
 초기화 전에 변수는 나중에 바꾸려고 미리 선언하는것니깐 나중에 바뀔 가능성이 무한한 이야이고
 그럼 당근빠따로 var이랑만 쓸수 있음


+막 배운 허접한 정리였습니다....다들 인터넷보고 개인 공부 해주세요~
참고링크는 없구,,그냥 구글에 코틀린 기본 문법 암거나 쳐보고 맘에 드는거 보면 될듯??

*/


package com.example.stt

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.SeekBar
import android.widget.Toast
import java.util.*

class TextToSpeech : AppCompatActivity() {

   // xml 에서 뷰들을 초기화 해줄 변수 설정
    lateinit var mTTS: TextToSpeech
    lateinit var mEditText:EditText
    lateinit var mSeekBarPitch:SeekBar
    lateinit var mSeekBarSpeed:SeekBar
    lateinit var mSpeakBtn:Button
    lateinit var mgotoSTT:Button


    //해당 엑티비티가 생성될때 가장 먼저 실행되는 함수 onCreate
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.texttospeech)

        //변수에 findviewbyid로 뷰의 이름으로 참조값을 대입해줌
        mSpeakBtn = findViewById(R.id.change_btn) as Button
        mEditText = findViewById(R.id.input_et) as EditText
        mSeekBarPitch = findViewById(R.id.pitch_progressbar) as SeekBar
        mSeekBarSpeed = findViewById(R.id.speed_progressbar) as SeekBar
        mgotoSTT = findViewById(R.id.gotoSTT) as Button

        //mTTS 라는 TextToSpeech 엔진 객체를 설정해줌
        mTTS = TextToSpeech(this, TextToSpeech.OnInitListener { status->
            if(status == TextToSpeech.SUCCESS){ //TTS 엔진 초기화를 완료한다면, 언어설정
                var result = mTTS.setLanguage(Locale.KOREAN)//한국어

                if(result == TextToSpeech.LANG_MISSING_DATA || result ==TextToSpeech.LANG_NOT_SUPPORTED){ //언어 데이터가 없거나, 언어가 지원되지 않은 경우
                    Log.e("TTS","언어가 지원되지 않습니다.") //로그찍어줌,로그는 개발하다가 이상이 있는지 알고 싶을 떄 곤솔창에서 확인을 위한 것, 앱의 실행과관련 없음

                }else{//초기화, 언어설정 all good 인 경우
                    mSpeakBtn.setEnabled(true) //버튼을 활성화 해줌
                }

            }else{//TTS 엔진 초기화를 실패한 경우
                Log.e("TTS","TTS 엔진 초기화 실패") //로그찍어줌
            }
        } )


        //입력한 텍스트를 Speech 로 바꿔주는 함수
        fun speak() {
            val changetext = mEditText.text.toString()//입력한 문장의 text를 참조(=가져온다) ,정확히는 주소를 point

            if (changetext == "") { //텍스트 입력된것이 없을때
                Toast.makeText(this, "텍스트를 입력해주세요", Toast.LENGTH_SHORT).show() //해당 팝업(=토스트) 출력
            }else{//텍스트가 있는 경우
                var pitch  = mSeekBarPitch.progress/50.0; //중간이 50이었으므로 초기값이 1 (범위는 0(0/50)~2(100/50))
                if(pitch <0.1) { //seekbarpitch progress 0 으로 설정할경우 소리 않나오므로 최소값은 0.1로 설정
                    pitch = 0.1
                }
                var speed  = mSeekBarSpeed.progress/50.0; //위와 동일
                if(speed <0.1) {
                    speed = 0.1
                }

                mTTS.setPitch(pitch.toFloat()) //TTS 엔진에 음량, 속도 설정
                mTTS.setSpeechRate(speed.toFloat())
                mTTS.speak(changetext, TextToSpeech.QUEUE_FLUSH, null)//출력할 문장 넘겨줌(changetext),QUEUE_FLUSH: 진행중인 음성 출력을 끊고 이번 TTS 음성 출력함
            }
        }


        //리스너 생성
        val mClickListener : View.OnClickListener = object :View.OnClickListener{
            override fun onClick(v: View?) {
                speak(); //speak 함수 호출
            }
        }
        //클릭리스너로 리스너 달아주기
        mSpeakBtn.setOnClickListener(mClickListener)//리스너 객체 mClickListener을 달아준다

        //앱이 종료될때 TTS 엔진을 종료해줘야됨
        fun onDestroy() {
            if(mTTS != null){
                mTTS.stop()//mTTS객체 실행중지
                mTTS.shutdown()//mTTS객체 종료
            }
            super.onDestroy()
        }

        //참고 https://blog.yena.io/studynote/2017/11/27/Android-Kotlin-Activity.html
        //intent로  STT로 화면 전환
        mgotoSTT.setOnClickListener(View.OnClickListener {
            val intent = Intent(this,SpeechToText::class.java)
            startActivity(intent)
        })




    }

}