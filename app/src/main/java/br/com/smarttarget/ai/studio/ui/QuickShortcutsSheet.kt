package br.com.smarttarget.ai.studio.ui

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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.com.smarttarget.ai.studio.R

data class QuickNavShortcut(
    val title: String,
    val subtitle: String,
    val url: String,
    val iconResId: Int,
    val tag: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuickShortcutsSheet(
    onDismiss: () -> Unit,
    onNavigateTo: (String) -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    val shortcuts = listOf(
        QuickNavShortcut(
            title = "Entrar com Conta Google",
            subtitle = "Autenticar com a conta Google do dispositivo no AI Studio",
            url = "https://accounts.google.com/ServiceLogin?service=wise&continue=https://aistudio.google.com",
            iconResId = R.drawable.ic_shield_secure,
            tag = "shortcut_google_signin"
        ),
        QuickNavShortcut(
            title = "Novo Prompt de Chat",
            subtitle = "Criar novo chat interativo com Gemini 1.5 e 2.0",
            url = "https://aistudio.google.com/prompts/new_chat",
            iconResId = R.drawable.ic_prompt_chat,
            tag = "shortcut_new_chat"
        ),
        QuickNavShortcut(
            title = "Prompt Livre / Estruturado",
            subtitle = "Crie prompts de formato livre com parâmetros e exemplos",
            url = "https://aistudio.google.com/prompts/new_freeform",
            iconResId = R.drawable.ic_sparkle_gemini,
            tag = "shortcut_new_freeform"
        ),
        QuickNavShortcut(
            title = "Meus Prompts (Biblioteca)",
            subtitle = "Acessar seus prompts salvos no Google Drive e na nuvem",
            url = "https://aistudio.google.com/library",
            iconResId = R.drawable.ic_library_prompts,
            tag = "shortcut_library"
        ),
        QuickNavShortcut(
            title = "Modelos Ajustados (Tuned Models)",
            subtitle = "Gerenciar ajuste fino e modelos personalizados",
            url = "https://aistudio.google.com/tuned_models",
            iconResId = R.drawable.ic_tuning,
            tag = "shortcut_tuning"
        ),
        QuickNavShortcut(
            title = "Obter Chave de API (API Keys)",
            subtitle = "Criar e visualizar chaves de API para desenvolvimento",
            url = "https://aistudio.google.com/apikey",
            iconResId = R.drawable.ic_key_api,
            tag = "shortcut_apikey"
        ),
        QuickNavShortcut(
            title = "Documentação Gemini AI",
            subtitle = "Guias oficiais, referências de API e SDKs",
            url = "https://ai.google.dev/docs",
            iconResId = R.drawable.ic_shield_secure,
            tag = "shortcut_docs"
        )
    )

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = MaterialTheme.colorScheme.surface
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 8.dp)
        ) {
            Text(
                text = "Atalhos do Google AI Studio",
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp
                ),
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = "Navegue instantaneamente para seções principais da plataforma",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(16.dp))
            HorizontalDivider(color = MaterialTheme.colorScheme.surfaceVariant)
            Spacer(modifier = Modifier.height(8.dp))

            shortcuts.forEach { item ->
                Surface(
                    color = Color.Transparent,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .clickable {
                            onNavigateTo(item.url)
                            onDismiss()
                        }
                        .testTag(item.tag)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(vertical = 12.dp, horizontal = 8.dp)
                    ) {
                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = MaterialTheme.colorScheme.primaryContainer,
                            modifier = Modifier.size(42.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    painter = painterResource(id = item.iconResId),
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.onPrimaryContainer,
                                    modifier = Modifier.size(22.dp)
                                )
                            }
                        }
                        Spacer(modifier = Modifier.width(16.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = item.title,
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.SemiBold,
                                    fontSize = 15.sp
                                ),
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = item.subtitle,
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}
