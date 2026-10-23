---
tests:
  - name: "br.com.smarttarget.ai.studio.auth.AuthManagerTest.testGoogleChooserUrlWithEmail"
    description: "Valida geração de URL do AccountChooser com o e-mail da conta Google."
    result: "PASSED"
    duration_seconds: 0.089
  - name: "br.com.smarttarget.ai.studio.auth.AuthManagerTest.testGoogleChooserUrlWithoutEmail"
    description: "Valida fallback para ServiceLogin padrão."
    result: "PASSED"
    duration_seconds: 0.088
  - name: "br.com.smarttarget.ai.studio.ExampleRobolectricTest.read string from context"
    description: "Verifica a resolução da string do aplicativo no namespace br.com.smarttarget.ai.studio."
    result: "PASSED"
    duration_seconds: 4.558
  - name: "br.com.smarttarget.ai.studio.security.SecurityManagerTest.testAllowedDomains"
    description: "Valida lista de permissão de domínios seguros do AI Studio."
    result: "PASSED"
    duration_seconds: 0.065
  - name: "br.com.smarttarget.ai.studio.security.SecurityManagerTest.testBlockedUntrustedDomains"
    description: "Valida bloqueio de domínios não autorizados e phishing."
    result: "PASSED"
    duration_seconds: 0.040
  - name: "br.com.smarttarget.ai.studio.security.SecurityManagerTest.testHttpsValidation"
    description: "Valida obrigatoriedade de TLS/HTTPS."
    result: "PASSED"
    duration_seconds: 0.105
  - name: "br.com.smarttarget.ai.studio.security.SecurityManagerTest.testHostExtraction"
    description: "Valida extração do domínio para o badge de segurança."
    result: "PASSED"
    duration_seconds: 0.059
  - name: "br.com.smarttarget.ai.studio.security.SecurityManagerTest.testEvaluateSecurity"
    description: "Valida a integridade do modelo SecurityState."
    result: "PASSED"
    duration_seconds: 0.096
  - name: "br.com.smarttarget.ai.studio.ExampleUnitTest.addition_isCorrect"
    description: "Sanidade da suíte de testes."
    result: "PASSED"
    duration_seconds: 0.001
total_tests: 9
passed: 9
failed: 0
skipped: 0
test_start_time: "2026-10-23T11:40:04-07:00"
test_end_time: "2026-10-23T11:40:24-07:00"
overall_status: "SUCCESS"
---

# Teste - 1.0.5

## Resumo dos Testes
- **Início:** 2026-10-23T11:40:04-07:00
- **Conclusão:** 2026-10-23T11:40:24-07:00
- **Status Geral:** **SUCCESS (100% dos testes aprovados)**
- **Total de Testes:** 9 executados, 9 aprovados, 0 falhas, 0 pulados.

## Resultados Detalhados
| Teste | Descrição | Resultado | Tempo |
|---|---|---|---|
| `AuthManagerTest.testGoogleChooserUrlWithEmail` | URL do AccountChooser com email | **PASSED** | 0.089s |
| `AuthManagerTest.testGoogleChooserUrlWithoutEmail` | URL de fallback do ServiceLogin | **PASSED** | 0.088s |
| `ExampleRobolectricTest` | Resolução do nome do app no novo namespace | **PASSED** | 4.558s |
| `SecurityManagerTest.testAllowedDomains` | Whitelist de domínios seguros | **PASSED** | 0.065s |
| `SecurityManagerTest.testBlockedUntrustedDomains` | Bloqueio de domínios maliciosos | **PASSED** | 0.040s |
| `SecurityManagerTest.testHttpsValidation` | Exigência estrita de HTTPS/TLS | **PASSED** | 0.105s |
| `SecurityManagerTest.testHostExtraction` | Extração do domínio para o badge | **PASSED** | 0.059s |
| `SecurityManagerTest.testEvaluateSecurity` | Avaliação do SecurityState | **PASSED** | 0.096s |
| `ExampleUnitTest.addition_isCorrect` | Sanidade do test runner | **PASSED** | 0.001s |

## Log Detalhado
```
br.com.smarttarget.ai.studio.ExampleRobolectricTest > read string from context PASSED
br.com.smarttarget.ai.studio.ExampleUnitTest > addition_isCorrect PASSED
br.com.smarttarget.ai.studio.auth.AuthManagerTest > testGoogleChooserUrlWithEmail PASSED
br.com.smarttarget.ai.studio.auth.AuthManagerTest > testGoogleChooserUrlWithoutEmail PASSED
br.com.smarttarget.ai.studio.security.SecurityManagerTest > testHttpsValidation PASSED
br.com.smarttarget.ai.studio.security.SecurityManagerTest > testEvaluateSecurity PASSED
br.com.smarttarget.ai.studio.security.SecurityManagerTest > testAllowedDomains PASSED
br.com.smarttarget.ai.studio.security.SecurityManagerTest > testBlockedUntrustedDomains PASSED
br.com.smarttarget.ai.studio.security.SecurityManagerTest > testHostExtraction PASSED

BUILD SUCCESSFUL in 19s
33 actionable tasks: 9 executed, 24 up-to-date
```
