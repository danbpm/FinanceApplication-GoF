package org.tigerbank.finance.service.export.impl;

import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import org.springframework.stereotype.Service;
import org.tigerbank.finance.config.JacksonConfig;
import org.tigerbank.finance.dto.FinanceData;
import org.tigerbank.finance.model.BankAccount;
import org.tigerbank.finance.model.Category;
import org.tigerbank.finance.model.Operation;
import org.tigerbank.finance.repository.IBankAccountRepository;
import org.tigerbank.finance.repository.ICategoryRepository;
import org.tigerbank.finance.repository.IOperationRepository;
import org.tigerbank.finance.service.export.DataService;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

@Service("CsvDataService")
public class CsvDataService extends DataService {
    private final ObjectMapper csvMapper;
    private final CsvSchema accountsSchema;
    private final CsvSchema operationSchema;
    private final CsvSchema categorySchema;

    public CsvDataService(IBankAccountRepository accountRepo,
                           ICategoryRepository categoryRepo,
                           IOperationRepository operationRepo) {
        super(accountRepo, categoryRepo, operationRepo);
        // Настраиваем ObjectMapper с Mixin'ами
        this.csvMapper = JacksonConfig.createCsvMapper();
        JacksonConfig.configureMapper(csvMapper);
        // создадим схемы
        this.accountsSchema = createBankAccountCsvSchema();
        this.operationSchema = createOperationCsvSchema();
        this.categorySchema = createCategoryCsvSchema();
    }

    @Override
    public void exportToFile(String directory) throws IOException {
        Path dir = Path.of(directory);
        Files.createDirectories(dir);

        exportAccountsToCsv(dir.resolve("accounts.csv").toString());
        System.out.println("Экспорт завершён: " + directory + "accounts.csv");

        exportCategoriesToCsv(dir.resolve("categories.csv").toString());
        System.out.println("Экспорт завершён: " + directory + "categories.csv");

        exportOperationsToCsv(dir.resolve("operations.csv").toString());
        System.out.println("Экспорт завершён: " + directory + "operations.csv");

    }

    @Override
    protected FinanceData parseFromFile(String directory) throws IOException {
        Path dir = Path.of(directory);

        System.out.println("Парсинг файла: " + directory + "accounts.csv");
        List<BankAccount> accounts = loadAccountsFromCsv(dir.resolve("accounts.csv").toString());
        System.out.println("Парсинг файла: " + directory + "operations.csv");
        List<Operation> operations = loadOperationsFromCsv(dir.resolve("operations.csv").toString());
        System.out.println("Парсинг файла: " + directory + "categories.csv");
        List<Category> categories = loadCategoriesFromCsv(dir.resolve("categories.csv").toString());

        return new FinanceData(accounts, categories, operations);
    }

    private void exportAccountsToCsv(String filePath) throws IOException {
        csvMapper.writer()
                .with(accountsSchema)
                .writeValue(new File(filePath), accountRepo.findAll());

    }

    private void exportOperationsToCsv(String filePath) throws IOException {
        csvMapper.writer()
                .with(operationSchema)
                .writeValue(new File(filePath), operationRepo.findAll());

    }

    private void exportCategoriesToCsv(String filePath) throws IOException {
        csvMapper.writer()
                .with(categorySchema)
                .writeValue(new File(filePath), categoryRepo.findAll());

    }

    private List<BankAccount> loadAccountsFromCsv(String filePath) throws IOException {
        MappingIterator<BankAccount> it = csvMapper.readerFor(BankAccount.class)
                .with(accountsSchema)
                .readValues(new File(filePath));
        return it.readAll();
    }

    private List<Category> loadCategoriesFromCsv(String filePath) throws IOException {
        MappingIterator<Category> it = csvMapper.readerFor(Category.class)
                .with(categorySchema)
                .readValues(new File(filePath));
        return it.readAll();
    }

    private List<Operation> loadOperationsFromCsv(String filePath) throws IOException {
        MappingIterator<Operation> it = csvMapper.readerFor(Operation.class)
                .with(operationSchema)
                .readValues(new File(filePath));
        return it.readAll();
    }

    private static CsvSchema createOperationCsvSchema() {
        return CsvSchema.builder()
                .addColumn("id")
                .addColumn("type")
                .addColumn("bankAccountId")
                .addColumn("amount")
                .addColumn("date")
                .addColumn("description")
                .addColumn("categoryId")
                .setUseHeader(true)
                .setColumnSeparator(',')
                .build();
    }

    private static CsvSchema createBankAccountCsvSchema() {
        return CsvSchema.builder()
                .addColumn("id")
                .addColumn("name")
                .addColumn("balance")
                .setUseHeader(true)
                .setColumnSeparator(',')
                .build();
    }

    private static CsvSchema createCategoryCsvSchema() {
        return CsvSchema.builder()
                .addColumn("id")
                .addColumn("name")
                .addColumn("type")
                .setColumnSeparator(',')
                .setUseHeader(true)
                .build();
    }
}
