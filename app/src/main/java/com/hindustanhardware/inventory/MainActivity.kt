package com.hindustanhardware.inventory

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Inventory2
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.hindustanhardware.inventory.data.InventoryViewModel
import com.hindustanhardware.inventory.data.Product
import kotlinx.coroutines.flow.flowOf

private val Navy = Color(0xFF07111F)
private val Navy2 = Color(0xFF0D1B2A)
private val Panel = Color(0xFF142238)
private val Saffron = Color(0xFFFFA726)
private val Gold = Color(0xFFFFC107)
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

private enum class AppPage {
    SPLASH,
    WELCOME,
    HOME,
    INVENTORY,
    ADD_PRODUCT
}

@Composable
fun HindustanHardwareApp(
    inventoryViewModel: InventoryViewModel = viewModel()
) {
    var page by remember { mutableStateOf(AppPage.SPLASH) }

    androidx.compose.runtime.LaunchedEffect(Unit) {
        kotlinx.coroutines.delay(3800)
        page = AppPage.WELCOME
    }

    when (page) {
        AppPage.SPLASH -> SplashScreen()
        AppPage.WELCOME -> WelcomeScreen {
            page = AppPage.HOME
        }
        AppPage.HOME -> DashboardScreen(
            viewModel = inventoryViewModel,
            onInventory = { page = AppPage.INVENTORY },
            onAddProduct = { page = AppPage.ADD_PRODUCT }
        )
        AppPage.INVENTORY -> InventoryScreen(
            viewModel = inventoryViewModel,
            onBack = { page = AppPage.HOME },
            onAdd = { page = AppPage.ADD_PRODUCT }
        )
        AppPage.ADD_PRODUCT -> AddProductScreen(
            viewModel = inventoryViewModel,
            onBack = { page = AppPage.HOME }
        )
    }
}

@Composable
private fun SplashScreen() {
    val infinite = rememberInfiniteTransition(label = "splash")
    val pulse by infinite.animateFloat(
        0.88f, 1.08f,
        infiniteRepeatable(
            tween(1100, easing = FastOutSlowInEasing),
            RepeatMode.Reverse
        ),
        label = "pulse"
    )

    Box(
        Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(listOf(Navy, Navy2, Color(0xFF162943)))
            ),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            IndiaMark(
                Modifier
                    .size(180.dp)
                    .scale(pulse)
            )

            Spacer(Modifier.height(24.dp))

            Text(
                "Hindustan",
                color = White,
                fontSize = 43.sp,
                fontFamily = FontFamily.Cursive,
                fontWeight = FontWeight.Bold
            )

            Text(
                "HARDWARE",
                color = Saffron,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 5.sp
            )

            Spacer(Modifier.height(18.dp))

            Text(
                "SMART INVENTORY",
                color = Muted,
                fontSize = 12.sp,
                letterSpacing = 3.sp
            )
        }
    }
}

@Composable
private fun IndiaMark(modifier: Modifier = Modifier) {
    Canvas(modifier) {
        val path = Path().apply {
            moveTo(size.width * .45f, size.height * .04f)
            lineTo(size.width * .58f, size.height * .15f)
            lineTo(size.width * .68f, size.height * .23f)
            lineTo(size.width * .62f, size.height * .34f)
            lineTo(size.width * .73f, size.height * .42f)
            lineTo(size.width * .64f, size.height * .54f)
            lineTo(size.width * .58f, size.height * .66f)
            lineTo(size.width * .51f, size.height * .77f)
            lineTo(size.width * .47f, size.height * .94f)
            lineTo(size.width * .40f, size.height * .79f)
            lineTo(size.width * .32f, size.height * .69f)
            lineTo(size.width * .28f, size.height * .54f)
            lineTo(size.width * .19f, size.height * .46f)
            lineTo(size.width * .28f, size.height * .35f)
            lineTo(size.width * .24f, size.height * .24f)
            lineTo(size.width * .37f, size.height * .19f)
            close()
        }

        drawPath(
            path,
            Brush.verticalGradient(
                listOf(Color(0xFFFF9933), Color.White, Color(0xFF138808))
            ),
            style = Stroke(width = 5.dp.toPx())
        )

        drawCircle(
            Color(0xFF4D7CFE).copy(alpha = .3f),
            size.minDimension * .48f,
            style = Stroke(width = 2.dp.toPx())
        )
    }
}

