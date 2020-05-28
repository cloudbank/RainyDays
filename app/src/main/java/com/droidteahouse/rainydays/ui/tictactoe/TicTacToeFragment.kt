package com.droidteahouse.rainydays.ui.tictactoe

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.droidteahouse.rainydays.R
import com.google.android.material.button.MaterialButton
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_tic_tac_toe.*

import java.util.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [TicTacToeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class TicTacToeFragment : Fragment() {


    var activePlayer = 1
    lateinit var buttons: List<MaterialButton>
    var player1WinsCounts = 0
    var player2WinsCounts = 0
    private var param1: String? = null
    private var param2: String? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it?.getString(ARG_PARAM1)
            param2 = it?.getString(ARG_PARAM2)
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_tic_tac_toe, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        buttons = arrayListOf<MaterialButton>(bu1, bu2, bu3, bu4, bu5, bu6, bu7, bu8, bu9)
        buttons.forEach { it -> it.setOnClickListener({ playGame(it as MaterialButton) }) }
        reset.setOnClickListener { reset(it) }
    }


    fun playGame(buSelected: MaterialButton) {
        if (activePlayer == 1) {
            buSelected.text = "X"
            buSelected.isEnabled = false
        } else {
            buSelected.text = "O"
            buSelected.isEnabled = false
        }
        val winner = checkWinner()
        if (winner > 0) {
            table.isClickable = false
            if (winner == 1) player1WinsCounts++ else (player2WinsCounts++)
            Snackbar.make(clayout, "The winner is Player ${winner}", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
            getEmptyCells()
                .forEach { it.isClickable = false }
            reset.visibility = View.VISIBLE

            //restartGame()
        } else {
            if (activePlayer == 1) {
                activePlayer = 2
                if (getEmptyCells().size > 0) {
                    autoPlay()
                } else {
                    Snackbar.make(clayout, "A tie, no winner.", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show()
                    reset.visibility = View.VISIBLE
                }

            } else {
                activePlayer = 1
            }
        }

    }


    fun checkWinner(): Int {
        return when {
            (bu1.text != "") && ((bu1.text.equals(bu2.text) && bu2.text.equals(bu3.text)) || (bu1.text.equals(
                bu4.text
            ) && bu4.text.equals(bu7.text)) || (bu1.text.equals(bu5.text) && bu5.text.equals(bu9.text))) -> numberForText(
                bu1.text.toString()
            )
            (bu5.text != "") && ((bu4.text.equals(bu5.text) && bu5.text.equals(bu6.text)) || (bu2.text.equals(
                bu5.text
            ) && bu5.text.equals(bu8.text)) || (bu3.text.equals(bu5.text) && bu5.text.equals(bu7.text))) -> numberForText(
                bu5.text.toString()
            )
            (bu9.text != "") && ((bu7.text.equals(bu8.text) && bu8.text.equals(bu9.text)) || (bu3.text.equals(
                bu6.text
            ) && bu6.text.equals(bu9.text))) -> numberForText(
                bu9.text.toString()
            )
            else -> -1
        }
    }

    private fun numberForText(s: String): Int {
        return if (s.equals("X")) 1 else 2
    }

    fun autoPlay() {

        val emptyCells = getEmptyCells()
        if (emptyCells.size == 0) {
            restartGame()
        }
        val r = Random()
        val randIndex = r.nextInt(emptyCells.size)
        val selected = emptyCells[randIndex]
        playGame(selected)
    }

    private fun getEmptyCells(): ArrayList<MaterialButton> {
        var emptyCells = arrayListOf<MaterialButton>()
        for (b in buttons) {
            if (b.text == "") {
                emptyCells.add(b)
            }
        }
        return emptyCells
    }

    fun restartGame() {
        Snackbar.make(
            clayout,
            "Restarting Game Player1: $player1WinsCounts, Player2: $player2WinsCounts",
            Snackbar.LENGTH_LONG
        )
            .setAction("Action", null).show()

        activePlayer = 1
        for (b in buttons) {
            b!!.text = ""
            b!!.isEnabled = true
            b!!.isClickable = true
        }

    }

    fun reset(view: View) {
        for (i in 0 until table.getChildCount()) {
            table.getChildAt(i).isClickable = true
        }
        view.visibility = View.INVISIBLE
        restartGame()

    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment TicTacToeFragment.
         */

        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            TicTacToeFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
