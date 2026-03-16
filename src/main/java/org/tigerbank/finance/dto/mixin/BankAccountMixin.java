package org.tigerbank.finance.dto.mixin;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * Mixin для сериализации/десериализации BankAccount без изменения доменной сущности
 */
public abstract class BankAccountMixin {
    @JsonCreator
    public BankAccountMixin(
            @JsonProperty("id") UUID id,
            @JsonProperty("name") String name,
            @JsonProperty("balance") BigDecimal balance
    ) {}
}