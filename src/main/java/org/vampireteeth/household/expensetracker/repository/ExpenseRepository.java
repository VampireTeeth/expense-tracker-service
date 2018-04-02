package org.vampireteeth.household.expensetracker.repository;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import org.vampireteeth.household.expensetracker.model.Expense;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Created by steven on 2/04/18.
 */
@Repository
public interface ExpenseRepository extends ReactiveCrudRepository<Expense, String> {

    Flux<Expense> findByDescription(Mono<String> description);

    Flux<Expense> findByStore(Mono<String> store);
}
