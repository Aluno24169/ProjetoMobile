# Projeto Mobile Stocks

Aplicação Android desenvolvida em Kotlin para acompanhamento de cotações de ações e notícias do mercado financeiro, construída com arquitetura MVVM e interface em tons de azul escuro.

## Funcionalidades

- **Carteira de Ações**: Apresentação de cotações, variações percentuais e armazenamento local de dados via SharedPreferences.
- **Pesquisa e Gestão**: Filtragem em tempo real de títulos e gestão dinâmica da lista de acompanhamento.
- **Análise Detalhada**: Perfil corporativo completo e gráfico histórico de cotações (GraphView).
- **Notícias Financeiras**: Feed de notícias do mercado com carregamento assíncrono de imagens via Picasso e expansão dinâmica de texto.

## Tecnologias Utilizadas

- **Linguagem**: Kotlin
- **Arquitetura**: MVVM (Model-View-ViewModel) e Single Activity
- **Componentes Jetpack**: ViewBinding, ViewModel, LiveData, Navigation Component
- **Rede & Comunicação**: Retrofit 2, OkHttp e Gson
- **Concorrência**: Kotlin Coroutines
- **Bibliotecas Adicionais**: Picasso 2.8 e GraphView 4.2.2

## Testes Unitários

O projeto inclui testes unitários para a verificação do repositório de dados e rotas da API. Para executar os testes:

```bash
./gradlew testDebugUnitTest
```

## Autor

- Filipe Simões
- Rodrigo Rodrigues
