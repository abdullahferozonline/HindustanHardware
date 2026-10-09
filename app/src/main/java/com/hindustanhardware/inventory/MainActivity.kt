package com.hindustanhardware.inventory

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Inventory2
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.hindustanhardware.inventory.data.InventoryViewModel
import kotlinx.coroutines.delay

private val Navy = Color(0xFF07111F)
private val Panel = Color(0xFF142238)
private val Gold = Color(0xFFFFA726)
private val White = Color(0xFFF8FAFC)
private val Muted = Color(0xFFB8C4D4)
private val Green = Color(0xFF46C486)
private val Red = Color(0xFFFF7373)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            HindustanHardwareApp()
        }
    }
}

private enum class Page {
    SPLASH, WELCOME, HOME, INVENTORY, ADD
}

@Composable
fun HindustanHardwareApp(
    vm: InventoryViewModel = viewModel()
) {
    var page by remember { mutableStateOf(Page.SPLASH) }

    LaunchedEffect(Unit) {
        delay(3000)
        page = Page.WELCOME
    }

    when (page) {
        Page.SPLASH -> SplashScreen()
        Page.WELCOME -> WelcomeScreen {
            page = Page.HOME
        }
        Page.HOME -> Dashboard(
            vm = vm,
            onInventory = { page = Page.INVENTORY },
            onAdd = { page = Page.ADD }
        )
        Page.INVENTORY -> InventoryScreen(
            vm = vm,
            onBack = { page = Page.HOME },
            onAdd = { page = Page.ADD }
        )
        Page.ADD -> AddProductScreen(
            vm = vm,
            onBack = { page = Page.INVENTORY }
        )
    }
}

@Composable
private fun SplashScreen() {
    Box(
        Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(Navy, Color(0xFF10243B), Navy)
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text("🇮🇳", fontSize = 76.sp)
            Spacer(Modifier.height(20.dp))
            Text(
                "Hindustan",
                fontSize = 43.sp,
                color = White,
                fontFamily = FontFamily.Cursive,
                fontWeight = FontWeight.Bold
            )
            Text(
                "HARDWARE",
                color = Gold,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 5.sp
            )
            Spacer(Modifier.height(15.dp))
            Text("SMART INVENTORY", color = Muted, letterSpacing = 3.sp)
        }
    }
}

@Composable
private fun WelcomeScreen(onStart: () -> Unit) {
    Column(
        Modifier
            .fillMaxSize()
            .background(Navy)
            .padding(26.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Spacer(Modifier.height(10.dp))

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text("🇮🇳", fontSize = 66.sp)
            Text(
                "Hindustan Hardware",
                color = White,
                fontSize = 34.sp,
                fontFamily = FontFamily.Cursive,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
            Text(
                "YOUR STOCK. SIMPLIFIED.",
                color = Gold,
                fontSize = 12.sp,
                letterSpacing = 2.sp
            )
        }

        Column(verticalArrangement = Arrangement.spacedBy(20.dp)) {
            Feature("✓", "Fast inventory", "Manage hardware products easily")
            Feature("⌁", "Works offline", "Your data stays on this phone")
            Feature("▣", "Smart stock control", "Know what is running low")
        }

        Button(
            onClick = onStart,
            modifier = Modifier.fillMaxWidth().height(58.dp),
            shape = RoundedCornerShape(18.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Gold,
                contentColor = Navy
            )
        ) {
            Text("GET STARTED", fontWeight = FontWeight.ExtraBold)
        }
    }
}

@Composable
private fun Feature(icon: String, title: String, description: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Text(icon, color = Gold, fontSize = 28.sp)
        Spacer(Modifier.width(16.dp))
        Column {
            Text(title, color = White, fontWeight = FontWeight.Bold)
            Text(description, color = Muted, fontSize = 12.sp)
        }
    }
}

@Composable
private fun Dashboard(
    vm: InventoryViewModel,
    onInventory: () -> Unit,
    onAdd: () -> Unit
) {
    val count by vm.productCount.collectAsState()
    val units by vm.totalUnits.collectAsState()
    val low by vm.lowStockCount.collectAsState()
    val out by vm.outOfStockCount.collectAsState()

    Scaffold(
        containerColor = Navy,
        bottomBar = {
            NavigationBar(containerColor = Panel) {
                NavigationBarItem(
                    selected = true,
                    onClick = {},
                    icon = { Icon(Icons.Default.Home, null) },
                    label = { Text("Home") }
                )
                NavigationBarItem(
                    selected = false,
                    onClick = onInventory,
                    icon = { Icon(Icons.Default.Inventory2, null) },
                    label = { Text("Inventory") }
                )
            }
        }
    ) { padding ->
        Column(
            Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(18.dp)
        ) {
            Text(
                "Hindustan Hardware",
                color = White,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )
            Text("Your shop, organized.", color = Muted)

            Card(colors = CardDefaults.cardColors(containerColor = Panel)) {
                Column(Modifier.padding(22.dp)) {
                    Text("TOTAL PRODUCTS", color = Muted, letterSpacing = 2.sp)
                    Text(
                        "$count",
                        color = White,
                        fontSize = 42.sp,
                        fontWeight = FontWeight.ExtraBold
                    )
                    Text("$units total units", color = Green)
                }
            }

            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                SummaryCard("LOW STOCK", low, Gold, Modifier.weight(1f))
                SummaryCard("OUT OF STOCK", out, Red, Modifier.weight(1f))
            }

            Button(
                onClick = onAdd,
                modifier = Modifier.fillMaxWidth().height(56.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Gold, contentColor = Navy)
            ) {
                Icon(Icons.Default.Add, null)
                Spacer(Modifier.width(8.dp))
                Text("ADD PRODUCT")
            }

            OutlinedButton(
                onClick = onInventory,
                modifier = Modifier.fillMaxWidth().height(56.dp)
            ) {
                Icon(Icons.Default.Inventory2, null)
                Spacer(Modifier.width(8.dp))
                Text("VIEW INVENTORY")
            }
        }
    }
}

