package com.example.camdrive

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val Orange = Color(0xFFFF6900)
private val Ink = Color(0xFF202124)
private val Muted = Color(0xFF74777C)
private val SurfaceBg = Color(0xFFF5F6F7)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { CamDriveApp() }
    }
}

data class MotionEvent(val time: String, val date: String, val title: String, val status: String)

@Composable
fun CamDriveApp() {
    var tab by remember { mutableStateOf(0) }
    var selectedEvent by remember { mutableStateOf<MotionEvent?>(null) }
    val events = listOf(
        MotionEvent("20:57:12", "Today · 21 Oct", "Motion detected", "Demo event · no video attached"),
        MotionEvent("19:42:08", "Today · 21 Oct", "Motion detected", "Demo event · no video attached"),
        MotionEvent("17:18:51", "Today · 21 Oct", "Motion detected", "Demo event · no video attached"),
        MotionEvent("08:06:24", "Today · 21 Oct", "Motion detected", "Demo event · no video attached")
    )
    MaterialTheme(colorScheme = lightColorScheme(primary = Orange, background = SurfaceBg, surface = Color.White)) {
        Scaffold(
            containerColor = SurfaceBg,
            bottomBar = {
                NavigationBar(containerColor = Color.White) {
                    NavigationBarItem(selected = tab == 0, onClick = { tab = 0; selectedEvent = null }, icon = { Icon(Icons.Default.Home, null) }, label = { Text("Home") })
                    NavigationBarItem(selected = tab == 1, onClick = { tab = 1; selectedEvent = null }, icon = { Icon(Icons.Default.VideoLibrary, null) }, label = { Text("Events") })
                    NavigationBarItem(selected = tab == 2, onClick = { tab = 2; selectedEvent = null }, icon = { Icon(Icons.Default.Cloud, null) }, label = { Text("Cloud") })
                    NavigationBarItem(selected = tab == 3, onClick = { tab = 3; selectedEvent = null }, icon = { Icon(Icons.Default.Settings, null) }, label = { Text("Settings") })
                }
            }
        ) { padding ->
            Column(Modifier.fillMaxSize().padding(padding)) {
                Row(Modifier.fillMaxWidth().background(Color.White).padding(horizontal = 20.dp, vertical = 18.dp), verticalAlignment = Alignment.CenterVertically) {
                    Column(Modifier.weight(1f)) {
                        Text("CamDrive", fontSize = 25.sp, fontWeight = FontWeight.Bold, color = Ink)
                        Text("Smart camera · Google Drive", fontSize = 12.sp, color = Muted)
                    }
                    Box(Modifier.size(42.dp).background(Color(0xFFFFE8DA), CircleShape), contentAlignment = Alignment.Center) {
                        Icon(Icons.Default.Videocam, contentDescription = null, tint = Orange)
                    }
                }
                when (tab) {
                    0 -> HomeScreen(events.size) { tab = 1 }
                    1 -> if (selectedEvent == null) EventsScreen(events) { selectedEvent = it } else EventDetail(selectedEvent!!) { selectedEvent = null }
                    2 -> CloudScreen()
                    else -> SettingsScreen()
                }
            }
        }
    }
}

@Composable
fun HomeScreen(eventCount: Int, onEvents: () -> Unit) {
    Column(Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
        Card(shape = RoundedCornerShape(22.dp), colors = CardDefaults.cardColors(containerColor = Color.White)) {
            Column(Modifier.fillMaxWidth().padding(18.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Videocam, null, tint = Orange, modifier = Modifier.size(28.dp))
                    Spacer(Modifier.width(10.dp))
                    Column(Modifier.weight(1f)) {
                        Text("Living Room · C300", fontWeight = FontWeight.SemiBold, fontSize = 17.sp)
                        Text("Camera connection not configured", color = Muted, fontSize = 12.sp)
                    }
                    Surface(color = Color(0xFFFFF0E7), shape = RoundedCornerShape(50)) { Text("SETUP", Modifier.padding(horizontal = 10.dp, vertical = 5.dp), color = Orange, fontSize = 10.sp, fontWeight = FontWeight.Bold) }
                }
                Box(Modifier.fillMaxWidth().height(168.dp).background(Color(0xFF25282C), RoundedCornerShape(16.dp)), contentAlignment = Alignment.Center) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(Icons.Default.VideocamOff, null, tint = Color.White.copy(alpha = .85f), modifier = Modifier.size(42.dp))
                        Spacer(Modifier.height(8.dp))
                        Text("Live view unavailable", color = Color.White, fontWeight = FontWeight.Medium)
                        Text("Camera stream integration required", color = Color.LightGray, fontSize = 11.sp)
                    }
                }
                Text("This prototype does not connect to the C300 yet.", color = Muted, fontSize = 12.sp)
            }
        }
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            StatCard("Motion events", eventCount.toString(), Icons.Default.Sensors, Modifier.weight(1f))
            StatCard("Drive storage", "Not linked", Icons.Default.CloudUpload, Modifier.weight(1f))
        }
        Card(shape = RoundedCornerShape(20.dp), colors = CardDefaults.cardColors(containerColor = Color.White), modifier = Modifier.fillMaxWidth().clickable { onEvents() }) {
            Row(Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.History, null, tint = Orange, modifier = Modifier.size(26.dp))
                Spacer(Modifier.width(12.dp))
                Column(Modifier.weight(1f)) { Text("Recent motion events", fontWeight = FontWeight.SemiBold); Text("Review by date and time", color = Muted, fontSize = 12.sp) }
                Icon(Icons.Default.ChevronRight, null, tint = Muted)
            }
        }
        Text("Prototype mode · sample events only", color = Muted, fontSize = 11.sp)
    }
}

