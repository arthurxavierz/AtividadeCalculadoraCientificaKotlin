package com.example.atividadecalculadoracientifica

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import kotlin.math.*

class MainActivity : AppCompatActivity() {

    // Estado da calculadora
    private var expressao = ""          // Expressão completa mostrada no visor secundário
    private var entrada = ""            // Número sendo digitado agora
    private var operadorPendente = ""   // Operador aguardando segundo número
    private var primeiroNumero = 0.0    // Primeiro operando
    private var acabouDeCalcular = false // Flag: acabou de pressionar =

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // ── Referências ──────────────────────────────────────
        val tvResultado     = findViewById<TextView>(R.id.tvResultado)
        val tvExpressao     = findViewById<TextView>(R.id.tvExpressao)
        val drawerLayout    = findViewById<DrawerLayout>(R.id.drawerLayout)
        val tvAbrirCientifico = findViewById<TextView>(R.id.tvAbrirCientifico)

        // ── Abrir menu científico ─────────────────────────────
        tvAbrirCientifico.setOnClickListener {
            drawerLayout.openDrawer(findViewById(R.id.drawerCientifico))
        }

        // ── Função auxiliar para atualizar o visor ────────────
        fun atualizarVisor() {
            tvResultado.text = if (entrada.isEmpty()) "0" else entrada
            tvExpressao.text = expressao
        }

        // ── Função para digitar número ────────────────────────
        fun digitarNumero(digito: String) {
            if (acabouDeCalcular) {
                entrada = ""
                expressao = ""
                acabouDeCalcular = false
            }
            if (digito == "." && entrada.contains(".")) return
            if (entrada == "0" && digito != ".") entrada = ""
            entrada += digito
            atualizarVisor()
        }

        // ── Função para aplicar operador ──────────────────────
        fun aplicarOperador(op: String) {
            if (entrada.isEmpty() && operadorPendente.isNotEmpty()) {
                // Impede dois operadores seguidos: só troca o operador
                operadorPendente = op
                expressao = expressao.dropLast(1) + op
                tvExpressao.text = expressao
                return
            }
            if (entrada.isNotEmpty()) {
                primeiroNumero = if (operadorPendente.isEmpty()) {
                    entrada.toDouble()
                } else {
                    val segundo = entrada.toDouble()
                    when (operadorPendente) {
                        "+" -> primeiroNumero + segundo
                        "−" -> primeiroNumero - segundo
                        "×" -> primeiroNumero * segundo
                        "÷" -> {
                            if (segundo == 0.0) {
                                Toast.makeText(this, "Divisão por zero!", Toast.LENGTH_SHORT).show()
                                limpar(tvResultado, tvExpressao)
                                return
                            }
                            primeiroNumero / segundo
                        }
                        else -> segundo
                    }
                }
                expressao += "${formatarNumero(primeiroNumero)}$op"
                entrada = ""
                operadorPendente = op
                tvResultado.text = formatarNumero(primeiroNumero)
                tvExpressao.text = expressao
            }
        }

        // ── Função calcular (=) ───────────────────────────────
        fun calcular() {
            if (entrada.isEmpty() || operadorPendente.isEmpty()) return
            val segundo = entrada.toDouble()
            val resultado = when (operadorPendente) {
                "+" -> primeiroNumero + segundo
                "−" -> primeiroNumero - segundo
                "×" -> primeiroNumero * segundo
                "÷" -> {
                    if (segundo == 0.0) {
                        Toast.makeText(this, "Divisão por zero!", Toast.LENGTH_SHORT).show()
                        limpar(tvResultado, tvExpressao)
                        return
                    }
                    primeiroNumero / segundo
                }
                "^" -> primeiroNumero.pow(segundo)
                else -> segundo
            }
            expressao += "${formatarNumero(segundo)} ="
            tvExpressao.text = expressao
            entrada = formatarNumero(resultado)
            tvResultado.text = entrada
            operadorPendente = ""
            primeiroNumero = resultado
            acabouDeCalcular = true
        }

        // ── Botões numéricos ──────────────────────────────────
        val numerosIds = mapOf(
            R.id.btn0 to "0", R.id.btn1 to "1", R.id.btn2 to "2",
            R.id.btn3 to "3", R.id.btn4 to "4", R.id.btn5 to "5",
            R.id.btn6 to "6", R.id.btn7 to "7", R.id.btn8 to "8",
            R.id.btn9 to "9", R.id.btnPonto to "."
        )
        numerosIds.forEach { (id, digito) ->
            findViewById<Button>(id).setOnClickListener { digitarNumero(digito) }
        }

