package com.example.debugghard


import android.os.Bundle
import android.os.Debug
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Alignment
import com.example.debugghard.ui.theme.DebuggHardTheme //hibas név (ToDebugTheme -> DebuggHardTheme)

data class Task(val id: Int, val title: String, var done: Boolean)

class MainActivity : ComponentActivity() { //zárójelek ComponentActivity()
    override fun onCreate(savedInstanceState: Bundle?) { //elírás SavedInstanceState
        super.onCreate(savedInstanceState)
        setContent {
            DebuggHardTheme { //hibás név ToDebugTheme -> DebuggHardTheme
                Surface(color = MaterialTheme.colorScheme.background) {
                    TodoApp()
                }
            }
        }
    }
}

@Composable
fun TodoApp() {
    var newTaskText = remember { mutableStateOf("") }
    var tasks = remember { mutableStateListOf<Task>() }
    var message = remember { mutableStateOf("Üdv a teendőlistában!") }
    var idCounter by remember { mutableIntStateOf(0) } //idCounter változó definiálva

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally //Center -> CenterHorizontally
    ) {
        Spacer(Modifier.height(16.dp)) //hiányzó Spacer

        Text("Teendőlista", style = MaterialTheme.typography.headlineSmall)

        Spacer(Modifier.height(8.dp))

        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = newTaskText.value, //value = newTaskText -> value = newTaskText.value
                onValueChange = { value -> newTaskText.value = value },
                label = { Text("Új teendő") },
                modifier = Modifier.weight(0.7f)
            )
            Button(
                onClick = {
                    if (newTaskText.value == "") {
                        message.value = "Adj meg egy teendőt!"
                    } else {
                        val newTask = Task(idCounter, newTaskText.value, false)
                        idCounter++
                        tasks.add(newTask)
                        message.value = "Hozzáadva: ${newTask.title}"
                        newTaskText.value = "" //egyenlőségjel kivéve, nem törölte fieldet
                    }
                },
                modifier = Modifier.weight(0.3f)
            ) {
                Text("Hozzáadás")
            }
        }

        Spacer(Modifier.height(12.dp))

        Text(message.value) //message -> message.value

        Spacer(Modifier.height(12.dp))

        LazyColumn(
            modifier = Modifier.fillMaxWidth().height(300.dp)
        ) {
            items(tasks) { task ->
                Row(
                    Modifier.fillMaxWidth().padding(4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Checkbox(
                            checked = task.done, //checked = false -> checked = task.done
                            onCheckedChange = { isChecked ->
                                val taskIndex = tasks.indexOf(task)
                                if (taskIndex != -1) {
                                    tasks[taskIndex] = tasks[taskIndex].copy(done = isChecked)
                                }
                            }//onCheckedChange teljes logikája átírva
                        )
                        Text(task.title, style = MaterialTheme.typography.bodyLarge)
                    }
                    Button(onClick = {
                        tasks.remove(task) //tasks.removeAt(0) -> tasks.remove(task)
                        message.value = "Törölve: ${task.title}"
                    }) {
                        Text("Törlés")
                    }
                }
            }
        }

        Spacer(Modifier.height(8.dp))

        Button(onClick = {
            tasks.clear()
            message.value = "Minden törölve"
        },
            enabled = tasks.isNotEmpty() //enabled = tasks.size < 0 -> enabled = tasks.isNotEmpty()
        ) {
            Text("Mindent töröl")
        }

        Spacer(Modifier.height(12.dp))

        val completed = tasks.count { it.done }
        val remaining = tasks.size // count helyett az összes elem száma kell
        Text("Kész: ${completed}/${remaining}") //két változó felcserélve
    }
}