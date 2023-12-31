package by.home.controller.servlet;

import by.home.controller.util.ParameterExtractor;
import by.home.data.dto.MoneyTransferDto;
import by.home.factory.service.AccountServiceSingleton;
import by.home.service.api.IAccountService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;

import static by.home.util.Constant.Utils.OPERATION_SUCCESSFUL;

/**
 * Сервлет для обработки запроса по переводу денежных средств
 * с одного счета на другой
 */
@WebServlet("/accounts/transferMoney")
public class MoneyTransferServlet extends HttpServlet {

    private final IAccountService accountService;

    public MoneyTransferServlet() {
        this.accountService = AccountServiceSingleton.getInstance();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        MoneyTransferDto moneyTransferDto = ParameterExtractor.getMoneyTransferDto(req);

        this.accountService.transferMoney(moneyTransferDto);

        try (PrintWriter printWriter = resp.getWriter()) {
            printWriter.write(OPERATION_SUCCESSFUL);
        }
    }
}
