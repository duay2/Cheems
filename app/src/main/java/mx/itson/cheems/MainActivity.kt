package mx.itson.cheems

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import android.util.Log
import android.view.View
import android.widget.ImageButton
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity(), View.OnClickListener {

    var gameOverCard = 0
    val selectedCards = mutableListOf<Int>() // Lista para rastrear las cartas seleccionadas correctamente

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        start()
    }

    fun start() {
        selectedCards.clear() // Limpiar la lista de cartas seleccionadas al iniciar el juego
        for(i in 1..6){
            val btnCard = findViewById<View>(
                resources.getIdentifier("card$i", "id", this.packageName)
            ) as ImageButton
            btnCard.setOnClickListener(this)
            btnCard.setBackgroundResource(R.drawable.icon_pregunta)
        }
        gameOverCard = (1 .. 6).random()

        Log.d("El valor de la carta", "La carta perdedora es ${gameOverCard.toString()}")
    }

    fun flip(card : Int){
        if(card == gameOverCard){
            // Ya perdió

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S){
                //Si la version de android de el usuario es mayor a la version 12 entonces
                val vibratorAdmin = applicationContext.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager
                val vibrator = vibratorAdmin.defaultVibrator
                vibrator.vibrate(VibrationEffect.createOneShot(1000, VibrationEffect.DEFAULT_AMPLITUDE))
            }else{
                //Si es menor a la 12 entonces
                val vibrator = applicationContext.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
                vibrator.vibrate(1000)
            }

            Toast.makeText(this, getString(R.string.text_game_over), Toast.LENGTH_LONG).show()

            for(i in 1..6){
                val btnCard = findViewById<View>(
                    resources.getIdentifier("card$i", "id", this.packageName)
                ) as ImageButton
                if(i == card) {
                    btnCard.setBackgroundResource(R.drawable.icon_chempe)
                }else {
                    btnCard.setBackgroundResource(R.drawable.icon_cheems)
                }
            }
        } else {
            // Continúa en el juego
            val btnCard = findViewById<View>(
                resources.getIdentifier("card$card", "id", this.packageName)
            ) as ImageButton
            btnCard.setBackgroundResource(R.drawable.icon_cheems)
            selectedCards.add(card) // Añadir la carta seleccionada a la lista

            // Verificar si el usuario ha seleccionado todas las cartas seguras
            if (selectedCards.size == 5) {
                Toast.makeText(this, getString(R.string.text_you_win), Toast.LENGTH_LONG).show()

                start() // Reiniciar el juego después de ganar
            }
        }
    }

    override fun onClick(v: View) {
        when(v.id){
            R.id.card1 -> { flip(1)}
            R.id.card2 -> { flip(2)}
            R.id.card3 -> { flip(3)}
            R.id.card4 -> { flip(4)}
            R.id.card5 -> { flip(5)}
            R.id.card6 -> { flip(6)}
        }
    }
}