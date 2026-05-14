# WTC CRM Backend

## Tecnologias Utilizadas

* Java 17
* Spring Boot
* Spring Security
* JWT
* MongoDB
* WebSocket
* Maven

---

# Como Executar

## 1. Clonar projeto

```bash
git clone <repositorio>
```

---

## 2. Abrir no IntelliJ

Abrir a pasta do projeto.

---

## 3. Configurar MongoDB

No arquivo:

```properties
application.properties
```

Configurar:

```properties
spring.data.mongodb.uri=mongodb://localhost:27017/wtc-crm
```

---

## 4. Executar aplicação

Rodar:

```text
WtcCrmApplication.java
```

---

## 5. Porta utilizada

```text
http://localhost:8080
```

---

# Funcionalidades

* Autenticação JWT
* Cadastro/Login
* CRUD de Clientes
* Chat
* Segmentação
* Campanhas
* WebSocket
* Logs/Auditoria

---

# Coleções MongoDB

* users
* customers
* messages
