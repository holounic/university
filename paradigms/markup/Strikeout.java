package markup;
import java.util.List;

public class Strikeout extends MarkupBlock implements toParagraph {
    public Strikeout(List<toParagraph> content) {
        super(content);
    }

    @Override
    public void toMarkdown(StringBuilder builder) {
        toMarkupLang(builder, "~", "~", Lang.MARKDOWN);
    }
    @Override
    public void toTex(StringBuilder builder) {
        toMarkupLang(builder, "\\textst{", "}", Lang.TEX);
    }
}
