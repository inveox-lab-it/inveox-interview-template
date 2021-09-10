package com.inveox;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class UrlShortenerTest {

    private final UrlShortener urlShortener = new UrlShortener();

    @Test
    public void shouldReturnShortUrlForLongUrl() {
        //given
        String longUrl = "http://inveox.com/v4/patients/idAB46#";

        //when
        var shortUrl = urlShortener.getShorten(longUrl);

        //then
        assertEquals(shortUrl.length(), 7);
    }

    @Test
    public void shouldReturnLongUrlForShortUrl() {
        //given
        String shortUrl = "idAB46#";

        //when
        var longUrl = urlShortener.getFullUrl(shortUrl);

        //then
        assertEquals("http://shortly.inveox.com/"+shortUrl, longUrl );
    }
}
