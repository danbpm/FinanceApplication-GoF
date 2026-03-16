package org.tigerbank.finance.dto.mixin;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.tigerbank.finance.model.CategoryType;

import java.util.UUID;

public abstract class CategoryMixin {
    @JsonCreator
    public CategoryMixin(
            @JsonProperty("id") UUID id,
            @JsonProperty("name") String name,
            @JsonProperty("type") CategoryType type
    ) {}
}