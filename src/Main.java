import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws FileNotFoundException {
        // 获取文件路径
        String filePath = Main.class.getResource("cppfile/test.cpp").getPath();
        Scanner scanner = new Scanner(new FileReader(filePath));
        // 创建自动机
        AutoMachine machine = new AutoMachine();
        // 按行读取cpp文件
        while (scanner.hasNextLine()) {
            machine.analyzeFragment(scanner.nextLine());
        }
        machine.displayAnalyzeResults();
    }
    
}
