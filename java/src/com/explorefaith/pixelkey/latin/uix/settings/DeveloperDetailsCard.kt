package com.explorefaith.pixelkey.latin.uix.settings

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.painterResource
import com.explorefaith.pixelkey.latin.R
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.explorefaith.pixelkey.latin.uix.theme.SpaceGrotesk
import com.explorefaith.pixelkey.latin.uix.theme.Inter
import androidx.compose.material3.MaterialTheme

private val PixelAccent = Color(0xFF6366F1)

object DeveloperInfo {
    const val NAME = "Eyasin Arafat"
    const val GITHUB = "explorefaith"
    const val GITHUB_URL = "https://github.com/explorefaith"
    const val INSTAGRAM = "explorearafat"
    const val INSTAGRAM_URL = "https://www.instagram.com/explorearafat"
    const val WHATSAPP_NUMBER = "8801610709657"
    const val WHATSAPP_URL = "https://wa.me/8801610709657"

    fun openUrl(context: android.content.Context, url: String) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url)).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
        }
        context.startActivity(intent)
    }
}

@Composable
fun InstagramBrandIcon(modifier: Modifier = Modifier, tint: Color = Color.White) {
    Canvas(modifier = modifier) {
        val w = size.width
        val h = size.height
        val stroke = Stroke(width = w * 0.09f, cap = StrokeCap.Round, join = StrokeJoin.Round)
        drawRoundRect(
            color = tint,
            topLeft = Offset(w * 0.08f, h * 0.08f),
            size = Size(w * 0.84f, h * 0.84f),
            cornerRadius = CornerRadius(w * 0.26f, w * 0.26f),
            style = stroke
        )
        drawCircle(
            color = tint,
            radius = w * 0.19f,
            center = Offset(w * 0.5f, h * 0.5f),
            style = stroke
        )
        drawCircle(
            color = tint,
            radius = w * 0.06f,
            center = Offset(w * 0.71f, h * 0.29f)
        )
    }
}

@Composable
fun WhatsAppBrandIcon(modifier: Modifier = Modifier, tint: Color = Color.White) {
    Canvas(modifier = modifier) {
        val w = size.width
        val h = size.height
        val stroke = Stroke(width = w * 0.09f, cap = StrokeCap.Round, join = StrokeJoin.Round)
        // Speech bubble outline with tail
        val bubble = Path().apply {
            moveTo(w * 0.5f, h * 0.08f)
            cubicTo(w * 0.24f, h * 0.08f, w * 0.06f, h * 0.26f, w * 0.06f, h * 0.48f)
            cubicTo(w * 0.06f, h * 0.58f, w * 0.10f, h * 0.66f, w * 0.16f, h * 0.73f)
            lineTo(w * 0.10f, h * 0.92f)
            lineTo(w * 0.30f, h * 0.86f)
            cubicTo(w * 0.36f, h * 0.89f, w * 0.43f, h * 0.90f, w * 0.50f, h * 0.90f)
            cubicTo(w * 0.76f, h * 0.90f, w * 0.94f, h * 0.71f, w * 0.94f, h * 0.48f)
            cubicTo(w * 0.94f, h * 0.26f, w * 0.76f, h * 0.08f, w * 0.50f, h * 0.08f)
            close()
        }
        drawPath(bubble, color = tint, style = stroke)
        // Phone handset inside bubble
        val handset = Path().apply {
            moveTo(w * 0.33f, h * 0.30f)
            cubicTo(w * 0.37f, h * 0.27f, w * 0.40f, h * 0.29f, w * 0.42f, h * 0.33f)
            lineTo(w * 0.44f, h * 0.38f)
            cubicTo(w * 0.45f, h * 0.41f, w * 0.42f, h * 0.42f, w * 0.41f, h * 0.44f)
            cubicTo(w * 0.40f, h * 0.46f, w * 0.42f, h * 0.50f, w * 0.45f, h * 0.54f)
            cubicTo(w * 0.48f, h * 0.58f, w * 0.53f, h * 0.62f, w * 0.57f, h * 0.63f)
            cubicTo(w * 0.60f, h * 0.63f, w * 0.62f, h * 0.61f, w * 0.64f, h * 0.60f)
            lineTo(w * 0.68f, h * 0.62f)
            cubicTo(w * 0.72f, h * 0.65f, w * 0.74f, h * 0.68f, w * 0.71f, h * 0.72f)
            cubicTo(w * 0.68f, h * 0.76f, w * 0.62f, h * 0.78f, w * 0.55f, h * 0.74f)
            cubicTo(w * 0.44f, h * 0.68f, w * 0.33f, h * 0.56f, w * 0.30f, h * 0.44f)
            cubicTo(w * 0.28f, h * 0.38f, w * 0.30f, h * 0.33f, w * 0.33f, h * 0.30f)
            close()
        }
        drawPath(handset, color = tint)
    }
}

@Composable
fun DeveloperDetailsCard(modifier: Modifier = Modifier) {
    val context = LocalContext.current

    NeuCard(modifier = modifier.fillMaxWidth(), contentPadding = 14.dp) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(42.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(MaterialTheme.colorScheme.surfaceVariant)
                        .border(1.dp, MaterialTheme.colorScheme.outlineVariant, RoundedCornerShape(12.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.people_and_body),
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(22.dp)
                    )
                }

                Column {
                    Text(
                        text = DeveloperInfo.NAME,
                        fontFamily = SpaceGrotesk,
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp,
                        letterSpacing = 0.5.sp,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = "Developer & Designer",
                        fontFamily = Inter,
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            // Social Profile Buttons
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // GitHub
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.surfaceVariant)
                        .border(1.dp, MaterialTheme.colorScheme.outlineVariant, CircleShape)
                        .clickable { DeveloperInfo.openUrl(context, DeveloperInfo.GITHUB_URL) },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.code),
                        contentDescription = "GitHub: ${DeveloperInfo.GITHUB}",
                        tint = Color.White,
                        modifier = Modifier.size(16.dp)
                    )
                }

                // Instagram
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.surfaceVariant)
                        .border(1.dp, MaterialTheme.colorScheme.outlineVariant, CircleShape)
                        .clickable { DeveloperInfo.openUrl(context, DeveloperInfo.INSTAGRAM_URL) },
                    contentAlignment = Alignment.Center
                ) {
                    InstagramBrandIcon(modifier = Modifier.size(16.dp), tint = Color.White)
                }

                // WhatsApp
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.surfaceVariant)
                        .border(1.dp, MaterialTheme.colorScheme.outlineVariant, CircleShape)
                        .clickable { DeveloperInfo.openUrl(context, DeveloperInfo.WHATSAPP_URL) },
                    contentAlignment = Alignment.Center
                ) {
                    WhatsAppBrandIcon(modifier = Modifier.size(16.dp), tint = Color.White)
                }
            }
        }
    }
}
