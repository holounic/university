package markup;
import java.util.List;

public class Emphasis extends MarkupBlock implements toParagraph {
    public Emphasis(List<toParagraph> content) {
        super(content);
        this.specifier = "*";
    }
}
