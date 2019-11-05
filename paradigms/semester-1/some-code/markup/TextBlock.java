package markup;

public interface TextBlock extends TexBlock {
    void toMarkdown(StringBuilder builder);
}