package org.tigerbank.finance.service.export.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import org.tigerbank.finance.config.JacksonConfig;
import org.tigerbank.finance.dto.FinanceData;
import org.tigerbank.finance.repository.IBankAccountRepository;
import org.tigerbank.finance.repository.ICategoryRepository;
import org.tigerbank.finance.repository.IOperationRepository;
import org.tigerbank.finance.service.export.DataService;

import java.io.File;
import java.io.IOException;

@Service("YamlDataService")
public class YamlDataService extends DataService {
    private final ObjectMapper yamlMapper;
    private final String fileName = "full-data.yaml";

    public YamlDataService(IBankAccountRepository accountRepo,
                           ICategoryRepository categoryRepo,
                           IOperationRepository operationRepo) {
        super(accountRepo, categoryRepo, operationRepo);
        this.yamlMapper = JacksonConfig.createYamlMapper();
        JacksonConfig.configureMapper(yamlMapper);
    }

    @Override
    public void exportToFile(String filePath) throws IOException {
        FinanceData data = new FinanceData(
                accountRepo.findAll(),
                categoryRepo.findAll(),
                operationRepo.findAll()
        );

        yamlMapper.writerWithDefaultPrettyPrinter().writeValue(new File(filePath + fileName), data);
        System.out.println("Экспорт завершён: " + filePath + fileName);
    }

    @Override
    protected FinanceData parseFromFile(String filePath) throws IOException {
        System.out.println("Парсинг файла: " + filePath + fileName);
        return yamlMapper.readValue(new File(filePath + fileName), FinanceData.class);
    }
}
