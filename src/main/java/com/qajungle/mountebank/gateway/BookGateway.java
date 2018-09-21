package com.qajungle.mountebank.gateway;

import com.qajungle.mountebank.domain.Book;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

public class BookGateway {

    private final RestTemplate restTemplate;
    private final String bookUrl;

    public BookGateway(RestTemplateBuilder restTemplateBuilder, @Value("${account.service.url}") String bookUrl) {
        this.restTemplate = restTemplateBuilder.build();
        this.bookUrl = bookUrl;
    }

    public Book getBook(long id){
        URI uri = UriComponentsBuilder.fromUriString(bookUrl)
                .pathSegment("book/" + id)
                .build()
                .toUri();

        ResponseEntity<Book> response = restTemplate.getForEntity(uri, Book.class);

        return response.getBody();

    }
}
