package com.example.bepis

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import androidx.core.text.isDigitsOnly
import androidx.core.view.children

import kotlinx.android.synthetic.main.activity_main.*
import java.util.*
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity() {

    private fun evalexpr(str: CharSequence): CharSequence {
        var operands = Stack<Int>()
        var operators = Stack<Char>()
        val ops = "*/+-"


        val opfun = { c: Char ->
            when (c) {
                '+' -> {a: Int, b: Int -> a + b}
                '-' -> {a: Int, b: Int -> a - b}
                '*' -> {a: Int, b: Int -> a * b}
                '/' -> {a: Int, b: Int -> a / b}
                else -> {_: Int, _: Int -> 0}
            }
        }

        val apply =  {
            val fop = opfun(operators.pop());
            val a = operands.pop()
            val b = operands.pop()
            operands.push(fop(a, b))
        }
        // L'algorithme de Djikstra

        val tokenize = { s: String ->
            s.split("").fold(ArrayList(), { acc: ArrayList<String>, str: String ->
                if (str == "")
                    acc
                else if ("()".contains(str) || ops.contains(str)) {
                    acc.add(str)
                } else if ("0123456789".contains(str[0])) {
                    if (acc.size > 0 && acc.last().isDigitsOnly()) {
                        var e = acc.last() + str
                        acc.remove(acc.last())
                        acc.add(e)
                    } else {
                        acc.add(str)
                    }
                }
                acc
            })
        }
        for (token in tokenize(str.toString())) {
            Log.d("myTag2", token)

            when (token) {
                "+", "-", "*", "/" -> {
                    while (!operators.empty() &&
                        (operators.peek() != '(' &&
                                ops.indexOf(operators.peek()) < ops.indexOf(token))) {
                        apply()
                    }
                    operators.push(token[0])
                }
                "(" -> operators.push('(')
                ")" -> {
                    while(operators.peek() != '(') {
                        apply()
                    }
                    operators.pop()
                }
                else ->
                    operands.push(token.toInt())
            }

        }
        while(operators.size != 0)
            apply()
        return operands.pop().toString()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        var t = this.textView4;
        for (v in this.lays.children) {
            if (v is Button) {
                v.setOnClickListener {
                    when (v.text) {
                        "=" -> this.textView4.text = evalexpr(this.textView4.text)
                        else -> this.textView4.append(v.text)
                    }
                }
            }
        }
    }

    override fun onBackPressed() {
        if (this.textView4.text.isNotEmpty())
            this.textView4.text = this.textView4.text.substring(0, this.textView4.text.length - 1)
         else
            super.onBackPressed()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }


}
