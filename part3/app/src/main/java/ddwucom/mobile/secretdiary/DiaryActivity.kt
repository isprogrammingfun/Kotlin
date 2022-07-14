package ddwucom.mobile.secretdiary

import android.os.Bundle
import android.os.Handler
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.edit
import android.os.Looper
import androidx.core.widget.addTextChangedListener

class DiaryActivity: AppCompatActivity() {

    private val handler = Handler(Looper.getMainLooper()) //MainLooper를 넣어주면 메인쓰레드에 연결된 핸들러 만들어짐

    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_diary)

        val diaryEditText = findViewById<EditText>(R.id.diaryEditText)
        val detailPreferences = getSharedPreferences("diary", MODE_PRIVATE)

        diaryEditText.setText(detailPreferences.getString("detail", ""))

        //쓰레드 일정 시간동안 입력이 없을시, 입력되어있는 텍스트가 저장되는
        val runnable = Runnable {
            getSharedPreferences("diary", MODE_PRIVATE).edit {
                putString("detail", diaryEditText.text.toString())
            }
        }

        //쓰레드를 열었을때 ui에서 처리되는 작업을 ui 쓰레드(메인쓰레드), 새로운 쓰레드를 열었을때 이 쓰레드를 관리를 할 때 ui 쓰레드와
       // 그냥 생성한 쓰레드 연결 해줘야할 필요가 있다 메인 쓰레드가 아닌 곳에서는 ui 체인지를 하는 동작을 수행할 수 없기 때문
       // 이렇게 메인 쓰레드와 연결을 해주는 기능을 핸들러가 해준다
        diaryEditText.addTextChangedListener {
            //500ms(0.5초) 이전에 아직 실행되지 않고 있는 runnable이 있다면 지워주는 기능
            //제일 처음에 addTextChangedListener 로 들어왔을때, 이전에 있는 runnable이 있다면 지우고,
            handler.removeCallbacks(runnable)
            //몇초 이후에 쓰레드를 실행 시켜주는 기능 postDelayed에 runnable이란 객체가 들어갔기 때문에
            //handler에 runnable 설정 되어있다.
            //0.5초 이후에 runnable을 실행시키는 메세지를 핸들러에 전달
            handler.postDelayed(runnable, 500)

        }
    }
}