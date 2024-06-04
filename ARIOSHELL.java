import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ARIOSHELL {
    public static void writeToFile(String filePath, String message){
        try{
            System.out.println("saving into: " + filePath);
            File file = new File(filePath);
            FileWriter writer = new FileWriter(file);
            writer.write(message);
            writer.close();
            System.out.println("[OK] File written successfully.\n");
        }
        catch(IOException e){
            System.out.println("Wrong type of input pesare gol.");
        }
    }
    public static void readFile(String filePath){
        try{
            File file = new File(filePath);
            Scanner sc = new Scanner(file);
            while(sc.hasNextLine()){
                System.out.println(sc.nextLine());
            }
            sc.close();
            System.out.println();
        }
        catch(FileNotFoundException e){
            System.out.println("File was not found.\n");
        }
    }
    public static String fileContentCopy(String filePath){
        String content = "";
        try{
            File file = new File(filePath);
            Scanner sc = new Scanner(file);
            while(sc.hasNextLine()){
                content += sc.nextLine() + "\n";
            }
            System.out.println("[OK] copied.\n");
            sc.close();
        }
        catch(FileNotFoundException e){
            System.out.println("File was not found.\n");
        }
        return content;
    }
    public static void clearScreen() {  
        System.out.print("\033[H\033[2J");  
        System.out.flush();  
    } 
    public static String pwd(){
        Path currentRelativePath = Paths.get("");
        return currentRelativePath.toAbsolutePath().toString(); 
    }
    public static boolean commandCheck(String command, String text){
        return command.toLowerCase().strip().startsWith(text);
    }
    public static boolean pathCheck(String currentPath, String filePath){
        // true means it should use relative
        Pattern path = Pattern.compile(currentPath);
        Matcher pathMatch= path.matcher(filePath);
        if(pathMatch.find()){
            System.out.println("PATHS MATCH");
            return true;
        }
        return false;
    }
    public static void printHelp(){
        System.out.println("write to file:          wtf [filePath]");
        System.out.println("read file:              rf [filePath]");
        System.out.println("copy file content:      copy [filePath]");
        System.out.println("paste copied content:   paste [filePath]");
        System.out.println("clear terminal:         clear");
        System.out.println("Current directory:      pwd");
        System.out.println("change directory:       cd [filePath]");
        System.out.println("---initial directory:   cd");
        System.out.println("list directory:         ls");
        System.out.println("get info:               get [sub]");
        System.out.println("---get os               get os");
        System.out.println();
    }
    public static void main(String[] args){
        boolean end = false;
        String msg = "";
        String finalMessage = "";
        String command = "";
        String copiedMessage = "";
        String currentPath = pwd();
        Scanner scanner = new Scanner(System.in);
        boolean exit = false;
        String filePath = "";
        Pattern pattern;
        Matcher matcher;
        String dirTree = "";
        boolean osIsLinux = System.getProperty("os.name").equals("Linux");
        String between = osIsLinux ? "/" : "\\";
        Pattern unrecognized;
        System.out.println(".:ARIO SHELL:.");
        while(!exit){
            if(dirTree.equals("")){
                System.out.print("$_ ");
            }
            else{
                System.out.print(dirTree + " $_ ");
            }
            command = scanner.nextLine();
            unrecognized = Pattern.compile("[A-z0-9\\/ .]");
            Matcher unrec = unrecognized.matcher(command);
            // for help
            if(commandCheck(command, "/help") || commandCheck(command, "/?") || commandCheck(command, "?")){
                printHelp();
            }
            // for writing 
            else if(commandCheck(command, "wtf")){
                finalMessage = "";
                pattern = Pattern.compile("wtf\\s+([A-z0-9\\/ .]+)");
                matcher = pattern.matcher(command);
                if(matcher.find()){
                    filePath = matcher.group(1);
                    System.out.println("LOLLLL: " + filePath);
                    System.out.println("To end writing simply type \"end\" and press enter");
                    end = false;
                    while(!end){
                        System.out.print(": ");
                        msg = scanner.nextLine();
                        if(msg.toLowerCase().equals("end")){
                            end = true;
                            break;
                        }
                        finalMessage += msg + "\n";
                    }
                }
                else{
                    System.out.print("file path: ");
                    filePath = scanner.nextLine();
                    System.out.println("To end writing simply type \"end\" and press enter.");
                    end = false;
                    while(!end){
                        System.out.print(": ");
                        msg = scanner.nextLine();
                        if(msg.toLowerCase().equals("end")){
                            end = true;
                            break;
                        }
                        finalMessage += msg + "\n";
                    }
                }
                if(pathCheck(currentPath, filePath)) writeToFile(filePath, finalMessage);
                else{
                    if(currentPath.endsWith(between)) writeToFile(currentPath + filePath, finalMessage);
                    else writeToFile(currentPath + between + filePath, finalMessage);
                } 
            }
            // for reading
            else if(commandCheck(command, "rf")){
                pattern = Pattern.compile("rf\\s+([A-z0-9\\/ .]+)");
                matcher = pattern.matcher(command);
                if(matcher.find()){
                    filePath = matcher.group(1);
                }
                else{
                    System.out.print("file path: ");
                    filePath = scanner.nextLine();
                }
                if(pathCheck(currentPath, filePath)) readFile(filePath);
                else{
                    if(currentPath.endsWith(between)) readFile(currentPath + filePath);
                    else readFile(currentPath + between + filePath);
                }
            }
            // for copying
            else if(commandCheck(command, "copy")){
                pattern = Pattern.compile("copy\\s+([A-z0-9\\/ .]+)");
                matcher = pattern.matcher(command);
                if(matcher.find()){
                    filePath = matcher.group(1);
                }
                else{
                    System.out.print("file path: ");
                    filePath = scanner.nextLine();
                }
                if(pathCheck(currentPath, filePath)) copiedMessage = fileContentCopy(filePath);
                else{
                    if(currentPath.endsWith(between)) copiedMessage = fileContentCopy(currentPath + filePath);
                    else copiedMessage = fileContentCopy(currentPath + between + filePath);
                }
            }
            // for pasting
            else if(commandCheck(command, "paste")){
                pattern = Pattern.compile("paste\\s+([A-z0-9\\/ .]+)");
                matcher = pattern.matcher(command);
                if(matcher.find()){
                    filePath = matcher.group(1);
                }
                else{
                    System.out.print("file path: ");
                    filePath = scanner.nextLine();
                }
                if(pathCheck(currentPath, filePath)) writeToFile(filePath, copiedMessage);
                else{
                    if(currentPath.endsWith(between)) writeToFile(currentPath + filePath, copiedMessage);
                    else writeToFile(currentPath + between + filePath, copiedMessage);
                }
            }
            // to clear
            else if(commandCheck(command, "clear")){
                clearScreen();
            }
            // pwd (current directory)
            else if(commandCheck(command, "pwd")){
                System.out.println(currentPath + "\n");
            }
            // cd (change directory)
            else if(commandCheck(command, "cd")){
                if(osIsLinux){
                    pattern = Pattern.compile("cd\\s+([A-z0-9\\/ ]+)");
                }
                else{
                    pattern = Pattern.compile("cd\\s+([A-z0-9\\ ]+)");
                }
                matcher = pattern.matcher(command);
                if(matcher.find()){
                    String addition = matcher.group(1);
                    File f;
                    // dir1/dir2/dir3/dir4/
                    if(currentPath.endsWith(between)){
                        if(addition.endsWith(between)){
                            f = new File(currentPath + addition);
                        }
                        else{
                            f = new File(currentPath + addition + between);
                        }
                    }
                    else{
                        if(addition.endsWith(between)){
                            f = new File(currentPath + between + addition);
                        }
                        else{
                            f = new File(currentPath + between + addition + between);
                        }
                    }
                    if(f.exists()){ // Cheking if directory exists
                        if(currentPath.endsWith(between)){
                            currentPath += addition;
                        }
                        else{
                            currentPath += between + addition;
                        }

                        // for Terminal
                        if(addition.endsWith(between)){
                            dirTree += addition;
                        }
                        else{
                            dirTree += addition + between;
                        }
                    }
                }
                else{
                    currentPath = pwd();
                    dirTree = "";
                }
            }
            // ls (list directories)
            else if(commandCheck(command, "ls")){
                File file = new File(currentPath);
                File[] files = file.listFiles();
                for(File f:files){
                    if(f.isDirectory()){
                        System.out.println(f.getName() + between);
                    }
                    else{
                        System.out.println(f.getName());
                    }
                }
            }
            // get
            else if(commandCheck(command, "get")){
                if(commandCheck(command, "get os")){
                    System.out.println(System.getProperty("os.name"));
                }
            }
            // any unrecognized command
            else if(unrec.find()){
                printHelp();
            }
        }
        System.out.println("[OK] Exitted.");
        scanner.close();
    }
}
