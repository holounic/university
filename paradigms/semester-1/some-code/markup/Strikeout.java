package markup;
import java.util.List;

public class Strikeout extends MarkableBlock implements TextBlock {
    public Strikeout(List<TextBlock> content) {
        super(content);
        this.specifier = "~";
    }
}
