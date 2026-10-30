
package com.example.personalfinancetracker.ui.screens

import android.content.res.Configuration
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.personalfinancetracker.data.local.Transaction
import com.example.personalfinancetracker.ui.FinanceViewModel
import java.text.NumberFormat
import java.util.Locale

private val Blue = Color(0xFF1565C0)
private val Green = Color(0xFF15803D)
private val Red = Color(0xFFDC2626)
private val LightGreen = Color(0xFFE6F6EA)
private val LightRed = Color(0xFFFDE9E9)
private val PageBackground = Color(0xFFF8FAFC)

@Composable
fun HomeScreen(
    vm: FinanceViewModel,
    onAdd: () -> Unit,
    onEdit: (Long) -> Unit,
    onSettings: () -> Unit
) {
    val transactions by vm.transactions.collectAsState()
    val filter by vm.filter.collectAsState()

    val visible = remember(transactions, filter) {
        transactions.filter {
            filter == 0 ||
                    (filter == 1 && it.type == "Орлого") ||
                    (filter == 2 && it.type == "Зарлага")
        }
    }

    var index by remember { mutableIntStateOf(0) }
    var showAmount by remember { mutableStateOf(false) }
    var askToDelete by remember { mutableStateOf(false) }

    val position = index.coerceIn(
        0,
        (visible.size - 1).coerceAtLeast(0)
    )

    val current = visible.getOrNull(position)

    val landscape =
        LocalConfiguration.current.orientation ==
                Configuration.ORIENTATION_LANDSCAPE

    LaunchedEffect(current?.id, filter) {
        showAmount = false
    }

    LaunchedEffect(visible.size, filter) {
        index = index.coerceIn(
            0,
            (visible.size - 1).coerceAtLeast(0)
        )
    }

    Scaffold(
        topBar = {
            FinanceHeader(
                "Санхүү",
                "Тохиргоо",
                onSettings
            )
        }
    ) { inset ->

        val info: @Composable () -> Unit = {
            Column(
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                FilterTitle(filter, position, visible.size)

                TypeLegend(filter)

                TransactionCard(
                    item = current,
                    showAmount = showAmount,
                    onAmountClick = {
                        showAmount = !showAmount
                    },
                    onLongClick = {
                        current?.let { onEdit(it.id) }
                    }
                )
            }
        }

        val actions: @Composable () -> Unit = {
            RecordActions(
                hasRecord = current != null,
                position = position,
                count = visible.size,
                onPrevious = { index = position - 1 },
                onNext = { index = position + 1 },
                onAdd = onAdd,
                onEdit = {
                    current?.let { onEdit(it.id) }
                },
                onDelete = { askToDelete = true }
            )
        }

        if (landscape) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(inset)
                    .background(PageBackground)
                    .padding(16.dp),
                horizontalArrangement =
                    Arrangement.spacedBy(16.dp)
            ) {
                Column(
                    modifier = Modifier
                        .weight(1.4f)
                        .fillMaxHeight()
                        .verticalScroll(rememberScrollState())
                ) {
                    info()
                }

                Column(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement =
                        Arrangement.spacedBy(12.dp)
                ) {
                    actions()
                }
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(inset)
                    .background(PageBackground)
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp),
                verticalArrangement =
                    Arrangement.spacedBy(20.dp)
            ) {
                info()
                actions()
            }
        }
    }

    if (askToDelete && current != null) {
        AlertDialog(
            onDismissRequest = {
                askToDelete = false
            },
            title = {
                Text("Бүртгэлийг устгах уу?")
            },
            text = {
                Text(
                    "${current.name} бүртгэлийг " +
                            "бүрмөсөн устгах уу?"
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        vm.delete(current)
                        askToDelete = false
                    }
                ) {
                    Text("ТИЙМ", color = Red)
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        askToDelete = false
                    }
                ) {
                    Text("ҮГҮЙ")
                }
            }
        )
    }
}

// Тохиргооноос сонгосон шүүлтүүрийн нэр
@Composable
private fun FilterTitle(
    filter: Int,
    position: Int,
    count: Int
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement =
            Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = when (filter) {
                1 -> "Зөвхөн орлого"
                2 -> "Зөвхөн зарлага"
                else -> "Бүх бүртгэл"
            },
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF1E293B)
        )

        Surface(
            shape = RoundedCornerShape(50),
            color = Color(0xFFE2E8F0)
        ) {
            Text(
                text = "${if (count == 0) 0 else position + 1} / $count",
                modifier = Modifier.padding(
                    horizontal = 12.dp,
                    vertical = 6.dp
                ),
                fontSize = 13.sp
            )
        }
    }
}

// Орлого, зарлагын өнгийг ялгаж харуулах хэсэг

