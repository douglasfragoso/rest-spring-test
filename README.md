# API Projeto do curso JUnit 5 e Mockito

[![NPM](https://img.shields.io/npm/l/react)](https://github.com/douglasfragoso/intensivo-java-spring/blob/main/LICENSE) 

# Sobre o projeto
O projeto consiste numa API RESTFul com Java e Spring boot para fazer requisições HTTP de uma entidade Person. 

# Tecnologias utilizadas

## Linguagem de Programação:

- **Java 21**: Linguagem de programação principal usada para desenvolver a aplicação.

## Ferramenta de Build:

- **Maven**: Ferramenta de automação de build usada para gerenciar dependências, compilar o código, empacotar a aplicação e executar testes.

## Framework:

- **Spring Boot**: Framework popular para construção de microsserviços e aplicações web. Proporciona uma experiência de desenvolvimento rápida ao simplificar a configuração e oferecer muitos recursos pré-construídos.

## Persistência:

- **Spring Data JPA**: Biblioteca do Spring que simplifica a interação com bancos de dados relacionais usando JPA (Java Persistence API). Fornece abstração e simplifica operações de acesso a dados.

## Bancos de Dados:

- **H2 Database**: Banco de dados em memória frequentemente usado para desenvolvimento e testes. É conveniente pois não requer instalação separada e funciona inteiramente em memória.
- **MySQL**: Sistema de gerenciamento de banco de dados relacional (RDBMS) popular e open-source usado para implantações em produção.

## Teste do Lado do Cliente:

- **Postman**: Ferramenta popular para fazer requisições HTTP e testar APIs. Pode ser usada para enviar requisições à aplicação desenvolvida e verificar respostas.

### Testes Unitários e de Integração

- **JUnit 5**: Framework para execução de testes unitários. O JUnit 5 fornece a estrutura para escrever e executar testes automatizados de unidade, tornando o código mais robusto e confiável.
  
- **Mockito**: Framework para criação de mocks (objetos simulados) para testes unitários. O Mockito permite simular comportamentos de dependências e testar unidades isoladas de código.

- **TestContainers**: Biblioteca para executar containers Docker de maneira simples dentro dos testes. Com o TestContainers, podemos usar containers para simular um banco de dados ou outro serviço necessário durante os testes.

- **Rest-Assured**: Ferramenta para realizar testes de APIs REST. Rest-Assured permite enviar requisições HTTP e verificar respostas de forma fluida e legível, facilitando os testes de endpoints da API.

## Relatório de Testes do Código:

- **JaCoCo** (Java Code Covarage): Plugin Maven para cobertura de código de testes, gerando relatórios em HTML, XML e CSV. 

- **Maven Surefire Plugin**: Plugin Maven para execução de testes e criar relatórios em XML e HTML.

## Documentação de API:

- **Swagger**: Framework open-source para criação de documentação de API interativa. Pode gerar automaticamente documentação baseada em anotações de código e permite que os desenvolvedores explorem e compreendam facilmente a API de uma aplicação.

## Ferramentas de Simplificação de Código:

- **Bean Validation**: Padrão JSR (Java Specification Request) para validação de JavaBeans. Fornece anotações que podem ser usadas para definir regras de validação para campos em suas classes.
- **Lombok**: Biblioteca que pode gerar automaticamente código boilerplate, como getters, setters e métodos equals, reduzindo a quantidade de código que você precisa escrever.

# Modelos de domínios 

![Modelo de domínio](<Classe UML.jpeg>)

# Perfis de projeto

## Perfil de desenvolvimento e testes
 - test
 - H2 Database

### Como Utilizar

1. Clone o projeto para sua IDE preferida.
2. Execute o projeto Spring Boot com o no perfil de test aplication.yml:

```yml
profiles:
    active: test 
```

3. Importe a collection para o Postman através do arquivo [Collection](<Spring Test.postman_collection.json>).
4. Utilize as rotas fornecidas para realizar requisições ao sistema.
5. Verifique a documentação no Swagger acessando: [Swagger UI](http://localhost:8080/swagger-ui/index.html)
6. No terminal, adicione o comando Maven a seguir para fazer os testes:

```sh
mvn clean test
```

automaticamente será criado os relatórios do JaCoCo, inclusive em HTML e do Surefire em XML.


[Caminho JaCoCo Report](rest-spring-test/target/site/jacoco/index.html)

7. No terminal, adicione o comando Maven a seguir para criar relatório HTML do Surefire:

```sh
mvn site 
```

[Caminho Surefire Report](rest-spring-test/target/site/surefire-report.html)

## Perfil de homologação
 - prod
 - MySQL

### Como Utilizar

1. Clone o projeto para sua IDE preferida.
2. Abra o MySQL Workbench
  - certifique-se das propriedades no [application-prod.yml](rest-spring-test/src/main/resources/application-prod.yml)
3. Execute o projeto Spring Boot com o no perfil de prod aplication.yml:

```yml
profiles:
    active: prod
```

4. Importe a collection para o Postman através do arquivo [Collection](<Spring Test.postman_collection.json>).
5. Utilize as rotas fornecidas para realizar requisições ao sistema.
6. Verifique a documentação no Swagger acessando: [Swagger UI](http://localhost:8080/swagger-ui/index.html)
7. No terminal, adicione o comando Maven a seguir para fazer os testes:

```sh
mvn clean test
```

automaticamente será criado os relatórios do JaCoCo, inclusive em HTML e do Surefire em XML.


[Caminho JaCoCo Report](rest-spring-test/target/site/jacoco/index.html)

8. No terminal, adicione o comando Maven a seguir para criar relatório HTML do Surefire:

```sh
mvn site 
```

[Caminho Surefire Report](rest-spring-test/target/site/surefire-report.html)




  Feito com carinho por Douglas Fragoso 👊
