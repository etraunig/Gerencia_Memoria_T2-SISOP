import java.util.ArrayList;
import java.util.Stack;

public class BuddySystem {
    /* tree class ******************************/
    private static class Node {
        public Node right, left, father;
        public int size;
        public boolean free; //free == false if a process was given to it, else it's true
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
    public Node totalMemoryNode; //root of the tree, the "whole" of the memory. the only node with a null father

    public BuddySystem(int totalMemorySize) {
        this.totalMemoryNode = new Node(null, totalMemorySize); //fatherless
    }

    public int nextPowerOf2(int size) { //there isn't much to explain, it finds the next power of 2 of a given number
        int power = 1;
        while (power < size) {
            power *= 2;
        }
        return power;
    }
    
    //receives the instruction array from main, then chooses what to do with each of them
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

    public void InstructionIN(Instruction inst) { //the name's self explanatory
        if(inst.size > totalMemoryNode.size) {
            System.out.println("ESPAÇO INSUFICIENTE DE MEMÓRIA");
            return;
        }
        Stack<Node> stack = new Stack<Node>(); //stack of nodes it "goes" throgh
        int sizeNeeded = nextPowerOf2(inst.size); //block size it needs
        stack.push(totalMemoryNode);  //first item in the stack needs to be the root
        Node aux = totalMemoryNode; //needs to be started, otherwise java won't shut up
        while(!stack.empty()){
            aux = stack.pop();
            if(!aux.free) //if the block isn't free, it can't be chosen
                continue;
            if(aux.size == sizeNeeded) {
                if(aux.left == null && aux.right == null) { //if it's the size needed and don't have children, it's the one we wanted
                    aux.processID = inst.processID;
                    aux.programSize = inst.size;
                    aux.free = false;
                    break;
                }
            }
            else if(aux.left == null && aux.right == null){ //otherwise it is too big, we need to divide it
                aux.left = new Node(aux, aux.size/2);
                aux.right = new Node(aux, aux.size/2);
                stack.push(aux.right);
                stack.push(aux.left);
            }
            else { //if it's already divided, we go into it and iterate again
                stack.push(aux.right);
                stack.push(aux.left);
            }
        }
        System.out.print("Instruçao IN(" + inst.processID + ", " + inst.size + ") -> ");
        if(stack.empty()) //went through the whole tree, was not able to find a spot
            System.out.println("ESPAÇO INSUFICIENTE DE MEMÓRIA");
        else // was able to find a spot
            System.out.println("Fragmentaçao Interna: " + aux.getFragmentation());
    }

    public void InstructionOUT(Instruction inst) { //good name, I know
        Stack<Node> stack = new Stack<Node>(); //stack to get the nodes we need to go through
        stack.push(totalMemoryNode); //base start of stack
        Node aux = totalMemoryNode; //I love java, my favorite language
        while(!stack.empty()) {
            aux = stack.pop(); //get item from stack
            if(!aux.free) { //if the block has a process, might be what we want
                if(aux.processID == inst.processID) { //is it the process we want?
                    aux.free = true; //if it is, now it's free from it
                    break;
                }
            }
            else { //if it doesn't have a process with it
                if(aux.left != null && aux.right != null) { //if it's not a leaf, we go to it's children
                    stack.push(aux.right);
                    stack.push(aux.left);
                }
            }
        }
        //after removing/freeing the process we wanted
        //we see if we can regroup some memory blocks to make it to full memory again
        RegroupBlocks();
        System.out.println("Instruçao OUT(" + inst.processID + ") finalizada");
    }

    //here it gets confusing, but it works perfectly (at least until I'm writing these comments)
    public void RegroupBlocks() {
        Stack<Node> stack = new Stack<Node>(); //it's the third time I've written this
        stack.push(totalMemoryNode); // default
        Node aux = totalMemoryNode; // man I wish I could marry java
        while(!stack.empty()) {
            aux = stack.pop();
            //if aux don't have children and it's free, we check it's brother
            if(aux.left == null && aux.right == null && aux.free) {
                if(aux.father != null) { //it has a father
                    Node aux2 = aux.father;
                    Node aux3 = new Node(null, 0);
                    //if/else to discover aux's brother
                    if(aux2.left.equals(aux)) { 
                        aux3 = aux2.right;
                    }
                    else if(aux2.right.equals(aux)) {
                        aux3 = aux2.left;
                    } //if the brother doesn't have children and it's free, we remove both from the parent
                    if(aux3.left == null && aux3.right == null && aux3.free) {
                        aux2.right = null;
                        aux2.left = null;
                        //and then we restart the stack to see again
                        stack = new Stack<>();
                        stack.push(totalMemoryNode);
                    }
                }
                else //if it don't have a father, it's the root and the search ended
                    return;
            }
            else if(aux.left != null && aux.right != null) {
                stack.push(aux.right);
                stack.push(aux.left);
            }
        }
    }

    public void printFreeBlocks() { //simple enough to write something on it
        System.out.print("Blocos Livres: |");
        Stack<Node> stack = new Stack<>();
        Node aux = totalMemoryNode;
        stack.push(totalMemoryNode);
        while(!stack.empty()) {
            aux = stack.pop();
            if(!aux.free) {
                continue;
            }
            if(aux.left == null && aux.right == null) {
                System.out.print(aux.size + "|");
            }
            else {
                stack.push(aux.right);
                stack.push(aux.left);
            }
        }
        System.out.println("\n");
    }
}
