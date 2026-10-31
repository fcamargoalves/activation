Como Executar
Pré-requisitos
Java 21
Maven 3.9+
Docker
Docker Compose
Executando localmente
Clonar o projeto
Shell
git clone git@github.com:fcamargoalves/activation.git  
ou https://github.com/fcamargoalves/activation
cd activation-service
Executar a aplicação
Shell
mvn spring-boot:run

A aplicação ficará disponível em:

Plain Text
http://localhost:8080
Executando com Docker Compose

Subir banco PostgreSQL e aplicação:

Shell
docker compose up --build

A aplicação ficará disponível em:

Plain Text
http://localhost:8080

Banco PostgreSQL:

Plain Text
localhost:5432

Parar ambiente:

Shell
docker compose down
Executar Testes
Shell
mvn test
Estrutura do Projeto
Plain Text
src/
README.md
pom.xml
Dockerfile
docker-compose.yml
 
docs/
└── adr/
├── ADR-001-duplicate-request-strategy.md
├── ADR-002-provider-integration-strategy.md
└── ADR-003-security-strategy.md
ADRs (Architecture Decision Records)

As principais decisões arquiteturais encontram-se documentadas em:

ADR-001: Estratégia de prevenção de duplicidade
ADR-002: Estratégia de integração com provedor externo
ADR-003: Estratégia de segurança

Localização:

Plain Text
docs/adr/
Decisões Técnicas
Java

Foi utilizado Java 21.

Também foram utilizados recursos modernos da linguagem:

Records para DTOs.
Switch Expressions.
Pattern Matching.
Banco de Dados

Foi utilizado PostgreSQL como banco relacional.

Migrações

Foi utilizado Flyway para versionamento do banco de dados.

Estratégia contra Duplicidade

A proteção contra duplicidade foi implementada através de:

SQL
UNIQUE(external_id)
 

Essa abordagem garante integridade mesmo em cenários concorrentes e ambientes distribuídos com múltiplas instâncias da aplicação.

Quando ocorre tentativa de duplicidade, a API retorna:

HTTP
409 Conflict
 
Controle de Concorrência

Foi utilizado:

Java
@Version

para implementar Lock Otimista e evitar atualizações concorrentes indevidas.

Integração Externa

A integração foi abstraída através da interface:

Java
ProviderClient

permitindo substituição da implementação sem impacto nas regras de negócio.

Tratamento de Erros

Os erros seguem o padrão RFC 9457 utilizando:

Java
ProblemDetail

Exemplo:

JSON
{
"title": "Duplicate Request",
"status": 409,
"detail": "ExternalId already exists"
}
Segurança
Estratégia de Autenticação

Como a API será consumida por outros sistemas, a estratégia recomendada é utilizar OAuth 2.0 Client Credentials Flow, adequado para comunicação entre aplicações (Machine-to-Machine).

Nesse fluxo cada sistema consumidor possui:

Plain Text
client_id
client_secret

e utiliza essas credenciais para obter um JWT junto a um provedor de identidade.

A API atua como:

Plain Text
OAuth 2.0 Resource Server
 

responsável pela validação dos tokens recebidos.

Não é necessário implementar um servidor próprio de autenticação.

Validação do JWT

A API deve validar:

Assinatura

Garante que o token foi emitido por uma autoridade confiável.

Issuer (iss)

Identifica o emissor autorizado.

Audience (aud)

Verifica se o token foi destinado para esta API.

Expiration (exp)

Impede utilização de tokens expirados.

Not Before (nbf)

Define quando o token passa a ser válido.

Scopes

Identificam quais operações o consumidor está autorizado a executar.

Recomenda-se utilizar:

Plain Text
RS256
JWKS

possibilitando rotação de chaves sem compartilhamento de segredos.

Autorização por Escopo

A autorização é baseada no princípio do menor privilégio.

Escopo	Permissãorequests:read	Consultar solicitações
requests:write	Criar solicitações
Criação

Exige:

Plain Text
requests:write
Consulta

Exige:

Plain Text
requests:read
Respostas esperadas

Sem autenticação:

HTTP
401 Unauthorized

Sem permissão suficiente:

HTTP
403 Forbidden
Isolamento entre Consumidores

A identidade do consumidor deve ser obtida de uma claim confiável do JWT.

Nunca através de parâmetros enviados pelo cliente.

