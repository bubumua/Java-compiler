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
     * 单词栈
     */
    private String tempWord;
    /**
     * 单词类型码
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
        // 初始化已经判断出的单词为空
        tempWord = "";
        // 初始化单词类型
        tempWordType = 0;
        result = new ArrayList<>();
        
    }
    
    /**
     * 对输入的一行字符串进行分析
     *
     * @param fragment 输入的字符串
     * @Return java.util.ArrayList<Word> 返回自动机目前分析出的所有结果
     * @author Bubu
     */
    public ArrayList<Word> analyzeFragment(String fragment) {
        // 分析输入的整行代码字符串
        tempWord = "";
        for (int i = 0; i < fragment.length(); i++) {
            char c = fragment.charAt(i);
            tempWordType = judgeCharacter(c, tempWordType);
            
        }
        return result;
    }
    
    
    /**
     * 对输入的下一字符进行识别、分析
     *
     * @param c        输入的下一字符
     * @param wordType 当前正在判断的单词类型码
     * @Return int 分析后的单词类型码
     * @author Bubu
     */
    private int judgeCharacter(char c, int wordType) {
        // 当输入是一个字母、下划线或数字时
        if (Character.isLetter(c) || Character.isDigit(c) || c == '_') {
            // 如果输入是一个数字
            if (Character.isDigit(c)) {
                // 若字符栈为空，非0添加，并将类型设为数值；0则空过
                if (tempWord.length() == 0) {
                    
                    tempWord += c;
                    return 3;
                    
                }
                // 若字符栈非空，则直接在后面添加，且不改变类型。
                else {
                    tempWord += c;
                    return wordType;
                }
            }
            // 若输入是字母或下划线，根据不同类型，做不同选择
            else {
                // 如果字符栈为空，且类型码为0，则将字符加入符号栈，返回类型码为2（函数/变量名）
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
        // 若输入的不是数字、字母、下划线
        else {
            String cstr = String.valueOf(c);
            // 在符号表中查询这个非字母/下划线/数字字符
            if (symbols.containsKey(cstr)) {
                // 先看字符栈内的单词是不是符号表里的关键词/保留字。若是，则加入结果列表；
                // 若不是，则作为变量名/函数名加入结果列表
                if (tempWord.length() > 0) {
                    result.add(new Word(tempWord, symbols.getOrDefault(tempWord, wordType)));
                }
                // 符号本身也要加入结果列表
                result.add(new Word(cstr, symbols.get(cstr)));
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
            // 以下都是不在预置符号表中的字符
            // 如果输入的是小数点
            else if (".".equals(cstr)) {
                // 如果正在判断数值，则将字符加入字符栈
                if (wordType == 3) {
                    tempWord += c;
                    return wordType;
                }
                // 如果不在判断数值/立即数，则将字符栈单词和小数点分别加入结果，清空字符栈
                else {
                    if (tempWord.length() > 0) {
                        result.add(new Word(tempWord, symbols.getOrDefault(tempWord, wordType)));
                    }
                    // 符号本身也要加入结果列表
                    result.add(new Word(cstr, symbols.get(cstr)));
                    tempWord = "";
                    return 1;
                }
            }
            // 如果输入的是反斜杠\
            else if ("\\".equals(cstr)) {
                // 如果正在判断的是字符串，就将反斜杠加入字符栈
                if (wordType == 6) {
                    tempWord += cstr;
                    return wordType;
                }
                // 如果正在判断的不是字符串，则空过。置类型码为0
                else {
                    return 0;
                }
            }
            // 如果输入的是空格
            else if (" ".equals(cstr)) {
                // 若当前正在判断字符串，则直接添加
                if (wordType == 6) {
                    tempWord += c;
                    return wordType;
                }
                // 若不是字符串，且已经判断出的单词不为空，则将已经判断出的单词加入结果列表
                else {
                    if (tempWord.length() > 0) {
                        result.add(new Word(tempWord, symbols.getOrDefault(tempWord, wordType)));
                        tempWord = "";
                    }
                    return 0;
                }
            }
            // （debug）若查询不到则报错并空过
            else {
                System.out.println("not found in symbol table: " + cstr);
                return 0;
            }
        }
    }
    
    /**
     * 展示分析结果，默认展示自动机的结果列表
     *
     * @Return void
     * @author Bubu
     */
    public void displayAnalyzeResults() {
        displayAnalyzeResults(result);
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
        symbols.put("return", 1);
        
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
