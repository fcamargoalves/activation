ADR-003 - Estratégia de segurança
Status
Aceito

Contexto
A API será consumida por outros sistemas.

Decisão
Adotar OAuth 2.0 Client Credentials Flow utilizando JWT.

A aplicação atua como OAuth2 Resource Server.

Escopos
requests:read

requests:write

Motivação
O fluxo Client Credentials é adequado para comunicação sistema a sistema.

Evolução futura
Integração com Auth0, Keycloak, Azure AD ou AWS Cognito.