public class Word {
    public String word;
    public int category;
    
    public Word(String word, int category) {
        this.word = word;
        this.category = category;
    }
    @Override
    public String toString() {
        return "(" + category + ",\"" + word + "\")";
    }
}
