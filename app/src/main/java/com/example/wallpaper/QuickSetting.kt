package com.example.wallpaper

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Snackbar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

class QuickSetting {

}

@Composable
fun Setting(name: String, onClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(16.dp)
    ) {
        Text(text = name, style = MaterialTheme.typography.bodySmall)
        Spacer(modifier = Modifier.height(4.dp))
    }
}

@Composable
fun QuickSettingsScreen() {
    var selectedSetting by remember { mutableStateOf("") }

    LazyColumn {
        items(10) { index ->
            Setting(
                name = "Setting $index",
                onClick = { selectedSetting = "Setting $index clicked" }
            )
        }
    }

    if (selectedSetting.isNotEmpty()) {
        // 선택한 항목에 대한 추가 동작 또는 다이얼로그를 표시할 수 있습니다.
        // 예: Toast 또는 Snackbar 표시
        Snackbar(
            modifier = Modifier
                .padding(16.dp)
                .background(Color.Gray), // 배경색 설정
            contentColor = Color.White,
            content = { Text(text = selectedSetting) }
        )
    }
}