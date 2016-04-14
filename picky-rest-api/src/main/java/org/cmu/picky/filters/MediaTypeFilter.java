package org.cmu.picky.filters;

import com.sun.jersey.spi.container.ContainerRequest;
import com.sun.jersey.spi.container.ContainerResponse;
import com.sun.jersey.spi.container.ContainerResponseFilter;

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;

public class MediaTypeFilter implements ContainerResponseFilter {
    public static final String CHARSET = "charset";
    public static final String UTF8 = "UTF-8";
    public static final String DEFAULT_ENCODING = UTF8;
    public static final MediaType DEFAULT_MEDIA_TYPE = MediaType.TEXT_HTML_TYPE;

    public ContainerResponse filter(ContainerRequest request, ContainerResponse response) {
        MediaType mediaType = response.getMediaType();
        if (mediaType == null) {
            mediaType = DEFAULT_MEDIA_TYPE;
        }

        if (!mediaType.getParameters().containsKey(CHARSET)) {
            StringBuilder typeBuilder = new StringBuilder(mediaType.toString()).append("; ");
            typeBuilder.append(CHARSET).append("=").append(DEFAULT_ENCODING);
            mediaType = MediaType.valueOf(typeBuilder.toString());
            response.getHttpHeaders().putSingle(HttpHeaders.CONTENT_TYPE, mediaType);
        }
        return response;
    }
}