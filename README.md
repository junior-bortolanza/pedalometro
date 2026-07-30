![Logo](https://github.com/user-attachments/assets/88225696-8925-4367-96a5-1cbdab42b6f8)
# 🚴 Pedalometro

### O que é

O Pedalometro pega a previsão do tempo de uma cidade e decide, sem meio-termo, se hoje dá pra pedalar. Ele busca a localização da cidade, consulta a previsão hora a hora e calcula um score de 0 a 100 com base em chance de chuva e velocidade do vento, nada de olhar app de tempo genérico e tentar adivinhar se vai molhar no meio do rolê.


## Tech Stack

**Backend:** : Java 21, Spring Boot 3, Maven

**Frontend::** Angular (em construção)

**Resiliência::** Resilience4j (Circuit Breaker + Retry) nas chamadas às APIs externas

## Funcionalidades

- Busca de cidade com geocodificação automática
- Score de pedalabilidade (0-100) hora a hora, calculado a partir de chuva e vento
- Identificação do pior horário do dia pra pedalar (`badTime`)
- Mensagens de veredito que mudam conforme o score
- Tratamento de falha nas APIs externas sem derrubar a aplicação

## 🛡️ Arquitetura e resiliência
 
O Pedalometro depende de duas APIs externas (geocodificação e previsão do tempo, ambas da Open-Meteo), e dependência externa é sempre um ponto de falha. Por isso as chamadas passam por Resilience4j:
 
- **Retry** reexecuta a chamada automaticamente em falhas transitórias, antes de considerar erro de verdade.
- **Circuit Breaker** abre o circuito se as falhas persistirem, parando de bater na API instável em vez de insistir e travar a resposta.
- **Fallback** entra quando o circuito abre ou a chamada falha, lançando uma exceção tratada de forma centralizada, o usuário recebe um erro claro, não um 500 genérico.


## Estrutura

```
pedalometro/
├── backend/
│   ├── src/main/java/com/pedalometro/weather_api/
│   │   ├── client/       # Geocoding, OpenMeteo
│   │   ├── config/       # RestClient, propriedades
│   │   ├── controller/   # Endpoints + tratamento global de exceções
│   │   ├── dto/
│   │   ├── exceptions/
│   │   └── service/      # Score, mensagens, orquestração
│   ├── src/main/resources/application.yml
│   └── pom.xml
└── frontend/             # Angular (em construção)
```
 



## 🧪 Testes
 
Cobertura de testes unitários com JUnit 5 + Mockito. Para rodar:
 
```bash
./mvnw test
```
## ⚙️ Configuração
 
As faixas e penalidades do score ficam em `application.yml`, prefixo `pedalometro`:
 
```yaml
pedalometro:
  client:
    timeout:
      connect: 3000
      read: 5000
  score:
    rain:
      high: 70
      medium: 40
      low: 10
      high-penalty: 40
      medium-penalty: 20
      low-penalty: 5
    wind:
      high: 30
      medium: 20
      low: 10
      high-penalty: 30
      medium-penalty: 15
      low-penalty: 5
    status:
      excellent: 80
      good: 60
      regular: 40
```
## 📡 Endpoint
 
```
GET /weather?city={cidade}
```
 
```json
{
  "city": "Sorocaba",
  "status": "BOM",
  "score": 78,
  "message": "Bora, Fuzilos! Hoje até o Alemão pode sair sem medo de chuva.",
  "rainChance": 10,
  "windSpeed": 12.5,
  "sunrise": "06:12",
  "sunset": "17:48",
  "badTime": "15:00",
  "hourlyForecasts": [
    { "time": "08:00", "rainChance": 5, "windSpeed": 8.2, "score": 90 }
  ]
}
```
 
Erros retornam um `ErrorDTO` padronizado:
 
| Exceção | Status |
|---|---|
| `CityNotFoundException` | 404 |
| `WeatherDataNotFoundException` | 404 |
| `InvalidDateException` | 400 |
| `InvalidWeatherDataException` | 502 |
| `ExternalApiException` | 503 |
## Screenshots

![App Screenshot](https://dummyimage.com/468x300?text=App+Screenshot+Here)

## 📌 Próximos passos
 
- [ ] Finalizar frontend (Angular)
- [ ] Integrar API de clima mais robusta pra enriquecer os dados
- [ ] Subir camada de banco de dados persistente (hoje é tudo em memória)
- [ ] Swagger/OpenAPI
- [ ] Docker
- [ ] Deploy em produção
## Autor

- [@junior-bortolanza](https://www.github.com/junior-bortolanza)


## Feedback

Se tiver algum comentário, entre em contato através de gbortolanzajr@gmail.com
