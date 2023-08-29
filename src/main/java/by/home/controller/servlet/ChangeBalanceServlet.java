package by.home.controller.servlet;

import by.home.controller.util.ParameterExtractor;
import by.home.data.dto.ChangeBalanceDto;
import by.home.factory.service.AccountServiceSingleton;
import by.home.service.api.IAccountService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.PrintWriter;

import static by.home.util.Constant.Utils.OPERATION_SUCCESSFUL;

@WebServlet("/accounts/changeBalance")
@Slf4j
public class ChangeBalanceServlet extends HttpServlet {

    private final IAccountService accountService;

    public ChangeBalanceServlet() {
        this.accountService = AccountServiceSingleton.getInstance();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        ChangeBalanceDto changeBalanceDto = ParameterExtractor.getChangeBalanceDto(req);
        this.accountService.changeBalance(changeBalanceDto);
        try (PrintWriter printWriter = resp.getWriter()) {
            printWriter.write(OPERATION_SUCCESSFUL);
        }
    }
}
