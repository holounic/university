package md2html;

public class Md2Html {

    public static void main(String[] args) {
        try {
            String inputFile = args[0];
            String outputFile = args[1];
            Reader reader = new Reader(inputFile);
            Writer writer = new Writer(outputFile);
            String line = reader.read();
            while (line != null) {
                Converter converter = new Converter(line);
                writer.write(converter.convert());
                line = reader.read();
            }
            reader.close();
            writer.close();
        } catch (Exception e){}

    }
}
