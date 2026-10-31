ADR-002 - Estratégia de integração com provedor externo
Status
Aceito

Contexto
O sistema precisa ativar produtos através de um provedor externo.

Decisão
Foi criada uma abstração ProviderClient.

A implementação atual utiliza MockProviderClient para simplificar os testes e permitir futura substituição por WebClient.

Consequências
Vantagens:

Baixo acoplamento.
Facilidade para testes.
Fácil substituição da implementação.
Evolução futura
Substituir MockProviderClient por implementação HTTP utilizando Spring WebClient e Resilience4j.