package com.example.mycalculator

import android.graphics.Color
import android.os.Bundle
import android.view.GestureDetector
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.mycalculator.databinding.ActivityMainBinding
import java.text.DecimalFormat
import java.text.NumberFormat
import java.util.*


class MainActivity : AppCompatActivity() {
    // textview
    private var textViewInput: TextView? = null
    // button
    private var ButtonC: Button? = null
    // check
    var lastNumeric : Boolean = false
    var lastDot : Boolean = false
    var checkerFirst : Boolean = true
    var firstInt: Boolean = false
    var tempView: View? = null
    var checkView: View? = null
    var check: View? = null
    // calculate
    var main : String = ""
    var oper : String = ""
    var second : String = ""
    // bucket
    var bucketOne : String = ""
    // GesterDetector
    private var gestureDetector: GestureDetector? = null


    var decimalFormat: DecimalFormat = DecimalFormat("#,###.##")

    private val binding: ActivityMainBinding by lazy{
        ActivityMainBinding.inflate(layoutInflater)}
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        textViewInput = binding.tvInput
        ButtonC = binding.clean
    }

    fun onDigit(view: View){
        check = view
        if(!checkerFirst){
            textViewInput?.setText("")
            checkerFirst = true
            (tempView as Button).setTextColor(Color.WHITE)
            tempView?.setBackgroundColor(resources.getColor(R.color.orange))
        }
        var digit = (check as Button).getText().toString()
        bucketOne += digit
        textViewInput?.setText(onFormat(bucketOne))
        ButtonC?.setText("C")
        lastNumeric = true
        lastDot = false
        //        bucketOne = bucketOne + tvInput?.append((view as Button).text)
//        Toast.makeText(getApplicationContext(),bucketOne,Toast.LENGTH_SHORT).show();
//        textView2Input?.append((view as Button).text)
//        textViewInput?.append(bucketOne)
//        textViewInput?.append((view as Button).text)
        //        tvInput?.append((view as Button).text)
    }


    fun onPlusMinus(view: View){
        val tvValue = textViewInput?.text.toString()
        val calPM = (tvValue.toDouble() *-1).toString()
        textViewInput?.setText(removeZeroAfterDot(calPM))
    }
    fun onPercent(view:View){
        val tvValue = textViewInput?.text.toString()
        val calPM = (tvValue.toDouble() /100).toString()
        textViewInput?.text = calPM
    }
    fun onClear(view: View){
        textViewInput?.text = ""
        ButtonC?.setText("AC")
        bucketOne = ""
        tempView?.setBackgroundColor(resources.getColor(R.color.orange))
    }
    fun onDecimalPoint(view:View){
        var textView = textViewInput?.text
        if(lastNumeric && !lastDot){
            if(textView?.contains('.') == false){
                textViewInput?.append(".")
                bucketOne += "."
                lastNumeric = false
                lastDot = true
            }
        }
        if(textView?.length == 0){
            textViewInput?.append("0.")
            bucketOne += "0."
            lastNumeric = false
            lastDot = true
        }

    }
    fun onEqual(view:View){
        second = textViewInput!!.text.toString()
        if(lastNumeric){
            var prefix = ""
            try {
                if(main.startsWith("-")){
                    prefix = "-"
                    main = main.substring(1)
                }
                if(oper == "-"){
                    if(prefix.isNotEmpty()){
                        main = prefix + main
                    }
                    textViewInput?.text = removeZeroAfterDot((main.toDouble() - second.toDouble()).toString())
                }
                if(oper == "+"){
                    if(prefix.isNotEmpty()){
                        main = prefix + main
                    }
                    textViewInput?.text = removeZeroAfterDot((main.toDouble() + second.toDouble()).toString())
                }
                if(oper == "\u00d7"){
                    if(prefix.isNotEmpty()){
                        main = prefix + main
                    }
                    textViewInput?.text = removeZeroAfterDot((main.toDouble() * second.toDouble()).toString())
                }
                if(oper == "\u00f7"){
                    if(prefix.isNotEmpty()){
                        main = prefix + main
                    }
                    textViewInput?.text = removeZeroAfterDot((main.toDouble() / second.toDouble()).toString())
                }
            }catch(e: ArithmeticException){
                e.printStackTrace()
            }
        }
    }

    fun onOperator(view:View){
        textViewInput?.text?.let{
            if(firstInt){
                if(lastNumeric && !isOperatorAdded(it.toString())){
                    tempView = view
                    if (tempView is Button) {
                        (tempView as Button).setTextColor(resources.getColor(R.color.orange))
                        tempView?.setBackgroundColor(Color.WHITE)
                    }
                    lastNumeric = false
                    lastDot = false
                    checkerFirst = false
                    main = textViewInput!!.text.toString()
                    oper = (tempView as Button).getText().toString()
                    bucketOne = ""

                }
            } else {
                checkView = view
                var data = (checkView as Button).getText().toString()
                if( data == "-"){
                    textViewInput?.append("-")
                }
                firstInt = true
            }
        }
    }
    private fun isOperatorAdded(value :String): Boolean{
        return if(value.startsWith("")){
            false
        } else{
            value.contains("\u00f7")|| value.contains("\u00d7") || value.contains("+") || value.contains("-")
        }
    }
    private fun removeZeroAfterDot(result:String):String{
        var value = result
        if(result.contains(".0"))
            value = result.substring(0,result.length-2)
        return value
    }

    private fun onFormat(result: String): String {
        val change = result.toDouble()
        val numberFormat = NumberFormat.getNumberInstance(Locale.US)
        return numberFormat.format(change)
    }
}