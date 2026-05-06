package module_2_core._46_text_blocks;

public class TextBlockBasics {
    public static void main(String[] args) {
        // Java 13/15 (JEP 378): Text Blocks bằng """ ... """
        // - Mở/đóng phải có newline ngay sau """
        // - Indent chung (incidental whitespace) bị strip tự động
        // - Type vẫn là String, không class mới

        String oldWay = "Line 1\n" +
                        "Line 2\n" +
                        "Line 3\n";

        String textBlock = """
                Line 1
                Line 2
                Line 3
                """;

        System.out.println(oldWay.equals(textBlock));   // true
        System.out.println(textBlock);

        // Indent reference: dòng cuối """ quyết định "lề trái"
        String aligned = """
                left edge here
                    +4 spaces
                back to left
                """;
        System.out.println(aligned);

        // Escape \s (space — giữ trailing space) và \ (line continuation)
        String trimmedNo = """
                AAA   \s
                BBB""";
        System.out.println("[" + trimmedNo + "]");

        String oneLine = """
                Hello \
                World""";       // \ ở cuối -> nối dòng
        System.out.println("[" + oneLine + "]");
    }
}
