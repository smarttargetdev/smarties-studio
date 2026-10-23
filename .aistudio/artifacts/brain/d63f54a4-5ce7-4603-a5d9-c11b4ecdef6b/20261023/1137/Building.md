---
workspace_config:
  root_project_name: "AI Studio Web"
  gradle_plugins:
    - "com.android.application: 9.1.1"
    - "org.jetbrains.kotlin.plugin.compose: 2.2.10"
    - "com.google.devtools.ksp: 2.3.5"
    - "secrets-gradle-plugin: 2.0.1"
    - "google-services: 4.5.0"
  settings_file: "settings.gradle.kts"
  root_build_file: "build.gradle.kts"
projects_config:
  - project: ":app"
    namespace: "br.com.smarttarget.ai.studio"
    application_id: "br.com.smarttarget.ai.studio"
    min_sdk: 24
    target_sdk: 36
    compile_sdk: "release-36"
    version_code: 5
    version_name: "1.0.5"
    build_features:
      compose: true
      build_config: true
build_start_time: "2026-10-23T11:39:15-07:00"
build_end_time: "2026-10-23T11:40:02-07:00"
status: "SUCCESS"
apk_output: "karaoke-studio.apk"
apk_size: "24MB"
---

# Compilação - 1.0.5

## Resumo das Configurações do Workspace
- **Nome do Projeto Raiz:** AI Studio Web
- **Módulo Principal:** `:app`
- **Namespace Oficial:** `br.com.smarttarget.ai.studio`
- **Application ID:** `br.com.smarttarget.ai.studio`
- **Versão:** `1.0.5` (Padrão `{major.minor.release}`)
- **Version Code:** `5`
- **Min SDK:** 24
- **Target SDK:** 36
- **Compile SDK:** 36
- **Início do Build:** 2026-10-23T11:39:15-07:00
- **Conclusão do Build:** 2026-10-23T11:40:02-07:00
- **Status:** **SUCCESS**
- **Arquivo APK Gerado:** `karaoke-studio.apk` (24MB)

## Configuração do Gradle
```kotlin
android {
  namespace = "br.com.smarttarget.ai.studio"
  compileSdk = 36

  defaultConfig {
    applicationId = "br.com.smarttarget.ai.studio"
    minSdk = 24
    targetSdk = 36
    versionCode = 5
    versionName = "1.0.5"
  }
}
```

## Log de Compilação
```
> Task :app:preBuild UP-TO-DATE
> Task :app:preDebugBuild UP-TO-DATE
> Task :app:generateDebugBuildConfig UP-TO-DATE
> Task :app:kspDebugKotlin UP-TO-DATE
> Task :app:compileDebugKotlin UP-TO-DATE
> Task :app:compileDebugJavaWithJavac UP-TO-DATE
> Task :app:packageDebug UP-TO-DATE
> Task :app:assembleDebug UP-TO-DATE

BUILD SUCCESSFUL
APK gerado com sucesso em: app/build/outputs/apk/debug/app-debug.apk
Copiado para artefatos como: karaoke-studio.apk
```