Essa abordagem impede que um consumidor consulte recursos pertencentes a outro consumidor.

Alinhado ao OWASP API Security Top 10:

Plain Text
Broken Object Level Authorization
Credenciais do Provedor Externo

As credenciais do provedor não devem ser armazenadas:

No código-fonte
Em arquivos versionados
No Git

Recomenda-se:

Variáveis de ambiente
AWS Secrets Manager
Azure Key Vault
Hashicorp Vault

Boas práticas:

Rotação periódica
Restrição de acesso
HTTPS obrigatório
Não registrar segredos em logs
Renovação automática dos tokens
Proteção Contra Abuso

Recomenda-se:

Limite de tamanho de payload
Rate Limiting
Validação de entrada
Padronização de erros de autenticação
Testes de Segurança
Cenário	Resultado EsperadoRequisição sem token	401
Token inválido	401
Token expirado	401
Escopo insuficiente	403
Consumidor acessando recurso de outro consumidor	Acesso negado
Escopo de leitura tentando criar recurso	403
Consumidor autorizado	200 ou 201

Observação: OAuth2, JWT e Spring Security representam a arquitetura recomendada para produção. A implementação completa da autenticação não faz parte da versão simplificada entregue.

Estratégia de Testes

Nossa estratégia de testes foi desenhada para garantir alta confiabilidade, feedback rápido e validação precisa das regras de negócio.

Testes Unitários
Objetivo

Validar regras de negócio isoladamente, sem subir contexto Spring.

Tecnologias
JUnit 5
Mockito
AssertJ
Cobertura
Transições de status
Regras de domínio
Validações de negócio
Serviços isolados
Benefícios
Execução rápida
Feedback imediato
Fácil manutenção
Testes de Integração
Objetivo

Validar a integração entre Controller, Service, Repository e recursos externos.

Tecnologias
Spring Boot Test
MockMvc
Testcontainers
WireMock
Cobertura
Persistência e Duplicidade

Validação de:

Constraint UNIQUE
Concorrência
Banco PostgreSQL real
Integração Externa

Simulação de:

Sucesso
Erro
Timeout
Segurança

Validação de:

401 Unauthorized
403 Forbidden
Benefícios

Garantem que os componentes funcionem corretamente quando integrados em um ambiente próximo ao real.

Cenário de Análise
O que investigar primeiro

Os sintomas mostram degradação simultânea:

API: 300ms → 2,5s
Banco: 50ms → 1,2s
Provedor: 400ms → 3s

Inicialmente investigaria qual dependência está causando efeito cascata.

Como descobrir a origem do problema

Avaliar:

Logs da aplicação
Latência do banco
Latência do provedor
Pool de conexões
Thread pools
Métricas de infraestrutura
Logs e Métricas

Utilizaria:

Actuator
Micrometer
Prometheus
Grafana

Métricas principais:

Tempo médio de resposta
P95
P99
Throughput
Taxa de erro
Possíveis Melhorias
Índices
Ajuste de pool de conexões
Cache
Circuit Breaker
Retry com Backoff
Processamento assíncrono
Possível Causa Comum

Saturação do serviço externo causando acúmulo de threads e degradação geral da aplicação.

Como Validar a Correção
Comparar métricas antes e depois do deploy
Executar testes de carga
Monitorar indicadores por período determinado
Produção

Em produção a aplicação seria executada em múltiplas instâncias containerizadas.

Componentes recomendados:

Kubernetes
PostgreSQL gerenciado
Load Balancer
Prometheus
Grafana
OpenTelemetry

Todas as configurações seriam fornecidas via variáveis de ambiente ou Secret Manager.

Pontos Pendentes

Por simplificação do desafio não foram implementados:

OAuth2 completo
Keycloak/Auth0
Circuit Breaker
Retry automático
OpenTelemetry
Rate Limiting

Todos foram considerados na arquitetura e documentados para evolução futura.

Melhorias Futuras
Resilience4j
Kafka
Outbox Pattern
Redis
OpenTelemetry
Grafana
Rate Limiting
Feature Flags
Uso de IA

Foi utilizada Inteligência Artificial como apoio para:

Discussão de arquitetura
Revisão de implementação
Estruturação da documentação
Revisão dos testes

Todas as decisões técnicas, implementação e validação final foram realizadas manualmente.