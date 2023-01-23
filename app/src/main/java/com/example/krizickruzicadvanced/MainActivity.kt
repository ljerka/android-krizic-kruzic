package com.example.krizickruzicadvanced

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*


enum class Odabir(val ikona: Int, val naziv: String, val oznaka: String) {
    KRIZIC(R.drawable.krizic, "krizic", "X"),
    KRUZIC(R.drawable.kruzic, "kruzic", "O")
}

class MainActivity : AppCompatActivity() {

    private var neiskoristeno: MutableSet<ImageView> = mutableSetOf()
    private var poteziIgrac: MutableSet<ImageView> = mutableSetOf()
    private var poteziRacunalo: MutableSet<ImageView> = mutableSetOf()
    private var igracXO = Odabir.KRIZIC
    private var racunaloXO = Odabir.KRUZIC
    private var pobjeda = false



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setFieldForGame()

        neiskoristeno.forEach {
            it.setOnClickListener { v ->
                if (pobjeda || neiskoristeno.size==0) {
                    setFieldForGame()
                }
                else if (!neiskoristeno.contains(it)) {
                    Toast.makeText(this, "NEDOZVOLJEN POTEZ!", Toast.LENGTH_SHORT).show()
                }
                else {
                    it.setImageResource(igracXO.ikona)
                    neiskoristeno.remove(it)
                    poteziIgrac.add(it)

                    if (!isWin(poteziIgrac)) {
                        calculateNextMove()
                    }
                }
            }
        }

        reset.setOnClickListener {
            setFieldForGame()
        }

