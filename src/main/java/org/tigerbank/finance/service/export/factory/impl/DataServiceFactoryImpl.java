package org.tigerbank.finance.service.export.factory.impl;

import org.springframework.stereotype.Service;
import org.tigerbank.finance.repository.IBankAccountRepository;
import org.tigerbank.finance.repository.ICategoryRepository;
import org.tigerbank.finance.repository.IOperationRepository;
import org.tigerbank.finance.service.export.DataService;
import org.tigerbank.finance.service.export.factory.DataServiceFactory;
import org.tigerbank.finance.service.export.impl.CsvDataService;
import org.tigerbank.finance.service.export.impl.JsonDataService;
import org.tigerbank.finance.service.export.impl.YamlDataService;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;

@Service
public class DataServiceFactoryImpl implements DataServiceFactory {
    private final Map<String, Supplier<DataService>> registry = new ConcurrentHashMap<>();

    public DataServiceFactoryImpl(IBankAccountRepository accountRepo,
                                  ICategoryRepository categoryRepo,
                                  IOperationRepository operationRepo) {
        registry.put("json", () -> new JsonDataService(accountRepo, categoryRepo, operationRepo));
        registry.put("yaml", () -> new YamlDataService(accountRepo, categoryRepo, operationRepo));
        registry.put("csv",  () -> new CsvDataService(accountRepo, categoryRepo, operationRepo));
    }

    @Override
    public DataService getDataService(String type) {
        if (type == null) {
            throw new IllegalArgumentException("Unsupported type: null");
        }
        Supplier<DataService> supplier = registry.get(type.toLowerCase());
        if (supplier == null) {
            throw new IllegalArgumentException("Неподдерживаемый формат: " + type);
        }
        return supplier.get();
    }
}
