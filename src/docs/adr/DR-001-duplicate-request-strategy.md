DR-001 - Estratégia para prevenção de duplicidade
Status
Aceito

Contexto
A API recebe solicitações de ativação identificadas por um externalId.

O desafio exige que múltiplas requisições utilizando o mesmo externalId não gerem registros duplicados, inclusive em cenários concorrentes ou com múltiplas instâncias da aplicação.

Decisão
Foi adotada proteção no nível do banco de dados através de uma restrição UNIQUE para o campo external_id.

Também foi implementado tratamento da exceção DataIntegrityViolationException para retorno HTTP 409 Conflict.

Consequências
Vantagens:

Funciona em múltiplas instâncias.
Funciona em cenários concorrentes.
Simples de manter.
Não depende de sincronização em memória.
Desvantagens:

A exceção ocorre no momento da persistência.
Alternativas avaliadas
Verificação prévia com findByExternalId (rejeitada).
Locks distribuídos (complexidade desnecessária para o desafio).