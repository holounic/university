package markup;
import java.util.List;

public class Emphasis extends MarkupBlock implements toParagraph {
    public Emphasis(List<toParagraph> content) {
        super(content);
    }
    @Override
    public void toMarkdown(StringBuilder builder) {
        toMarkupLang(builder,"*", "*" , Lang.MARKDOWN);
    }
    @Override
    public void toTex(StringBuilder builder) {
        toMarkupLang(builder, "\\emph{", "}", Lang.TEX);
    }
}
