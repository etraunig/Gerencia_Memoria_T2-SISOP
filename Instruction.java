public class Instruction {
    public char instruction;
    public char processID;
    public int size;

    public Instruction(char instruction, char processID, int size) {
        this.instruction = instruction;
        this.processID = processID;
        this.size = size;
    }
}
