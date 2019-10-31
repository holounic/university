package markup;
import java.util.List;

public abstract class MarkupBlock implements TextBlock {
    protected List<toParagraph> textField;
    protected String specifier = "";

    public MarkupBlock(List<toParagraph> content) {
        this.textField = content;
    }
    @Override
    public StringBuilder toMarkdown(StringBuilder builder) {
        builder.append(this.specifier);
        for (int idx = 0; idx < this.textField.size(); idx++) {
            builder.append(this.textField.get(idx).toMarkdown());
        }
        builder.append(specifier);
        return builder;
    }
    @Override
    public String toMarkdown() {
        return toMarkdown(new StringBuilder()).toString();
    }
}
