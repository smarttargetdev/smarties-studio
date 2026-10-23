# Documentação Técnica dos Componentes - AI Studio Web App (v1.0.3)

Este documento descreve os 4 novos recursos implementados no aplicativo conforme as diretrizes técnicas solicitadas:

---

## 1. BackHandler Customizado do Sistema (`MainActivity.kt`)
- **Componente:** `androidx.activity.compose.BackHandler`
- **Comportamento e Características:**
  - **Navegação no Histórico:** Enquanto `webView.canGoBack()` for verdadeiro, o botão "Voltar" do sistema (físico ou por gestos nas bordas do Android) consome o evento e chama `webView.goBack()`, retornando à página visitada anteriormente sem sair do aplicativo.
  - **Prevenção de Fechamento Acidental (Double-Back to Exit):**
    - Quando o usuário atinge a raiz do histórico do AI Studio (`!webView.canGoBack()`), o primeiro toque no botão voltar exibe uma mensagem Toast informativa: *"Pressione 'Voltar' novamente para sair do AI Studio"*.
    - O aplicativo só é encerrado se o usuário pressionar o botão voltar uma segunda vez dentro do intervalo de 2 segundos (`currentTime - lastBackPressTime < 2000L`).
    - Isso protege o usuário contra a perda acidental de prompts complexos ou chats em andamento.

---

## 2. Configurações de DOM Storage e Cache de Aplicação (`AiStudioWebView.kt`)
- **Características:**
  - **DOM Storage:** `settings.domStorageEnabled = true` habilitado para suporte pleno a `localStorage` e `sessionStorage`, fundamentais para salvar localmente estados temporários de chat e preferências de tema no Google AI Studio.
  - **Database API:** `settings.databaseEnabled = true` para suporte a `IndexedDB`.
  - **Cache HTTP / Application Cache Adaptativo:**
    ```kotlin
    val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as? ConnectivityManager
    val isConnected = cm?.activeNetworkInfo?.isConnectedOrConnecting == true
    settings.cacheMode = if (isConnected) WebSettings.LOAD_DEFAULT else WebSettings.LOAD_CACHE_ELSE_NETWORK
    ```
    - Quando o dispositivo está online, o WebView opera com `LOAD_DEFAULT`, revalidando recursos com os servidores do Google e aproveitando caches HTTP locais.
    - Se o dispositivo perder a conexão, o WebView alterna dinamicamente para `LOAD_CACHE_ELSE_NETWORK`, permitindo carregar scripts e layouts cacheados instantaneamente sem tela em branco.
    - Criação de diretório de cache isolado em `context.cacheDir/webview_cache`.

---

## 3. Barra de Progresso Linear no Topo da WebView (`MainActivity.kt`)
- **Componente:** `androidx.compose.material3.LinearProgressIndicator`
- **Posicionamento:** Posicionado diretamente acima do container da WebView (`AiStudioWebView`), no topo da área de conteúdo.
- **Animação e Precisão:**
  - Integração com `WebChromeClient.onProgressChanged` emitindo valores inteiros de 0 a 100%.
  - Transição contínua com `animateFloatAsState(targetValue = state.progress / 100f, animationSpec = tween(200, easing = LinearEasing))`.
  - Visível unicamente enquanto `state.isLoading && state.progress < 100`, ocultando-se suavemente ao concluir a renderização da página.

---

## 4. Autenticação com Contas Google do Dispositivo (`com.example.auth.AuthManager`)
- **Componente:** Jetpack `CredentialManager` (`androidx.credentials`) com `GetSignInWithGoogleOption` e `GoogleIdTokenCredential`.
- **Fluxo de Autenticação:**
  1. O usuário aciona o botão **"Entrar com Conta Google"** na barra superior (TopBar) ou nos atalhos rápidos.
  2. O aplicativo invoca `AuthManager.signInWithDeviceGoogleAccount(context)`, abrindo a interface nativa do Android (BottomSheet de Contas do Sistema) que lista as contas Google ativas no aparelho.
  3. Ao selecionar a conta desejada (ex: `usuario@gmail.com`), o app obtém a identidade e direciona a WebView para o endpoint seguro do Google AccountChooser:
     `https://accounts.google.com/AccountChooser?Email=usuario@gmail.com&continue=https://aistudio.google.com`.
  4. **Proteção contra bloqueio OAuth:** Configurado o User-Agent oficial do Chrome Mobile para evitar a mensagem de erro `disallowed_useragent` imposta pelo Google em WebViews com headers customizados.
  5. **Sincronização de Sessão:** `CookieManager.getInstance().flush()` e `setAcceptThirdPartyCookies(webView, true)` garantem a permanência e segurança da sessão autenticada.
