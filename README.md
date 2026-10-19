# Kumo — Leitor de Mangá (Android)

App Android (Kotlin + WebView) que empacota o leitor HTML do Kumo. Funciona 100% offline e não pede nenhuma permissão.

## Gerar o APK pelo GitHub

1. Crie um repositório e envie **todos** os arquivos deste zip para a raiz dele, incluindo a pasta oculta `.github`.
   - Pelo navegador: **Add file → Upload files** e arraste as pastas já descompactadas (no celular, use o modo "Site para computador").
   - Se a pasta `.github` não subir: **Add file → Create new file**, digite `.github/workflows/build-apk.yml` como nome e cole o conteúdo do arquivo.
2. Abra a aba **Actions → Build APK → Run workflow** (também roda sozinho a cada push em `main`).
3. Quando terminar (uns 5 a 10 min), abra a execução e baixe o artefato **Kumo-APK** (um zip com `Kumo.apk`).
4. Instale o APK no celular (permita "instalar apps de fontes desconhecidas" para o navegador ou gerenciador de arquivos).

Para publicar em **Releases**, crie uma tag como `v1.0.0`; o workflow anexa o APK automaticamente.

## O que tem aqui

- `app/src/main/assets/index.html`: o leitor (versão adaptada do `kumo_leitor_manga_v2.html`).
- `app/src/main/assets/lib/jszip.min.js`: JSZip 3.10.1 embutido.
- PDF.js 3.11.174: baixado automaticamente durante o build (tarefa `fetchPdfJs`) e embutido no APK. O build precisa de internet; o app depois não.
- `MainActivity.kt`: WebView, seletor de arquivos nativo, tela cheia, botão voltar, tela sempre ligada.

## Dados salvos no aparelho

Mangás importados (páginas, PDFs e capas), favoritos, página atual e ajustes ficam no IndexedDB/localStorage do app. Desinstalar o app apaga tudo. Use o botão 🗑 no cartão para excluir um título.

## Limitações

- Aceita CBZ/ZIP, PDF e imagens. CBR/RAR e CB7 não são suportados.
- PDFs com fontes CJK sem fonte embutida podem aparecer sem texto (cMaps do PDF.js não incluídos).
- CBZ muito grandes são lidos inteiros na memória ao importar.
- O APK é de debug (assinado com a chave de debug): serve para uso pessoal.

## Compilar localmente

Abra a pasta no Android Studio (JDK 17). Na primeira compilação é preciso internet para baixar Gradle, dependências e o PDF.js.
