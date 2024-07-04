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
            this.free = true;
        }

        public int getFragmentation() {
            return size - programSize;
        }
    }
    /*******************************************/
    public Node totalMemoryNode;

    public BuddySystem(int totalMemorySize) {
        this.totalMemoryNode = new Node(null, totalMemorySize);
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
            if(inst.instruction == 'I')
                InstructionIN(inst);
            else if(inst.instruction == 'O')
                InstructionOUT(inst);
            else 
                System.out.println("error at runInstructions: Instruction -> " + inst.instruction + inst.processID + inst.size);

            printFreeBlocks();
        }
    }

    public void InstructionIN(Instruction inst) {
        if(inst.size > totalMemoryNode.size) {
            System.out.println("ESPAÇO INSUFICIENTE DE MEMÓRIA");
            return;
        }
        Stack<Node> stack = new Stack<Node>();
        int nextPower = nextPowerOf2(inst.size);
        stack.push(totalMemoryNode);
        Node aux = totalMemoryNode;
        while(aux.size >= nextPower){
            aux = stack.pop();
            if(!aux.free)
                continue;
            if(aux.size == nextPower) {
                if(aux.left == null && aux.right == null) {
                    aux.processID = inst.processID;
                    aux.programSize = inst.size;
                    aux.free = false;
                    break;
                }
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
        System.out.print("Instruçao IN(" + inst.processID + ", " + inst.size + ") -> ");
        System.out.println("Fragmentaçao Interna: " + aux.getFragmentation());
    }

    public void InstructionOUT(Instruction inst) {
        Stack<Node> stack = new Stack<Node>();
        stack.push(totalMemoryNode);
        Node aux = totalMemoryNode;
        while(!stack.empty()) {
            aux = stack.pop();
            if(!aux.free) {
                if(aux.processID == inst.processID) {
                    aux.free = true;
                    break;
                }
            }
            else {
                if(aux.left != null && aux.right != null) {
                    stack.push(aux.right);
                    stack.push(aux.left);
                }
            }
        }
        RegroupBlocks();
        System.out.println("Instruçao OUT(" + inst.processID + ") finalizada");
    }

    public void RegroupBlocks() {
        Stack<Node> stack = new Stack<Node>();
        stack.push(totalMemoryNode);
        Node aux = totalMemoryNode;
        while(!stack.empty()) {
            aux = stack.pop();
            if(aux.left == null && aux.right == null && aux.free) {
                if(aux.father != null) {
                    Node aux2 = aux.father;
                    Node aux3 = new Node(null, 0);
                    if(aux2.left.equals(aux)) {
                        aux3 = aux2.right;
                    }
                    else if(aux2.right.equals(aux)) {
                        aux3 = aux2.left;
                    }
                    if(aux3.left == null && aux3.right == null && aux3.free) {
                        aux2.right = null;
                        aux2.left = null;
                        stack = new Stack<>();
                        stack.push(totalMemoryNode);
                    }
                }
                else
                    return;
            }
            else if(aux.left != null && aux.right != null) {
                stack.push(aux.right);
                stack.push(aux.left);
            }
        }
    }

    public void printFreeBlocks() {
        System.out.print("Blocos Livres: |");
        Stack<Node> stack = new Stack<>();
        int sum = 0;
        Node aux = totalMemoryNode;
        stack.push(totalMemoryNode);
        int count = 0;
        while(!stack.empty()) {
            aux = stack.pop();
            if(!aux.free) {
                sum+=aux.size;
                continue;
            }
            if(aux.left == null && aux.right == null) {
                System.out.print(aux.size + "|");
            }
            else {
                stack.push(aux.right);
                stack.push(aux.left);
            }
            sum+=aux.size;
        }
        System.out.println("\n");
    }
}
