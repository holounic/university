package markup;
import java.util.List;

public class Strong extends MarkupBlock implements toParagraph {
    public Strong(List<toParagraph> content) {
        super(content);
        this.mdSpecifier = "__";
        this.texSpecifier = "textbf";
    }
}
