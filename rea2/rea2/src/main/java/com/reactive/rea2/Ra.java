package com.reactive.rea2;

import java.time.Duration;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import reactor.core.Disposable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.SignalType;
import reactor.util.function.Tuple2;
import reactor.util.function.Tuple3;

public class Ra {
	
	private Mono<String> val() {
		return Mono.just("it is working") // mono will not have null value and if it does the error would be shown 
				// but we have empty by using command empty instead of just
				.log(); // with the help of this log we can know how the data transaction between publisher and subscriber
	}
	
	private Flux<String> val2(){
		
		//we can also have an list of string 
		List<String> normalList = List.of("im","Sri","gopinath");
		// without fromIterable it is said to been an List with fromIterable we can have separate value
		return Flux.fromIterable(normalList) // it is for returing multiple values 
				.log();
	}
	
	private Flux<String> val3(){
		Flux<String> a = Flux.just("im","Sri","gopinath");
		return a.map(s-> s.toUpperCase(Locale.ROOT)); 
	}
	
	private Flux<String> val4(){
		Flux<String> a = Flux.just("im","Sri","gopinath");
		return a.flatMap(s-> Mono.just(s.toUpperCase(Locale.ROOT))).log(); // in this using Mono because we getting it from publisher 
	}
	
	private Flux<String> val6(){
		Flux<String> a = Flux.just("im","Sri","gopinath").delayElements(Duration.ofSeconds(1)).log() ; // print the elements in delay of 1 sec.
		return a.skip(Duration.ofSeconds(2)).log();
	}
	
	
	private Flux<String> val5(){
		Flux<String> a = Flux.just("im","Sri","gopinath");
		return a.delayElements(Duration.ofSeconds(1)).log() ; // print the elements in delay of 1 sec.
	}
	
	
	private Flux<Integer> val7(){
		Flux<Integer> a = Flux.range(1, 12);
		return a.log();
	}
	
	private Flux<String> val8(){
		Flux<String> a = Flux.just("im","Sri","gopinath").delayElements(Duration.ofSeconds(1));
		return  a.skipLast(1).log() ; // print the elements in delay of 1 sec.
	}
	
	private Flux<Integer> val9(){
		Flux<Integer> a = Flux.range(1, 24);
		return a.skipUntil(integer -> integer == 11).log();
	}
	
	private Flux<Integer> val10(){ // concat is nothing but linking 2 array or integers and it just add to integer completing of after the other
		Flux<Integer> a = Flux.range(1, 5);
		Flux<Integer> b = Flux.range(5,12);
		return Flux.concat(a,b).log();
		
	}
	
	private Flux<Integer> val11(){ // Merge add to integers alternatively like 1,2,2,3,3,....
		Flux<Integer> a = Flux.range(1, 5).delayElements(Duration.ofMillis(500));
		Flux<Integer> b = Flux.range(5,12).delayElements(Duration.ofMillis(500));
		return Flux.merge(a,b).log();
		
	}
	
	private Flux<Tuple2<Integer, Integer>> val12(){ // Tuple allows to merge the elemnts like first element from a and first element from b
		// it is like 1,5 / 2,6 / 3,7
		Flux<Integer> a = Flux.range(1, 5).delayElements(Duration.ofMillis(500));
		Flux<Integer> b = Flux.range(5,12).delayElements(Duration.ofMillis(500));
		return Flux.zip(a,b).log();
		
	}
	
	private Flux<Tuple3<Integer, Integer, Integer>> val13(){ // Tuple allows to merge the elemnts like first element from a and first element from b
		// it is like 1,5 / 2,6 / 3,7
		Flux<Integer> a = Flux.range(1, 5).delayElements(Duration.ofMillis(500));
		Flux<Integer> b = Flux.range(5,12).delayElements(Duration.ofMillis(500));
		Flux<Integer> c = Flux.range(12,20).delayElements(Duration.ofMillis(500));
		return Flux.zip(a,b,c).log();
		
	}
	
	private Flux<Tuple2<Integer, Integer>> val14(){ // even though it has 10 items , the mono is one it only shows 1 item in it.   
		Flux<Integer> a = Flux.range(1, 5).delayElements(Duration.ofMillis(500));
		Mono<Integer> b = Mono.just(1);
		return Flux.zip(a,b).log();
		
	}
	
