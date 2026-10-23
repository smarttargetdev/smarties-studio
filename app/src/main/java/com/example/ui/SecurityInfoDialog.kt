package com.example.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.security.SecurityState
import com.example.ui.theme.StudioSecurityGreen

@Composable
fun SecurityInfoDialog(
    securityState: SecurityState,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = if (securityState.isHttps) Icons.Default.Lock else Icons.Default.Warning,
                    contentDescription = null,
                    tint = if (securityState.isHttps) StudioSecurityGreen else MaterialTheme.colorScheme.error,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = if (securityState.isHttps) "Conexão Criptografada" else "Alerta de Conexão",
                    style = MaterialTheme.typography.titleLarge.copy(fontSize = 18.sp),
                    fontWeight = FontWeight.Bold
                )
            }
        },
        text = {
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "Status de Segurança Chromium",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Host atual: ${securityState.host}",
                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium),
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(12.dp))

                Card(
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant
                    )
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        SecurityFeatureRow(
                            icon = Icons.Default.CheckCircle,
                            tint = StudioSecurityGreen,
                            title = "TLS / HTTPS Rigoroso",
                            desc = "Certificado de segurança verificado. Tráfego não seguro (HTTP) é bloqueado."
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        SecurityFeatureRow(
                            icon = Icons.Default.CheckCircle,
                            tint = StudioSecurityGreen,
                            title = "Isolamento Chromium Sandbox",
                            desc = "Proteção em sandbox nativo do motor Chromium com navegação restrita aos domínios do Google."
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        SecurityFeatureRow(
                            icon = Icons.Default.CheckCircle,
                            tint = StudioSecurityGreen,
                            title = "Chromium Safe Browsing",
                            desc = "Detecção em tempo real contra phishing e malware ativada."
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        SecurityFeatureRow(
                            icon = Icons.Default.CheckCircle,
                            tint = StudioSecurityGreen,
                            title = "Autenticação Segura Google",
                            desc = "Sessão e cookies de autenticação isolados e protegidos contra vazamento entre apps."
                        )
                    }
                }

                if (securityState.sslErrorDetails != null) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Erro: ${securityState.sslErrorDetails}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }
        },
        confirmButton = {
            Button(
                onClick = onDismiss,
                modifier = Modifier.testTag("security_dialog_ok_button")
            ) {
                Text("Entendido")
            }
        }
    )
}

@Composable
private fun SecurityFeatureRow(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    tint: Color,
    title: String,
    desc: String
) {
    Row(verticalAlignment = Alignment.Top) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = tint,
            modifier = Modifier
                .padding(top = 2.dp)
                .size(16.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Column {
            Text(
                text = title,
                style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = desc,
                style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.8f)
            )
        }
    }
}
