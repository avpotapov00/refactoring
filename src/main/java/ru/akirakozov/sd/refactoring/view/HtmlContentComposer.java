package ru.akirakozov.sd.refactoring.view;

import lombok.SneakyThrows;

import javax.servlet.http.HttpServletResponse;

public class HtmlContentComposer {

    @SneakyThrows
    public static void composeResponse(HttpServletResponse response, String content) {
        response.getWriter().println(content);
        response.setContentType("text/html");
        response.setStatus(HttpServletResponse.SC_OK);
    }

}
