# Documentação Técnica dos Componentes - AI Studio Web App (v1.0.2)

Este documento detalha todos os componentes técnicos do aplicativo, com foco aprofundado na implementação do gesto nativo de **Pull-to-Refresh** no componente `AiStudioWebView`.

---

## 1. Módulo WebView & Gesto Pull-to-Refresh (`com.example.ui.AiStudioWebView`)

### 1.1 Integração do `SwipeRefreshLayout`
- **Componente:** `androidx.swiperefreshlayout.widget.SwipeRefreshLayout`
- **Desafio Técnico Resolvido:** O site `aistudio.google.com` contém áreas de rolagem densas (históricos longos de prompts, editores de texto, seletores de modelos, parâmetros deslizantes e saídas de código). Um container de pull-to-refresh comum interceptaria toques involuntários enquanto o usuário tenta rolar a página para cima.
- **Detecção de Rolagem Nativa (`setOnChildScrollUpCallback`):**
  ```kotlin
  setOnChildScrollUpCallback { _, _ ->
      webView.canScrollVertically(-1)
  }
  ```
  - Se `webView.canScrollVertically(-1)` retornar `true`, significa que a página possui conteúdo acima e o usuário está no meio do documento. Nesse caso, o `SwipeRefreshLayout` não consome o evento e permite a rolagem fluida normal da página.
  - Somente quando o usuário atinge o topo exato da página (`canScrollVertically(-1) == false`), puxar para baixo ativa o gesto de recarregamento.

### 1.2 Paleta de Cores e Estilo Visual do Indicador
- As cores do spinner circular seguem a identidade visual do Google AI Studio:
  - Azul Primário: `#1B6EF3`
  - Roxo Secundário: `#B588F8`
  - Ciano de Destaque: `#70D2FA`
  - Fundo do Indicador: `#1E1F20` (compatível com modo escuro)

### 1.3 Sincronização de Estado Reativo
- O estado de carregamento do WebView (`state.isLoading`) atualiza diretamente `swipeRefresh.isRefreshing`.
- Ao acionar o gesto, `onRefresh()` invoca `webView.reload()`. Quando a página conclui (`onPageFinished`), o indicador é finalizado automaticamente.

---

## 2. Demais Componentes do Sistema

### 2.1 `AiStudioTopBar`
- Barra superior M3 com badge interativo de segurança TLS, alternador de modo Desktop/Mobile, botão de atalhos rápidos e menu suspenso de ações (recarregar, zoom, limpar cache).

### 2.2 `AiStudioBottomNav`
- Barra de navegação inferior com botões de histórico Voltar, Avançar, Início, Recarregar e alternador de Modo Desktop com badge indicador "PC".

### 2.3 `QuickShortcutsSheet`
- Gaveta modal inferior com atalhos diretos para:
  - Novo Chat (`/prompts/new_chat`)
  - Prompt Livre (`/prompts/new_freeform`)
  - Biblioteca (`/library`)
  - Modelos Ajustados (`/tuned_models`)
  - Chaves de API (`/apikey`)
  - Documentação Oficial (`ai.google.dev/docs`)

### 2.4 `SecurityManager` & `SecurityInfoDialog`
- Sandbox de isolamento de domínios, validação HTTPS obrigatória, Safe Browsing ativo e diálogo detalhado de auditoria de criptografia e proteção de cookies.

### 2.5 `ErrorScreen`
- Tela amigável de contingência com suporte a reconexão e nova tentativa.
