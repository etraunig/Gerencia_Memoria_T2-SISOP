# Trabalho II - Sistemas Operacionais
## Gerenciamento de Memória

O presente trabalho tem por objetivo explorar as abordagens de alocação de espaço contíguo para procesos, e explorar as diferentes técnicas e políticas de alocação de espaços em uma memória.

## Estratégias de alocação

A ferramenta deverá permitir a exploração de (i) partições variáveis ou (ii) partições definidas com o sistema buddy. Comum a todos os métodos deverá ser a informação do tamanho da memória principal a ser empregada, que define o tamanho total inicial disponível para alocação. Deverá ser assumido um tamanho
sempre equivalente a uma potência de dois.

Para o sistema de partições variáveis, deverá ser possível informar a política de alocação a ser empregada, se Worst-Fit ou Circular-Fit. A escolha do tipo de política a ser aplicada deverá ser realizada em tempo de execução pelo usuário. Diferente do mecanismo de particionamento variável, para o sistema buddy,
nada além da requisição de alocação de memória e liberação de memória dos processos deverá ser informado.

No escopo deste trabalho, o tratamento de alocações de processos que venham a ultrapassar a quantidade de espaço disponível na memória principal, deverá se dar a partir da notificação de “ESPAÇO
INSUFICIENTE DE MEMÓRIA”.