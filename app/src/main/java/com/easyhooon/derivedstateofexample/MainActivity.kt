package com.easyhooon.derivedstateofexample

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.easyhooon.derivedstateofexample.ui.theme.DerivedStateOfExampleTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DerivedStateOfExampleTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    ComparisonExample(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun ComparisonExample(modifier: Modifier = Modifier) {
    var selectedTab by remember { mutableIntStateOf(0) }
    val tabs = listOf("DerivedStateOf", "Remember + Key", "직접 접근")

    Column(modifier = modifier.fillMaxSize()) {
        TabRow(selectedTabIndex = selectedTab) {
            tabs.forEachIndexed { index, title ->
                Tab(
                    selected = selectedTab == index,
                    onClick = { selectedTab = index },
                    text = { Text(title) }
                )
            }
        }
        
        when (selectedTab) {
            0 -> DerivedStateOfExample()
            1 -> RememberKeyExample()
            2 -> DirectAccessExample()
        }
    }
}

@Composable
fun DerivedStateOfExample() {
    val lazyListState = rememberLazyListState()
    
    // derivedStateOf를 사용하여 스크롤 위치에 따른 상태 최적화
    val isButtonVisible by remember {
        derivedStateOf {
            Log.d("DerivedStateOf", "derivedStateOf 계산됨 - firstVisibleItemIndex: ${lazyListState.firstVisibleItemIndex}")
            lazyListState.firstVisibleItemIndex >= 10
        }
    }
    
    Log.d("DerivedStateOf", "DerivedStateOfExample Recomposition 발생!")
    
    Box(modifier = Modifier.fillMaxSize()) {
        Column {
            Text(
                text = "DerivedStateOf 방식 - Logcat 확인",
                modifier = Modifier.padding(16.dp),
                color = MaterialTheme.colorScheme.primary,
                fontSize = 16.sp
            )
            
            LazyColumn(
                state = lazyListState,
                modifier = Modifier.weight(1f)
            ) {
                items(100) { index ->
                    ItemCard(index = index, type = "DerivedStateOf")
                }
            }
        }
        
        if (isButtonVisible) {
            Button(
                onClick = { /* 버튼 클릭 동작 */ },
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(16.dp)
            ) {
                Text("10번 아이템 이후 표시되는 버튼")
            }
        }
    }
}

@Composable
fun RememberKeyExample() {
    val lazyListState = rememberLazyListState()
    
    // remember + key 방식
    val isButtonVisible = remember(lazyListState.firstVisibleItemIndex >= 10) {
        Log.d("RememberKey", "remember 계산됨 - key: ${lazyListState.firstVisibleItemIndex >= 10}")
        lazyListState.firstVisibleItemIndex >= 10
    }
    
    Log.d("RememberKey", "RememberKeyExample Recomposition 발생!")
    
    Box(modifier = Modifier.fillMaxSize()) {
        Column {
            Text(
                text = "Remember + Key 방식 - Logcat 확인",
                modifier = Modifier.padding(16.dp),
                color = Color(0xFF4CAF50),
                fontSize = 16.sp
            )
            
            LazyColumn(
                state = lazyListState,
                modifier = Modifier.weight(1f)
            ) {
                items(100) { index ->
                    ItemCard(index = index, type = "RememberKey")
                }
            }
        }
        
        if (isButtonVisible) {
            Button(
                onClick = { /* 버튼 클릭 동작 */ },
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(16.dp)
            ) {
                Text("10번 아이템 이후 표시되는 버튼")
            }
        }
    }
}

@Composable
fun DirectAccessExample() {
    val lazyListState = rememberLazyListState()
    
    // 직접 접근 방식 - 매번 재계산됨
    val isButtonVisible = lazyListState.firstVisibleItemIndex >= 10
    
    Log.d("DirectAccess", "DirectAccessExample Recomposition 발생! - firstVisibleItemIndex: ${lazyListState.firstVisibleItemIndex}")
    
    Box(modifier = Modifier.fillMaxSize()) {
        Column {
            Text(
                text = "직접 접근 방식 - Logcat 확인",
                modifier = Modifier.padding(16.dp),
                color = MaterialTheme.colorScheme.error,
                fontSize = 16.sp
            )
            
            LazyColumn(
                state = lazyListState,
                modifier = Modifier.weight(1f)
            ) {
                items(100) { index ->
                    ItemCard(index = index, type = "DirectAccess")
                }
            }
        }
        
        if (isButtonVisible) {
            Button(
                onClick = { /* 버튼 클릭 동작 */ },
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(16.dp)
            ) {
                Text("10번 아이템 이후 표시되는 버튼")
            }
        }
    }
}

@Composable
fun ItemCard(index: Int, type: String) {
    var itemRecompositionCount by remember { mutableIntStateOf(0) }
    itemRecompositionCount++
    
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "아이템 $index",
                fontSize = 18.sp
            )
            Text(
                text = "$type 방식 - 재구성: $itemRecompositionCount",
                fontSize = 12.sp,
                color = Color.Gray
            )
        }
    }
}