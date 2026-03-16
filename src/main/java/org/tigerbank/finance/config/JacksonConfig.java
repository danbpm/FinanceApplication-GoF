package org.tigerbank.finance.config;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.tigerbank.finance.dto.mixin.BankAccountMixin;
import org.tigerbank.finance.dto.mixin.CategoryMixin;
import org.tigerbank.finance.dto.mixin.OperationMixin;
import org.tigerbank.finance.model.BankAccount;
import org.tigerbank.finance.model.Category;
import org.tigerbank.finance.model.Operation;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvParser;


public class JacksonConfig {
    public static void configureMapper(ObjectMapper mapper) {
        mapper.addMixIn(Operation.class, OperationMixin.class);
        mapper.addMixIn(Category.class, CategoryMixin.class);
        mapper.addMixIn(BankAccount.class, BankAccountMixin.class);

        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    public static ObjectMapper createJsonMapper() {
        ObjectMapper mapper = new ObjectMapper();
        configureMapper(mapper);
        return mapper;
    }

    public static ObjectMapper createCsvMapper() {
        CsvMapper mapper = CsvMapper.builder()
                .enable(CsvParser.Feature.WRAP_AS_ARRAY)
                .build();
        configureMapper(mapper);
        return mapper;
    }
    public static YAMLMapper createYamlMapper() {
        YAMLMapper mapper = YAMLMapper.builder()
                .enable(com.fasterxml.jackson.dataformat.yaml.YAMLGenerator.Feature.USE_PLATFORM_LINE_BREAKS)
                .build();
        configureMapper(mapper);
        return  mapper;
    }

}
