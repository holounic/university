package markup;
import java.util.List;

public class Strikeout extends MarkupBlock implements toParagraph {
    public Strikeout(List<toParagraph> content) {
        super(content);
        this.specifier = "~";
    }
}
