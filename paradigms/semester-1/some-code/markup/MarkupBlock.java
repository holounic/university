package markup;
import java.util.List;

public abstract class MarkupBlock implements TextBlock {
    protected List<toParagraph> textField;
    protected MarkupBlock(List<toParagraph> content) {
        this.textField = content;
    }

    protected void toMarkupLang(StringBuilder builder, String beginSpecifier, String endSpecifier, Lang lang) {
        builder.append(beginSpecifier);
        for (toParagraph content : this.textField) {
            switch(lang) {
                case MARKDOWN:
                    content.toMarkdown(builder);
                    break;
                case TEX:
                    content.toTex(builder);
            }
        }
        builder.append(endSpecifier);
    }

    @Override
    public void toMarkdown(StringBuilder builder) {
        toMarkupLang(builder, "", "", Lang.MARKDOWN);
    }

    @Override
    public void toTex(StringBuilder builder) {
        toMarkupLang(builder, "", "", Lang.TEX);
    }
}
