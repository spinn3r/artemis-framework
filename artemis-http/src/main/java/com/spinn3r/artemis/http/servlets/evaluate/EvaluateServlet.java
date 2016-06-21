package com.spinn3r.artemis.http.servlets.evaluate;

import org.eclipse.jetty.servlet.DefaultServlet;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

/**
 *
 */
public class EvaluateServlet extends DefaultServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        // merge this and the RequestMeta servlet...

        String responseDescriptorText = request.getParameter("response");
        ResponseDescriptor responseDescriptor = ResponseDescriptor.parse(responseDescriptorText);

        for (Map.Entry<String, String> entry : responseDescriptor.getHeaders().entrySet()) {
            response.setHeader(entry.getKey(), entry.getValue());
        }

        for (ResponseDescriptor.Cookie currentCookie : responseDescriptor.getCookies()) {

            Cookie cookie = new Cookie(currentCookie.getName(), currentCookie.getValue());

            currentCookie.getDomain().ifPresent(cookie::setDomain);
            currentCookie.getPath().ifPresent(cookie::setPath);

            currentCookie.getMaxAge().ifPresent(cookie::setMaxAge);
            currentCookie.getHttpOnly().ifPresent(cookie::setHttpOnly);

            response.addCookie(cookie);

        }

        response.setContentType(responseDescriptor.getContentType());
        response.setCharacterEncoding(responseDescriptor.getCharacterEncoding());
        response.setStatus(responseDescriptor.getStatus());

    }

}
