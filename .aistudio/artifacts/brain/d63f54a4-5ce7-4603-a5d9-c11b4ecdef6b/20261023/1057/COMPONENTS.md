# Documentação Técnica dos Componentes - AI Studio Web App

Este documento detalha todos os componentes arquiteturais, módulos de segurança, clientes Chromium e elementos de interface construídos para executar o **Google AI Studio** (`aistudio.google.com`) no Android como uma Web App rápida, segura e com recursos completos.

---

## 1. Módulo de Segurança (`com.example.security`)

### 1.1 `SecurityManager`
- **Arquivo:** `app/src/main/java/com/example/security/SecurityManager.kt`
- **Finalidade:** Responsável pela governança do perímetro de segurança, validação de URLs, proteção Zero-Trust e isolamento sandbox.
- **Características Técnicas:**
  - **Whitelist de Domínios Confiáveis:**
    - `aistudio.google.com` (Aplicação principal do AI Studio)
    - `makersuite.google.com` (Domínio legado redirecionado)
    - `accounts.google.com` e `myaccount.google.com` (Fluxo OAuth e autenticação Google)
    - `ai.google.dev` e `developers.google.com` (Documentação e referências)
    - `gstatic.com`, `googleapis.com`, `googleusercontent.com` (CDNs de scripts, estilos e recursos)
  - **Método `isAllowedDomain(url: String)`:** Analisa o esquema e o host da URI de destino; subdomínios autorizados recebem permissão interna de carregamento.
  - **Método `isSecureHttps(url: String)`:** Exige estritamente conexões cifradas com HTTPS (TLS 1.2+).
  - **Método `evaluateSecurity(url: String, sslError: String?)`:** Produz uma estrutura imutável `SecurityState` contendo o status de cifragem, o hostname formatado e a ativação do Safe Browsing.

### 1.2 `SecurityState`
- **Estrutura:** Data class contendo `currentUrl`, `host`, `isHttps`, `isAllowedDomain`, `isSafeBrowsingActive` e `sslErrorDetails`.
- **Uso:** Alimenta em tempo real os badges de status e o diálogo de auditoria de conexão.

---

## 2. Motor Chromium & Clientes Web (`com.example.webview`)

### 2.1 `ChromiumWebViewClient`
- **Arquivo:** `app/src/main/java/com/example/webview/ChromiumWebViewClient.kt`
- **Finalidade:** Interceptação de tráfego, prevenção de phishing, tratamento de certificados e ciclo de vida de páginas.
- **Especificidades:**
  - **Sandbox de Navegação (`shouldOverrideUrlLoading`):** Se o usuário clicar em um link para um domínio fora do ecossistema Google, a navegação não ocorre no contexto privilegiado do app; o link é delegado com segurança ao navegador padrão do sistema Android via `Intent(Intent.ACTION_VIEW)`.
  - **TLS Enforcement (`onReceivedSslError`):** Invoca `handler.cancel()` imediatamente diante de falhas de certificado SSL, impossibilitando ataques Man-in-the-Middle (MitM).
  - **Safe Browsing (`onSafeBrowsingHit`):** Integrado à API Safe Browsing do Chromium para abortar carregamento de páginas sinalizadas como ameaça.

### 2.2 `ChromiumWebChromeClient`
- **Arquivo:** `app/src/main/java/com/example/webview/ChromiumWebChromeClient.kt`
- **Finalidade:** Recursos avançados do navegador, telemetria de renderização e suporte a fluxos multimodais.
- **Especificidades:**
  - **Upload de Arquivos Multimodais (`onShowFileChooser`):** Intercepta seletores `<input type="file">` e dispara o Intent de seleção de arquivos do Android, permitindo o upload de imagens, PDFs, áudio e códigos nos prompts do AI Studio.
  - **Popups de Autenticação (`onCreateWindow`):** Gerencia janelas secundárias usadas por telas de consentimento do Google Sign-In, redirecionando o transporte para a sessão ativa.
  - **Captura de Áudio (`onPermissionRequest`):** Captura pedidos de microfone do WebRTC para prompts de voz do Gemini, integrando-se com permissões de runtime do Android.

### 2.3 `WebViewState`
- **Arquivo:** `app/src/main/java/com/example/webview/WebViewState.kt`
- **Constantes:** `DESKTOP_USER_AGENT` configurado com `Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/130.0.0.0 Safari/537.36`.
- **Propriedades:** `url`, `title`, `progress`, `isLoading`, `canGoBack`, `canGoForward`, `isDesktopMode`, `hasNetworkError`, `securityState`.

