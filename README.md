Este projeto é um aplicativo feito com o Android Studio e linguagem Java, que consome a API gratuita "REST Countries API" (https://restcountries.com) para exibir informações sobre países do mundo.

Com esse aplicativo, é possível pesquisar países pelo nome ou pelo continente, visualizar uma lista com a bandeira e o nome de cada país e, ao selecionar um país, abrir uma tela com detalhes adicionais. Também é possível marcar um país como favorito e listar todos os países favoritos.

O aplicativo implementa o SharedPreferences do Android, permitindo que a lista de países favoritos seja salva de forma persistente, mesmo após o fechamento do aplicativo.

Além disso, o aplicativo também consome a API gratuita "Lingva Translate API" (https://lingva.ml) para realizar traduções automáticas dos textos, permitindo que a pesquisa possa ser feita fornecendo o nome em português e que os textos apareçam em português.

REST Countries API

Endpoints utilizados:

- `GET /v3.1/name/{name}?fullText=true` - Busca país pelo nome
- `GET /v3.1/region/{region}` - Busca países de uma região

Exemplo de requisições:

- Buscar Brasil: https://restcountries.com/v3.1/name/brazil?fullText=true
- Buscar países da Europa: https://restcountries.com/v3.1/region/europe

Lingva Translate API

Endpoint utilizado:

- `GET /api/v1/{source}/{target}/{query}` - Traduz o texto {query} do idioma {source} para o idioma {target}

Exemplo de requisição:

- Traduzir “Hello_World” do inglês para português: https://lingva.ml/api/v1/en/pt/Hello_World
