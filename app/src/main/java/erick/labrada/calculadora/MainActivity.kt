package erick.labrada.calculadora

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.os.Build
import java.util.*

class MainActivity : AppCompatActivity() {
    @RequiresApi(Build.VERSION_CODES.S)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        val txtOperation = findViewById<TextView>(R.id.Operation)
        val txtResult = findViewById<TextView>(R.id.Result)

        // Buttons
        val btnOne = findViewById<Button>(R.id.One)
        val btnTwo = findViewById<Button>(R.id.Two)
        val btnThree = findViewById<Button>(R.id.Three)
        val btnFour = findViewById<Button>(R.id.Four)
        val btnFive = findViewById<Button>(R.id.Five)
        val btnSix = findViewById<Button>(R.id.Six)
        val btnSeven = findViewById<Button>(R.id.Seven)
        val btnEight = findViewById<Button>(R.id.Eight)
        val btnNine = findViewById<Button>(R.id.Nine)
        val btnZero = findViewById<Button>(R.id.Zero)
        val btnTimes = findViewById<Button>(R.id.Times)
        val btnDivision = findViewById<Button>(R.id.Division)
        val btnPlus = findViewById<Button>(R.id.Plus)
        val btnMinus = findViewById<Button>(R.id.Minus)
        val btnDelete = findViewById<Button>(R.id.Delete)
        val btnEquals = findViewById<Button>(R.id.Equals)
        val btnDot = findViewById<Button>(R.id.Dot)

        var operation: String = ""

        fun updateOperation(value: String): String {
            operation += value
            return operation
        }

        btnOne.setOnClickListener { operation = updateOperation("1"); txtOperation.text = operation }
        btnTwo.setOnClickListener { operation = updateOperation("2"); txtOperation.text = operation }
        btnThree.setOnClickListener { operation = updateOperation("3"); txtOperation.text = operation }
        btnFour.setOnClickListener { operation = updateOperation("4"); txtOperation.text = operation }
        btnFive.setOnClickListener { operation = updateOperation("5"); txtOperation.text = operation }
        btnSix.setOnClickListener { operation = updateOperation("6"); txtOperation.text = operation }
        btnSeven.setOnClickListener { operation = updateOperation("7"); txtOperation.text = operation }
        btnEight.setOnClickListener { operation = updateOperation("8"); txtOperation.text = operation }
        btnNine.setOnClickListener { operation = updateOperation("9"); txtOperation.text = operation }
        btnZero.setOnClickListener { operation = updateOperation("0"); txtOperation.text = operation }
        btnDot.setOnClickListener { operation = updateOperation("."); txtOperation.text = operation }
        btnPlus.setOnClickListener { operation = updateOperation(" + "); txtOperation.text = operation }
        btnMinus.setOnClickListener { operation = updateOperation(" - "); txtOperation.text = operation }
        btnTimes.setOnClickListener { operation = updateOperation(" * "); txtOperation.text = operation }
        btnDivision.setOnClickListener { operation = updateOperation(" / "); txtOperation.text = operation }

        btnDelete.setOnClickListener {
            operation = ""
            txtOperation.text = "0"
            txtResult.text = "0"
        }

        btnEquals.setOnClickListener {
            val result = calculate(operation)
            txtResult.text = result
            operation = ""
            txtOperation.text = operation
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun calculate(expression: String): String {
        return try {
            val result = eval(expression)
            result.toString()
        } catch (e: Exception) {
            "Error"
        }
    }

    private fun eval(expression: String): Double {
        val tokens = StringTokenizer(expression, " +-*/", true)
        val values = Stack<Double>()
        val operators = Stack<Char>()

        while (tokens.hasMoreTokens()) {
            val token = tokens.nextToken().trim()
            when {
                token.isEmpty() -> continue
                token.matches(Regex("-?\\d+(\\.\\d+)?")) -> values.push(token.toDouble())
                token in listOf("+", "-", "*", "/") -> {
                    while (operators.isNotEmpty() && precedence(token[0]) <= precedence(operators.peek())) {
                        values.push(applyOperation(operators.pop(), values.pop(), values.pop()))
                    }
                    operators.push(token[0])
                }
            }
        }

        while (operators.isNotEmpty()) {
            values.push(applyOperation(operators.pop(), values.pop(), values.pop()))
        }

        return values.pop()
    }

    private fun precedence(op: Char): Int {
        return when (op) {
            '+', '-' -> 1
            '*', '/' -> 2
            else -> -1
        }
    }

    private fun applyOperation(op: Char, b: Double, a: Double): Double {
        return when (op) {
            '+' -> a + b
            '-' -> a - b
            '*' -> a * b
            '/' -> if (b != 0.0) a / b else throw ArithmeticException("Division by zero")
            else -> 0.0
        }
    }
}