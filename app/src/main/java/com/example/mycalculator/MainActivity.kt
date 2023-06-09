package com.example.mycalculator

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.VelocityTracker
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import java.text.NumberFormat
import java.util.Locale
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintSet.Motion
import androidx.core.app.unusedapprestrictions.IUnusedAppRestrictionsBackportCallback.Default
import androidx.core.view.GestureDetectorCompat
import com.example.mycalculator.databinding.ActivityMainBinding
import java.math.BigDecimal
import java.text.DecimalFormat

class MainActivity : AppCompatActivity(){
    private var textViewInput: TextView? = null
    private var ButtonClean: Button? = null

    // gesture detector
    lateinit var gestureDetectorCompat : GestureDetector
    // view
    var DigitView: View? = null
    var operatorView: View? = null
    var operatorViewCheckOne: View? = null
    var operatorViewCheckTwo: View? = null
    // container
    var containerDigit: String = ""
    var operator: String = ""
    var operatorCheckOne: String = ""
    var operatorCheckTwo: String = ""
    var main: String = ""
    var second: String = ""
    // Boolean
    var haveDigit: Boolean = false
    var haveDot: Boolean = false
    var haveOperator: Boolean = false
    var firstOperator: Boolean = false
    var secondOperator: Boolean = true
    // format
    val decimalFormat = DecimalFormat("#,##0.00000000")

    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        textViewInput = binding.tvInput
        ButtonClean = binding.clean
        if (!haveDigit) {
            textViewInput?.setText("0")
        }
//        gestureDetectorCompat = GestureDetectorCompat(this, object : GestureDetector.SimpleOnGestureListener(){
//            override fun onFling(
//                e1: MotionEvent,
//                e2: MotionEvent,
//                velocityX:Float,
//                velocityY:Float): Boolean {
//                val distanceX = e2.x-e1.x
//                val detectorY = e2.y-e1.y
//                val text = binding.tvInput
//                if(kotlin.math.abs(distanceX) > kotlin.math.abs(detectorY)){
//                    if(distanceX > 0) {
//                        var current = containerDigit
//                        if(current.isNotEmpty()){
//                            containerDigit = current.dropLast(1)
//                        }
//                    }
//                }
//            }
//            )
//        })
    }

    fun onDigit(view: View) {
        haveDigit = true
        if (!haveDot && containerDigit.length != 9) {
            if (haveDigit) {
                // contain input & display
                DigitView = view
                containerDigit += (DigitView as Button).getText().toString()
                textViewInput?.setText(onFormat(containerDigit))
                // setButton Clear
                ButtonClean?.setText("C")
                if(haveOperator){
                    haveOperator = false
                    (operatorView as Button).setTextColor(Color.WHITE)
                    operatorView?.setBackgroundColor(resources.getColor(R.color.orange))
                    Toast.makeText(getApplicationContext(),operator,Toast.LENGTH_SHORT).show();
                }
            }
        } else if (haveDot && containerDigit.length != 10) {
            // contain input & display
            DigitView = view
            containerDigit += (DigitView as Button).getText().toString()
            textViewInput?.setText(onFormat(containerDigit))
            // setButton Clear
            ButtonClean?.setText("C")
            if(haveOperator){
                haveOperator = false
                (operatorView as Button).setTextColor(Color.WHITE)
                operatorView?.setBackgroundColor(resources.getColor(R.color.orange))
            }
        }
    }

    @SuppressLint("SetTextI18n")
    fun onClear(view: View) {
        // setButton
        ButtonClean?.setText("AC")
        // value
        containerDigit = ""
        // display
        textViewInput?.setText("0")
        // bool
        haveDot = false
        haveDigit = false
        // condition

    }


    fun onPlusMinus(view: View) {
        // calculate
        val value = containerDigit.toDouble() * (-1)
        // display & value
        containerDigit = value.toString()
        textViewInput?.setText(removeZeroAfterDot(value.toString()))
    }

    @SuppressLint("SetTextI18n")
    fun onDecimalPoint(view: View) {
        if (!containerDigit.contains(".") && containerDigit == "") {
            // value
            containerDigit += "0."
            // display
            textViewInput?.setText("0.")
            // bool
            haveDot = true
        } else if (!containerDigit.contains(".")) {
            // value
            containerDigit += "."
            // display
            textViewInput?.setText(onFormat(containerDigit) + ".")
            // bool
            haveDot = true
        }
    }

    fun onPercent(view: View) {
        // calculate
        val value = (containerDigit.toDouble() / 100).toString()
        // display & value
        containerDigit = value
        textViewInput?.setText(value)
    }

    fun onOperator(view: View) {
        operatorView = view
        operator = (operatorView as Button).getText().toString()
        haveOperator = true
        if(!secondOperator && firstOperator){
            operatorViewCheckTwo = operatorView
            (operatorViewCheckTwo as Button).setTextColor(resources.getColor(R.color.orange))
            operatorViewCheckTwo?.setBackgroundColor(Color.WHITE)
            (operatorViewCheckOne as Button).setTextColor(Color.WHITE)
            operatorViewCheckOne?.setBackgroundColor(resources.getColor(R.color.orange))
            secondOperator = true
            firstOperator = false
        }
        if(!firstOperator && secondOperator){
            operatorViewCheckOne = operatorView
            (operatorViewCheckOne as Button).setTextColor(resources.getColor(R.color.orange))
            operatorViewCheckOne?.setBackgroundColor(Color.WHITE)
            firstOperator = true
            secondOperator = false
        }





    }

    private fun onFormat(number: String): String {
        // set comma
        val changeType = number.toDouble()
        val formattedNumber = decimalFormat.format(changeType)
        // set zero
        val changeBack = formattedNumber.toString()
        if ('.' in changeBack) {
            // Remove trailing zeros
            val trimmedStr = changeBack.trimEnd('0').trimEnd('.')
            return trimmedStr
        }
        return number
    }

    private fun removeZeroAfterDot(result: String): String {
        var value = result
        if (result.contains(".0"))
            value = result.substring(0, result.length - 2)
        return value
    }

}