@Composable
private fun LogoMark(modifier: Modifier = Modifier) {
    Box(
        modifier
            .background(Panel, RoundedCornerShape(22.dp))
            .border(1.dp, Saffron.copy(alpha = .6f), RoundedCornerShape(22.dp)),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                "HH",
                color = White,
                fontWeight = FontWeight.ExtraBold,
                fontSize = 28.sp
            )
            Box(
                Modifier
                    .padding(top = 3.dp)
                    .size(width = 35.dp, height = 3.dp)
                    .background(Saffron, CircleShape)
            )
        }
    }
}

@Composable
private fun WelcomeScreen(onStart: () -> Unit) {
    Column(
        Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(listOf(Navy, Navy2, Color(0xFF07111F)))
            )
            .padding(26.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Spacer(Modifier.height(12.dp))

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            LogoMark(Modifier.size(110.dp))

            Spacer(Modifier.height(20.dp))

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
                color = Saffron,
                fontSize = 12.sp,
                letterSpacing = 2.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Column(verticalArrangement = Arrangement.spacedBy(18.dp)) {
            FeatureLine("✓", "Fast inventory", "Manage products and stock easily")
            FeatureLine("⌁", "Works offline", "Your inventory stays on your phone")
            FeatureLine("▣", "Smart stock control", "Find low-stock items quickly")
        }

        Button(
            onClick = onStart,
            modifier = Modifier
                .fillMaxWidth()
                .height(58.dp),
            shape = RoundedCornerShape(18.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Saffron,
                contentColor = Navy
            )
        ) {
            Text(
                "GET STARTED",
                fontWeight = FontWeight.ExtraBold,
                letterSpacing = 2.sp
            )
        }
    }
}

@Composable
private fun FeatureLine(icon: String, title: String, description: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
            Modifier
                .size(44.dp)
                .background(Panel, RoundedCornerShape(14.dp)),
            contentAlignment = Alignment.Center
        ) {
            Text(icon, color = Saffron, fontSize = 21.sp)
        }

        Spacer(Modifier.size(13.dp))

        Column {
            Text(title, color = White, fontWeight = FontWeight.Bold)
            Text(description, color = Muted, fontSize = 12.sp)
        }
    }
}

@Composable
private fun DashboardScreen(
    viewModel: InventoryViewModel,
    onInventory: () -> Unit,
    onAddProduct: () -> Unit
) {
    val count by viewModel.productCount.collectAsState()
    val units by viewModel.totalUnits.collectAsState()
    val inStock by viewModel.inStockCount.collectAsState()
    val lowStock by viewModel.lowStockCount.collectAsState()
    val outOfStock by viewModel.outOfStockCount.collectAsState()

    Scaffold(
        containerColor = Navy,
        bottomBar = {
            BottomNavigationBar(
                selected = "Home",
                onHome = {},
                onInventory = onInventory
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.spacedBy(18.dp)
        ) {
            item {
                Spacer(Modifier.height(12.dp))
                Header()
            }

            item {
                Column {
                    Text("Good morning 👋", color = White, fontSize = 25.sp, fontWeight = FontWeight.Bold)
                    Text("Here's your stock overview", color = Muted, fontSize = 14.sp)
                }
            }

            item {
                Card(
                    colors = CardDefaults.cardColors(containerColor = Panel),
                    shape = RoundedCornerShape(24.dp)
                ) {
                    Column(Modifier.padding(22.dp)) {
                        Text("TOTAL PRODUCTS", color = Muted, fontSize = 12.sp, letterSpacing = 2.sp)
                        Spacer(Modifier.height(8.dp))
                        Text("$count", color = White, fontSize = 43.sp, fontWeight = FontWeight.ExtraBold)
                        Text("$units total units in inventory", color = Green, fontSize = 13.sp)
                    }
                }
            }

            item {
                Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    StatCard("IN STOCK", inStock, Green, Modifier.weight(1f))
                    StatCard("LOW STOCK", lowStock, Saffron, Modifier.weight(1f))
                    StatCard("OUT", outOfStock, Red, Modifier.weight(1f))
                }
            }

            item {
                Text("QUICK ACTIONS", color = Muted, fontWeight = FontWeight.Bold, letterSpacing = 2.sp)
            }

            item {
                ActionButton(
                    title = "Add Product",
                    subtitle = "Enter a new inventory item",
                    onClick = onAddProduct
                )
            }

            item {
                ActionButton(
                    title = "View Inventory",
                    subtitle = "Search and manage your products",
                    onClick = onInventory
                )
            }

            item {
                Text("Your inventory is stored locally on this phone.", color = Muted, fontSize = 12.sp)
                Spacer(Modifier.height(12.dp))
            }
        }
    }
}

