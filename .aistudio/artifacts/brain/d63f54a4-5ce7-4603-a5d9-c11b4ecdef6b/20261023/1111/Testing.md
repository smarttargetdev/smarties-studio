---
tests:
  - name: "com.example.ExampleRobolectricTest.read string from context"
    description: "Verifica a resolução do nome do app 'AI Studio Web' em contexto Android via Robolectric."
    result: "PASSED"
    duration_seconds: 3.948
  - name: "com.example.security.SecurityManagerTest.testAllowedDomains"
    description: "Verifica a whitelist de domínios seguros (aistudio.google.com, accounts.google.com, etc.)."
    result: "PASSED"
    duration_seconds: 0.040
  - name: "com.example.security.SecurityManagerTest.testBlockedUntrustedDomains"
    description: "Verifica o bloqueio de domínios maliciosos e phishing."
    result: "PASSED"
    duration_seconds: 0.035
  - name: "com.example.security.SecurityManagerTest.testHttpsValidation"
    description: "Valida obrigatoriedade de HTTPS/TLS."
    result: "PASSED"
    duration_seconds: 0.107
  - name: "com.example.security.SecurityManagerTest.testHostExtraction"
    description: "Verifica a extração de hostname do URL."
    result: "PASSED"
    duration_seconds: 0.032
  - name: "com.example.security.SecurityManagerTest.testEvaluateSecurity"
    description: "Valida o modelo de estado SecurityState."
    result: "PASSED"
    duration_seconds: 0.071
  - name: "com.example.ExampleUnitTest.addition_isCorrect"
    description: "Sanidade aritmética de testes unitários."
    result: "PASSED"
    duration_seconds: 0.001
total_tests: 7
passed: 7
failed: 0
skipped: 0
test_start_time: "2026-10-23T11:15:33-07:00"
test_end_time: "2026-10-23T11:15:54-07:00"
overall_status: "SUCCESS"
---

# Relatório de Testes v1.0.2 - Pull-to-Refresh

## Resumo dos Testes
- **Início:** 2026-10-23T11:15:33-07:00
- **Conclusão:** 2026-10-23T11:15:54-07:00
- **Status Geral:** **SUCCESS (100% dos testes aprovados)**
- **Total de Testes:** 7 executados, 7 aprovados, 0 falhas.

## Tabela de Resultados
| Teste | Descrição | Resultado | Tempo |
|---|---|---|---|
| `ExampleRobolectricTest` | Resolução do nome do app no contexto Android | **PASSED** | 3.948s |
| `SecurityManagerTest.testAllowedDomains` | Validação de domínios permitidos | **PASSED** | 0.040s |
| `SecurityManagerTest.testBlockedUntrustedDomains` | Bloqueio de domínios não confiáveis | **PASSED** | 0.035s |
| `SecurityManagerTest.testHttpsValidation` | Verificação do protocolo HTTPS/TLS | **PASSED** | 0.107s |
| `SecurityManagerTest.testHostExtraction` | Extração de domínio para o badge | **PASSED** | 0.032s |
| `SecurityManagerTest.testEvaluateSecurity` | Modelo SecurityState | **PASSED** | 0.071s |
| `ExampleUnitTest.addition_isCorrect` | Sanidade da suíte de testes | **PASSED** | 0.001s |

## Log Detalhado
```
com.example.ExampleRobolectricTest > read string from context PASSED
com.example.ExampleUnitTest > addition_isCorrect PASSED
com.example.security.SecurityManagerTest > testHttpsValidation PASSED
com.example.security.SecurityManagerTest > testEvaluateSecurity PASSED
com.example.security.SecurityManagerTest > testAllowedDomains PASSED
com.example.security.SecurityManagerTest > testBlockedUntrustedDomains PASSED
com.example.security.SecurityManagerTest > testHostExtraction PASSED

BUILD SUCCESSFUL in 19s
33 actionable tasks: 10 executed, 23 up-to-date
```
