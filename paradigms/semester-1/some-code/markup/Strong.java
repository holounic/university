package markup;
import java.util.List;

public class Strong extends MarkupBlock implements toParagraph {
    public Strong(List<toParagraph> content) {
        super(content);
    }
    @Override
    public void toMarkdown(StringBuilder builder) {
        toMarkupLang(builder, "__", "__", Lang.MARKDOWN);
    }
    @Override
    public void toTex(StringBuilder builder) {
        toMarkupLang(builder, "\\textbf{", "}", Lang.TEX);
    }
}
