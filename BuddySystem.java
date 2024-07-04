import java.util.ArrayList;
import java.util.Stack;

public class BuddySystem {
    /* tree class ******************************/
    private static class Node {
        public Node right, left, father;
        public int size;
        public boolean free;
        public char processID;
        public int programSize;
        public Node(Node father, int totalSize) {
            this.father = father;
            this.size = totalSize;
        }

        public int getFragmentation() {
            return size - programSize;
        }
    }
    /*******************************************/
    public Node totalMemory;

    public BuddySystem(int totalMemorySize) {
        this.totalMemory = new Node(null, totalMemorySize);
    }

    public int nextPowerOf2(int size) {
        int power = 1;
        while (power < size) {
            power *= 2;
        }
        return power;
    }

    public void runInstructions(ArrayList<Instruction> instructions) {
        for(Instruction inst : instructions) {
            //in
            if(inst.instruction == 'I') {
                InstructionIN(inst);
            }
            //out
            else if(inst.instruction == 'O'){
                //code
            }
            else {
                System.out.println("error at runInstructions");
            }
        }
    }

    public void InstructionIN(Instruction inst) {
        if(inst.size > totalMemory.size) {
            System.out.println("ESPAÇO INSUFICIENTE DE MEMÓRIA");
            return;
        }
        Stack<Node> stack = new Stack<Node>();
        int nextPower = nextPowerOf2(inst.size);
        stack.push(totalMemory);
        Node aux = totalMemory;
        while(aux.size >= nextPower){
            aux = stack.pop();
            if(!aux.free)
                continue;
            if(aux.size == nextPower) {
                if(aux.left == null && aux.right == null) {
                    aux.processID = inst.processID;
                    aux.programSize = inst.size;
                    aux.free = false;
                }
                /*else {
                    if(aux.father != null) {
                        aux = aux.father.right;
                        if(aux.left == null && aux.right == null) {
                            aux.processID = inst.processID;
                            aux.programSize = inst.size;
                            aux.free = false;
                        }
                        aux = aux.father;
                        if(aux.father != null) {
                            aux = aux.father.right;
                        }
                    }
                }*/
            }
            else if(aux.left == null && aux.right == null){
                aux.left = new Node(aux, aux.size/2);
                aux.right = new Node(aux, aux.size/2);
                stack.push(aux.right);
                stack.push(aux.left);
            }
            else {
                stack.push(aux.right);
                stack.push(aux.left);
            }
        }
    }
}
