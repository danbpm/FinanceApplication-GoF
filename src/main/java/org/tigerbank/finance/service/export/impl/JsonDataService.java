package org.tigerbank.finance.service.export.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.tigerbank.finance.config.JacksonConfig;
import org.tigerbank.finance.dto.FinanceData;
import org.tigerbank.finance.repository.IBankAccountRepository;
import org.tigerbank.finance.repository.ICategoryRepository;
import org.tigerbank.finance.repository.IOperationRepository;
import org.springframework.stereotype.Service;
import org.tigerbank.finance.service.export.DataService;

import java.io.File;
import java.io.IOException;

@Service("JsonDataService")
public class JsonDataService extends DataService {
    private final ObjectMapper jsonMapper;
    private final String fileName = "full-data.json";

    public JsonDataService(IBankAccountRepository accountRepo,
                           ICategoryRepository categoryRepo,
                           IOperationRepository operationRepo) {
        super(accountRepo, categoryRepo, operationRepo);
        // Настраиваем ObjectMapper с Mixin'ами
        this.jsonMapper = JacksonConfig.createJsonMapper();
        JacksonConfig.configureMapper(jsonMapper);
    }

    @Override
    public void exportToFile(String filePath) throws IOException {
        FinanceData data = new FinanceData(
                accountRepo.findAll(),
                categoryRepo.findAll(),
                operationRepo.findAll()
        );


        jsonMapper.writerWithDefaultPrettyPrinter().writeValue(new File(filePath + fileName), data);
        System.out.println("Экспорт завершён: " + filePath + fileName);
    }

    @Override
    protected FinanceData parseFromFile(String filePath) throws IOException {
        System.out.println("Парсинг файла: " + filePath + fileName);
        return jsonMapper.readValue(new File(filePath + fileName), FinanceData.class);
    }
}