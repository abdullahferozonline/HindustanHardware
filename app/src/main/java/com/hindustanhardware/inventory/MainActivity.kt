package com.hindustanhardware.inventory

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloatAsState
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            HindustanHardwareApp()
        }
    }
}

private val Navy = Color(0xFF07111F)
private val Navy2 = Color(0xFF0D1B2A)
private val Saffron = Color(0xFFFFA726)
private val Gold = Color(0xFFFFC107)
private val White = Color(0xFFF8FAFC)
private val Muted = Color(0xFFB8C4D4)
private val Green = Color(0xFF2E9B63)

@Composable
fun HindustanHardwareApp() {

    var splashFinished by remember {
        mutableStateOf(false)
    }

    if (!splashFinished) {
        AnimatedSplash {
            splashFinished = true
        }
    } else {
        WelcomeScreen()
    }
}

@Composable
private fun AnimatedSplash(
    onFinished: () -> Unit
) {

    var scene by remember {
        mutableStateOf(0)
    }

    LaunchedEffect(Unit) {
        delay(350)
        scene = 1

        delay(1200)
        scene = 2

        delay(1400)
        scene = 3

        delay(1500)
        onFinished()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(
                        Navy,
                        Navy2,
                        Color(0xFF101D2F)
                    )
                )
            ),
        contentAlignment = Alignment.Center
    ) {

        AnimatedVisibility(
            visible = scene >= 1,
            enter = fadeIn(tween(800)) + scaleIn(tween(900)),
            exit = fadeOut(tween(400))
        ) {
            IndiaGlow()
        }

        AnimatedVisibility(
            visible = scene >= 2,
            enter = fadeIn(tween(700)) + scaleIn(tween(700)),
            exit = fadeOut(tween(350))
        ) {
            BrandSplash()
        }

        AnimatedVisibility(
            visible = scene >= 3,
            enter = fadeIn(tween(600)) +
                    slideInVertically(
                        initialOffsetY = { 80 },
                        animationSpec = tween(700)
                    ),
            exit = fadeOut(tween(300))
        ) {
            FinalSplash()
        }
    }
}

