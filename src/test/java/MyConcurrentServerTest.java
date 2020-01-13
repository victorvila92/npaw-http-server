import com.server.HandleRequest;
import com.server.MyConcurrentServer;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import static org.junit.Assert.*;
import static org.junit.Assert.assertTrue;

public class MyConcurrentServerTest {

    private static final int PORT = 8500;

    @Test
    public void correctArguments() {
        String[] args = {"-port", String.valueOf(PORT)};
        assertFalse(MyConcurrentServer.argumentsAreWrong(args));
    }

    @Test
    public void wrongArgumentPort() {
        String[] args = {"-p", String.valueOf(PORT)};
        assertTrue(MyConcurrentServer.argumentsAreWrong(args));
    }
    @Test
    public void wrongNumberPort() {
        String[] args = {"-port", "8080", "another_argument"};
        assertTrue(MyConcurrentServer.argumentsAreWrong(args));
    }

    @Test
    public void serverUp() throws Exception {
        String[] args = {"-port", String.valueOf(PORT)};
        int okHttpResponse = 200;
        MyConcurrentServer.main(args);
        URL url = new URL("http://localhost:" + PORT);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");
        assertEquals(okHttpResponse, con.getResponseCode());
    }

    @Test
    public void serverResponse() throws Exception {
        int newPort = PORT + 1;
        String[] args = {"-port", String.valueOf(newPort)};
        MyConcurrentServer.main(args);
        URL url = new URL("http://localhost:" + newPort);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");

        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuilder content = new StringBuilder();
        while ((inputLine = in.readLine()) != null) {
            content.append(inputLine);
        }
        assertTrue(HandleRequest.isHexValid(content.toString()));
    }
}
