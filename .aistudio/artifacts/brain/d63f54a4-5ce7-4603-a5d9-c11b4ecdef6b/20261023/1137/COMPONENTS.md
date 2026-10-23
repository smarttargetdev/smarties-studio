# Documentação - 1.0.5

Este documento técnico detalha os componentes da arquitetura do **Smarties IA Studio** (versão 1.0.5) com o namespace oficial `br.com.smarttarget.ai.studio`.

---

## 1. Tela de Apresentação (`SplashScreen.kt`)
- **Arquivo:** `app/src/main/java/br/com/smarttarget/ai/studio/ui/SplashScreen.kt`
- **Finalidade:** Tela de abertura inicial com identidade visual da Smart Target, carregamento assíncrono em segundo plano e exibição da versão.
- **Características Técnicas:**
  - **Asset SVG Exclusivo (`ic_smarties_ia_studio.xml`):**
    - Desenvolvido no padrão VectorDrawable com anéis de mira inteligente (Smart Target), miras de canto em `#1B6EF3` e núcleo multifacetado de inteligência artificial com gradiente ciano (`#70D2FA`), roxo (`#B588F8`) e branco puro.
  - **Animação Fluida:**
    - Efeito contínuo de respiração e pulsação suave utilizando `rememberInfiniteTransition` e `animateFloat(0.95f -> 1.05f)` com interpolação `FastOutSlowInEasing`.
  - **Exibição Dinâmica da Versão:**
    - Badge estilizado com `BuildConfig.VERSION_NAME` exibindo `"Versão 1.0.5"`.
  - **Preload Transparente:**
    - O Chromium `WebView` inicia a conexão e resolução de `aistudio.google.com` em paralelo por baixo da splash screen; após 2000ms, ocorre uma transição suave com `fadeOut(tween(400))` revelando a interface já pronta.

---

## 2. Motor Chromium & Configuração da WebView (`AiStudioWebView.kt`)
- **Arquivo:** `app/src/main/java/br/com/smarttarget/ai/studio/ui/AiStudioWebView.kt`
- **Características Técnicas:**
  - **Gesto Nativo Pull-to-Refresh:**
    - Encapsulado em `SwipeRefreshLayout` com detecção inteligente de rolagem (`setOnChildScrollUpCallback { _, _ -> webView.canScrollVertically(-1) }`). Impede acionamentos acidentais enquanto o usuário rola o chat no meio da página.
  - **DOM Storage & Database Storage:**
    - `domStorageEnabled = true` (localStorage / sessionStorage) e `databaseEnabled = true` (IndexedDB) para rápida restauração de rascunhos de prompts e tokens de navegação.
  - **Cache de Aplicação Adaptativo:**
    - Avalia a conectividade em tempo real: usa `LOAD_DEFAULT` quando online e `LOAD_CACHE_ELSE_NETWORK` quando offline, permitindo inicialização ultrarrápida.
  - **User-Agent Otimizado para Autenticação Google:**
    - Utiliza o User-Agent oficial do Chrome Mobile para contornar o bloqueio `disallowed_useragent` em telas de login do Google.

---

## 3. Gestão do Botão Voltar do Sistema (`BackHandler`)
- **Arquivo:** `app/src/main/java/br/com/smarttarget/ai/studio/MainActivity.kt`
- **Características Técnicas:**
  - Se `webView.canGoBack()` for verdadeiro, o botão voltar retrocede no histórico de navegação interna.
  - Se estiver na raiz (`!webView.canGoBack()`), o primeiro toque aciona um Toast informativo (*"Pressione 'Voltar' novamente para sair do AI Studio"*); somente um segundo toque dentro de 2000ms fecha a aplicação.

---

## 4. Módulo de Autenticação com Conta Google (`AuthManager.kt`)
- **Arquivo:** `app/src/main/java/br/com/smarttarget/ai/studio/auth/AuthManager.kt`
- **Características Técnicas:**
  - Integração com Jetpack `CredentialManager` e `GetSignInWithGoogleOption`.
  - Apresenta o BottomSheet nativo do Android com as contas Google já configuradas no dispositivo móvel.
  - Gera o endpoint seguro de redirecionamento `AccountChooser` com o email selecionado.

---

## 5. Módulo de Segurança & Governança de Rede (`SecurityManager.kt`)
- **Arquivo:** `app/src/main/java/br/com/smarttarget/ai/studio/security/SecurityManager.kt`
- **Características Técnicas:**
  - Whitelist restrita de domínios seguros autorizados do Google AI Studio (`aistudio.google.com`, `makersuite.google.com`, `accounts.google.com`, `ai.google.dev`, `developers.google.com`).
  - Rejeição e cancelamento automático de certificados SSL inválidos (`onReceivedSslError`).
  - Chromium Safe Browsing ativo para mitigação de ameaças em tempo real.