@Composable
private fun IndiaGlow() {

    val infinite = rememberInfiniteTransition(
        label = "indiaGlow"
    )

    val pulse by infinite.animateFloat(
        initialValue = 0.85f,
        targetValue = 1.08f,
        animationSpec = infiniteRepeatable(
            animation = tween(1300, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse"
    )

    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Box(
            modifier = Modifier
                .size(230.dp)
                .scale(pulse),
            contentAlignment = Alignment.Center
        ) {

            Canvas(
                modifier = Modifier.fillMaxSize()
            ) {

                val path = Path()

                path.moveTo(size.width * .45f, .04f)
                path.lineTo(size.width * .57f, .15f)
                path.lineTo(size.width * .67f, .22f)
                path.lineTo(size.width * .62f, .33f)
                path.lineTo(size.width * .73f, .42f)
                path.lineTo(size.width * .65f, .52f)
                path.lineTo(size.width * .59f, .65f)
                path.lineTo(size.width * .51f, .76f)
                path.lineTo(size.width * .47f, .94f)
                path.lineTo(size.width * .41f, .79f)
                path.lineTo(size.width * .33f, .69f)
                path.lineTo(size.width * .29f, .54f)
                path.lineTo(size.width * .20f, .46f)
                path.lineTo(size.width * .28f, .35f)
                path.lineTo(size.width * .24f, .24f)
                path.lineTo(size.width * .37f, .19f)
                path.close()

                drawPath(
                    path = path,
                    brush = Brush.verticalGradient(
                        listOf(
                            Color(0xFFFF9933),
                            Color.White,
                            Color(0xFF138808)
                        )
                    ),
                    style = Stroke(
                        width = 5.dp.toPx()
                    )
                )

                drawCircle(
                    color = Color(0xFF4D7CFE).copy(alpha = .25f),
                    radius = size.minDimension * .48f,
                    center = center,
                    style = Stroke(
                        width = 2.dp.toPx()
                    )
                )
            }
        }

        Spacer(modifier = Modifier.height(18.dp))

        Text(
            text = "MADE IN INDIA",
            color = Muted,
            fontSize = 13.sp,
            letterSpacing = 4.sp,
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
private fun BrandSplash() {

    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        LogoMark(
            modifier = Modifier.size(96.dp)
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Hindustan",
            color = White,
            fontSize = 42.sp,
            fontWeight = FontWeight.SemiBold,
            fontFamily = FontFamily.Cursive
        )

        Text(
            text = "HARDWARE",
            color = Saffron,
            fontSize = 25.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 6.sp
        )
    }
}

@Composable
private fun FinalSplash() {

    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        LogoMark(
            modifier = Modifier.size(82.dp)
        )

        Spacer(modifier = Modifier.height(18.dp))

        Text(
            text = "Hindustan Hardware",
            color = White,
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = FontFamily.Cursive
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "SMART INVENTORY",
            color = Gold,
            fontSize = 12.sp,
            letterSpacing = 4.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "Simple • Fast • Offline",
            color = Muted,
            fontSize = 14.sp
        )
    }
}

@Composable
private fun LogoMark(
    modifier: Modifier = Modifier
) {

    Box(
        modifier = modifier
            .background(
                Brush.linearGradient(
                    listOf(
                        Color(0xFF162A42),
                        Color(0xFF0A1727)
                    )
                ),
                RoundedCornerShape(26.dp)
            )
            .border(
                width = 1.dp,
                color = Saffron.copy(alpha = .55f),
                shape = RoundedCornerShape(26.dp)
            ),
        contentAlignment = Alignment.Center
    ) {

        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(
                text = "HH",
                color = White,
                fontSize = 28.sp,
                fontWeight = FontWeight.ExtraBold
            )

            Box(
                modifier = Modifier
                    .size(
                        width = 38.dp,
                        height = 3.dp
                    )
                    .background(
                        Saffron,
                        RoundedCornerShape(20.dp)
                    )
            )
        }
    }
}

@Composable
private fun WelcomeScreen() {

    val infinite = rememberInfiniteTransition(
        label = "welcome"
    )

    val glow by infinite.animateFloat(
        initialValue = .35f,
        targetValue = .75f,
        animationSpec = infiniteRepeatable(
            tween(1800, easing = LinearEasing),
            RepeatMode.Reverse
        ),
        label = "glow"
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(
                        Navy,
                        Color(0xFF0B1727),
                        Color(0xFF07111F)
                    )
                )
            )
            .padding(
                horizontal = 28.dp,
                vertical = 36.dp
            ),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {

        Spacer(modifier = Modifier.height(20.dp))

        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Box(
                modifier = Modifier
                    .size(120.dp)
                    .alpha(glow),
                contentAlignment = Alignment.Center
            ) {

                LogoMark(
                    modifier = Modifier.size(105.dp)
                )
            }

            Spacer(modifier = Modifier.height(22.dp))

            Text(
                text = "Hindustan Hardware",
                color = White,
                fontSize = 34.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Cursive,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = "YOUR STOCK. SIMPLIFIED.",
                color = Saffron,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 3.sp
            )
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            FeatureLine(
                icon = "▣",
                title = "Fast inventory",
                description = "Add products manually, by photo or barcode"
            )

            Spacer(modifier = Modifier.height(16.dp))

            FeatureLine(
                icon = "⌁",
                title = "Works offline",
                description = "Your inventory stays on your phone"
            )

            Spacer(modifier = Modifier.height(16.dp))

            FeatureLine(
                icon = "✓",
                title = "Built for your shop",
                description = "Simple stock tracking without the complexity"
            )
        }

        Button(
            onClick = {
                // Dashboard will be connected in the next stage.
            },
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
                text = "GET STARTED",
                fontSize = 15.sp,
                fontWeight = FontWeight.ExtraBold,
                letterSpacing = 2.sp
            )
        }
    }
}

@Composable
private fun FeatureLine(
    icon: String,
    title: String,
    description: String
) {

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Box(
            modifier = Modifier
                .size(44.dp)
                .background(
                    Color.White.copy(alpha = .06f),
                    RoundedCornerShape(14.dp)
                ),
            contentAlignment = Alignment.Center
        ) {

            Text(
                text = icon,
                color = Saffron,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.size(14.dp))

        Column(
            modifier = Modifier.weight(1f)
        ) {

            Text(
                text = title,
                color = White,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = description,
                color = Muted,
                fontSize = 12.sp
            )
        }
    }
}
