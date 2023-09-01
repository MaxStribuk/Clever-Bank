package by.home.controller.servlet;

import by.home.data.dto.ErrorDto;
import by.home.util.ConstraintViolationUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

import javax.validation.ConstraintViolationException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import static jakarta.servlet.RequestDispatcher.ERROR_EXCEPTION;

@Slf4j
@WebServlet("/error")
public class ErrorServlet extends HttpServlet {

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        handleException(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        handleException(req, resp);
    }

    private void handleException(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        Throwable exception = (Throwable) req.getAttribute(ERROR_EXCEPTION);
        if (exception instanceof ConstraintViolationException violationException) {
            List<ErrorDto> errors = ConstraintViolationUtil.getErrors(violationException.getConstraintViolations());
            writeAnswer(resp, errors.toString(), HttpServletResponse.SC_BAD_REQUEST);
        } else if (exception instanceof RuntimeException runtimeException) {
            writeAnswer(resp, runtimeException.getMessage(), HttpServletResponse.SC_BAD_REQUEST);
        } else {
            writeAnswer(resp, exception.getMessage(), HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    private void writeAnswer(HttpServletResponse resp, String message, int status)
            throws IOException {
        log.error(message);
        resp.setStatus(status);
        try (PrintWriter printWriter = resp.getWriter()) {
            printWriter.write(message);
        }
    }
}
