package hr.abysalto.flight_search.exception;

import org.springframework.http.HttpMethod;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.util.StreamUtils;
import org.springframework.web.client.ResponseErrorHandler;

import java.io.IOException;
import java.net.URI;
import java.nio.charset.StandardCharsets;

public class RestErrorHandler implements ResponseErrorHandler {

    @Override
    public boolean hasError(ClientHttpResponse response) throws IOException {
        return response.getStatusCode().isError();
    }

    @Override
    public void handleError(URI url, HttpMethod method, ClientHttpResponse response) throws IOException {
        if (response.getStatusCode().is4xxClientError()) {
            throw new BadRequestException(StreamUtils.copyToString(response.getBody(), StandardCharsets.UTF_8));
        } else if (response.getStatusCode().is5xxServerError()) {
            throw new InternalServerErrorException(StreamUtils.copyToString(response.getBody(), StandardCharsets.UTF_8));
        }
    }
}
