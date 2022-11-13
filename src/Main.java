import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws FileNotFoundException {
        String filePath = Main.class.getResource("cppfile/test.cpp").getPath();
        AutoMachine machine = new AutoMachine();
        FileReader reader = new FileReader(filePath);
        Scanner scanner = new Scanner(reader);
        String fragment;
        // 以空格为分界，初步读取源文件
        while (scanner.hasNextLine()) {
            // 获取代码片段的字符串
            fragment = scanner.nextLine();
            System.out.println("line words: " + fragment);
            machine.analyzeFragment(fragment);
        }
        machine.displayAnalyzeResults(machine.result);
    }
    
    
}