//    private fun isOperatorAdded(value :String): Boolean{
//        return if(value.startsWith("")){
//            false
//        } else{
//            value.contains("\u00f7")|| value.contains("\u00d7") || value.contains("+") || value.contains("-")
//        }
//    }


//}

//    fun onEqual(view:View){
//        second = tvInput!!.text.toString()
//        if(lastNumeric){
//            var prefix = ""
//
//            try {
//                if(main.startsWith("-")){
//                    prefix = "-"
//                    main = main.substring(1)
//                }
//                if(oper == "-"){
//                    if(prefix.isNotEmpty()){
//                        main = prefix + main
//                    }
//                    tvInput?.text = removeZeroAfterDot((main.toDouble() - second.toDouble()).toString())
//                }
//                if(oper == "+"){
//                    if(prefix.isNotEmpty()){
//                        main = prefix + main
//                    }
//                    tvInput?.text = removeZeroAfterDot((main.toDouble() + second.toDouble()).toString())
//                }
//                if(oper == "\u00d7"){
//                    if(prefix.isNotEmpty()){
//                        main = prefix + main
//                    }
//                    tvInput?.text = removeZeroAfterDot((main.toDouble() * second.toDouble()).toString())
//                }
//                if(oper == "\u00f7"){
//                    if(prefix.isNotEmpty()){
//                        main = prefix + main
//                    }
//                    tvInput?.text = removeZeroAfterDot((main.toDouble() / second.toDouble()).toString())
//                }
//            }catch(e: ArithmeticException){
//                e.printStackTrace()
//            }
//        }
//    }
//
//
//    fun onOperator(view:View){
//        tvInput?.text?.let{
//            if(firstInt){
//                if(lastNumeric && !isOperatorAdded(it.toString())){
//                    tempView = view
//                    if (tempView is Button) {
//                        (tempView as Button).setTextColor(resources.getColor(R.color.orange))
//                        tempView?.setBackgroundColor(Color.WHITE)
//                    }
//                    lastNumeric = false
//                    lastDot = false
//                    checker = false
//                    main = tvInput!!.text.toString()
//                    oper = (tempView as Button).getText().toString()
//                }
//            } else {
//                checkView = view
//                var data = (checkView as Button).getText().toString()
//                if( data == "-"){
//                    tvInput?.append("-")
//                }
//                firstInt = true
//            }
//        }
//    }
//    private fun isOperatorAdded(value :String): Boolean{
//        return if(value.startsWith("")){
//            false
//        } else{
//            value.contains("\u00f7")|| value.contains("\u00d7") || value.contains("+") || value.contains("-")
//        }
//    }

