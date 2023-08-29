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
            log.error(errors.toString());
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            try (PrintWriter printWriter = resp.getWriter()) {
                printWriter.write(errors.toString());
            }
        } else if (exception instanceof RuntimeException runtimeException) {
            log.error(runtimeException.getMessage());
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, runtimeException.getMessage());
        } else {
            log.error(exception.getMessage());
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, exception.getMessage());
        }
    }
}
