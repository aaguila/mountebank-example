package com.qajungle.mountebank.gateway;

import com.qajungle.mountebank.domain.Book;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mbtest.javabank.Client;
import org.mbtest.javabank.http.imposters.Imposter;
import org.springframework.boot.web.client.RestTemplateBuilder;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.FileReader;
import java.io.IOException;

public class BookGatewayTest {

    private static final String FILE = "src/test/resources/books_imposter.ejs";
    private static final String IMPOSTER_URL = "http://localhost:4545";

    private static final String TEST_BOOK_TITLE = "Moby Dick";
    private static final String TEST_BOOK_AUTHOR = "Herman Melville";
    private static final String TEST_BOOK_ISBN = "9787806261279";
    private static final long TEST_BOOK_ID = 1L;

    private static Client client = new Client();
    private static BookGateway bookGateway;

    @BeforeAll
    static void setup() {
        Imposter imposter = Imposter.fromJSON(getJsonObject());
        client.createImposter(imposter);
        bookGateway = new BookGateway(new RestTemplateBuilder(), IMPOSTER_URL);
    }

    @Test
    public void getBooksTest() {

        //when
        Book book = bookGateway.getBook(TEST_BOOK_ID);

        //then
        assertEquals(book.getTitle(), TEST_BOOK_TITLE);
        assertEquals(book.getAuthor(), TEST_BOOK_AUTHOR);
        assertEquals(book.getIsbn(), TEST_BOOK_ISBN);
    }


    @AfterAll
    static void clean() {
        client.deleteAllImposters();
    }

    private static JSONObject getJsonObject() {

        JSONObject jsonObject = new JSONObject();

        try {
            JSONParser parser = new JSONParser();
            Object obj = parser.parse(new FileReader(FILE));

            jsonObject = (JSONObject) obj;
        } catch (ParseException | IOException e) {
            e.printStackTrace();
        }

        return jsonObject;
    }

}
