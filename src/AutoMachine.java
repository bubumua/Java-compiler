import java.util.ArrayList;
import java.util.HashMap;

public class AutoMachine {
    /**
     * 符号表
     */
    public HashMap<String, Integer> symbols;
    /**
     * 结果列表
     */
    public ArrayList<Word> result;
    /**
     * 临时单词
     */
    private String tempWord;
    /**
     * 临时单词类型
     */
    private int tempWordType;
    
    /**
     * 构造函数，初始化符号表
     *
     * @Return none
     * @author Bubu
     */
    public AutoMachine() {
        initSymbols();
        tempWord = "";
        tempWordType = 0;
        result = new ArrayList<>();
        
    }
    
    public ArrayList<Word> analyzeFragment(String fragment) {
        // 分析输入的整行代码字符串
        tempWord = "";
        for (int i = 0; i < fragment.length(); i++) {
            char c = fragment.charAt(i);
            tempWordType = judgeCharacter(c, tempWordType);
            
        }
        
        return result;
    }
    
    
    private int judgeCharacter(char c, int wordType) {
        // when input is a letter or a digit or '_' character
        if (Character.isLetter(c) || Character.isDigit(c) || c == '_') {
            // if input is a digit
            if (Character.isDigit(c)) {
                // 若已判断出的单词为空，非0添加，并将类型设为数值；0则空过
                if (tempWord.length() == 0) {
                    if ((int) c == 0) {
                        return 0;
                    } else {
                        tempWord += c;
                        return 3;
                    }
                }
                // 若已判断出单词的非空，则直接在后面添加，且不改变类型。
                else {
                    tempWord += c;
                    return wordType;
                }
            }
            // 若输入是字母或下划线，根据不同类型，做不同选择
            else {
                if (tempWord.length() == 0 && wordType == 0) {
                    tempWord += c;
                    return 2;
                }
                switch (wordType) {
                    // 若正在判断函数名/变量名，或字符串，直接添加，且不改变类型
                    case 2:
                    case 6:
                        tempWord += c;
                        return wordType;
                    // 若正在判断数值，则除特殊情况加入到结果列表外，空过
                    case 3:
                        if (c == 'L' || c == 'l' || c == 'F' || c == 'f') {
                            tempWord += c;
                            result.add(new Word(tempWord, wordType));
                            tempWord = "";
                        }
                        return 0;
                    default:
                        return 0;
                }
            }
        }
        // other condition
        else {
            String cstr = String.valueOf(c);
            // 在符号表中查询这个非字母/下划线/数字字符
            if (symbols.containsKey(cstr)) {
                // 先看已判断出的单词是不是符号表里的关键词/保留字。若是，则加入结果列表；
                // 若不是，则作为变量名/函数名加入结果列表
                if (tempWord.length() > 0) {
                    result.add(new Word(tempWord, symbols.getOrDefault(tempWord, wordType)));
                }
                // 符号本身也要加入结果列表
                result.add(new Word(String.valueOf(c), symbols.get(cstr)));
                tempWord = "";
                // 根据不同的输入字符，返回不同的值
                switch (c) {
                    case '\'':
                    case '"':
                        if (wordType == 6) {
                            return 0;
                        } else {
                            return 6;
                        }
                    default:
                        return 0;
                }
            }
            // 如果输入的是空格
            else if (" ".equals(cstr)) {
                if (wordType == 6) {
                    tempWord += c;
                    return wordType;
                } else {
                    if (tempWord.length() > 0) {
                        result.add(new Word(tempWord, symbols.getOrDefault(tempWord, wordType)));
                        tempWord = "";
                    }
                    return 0;
                }
            }
            // （保险/debug措施）若查询不到则报错并空过
            else {
                System.out.println("not found in symbol table: " + cstr);
                return 0;
            }
        }
    }
    
    /**
     * 展示分析结果
     *
     * @param result 结果列表
     * @Return void
     * @author Bubu
     */
    public void displayAnalyzeResults(ArrayList<Word> result) {
        for (Word word : result) {
            System.out.println(word.toString());
        }
    }
    
    
    /**
     * 初始化符号表，将各种保留字、关键字、运算符等加入其中
     * 关键字--1
     * 变量名、函数名--2
     * 数值（立即数）--3
     * 运算符--4
     * 语言特性符号（,;(){}等）--5
     * 字符串/字符--6
     *
     * @Return void
     * @author Bubu
     */
    private void initSymbols() {
        symbols = new HashMap<String, Integer>();
        symbols.put("void", 1);
        symbols.put("int", 1);
        symbols.put("long", 1);
        symbols.put("float", 1);
        symbols.put("double", 1);
        symbols.put("char", 1);
        symbols.put("boolean", 1);
        symbols.put("byte", 1);
        symbols.put("short", 1);
        
        symbols.put("main", 2);
        symbols.put("this", 2);
        
        symbols.put("+", 4);
        symbols.put("-", 4);
        symbols.put("*", 4);
        symbols.put("/", 4);
        symbols.put("%", 4);
        symbols.put("=", 4);
        symbols.put(">", 4);
        symbols.put("<", 4);
        
        symbols.put(",", 5);
        symbols.put(";", 5);
        symbols.put(":", 5);
        symbols.put("?", 5);
        symbols.put("\"", 5);
        symbols.put("'", 5);
        symbols.put("(", 5);
        symbols.put(")", 5);
        symbols.put("{", 5);
        symbols.put("}", 5);
        symbols.put("[", 5);
        symbols.put("]", 5);
        
        
    }
    
    
}
