import at.brigot.l33t.bl.ChatClient;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ChatTest {

    @Test
    public void givenGreetingClient_whenServerRespondsWhenStarted_thenCorrect() {
        ChatClient client = new ChatClient();
        client.startConnection("127.0.0.1", 6666);
        String response = client.sendMessage("hello server");
        assertEquals("hello client", response);
    }
}