	private Mono<List<Integer>> val15(){ // it allows to collect the integers and place it inside the list.   
		Flux<Integer> a = Flux.range(1, 5);
		return a.collectList();
		
	}
	
	private Flux<List<Integer>> val16(){ // it collect the item from publisher and holds it and send it to the subscriber all data at once.
		// it is like collecting items together into more then one list.
		Flux<Integer> a = Flux.range(1, 5).delayElements(Duration.ofMillis(1000));
		return a.buffer(Duration.ofMillis(2_100)); // it is like 2 pair in 1 milli sec
		
	}
	
	private Mono<Map<Integer, Integer>> val17(){ // it used to find the square of the number present.
		// a*a , 2*2
		Flux<Integer> a = Flux.range(1, 5);
		return a.collectMap(Integer -> Integer*Integer);	
	}
	
	private Flux<Integer> val18(){ // in dooneach consumer is a signal 
		Flux<Integer> a = Flux.range(1, 12);
		return a.doOnEach(signal -> {
			if(signal.getType() == SignalType.ON_COMPLETE) {
				System.out.println("im done");
			} else {
				System.out.println("im not");
			}
		});
	}

	
	private Flux<Integer> val19(){
		Flux<Integer> a = Flux.range(1,12);
		return a.doOnComplete(() -> System.out.println("Im done"));
	}
	
	
	private Flux<Integer> val20(){ // usefull when we want to track the subscription 
		Flux<Integer> a = Flux.range(1, 12);
		return a.doOnSubscribe(subscription -> System.out.println("Subscribed"));
	}
	
	private Flux<Integer> val21(){ // usefull when we want to track the subscription 
		Flux<Integer> a = Flux.range(1, 12).delayElements(Duration.ofSeconds(1));
		return a.doOnCancel(() -> System.out.println("Cancled"));
	}
	
	
	
	private Flux<Integer> ErrorHandling(){ // usually we should not throw error like this for practice purpose we are doing
		Flux<Integer> a = Flux.range(1, 12)
				.map(integer -> {
					if(integer == 7) {
						throw new RuntimeException("number miss  match");
					}
					return integer;
				});
		return a.onErrorContinue((throwable,o) -> System.out.println("It just an numberzz "+ o)); // resolving the error in this 
	}
	
	
	private Flux<Integer> Errorreturn(){ // usually we should not throw error like this for practice purpose we are doing
		Flux<Integer> a = Flux.range(1, 12)
				.map(integer -> {
					if(integer == 7) {
						throw new RuntimeException("number miss  match");
					}
					return integer;
				});
		return a.onErrorReturn(-1); // allow us to return an special value when an error happens 
	}
	
	private Flux<Integer> Errorresume(){ // usually we should not throw error like this for practice purpose we are doing
		Flux<Integer> a = Flux.range(1, 12)
				.map(integer -> {
					if(integer == 7) {
						throw new RuntimeException("number miss  match");
					}
					return integer;
				});
		Flux<Integer> specialErrorHandlerFlux = a.range(100,112 );
		return a.onErrorResume(throwable -> specialErrorHandlerFlux); // once the error happens in the first range onresume switches to specialErrorhandlerFlux.
	}
	
	private Flux<Integer> ErrorMap(){ // usually we should not throw error like this for practice purpose we are doing
		Flux<Integer> a = Flux.range(1, 12)
				.map(integer -> {
					if(integer == 7) {
						throw new RuntimeException("number miss  match");
					}
					return integer;
				});
		return a.onErrorMap(throwable -> new UnsupportedOperationException(throwable.getMessage())); // once the error happens in the first range onresume switches to specialErrorhandlerFlux.
	}
	
	
	public static void main(String [] args) throws InterruptedException {
		Ra trial = new Ra();
		trial.ErrorMap() // till this point subscriber can receive data from the publisher 
		.subscribe(data -> System.out.println(data)); // but we need subscriber in order to get obtain the data.
		
		
		
		//Disposable dis = trial.val21().subscribe(System.out::println);
		
		//Thread.sleep((long) 10_000); // it used to stop instant compiling and increase the run time.
		
		//dis.dispose();
	}
}
