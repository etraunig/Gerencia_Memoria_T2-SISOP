import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    
    public static void main(String[] args) {
        int totalMemory = 16;
        //int partitionSize = 4;
        ArrayList<Instruction> instructions = readInstructions("instructions.txt");

        int[] option = menu();
        if(option[0] == 1) {
            //continue
        }
        else if(option[0] == 2) {
            if(option[1] == 1) {
                //worst-fit
            }
            else if(option[1] == 2) {
                //circular-fit
            }
        }
    }

    public static int[] menu() {
        Scanner scanner = new Scanner(System.in);
        int[] option = {0,0};
        String title =  " ___  ___   _______  ___  ___    ______    ______  ____    ____  \n"+
                        "|   \\/   | |   ____||   \\/   |  /  __  \\  |   _  \\ \\   \\  /   / \n"+
                        "|  \\  /  | |  |__   |  \\  /  | |  |  |  | |  |_)  | \\   \\/   /  \n"+
                        "|  |\\/|  | |   __|  |  |\\/|  | |  |  |  | |      /   \\_    _/    \n"+
                        "|  |  |  | |  |____ |  |  |  | |  |__|  | |  |\\  \\     |  |      \n"+
                        "|__|  |__| |_______||__|  |__|  \\______/  | _| \\__|    |__|      \n\n\n";

        String[] options = {"Opçao de memoria desejada? (1 - Memoria Fixa | 2 - Memoria Variavel): ",
                            "Politica desejada? (1 - Worst-Fit | 2 - Circular-Fit): "};

        System.out.println(title);

        if(option[0] == 0) {
            System.out.print(options[0]);
            option[0] = scanner.nextInt();
        }
        if(option[0] == 2) {
            System.out.print(options[1]);
            option[1] = scanner.nextInt();
        }
        scanner.close();
        return option;
    }

    public static ArrayList<Instruction> readInstructions(String fileName) {
        ArrayList<Instruction> instructions = new ArrayList<>();
        char inst, pid;
        int size;
        try (BufferedReader buffReader = new BufferedReader(new FileReader(fileName))){
            String line;
            while((line = buffReader.readLine()) != null) {
                inst = line.charAt(0);
                size = 0;
                if(inst == 'I') {
                    pid = line.charAt(3);
                    size = Character.getNumericValue(line.charAt(6));
                }
                else { //inst == 'O'
                    pid = line.charAt(4);
                }
                instructions.add(new Instruction(inst, pid, size));
            }
        } 
        catch (Exception e) {
            e.printStackTrace();
        }
        return instructions;
    }
}