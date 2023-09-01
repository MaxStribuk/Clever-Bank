package by.home.controller.servlet;

import by.home.controller.util.ParameterExtractor;
import by.home.data.dto.AccountStatementDto;
import by.home.factory.service.StatementServiceSingleton;
import by.home.service.api.IStatementService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;

import static by.home.util.Constant.Utils.OPERATION_SUCCESSFUL;

/**
 * Сервлет для формирования выписки по банковскому счету
 */
@WebServlet("/transactions/statement")
public class StatementServlet extends HttpServlet {

    private final IStatementService statementService;

    public StatementServlet() {
        this.statementService = StatementServiceSingleton.getInstance();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        AccountStatementDto accountStatementDto = ParameterExtractor.getAccountStatementDto(req);
        this.statementService.createStatement(accountStatementDto);
        try (PrintWriter printWriter = resp.getWriter()) {
            printWriter.write(OPERATION_SUCCESSFUL);
        }
    }
}
