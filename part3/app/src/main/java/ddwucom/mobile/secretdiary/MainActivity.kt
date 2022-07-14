package ddwucom.mobile.secretdiary

import android.content.Context
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings.Global.putString
import android.widget.NumberPicker
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.AppCompatButton
import androidx.core.content.edit

class MainActivity : AppCompatActivity() {

    private val NumberPicker1 : NumberPicker by lazy{
        findViewById<NumberPicker>(R.id.NumberPicker1)
            .apply{
                minValue = 0
                maxValue = 9
            }
    }

    private val NumberPicker2: NumberPicker by lazy{
        findViewById<NumberPicker>(R.id.NumberPicker2)
            .apply{
                minValue = 0
                maxValue = 9
            }
    }

    private val NumberPicker3 : NumberPicker by lazy{
        findViewById<NumberPicker>(R.id.NumberPicker3)
            .apply{
                minValue = 0
                maxValue = 9
            }
    }

    private val openButton: AppCompatButton by lazy{
        findViewById<AppCompatButton>(R.id.openButton)
    }

    private val changePasswordButton: AppCompatButton by lazy{
        findViewById<AppCompatButton>(R.id.changePasswordButton)
    }

    private var changePasswordMode = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        NumberPicker1
        NumberPicker2
        NumberPicker3

        openButton.setOnClickListener{

            if(changePasswordMode){
                Toast.makeText(this, "비밀번호를 변경중입니다",Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val passwordPreferences = getSharedPreferences("password", Context.MODE_PRIVATE)
            val passwordFromUser = "${NumberPicker1.value}${NumberPicker2.value}${NumberPicker3.value}"
            if(passwordPreferences.getString("password", "000").equals(passwordFromUser)){
                //성공
                startActivity(Intent(this, DiaryActivity::class.java))
            }else{
                //실패
                showErrorAlertDialog()
            }

        }
        changePasswordButton.setOnClickListener {
            val passwordPreferences = getSharedPreferences("password", Context.MODE_PRIVATE)
            val passwordFromUser = "${NumberPicker1.value}${NumberPicker2.value}${NumberPicker3.value}"
            if(changePasswordMode){
                //번호 저장
                val passwordPreferences = getSharedPreferences("password", Context.MODE_PRIVATE)

                passwordPreferences.edit(true){
                    putString("password", passwordFromUser)
                }

                changePasswordMode = false
                changePasswordButton.setBackgroundColor(Color.BLACK)
            }
            else{
                //changePasswordMode가 활성화 :: 비밀번호가 맞는지 체크
                if(passwordPreferences.getString("password", "000").equals(passwordFromUser)){
                    //성공
                    changePasswordMode = true
                    Toast.makeText(this, "변경할 패스워드를 입력해주세요", Toast.LENGTH_SHORT).show()
                    changePasswordButton.setBackgroundColor(Color.RED)
                }else{
                    //실패
                    showErrorAlertDialog()
                }

            }
        }

    }
    private fun showErrorAlertDialog(){
        AlertDialog.Builder(this)
            .setTitle("실패")
            .setMessage("비밀번호가 잘못되었습니다")
            .setPositiveButton("확인"){_, _ ->}
            .create()
            .show()
    }
}