# Plan

1. Lambda
2. Streaming API. Lazy Streams.
3. Design for lambda's
4. Either
5. Reduce and fold
6. Functional Java library

# Refactorings

## Before refactorings

1. Show source code, build, continuous build in Gradle
2. Enable source level 1.8 in Gradle build


## Lambda's. NumberUtil.

1. Multiple return statements, early abort
2. Replace with `str.chars()` and `allMatch` but with an anonymous inner class. No early abort, "declarative" style. Feel visitor pattern behind. API: http://docs.oracle.com/javase/8/docs/api/java/lang/CharSequence.html#i1, http://docs.oracle.com/javase/8/docs/api/java/util/stream/IntStream.html#i0
3. Change inner class by lambda. Lambda is a syntax sugar and nothing more. What can be a functional interface, OK for deafault methods, not OK for abstract classes, OK to have decalred exceptions.
4. jshell, implement isZero() wtih anonymous class, reimplement with lambda
5. How lambdas are called, interface with single method
6. Change lambda with a method reference. API: http://docs.oracle.com/javase/8/docs/api/java/lang/Character.html#i31
7. How method references work. static::, instance::, class::(instnace), ::new (compareWithM, getOpName, Sum::new;)


## Streaming API. OperatorsRegistry.

Lambdas are often used in new Streaming API:

1. Main - use stream from Arrays.stream(args). Streams can pipeline changes, can have terminal or non-terminal ops. Can caonsume once only.
2. Example with peek(print) . map(calculate) . peek(print) . filter(>10) . findFirst(). Update gradle.build run args.
3. Note that Streams are lazy. In example above even though there was an error in latter expressions, we stopped early.
4. Remove example, come back
5. We don't have an explicit loop, but no clear benefit yet - let's keep and see how it pays off later

1. OperatorsRegistry.of - creates a Map, fills in a Map, ... - refactor to use lambda. Arrays.stream() and forEach()
2. Still not good - creating an immutable collection at one place, filling it in later - can use Collectors.toMap
3. Explain Collector and Collectors


## Design for lambda's

1. Design for lambdas - can also build own code for it
2. Make Operator interface "functional" - we don't use `designation()` outside of registration anyway
3. First, refactor the code to get it working again (for OperatorRegistry - allow passing a map)
4. Now, looking at Sum, Subtraction, etc. - they can be replaced with lambdas
5. Git diff - whole bunch of classes removed
6.1. Hint - Java 8 comes with a lot of "functional" interfaces. Look at Javadoc. Use Java's BinaryOperator<Double>. Git diff again.
7. AlgebraicOperators serves sharing those operators, so let's keep it. Don't like the Map stuff though.

Note: so far we were changing a lot of things, but only those which concern us. We didn't even touch the
Calculator class. Separation of concerns is cool and we're good at it. With new tools we are even better.

Note 2: isn't it nice to de refactoring when you have tests?


## Either

1. Now the Main - we have the exception... Why would we? Let's design a class for "result or error"
2. ResultOrError. Now we still have an if in the Main
3. Add `forEach` a function to it. Better we have this if inside now though, or no if at all!
4. Also, construction is not evident - do we pass result or error
5. Notice how Main became declarative now. Instead of telling to iterate and checking, we "define" what the output is.
6. Also, it seems to be a common need. Let's factorize - `Either`.

1. Au passage - change `OperatorRegistry.find` to return an optional

1. Now the last piece - `Calculator` itself: for loop, multiple return statements.
2. We want to walk the stack and come to a single value - reduce it to a single value.
3. jshell - show reduce to sum an IntSteam, reduce to cancatenate Stream.of(1,2,3) to String.
4. Explain why combiner function is needed in case of parallel streams.
5. Start reduce implementation. Stuck on Either's lack of flatMap.
6. Implement flatMap. Show flatMap on Optinal in Javadoc: http://docs.oracle.com/javase/8/docs/api/java/util/Optional.html#i3
7. Use flatMap for final result - we want this value to be either an error or a result - we already have a thing for it!


## Functional Java

Add dependencies:

    compile "org.functionaljava:functionaljava:4.4"
    compile "org.functionaljava:functionaljava-java8:4.4"
    compile "org.functionaljava:functionaljava-quickcheck:4.4"

1. Get rid of Either, use the one from fj.date. Javadoc: https://functionaljava.ci.cloudbees.com/job/master/javadoc/fj/data/Either.RightProjection.html
2. Use fj.data.Array.array(...).foldLeft(...)
3. Use fj.data.Seq instead of Queue. Seq.empty, length(), tail(), head(), cons()
4. Inline tail().head() stuff...
5. Bonus - partially applied applyToken()


## Count the lines of code

Very simplistic, and yet:

    $ find src/main -name '*.java' | xargs wc -lA

And compare the same with original Java 7 implementation
