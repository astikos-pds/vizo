# Vizo

Vizo é uma Progressive Web App (PWA) que conecta cidadãos e gestores públicos para transformar a cidade em um espaço mais democrático. Com um mapa colaborativo, análise inteligente de textos e imagens e um painel de monitoramento eficiente, o Vizo se torna mais do que uma ferramenta tecnológica, mas um dispositivo de reterritorialização, democracia espacial e cidadania digital.

## Sumário

- [Visão Geral](#visão-geral)
- [Tecnologias](#tecnologias)
- [Documentação](#documentação)

## Visão Geral

Vizo permite que qualquer pessoa possa relatar problemas urbanos — como buracos, falta de iluminação e lixo acumulado — de forma simples, rápida e eficaz. Os relatos são analisados, classificados e agrupados com base no texto, na foto e na geolocalização, compondo um mapa que reescreve a cidade a partir das vozes de quem a habita.

Além disso, o Vizo conta com um sistema de credibilidade que valoriza a participação cidadã e ajuda a destacar os relatos mais confiáveis.

## Tecnologias

Vizo é desenvolvido com uma arquitetura modular que combina diferentes tecnologias de acordo com a finalidade de cada serviço:

- **Front-end**: Nuxt é um framework baseado em Vue com o superconjunto TypeScript para construção de aplicações web performáticas e escaláveis, com suporte a _File-based Routing_, _Server Side Rendering_ (SSR) e _Search Engine Optimization_ (SEO) e ao desenvolvimento de uma _Progressive Web Application_ (PWA).

  ![Nuxtjs](https://img.shields.io/badge/Nuxt-002E3B?style=for-the-badge&logo=nuxt&logoColor=#00DC82)
  ![Vue.js](https://img.shields.io/badge/vuejs-%2335495e.svg?style=for-the-badge&logo=vuedotjs&logoColor=%234FC08D)
  ![TypeScript](https://img.shields.io/badge/typescript-%23007ACC.svg?style=for-the-badge&logo=typescript&logoColor=white)
  ![TailwindCSS](https://img.shields.io/badge/tailwindcss-%2338B2AC.svg?style=for-the-badge&logo=tailwind-css&logoColor=white)

- **Back-end**

  - **API principal**: Spring Boot é um framework do ecossistema Java robusto para criação de APIs RESTful seguras e escaláveis, utilizado para autenticação, implementação dos principais casos de uso do domínio, como a criação de um relato, e integração geral do sistema.

    ![Spring](https://img.shields.io/badge/spring-%236DB33F.svg?style=for-the-badge&logo=spring&logoColor=white)
    ![Java](https://img.shields.io/badge/java-%23ED8B00.svg?style=for-the-badge&logo=openjdk&logoColor=white)
    ![Gradle](https://img.shields.io/badge/Gradle-02303A.svg?style=for-the-badge&logo=Gradle&logoColor=white)

  - **API de análise de relatos**: FastAPI é um microframework Python, que será usado para processamento de dados, análise textual, classificação de relatos e geoprocessamento.

    ![FastAPI](https://img.shields.io/badge/FastAPI-005571?style=for-the-badge&logo=fastapi)
    ![Python](https://img.shields.io/badge/python-3670A0?style=for-the-badge&logo=python&logoColor=ffffff)
    ![Poetry](https://img.shields.io/badge/Poetry-%233B82F6.svg?style=for-the-badge&logo=poetry&logoColor=0B3D8D)

- **Banco de Dados**: PostgreSQL é um banco relacional, com a extensão PostGIS para suporte a dados geoespaciais, essencial para o mapeamento das ocorrências urbanas.

  ![Postgres](https://img.shields.io/badge/postgres-%23316192.svg?style=for-the-badge&logo=postgresql&logoColor=white)

- **Deploy**: ferramentas e plataformas utilizadas para a implantação das aplicações.

  ![Docker](https://img.shields.io/badge/docker-%230db7ed.svg?style=for-the-badge&logo=docker&logoColor=white)
  ![Render](https://img.shields.io/badge/Render-%46E3B7.svg?style=for-the-badge&logo=render&logoColor=white)
  ![Vercel](https://img.shields.io/badge/vercel-%23000000.svg?style=for-the-badge&logo=vercel&logoColor=white)

## Documentação

Abaixo estão listadas as entregas que compõem a documentação técnica e conceitual do projeto Vizo. Cada uma delas descreve diferentes estágios de planejamento, experimentação e implementação da solução:

- **Proposta Inicial**: parte conceitual do documento que define o problema, os objetivos e a justificativa do projeto.

  - Documentação:
    - [Original](docs/1%20-%20Proposta%20Inicial/original/documento.pdf)
    - [Corrigido](docs/1%20-%20Proposta%20Inicial/corrigido/documento.pdf)
  - Apresentação:
    - [Original](docs/1%20-%20Proposta%20Inicial/original/slides.pdf)
    - [Corrigido](docs/1%20-%20Proposta%20Inicial/corrigido/slides.pdf)

- **Proof of Concept (PoC)**: em português, "Prova de Conceito", é demonstração da funcionalidade principal do projeto: o reporte de problemas.

  - [Apresentação](<docs/2%20-%20Proof%20of%20Concept%20(PoC)/slides.pdf>)

- **Minimum Viable Product**: em português, "Produto Mínimo Viável", é a implementação das funcionalidades principais, integradas num sistema coeso.

  - [Apresentação](<docs/3%20-%20Minimum%20Viable%20Product%20(MVP)/slides.pdf>)

- **Apresentação Final**: projeto finalizado, com ambos documento e aplicação completos.
  - [Documentação](docs/4%20-%20Apresentação%20Final/documento.pdf)
  - [Apresenção](docs/4%20-%20Apresentação%20Final/slides.pdf)
