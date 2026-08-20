public class LeBron {
    private static final String NAME = "LeBron";

    /** Horizontal divider printed around each block of chatbot output. */
    private static final String LINE = "____________________________________________________________";

    /** ASCII art banner displayed on startup, spelling "LEBRON" in block letters. */
    private static final String banner =
            """
            #     ##### ####  ####   ###  #   #
            #     #     #   # #   # #   # ##  #
            #     ###   ####  ####  #   # # # #
            #     #     #   # #  #  #   # #  ##
            ##### ##### ####  #   #  ###  #   #
            """;

    public static void main(String[] args) {
        greet();
        exit();
    }

    /** Prints the startup banner and greeting, wrapped in divider lines. */
    private static void greet() {
        System.out.println(LINE);
        System.out.print(banner);
        System.out.println("Hello! I'm " + NAME + ".");
        System.out.println("What can I do for you?");
        System.out.println(LINE);
    }

    /** Prints the farewell message, wrapped in a divider line. */
    private static void exit() {
        System.out.println("Bye. Hope to see you again soon!");
        System.out.println(LINE);
    }
}