        chooseXO.setOnClickListener {
            if (chooseXO.text.toString().equals("Igraj kao X")) {
                chooseXO.text = "Igraj kao O"
            }
            else {
                chooseXO.text = "Igraj kao X"
            }
        }
    }

    private fun setFieldForGame() {
        pobjeda = false

        poteziIgrac.clear()
        poteziRacunalo.clear()

        neiskoristeno.clear()
        neiskoristeno.add(f00)
        neiskoristeno.add(f01)
        neiskoristeno.add(f02)
        neiskoristeno.add(f10)
        neiskoristeno.add(f11)
        neiskoristeno.add(f12)
        neiskoristeno.add(f20)
        neiskoristeno.add(f21)
        neiskoristeno.add(f22)

        neiskoristeno.forEach {
            it.setImageDrawable(null)
        }

        f00.setBackgroundColor(this.getResources().getColor(R.color.odd))
        f02.setBackgroundColor(this.getResources().getColor(R.color.odd))
        f11.setBackgroundColor(this.getResources().getColor(R.color.odd))
        f20.setBackgroundColor(this.getResources().getColor(R.color.odd))
        f22.setBackgroundColor(this.getResources().getColor(R.color.odd))
        f01.setBackgroundColor(this.getResources().getColor(R.color.even))
        f10.setBackgroundColor(this.getResources().getColor(R.color.even))
        f12.setBackgroundColor(this.getResources().getColor(R.color.even))
        f21.setBackgroundColor(this.getResources().getColor(R.color.even))

        if (chooseXO.text.toString().equals("Igraj kao X")) {
            igracXO = Odabir.KRUZIC
            racunaloXO = Odabir.KRIZIC
        } else {
            igracXO = Odabir.KRIZIC
            racunaloXO = Odabir.KRUZIC
        }
        napomena.text = "Igrač igra kao "+igracXO.oznaka

        if (racunaloXO.naziv.equals("krizic")) {
            calculateNextMove()
        }
    }

    private fun calculateNextMove() {
        if (neiskoristeno.size == 0) {
            Toast.makeText(this, "NEMA POBJEDNIKA", Toast.LENGTH_SHORT).show()
        }
        // ako je prvi ili drugi potez, probaj staviti u sredinu
        // ako je sredina već puna, stavi u ugao
        else if (neiskoristeno.size > 7) {
            if (neiskoristeno.contains(f11)) {
                makeNextMove(f11)
            }
            else {
                makeNextMove(f00)
            }
        }
        // ako je treći potez (sigurno imaš sredinu), stavi uz odabrano polje
        else if (neiskoristeno.size == 7) {
            if (!neiskoristeno.contains(f00) || !neiskoristeno.contains(f20)) {
                makeNextMove(f10)
            }
            else if (!neiskoristeno.contains(f02) || !neiskoristeno.contains(f22)) {
                makeNextMove(f12)
            }
            else if (!neiskoristeno.contains(f01) || !neiskoristeno.contains(f10)) {
                makeNextMove(f00)
            }
            else if (!neiskoristeno.contains(f12) || !neiskoristeno.contains(f21)) {
                makeNextMove(f22)
            }
            else {
                makeNextMove(neiskoristeno.last())
            }
        }
        // ako je četvrti i > potez:
        else {
            // provjeri možeš li pobijediti
            // sredinu je sigurno ispunjena pa nju ne provjeravam
            if (neiskoristeno.contains(f00) &&
                ((poteziRacunalo.contains(f01) && poteziRacunalo.contains(f02)) ||
                (poteziRacunalo.contains(f10) && poteziRacunalo.contains(f20)) ||
                (poteziRacunalo.contains(f11) && poteziRacunalo.contains(f22))) ) {

                makeNextMove(f00)
            }
            else if (neiskoristeno.contains(f02) &&
                ((poteziRacunalo.contains(f01) && poteziRacunalo.contains(f00)) ||
                (poteziRacunalo.contains(f12) && poteziRacunalo.contains(f22)) ||
                (poteziRacunalo.contains(f11) && poteziRacunalo.contains(f20))) ) {

                makeNextMove(f02)
            }
            else if (neiskoristeno.contains(f20)&&
                ((poteziRacunalo.contains(f10) && poteziRacunalo.contains(f00)) ||
                (poteziRacunalo.contains(f21) && poteziRacunalo.contains(f22)) ||
                (poteziRacunalo.contains(f11) && poteziRacunalo.contains(f02))) ) {

                makeNextMove(f20)
            }
            else if (neiskoristeno.contains(f22)&&
                ((poteziRacunalo.contains(f02) && poteziRacunalo.contains(f12)) ||
                (poteziRacunalo.contains(f20) && poteziRacunalo.contains(f21)) ||
                (poteziRacunalo.contains(f11) && poteziRacunalo.contains(f00))) ) {

                makeNextMove(f22)
            }
            else if (neiskoristeno.contains(f01) &&
                ((poteziRacunalo.contains(f11) && poteziRacunalo.contains(f21)) ||
                (poteziRacunalo.contains(f00) && poteziRacunalo.contains(f02)) ) ) {

                makeNextMove(f01)
            }
            else if (neiskoristeno.contains(f10)&&
                ((poteziRacunalo.contains(f11) && poteziRacunalo.contains(f12)) ||
                (poteziRacunalo.contains(f00) && poteziRacunalo.contains(f20)) ) ) {

                makeNextMove(f10)
            }
            else if (neiskoristeno.contains(f12)&&
                ((poteziRacunalo.contains(f11) && poteziRacunalo.contains(f10)) ||
                (poteziRacunalo.contains(f02) && poteziRacunalo.contains(f22)) ) ) {

                makeNextMove(f12)
            }
            else if (neiskoristeno.contains(f21)&&
                ((poteziRacunalo.contains(f11) && poteziRacunalo.contains(f01)) ||
                (poteziRacunalo.contains(f20) && poteziRacunalo.contains(f22)) ) ) {

                makeNextMove(f21)
            }
            // provjeri jesu li protivnička 2 vezana i blokiraj
            else if (((poteziIgrac.contains(f10) && poteziIgrac.contains(f20)) ||
                (poteziIgrac.contains(f01) && poteziIgrac.contains(f02)) ||
                (poteziIgrac.contains(f11) && poteziIgrac.contains(f22))) &&
                !poteziRacunalo.contains(f00) && neiskoristeno.contains(f00)) {

                makeNextMove(f00)
            }
            else if (((poteziIgrac.contains(f00) && poteziIgrac.contains(f01)) ||
                (poteziIgrac.contains(f22) && poteziIgrac.contains(f12)) ||
                (poteziIgrac.contains(f20) && poteziIgrac.contains(f11))) &&
                !poteziRacunalo.contains(f02) && neiskoristeno.contains(f02)) {

                makeNextMove(f02)
            }
            else if (((poteziIgrac.contains(f00) && poteziIgrac.contains(f10)) ||
                (poteziIgrac.contains(f22) && poteziIgrac.contains(f21)) ||
                (poteziIgrac.contains(f02) && poteziIgrac.contains(f11))) &&
                !poteziRacunalo.contains(f20) && neiskoristeno.contains(f20)) {

                makeNextMove(f20)
            }
            else if (((poteziIgrac.contains(f02) && poteziIgrac.contains(f12)) ||
                (poteziIgrac.contains(f20) && poteziIgrac.contains(f21)) ||
                (poteziIgrac.contains(f00) && poteziIgrac.contains(f11))) &&
                !poteziRacunalo.contains(f22) && neiskoristeno.contains(f22)) {

                makeNextMove(f22)
            }
            else if ((poteziIgrac.contains(f11) && poteziIgrac.contains(f21)) &&
                !poteziRacunalo.contains(f01) && neiskoristeno.contains(f01)) {

                makeNextMove(f01)
            }
            else if ((poteziIgrac.contains(f11) && poteziIgrac.contains(f12)) &&
                !poteziRacunalo.contains(f10) && neiskoristeno.contains(f10)) {

                makeNextMove(f10)
            }
            else if ((poteziIgrac.contains(f11) && poteziIgrac.contains(f10)) &&
                !poteziRacunalo.contains(f12) && neiskoristeno.contains(f12)) {

                makeNextMove(f12)
            }
            else if ((poteziIgrac.contains(f11) && poteziIgrac.contains(f01)) &&
                !poteziRacunalo.contains(f21) && neiskoristeno.contains(f21)) {

                makeNextMove(f21)
            }
            else if ((poteziIgrac.contains(f00) && poteziIgrac.contains(f02)) &&
                !poteziRacunalo.contains(f01) && neiskoristeno.contains(f01)) {

                makeNextMove(f01)
            }
            else if ((poteziIgrac.contains(f00) && poteziIgrac.contains(f20)) &&
                !poteziRacunalo.contains(f10) && neiskoristeno.contains(f10)) {

                makeNextMove(f10)
            }
            else if ((poteziIgrac.contains(f22) && poteziIgrac.contains(f02)) &&
                !poteziRacunalo.contains(f12) && neiskoristeno.contains(f12)) {

                makeNextMove(f12)
            }
            else if ((poteziIgrac.contains(f20) && poteziIgrac.contains(f22)) &&
                !poteziRacunalo.contains(f21) && neiskoristeno.contains(f21)) {

                makeNextMove(f21)
            }
            // inače, ako imaš sredinu i protivnički su u 2 nasuprotna kuta, stavi u ne-kutni
            else if ((poteziIgrac.contains(f00) && poteziIgrac.contains(f22)) ||
                (poteziIgrac.contains(f02) && poteziIgrac.contains(f22)) &&
                !poteziRacunalo.contains(f11)) {

                if (neiskoristeno.contains(f01)) {
                    makeNextMove(f21)
                }
                else if (neiskoristeno.contains(f10)) {
                    makeNextMove(f10)
                }
                else if (neiskoristeno.contains(f12)) {
                    makeNextMove(f12)
                }
                else if (neiskoristeno.contains(f21)) {
                    makeNextMove(f21)
                }
                else {
                    makeNextMove(neiskoristeno.last())
                }
            }
            // rubni slučajevi (konjički skok)
            else if (poteziIgrac.contains(f00) && poteziIgrac.contains(f12) &&
                neiskoristeno.contains(f01) && neiskoristeno.contains(f02) ) {

                makeNextMove(f01)
            }
            else if (poteziIgrac.contains(f00) && poteziIgrac.contains(f21) &&
                neiskoristeno.contains(f10) && neiskoristeno.contains(f20) ) {

                makeNextMove(f10)
            }
            else if (poteziIgrac.contains(f02) && poteziIgrac.contains(f21) &&
                neiskoristeno.contains(f12) && neiskoristeno.contains(f22) ) {

                makeNextMove(f12)
            }
            else if (poteziIgrac.contains(f02) && poteziIgrac.contains(f10) &&
                neiskoristeno.contains(f00) && neiskoristeno.contains(f01) ) {

                makeNextMove(f00)
            }
            else if (poteziIgrac.contains(f20) && poteziIgrac.contains(f01) &&
                neiskoristeno.contains(f10) && neiskoristeno.contains(f00) ) {

                makeNextMove(f10)
            }
            else if (poteziIgrac.contains(f20) && poteziIgrac.contains(f12) &&
                neiskoristeno.contains(f21) && neiskoristeno.contains(f22) ) {

                makeNextMove(f22)
            }
            else if (poteziIgrac.contains(f22) && poteziIgrac.contains(f01) &&
                neiskoristeno.contains(f12) && neiskoristeno.contains(f02) ) {

                makeNextMove(f12)
            }
            else if (poteziIgrac.contains(f22) && poteziIgrac.contains(f10) &&
                neiskoristeno.contains(f21) && neiskoristeno.contains(f22) ) {

                makeNextMove(f22)
            }
            else {
                makeNextMove(neiskoristeno.last())
            }
        }
    }

    private fun makeNextMove (iv: ImageView) {
        iv.setImageResource(racunaloXO.ikona)
        neiskoristeno.remove(iv)
        poteziRacunalo.add(iv)

        if (!isWin(poteziRacunalo) && neiskoristeno.size == 0) {
            Toast.makeText(this, "NEMA POBJEDNIKA", Toast.LENGTH_SHORT).show()
        }
    }

    private fun isWin(f: MutableSet<ImageView>) : Boolean {

        if (f.contains(f00) && f.contains(f01) && f.contains(f02)) {
            setWinColor(f00, f01, f02)
            return true
        }
        else if (f.contains(f10) && f.contains(f11) && f.contains(f12)) {
            setWinColor(f10, f11, f12)
            return true
        }
        else if (f.contains(f20) && f.contains(f21) && f.contains(f22)) {
            setWinColor(f20, f21, f22)
            return true
        }
        else if (f.contains(f00) && f.contains(f10) && f.contains(f20)) {
            setWinColor(f00, f10, f20)
            return true
        }
        else if (f.contains(f01) && f.contains(f11) && f.contains(f21)) {
            setWinColor(f01, f11, f21)
            return true
        }
        else if (f.contains(f02) && f.contains(f12) && f.contains(f22)) {
            setWinColor(f02, f12, f22)
            return true
        }
        else if (f.contains(f00) && f.contains(f11) && f.contains(f22)) {
            setWinColor(f00, f11, f22)
            return true
        }
        else if (f.contains(f02) && f.contains(f11) && f.contains(f20)) {
            setWinColor(f02, f11, f20)
            return true
        }

        return false
    }

    private fun setWinColor(f1: ImageView, f2: ImageView, f3: ImageView) {
        pobjeda = true
        f1.setBackgroundColor(Color.GREEN)
        f2.setBackgroundColor(Color.GREEN)
        f3.setBackgroundColor(Color.GREEN)
    }
}