@Composable
private fun SummaryCard(
    title: String,
    value: Int,
    color: Color,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = Panel)
    ) {
        Column(Modifier.padding(15.dp)) {
            Text(title, color = Muted, fontSize = 11.sp)
            Text("$value", color = color, fontSize = 27.sp, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
private fun InventoryScreen(
    vm: InventoryViewModel,
    onBack: () -> Unit,
    onAdd: () -> Unit
) {
    val products by vm.products.collectAsState()

    Scaffold(
        containerColor = Navy,
        topBar = {
            TopAppBar(
                title = { Text("Inventory") },
                navigationIcon = {
                    TextButton(onClick = onBack) {
                        Text("BACK")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Navy,
                    titleContentColor = White,
                    navigationIconContentColor = Gold
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onAdd,
                containerColor = Gold,
                contentColor = Navy
            ) {
                Icon(Icons.Default.Add, null)
            }
        }
    ) { padding ->
        if (products.isEmpty()) {
            Box(
                Modifier.fillMaxSize().padding(padding),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(Icons.Default.Inventory2, null, tint = Gold, modifier = Modifier.size(48.dp))
                    Spacer(Modifier.height(12.dp))
                    Text("No products yet", color = White)
                    Text("Tap + to add your first item.", color = Muted)
                }
            }
        } else {
            androidx.compose.foundation.lazy.LazyColumn(
                Modifier.fillMaxSize().padding(padding)
            ) {
                items(products.size) { index ->
                    val product = products[index]
                    ListItem(
                        headlineContent = {
                            Text(product.name, color = White)
                        },
                        supportingContent = {
                            Text(
                                "${product.category} • Stock: ${product.quantity}",
                                color = Muted
                            )
                        },
                        trailingContent = {
                            Text("₹${product.sellingPrice}", color = Gold)
                        },
                        colors = ListItemDefaults.colors(containerColor = Panel)
                    )
                    HorizontalDivider(color = Navy)
                }
            }
        }
    }
}

@Composable
private fun AddProductScreen(
    vm: InventoryViewModel,
    onBack: () -> Unit
) {
    var name by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("Other") }
    var sku by remember { mutableStateOf("") }
    var barcode by remember { mutableStateOf("") }
    var purchase by remember { mutableStateOf("") }
    var selling by remember { mutableStateOf("") }
    var quantity by remember { mutableStateOf("") }
    var minimum by remember { mutableStateOf("5") }
    var unit by remember { mutableStateOf("Piece") }
    var message by remember { mutableStateOf("") }

    Scaffold(
        containerColor = Navy,
        topBar = {
            TopAppBar(
                title = { Text("Add Product") },
                navigationIcon = {
                    TextButton(onClick = onBack) { Text("BACK") }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Navy,
                    titleContentColor = White,
                    navigationIconContentColor = Gold
                )
            )
        }
    ) { padding ->
        androidx.compose.foundation.lazy.LazyColumn(
            Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item { Input(name, { name = it }, "Product name *") }
            item { Input(category, { category = it }, "Category") }
            item { Input(sku, { sku = it }, "SKU / Item code") }
            item { Input(barcode, { barcode = it }, "Barcode / QR value") }
            item { Input(purchase, { purchase = it }, "Purchase price ₹") }
            item { Input(selling, { selling = it }, "Selling price ₹") }
            item { Input(quantity, { quantity = it }, "Quantity *") }
            item { Input(minimum, { minimum = it }, "Minimum stock") }
            item { Input(unit, { unit = it }, "Unit") }

            item {
                if (message.isNotBlank()) {
                    Text(message, color = Red)
                }
            }

            item {
                Button(
                    onClick = {
                        val qty = quantity.toIntOrNull()
                        val buy = purchase.toDoubleOrNull() ?: 0.0
                        val sell = selling.toDoubleOrNull() ?: 0.0
                        val min = minimum.toIntOrNull() ?: 5

                        if (name.isBlank() || qty == null || qty < 0 ||
                            buy < 0 || sell < 0 || min < 0
                        ) {
                            message = "Enter a product name and valid non-negative values."
                        } else {
                            vm.addProduct(
                                com.hindustanhardware.inventory.data.Product(
                                    name = name.trim(),
                                    category = category.trim().ifBlank { "Other" },
                                    sku = sku.trim(),
                                    barcode = barcode.trim(),
                                    purchasePrice = buy,
                                    sellingPrice = sell,
                                    quantity = qty,
                                    minimumStock = min,
                                    unit = unit.trim().ifBlank { "Piece" }
                                )
                            ) { success ->
                                if (success) onBack()
                                else message = "Couldn't save this product."
                            }
                        }
                    },
                    modifier = Modifier.fillMaxWidth().height(54.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Gold, contentColor = Navy)
                ) {
                    Text("SAVE PRODUCT", fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
private fun Input(
    value: String,
    onValueChange: (String) -> Unit,
    label: String
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        modifier = Modifier.fillMaxWidth(),
        singleLine = true,
        colors = OutlinedTextFieldDefaults.colors(
            focusedTextColor = White,
            unfocusedTextColor = White,
            focusedBorderColor = Gold,
            unfocusedBorderColor = Muted,
            focusedLabelColor = Gold,
            unfocusedLabelColor = Muted,
            cursorColor = Gold
        )
    )
}
