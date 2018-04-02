package org.vampireteeth.household.expensetracker.model;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.assertj.core.util.Files;
import org.junit.Test;

import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.UUID;

/**
 * Created by steven on 2/04/18.
 */
public class ExpenseTest {

    @Test
    public void shouldParseJsonDataToExpense() throws Exception {
        URL url = ExpenseTest.class.getClassLoader().getResource("expense1.json");
        Path path = Paths.get(url.toURI());
        String jsonData = Files.contentOf(path.toFile(), "UTF-8");

        ObjectMapper mapper = new ObjectMapper();
        Expense expense = mapper.readValue(jsonData, Expense.class);
        System.out.println(expense);
    }

    @Test
    public void shouldWriteExpenseAsJsonData() throws JsonProcessingException {
        Expense expense = new Expense(UUID.randomUUID().toString(), 34.95, new Date(), "Another groceriese for baby", "Woolworth");
        ObjectMapper mapper = new ObjectMapper();
        String jsonData = mapper.writeValueAsString(expense);
        System.out.println(jsonData);
    }
}
