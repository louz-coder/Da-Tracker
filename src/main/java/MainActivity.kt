package com.datracker.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DATrackerTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MainDashboardScreen()
                }
            }
        }
    }
}

@Composable
fun DATrackerTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = darkColorScheme(
            primary = Color(0xFF10B981),
            background = Color(0xFF0F172A),
            surface = Color(0xFF1E293B)
        ),
        content = content
    )
}

data class Account(val name: String, val balance: Double, val color: Color)
data class Transaction(val title: String, val category: String, val amount: Double, val type: String, val date: String)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainDashboardScreen() {
    val accounts = remember {
        listOf(
            Account("Cash", 45000.0, Color(0xFF10B981)),
            Account("Main Bank", 185000.0, Color(0xFF3B82F6)),
            Account("Savings Account", 120000.0, Color(0xFFF59E0B))
        )
    }

    val transactions = remember {
        listOf(
            Transaction("Monthly Salary", "Salary", 150000.0, "INCOME", "Today, 09:00"),
            Transaction("Supermarket", "Groceries", 12500.0, "EXPENSE", "Yesterday, 17:30"),
            Transaction("ATM Withdrawal", "Transfer", 20000.0, "TRANSFER", "02 Nov 2026")
        )
    }

    val totalBalance = accounts.sumOf { it.balance }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("DA Tracker", fontWeight = FontWeight.Bold) },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFF0F172A))
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { /* Open Denomination Keypad */ },
                containerColor = Color(0xFF10B981)
            ) {
                Text("+", fontSize = 24.sp, color = Color.White, fontWeight = FontWeight.Bold)
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            // Total Balance Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF1E293B)),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text("Total Net Worth", color = Color.Gray, fontSize = 14.sp)
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "${String.format("%,.2f", totalBalance)} DA",
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Account Cards
            Text("Accounts", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.White)
            Spacer(modifier = Modifier.height(10.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                accounts.forEach { account ->
                    Card(
                        modifier = Modifier.weight(1f),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFF1E293B))
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text(account.name, fontSize = 12.sp, color = Color.Gray)
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                "${account.balance.toInt()} DA",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = account.color
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Recent Transactions Header
            Text("Recent Activity", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.White)
            Spacer(modifier = Modifier.height(10.dp))

            LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                items(transactions) { tx ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFF1E293B))
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(14.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(tx.title, fontWeight = FontWeight.Medium, color = Color.White)
                                Text("${tx.category} • ${tx.date}", fontSize = 12.sp, color = Color.Gray)
                            }
                            Text(
                                text = (if (tx.type == "EXPENSE") "-" else "+") + "${tx.amount.toInt()} DA",
                                fontWeight = FontWeight.Bold,
                                color = when (tx.type) {
                                    "INCOME" -> Color(0xFF10B981)
                                    "EXPENSE" -> Color(0xFFEF4444)
                                    else -> Color(0xFF3B82F6)
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}
