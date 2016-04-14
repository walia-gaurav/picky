package org.cmu.picky.filters;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.ws.rs.core.MediaType;
import java.io.IOException;
import java.util.*;

public class FileExtensionFilter implements Filter {
    private HashMap<String, String> extensionsToMimes;

    private static final String DEFAULT_MIME = MediaType.APPLICATION_JSON;

    @Override
    public void init(FilterConfig config) throws ServletException {
        extensionsToMimes = new HashMap<String, String>();

        Enumeration<String> extensions = config.getInitParameterNames();
        while (extensions.hasMoreElements()) {
            String extension = extensions.nextElement();
            extensionsToMimes.put("." + extension, config.getInitParameter(extension));
        }
    }

    @Override
    public void destroy() {}

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        chain.doFilter(new NoExtensionWrapper((HttpServletRequest)request), response);
    }

    private class NoExtensionWrapper extends HttpServletRequestWrapper {
        private String extension;

        public NoExtensionWrapper(HttpServletRequest request) {
            super(request);
            extension = getExtension(request.getRequestURI());
        }

        @Override
        public String getRequestURI() {
            String uri = super.getRequestURI();
            if (extensionsToMimes.containsKey(extension)) {
                return uri.substring(0, uri.length() - extension.length());
            } else {
                return uri;
            }
        }

        @Override
        public Enumeration<String> getHeaders(String name) {
            if ("Accept".equalsIgnoreCase(name)) {
                String type = extensionsToMimes.get(extension);
                if (type != null) {
                    return Collections.enumeration(Arrays.asList(type));
                }
            }
            Enumeration<String> providedHeaders = super.getHeaders(name);
            if (providedHeaders == null || !providedHeaders.hasMoreElements()) {
                return Collections.enumeration(Arrays.asList(DEFAULT_MIME));
            } else {
                return providedHeaders;
            }
        }

        @Override
        public String getHeader(String name) {
            if ("Accept".equalsIgnoreCase(name)) {
                String type = extensionsToMimes.get(extension);
                if (type != null) {
                    return type;
                }
            }

            String providedHeader = super.getHeader(name);
            return providedHeader == null ? DEFAULT_MIME : providedHeader;
        }

        @Override
        public Enumeration<String> getHeaderNames() {
            List<String> names = Collections.list(super.getHeaderNames());
            names.add("Accept");
            return Collections.enumeration(names);
        }

        private String getExtension(String path) {
            int index = path.lastIndexOf('.');
            if (index != -1) {
                return path.substring(index);
            } else {
                return "";
            }
        }
    }
}