@Composable
fun StatCard(title: String, value: String, icon: androidx.compose.ui.graphics.vector.ImageVector, modifier: Modifier = Modifier) {
    Card(modifier, shape = RoundedCornerShape(18.dp), colors = CardDefaults.cardColors(containerColor = Color.White)) {
        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(9.dp)) {
            Icon(icon, null, tint = Orange)
            Text(value, fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Ink)
            Text(title, color = Muted, fontSize = 12.sp)
        }
    }
}

@Composable
fun EventsScreen(events: List<MotionEvent>, onOpen: (MotionEvent) -> Unit) {
    Column(Modifier.fillMaxSize().padding(16.dp)) {
        Text("Event history", fontSize = 23.sp, fontWeight = FontWeight.Bold, color = Ink)
        Text("Choose a timestamp to inspect an event", color = Muted, fontSize = 13.sp, modifier = Modifier.padding(top = 4.dp, bottom = 14.dp))
        LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            items(events) { event ->
                Card(Modifier.fillMaxWidth().clickable { onOpen(event) }, shape = RoundedCornerShape(16.dp), colors = CardDefaults.cardColors(containerColor = Color.White)) {
                    Row(Modifier.padding(15.dp), verticalAlignment = Alignment.CenterVertically) {
                        Box(Modifier.size(46.dp).background(Color(0xFFFFEEE4), RoundedCornerShape(12.dp)), contentAlignment = Alignment.Center) { Icon(Icons.Default.Sensors, null, tint = Orange) }
                        Spacer(Modifier.width(12.dp))
                        Column(Modifier.weight(1f)) {
                            Text(event.title, fontWeight = FontWeight.SemiBold)
                            Text(event.date, color = Muted, fontSize = 12.sp)
                            Text(event.status, color = Muted, fontSize = 11.sp)
                        }
                        Text(event.time, fontWeight = FontWeight.SemiBold, fontSize = 13.sp)
                    }
                }
            }
        }
    }
}

@Composable
fun EventDetail(event: MotionEvent, onBack: () -> Unit) {
    Column(Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
        TextButton(onClick = onBack) { Icon(Icons.Default.ArrowBack, null); Spacer(Modifier.width(6.dp)); Text("Back to events") }
        Text(event.title, fontSize = 24.sp, fontWeight = FontWeight.Bold)
        Text("${event.date} · ${event.time}", color = Muted)
        Box(Modifier.fillMaxWidth().height(210.dp).background(Color(0xFF25282C), RoundedCornerShape(18.dp)), contentAlignment = Alignment.Center) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) { Icon(Icons.Default.PlayCircle, null, tint = Color.White, modifier = Modifier.size(48.dp)); Text("No video attached", color = Color.White, fontWeight = FontWeight.Medium); Text("Playback will work after Drive integration", color = Color.LightGray, fontSize = 11.sp) }
        }
        Text("Event details", fontWeight = FontWeight.Bold)
        Text("Timestamp: ${event.date} ${event.time}\nSource: Demo data\nCloud file: Not available", color = Muted, fontSize = 13.sp)
    }
}

@Composable
fun CloudScreen() {
    Column(Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
        Text("Google Drive", fontSize = 23.sp, fontWeight = FontWeight.Bold)
        Card(shape = RoundedCornerShape(20.dp), colors = CardDefaults.cardColors(containerColor = Color.White)) {
            Column(Modifier.fillMaxWidth().padding(18.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Icon(Icons.Default.CloudQueue, null, tint = Orange, modifier = Modifier.size(34.dp))
                Text("Connect your Drive", fontWeight = FontWeight.SemiBold, fontSize = 18.sp)
                Text("Drive sign-in and uploads are not implemented in this prototype. The production app should use Google's OAuth consent flow and request only the required file access.", color = Muted, fontSize = 13.sp)
                Button(onClick = {}, enabled = false, modifier = Modifier.fillMaxWidth()) { Text("Connect Google Drive · Coming next") }
            }
        }
        Text("Planned folder: CamDrive / C300 / YYYY-MM-DD", color = Muted, fontSize = 12.sp)
    }
}

@Composable
fun SettingsScreen() {
    Column(Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text("Settings", fontSize = 23.sp, fontWeight = FontWeight.Bold)
        SettingRow(Icons.Default.CameraAlt, "Camera source", "Xiaomi Smart Camera C300 · not connected")
        SettingRow(Icons.Default.Cloud, "Cloud destination", "Google Drive · not connected")
        SettingRow(Icons.Default.Wifi, "Upload policy", "Wi-Fi only · planned")
        SettingRow(Icons.Default.Schedule, "Clip duration", "Configurable after video source integration")
        SettingRow(Icons.Default.DeleteSweep, "Retention", "Keep forever by default · planned")
        Spacer(Modifier.height(6.dp))
        Text("No Xiaomi credentials are collected by this prototype.", color = Muted, fontSize = 12.sp)
    }
}

@Composable
fun SettingRow(icon: androidx.compose.ui.graphics.vector.ImageVector, title: String, subtitle: String) {
    Card(shape = RoundedCornerShape(16.dp), colors = CardDefaults.cardColors(containerColor = Color.White)) {
        Row(Modifier.fillMaxWidth().padding(15.dp), verticalAlignment = Alignment.CenterVertically) {
            Icon(icon, null, tint = Orange)
            Spacer(Modifier.width(12.dp))
            Column { Text(title, fontWeight = FontWeight.SemiBold); Text(subtitle, color = Muted, fontSize = 12.sp) }
        }
    }
}
