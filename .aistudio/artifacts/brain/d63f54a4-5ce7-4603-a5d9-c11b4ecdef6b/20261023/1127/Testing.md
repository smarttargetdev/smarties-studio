---
tests:
  - name: "com.example.auth.AuthManagerTest.testGoogleChooserUrlWithEmail"
    description: "Valida a montagem do URL do AccountChooser com o e-mail do usuário obtido via Credential Manager."
    result: "PASSED"
    duration_seconds: 0.063
  - name: "com.example.auth.AuthManagerTest.testGoogleChooserUrlWithoutEmail"
    description: "Valida o fallback para o endpoint padrão de login Google (ServiceLogin)."
    result: "PASSED"
    duration_seconds: 0.038
  - name: "com.example.ExampleRobolectricTest.read string from context"
    description: "Verifica a resolução correta da string do aplicativo no contexto Android."
    result: "PASSED"
    duration_seconds: 4.053
  - name: "com.example.security.SecurityManagerTest.testAllowedDomains"
    description: "Valida a lista de permissão estrita para domínios do Google AI Studio."
    result: "PASSED"
    duration_seconds: 0.058
  - name: "com.example.security.SecurityManagerTest.testBlockedUntrustedDomains"
    description: "Garante o bloqueio e isolamento de domínios maliciosos fora da sandbox."
    result: "PASSED"
    duration_seconds: 0.052
  - name: "com.example.security.SecurityManagerTest.testHttpsValidation"
    description: "Garante a obrigatoriedade de TLS/HTTPS."
    result: "PASSED"
    duration_seconds: 0.080
  - name: "com.example.security.SecurityManagerTest.testHostExtraction"
    description: "Verifica extração de host para exibição do selo de segurança."
    result: "PASSED"
    duration_seconds: 0.030
  - name: "com.example.security.SecurityManagerTest.testEvaluateSecurity"
    description: "Valida a construção e integridade do modelo SecurityState."
    result: "PASSED"
    duration_seconds: 0.045
  - name: "com.example.ExampleUnitTest.addition_isCorrect"
    description: "Sanidade do runner de testes."
    result: "PASSED"
    duration_seconds: 0.001
total_tests: 9
passed: 9
failed: 0
skipped: 0
test_start_time: "2026-10-23T11:31:17-07:00"
test_end_time: "2026-10-23T11:31:36-07:00"
overall_status: "SUCCESS"
---

# Relatório de Testes v1.0.3 - BackHandler, Cache e Autenticação Google

## Resumo dos Testes
- **Início:** 2026-10-23T11:31:17-07:00
- **Conclusão:** 2026-10-23T11:31:36-07:00
- **Status Geral:** **SUCCESS (100% dos testes aprovados)**
- **Total de Testes:** 9 executados, 9 aprovados, 0 falhas, 0 pulados.

## Resultados Detalhados
| Teste | Descrição | Resultado | Tempo |
|---|---|---|---|
| `AuthManagerTest.testGoogleChooserUrlWithEmail` | URL do AccountChooser com email da conta Google | **PASSED** | 0.063s |
| `AuthManagerTest.testGoogleChooserUrlWithoutEmail` | URL de fallback do ServiceLogin Google | **PASSED** | 0.038s |
| `ExampleRobolectricTest` | Resolução do nome do app no contexto Android | **PASSED** | 4.053s |
| `SecurityManagerTest.testAllowedDomains` | Whitelist de domínios seguros do AI Studio | **PASSED** | 0.058s |
| `SecurityManagerTest.testBlockedUntrustedDomains` | Bloqueio de domínios externos e phishing | **PASSED** | 0.052s |
| `SecurityManagerTest.testHttpsValidation` | Exigência estrita de HTTPS/TLS | **PASSED** | 0.080s |
| `SecurityManagerTest.testHostExtraction` | Extração do domínio para o badge | **PASSED** | 0.030s |
| `SecurityManagerTest.testEvaluateSecurity` | Avaliação do SecurityState | **PASSED** | 0.045s |
| `ExampleUnitTest.addition_isCorrect` | Sanidade da suite de testes | **PASSED** | 0.001s |

## Log Detalhado
```
com.example.ExampleRobolectricTest > read string from context PASSED
com.example.ExampleUnitTest > addition_isCorrect PASSED
com.example.auth.AuthManagerTest > testGoogleChooserUrlWithEmail PASSED
com.example.auth.AuthManagerTest > testGoogleChooserUrlWithoutEmail PASSED
com.example.security.SecurityManagerTest > testHttpsValidation PASSED
com.example.security.SecurityManagerTest > testEvaluateSecurity PASSED
com.example.security.SecurityManagerTest > testAllowedDomains PASSED
com.example.security.SecurityManagerTest > testBlockedUntrustedDomains PASSED
com.example.security.SecurityManagerTest > testHostExtraction PASSED

BUILD SUCCESSFUL in 18s
33 actionable tasks: 9 executed, 24 up-to-date
```
