package com.example.debugghard


import android.os.Bundle
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
import com.example.tododebug.ui.theme.TodoDebugTheme

data class Task(val id: Int, val title: String, var done: Boolean)

class MainActivity : ComponentActivity {
    override fun onCreate(savedInstaceState: Bundle?) {
        super.onCreate(savedInstaceState)
        setContent {
            TodoDebugTheme {
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
    var idCounter = 0

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Center
    ) {
        Text("Teendőlista", style = MaterialTheme.typography.headlineSmall)

        Spacer(Modifier.height(8.dp))

        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = newTaskText,
                onValueChange = { value -> newTaskText = value },
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
                        newTaskText.value == ""
                    }
                },
                modifier = Modifier.weight(0.3f)
            ) {
                Text("Hozzáadás")
            }
        }

        Spacer(Modifier.height(12.dp))

        Text(message)

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
                            checked = false,
                            onCheckedChange = {
                                task.done == !task.done
                            }
                        )
                        Text(task.title, style = MaterialTheme.typography.bodyLarge)
                    }
                    Button(onClick = {
                        tasks.removeAt(0)
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
            enabled = tasks.size < 0
        ) {
            Text("Mindent töröl")
        }

        Spacer(Modifier.height(12.dp))

        val completed = tasks.count { it.done }
        val remaining = tasks.count { !it.done }
        Text("Kész: ${remaining}/${completed}")
    }
}