---

## 3. Componentes de Interface de Usuário (Jetpack Compose)

### 3.1 `AiStudioTopBar`
- **Arquivo:** `app/src/main/java/com/example/ui/AiStudioTopBar.kt`
- **Características:**
  - Barra superior no padrão Material 3.
  - **Badge de Segurança Interativo:** Apresenta o ícone de cadeado verde com o domínio verificado. Ao clicar, abre o diálogo de auditoria de segurança.
  - **Botão de Alternância de Modo Desktop:** Ícone dinâmico (monitor/smartphone) que permite alternar a renderização do AI Studio instantaneamente.
  - **Atalhos Rápidos:** Botão dedicado para abrir a gaveta de links rápidos.
  - **Menu de Opções:** Recarregar, Ajuste de Zoom (ZoomIn / ZoomOut) e Limpeza de Cache & Cookies.
  - **Barra de Progresso:** `LinearProgressIndicator` com animação contínua durante o carregamento de páginas.

### 3.2 `AiStudioBottomNav`
- **Arquivo:** `app/src/main/java/com/example/ui/AiStudioBottomNav.kt`
- **Características:**
  - Barra de controle inferior com alinhamento ergonômico.
  - Botões de navegação rápida: **Voltar**, **Avançar**, **Home (Início)**, **Recarregar** e **Modo Desktop (com Badge "PC")**.
  - Estados ativados/desativados vinculados a `canGoBack` e `canGoForward`.

### 3.3 `QuickShortcutsSheet`
- **Arquivo:** `app/src/main/java/com/example/ui/QuickShortcutsSheet.kt`
- **Características:**
  - `ModalBottomSheet` do Material 3 com atalhos diretos para os principais módulos do AI Studio:
    1. **Novo Prompt de Chat:** `https://aistudio.google.com/prompts/new_chat`
    2. **Prompt Livre / Estruturado:** `https://aistudio.google.com/prompts/new_freeform`
    3. **Meus Prompts (Biblioteca):** `https://aistudio.google.com/library`
    4. **Modelos Ajustados (Tuning):** `https://aistudio.google.com/tuned_models`
    5. **Obter Chave de API:** `https://aistudio.google.com/apikey`
    6. **Documentação Oficial Gemini:** `https://ai.google.dev/docs`

### 3.4 `SecurityInfoDialog`
- **Arquivo:** `app/src/main/java/com/example/ui/SecurityInfoDialog.kt`
- **Características:**
  - Diálogo de transparência e auditoria de segurança.
  - Exibe status de:
    - Criptografia TLS e protocolo HTTPS.
    - Isolamento em Sandbox nativo do Chromium.
    - Chromium Safe Browsing ativo.
    - Proteção e isolamento de cookies de sessão Google.

### 3.5 `ErrorScreen`
- **Arquivo:** `app/src/main/java/com/example/ui/ErrorScreen.kt`
- **Características:**
  - Tela de contingência para desconexões ou ausência de sinal de rede.
  - Ações claras de reteste de conexão e retorno à página inicial segura.

### 3.6 `AiStudioWebView`
- **Arquivo:** `app/src/main/java/com/example/ui/AiStudioWebView.kt`
- **Configurações Chromium:**
  - `javaScriptEnabled = true`
  - `domStorageEnabled = true` (Autosalvamento de prompts e roteamento SPA)
  - `databaseEnabled = true`
  - `mixedContentMode = MIXED_CONTENT_NEVER_ALLOW`
  - `setAcceptThirdPartyCookies = true` (Autenticação federada Google Accounts)
  - `DownloadListener` com integração nativa ao `DownloadManager` do Android para exportação de código e prompts.

---

## 4. Recursos Vetoriais SVG & Ícones Criados

Todos os ícones utilizam VectorDrawables baseados em vetores SVG:
1. `ic_shield_secure.xml`: Escudo verde de verificação de integridade TLS.
2. `ic_desktop_mode.xml`: Monitor de modo desktop para alta densidade de painéis.
3. `ic_mobile_mode.xml`: Smartphone para modo responsivo compacto.
4. `ic_sparkle_gemini.xml`: Estrela de quatro pontas e brilho oficial do Gemini / AI Studio com gradiente azul e roxo.
5. `ic_key_api.xml`: Chave de autenticação para atalho de API Keys.
6. `ic_prompt_chat.xml`: Balão de chat para novos prompts.
7. `ic_library_prompts.xml`: Pasta de biblioteca de prompts.
8. `ic_tuning.xml`: Controles deslizantes de afinação de modelos.
