package com.example.mycalculator

import android.content.ContentValues.TAG
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.example.mycalculator.databinding.ActivityMainBinding
import java.text.DecimalFormat
import kotlin.math.abs
import java.math.BigDecimal

class MainActivity : AppCompatActivity(){
    private var textViewInput: EditText? = null
    private var textViewInputTwo: EditText? = null
    private var ButtonClean: Button? = null

    // gesture detector
    private lateinit var gestureDetector: GestureDetector
    // view
    var operatorView: View? = null
    var operatorViewCheckOne: View? = null
    var operatorViewCheckTwo: View? = null
    // container
    var operator: String = ""
    var operatorBox: String =""
    var operatorSec: String = ""
    var operatorCal: String =""
    var num1: String = ""
    var num2: String = ""
    var backUpNum2: String = ""
    var clearOne: Int = 0
    // Boolean
    var haveDigit: Boolean = false
    var haveDot: Boolean = false
    var haveOperator: Boolean = false
    var firstOperator: Boolean = false
    var secondOperator: Boolean = true
    var editText: Boolean = true
    var checkFirst: Boolean = false
    // format
//    val decimalFormat = DecimalFormat("#,##0.00000000")
    val decimalFormat = DecimalFormat("#,###,###,##0.00000000")
    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        textViewInput = binding.textViewInput
        textViewInputTwo = binding.textViewInputTwo
        ButtonClean = binding.clean
        binding.textViewInput.isCursorVisible = false
        binding.textViewInput.isFocusable = false
        textViewInput?.setText("0")

        binding.textViewInput.setOnClickListener{
            Log.e("ggez","hello")
        }

