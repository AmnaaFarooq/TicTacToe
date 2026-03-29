package com.example.tictactoe

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.GridLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat

class MainActivity : AppCompatActivity() {

    private lateinit var tvStatus: TextView
    private lateinit var tvScore: TextView
    private lateinit var btnRestart: Button
    private val buttons = Array(3) { arrayOfNulls<Button>(3) }

    private var board = Array(3) { arrayOfNulls<String>(3) }
    private var currentPlayer = "X"
    private var gameActive = true
    private var scoreX = 0
    private var scoreO = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tvStatus = findViewById(R.id.tvStatus)
        tvScore = findViewById(R.id.tvScore)
        btnRestart = findViewById(R.id.btnRestart)

        setupBoard()
        updateStatus()
        updateScore()

        btnRestart.setOnClickListener { resetGame() }
    }

    private fun setupBoard() {
        val grid = findViewById<GridLayout>(R.id.gridBoard)
        grid.removeAllViews()

        for (row in 0..2) {
            for (col in 0..2) {
                val btn = Button(this).apply {
                    textSize = 36f
                    setTextColor(ContextCompat.getColor(context, R.color.cell_text))
                    setBackgroundResource(R.drawable.cell_background)
                    layoutParams = GridLayout.LayoutParams().apply {
                        width = 0
                        height = 0
                        rowSpec = GridLayout.spec(row, 1f)
                        columnSpec = GridLayout.spec(col, 1f)
                        setMargins(6, 6, 6, 6)
                    }
                    setOnClickListener { onCellClick(row, col, this) }
                }
                buttons[row][col] = btn
                grid.addView(btn)
            }
        }
    }

    private fun onCellClick(row: Int, col: Int, btn: Button) {
        if (!gameActive || board[row][col] != null) return

        board[row][col] = currentPlayer
        btn.text = currentPlayer

        if (currentPlayer == "X") {
            btn.setTextColor(ContextCompat.getColor(this, R.color.player_x))
        } else {
            btn.setTextColor(ContextCompat.getColor(this, R.color.player_o))
        }

        when {
            checkWinner(currentPlayer) -> {
                gameActive = false
                tvStatus.text = "🎉 Player $currentPlayer Wins!"
                if (currentPlayer == "X") scoreX++ else scoreO++
                updateScore()
                highlightWinner(currentPlayer)
            }
            checkDraw() -> {
                gameActive = false
                tvStatus.text = "🤝 It's a Draw!"
            }
            else -> {
                currentPlayer = if (currentPlayer == "X") "O" else "X"
                updateStatus()
            }
        }
    }

    private fun checkWinner(player: String): Boolean {
        for (i in 0..2) {
            if (board[i][0] == player && board[i][1] == player && board[i][2] == player) return true
            if (board[0][i] == player && board[1][i] == player && board[2][i] == player) return true
        }
        if (board[0][0] == player && board[1][1] == player && board[2][2] == player) return true
        if (board[0][2] == player && board[1][1] == player && board[2][0] == player) return true
        return false
    }

    private fun checkDraw(): Boolean {
        return board.all { row -> row.all { it != null } }
    }

    private fun highlightWinner(player: String) {
        val color = if (player == "X") R.color.player_x else R.color.player_o
        for (row in 0..2) {
            for (col in 0..2) {
                if (board[row][col] == player) {
                    buttons[row][col]?.setBackgroundResource(R.drawable.cell_winner)
                }
            }
        }
    }

    private fun updateStatus() {
        tvStatus.text = "Player $currentPlayer's Turn"
    }

    private fun updateScore() {
        tvScore.text = "X: $scoreX  |  O: $scoreO"
    }

    private fun resetGame() {
        board = Array(3) { arrayOfNulls<String>(3) }
        currentPlayer = "X"
        gameActive = true
        for (row in 0..2) {
            for (col in 0..2) {
                buttons[row][col]?.apply {
                    text = ""
                    setBackgroundResource(R.drawable.cell_background)
                    setTextColor(ContextCompat.getColor(context, R.color.cell_text))
                }
            }
        }
        updateStatus()
    }
}
