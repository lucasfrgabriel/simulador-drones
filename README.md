# Simulador de Entregas com Drones

Este projeto é uma API REST desenvolvida em Java com Spring Boot que simula a logística de entregas utilizando uma frota de drones. O sistema gerencia drones, pedidos e as entregas, alocando os pedidos aos drones disponíveis com base em critérios como capacidade de peso, autonomia de voo e prioridade do pedido.

---

## Funcionalidades Principais

- **Gerenciamento de Drones**: Cadastre novos drones, cada um com sua capacidade de carga e autonomia de voo. O sistema acompanha o status de cada drone (Ocioso, Carregando, Em Voo, Retornando) e seu nível de bateria.
- **Criação de Pedidos**: Crie novos pedidos especificando peso, coordenadas de entrega e nível de prioridade (Baixa, Média, Alta).
- **Alocação Inteligente**: Ao criar um novo pedido, o serviço de logística tenta alocá-lo automaticamente:
    1.  Primeiro, tenta agrupar o pedido em uma entrega já existente que esteja sendo preparada, respeitando os limites do drone.
    2.  Se não for possível agrupar, procura por um drone ocioso que possa realizar a entrega e cria uma nova viagem.
- **Simulação de Entrega**: Inicie e finalize entregas. O sistema atualiza o status dos pedidos e do drone, calcula o consumo de bateria com base na distância percorrida e registra os horários de início e fim.
- **Cálculo de Rota**: Calcula a distância total de uma entrega, considerando a rota da base (0,0) aos pontos de entrega (ordenados por prioridade) e o retorno à base.

## Tecnologias Utilizadas

- **Java 21**: Versão da linguagem Java utilizada no projeto.
- **Spring Boot 3**: Framework principal para a construção da aplicação e da API REST.
- **Spring Data JPA**: Para persistência de dados e abstração do acesso ao banco de dados.
- **H2 Database**: Banco de dados em memória para facilitar o desenvolvimento e testes, sem necessidade de configuração externa.
- **Maven**: Gerenciador de dependências e build do projeto.
- **Lombok**: Para reduzir o código boilerplate em classes de modelo.
- **JUnit 5 & Mockito**: Para a escrita de testes unitários e de integração.

## Como Executar o Projeto

### Pré-requisitos

* JDK 21 instalado e configurado no seu sistema.

### Executando a Simulação

1.  **Clone o repositório:**
    ```bash
    git clone https://github.com/lucasfrgabriel/simulador-drones.git
    cd simulador-drones
    ```

2.  **Execute a aplicação via Maven Wrapper:**
    O Maven Wrapper (`mvnw`) permite que você execute o projeto sem precisar instalar o Maven globalmente.

    *No Linux ou Mac:*
    ```bash
    ./mvnw spring-boot:run
    ```

    *No Windows:*
    ```bash
    .\mvnw.cmd spring-boot:run
    ```

3.  A aplicação estará disponível em `http://localhost:8080`.


4. **Acesse o Console H2 (Opcional):**
    Como o projeto utiliza um banco de dados em memória H2, você pode acessar seu console via navegador para visualizar as tabelas e dados:
    - **URL:** `http://localhost:8080/h2-console`
    - **JDBC URL:** `jdbc:h2:mem:testdb`
    - **User Name:** `sa`
    - **Password:** (deixe em branco)

## API Endpoints

Aqui estão os principais endpoints da API para interagir com o simulador.

### Drones (`/drones`)

- `POST /drones`
    - Cria um novo drone.
    - **Body (Exemplo):**
      ```json
      {
        "capacidadeMaximaKg": 12.0,
        "autonomiaMaximaKm": 30.0
      }
      ```

- `GET /drones`
    - Lista todos os drones cadastrados no sistema.


- `GET /drones/{id}`
    - Retorna os detalhes de um drone específico.


- `POST /drones/{id}/retornar`
    - Marca o drone como `IDLE` (ocioso) e recarrega sua bateria para 100%, simulando que ele retornou à base e está pronto para uma nova entrega. Isso só é possível se o status do drone for `RETORNANDO`.

### Pedidos (`/pedidos`)

- `POST /pedidos`
    - Cria um novo pedido, que será automaticamente processado pelo serviço de logística para alocação.
    - **Body (Exemplo):**
      ```json
      {
        "pesoKg": 5.5,
        "prioridade": "ALTA",
        "posX": 10,
        "posY": -15
      }
      ```

- `GET /pedidos`
    - Lista todos os pedidos cadastrados no sistema.


- `GET /pedidos/{id}`
    - Retorna os detalhes de um pedido específico.

### Entregas (`/entregas`)

- `GET /entregas`
    - Lista todas as entregas cadastradas no sistema.


- `GET /entregas/{id}`
    - Retorna os detalhes de uma entrega específica.


- `GET /entregas/{id}/rota`
    - Calcula e exibe os detalhes da rota para uma entrega específica, incluindo a distância total e se a autonomia do drone é suficiente.


- `POST /entregas/{id}/iniciar`
    - Inicia o processo de entrega. O status da entrega muda para `EM_CURSO`, o status do drone para `EM_VOO`, e os pedidos associados para `A_CAMINHO`.


- `POST /entregas/{id}/finalizar`
    - Finaliza uma entrega que está em curso. O status da entrega muda para `FINALIZADA`, o do drone para `RETORNANDO`, e o dos pedidos para `ENTREGUE`. A bateria do drone é consumida com base na distância percorrida.

## Executando os Testes

O projeto possui uma suíte de testes unitários que cobre a lógica de negócio nas camadas de domain, utils e service. Para executá-la, utilize o seguinte comando na raiz do projeto:


*No Linux ou Mac:*
```bash
./mvnw test
```

*No Windows:*
```bash
.\mvnw.cmd test
```

## Estrutura do Projeto

- `src/main/java/com/api/simulador_drones`
    - `controller`: Controladores REST que expõem os endpoints da API.
    - `domain`: Entidades JPA (`Drone`, `Pedido`, `Entrega`) e Enums de status.
    - `dto`: Objetos de Transferência de Dados (DTOs) para as requisições e respostas da API.
    - `repository`: Interfaces do Spring Data JPA para interação com o banco de dados.
    - `service`: Contém toda a lógica de negócio, incluindo a alocação de pedidos (`LogisticaService`).
    - `util`: Classes utilitárias, como a `CalculadoraDistancia`.
