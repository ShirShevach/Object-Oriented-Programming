import java.util.Scanner;

class Chat {
    public static void main(String[] args) {
        ChatterBot[] bots = new ChatterBot[2];
        String[] badAnswer1 = {"say say " + ChatterBot.ILLEGAL_REQUEST_PLACEHOLDER + "?",
        "say " + ChatterBot.ILLEGAL_REQUEST_PLACEHOLDER + "?", "what?"};

        String[] badAnswer2 = {"say what? " + ChatterBot.ILLEGAL_REQUEST_PLACEHOLDER + "?",
                "No! I don't say " + ChatterBot.ILLEGAL_REQUEST_PLACEHOLDER + "!"};

        String[] goodAnswer1 = {"okay, here goes: " + ChatterBot.REQUESTED_PHRASE_PLACEHOLDER,
        ChatterBot.REQUESTED_PHRASE_PLACEHOLDER, "I say " + ChatterBot.REQUESTED_PHRASE_PLACEHOLDER + " now"};
        String[] goodAnswer2 = {ChatterBot.REQUESTED_PHRASE_PLACEHOLDER,
        "I like to say " + ChatterBot.REQUESTED_PHRASE_PLACEHOLDER + " :)"};

        ChatterBot bot1 = new ChatterBot("Rivka", goodAnswer1, badAnswer1);
        ChatterBot bot2 = new ChatterBot("Hana", goodAnswer2, badAnswer2);
        bots[0] = bot1;
        bots[1] =  bot2;

        Scanner scanner = new Scanner(System.in);
        String statement = "say Hello";
        while (true) {
            for (ChatterBot bot : bots) {
                statement = bot.replyTo(statement);
                System.out.print(bot.getName() + ": " + statement);
                scanner.nextLine();
            }

        }
    }
}
