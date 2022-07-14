package ddwucom.mobile.lotto

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.NumberPicker
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible

class MainActivity : AppCompatActivity() {
    private val clearButton: Button by lazy{   //버튼 연결
        findViewById<Button>(R.id.clearButton)
    }

    private val addButton: Button by lazy{
        findViewById<Button>(R.id.addButton)
    }

    private val runButton: Button by lazy{
        findViewById<Button>(R.id.runButton)
    }

    private val numberPicker: NumberPicker by lazy{
        findViewById<NumberPicker>(R.id.numberPicker)
    }

    private val numberTextViewList: List<TextView> by lazy {

        listOf<TextView>(
            findViewById<TextView>(R.id.textView1),
            findViewById<TextView>(R.id.textView2),
            findViewById<TextView>(R.id.textView3),
            findViewById<TextView>(R.id.textView4),
            findViewById<TextView>(R.id.textView5),
            findViewById<TextView>(R.id.textView6)
        )
    }
    private var didRun = false
    private val pickNumberSet =  hashSetOf<Int>() // 예외 처리

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        numberPicker.minValue = 1
        numberPicker.maxValue = 45

        initRunButton()
        initAddButton()
        initClearButton()
    }
    private fun initRunButton(){
        runButton.setOnClickListener{
            val list = getRandomNumber()

            didRun = true

            list.forEachIndexed{ index, number->
                val textView = numberTextViewList[index]

                textView.text = number.toString()
                textView.isVisible = true

                setNumberBackground(number, textView)
            }
        }
    }

    private fun initAddButton(){
        addButton.setOnClickListener{
            if(didRun){
                Toast.makeText(this, "초기화 후에 시도해주세요 ", Toast.LENGTH_SHORT).show()
                //이미 자동완성 시작을 눌렀을 경우에는 초기화 후에 시도하라는 에러메세지
                return@setOnClickListener //이후 과정 실행 안함
            }

            if(pickNumberSet.size >= 5){
                Toast.makeText(this, "번호는 5개까지만 선택할 수 있습니다. ", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if(pickNumberSet.contains(numberPicker.value)) //pickNumberSet에 이미 포함되어있냐
            {
                Toast.makeText(this, "이미 선택한 번호 입니다 ", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val textView = numberTextViewList[pickNumberSet.size]//지금 초기화 하는 데이터의 위치
            textView.isVisible = true // 화면에 보이게
            textView.text = numberPicker.value.toString()

            setNumberBackground(numberPicker.value, textView)

            pickNumberSet.add(numberPicker.value)
        }
    }

    private fun setNumberBackground(number:Int, textView: TextView){
        when(number){
            in 1..10 ->   textView.background = ContextCompat.getDrawable(this, R.drawable.circle_yellow)
            in 11..20 ->   textView.background = ContextCompat.getDrawable(this, R.drawable.circle_blue)
            in 21..30 ->   textView.background = ContextCompat.getDrawable(this, R.drawable.circle_red)
            in 31..40 ->   textView.background = ContextCompat.getDrawable(this, R.drawable.circle_gray)
            else -> textView.background = ContextCompat.getDrawable(this, R.drawable.circle_green)

        }
    }

    private fun initClearButton() {
        clearButton.setOnClickListener{
            pickNumberSet.clear()
            numberTextViewList.forEach{
                it.isVisible = false //숨기기
            }

            didRun = false
        }
    }
    private fun getRandomNumber(): List<Int>{
        val numberList = mutableListOf<Int>()
            .apply {
                for(i in 1..45){
                    if(pickNumberSet.contains(i)){
                        continue
                    }
                    this.add(i)
                }
            }
        numberList.shuffle()

        val newList = pickNumberSet.toList() + numberList.subList(0, 6 - pickNumberSet.size) //subList가 하는 일: List에서 subList 추출

        return newList.sorted() //오름차순으로 정렬
    }
}