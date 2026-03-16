package org.tigerbank.finance.dto.mixin;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.tigerbank.finance.model.OperationType;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public abstract class OperationMixin {
    @JsonCreator
    public OperationMixin(
            @JsonProperty("id") UUID id,
            @JsonProperty("type") OperationType type,
            @JsonProperty("bankAccountId") UUID bankAccountId,
            @JsonProperty("amount") BigDecimal amount,
            @JsonProperty("date") LocalDate date,
            @JsonProperty("description") String description,
            @JsonProperty("categoryId") UUID categoryId
    ) {}
}