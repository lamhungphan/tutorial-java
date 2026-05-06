package module_2_core._46_text_blocks;

public class TextBlockUseCases {
    public static void main(String[] args) {
        // 1. JSON literal
        String json = """
                {
                  "name": "Alice",
                  "age": 30,
                  "skills": ["Java", "Spring", "SQL"]
                }
                """;
        System.out.println("--- JSON ---");
        System.out.println(json);

        // 2. SQL query (rất sạch, không cần "+ \n +")
        String sql = """
                SELECT u.id, u.email, COUNT(o.id) AS order_count
                FROM users u
                LEFT JOIN orders o ON o.user_id = u.id
                WHERE u.created_at > ?
                GROUP BY u.id, u.email
                HAVING COUNT(o.id) > 0
                ORDER BY order_count DESC
                """;
        System.out.println("--- SQL ---");
        System.out.println(sql);

        // 3. HTML/XML
        String html = """
                <html>
                  <body>
                    <h1>Hello, %s!</h1>
                    <p>Score: %d/%d</p>
                  </body>
                </html>
                """.formatted("Alice", 95, 100); // .formatted() (J15+) shortcut
        System.out.println("--- HTML ---");
        System.out.println(html);

        // 4. Multi-line error message
        String help = """
                Usage: app [OPTIONS] FILE

                Options:
                  -v, --verbose    Verbose output
                  -h, --help       Show this help
                """;
        System.out.println("--- HELP ---");
        System.out.println(help);

        // 5. Embedded " không cần escape
        String quoted = """
                He said "Hello" and she said "Hi back".
                """;
        System.out.println(quoted);

        // 6. Khi cần đúng 3 dấu """ trong chuỗi:
        String tricky = """
                It uses \"""triple-quote\""" syntax.
                """;
        System.out.println(tricky);
    }
}
