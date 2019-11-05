package markup;
import java.util.List;

public class Paragraph extends MarkupBlock implements toList{
    public Paragraph(List<toParagraph> content) {
        super(content);
    }
    @Override
    public void toTex(StringBuilder builder) {
        for (toParagraph items : textField) {
            items.toTex(builder);
        }
    }
}