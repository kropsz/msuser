# Microserviço de Usuários (CompassUOL)
O projeto foi desenvolvido para o desafio 3 do programa de Bolsas da Compass UOL | Back-end Journey (Spring Boot) - AWS Cloud Context. Esse é um microserviço para gerenciamento de usuários. Quando algum evento como o de registro, login, atualização de usuário e atualização de senha é utilizado é enviado uma mensagem a um outro microserviço [msnotification](https://github.com/kropsz/msnotification). Também possui integração via OpenFeign para utilizar o microserviço de Address para recuperar os dados do `cep` [msaddress](https://github.com/kropsz/msaddress).

## Tecnologias Utilizadas
![MySQL](https://img.shields.io/badge/mysql-%2300f.svg?style=for-the-badge&logo=mysql&logoColor=white)
![RabbitMQ](https://img.shields.io/badge/Rabbitmq-FF6600?style=for-the-badge&logo=rabbitmq&logoColor=white)
![Docker](https://img.shields.io/badge/docker-%230db7ed.svg?style=for-the-badge&logo=docker&logoColor=white)
![Spring](https://img.shields.io/badge/spring-%236DB33F.svg?style=for-the-badge&logo=spring&logoColor=white)
![Java](https://img.shields.io/badge/java-%23ED8B00.svg?style=for-the-badge&logo=openjdk&logoColor=white)
![Apache Maven](https://img.shields.io/badge/Apache%20Maven-C71A36?style=for-the-badge&logo=Apache%20Maven&logoColor=white)

### Dependências
Gerenciador de Dependências : Maven

- Spring Boot 3
- Spring Web
- Spring Validation
- Spring Data JPA
- Spring Doc OpenAPI Swagger
- Spring DevTools
- Spring Cloud OpenFeign
- Lombok
- ModelMapper
- Mockito
- JUnit 5
- Banco de dados H2 
- Banco de dados MySQL

## Endpoints 

## Endpoints
 Métodos | URL | Descrição |
| --- | --- | --- |
| `POST` | /api/users | Cadastrar um novo usuário. |
| `POST` | /api/users/login | Efetuar login com um usuário. |
| `GET` | /api/users/{id} | Buscar um usuário no banco de dados (AUTENTICAÇÃO NECESSÁRIA) |
| `PUT` | /api/users/{id} | Atualizar usuário: atualiza as informações de um usuário. |
| `PUT` | /api/users/{id}/password | Atualizar senha de um usuário. |

## Payloads
* `REQUEST`  - Utilizado para criação de um usuários
```JSON
{
  "firstName": "Ayrton",
  "lastName": "Senna",
  "cpf": "000.000.000-00",
  "birthdate": "0000-00-00",
  "email": "ayrton@email.com",
  "cep": "69999-999",
  "password": "12345678",
  "active": true
}
```
* `RESPONSE`
```JSON
{
  "id": 1,
  "firstName": "Ayrton",
  "lastName": "Senna",
  "cpf": "000.000.000-00",
  "birthdate": "0000-00-00",
  "email": "ayrton@email.com",
  "active": true,
  "address" {
        "city": "São Paulo",
        "state": "SP",
        "cep": "00000000"
  }
}
```

#### Fazer Login de Usuário
* `REQUEST`  - Utilizado para fazer LOGIN de um pedido.
```JSON
{
  "email": "ayrton@email.com",
  "password": "12345678"
}
```
* `RESPONSE`
```JSON
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c"
}
```
#### Atualizar Usuários
* `REQUEST`  - Utilizado para fazer UPDATE de um pedido.
```JSON
  "firstName": "Ayrton",
  "lastName": "Senna",
  "email": "senna@email.com",
  "cep": "69999-999",
  "active": true
```

### Atualizar Senha do Usuário
* `REQUEST`  - Utilizado para fazer UPDATE da senha de um usuário.
```JSON
 {
  "password": "12345678"
 }
```


# Como executar o projeto
### JDK 17
O projeto foi desenvolvido com a linguagem Java, fazendo uso do Java Development Kit (JDK) versão 17. Assim, para rodar o projeto, é necessário ter o JDK 17 instalado no seu computador. Você pode fazer o download do mesmo através do link fornecido -> [Download Java](https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html).

### Escolha a sua IDE:
* VSCode
* IntelliJ
* Spring Tools 4 (Eclipse)
  
### Configurações de Banco de Dados
É necessário  configurar o banco de dados MYSQL:
Navegue até a pasta  `src/main/resources` onde está o arquivo `application.yml` e altere as propriedades:
<div>
<img src="https://github.com/kropsz/compassuol-challenge-e-commerce/assets/114687669/76552929-fd39-4aa1-abe1-f3b381bfe9ee" width="500px" />
</div>

 Substitua as variáveis de ambiente pelos seus dados de acesso ao Banco de Dados: 

*  `url` : Informe o endereço do seu banco de dados MySql.
*  `username`: Informe o nome de usuário do banco de dados MySql.
*  `password`: Informe a senha do seu usuário do banco de dados MySql

### Docker Compose
No terminal navegue até a pasta raiz do projeto e execute o comando `docker-compose up -d` para poder executar o RabbitMQ via Docker

### Conclusão

O microserviço de User é reponsável por lidar com todas as operações relacionadas aos usuários, como criação, atualização, atualização de senha, recuperação. Ele foi implementado usando Spring Boot, com a utilização do RabbitMQ, OpenFeign e possui um serviço de autenticação com o Spring Security.
