package com.example.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.outlined.CleaningServices
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.Shield
import androidx.compose.material.icons.outlined.ZoomIn
import androidx.compose.material.icons.outlined.ZoomOut
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.ui.theme.StudioSecurityGreen
import com.example.webview.WebViewState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AiStudioTopBar(
    state: WebViewState,
    onDesktopModeToggle: () -> Unit,
    onOpenQuickNav: () -> Unit,
    onOpenSecurityInfo: () -> Unit,
    onReload: () -> Unit,
    onClearCache: () -> Unit,
    onZoomIn: () -> Unit,
    onZoomOut: () -> Unit,
    onGoogleSignIn: () -> Unit,
    modifier: Modifier = Modifier
) {
    var showMenu by remember { mutableStateOf(false) }

    Column(modifier = modifier.fillMaxWidth()) {
        TopAppBar(
            title = {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_sparkle_gemini),
                        contentDescription = "AI Studio Logo",
                        tint = Color.Unspecified,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))

                    Surface(
                        shape = RoundedCornerShape(20.dp),
                        color = MaterialTheme.colorScheme.surfaceVariant,
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(20.dp))
                            .clickable { onOpenSecurityInfo() }
                            .testTag("url_security_badge")
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                        ) {
                            Icon(
                                imageVector = if (state.securityState.isHttps) Icons.Outlined.Lock else Icons.Outlined.Shield,
                                contentDescription = "Security Status",
                                tint = if (state.securityState.isHttps) StudioSecurityGreen else MaterialTheme.colorScheme.error,
                                modifier = Modifier.size(15.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = state.securityState.host,
                                style = MaterialTheme.typography.labelMedium.copy(
                                    fontWeight = FontWeight.SemiBold,
                                    fontSize = 12.sp
                                ),
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            },
            actions = {
                // Desktop / Mobile Mode Toggle
                IconButton(
                    onClick = onDesktopModeToggle,
                    modifier = Modifier.testTag("action_toggle_desktop_top")
                ) {
                    Icon(
                        painter = painterResource(
                            id = if (state.isDesktopMode) R.drawable.ic_desktop_mode else R.drawable.ic_mobile_mode
                        ),
                        contentDescription = if (state.isDesktopMode) "Modo Desktop Ativo" else "Modo Mobile Ativo",
                        tint = if (state.isDesktopMode) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(22.dp)
                    )
                }

                // Quick Navigation Sheet
                IconButton(
                    onClick = onOpenQuickNav,
                    modifier = Modifier.testTag("action_quick_nav")
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_prompt_chat),
                        contentDescription = "Atalhos Rápidos",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(22.dp)
                    )
                }

                // Overflow Menu
                Box {
                    IconButton(
                        onClick = { showMenu = true },
                        modifier = Modifier.testTag("action_overflow_menu")
                    ) {
                        Icon(
                            imageVector = Icons.Default.MoreVert,
                            contentDescription = "Opções",
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }

                    DropdownMenu(
                        expanded = showMenu,
                        onDismissRequest = { showMenu = false }
                    ) {
                        DropdownMenuItem(
                            text = { Text("Entrar com Conta Google") },
                            leadingIcon = {
                                Icon(Icons.Default.AccountCircle, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                            },
                            onClick = {
                                showMenu = false
                                onGoogleSignIn()
                            }
                        )
                        HorizontalDivider()
                        DropdownMenuItem(
                            text = { Text("Recarregar Página") },
                            leadingIcon = {
                                Icon(Icons.Default.Refresh, contentDescription = null)
                            },
                            onClick = {
                                showMenu = false
                                onReload()
                            }
                        )
                        DropdownMenuItem(
                            text = { Text("Segurança da Conexão") },
                            leadingIcon = {
                                Icon(Icons.Outlined.Shield, contentDescription = null, tint = StudioSecurityGreen)
                            },
                            onClick = {
                                showMenu = false
                                onOpenSecurityInfo()
                            }
                        )
                        DropdownMenuItem(
                            text = { Text("Aumentar Zoom") },
                            leadingIcon = {
                                Icon(Icons.Outlined.ZoomIn, contentDescription = null)
                            },
                            onClick = {
                                showMenu = false
                                onZoomIn()
                            }
                        )
                        DropdownMenuItem(
                            text = { Text("Diminuir Zoom") },
                            leadingIcon = {
                                Icon(Icons.Outlined.ZoomOut, contentDescription = null)
                            },
                            onClick = {
                                showMenu = false
                                onZoomOut()
                            }
                        )
                        DropdownMenuItem(
                            text = { Text("Limpar Cache & Cookies") },
                            leadingIcon = {
                                Icon(Icons.Outlined.CleaningServices, contentDescription = null)
                            },
                            onClick = {
                                showMenu = false
                                onClearCache()
                            }
                        )
                    }
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.surface
            )
        )
    }
}
