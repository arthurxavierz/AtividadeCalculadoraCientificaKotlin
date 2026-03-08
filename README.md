# 🧮 Calculadora Científica

Aplicativo Android desenvolvido em Kotlin como atividade avaliativa da disciplina de **Programação Para Dispositivos Móveis**. Interface inspirada na calculadora do iOS com suporte a funções científicas via menu lateral.

---

## 📱 Sobre o Projeto

Calculadora funcional com layout em `GridLayout`, lógica completa das quatro operações básicas e um menu lateral deslizante (`DrawerLayout`) com funções científicas avançadas.

---

## ✨ Funcionalidades

### Calculadora Básica
- Operações: adição, subtração, multiplicação e divisão
- Botão de porcentagem (`%`) e inversão de sinal (`+/-`)
- Suporte a números decimais
- Visor duplo: expressão completa + resultado em destaque
- Proteção contra dois operadores seguidos
- Tratamento de divisão por zero com mensagem de erro

### Funções Científicas (menu lateral)
- Seno, Cosseno e Tangente — ângulos em graus
- Logaritmo base 10 (`log`) e logaritmo natural (`ln`)
- Potenciação (`x ^ y`)
- Raiz quadrada (`√`)
- Constante Pi (`π`)

---

## 🎨 Interface

- Tema escuro inspirado na calculadora do iOS
- Botões redondos: cinza escuro para números, laranja para operadores
- Menu lateral com fundo escuro e botões em laranja
- Visor com expressão em cinza e resultado em branco bold

---

## 🛠️ Tecnologias Utilizadas

- Kotlin
- XML (GridLayout + DrawerLayout)
- Material Components
- `kotlin.math` para as funções científicas

---

## 🚀 Como Rodar

1. Clone o repositório
2. Abra no Android Studio
3. Sincronize o Gradle
4. Rode em um dispositivo físico ou emulador com API 24+

---

## 📁 Estrutura do Projeto

```
app/
├── kotlin+java/
│   └── MainActivity.kt         # Lógica completa da calculadora
└── res/
    ├── layout/
    │   └── activity_main.xml   # Layout com GridLayout e DrawerLayout
    └── values/
        └── themes.xml          # Estilos dos botões
```

---

## 👨‍💻 Autor

Desenvolvido por **Arthur Xavier**
