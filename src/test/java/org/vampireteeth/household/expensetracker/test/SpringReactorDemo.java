package org.vampireteeth.household.expensetracker.test;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;

import org.junit.Test;

import reactor.core.publisher.ConnectableFlux;
import reactor.core.publisher.Flux;
import reactor.core.publisher.ParallelFlux;
import reactor.core.publisher.UnicastProcessor;
import reactor.core.scheduler.Schedulers;

public class SpringReactorDemo {

	@Test
	public void errorHandlingPlay() {
		Flux.range(1, 100).map(this::failOnEveryThird).subscribe(System.out::println, System.err::println);
	}

	@Test
	public void transformOperator() {
		Function<Flux<String>, Flux<String>> filterAndMap = 
				f -> f.filter(c -> !c.equals("orange")).map(String::toUpperCase);
		Flux
			.fromIterable(Arrays.asList("blue","green","orange","purple"))
			.doOnNext(System.out::println)
			.transform(filterAndMap)
			.subscribe(c -> System.out.println("Subscriber to Transformed FilterAndMap: " + c))
		;
	}

	@Test
	public void composeOpertor() {
		AtomicInteger ai = new AtomicInteger(0);
		Function<Flux<String>, Flux<String>> filterAndMap = 
				f -> {
					if (ai.incrementAndGet() == 1) {
						return f.filter(c -> !c.equals("orange")).map(String::toUpperCase);
					}
					return f.filter(c -> !c.equals("purple")).map(String::toUpperCase);
				};
		Flux<String> flux = Flux
			.fromIterable(Arrays.asList("blue","green","orange","purple"))
			.doOnNext(System.out::println)
			.compose(filterAndMap);
		flux.subscribe(c -> System.out.println("Subscriber 1 to Transformed FilterAndMap: " + c));
		flux.subscribe(c -> System.out.println("Subscriber 2 to Transformed FilterAndMap: " + c));
	}

	@Test
	public void coldFlux() {
		Flux<String> s = Flux
			.fromIterable(Arrays.asList("blue","green","orange","purple"))
			.doOnNext(System.out::println)
			.filter(c -> c.startsWith("o"))
			.map(String::toUpperCase)
			;
		s.subscribe(c -> System.out.println("Subscriber 1: " + c));
		System.out.println("---------------");
		s.subscribe(c -> System.out.println("Subscriber 2: " + c));
	}

	@Test
	public void hotFlux() {
		UnicastProcessor<String> hotSource = UnicastProcessor.create();

		Flux<String> hotFlux = hotSource.publish().autoConnect().map(String::toUpperCase);

		hotFlux.subscribe(d -> System.out.println("Subscriber 1 to Hot Source: " + d));

		hotSource.onNext("blue");
		hotSource.onNext("green");

		hotFlux.subscribe(d -> System.out.println("Subscriber 2 to Hot Source: " + d));

		hotSource.onNext("orange");
		hotSource.onNext("purple");
		hotSource.onComplete();
	}

	@Test
	public void connectableFlux1() throws InterruptedException {
		Flux<Integer> source = Flux.range(1, 3).doOnSubscribe(s -> System.out.println("Subscribed to source"));
		ConnectableFlux<Integer> co = source.publish();
		co.subscribe(System.out::println, e -> {}, () -> {});
		co.subscribe(System.out::println, e -> {}, () -> {});
		System.out.println("Done with subscriptions");
		TimeUnit.SECONDS.sleep(5);
		System.out.println("Will now connect");
		co.connect();
	}
	
	@Test
	public void connectableFlux2() throws InterruptedException {
		Flux<Integer> source = Flux.range(1, 3).doOnSubscribe(s -> System.out.println("Subscribed to source"));
		Flux<Integer> autoCo = source.publish().autoConnect(3);
		autoCo.subscribe(System.out::println, e -> {}, () -> {});
		autoCo.subscribe(System.out::println, e -> {}, () -> {});
		System.out.println("Done with 2 subscriptions");
		TimeUnit.SECONDS.sleep(5);
		autoCo.subscribe(System.out::println, e -> {}, () -> {});
	}

	@Test
	public void windowBatching() {
		Flux<Integer> source = Flux
				.range(1, 10)
				.window(5, 3)
				.concatMap(g -> g.defaultIfEmpty(-1))
				;
		source.subscribe(System.out::println);

	}

	@Test
	public void publishOnOperator() {
		Flux<Integer> source = Flux
				.range(1, 10)
				.publishOn(Schedulers.parallel());
		for (int i = 1; i <= 10; i++) {
			final String msg =":Subscriber "+i+":";
			source.subscribe(v -> System.out.println(Thread.currentThread().getName()+msg+v), e->{}, ()->{});
		}
	}


	@Test
	public void parallelFlux() {
		ParallelFlux<Integer> source = Flux
				.range(1, 10)
				.parallel(3)
				.runOn(Schedulers.parallel());

		for (int i = 1; i <= 10; i++) {
			final String msg =":Subscriber "+i+":";
			source.subscribe(v -> System.out.println(Thread.currentThread().getName()+msg+v), e->{}, ()->{});
		}
	}

	private String failOnEveryThird(int num) {
		if (num % 3 == 0)
			throw new IllegalStateException("I am the third one");
		return String.valueOf(num);
	}

}
