package markup;
import java.util.List;

public class Strong extends MarkableBlock implements TextBlock {
    public Strong(List <TextBlock> content) {
        super(content);
        this.specifier = "__";
    }
}
