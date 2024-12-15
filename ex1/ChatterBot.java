import java.util.*;

/**
 * Base file for the ChatterBot exercise.
 * The bot's replyTo method receives a statement.
 * If it starts with the constant REQUEST_PREFIX, the bot returns
 * whatever is after this prefix. Otherwise, it returns one of
 * a few possible replies as supplied to it via its constructor.
 * In this case, it may also include the statement after
 * the selected reply (coin toss).
 * @author Dan Nirel
 */
class ChatterBot {
    static final String REQUEST_PREFIX = "say ";
    static final String REQUESTED_PHRASE_PLACEHOLDER = "<phrase>";
    static final String ILLEGAL_REQUEST_PLACEHOLDER = "<request>";


    Random rand = new Random();
    String[] repliesToIllegalRequest;
    String[] repliesToLegalRequest;
    private String name;

    /**
     * Constructor.
     * @param name
     * @param repliesToLegalRequest
     * @param repliesToIllegalRequest
     */
    ChatterBot(String name, String[] repliesToLegalRequest, String[] repliesToIllegalRequest) {
        this.repliesToIllegalRequest = new String[repliesToIllegalRequest.length];
        for(int i = 0 ; i < repliesToIllegalRequest.length ; i = i+1) {
            this.repliesToIllegalRequest[i] = repliesToIllegalRequest[i];
        }

        this.repliesToLegalRequest = new String[repliesToLegalRequest.length];
        for(int i = 0 ; i < repliesToLegalRequest.length ; i = i+1) {
            this.repliesToLegalRequest[i] = repliesToLegalRequest[i];
        }

        this.name = name;
    }

    /**
     * The function return the name of its bot
     * @return
     */
    String getName() {
        return this.name;
    }

    /**
     * The function get the current statement,
     * classify the kind of the statement,
     * and return the next statement.
     * @param statement
     * @return The next sentences
     */
    String replyTo(String statement) {
        if(statement.startsWith(REQUEST_PREFIX)) {
            String phrase = statement.replaceAll(REQUEST_PREFIX, "");
            return replacePlaceholderInARandomPattern(phrase, repliesToLegalRequest, REQUESTED_PHRASE_PLACEHOLDER);
        }
        return replacePlaceholderInARandomPattern(statement, repliesToIllegalRequest, ILLEGAL_REQUEST_PLACEHOLDER);
    }

    /**
     * The function choose randomly a statement
     * from the current array (good or bad answer)
     * @param phrase
     * @param kindArray
     * @param kindStatement
     * @return
     */
    String replacePlaceholderInARandomPattern(String phrase, String[] kindArray, String kindStatement) {
        int randomIndex = rand.nextInt(kindArray.length);
        String reply = kindArray[randomIndex];
        reply = reply.replaceAll(kindStatement, phrase);
        return reply;
    }
}