        //set 1
        gestureDetector = GestureDetector(this, object : GestureDetector.SimpleOnGestureListener() {
            override fun onFling(
                e1: MotionEvent,
                e2: MotionEvent,
                velocityX: Float,
                velocityY: Float
            ): Boolean {
                if (e1 == null || e2 == null){
                    return false
                }
                val distanceX = e2.x - e1.x
                val distanceY = e2.y - e1.y
                val text = binding.textViewInput.text
                if (abs(distanceX) > abs(distanceY)) {
                    // Horizontal swipe
                    if (abs(distanceX) > SWIPE_THRESHOLD && abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) {
                        Log.e("ggez",abs(distanceX).toString())

                        if (distanceX > 0) {
                            if (text.isNotEmpty()) {
                                binding.textViewInput.setText(
                                    text.subSequence(
                                        0,
                                        text.length - 1
                                    )
                                )
                                binding.textViewInput.setSelection(text.length - 1)
                                val lastChar = text.substring(text.length - 1)
                                if (lastChar == "." && text.length > 2) {
                                    haveDot = false
                                    binding.textViewInput.setText(
                                        text.subSequence(
                                            0,
                                            text.length - 2
                                        )
                                    )
                                    binding.textViewInput.setSelection(text.length - 2)
                                } else {
                                    haveDot = false
                                    binding.textViewInput.setText(
                                        text.subSequence(
                                            0,
                                            text.length - 1
                                        )
                                    )
                                    binding.textViewInput.setSelection(text.length - 1)
                                }
                                if(text.length ==1 ){
                                    binding.textViewInput.setText("0")
                                    editText = true
                                }
                            }
                        } else {
                            binding.textViewInput.setText(
                                text.subSequence(
                                    0,
                                    text.length - 1
                                )
                            )
                            binding.textViewInput.setSelection(text.length - 1)
                            val lastChar = text.substring(text.length - 1)
                            if (lastChar == "." && text.length > 2) {
                                haveDot = false
                                binding.textViewInput.setText(
                                    text.subSequence(
                                        0,
                                        text.length - 2
                                    )
                                )
                                binding.textViewInput.setSelection(text.length - 2)
                            } else {
                                haveDot = false
                                binding.textViewInput.setText(
                                    text.subSequence(
                                        0,
                                        text.length - 1
                                    )
                                )
                                binding.textViewInput.setSelection(text.length - 1)
                            }
                            if(text.length ==1 ){
                                binding.textViewInput.setText("0")
                                editText = true
                            }
                        }
                        return true
                    }
                }
                return false
            }
        })
        val slideEvent = binding.textViewInput
        slideEvent.setOnTouchListener { y, event ->
            gestureDetector.onTouchEvent(event)
            false
        }
    }

    fun onDigit(view: View) {
        haveDigit = true
//        onDigitView = View
        if(editText || haveOperator){
            textViewInput?.setText("")
        }
        if (!haveDot && textViewInput?.getText()?.length != 9) {
            if (haveDigit ) {
                // contain input & display

                textViewInput?.append((view as Button).text)

                // setButton Clear
                ButtonClean?.setText("C")
                if(haveOperator){
                    changeButtonOperatorOne()
                    haveOperator = false
                }

                editText = false
            }
        } else if (haveDot && textViewInput?.getText()?.length != 10) {
            // contain input & display
            textViewInput?.append((view as Button).text)

            // setButton Clear
            ButtonClean?.setText("C")
            if(haveOperator){
                haveOperator = false
                changeButtonOperatorOne()
            }
            editText = false
        }
    }

    fun onEqual(view:View){
        if(num2 == ""){
            num2 = textViewInput?.getText().toString()
        }
        backUpNum2 = num2
        if(textViewInput?.getText().toString() != num1){
            num2 = textViewInput?.getText().toString()
            Log.e(TAG,textViewInput?.getText().toString())
            Log.e(TAG,num1)
        } else {
            num2 = backUpNum2
        }

        if(num1 != "" && num2 != ""){

            val changeNum1 = num1.toDouble()
            val changeNum2 = num2.toDouble()
            if(!num1.contains(".") && !num2.contains(".")){
                if(operator == "+"){
                    val value = changeNum1 + changeNum2
                    textViewInput?.setText(removeZeroAfterDot(value.toString()))
                    num1 = removeZeroAfterDot(value.toString())
                    operatorBox = operator
                }
                if(operator == "-"){
                    val value = changeNum1 - changeNum2
                    textViewInput?.setText(removeZeroAfterDot(value.toString()))
                    num1 = removeZeroAfterDot(value.toString())
                    operatorBox = operator
                }
                if(operator == "\u00d7"){
                    val value = changeNum1 * changeNum2
                    val format = decimalFormat.format(value)
                    textViewInput?.setText(format)
                    num1 = removeZeroAfterDot(value.toString())
                    operatorBox = operator
                }
                if(operator == "\u00f7"){
                    val value = changeNum1 / changeNum2
                    val format = decimalFormat.format(value)
                    textViewInput?.setText(format)
                    num1 = removeZeroAfterDot(value.toString())
                    operatorBox = operator
                }
            } else {
                if(operator == "+"){
                    val value = changeNum1 + changeNum2
                    val format = decimalFormat.format(value)
                    textViewInput?.setText(format)
                    num1 = removeZeroAfterDot(value.toString())
                    operatorBox = operator
                }
                if(operator == "-"){
                    val value = changeNum1 - changeNum2
                    val format = decimalFormat.format(value)
                    textViewInput?.setText(format)
                    num1 = removeZeroAfterDot(value.toString())
                    operatorBox = operator
                }
                if(operator == "\u00d7"){
                    val value = changeNum1 * changeNum2
                    val format = decimalFormat.format(value)
                    textViewInput?.setText(format)
                    num1 = removeZeroAfterDot(value.toString())
                    operatorBox = operator
                }
                if(operator == "\u00f7"){
                    val value = changeNum1 / changeNum2
                    val format = decimalFormat.format(value)
                    textViewInput?.setText(format)
                    num1 = removeZeroAfterDot(value.toString())
                    operatorBox = operator
                }
            }
        }
    }

        fun onOperator(view: View) {
        operatorView = view
        operator = (operatorView as Button).getText().toString()
            if(textViewInput?.getText().toString() !="0" && num1 == ""){
                num1 = textViewInput?.getText().toString()
                operatorSec = operator
            }
            if(num1 != "" && textViewInput?.getText().toString() !=num1 ){
                num2 = textViewInput?.getText().toString()
                cal()
            }
            if(num1 != "" && textViewInput?.getText().toString() ==num1 && operatorSec != operator){
                operatorSec = operator
                Log.e(TAG,operator)
                Log.e(TAG,"space")
                Log.e(TAG,operatorSec)
            }


        haveOperator = true
        editText = false
        // color button
        if(!secondOperator && firstOperator){
            operatorViewCheckTwo = operatorView
            changeButtonOperatorTwo()
            changeButtonOperatorOne()
            secondOperator = true
            firstOperator = false
        }
        if(!firstOperator && secondOperator){
            operatorViewCheckOne = operatorView
            changeButtonOperatorOneUse()
            firstOperator = true
            secondOperator = false
        }

    }
    private fun cal(){
        val changeNum1 = num1.toDouble()
        val changeNum2 = num2.toDouble()
        if(operatorSec == "+"){
            val value = changeNum1 + changeNum2
            textViewInput?.setText(removeZeroAfterDot(value.toString()))
            num1 = removeZeroAfterDot(value.toString())
            num2 = ""
        }
        if(operatorSec == "-"){
            val value = changeNum1 - changeNum2
            textViewInput?.setText(removeZeroAfterDot(value.toString()))
            num1 = removeZeroAfterDot(value.toString())
            num2=""
        }
        if(operator == "\u00d7"){
            val value = changeNum1 * changeNum2
            val number = BigDecimal(value)
            val trimmedNumber = number.stripTrailingZeros()
            textViewInput?.setText(trimmedNumber.toString())
            num1 = removeZeroAfterDot(value.toString())
            num2 = ""
        }
        if(operator == "\u00f7"){
            val value = changeNum1 / changeNum2
            val format = decimalFormat.format(value)
            textViewInput?.setText(format)
            num1 = removeZeroAfterDot(value.toString())
            num2 = ""
        }
    }

    fun onClear(view: View) {
        // condition
        if(textViewInput?.getText()?.toString() == "0" && haveOperator){
            haveOperator = false
            setZero()
            changeButtonOperatorOne()
        }
        if(textViewInput?.getText()?.toString() == "" && haveOperator){
            haveOperator = false
            setZero()
            changeButtonOperatorOne()
        }
        if(textViewInput?.getText()?.toString() == "0" && !haveOperator){
            setZero()
        }
        if(textViewInput?.getText()?.toString() != "0" && haveOperator){
            ButtonClean?.setText("AC")
            textViewInput?.setText("0")
            setZero()
        }
        if(textViewInput?.getText()?.toString() != "0" && !haveOperator){
            ButtonClean?.setText("AC")
            textViewInput?.setText("0")
            setZero()
        }
    }

    fun onPlusMinus(view: View) {
        if(haveDigit){
            val value = textViewInput?.getText().toString().toDouble() * (-1)
            textViewInput?.setText(removeZeroAfterDot(value.toString()))
        }
    }

    fun onDecimalPoint(view: View) {
        if(!textViewInput?.getText()?.toString()?.contains(".")!! && textViewInput?.getText()?.toString() == "0"){
            textViewInput?.setText("0.")
            haveDot = true
            editText = false
        } else if (!textViewInput?.getText()?.toString()?.contains(".")!!){
            textViewInput?.append(".")
            haveDot = true
            editText = false
        }
    }

    fun onPercent(view: View) {
        if(haveDigit){
            val value = (textViewInput?.getText()?.toString()?.toDouble()?.div(100)).toString()
            textViewInput?.setText(value)
        }
    }


