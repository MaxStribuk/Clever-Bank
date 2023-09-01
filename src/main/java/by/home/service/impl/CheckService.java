package by.home.service.impl;

import by.home.aop.api.Loggable;
import by.home.dao.entity.TransactionType;
import by.home.data.dto.TransactionDto;
import by.home.service.api.ICheckService;
import by.home.service.api.IPdfService;
import lombok.RequiredArgsConstructor;

import java.time.format.DateTimeFormatter;

import static by.home.util.Constant.Utils.CHECK_FILE_NAME;
import static by.home.util.Constant.Utils.CHECK_TEMPLATE;
import static by.home.util.Constant.Utils.DATE_PATTERN;

/**
 * класс для генерации чека
 */
@RequiredArgsConstructor
public class CheckService implements ICheckService {

    private final IPdfService pdfService;

    /**
     * создает текст чета и путь, по которому будет создан pdf файл
     * @param transaction dto с набором полей, необходимых для создания чека
     */
    @Override
    @Loggable
    public void createCheck(TransactionDto transaction) {
        String transactionId = transaction.getId().toString();
        String fileName = String.format(CHECK_FILE_NAME, transactionId);
        String formattedCheck = getFormattedCheck(transaction, transactionId);
        this.pdfService.createPdf(formattedCheck, fileName);
    }

    private String getFormattedCheck(TransactionDto transaction, String transactionId) {
        return String.format(CHECK_TEMPLATE,
                transactionId,
                transaction.getTime().toLocalDate().format(DateTimeFormatter.ofPattern(DATE_PATTERN)),
                transaction.getTime(),
                TransactionType.getTransactionType(transaction.getTypeId()).getDescription(),
                transaction.getBankFrom(),
                transaction.getBankTo(),
                transaction.getAccountFrom(),
                transaction.getAccountTo(),
                transaction.getAmount());
    }
}
