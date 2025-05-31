# 🔧 Sistema de Gerenciamento de Ordem de Serviço

## 📋 Índice
- [Visão Geral](#-visão-geral)
- [Pré-requisitos](#-pré-requisitos)
- [Configuração do Projeto](#-configuração-do-projeto)
- [Configuração no VS Code](#-configuração-no-vs-code)
- [Executando a Aplicação](#-executando-a-aplicação)
- [Usando a Aplicação](#-usando-a-aplicação)
- [Solução de Problemas](#-solução-de-problemas)

---

## 🎯 Visão Geral

Este guia te ajudará a configurar e executar o Sistema de Gerenciamento de Ordens de Serviço, uma aplicação Java que busca gerenciar as ordens de serviço.

---

## 🛠 Pré-requisitos

### ☕ Java Development Kit (JDK)
- *Versão recomendada:* JDK 21
- *Download:* [Oracle JDK](https://www.oracle.com) ou [Adoptium (Temurin)](https://adoptium.net)
- *Libs:* Junit Jupiter, BufferedImage   
- *Impressão:* Ter drivers de impressão

### 💻 Ambiente de Desenvolvimento

#### Opção 1: VS Code (Recomendado)
- *Editor:* [Visual Studio Code](https://code.visualstudio.com)
- *Extensão obrigatória:* Extension Pack for Java (Microsoft)

#### Opção 2: Outras IDEs
- IntelliJ IDEA
- Eclipse
- Ou linha de comando com javac e java
- Apertar no botão de play no Eclipse



---

## 📁 Configuração do Projeto

### 1. Obter o Código-fonte

#### Via Git:
bash
git clone https://github.com/Viniciusgithu/ordem_servico.git
cd ordem_servico


#### Via Download:
- Baixe o arquivo ZIP
- Extraia para uma pasta de sua preferência

### 2. Estrutura de Pastas
Organize seu projeto desta forma:

ordemdeservico/
├── TextEditor/
│   ├── bin/
│   └── src/
│       ├── ImageUtils.java
│       ├── LinhaPanel.java
│       ├── Main.java
│       ├── OrdemServico.java
│       ├── PrintUtils.java
│       └── TextEditor.java
│   └── test/
├── .gitignore
└── README.md


## 🚀 Executando a Aplicação

### No VS Code:

#### 1. Compilar
- O VS Code compila automaticamente ao salvar
- Para forçar recompilação: Ctrl+Shift+P → "Java: Clean Java Language Server Workspace"

#### 2. Executar
1. Abra o arquivo Main.java
2. Vá para a aba *"Run and Debug"* (ícone de play com bug)
3. Selecione *"Executar Aplicação Principal (Main)"* no dropdown
4. Clique no botão ▶ verde ou pressione F5

A aplicação será iniciada e a interface gráfica será exibida.

---

## 📱 Usando a Aplicação

### Interface Principal
A aplicação abrirá com uma interface gráfica TextEditor onde você pode:

### ✨ Funcionalidades Principais:
- *Criar OS:* Preencha os campos da Ordem de Serviço
- *Adicionar Itens:* Configure Refs, Pastas, Mts e Imagens
- *Imprimir:* Arquivo > *Imprimir*
- *Nova OS:* Arquivo > *+Página*

---


---

*🎉 Pronto! Sua aplicação de Ordem de Serviço está configurada e funcionando!*
