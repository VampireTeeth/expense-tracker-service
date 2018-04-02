package org.vampireteeth.household.expensetracker.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.data.annotation.Id;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by steven on 2/04/18.
 */
public class Expense {
    private final String id;
    private final BigDecimal amount;
    private final Date purchasedAt;
    private final Date updatedAt;
    private final String description;
    private final String store;


    @JsonCreator
    public Expense(@JsonProperty(value = "amount", required = true) double amount,
                   @JsonProperty(value = "purchasedAt") Date purchasedAt,
                   @JsonProperty(value = "description", required = true) String description,
                   @JsonProperty(value = "store", required = true) String store) {
        this(null, amount, purchasedAt == null ? new Date() : purchasedAt, description, store);
    }

    public Expense(String id, double amount, Date purchasedAt, String description, String store) {
        this(id, BigDecimal.valueOf(amount), purchasedAt, new Date(), description, store);
    }

    public Expense(String id, BigDecimal amount, Date purchasedAt, Date updatedAt, String description, String store) {
        this.id = id;
        this.amount = amount;
        this.purchasedAt = purchasedAt;
        this.updatedAt = updatedAt;
        this.description = description;
        this.store = store;
    }

    @Id
    public String getId() {
        return id;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    public Date getPurchasedAt() {
        return purchasedAt;
    }

    @JsonIgnore
    public Date getUpdatedAt() {
        return updatedAt;
    }

    public String getDescription() {
        return description;
    }

    public String getStore() {
        return store;
    }

    @Override
    public String toString() {
        return "Expense{" +
                "id=" + id +
                ", amount=" + amount +
                ", purchasedAt=" + purchasedAt +
                ", description='" + description + '\'' +
                ", store='" + store + '\'' +
                '}';
    }
}
