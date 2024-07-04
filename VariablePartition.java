import java.util.ArrayList;

class VariablePartition{
    private Particao head; //header da linked list
    private int circular_fit_index = 1;

    static class Particao{ //estrutura linked list de processos
        char status; //P (Partition) ou H (Hole)
        Processo p;
        int tamanho;
        Particao prev;
        Particao prox;

    }

    static class Processo{
        char Id;
        int tamanho;
        public Processo(char Id, int tamanho){
            this.Id = Id;
            this.tamanho = tamanho;
        }
    }
    

    static class Instrucao{
        String instrucao;
        char id_processo;
        int tamanho; //opcional
    }

    public void start(ArrayList<Instrucao> instrucoes, String estrategia, int tamanho_memoria){
        //inicializa a linked list
        head.prev = null;
        head.prox = null;
        head.status = 'H';
        head.tamanho = tamanho_memoria;

        //processa cada instrucao
        for (Instrucao instrucao : instrucoes) {
            switch (instrucao.instrucao) {
                case "IN":
                    Processo p = new Processo(instrucao.id_processo, instrucao.tamanho);
                    if (estrategia.equals("worstfit"))
                        aloca_processo_worst_fit(p);
                    else if (estrategia.equals("circularfit"))
                        aloca_processo_circular_fit(p);

                    print_particoes_livres();
                    break;
            
                case "OUT":
                    desaloca_processo(instrucao.id_processo);
                    print_particoes_livres();
                    break;
            }
        }
    }

    public boolean aloca_processo_worst_fit(Processo processo){
        Particao aux = head;
        int maior_tamanho = 0;
        int indice_maior_tamanho = 0;
        int i = 0;

        //itera pela linked list para achar maior particao, salvando indice
        do {
            i += 1;
            if (aux.tamanho >= maior_tamanho){
                maior_tamanho = aux.tamanho;
                indice_maior_tamanho = i; 
            }
            aux = aux.prox;
        } while (aux != null);

        if (maior_tamanho < processo.tamanho){ //caso nao tenha particao q o processo caiba, retorna falso
            System.out.println("ESPACO INSUFICIENTE DE MEMORIA");
            return false;
        }

        Particao aux2 = head;
        //acessa a particao pelo indice
        for (int j = 1; j < indice_maior_tamanho; j++) {
            aux2 = aux2.prox;
        }
        aux2.status = 'P';
        aux2.p = processo;
        // se sobrou espaco, aloca nova particao Hole do lado com o restante
        if (aux2.tamanho - processo.tamanho > 0){
            //verifica se particao do lado ja eh um hole, se sim basta aumentar seu tamanho
            if (aux2.prox.status == 'H'){
                aux2.prox.tamanho += aux2.tamanho - processo.tamanho;
                return true;
            }
            Particao aux3 = aux2.prox;
            Particao particao_nova = new Particao();
            particao_nova.status = 'H';
            particao_nova.tamanho = aux2.tamanho - processo.tamanho;
            aux2.tamanho = processo.tamanho;
            particao_nova.prev = aux2;
            particao_nova.prox = aux3;
            aux2.prox = particao_nova;
            aux3.prev = particao_nova;
        } 
        return true;
    }

    public boolean aloca_processo_circular_fit(Processo processo){
        Particao aux = head;
        int indice = 0;

        //busca ultimo lugar alocado
        for (int j = 1; j < circular_fit_index; j++) {
            aux = aux.prox;
        }

        do {
            indice +=1;
            if (aux.tamanho >= processo.tamanho){ //achou particao grande o suficiente
                aux.status = 'P';
                aux.p = processo;
                circular_fit_index = indice;
                // se sobrou espaco, aloca nova particao Hole do lado com o restante
                if (aux.tamanho - processo.tamanho > 0){
                    //verifica se particao do lado ja eh um hole, se sim basta aumentar seu tamanho
                    if (aux.prox.status == 'H'){
                        aux.prox.tamanho += aux.tamanho - processo.tamanho;
                        return true;
                    }
                    Particao aux2 = aux.prox;
                    Particao particao_nova = new Particao();
                    particao_nova.status = 'H';
                    particao_nova.tamanho = aux.tamanho - processo.tamanho;
                    aux.tamanho = processo.tamanho;
                    particao_nova.prev = aux;
                    particao_nova.prox = aux2;
                    aux.prox = particao_nova;
                    aux2.prev = particao_nova;
                }
                return true; 
            }
        } while (aux != null);

        //se ainda nao achou um fit, volta pro inicio da lista e itera ate onde tinha comecado antes
        aux = head;
        for (int i = 1; i < circular_fit_index; i++) {
            if (aux.status == 'H' && aux.tamanho >= processo.tamanho){
                aux.status = 'P';
                aux.p = processo;
                // se sobrou espaco, aloca nova particao Hole do lado com o restante
                if (aux.tamanho - processo.tamanho > 0){
                    //verifica se particao do lado ja eh um hole, se sim basta aumentar seu tamanho
                    if (aux.prox.status == 'H'){
                        aux.prox.tamanho += aux.tamanho - processo.tamanho;
                        return true;
                    }
                    Particao aux2 = aux.prox;
                    Particao particao_nova = new Particao();
                    particao_nova.status = 'H';
                    particao_nova.tamanho = aux.tamanho - processo.tamanho;
                    aux.tamanho = processo.tamanho;
                    particao_nova.prev = aux;
                    particao_nova.prox = aux2;
                    aux.prox = particao_nova;
                    aux2.prev = particao_nova;
                    return true;
                }
            }
        }
        //se nao achou fit retorna falso
        System.out.println("ESPACO INSUFICIENTE DE MEMORIA");
        return false;
    }

    public void desaloca_processo(char IdProcesso){
        //itera sobre a linked list para achar a particao que contem o processo
        Particao aux = head;
        do {
            if(aux.status == 'P'){
                if (aux.p.Id == IdProcesso){ //achou o processo
                    //verifica se alguma particao adjacente eh Hole, se for junta espaco livre com elas
                    if(aux.prev.status == 'H' && aux.prox.status == 'H'){
                        aux.prev.prox = aux.prox;
                        aux.prev.tamanho += aux.tamanho + aux.prox.tamanho;
                        return;
                    }
                    else if (aux.prev.status == 'H'){
                        aux.prev.prox = aux.prox;
                        aux.prev.tamanho += aux.tamanho;
                        return;
                    }
                    else if (aux.prox.status == 'H'){
                        aux.prox.prev = aux.prev;
                        aux.prox.tamanho += aux.tamanho;
                        return;
                    }
                    else{ //caso nenhuma particao adjacente eh hole
                        aux.status = 'H';
                        return;
                    }                    
                }
            }
            aux = aux.prox;
        } while (aux != null);
    }

    public void print_particoes_livres(){
        Particao aux = head;
        do {
            if (aux.status == 'H'){
                System.out.printf("| %d |",aux.tamanho);
            }
            aux = aux.prox;
        } while (aux != null);

    }
}