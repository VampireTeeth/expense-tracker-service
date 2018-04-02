package org.vampireteeth.household.expensetracker.controller;

import org.reactivestreams.Publisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.vampireteeth.household.expensetracker.model.Expense;
import org.vampireteeth.household.expensetracker.repository.ExpenseRepository;
import reactor.core.publisher.Mono;

/**
 * Created by steven on 2/04/18.
 */

@RestController
public class ExpenseController {
    private final ExpenseRepository repo;

    @Autowired
    public ExpenseController(ExpenseRepository repo) {
        this.repo = repo;
    }

    @PostMapping("/expense")
    Mono<Void> create(@RequestBody Mono<Expense> expense) {
        return this.repo.saveAll(expense).then();
    }
}
