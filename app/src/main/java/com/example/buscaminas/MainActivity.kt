package com.example.buscaminas

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.buscaminas.ui.theme.BuscaminasTheme
import java.util.Random

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BuscaminasTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier
                        .fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Column {
                        encabezado()
                        tablero()
                    }

                }
            }
        }
    }
}

@Composable
fun encabezado(modifier: Modifier = Modifier) {
    Column(modifier = Modifier
        .background(Color.Black)
        .fillMaxWidth()
        .fillMaxHeight(0.1F), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
        Text(text = "Buscaminas", modifier = modifier, style = TextStyle(fontSize = 24.sp), color = Color.White)
    }
}

@Composable
fun tablero(modifier: Modifier = Modifier) {

    val columns = 6
    val filas = 10

    var estadoBotones = remember {
        List(filas * columns) { mutableStateOf(true) }
    }

    var minas = remember {
        List(filas * columns) { mutableStateOf(asignaMina()) }
    }

    var verAlerta = remember {
        mutableStateOf(false)
    }

    if(verAlerta.value){
        AlertDialog(onDismissRequest = {
            verAlerta.value = false
            }, title = { Text(text = "Perdiste") }, text = { Text(text = "Encontraste una mina")
        }, confirmButton = {
            Button(onClick = {
                verAlerta.value = false
                estadoBotones.forEach { it.value = true }
                minas.forEach { it.value = asignaMina() }
            }) {
                Text(text = "Reiniciar juego")
            }
        })
    }

    Column(modifier = Modifier
        .fillMaxSize()
        .padding(4.dp), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
        for(i in 0 until filas) {
            Row(modifier = Modifier
                .fillMaxSize()
                .weight(.1f)
                .padding(1.dp)) {
                for(j in 0 until columns) {
                    val index = i * columns + j
                    Button(onClick = {
                                        // estadoBotones[index].value = !estadoBotones[index].value
                                        if(minas[index].value){
                                            for (m in minas.indices) {
                                                if (minas[m].value) {
                                                    estadoBotones[m].value = false
                                                }
                                            }
                                            verAlerta.value = true
                                        } else {
                                            activaCasillas(i, j, estadoBotones, minas, filas, columns)
                                        }
                                     }, modifier = Modifier
                        .fillMaxSize()
                        .weight(.1f)
                        .padding(1.dp),
                        shape = RectangleShape, enabled = estadoBotones[index].value){
                        Text(text = if (!estadoBotones[index].value && minas[index].value) "O" else "X", modifier = Modifier.fillMaxSize(), style = TextStyle(fontSize = 24.sp, textAlign = TextAlign.Center),
                            color = if (!estadoBotones[index].value && minas[index].value) Color.Red else Color.Black)
                    }
                }
            }
        }
    }


}

fun asignaMina() : Boolean {
    val numeroRandom = Random().nextInt(10)
    return numeroRandom > 7
}

fun activaCasillas(i: Int, j: Int, estadoBotones: List<MutableState<Boolean>>, minas: List<MutableState<Boolean>>, filas: Int, columns: Int) {
    val vecinos = listOf(-1, 0, 1)
    for (di in vecinos) {
        for (dj in vecinos) {
            val ni = i + di
            val nj = j + dj
            val index = ni * columns + nj
            if (ni in 0 until filas && nj in 0 until columns && estadoBotones[index].value && !minas[index].value) {
                estadoBotones[index].value = false
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    BuscaminasTheme {
        Column {
            encabezado()
            tablero()
        }
    }
}