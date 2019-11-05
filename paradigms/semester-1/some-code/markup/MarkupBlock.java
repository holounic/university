package markup;
import java.util.List;

public abstract class MarkupBlock implements TextBlock {
    protected List<toParagraph> textField;
    protected String mdSpecifier = "";
    protected String texSpecifier = "";
    public MarkupBlock(List<toParagraph> content) {
        this.textField = content;
    }
    @Override
    public void toMarkdown(StringBuilder builder) {
        builder.append(this.mdSpecifier);
        for (int idx = 0; idx < this.textField.size(); idx++) {
            this.textField.get(idx).toMarkdown(builder);
        }
        builder.append(this.mdSpecifier);
    }
    @Override
    public void toTex(StringBuilder builder) {
        builder.append('\\');
        builder.append(this.texSpecifier);
        builder.append("{");
        for (toParagraph content : textField) {
            content.toTex(builder);
        }
        builder.append("}");
    }
}