@Composable
private fun TypeLegend(filter: Int) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        if (filter == 0 || filter == 1) {
            TypeBox(
                label = "Орлого",
                symbol = "↑",
                background = LightGreen,
                color = Green,
                modifier = Modifier.weight(1f)
            )
        }

        if (filter == 0 || filter == 2) {
            TypeBox(
                label = "Зарлага",
                symbol = "↓",
                background = LightRed,
                color = Red,
                modifier = Modifier.weight(1f)
            )
        }
    }
}


@Composable
private fun TypeBox(
    label: String,
    symbol: String,
    background: Color,
    color: Color,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(14.dp),
        color = background
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement =
                Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = symbol,
                color = color,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = label,
                color = color,
                fontWeight = FontWeight.SemiBold,
                fontSize = 14.sp
            )
        }
    }
}

// Нэг бүртгэлийг харуулах карт
@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun TransactionCard(
    item: Transaction?,
    showAmount: Boolean,
    onAmountClick: () -> Unit,
    onLongClick: () -> Unit
) {
    if (item == null) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color.White
            )
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                verticalArrangement =
                    Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    "Бүртгэл байхгүй",
                    fontWeight = FontWeight.Bold
                )
                Text(
                    "НЭМЭХ товчоор шинэ бүртгэл үүсгэнэ үү.",
                    color = Color.Gray
                )
            }
        }
        return
    }

    val isIncome = item.type == "Орлого"
    val mainColor = if (isIncome) Green else Red
    val lightColor = if (isIncome) LightGreen else LightRed

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .combinedClickable(
                onClick = {},
                onLongClick = onLongClick
            ),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 3.dp
        )
    ) {
        Column(
            modifier = Modifier.padding(18.dp),
            verticalArrangement =
                Arrangement.spacedBy(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    shape = RoundedCornerShape(50),
                    color = lightColor
                ) {
                    Text(
                        text = if (isIncome) "↑" else "↓",
                        modifier = Modifier.padding(12.dp),
                        color = mainColor,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(Modifier.width(12.dp))

                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = item.name,
                        fontSize = 19.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Text(
                        text = item.date,
                        fontSize = 13.sp,
                        color = Color.Gray
                    )
                }

                Surface(
                    shape = RoundedCornerShape(50),
                    color = lightColor
                ) {
                    Text(
                        text = item.type,
                        modifier = Modifier.padding(
                            horizontal = 10.dp,
                            vertical = 6.dp
                        ),
                        color = mainColor,
                        fontSize = 12.sp
                    )
                }
            }

            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onAmountClick() },
                shape = RoundedCornerShape(12.dp),
                color = lightColor
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment =
                        Alignment.CenterHorizontally
                ) {
                    Text(
                        text = if (showAmount) {
                            val amount =
                                NumberFormat.getNumberInstance(
                                    Locale.US
                                ).format(item.amount)

                            "${if (isIncome) "+" else "-"} $amount ₮"
                        } else {
                            "**** ₮"
                        },
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = mainColor
                    )

                    Spacer(Modifier.height(4.dp))

                    Text(
                        text = "Дүн дээр дарж харах / нуух",
                        fontSize = 12.sp,
                        color = Color.DarkGray
                    )
                }
            }

            Text(
                text = "Карт дээр удаан дарж засна",
                fontSize = 12.sp,
                color = Color.Gray
            )
        }
    }
}

// Өмнөх, дараах, нэмэх, засах, устгах
@Composable
private fun RecordActions(
    hasRecord: Boolean,
    position: Int,
    count: Int,
    onPrevious: () -> Unit,
    onNext: () -> Unit,
    onAdd: () -> Unit,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement =
            Arrangement.spacedBy(10.dp)
    ) {
        OutlinedButton(
            onClick = onPrevious,
            enabled = hasRecord && position > 0,
            modifier = Modifier.weight(1f)
        ) {
            Text("ӨМНӨХ", fontSize = 12.sp)
        }

        OutlinedButton(
            onClick = onNext,
            enabled = hasRecord && position < count - 1,
            modifier = Modifier.weight(1f)
        ) {
            Text("ДАРААХ", fontSize = 12.sp)
        }
    }

    Spacer(Modifier.height(12.dp))

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement =
            Arrangement.spacedBy(7.dp)
    ) {
        Button(
            onClick = onAdd,
            modifier = Modifier.weight(1f),
            contentPadding = PaddingValues(4.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Green
            )
        ) {
            Text("НЭМЭХ", fontSize = 12.sp)
        }

        Button(
            onClick = onEdit,
            enabled = hasRecord,
            modifier = Modifier.weight(1f),
            contentPadding = PaddingValues(4.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Blue
            )
        ) {
            Text("ЗАСАХ", fontSize = 12.sp)
        }

        Button(
            onClick = onDelete,
            enabled = hasRecord,
            modifier = Modifier.weight(1f),
            contentPadding = PaddingValues(4.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Red
            )
        ) {
            Text("УСТГАХ", fontSize = 12.sp)
        }
    }
}