        // ── Botões de operador ────────────────────────────────
        mapOf(
            R.id.btnAdd to "+",
            R.id.btnSub to "−",
            R.id.btnMult to "×",
            R.id.btnDiv to "÷"
        ).forEach { (id, op) ->
            findViewById<Button>(id).setOnClickListener { aplicarOperador(op) }
        }

        // ── Botão = ───────────────────────────────────────────
        findViewById<Button>(R.id.btnIgual).setOnClickListener { calcular() }

        // ── Botão C (limpar) ──────────────────────────────────
        findViewById<Button>(R.id.btnC).setOnClickListener {
            limpar(tvResultado, tvExpressao)
        }

        // ── Botão +/- ─────────────────────────────────────────
        findViewById<Button>(R.id.btnPlusMinus).setOnClickListener {
            if (entrada.isNotEmpty() && entrada != "0") {
                entrada = if (entrada.startsWith("-")) entrada.drop(1)
                else "-$entrada"
                tvResultado.text = entrada
            }
        }

        // ── Botão % ───────────────────────────────────────────
        findViewById<Button>(R.id.btnPorcento).setOnClickListener {
            if (entrada.isNotEmpty()) {
                val valor = entrada.toDoubleOrNull() ?: return@setOnClickListener
                entrada = formatarNumero(valor / 100)
                tvResultado.text = entrada
            }
        }

        // ── Funções Científicas ───────────────────────────────
        fun aplicarFuncaoCientifica(funcao: String) {
            val valor = entrada.toDoubleOrNull() ?: run {
                Toast.makeText(this, "Digite um número primeiro!", Toast.LENGTH_SHORT).show()
                return
            }
            val resultado = when (funcao) {
                "sen"  -> sin(Math.toRadians(valor))
                "cos"  -> cos(Math.toRadians(valor))
                "tan"  -> {
                    if (valor % 180 == 90.0) {
                        Toast.makeText(this, "tan(90°) indefinida!", Toast.LENGTH_SHORT).show()
                        return
                    }
                    tan(Math.toRadians(valor))
                }
                "log"  -> {
                    if (valor <= 0) {
                        Toast.makeText(this, "log indefinido para x ≤ 0!", Toast.LENGTH_SHORT).show()
                        return
                    }
                    log10(valor)
                }
                "ln"   -> {
                    if (valor <= 0) {
                        Toast.makeText(this, "ln indefinido para x ≤ 0!", Toast.LENGTH_SHORT).show()
                        return
                    }
                    ln(valor)
                }
                "raiz" -> {
                    if (valor < 0) {
                        Toast.makeText(this, "Raiz de número negativo!", Toast.LENGTH_SHORT).show()
                        return
                    }
                    sqrt(valor)
                }
                else -> valor
            }
            expressao = "$funcao(${ formatarNumero(valor) }) ="
            entrada = formatarNumero(resultado)
            tvExpressao.text = expressao
            tvResultado.text = entrada
            acabouDeCalcular = true
            drawerLayout.closeDrawers()
        }

        findViewById<Button>(R.id.btnSen).setOnClickListener  { aplicarFuncaoCientifica("sen") }
        findViewById<Button>(R.id.btnCos).setOnClickListener  { aplicarFuncaoCientifica("cos") }
        findViewById<Button>(R.id.btnTan).setOnClickListener  { aplicarFuncaoCientifica("tan") }
        findViewById<Button>(R.id.btnLog).setOnClickListener  { aplicarFuncaoCientifica("log") }
        findViewById<Button>(R.id.btnLn).setOnClickListener   { aplicarFuncaoCientifica("ln") }
        findViewById<Button>(R.id.btnRaiz).setOnClickListener { aplicarFuncaoCientifica("raiz") }

        // Potência: usa o operador ^ entre dois números
        findViewById<Button>(R.id.btnPot).setOnClickListener {
            aplicarOperador("^")
            drawerLayout.closeDrawers()
        }

        // Pi: insere o valor de π
        findViewById<Button>(R.id.btnPi).setOnClickListener {
            entrada = formatarNumero(Math.PI)
            tvResultado.text = entrada
            drawerLayout.closeDrawers()
        }
    }

    // ── Limpar estado ─────────────────────────────────────────
    private fun limpar(tvResultado: TextView, tvExpressao: TextView) {
        expressao = ""
        entrada = ""
        operadorPendente = ""
        primeiroNumero = 0.0
        acabouDeCalcular = false
        tvResultado.text = "0"
        tvExpressao.text = ""
    }

    // ── Formata número: remove .0 desnecessário ───────────────
    private fun formatarNumero(valor: Double): String {
        return if (valor == valor.toLong().toDouble()) {
            valor.toLong().toString()
        } else {
            // Limita a 10 casas decimais para não explodir o visor
            "%.10f".format(valor).trimEnd('0').trimEnd('.')
        }
    }
}