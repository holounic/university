package markup;

import java.util.List;

public class ListItem implements TexBlock {
    private List<toList> textField;

    public ListItem(List<toList> content) {
        this.textField = content;
    }

    public void toTex(StringBuilder builder) {
        builder.append("\\item ");
        for (toList item : textField) {
            item.toTex(builder);
        }
    }
}
