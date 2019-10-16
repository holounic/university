package markup;

public class Text implements TextBlock {
    private String textField;
    public Text(String text) {
        this.textField = text;
    }
    @Override
    public String toMarkdown() {
        return this.textField;
    }
}
