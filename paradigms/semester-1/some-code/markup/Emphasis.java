package markup;
import java.util.List;

public class Emphasis extends MarkableBlock implements TextBlock {
    public Emphasis(List<TextBlock> content) {
        super(content);
        this.specifier = "*";
    }
}