@Composable
private fun Header() {
    Row(
        Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        LogoMark(Modifier.size(48.dp))
        Spacer(Modifier.size(12.dp))
        Column(Modifier.weight(1f)) {
            Text("Hindustan Hardware", color = White, fontSize = 19.sp, fontWeight = FontWeight.Bold)
            Text("SMART INVENTORY", color = Saffron, fontSize = 10.sp, letterSpacing = 2.sp)
        }
        Icon(Icons.Default.Inventory2, contentDescription = null, tint = Saffron)
    }
}

@Composable
private fun StatCard(
    title: String,
    count: Int,
    color: Color,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = Panel),
        shape = RoundedCornerShape(18.dp)
    ) {
        Column(Modifier.padding(12.dp)) {
            Text(title, color = Muted, fontSize = 9.sp, fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(8.dp))
            Text("$count", color = color, fontSize = 25.sp, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
private fun ActionButton(
    title: String,
    subtitle: String,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        colors = CardDefaults.cardColors(containerColor = Panel),
        shape = RoundedCornerShape(20.dp)
    ) {
        Row(
            Modifier.padding(18.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                Modifier
                    .size(45.dp)
                    .background(Saffron.copy(alpha = .14f), RoundedCornerShape(14.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Default.Add, contentDescription = null, tint = Saffron)
            }

            Spacer(Modifier.size(14.dp))

            Column(Modifier.weight(1f)) {
                Text(title, color = White, fontWeight = FontWeight.Bold)
                Text(subtitle, color = Muted, fontSize = 12.sp)
            }
        }
    }
}

@Composable
private fun InventoryScreen(
    viewModel: InventoryViewModel,
    onBack: () -> Unit,
    onAdd: () -> Unit
) {
    var query by remember { mutableStateOf("") }

    val productsFlow = remember(query) {
        if (query.isBlank()) {
            viewModel.products
        } else {
            viewModel.searchProducts(query)
        }
    }

    val products by productsFlow.collectAsState(initial = emptyList())

    Scaffold(
        containerColor = Navy,
        bottomBar = {
            BottomNavigationBar(
                selected = "Inventory",
                onHome = onBack,
                onInventory = {}
            )
        },
        floatingActionButton = {
            androidx.compose.material3.FloatingActionButton(
                onClick = onAdd,
                containerColor = Saffron,
                contentColor = Navy
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add product")
            }
        }
    ) { padding ->
        Column(
            Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 18.dp)
        ) {
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(vertical = 15.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onBack) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = White)
                }
                Text("Inventory", color = White, fontSize = 25.sp, fontWeight = FontWeight.Bold)
            }

            OutlinedTextField(
                value = query,
                onValueChange = { query = it },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                placeholder = { Text("Search name, SKU or barcode", color = Muted) },
                leadingIcon = {
                    Icon(Icons.Default.Search, contentDescription = null, tint = Muted)
                },
                colors = fieldColors()
            )

            Spacer(Modifier.height(12.dp))

            if (products.isEmpty()) {
                Box(
                    Modifier
                        .fillMaxSize()
                        .padding(bottom = 70.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            Icons.Default.Inventory2,
                            contentDescription = null,
                            tint = Saffron,
                            modifier = Modifier.size(54.dp)
                        )
                        Spacer(Modifier.height(12.dp))
                        Text(
                            if (query.isBlank()) "No products yet" else "No matching products",
                            color = White,
                            fontWeight = FontWeight.Bold
                        )
                        Text("Add your first hardware item.", color = Muted, fontSize = 13.sp)
                    }
                }
            } else {
                LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    items(products, key = { it.id }) { product ->
                        ProductCard(
                            product = product,
                            onDelete = {
                                viewModel.deleteProduct(product) {}
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ProductCard(
    product: Product,
    onDelete: () -> Unit
) {
    val stockColor = when {
        product.quantity <= 0 -> Red
        product.quantity <= product.minimumStock -> Saffron
        else -> Green
    }

    Card(
        colors = CardDefaults.cardColors(containerColor = Panel),
        shape = RoundedCornerShape(18.dp)
    ) {
        Row(
            Modifier
                .fillMaxWidth()
                .padding(15.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                Modifier
                    .size(45.dp)
                    .background(Navy2, RoundedCornerShape(13.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Default.Inventory2, contentDescription = null, tint = Saffron)
            }

            Spacer(Modifier.size(12.dp))

            Column(Modifier.weight(1f)) {
                Text(
                    product.name,
                    color = White,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    "${product.category} • ${product.unit}",
                    color = Muted,
                    fontSize = 12.sp
                )
                Text(
                    "Stock: ${product.quantity}",
                    color = stockColor,
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp
                )
                Text(
                    "Selling: ₹${"%.2f".format(product.sellingPrice)}",
                    color = White,
                    fontSize = 12.sp
                )
            }

            IconButton(onClick = onDelete) {
                Icon(Icons.Default.Delete, contentDescription = "Delete", tint = Red)
            }
        }
    }
}

@Composable
private fun AddProductScreen(
    viewModel: InventoryViewModel,
    onBack: () -> Unit
) {
    var name by remember { mutableStateOf("") }
    var sku by remember { mutableStateOf("") }
    var barcode by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("Other") }
    var purchasePrice by remember { mutableStateOf("") }
    var sellingPrice by remember { mutableStateOf("") }
    var quantity by remember { mutableStateOf("") }
    var minimumStock by remember { mutableStateOf("5") }
    var unit by remember { mutableStateOf("Piece") }
    var rack by remember { mutableStateOf("") }
    var error by remember { mutableStateOf("") }
    var saving by remember { mutableStateOf(false) }

    Scaffold(
        containerColor = Navy,
        topBar = {
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onBack) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = White)
                }
                Text("Add Product", color = White, fontSize = 23.sp, fontWeight = FontWeight.Bold)
            }
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                Text("PRODUCT DETAILS", color = Saffron, fontSize = 12.sp, fontWeight = FontWeight.Bold, letterSpacing = 2.sp)
            }

            item {
                AppField(name, { name = it }, "Product name *")
            }

            item {
                AppField(sku, { sku = it }, "SKU / Item code")
            }

            item {
                AppField(barcode, { barcode = it }, "Barcode / QR value")
            }

            item {
                AppField(category, { category = it }, "Category")
            }

            item {
                AppField(purchasePrice, { purchasePrice = it }, "Purchase price (₹)", KeyboardType.Decimal)
            }

            item {
                AppField(sellingPrice, { sellingPrice = it }, "Selling price (₹)", KeyboardType.Decimal)
            }

            item {
                Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    Box(Modifier.weight(1f)) {
                        AppField(quantity, { quantity = it }, "Quantity *", KeyboardType.Number)
                    }
                    Box(Modifier.weight(1f)) {
                        AppField(minimumStock, { minimumStock = it }, "Minimum stock", KeyboardType.Number)
                    }
                }
            }

            item {
                AppField(unit, { unit = it }, "Unit (Piece, Box, Kg, etc.)")
            }

            item {
                AppField(rack, { rack = it }, "Rack / shelf")
            }

            item {
                if (error.isNotBlank()) {
                    Text(error, color = Red, fontSize = 13.sp)
                }
            }

            item {
                Button(
                    onClick = {
                        if (name.isBlank()) {
                            error = "Please enter a product name."
                            return@Button
                        }

                        val qty = quantity.toIntOrNull()
                        if (qty == null || qty < 0) {
                            error = "Enter a valid non-negative quantity."
                            return@Button
                        }

                        val purchase = purchasePrice.toDoubleOrNull() ?: 0.0
                        val selling = sellingPrice.toDoubleOrNull() ?: 0.0
                        val minimum = minimumStock.toIntOrNull() ?: 5

                        if (purchase < 0 || selling < 0 || minimum < 0) {
                            error = "Prices and minimum stock cannot be negative."
                            return@Button
                        }

                        saving = true
                        error = ""

                        viewModel.addProduct(
                            Product(
                                name = name.trim(),
                                sku = sku.trim(),
                                barcode = barcode.trim(),
                                category = category.trim().ifBlank { "Other" },
                                purchasePrice = purchase,
                                sellingPrice = selling,
                                quantity = qty,
                                minimumStock = minimum,
                                unit = unit.trim().ifBlank { "Piece" },
                                rack = rack.trim()
                            )
                        ) { success ->
                            saving = false
                            if (success) {
                                onBack()
                            } else {
                                error = "Couldn't save the product. Check if the item already exists."
                            }
                        }
                    },
                    enabled = !saving,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = RoundedCornerShape(17.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Saffron,
                        contentColor = Navy
                    )
                ) {
                    Text(
                        if (saving) "SAVING..." else "SAVE PRODUCT",
                        fontWeight = FontWeight.ExtraBold,
                        letterSpacing = 1.sp
                    )
                }
            }

            item {
                Text(
                    "Saved on this phone. Internet is not required.",
                    color = Muted,
                    fontSize = 12.sp
                )
                Spacer(Modifier.height(24.dp))
            }
        }
    }
}

