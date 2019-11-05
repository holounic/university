package markup;

public class Text implements toParagraph {
    private String textField;
    public Text(String text) {
        this.textField = text;
    }
    @Override
    public void toMarkdown(StringBuilder builder) {
        builder.append(this.textField);
    }
    @Override
    public void toTex(StringBuilder builder) {
        builder.append(this.textField);
    }
}
