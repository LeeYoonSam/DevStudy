# Library
- [Dagger Hilt Koin 비교](#dagger-hilt-koin-비교)
- [RxJava](#rxjava)

---

## [Dagger, Hilt, Koin 비교](https://ichi.pro/ko/dagger-hilt-mich-koin-eun-hudeu-alaeeseo-eotteohge-daleungayo-215221250120151)

### Hilt
- 프로젝트에서 DI 실행하는 상용구를 줄이는 Android 용 DI 라이브러리
- 기존 DI 라이브러리 Dagger 를 래핑하여 훨씬 더 쉬운 사용성을 제공
- Dagger-Android 와 비교했을 때 Annotation, Module, Component 관계 Scope 설정을 위한 러닝커브가 높았던 문제를 해결
- 내부적으로 Dagger 를 사용하므로 Dagger 의 모든 내용은 Hilt 에도 적용 된다. 
- 2020년 6월에 Dagger Hilt 를 발표

- 종속성: 일부 핵심 클래스에 종속성을 주입
- 호출방법: 처음 한번만 호출(명시적인 호출이 없음)
- 의존 관계: 의존 관계 파악이 쉬움(대거 거터 지원)

### Dagger
- 클래스의 인스턴스를 제공하기 위해 `@Inject` 어노테이션만 추가하면 된다.
- `@Inject` 어노테이션을 추가하게 되면 빌드시 Class_Factory 형태로 해당 클래스의 팩토리를 생성한다.
- 이 팩토리 클래스는 인스턴스를 만드는데 필요한 모든 정보가 포함되어 있다.
- 이것은 실제로 `Provider<T>` 클래스가 구현하는 인터페이스에 지정된 메서드다. 다른 클래스는 `Provider<T>` 인터페이스를 사용하여 클래스의 인스턴스를 얻을 수 있다.

**의존성 주입 시점**
- Build -> (Di Container, 객체 생성, 주입) generate -> run

### Koin
**특징**
- Kotlin 개발 환경에 쉽게 적용할 수 있는 경량화된 DI 프레임워크.
- Kotlin DSL 을 사용
- 런타임에서 의존성 주입을 한다.
- Annotation Processing 을 사용하지 않고 빌드 타임에 Stub 파일을 생성하지 않는다.
- 프로젝트에 필요한 각 클래스의 인스턴스를 만드는데 사용할 팩토리를 모듈에게 제공해야 한다.
- 이러한 팩토리에 `InstanceRegistry`에 대한 참조는 우리가 작성한 모든 팩토리에 대한 참조를 포함하는 클래스에 추가 된다.
- instances 맵의 키는 명명 된 매개 변수를 사용하는 경우 제공 한 클래스의 전체 이름이고, 값은 클래스의 인스턴스를 만드는데 사용되는 작성항 팩토리다.
- 의존성을 얻기 위해 `get()` 이나 `by inject()`를 호출 하거나 내부에서 `get()` 은 `by lazy` 로 호출하는 액티비티나 프레그먼트에서 위임 된 속성을 호출해야 한다.

- 종속성: 모든 클래스가 서비스 로케이터에 종속
- 호출방법: 인젝터를 직접 호출(명시적인 호출)
- 의존 관계: 의존 관계 파악이 어려움

**의존성 주입 시점**
- Build -> run (Di Container, DI 객체 생성 및 주입) generate -> run

**장점**
- 러닝커브가 낮아 쉽고 빠르게 DI를 적용할 수 있다.
- Kotlin 개발 환경에서 도입하기에 가장 가볍고 빠르게 적용 가능한 방법이라고 생각한다.
- 별도의 어노테이션을 사용하지 않기 때문에 컴파일 시간이 단축된다.
- ViewModel 주입을 쉽게 할 수 있는 별도의 라이브러리를 제공한다.

**단점**
- 런타임시 컴포넌트가 생성이 되어있지 않다면 크래시가 발생
- 런타임에 서비스 로케이팅을 통해 인스턴스를 동적으로 주입해주기 때문에 런타임 퍼포먼스가 떨어진다.
- 리플렉션을 이용하기 때문에 성능상 좋지 않다.
- koin.get() 함수와 같이 모듈간 의존성에 대해 신경을 쓰지 않고 인스턴스를 사용하는 경우, 추후 멀티모듈로 전향 시 어려움을 겪을 수 있다.


### 여러가지 비교
1. 오류 처리
    - Dagger: `컴파일 타임 빌드`, 컴파일 시간 의존성 주입 프레임워크, 코드를 잘못 입력하거나 실수가 발생하면 `빌드 실패`
    - Koin: `런타임 빌드`, 클래스에 대한 팩토리를 추가하는 것을 잊은 경우 코드를 생성하지 않기 때문에 앱이 빌드되지만 이 클래스의 인스턴스를 요청하면 충돌이 발생, 그러므로 나중에 추가 화면에서 또는 사용자가 특정 작업을 수행 할 때도 발생 할 수 있다.

2. 빌드 시간
    - Dagger: Annotation 프로세서를 사용하여 코드를 스캔하고 적절한 클래스를 생성해야 하므로 시간이 걸리고 빌드 속도가 느려질 수 있다.
    - Koin: 어떤 코드도 생성하지 않는다는 장점이 있어서 빌드 시간에 미치는 영향이 훨씬 적다.

3. 런타임 성능에 미치는 영향
    - Dagger: 컴파일 타임에 종속성을 해결하기 때문에 런타입 성능에 거의 영향을 미치지 않는다.
    - Koin: 런타임에 종속성을 해결 하기 때문에 런타임 성능이 약간 떨어진다.

## RxJava
> ReactiveX는 관찰 가능한 시퀀스를 사용하여 비동기 및 이벤트 기반 프로그램을 구성하기 위한 라이브러리입니다.

- ReactiveX는 관찰가능한 절차를 통해 비동기, 이벤트 기반 프로그램을 구성하기 위한 라이브러리이다.
- Observer Pattern을 확장하며, Sequence를 조합할 수 있는 연산자를 지원한다.
- low-level Thread, 동기화, Thread 안전성, non-blocking I/O에 관한 우려를 줄인다.

### RxJava 는 가볍다.
> RxJava는 매우 가볍습니다. Observable 추상화 및 관련 고차 함수에만 초점을 맞춘 단일 JAR로 구현됩니다.

### RxJava 는 다중 언어 구현이다.
- RxJava는 Java 6 이상과 Groovy, Clojure, JRuby, Kotlin 및 Scala와 같은 JVM 기반 언어를 지원합니다.
- RxJava는 Java/Scala보다 더 많은 다중 언어 환경을 의미하며 각 JVM 기반 언어의 관용구를 존중하도록 설계되었습니다. 

### Observables 를 사용하는 이유?
- ReactiveX Observable 모델을 사용하면 배열과 같은 데이터 항목 컬렉션에 사용하는 것과 동일한 종류의 단순하고 구성 가능한 작업으로 비동기 이벤트 스트림을 처리할 수 있습니다. 
- 얽힌 콜백에서 벗어나 코드를 더 읽기 쉽게 만들고 버그를 덜 발생시킵니다.

### Scheduler
> Observable 연산자 체인에 멀티스레딩을 적용하고 싶다면, 특정 스케줄러를 사용해서 연산자(또는 특정 Observable)를 실행하면 된다.

**스케줄러 개념**
> RxJava의 스케줄러는 RxJava의 코드가 어느 스레드에서 실행될 것인지 지정하는 역할을 한다. RxJava만 사용한다고 비동기 처리가 되는 것이 아니라, 스케줄러를 통해 스레드를 분리해주어야 비동기 작업이 가능한 것이다. 스케줄러의 지정은 RxJava의 subscribeOn 과 observeOn 연산자를 통해 가능하다.

- Observable과 연산자 체인은 이처럼 스케줄러를 통해 동작하고 Subscribe 메서드가 호출되는 스레드를 사용해서 옵저버에게 알림을 보낸다. 
- `subscribeOn`은 Observable이 데이터 흐름을 발생시키고 연산하는 스케줄러를 지정한다.
- `subscribeOn` 연산자는 `Observable이 연산을 위해 사용할 스레드를 지정`하며, 연산자 체인 중 아무 곳에서 호출해도 문제되지 않는다. 
- `observeOn`은 Observable이 Observer에게 알림을 보내는 스케줄러를 명시한다.
- `ObserveOn` 연산자는 연산자 체인 중 Observable이 사용할 스레드가 호출 체인 중 어느 시점에서 할당되는지에 따라 그 후에 호출되는 연산자는 영향을 받는다. 그렇기 때문에, 어쩌면 특정 연산자를 별도의 스레드에서 실행 시키기 위해 연산자 체인 중 한 군데 이상에서 ObserveOn을 호출하게 될 것이다.


| 스케줄러 | 생성방법 | 내용 |
| --- | --- | --- |
| SINGLE | `Schedules.single()` | 단일 스레드를 생성해 계속 재사용 |
| COMPUTATION | `Schedules.computation()` | 내부적으로 스레드 풀 생성, 스레드 개수=프로세서 개수 |
| IO | `Schedules.io()` | 필요할때마다 스레드를 계속 생성 |
| TRAMPOLINE | `Schedules.trampoline()` | 	현재 스레드에 무한한 크기의 대기 큐 생성 |
| NEW_THREAD | `Schedules.newThread()` | 매번 새로운 스레드 생성 |

 - RxJava에서는 이 중 `Computation`, `IO`, `Trampoline` 세 가지의 스케줄러를 권장한다.

---

> Single Thread Scheduler
- Single 스레드 스케줄러는 단일 스레드를 계속 재사용한다. 
- RxJava 내부에서 스레드를 별도로 생성하며, 한 번 생성된 스레드로 여러 작업을 처리한다. 
- 비동기 처리를 지향한다면 Single 스레드 스케줄러를 사용할 일은 거의 없다.

```java
String[] source = {"First", "Second", "Third"};
Observable.fromArray(source)
        .observeOn(Schedulers.single())
        .subscribe( data -> {
            System.out.println("Observe On : "+Thread.currentThread().getName()+" | "+"value : "+data);
        });
Thread.sleep(100);

[실행결과]
Observe On : RxSingleScheduler-1 | value : First
Observe On : RxSingleScheduler-1 | value : Second
Observe On : RxSingleScheduler-1 | value : Third
```

> Computation Thread Scheduler
- Computation 스레드 스케줄러는 CPU에 대응하는 계산용 스케줄러이다. 
- IO 작업을 하지 않고 일반적인 계산/연산 작업을 할 때 사용한다. 
- 내부적으로 스레드 풀을 생성하고 생성된 스레드를 이용한다. 
- 기본적으로 스레드의 개수는 프로세서의 개수와 같다.

```java
String[] source = {"First", "Second", "Third"};
Observable.fromArray(source)
        .observeOn(Schedulers.computation())
        .subscribe( data -> {
            System.out.println("Observe On : "+Thread.currentThread().getName()+" | "+"value : "+data);
        });
Thread.sleep(100);

[실행결과]
Observe On : RxComputationThreadPool-1 | value : First
Observe On : RxComputationThreadPool-1 | value : Second
Observe On : RxComputationThreadPool-1 | value : Third
```

> IO Thread Scheduler
- 파일 입출력 등의 IO 작업을 하거나 네트워크 요청 처리 시에 사용하는 스케줄러이다. 
- Computation 스케줄러와 다르게 필요할 때마다 스레드를 계속 생성한다.

```java
String[] source = {"First", "Second", "Third"};
Observable.fromArray(source)
        .observeOn(Schedulers.io())
        .subscribe( data -> {
            System.out.println("Observe On : "+Thread.currentThread().getName()+" | "+"value : "+data);
        });
Thread.sleep(100);

[실행결과]
Observe On : RxCachedThreadScheduler-1 | value : First
Observe On : RxCachedThreadScheduler-1 | value : Second
Observe On : RxCachedThreadScheduler-1 | value : Third
```

> Trampoline Thread Scheduler
- 트램펄린 스케줄러는 새로운 스레드를 생성하지 않고 사용하고 있는 현재 스레드에 무한한 크기의 대기 큐를 생성한다.

```java
String[] source = {"First", "Second", "Third"};
Observable.fromArray(source)
        .observeOn(Schedulers.trampoline())
        .subscribe( data -> {
            System.out.println("Observe On : "+Thread.currentThread().getName()+" | "+"value : "+data);
        });
Thread.sleep(100);

[실행결과]
Observe On : main | value : First
Observe On : main | value : Second
Observe On : main | value : Third
```

> New Thread Scheduler
- New Thread 스케줄러는 다른 스케줄러와 달리 요청을 받을 때 마다 매번 새로운 스레드를 생성한다.

```java
String[] source = {"First", "Second", "Third"};
Observable.fromArray(source)
        .observeOn(Schedulers.newThread())
        .subscribe( data -> {
            System.out.println("Observe On : "+Thread.currentThread().getName()+" | "+"value : "+data);
        });
Thread.sleep(100);

[실행결과]
Observe On : RxNewThreadScheduler-1 | value : First
Observe On : RxNewThreadScheduler-1 | value : Second
Observe On : RxNewThreadScheduler-1 | value : Third
```

> Thread Pool을 직접 생성하는 방법
- Java에서는 Executor를 통해 스레드 풀을 직접 생성할 수 있으며, 생성한 스레드 풀을 Rxjava에서 사용할 수 있다.

```java
String[] source = {"First", "Second", "Third"};
Executor executor = Executors.newFixedThreadPool(10);
Observable.fromArray(source)
        .observeOn(Schedulers.from(executor))
        .subscribe( data -> {
            System.out.println("Observe On : "+Thread.currentThread().getName()+" | "+"value : "+data);
        });
Thread.sleep(100);

[실행결과]
Observe On : pool-1-thread-1 | value : First
Observe On : pool-1-thread-1 | value : Second
Observe On : pool-1-thread-1 | value : Third
```

--- 

### 참고
- [RxJava-wiki](https://github.com/ReactiveX/RxJava/wiki)
- [ReactiveX](http://reactivex.io/intro.html)
- [Scheduler](http://reactivex.io/documentation/scheduler.html)
- [[RxJava] RxJava 이해하기 - 5. 스케줄러](https://4z7l.github.io/2020/12/14/rxjava-5.html)