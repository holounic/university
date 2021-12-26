package generator;

import util.Block;
import util.Import;
import util.NonTerminal;
import util.Terminal;
import java.util.List;
import java.util.stream.Collectors;
import java.io.File;
import java.io.IOException;
import java.io.FileWriter;

public class MainGen {
    private static final String COMMON_PATH = "fourth/src";
    private static final String DEFAULT_PACKAGE = "generated";
    private final String packageName;

    public MainGen(String packageName) {
        this.packageName = packageName;
    }

    public MainGen() {
        this(DEFAULT_PACKAGE);
    }

    public void generate(List<Block> blocks) {
        List<Terminal> terminals = blocks.stream()
                .filter(x -> x instanceof Terminal)
                .map(x -> (Terminal) x)
                .collect(Collectors.toList());
        List<NonTerminal> nonTerminals = blocks.stream()
                .filter(x -> x instanceof NonTerminal)
                .map(x -> (NonTerminal) x)
                .collect(Collectors.toList());
        List<Import> imports = blocks.stream()
                .filter(x -> x instanceof Import)
                .map(x -> (Import) x)
                .collect(Collectors.toList());

        writeJava(generateLexer(terminals), "GeneratedLexer.java");
        writeJava(generateEnum(terminals), "GeneratedEnum.java");
        writeJava(generateParser(nonTerminals, imports), "GeneratedParser.java");
    }

    private String generateParser(List<NonTerminal> blocks, List<Import> imports) {
        return addPackage(generateImports(imports) + '\n' + new ParserGen().generate(blocks));
    }

    private String generateEnum(List<Terminal> terminals) {
        return addPackage(new EnumGen().generate(terminals));
    }

    private String generateImports(List<Import> packages) {
        return new ImportGen().generate(packages);
    }

    private String addPackage(String generated) {
        return String.format("package %s;\n%s", packageName, generated);
    }

    private String generateLexer(List<Terminal> blocks) {
        return addPackage(new LexerGen().generate(blocks));
    }

    private void writeJava(String text, String fileName) {
        File packageDir = new File(COMMON_PATH + '/' + packageName);
        File f = new File(COMMON_PATH + '/' + packageName + '/' + fileName);
        packageDir.mkdir();
        try {
            f.createNewFile();
            try (FileWriter w = new FileWriter(f)) {
                w.write(tabulate(text));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String tabulate(String text) {
        int[] tabLevel = new int[]{0};
        return text.lines()
                .map(String::strip)
                .map(x -> {
                    if (x.startsWith("}") || x.startsWith(")")) {
                        tabLevel[0] = Math.max(0, tabLevel[0] - 1);
                    }
                    String result = "\t".repeat(tabLevel[0]) + x;
                    if (x.endsWith("{") || x.endsWith("(")) {
                        tabLevel[0]++;
                    }
                    return result;
                }).collect(Collectors.joining("\n"));
    }
}