@Composable
private fun AppField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    keyboardType: KeyboardType = KeyboardType.Text
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = Modifier.fillMaxWidth(),
        label = { Text(label) },
        singleLine = true,
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        colors = fieldColors()
    )
}

@Composable
private fun fieldColors() = OutlinedTextFieldDefaults.colors(
    focusedTextColor = White,
    unfocusedTextColor = White,
    focusedBorderColor = Saffron,
    unfocusedBorderColor = Color(0xFF35465D),
    focusedLabelColor = Saffron,
    unfocusedLabelColor = Muted,
    cursorColor = Saffron,
    focusedContainerColor = Panel,
    unfocusedContainerColor = Panel
)

@Composable
private fun BottomNavigationBar(
    selected: String,
    onHome: () -> Unit,
    onInventory: () -> Unit
) {
    Surface(color = Navy2, shadowElevation = 10.dp) {
        Row(
            Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            NavigationItem(
                icon = { Icon(Icons.Default.Home, contentDescription = null) },
                label = "Home",
                selected = selected == "Home",
                onClick = onHome
            )
            NavigationItem(
                icon = { Icon(Icons.Default.Inventory2, contentDescription = null) },
                label = "Inventory",
                selected = selected == "Inventory",
                onClick = onInventory
            )
        }
    }
}

@Composable
private fun NavigationItem(
    icon: @Composable () -> Unit,
    label: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .clickable(onClick = onClick)
            .padding(horizontal = 25.dp, vertical = 5.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(contentAlignment = Alignment.Center) {
            androidx.compose.runtime.CompositionLocalProvider(
                androidx.compose.material3.LocalContentColor provides
                    if (selected) Saffron else Muted
            ) {
                icon()
            }
        }
        Text(
            label,
            color = if (selected) Saffron else Muted,
            fontSize = 11.sp,
            fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal
        )
    }
}