//    private fun onFormat(number: String): String {
//        // set comma
//        val changeType = number.toDouble()
//        val formattedNumber = decimalFormat?.format(changeType)
//        // set zero
//        val changeBack = formattedNumber.toString()
//        if ('.' in changeBack) {
//            // Remove trailing zeros
//            val trimmedStr = changeBack.trimEnd('0').trimEnd('.')
//            return trimmedStr
//        }
//        return number
//    }
    private fun removeZeroAfterDot(result: String): String {
        var value = result
        if (result.contains(".0"))
            value = result.substring(0, result.length - 2)
        return value
    }

    private fun changeButtonOperatorOne(){
        (operatorViewCheckOne as Button).setTextColor(Color.WHITE)
        operatorViewCheckOne?.setBackgroundColor(resources.getColor(R.color.orange))
    }
    private fun changeButtonOperatorTwo(){
        (operatorViewCheckTwo as Button).setTextColor(resources.getColor(R.color.orange))
        operatorViewCheckTwo?.setBackgroundColor(Color.WHITE)
    }

    private fun changeButtonOperatorOneUse(){
        (operatorViewCheckOne as Button).setTextColor(resources.getColor(R.color.orange))
        operatorViewCheckOne?.setBackgroundColor(Color.WHITE)
    }

    private fun setZero(){
        operator = ""
        firstOperator= false
        secondOperator = true
        editText = true
        clearOne = 0
        haveDot = false
        num1=""
        num2=""
        checkFirst = false
        haveDigit = false
    }

    companion object {
        private const val SWIPE_THRESHOLD = 100
        private const val SWIPE_VELOCITY_THRESHOLD = 100
    }
}

//    fun onOperator(view: View) {
//        operatorView = view
//        operator = (operatorView as Button).getText().toString()
//        haveOperator = true
//        editText = false
//        // color button
//        if(!secondOperator && firstOperator){
//            operatorViewCheckTwo = operatorView
//            changeButtonOperatorTwo()
//            changeButtonOperatorOne()
//            secondOperator = true
//            firstOperator = false
//        }
//        if(!firstOperator && secondOperator){
//            operatorViewCheckOne = operatorView
//            changeButtonOperatorOneUse()
//            firstOperator = true
//            secondOperator = false
//        }
//        if(checkFirst){
//
//
////            if(operatorSec != "" && operator != operatorSec){
////                operator = operatorSec
////                Log.e(TAG,"not equal")
////            }
////            if(operatorSec != "" && operator == operatorSec){
////                Log.e(TAG,"equal")
////            }
////            if(operatorSec == ""){
////                operatorSec = operator
////                operatorCal = operator
////                Log.e(TAG,"step1")
////            }
////            if(operatorSec == ""){
////                operatorSec = operator
////            }
//
//            num2 = textViewInput?.getText().toString()
//            if(operatorSec != operator){
//                operatorSec = operator
//                operatorCal
//                delay()
//                num2=""
//                operatorSec=""
//            } else {
//                delay()
//                operatorSec=""
//            }
//
//        } else {
//            if(textViewInput?.getText().toString() !="0" && num1 == ""){
//                num1 = textViewInput?.getText().toString()
//                haveDigit = false
//            }
//            checkFirst = true
//        }
//
//    }
