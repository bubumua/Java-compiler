import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws FileNotFoundException {
        String filePath = "D:\\IntelliJ-IDEA-Saves\\LexicalAnalysis\\src\\cppfile\\test.cpp";
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
            // for (int i = 0; i < fragment.length(); i++) {
            //     // 对于每一个代码片段，遍历得到每一个字符
            //     char alpha = fragment.charAt(i);
            //
            // }
        }
        machine.displayAnalyzeResults(machine.result);
    }
    
    
}
