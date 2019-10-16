package markup;
import java.util.List;

public class Paragraph extends MarkableBlock implements TextBlock {
    public Paragraph(List<TextBlock> content) {
        super(content);
    }
}
