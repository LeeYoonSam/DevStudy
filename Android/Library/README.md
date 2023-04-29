# Library
- [Dagger Hilt Koin 비교](#dagger-hilt-koin-비교)
- [RxJava](#rxjava)
- [RxJava Hot, Cold Observable](#rxjava-hot-cold-observable)
- [RxJava Backpressure(배압)](#rxjava-backpressure배압)
- [Glide](#glide)

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
- instances 맵의 키는 명명 된 매개 변수를 사용하는 경우 제공 한 클래스의 전체 이름이고, 값은 클래스의 인스턴스를 만드는데 사용되는 구현된 팩토리다.
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
    - Dagger: 컴파일 타임에 종속성을 해결하기 때문에 런타임 성능에 거의 영향을 미치지 않는다.
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


## RxJava Hot, Cold Observable

### Hot Observable
> 획득한 순간 Subscriber 여부와 관계 없이 즉시 이벤트를 방출한다. 심지어 아무도 듣고 있지 않아도 이벤트를 다운스트림으로 밀어내기 때문에 이벤트 유실이 있을 수 있다.

- 차가운 Observable 은 전적으로 통제가 가능하지만 뜨거운 Observable 은 소비자로부터 독립적이다.
- Subscriber가 나타나면, 뜨거운 Observable은 흘러가는 이벤트를 그대로 게시한다.
- Subscriber 의 존재 여부가 Observable의 동작에 영향을 미치지 않으며, 서로 완전히 분리되어 있고 독립적이다.
- 보통 이벤트 소스를 전혀 통제할 수 없는 경우에 발생한다.
- 일반적으로 외부에서 발생하여 오는 그대로 이벤트를 표현하는데, 이벤트를 시계열에 맞춰 늘어놓기 때문에 해당 값이 언제 발생했는지가 무척 중요하다.

**뜨거운 Observable 예시**
- 마우스 움직임이나 키보드 입력등

### Cold Observable
> 전적으로 느긋하여 실제로 누군가 관심을 기울이지 않으면 절대 이벤트 방출을 시작하지 않는다. 관찰자가 없으면 Observable은 단순히 정적 자료 구조일 뿐이다. 이벤트는 느긋하게 만들어지며 어떤 식으로든 캐시 처리되지 않기 때문에 모든 구독자는 각자 별도로 스트림의 복사본을 받는다는 뜻이기도 하다.

- 일반적으로 Observable.create() 를 써서 만든다.
- Observable.just(), from(), range() 등도 있다.
- 누군가 수신하지 않는 한 어떤 작업도 시작하면 안되고 실행을 연기해야 한다.
- 어느정도 Subscriber 에 의존한다.
- 요청할 때마다 값을 생성하며 여러 번 요청해도 되기 때문에 항목이 정확히 언제 만들어졌는지 별로 중요하지 않다.


## RxJava Backpressure(배압)
> 관찰자가 소비하는 것보다 더 빠르게 항목을 생성하는 관찰 가능 항목에 대처하기 위한 전략

<br/>

RxJava 에는 구독자보다 더 Activity적인 생산자를 다루는 두 가지 방법이 있다.
    
1. 내장된 연산자를 사용한 샘플링이나 일괄 처리와 같은 다양한 흐름 제어 방식을 구현한다.
2. 구독자는 배압(backpressure)이라 부르는 피드백 채널을 사용하여 처리할 수 있는 만큼 항목을 요청하고 이를 전파할 수 있다.

<br/>

### 국어사전  뜻
> 증기 원동기 또는 내연 기관에서 뿜어져 나오는 증기나 가스의 압력. 압력이 높으면 기관의 효율이 떨어진다.

<br/>

### 특징
- ReactiveX에서는 Observable이 오퍼레이터나 관찰자가 소비할 수 있는 것보다 더 빠르게 항목을 방출하는 상황에 빠지는 것이 어렵지 않습니다. 이것은 소비되지 않은 품목의 증가하는 잔고를 어떻게 해야 하는지에 대한 문제를 나타냅니다.

- 예를 들어, Zip 연산자를 사용하여 두 개의 무한 Observable을 함께 압축한다고 상상해보세요. 연산자의 일반적인 구현은 더 빠른 Observable이 내보낸 항목의 계속 확장하는 버퍼를 유지하여 더 느린 Observable에서 내보낸 항목과 결국 결합해야 합니다. 이로 인해 ReactiveX가 감당하기 힘든 양의 시스템 리소스를 점유할 수 있습니다.

- 빠르게 생성되는 Observable이 느리게 소모되는 관찰자를 만날 때 발생하는 문제를 완화하기 위해 ReactiveX에서 흐름 제어 및 배압을 실행할 수 있는 다양한 전략이 있습니다. 여기에는 일부 ReactiveX 구현에서 반응성 풀 배압 및 일부 배압 관련 연산자.

- 콜드 Observable은 항목의 특정 시퀀스를 방출하지만 관찰자가 편리하다고 생각하면 시퀀스의 무결성을 방해하지 않고 관찰자가 원하는 속도로 이 시퀀스를 방출하기 시작할 수 있습니다. 예를 들어 정적 iterable을 Observable로 변환하면 해당 Observable은 나중에 구독되는 시기나 항목이 관찰되는 빈도에 관계없이 동일한 항목 시퀀스를 방출합니다. 콜드 Observable에서 내보낸 항목의 예에는 데이터베이스 쿼리, 파일 검색 또는 웹 요청의 결과가 포함될 수 있습니다.

- 핫 Observable은 생성되는 즉시 방출할 항목을 생성하기 시작합니다. 구독자는 일반적으로 구독이 설정된 후 Observable이 내보낸 첫 번째 항목부터 시작하여 시퀀스 중간 어딘가에서 핫 Observable이 내보낸 항목 시퀀스를 관찰하기 시작합니다. 그러한 Observable은 자체 속도로 항목을 방출하며, 따라가는 것은 관찰자에게 달려 있습니다. 핫 Observable이 내보내는 항목의 예로는 마우스 및 키보드 이벤트, 시스템 이벤트 또는 주가가 포함될 수 있습니다.

- 콜드 Observable이 멀티캐스트인 경우(연결 가능한 Observable로 변환되고 해당 Connect 메서드가 호출될 때), Hot 옵저버블이 되며 배압 및 흐름 제어 목적으로 핫 Observable로 취급되어야 합니다.

- Cold Observable은 ReactiveX의 일부 구현에 의해 구현된 배압의 반응 풀 모델에 이상적입니다. Hot Observable은 일반적으로 반응적 풀 모델에 잘 대처하지 못하며 이 페이지에 설명된 연산자나 Buffer, Sample, Debounce 또는 Window와 같은 연산자 사용과 같은 다른 흐름 제어 전략의 더 나은 후보입니다.

<br/>

### 흐름 제어
> RxJava가 배압을 구현하기 전에는, 소비자(Observer)를 압도하는 생산자(Observable)를 다루기란 어려운 작업이었다. 너무 많은 이벤트를 밀어내는 생산자를 취급하기 위해 고안한 연산자가 제법 있는데, 그 자체만으로도 무척 흥미롭고, 몇몇은 일괄 처리 이벤트를 다룰 때 유용하다. 나머지는 일반 이벤트를 버린다.

**주기적인 샘플링과 스로틀링**
- 업스트림 Observable에서 밀어낸 모든 단일 이벤트를 받아서 처리해야 하는 경우가 있다.
- 예: 어떤 장치에서 온도와 같은 측정 값을 수신할 때, 장치가 새로운 측정 값을 산출하는 관심 대상이 아닌데, 특히 측정 값 빈도는 빈번하지만 측정 값이 서로 상당히 비슷할 때 더욱 그렇다.
- `sample()` 연산자는 업스트림 Observable을 주기적으로 살펴보다가 마지막으로 발생한 이벤트를 방출한다. 정해진 시간동안 이벤트가 발생하지 않았다면, 다운스트림으로 이송되는 샘플은 없고 다음 간격 사이에 다른 샘플을 채취한다.

```java
long startTime = System.currentTimeMillis();
Observable
    .interval(7, TimeUnit.MILLISECONDS)
    .timestamp()
    .sample(1, TimeUnit.SECOND)
    .map(ts -> ts.getTimestampMillis() - startTime + "ms: " + ts.getValue())
    .take(5)
    .subscribe(System.out::println);

1088ms: 141
2089ms: 284
3090ms: 427
4084ms: 569
5085ms: 712
```

**이벤트를 리스트에 버퍼링하기**
- 버퍼링과 움직이는 윈도우(moving windows)는 RxJava가 제공하는 기본 제공 연산자 중 흥미로운 부류이다. 둘 다 어떤 윈도우(틀)를 통해 연속적인 요소를 포착하며 입력 스트림을 가로질러 진행한다.
- 한편으로는 업스트림 소스의 일괄 처리값을 보다 효율적으로 다룰 수 있다. 실제로는 여러 가지 집계 처리를 즉시 수행할 수 있는 유연하면서도 다재 다능한 도구이다.
- `buffer()` 연산자는 이벤트 묶음을 실시간으로 List에 집계한다. 그러나 `buffer()`는 `toList()` 연산자처럼 모든 이벤트를 하나로 모아놓지 않고 몇 개의 후속 이벤트를 그릅화한 여러 목록을 내보낸다.
- 예제는 `buffer()` 형식 중 가장 간단한 모습으로, 상류 Observable 값을 같은 크기의 목록으로 무리 짓는다.

```java
Observable
    .range(1, 7) // 1, 2, 3, ... 7
    .buffer(3)
    .subscribe((List<Integer> list) -> {
        System.out.println(list);
    });

[1, 2, 3]
[4, 5, 6]
[7]
```
- `buffer()`는 업스트림 이벤트를 계속 수신하면서 버퍼 크기가 3이 될 때까지 내부적으로 버퍼링한다.

**debounce()로 낡은 이벤트 건너뛰기**
- `buffer()`와 `window()` 둘 다 몇 개의 이벤트를 묶어서 일괄적으로 처리한다. `sample()`은 임의의 이벤트를 선택한다. 이 연산자들은 이벤트 사이의 경과 시간을 고려하지 않는다. 그러나 많은 경우, 이벤트를 버리려면 곧이어 다른 이벤트가 뒤따라야 한다.
- 예는 거래 플랫폼에서 유입되는 주가 스트림을 상상해 보자

```java
Observable<BigDecimal> prices = trandingPlatform.pricesOf("NFLX")
Observable<BigDecimal> debounced = prices.debounce(100, MILLISECONDS);
```
- `debounce()(별칭: throttleWithTimeout())`는 특정 이벤트 직후에 뒤따르는 모든 이벤트를 버린다. 즉, 지정된 이벤트 이후 타임 윈도우 안에 다른 이벤트가 나타나지 않으면 해당 이벤트를 방출한다.
- 앞의 예에서 `prices` 스트림은 `NFLX` 주식 가격이 바뀔 때마다 이를 밀어낸다. 주가 정본즌 매우 빈번하게 바뀌어서 때로는 초당 수십 번씩 바뀌기도 한다. 주가가 바뀔 때마다 제법 오래 걸리는 계산을 하고자 한다. 새로운 주가 정보가 도착하면 앞의 계산의 결과는 부적합하여 새로운 주가로 처음부터 다시 계산해야 한다. 따라서 곧바로 새로운 이벤트가 뒤따르면 직전 이벤트를 폐기하고자 한다
- `debounce()`는 나중에 나타날 두 번째 이벤트를 위해 잠시 기다린다. 이 절차를 반복하여 두 번째 이벤트가 첫 번째 이벤트 다음 100ms 이내에 나타난다면, RxJava는 방출을 연기하고 제 3의 이벤트가 나타나기를 기다린다.


## Glide
> Glide는 미디어 디코딩, 메모리 및 디스크 캐싱, 리소스 풀링을 간단하고 사용하기 쉬운 인터페이스로 래핑하는 Android용 빠르고 효율적인 오픈 소스 미디어 관리 및 이미지 로딩 프레임워크입니다.

- Glide는 비디오 스틸, 이미지 및 애니메이션 GIF 가져오기, 디코딩 및 표시를 지원합니다. 
- Glide에는 개발자가 거의 모든 네트워크 스택에 연결할 수 있는 유연한 API가 포함되어 있습니다. 
- 기본적으로 Glide는 사용자 지정 HttpUrlConnection 기반 스택을 사용하지만 대신 Google의 Volley 프로젝트 또는 Square의 OkHttp 라이브러리에 플러그인하는 유틸리티 라이브러리도 포함합니다.
- Glide의 주목할점은 모든 종류의 이미지 목록을 가능한 한 부드럽고 빠르게 스크롤하는 것이지만 Glide는 원격 이미지를 가져오고, 크기를 조정하고, 표시해야 하는 경우에도 효과적입니다.

### [Caching in Glide](https://bumptech.github.io/glide/doc/caching.html#caching-in-glide)
> 기본적으로 Glide는 이미지에 대한 새 요청을 시작하기 전에 여러 계층의 캐시를 확인합니다.

1. Active resources - 이 이미지가 지금 다른 뷰에 표시되고 있습니까?
2. Memory cache - 이 이미지가 최근에 로드되었고 여전히 메모리에 있습니까?
3. Resource - 이 이미지가 이전에 디코딩, 변환 및 디스크 캐시에 기록되었습니까?
4. Data - 이 이미지를 가져온 데이터가 이전에 디스크 캐시에 기록되었습니까?

- 처음 두 단계는 리소스가 메모리에 있는지 확인하고 그렇다면 즉시 이미지를 반환합니다. 
- 두 번째 두 단계는 이미지가 디스크에 있는지 확인하고 신속하게 비동기식으로 반환되는지 확인합니다.
- 4단계 모두 이미지를 찾지 못하면 Glide는 데이터(원본 파일, Uri, Url 등)를 검색하기 위해 원래 소스로 돌아갑니다.

### [Cache Keys](https://bumptech.github.io/glide/doc/caching.html#cache-keys)
> 실제로 1-3단계(활성 리소스, 메모리 캐시, 리소스 디스크 캐시)의 캐시 키에는 다음을 비롯한 여러 다른 데이터 Fragment도 포함됩니다.

1. 너비와 높이
2. 선택적 변환
3. 추가된 옵션
4. 요청된 데이터 유형(Bitmap, GIF 등)

- 활성 리소스 및 메모리 캐시에 사용되는 키도 비트맵 또는 기타 디코딩 시간 전용 매개변수의 구성에 영향을 미치는 것과 같은 메모리 옵션을 수용하기 위해 리소스 디스크 캐시에서 사용되는 키와 약간 다릅니다.
- 디스크에 디스크 캐시 키의 이름을 생성하기 위해 키의 개별 요소를 해시하여 단일 문자열 키를 만든 다음 디스크 캐시에서 파일 이름으로 사용합니다.

### Cache Configuration
> Glide는 로드가 요청별로 Glide의 캐시와 상호 작용하는 방식을 선택할 수 있는 다양한 옵션을 제공합니다.

**Disk Cache Strategies**
- `DiskCacheStrategy`는 `diskCacheStrategy` 메서드와 함께 개별 요청에 적용할 수 있습니다. 사용 가능한 전략을 사용하면 로드에서 디스크 캐시를 사용하거나 쓰는 것을 방지하거나 로드를 지원하는 수정되지 않은 원본 데이터만 캐시하거나 로드에 의해 생성된 변환된 축소판만 캐시하거나 둘 다 캐시하도록 선택할 수 있습니다.
- 기본 전략인 `AUTOMATIC`은 로컬 및 원격 이미지에 대해 최적의 전략을 사용하려고 시도합니다. 
원격 데이터를 다운로드하는 것은 디스크에 이미 있는 데이터의 크기를 조정하는 것보다 비용이 많이 들기 때문에 `AUTOMATIC`은 URL에서와 같이 원격 데이터를 로드할 때 로드를 뒷받침하는 수정되지 않은 데이터만 저장합니다. 로컬 데이터의 경우 두 번째 썸네일 크기 또는 유형을 생성해야 하는 경우 원본 데이터를 검색하는 비용이 저렴하기 때문에 `AUTOMATIC`은 변환된 썸네일만 저장합니다.

```
Glide.with(fragment)
  .load(url)
  .diskCacheStrategy(DiskCacheStrategy.ALL)
  .into(imageView);
```

**Loading only from cache**
- 어떤 경우에는 이미지가 아직 캐시에 없으면 로드가 실패하기를 원할 수 있습니다. 이렇게 하려면 요청별로 `onlyRetrieveFromCache` 메서드를 사용할 수 있습니다.

```
Glide.with(fragment)
  .load(url)
  .onlyRetrieveFromCache(true)
  .into(imageView);
```
- 이미지가 메모리 캐시나 디스크 캐시에서 발견되면 로드됩니다. 그렇지 않고 이 옵션이 true로 설정되면 로드가 실패합니다.

**Skipping the cache**
- 특정 요청이 디스크 캐시나 메모리 캐시 또는 둘 다를 건너뛰도록 하려면 Glide가 몇 가지 대안을 제공합니다. 

- 메모리 캐시만 건너뛰려면 skipMemoryCache()를 사용하십시오.
```
Glide.with(fragment)
  .load(url)
  .skipMemoryCache(true)
  .into(view);
```

- 디스크 캐시만 건너뛰려면 DiskCacheStrategy.NONE을 사용합니다.
```
Glide.with(fragment)
  .load(url)
  .diskCacheStrategy(DiskCacheStrategy.NONE)
  .into(view);
```

- 메모리, 디스크 캐시를 둘다 건너 뛰려면 같이 사용 가능합니다.
```
Glide.with(fragment)
  .load(url)
  .diskCacheStrategy(DiskCacheStrategy.NONE)
  .skipMemoryCache(true)
  .into(view);
```
- 일반적으로 캐시를 건너뛰는 것을 피하려고 합니다. 
- 이미지를 검색, 디코딩 및 변환하여 새 축소판을 만드는 것보다 캐시에서 이미지를 로드하는 것이 훨씬 빠릅니다.


### Memory cache
- 기본적으로 Glide는 LRU 제거와 함께 고정된 양의 메모리를 사용하는 MemoryCache 인터페이스의 기본 구현인 LruResourceCache를 사용합니다. 
- LruResourceCache의 크기는 Glide의 MemorySizeCalculator 클래스에 의해 결정됩니다. 
- 이 클래스는 장치 메모리 클래스, 장치의 램이 낮은지 여부 및 화면 해상도를 확인합니다.
- 응용 프로그램은 MemorySizeCalculator를 구성하여 applyOptions(Context, GlideBuilder) 메서드를 사용하여 AppGlideModule의 MemoryCache 크기를 사용자 지정할 수 있습니다.

**MemorySizeCalculator**
- 일부 상수와 장치 화면 밀도, 너비 및 높이를 기반으로 주어진 장치의 캐시 크기를 지능적으로 결정하려고 시도하는 계산기입니다.

### Bitmap pool
- Glide는 LruBitmapPool을 기본 BitmapPool로 사용합니다. 
- LruBitmapPool은 LRU 축출을 사용하는 메모리 BitmapPool의 고정 크기입니다. 
- 기본 크기는 해당 장치의 화면 크기와 밀도, 메모리 클래스 및 isLowRamDevice의 반환 값을 기반으로 합니다. 
- 특정 계산은 Glide의 MemoryCache에 대해 크기가 결정되는 방식과 유사하게 Glide의 MemorySizeCalculator에 의해 수행됩니다.
- 응용 프로그램은 MemorySizeCalculator를 구성하여 applyOptions(Context, GlideBuilder) 메서드로 AppGlideModule의 BitmapPool 크기를 사용자 지정할 수 있습니다.

**BitmapPool**
- 사용자가 Bitmap 개체를 재사용할 수 있도록 하는 풀용 인터페이스입니다.

**LruBitmapPool**
- LruPoolStrategy를 사용하여 Bitmaps를 버킷화한 다음 LRU 제거 정책을 사용하여 풀을 주어진 최대 크기 제한 아래로 유지하기 위해 가장 최근에 사용된 버킷에서 Bitmaps를 제거하는 BitmapPool 구현입니다.

### Disk Cache
- Glide는 DiskLruCacheWrapper를 기본 DiskCache로 사용합니다.
- DiskLruCacheWrapper는 LRU 제거 기능이 있는 고정 크기 디스크 캐시입니다. 
- 기본 디스크 캐시 크기는 250MB이며 응용 프로그램 캐시 폴더의 특정 디렉터리에 있습니다.

### [Log level](https://bumptech.github.io/glide/javadocs/420/com/bumptech/glide/GlideBuilder.html#setLogLevel-int-)
- 요청이 실패할 때 기록되는 행을 포함하여 형식이 잘 지정된 로그의 하위 집합의 경우 Android의 Log 클래스 값 중 하나와 함께 setLogLevel을 사용할 수 있습니다. 

### 이미지 로드 과정
1. User -> Key 를 사용해서 데이터 요청 -> 메모리 캐시에 조회 -> 데이터 반환, 존재하지 않을 경우 2번으로 이동
2. 디스크 캐시 조회 -> 메모리 캐시에 데이터 저장 -> 데이터 반환, 존재하지 않을 경우 3번 이동
3. Network 에서 데이터를 받아옴 -> 디스크 캐시 저장 -> 메모리 캐시 저장 -> 데이터 반환

### 캐시 무효화
- 한번 캐시된 데이터를 같은 URL 에서 불러올때 이미지는 변경이 되었지만 Glide에 업로드가 되지 않는 경우가 있는데, 이는 캐시에 남아있는 데이터가 먼저 호출되기 때문이다.
- 디스크의 캐시들은 해싱된 키 값이기 때문에 이들을 모두 추적해서 캐시를 삭제하는 방법은 쉽지 않다.
- 원하는 이미지를 로드하려면 콘텐츠가 변경될 때 이를 식별하는 식별자를 매번 다르게 하여 캐시가 저장하지 않은 데이터라고 인식하게 하는 것이다.

```
signiture(objectkey("metaData"))
```
- 해당 메서드를 사용해서 해시키 값을 변경 가능
- `metaData` 에는 수정된 날짜, mime type, mediaStore item 등 다양한 데이터를 넣어주면 된다.

### Glide Custom Key 로 Cache 하기
- dynamic url 을 사용해서 매번 URL 이 변경되면 매번 다른 이미지로 인식해서 저장소 사용량이 빠르게 증가한다.
- 이때 `GlideUrl` 을 사용해서 Custom Key 를 구현 가능하다

```java
import androidx.annotation.NonNull; 
import com.bumptech.glide.load.model.GlideUrl; 
import com.bumptech.glide.util.Preconditions; 

public class GlideUrlWithCacheKey extends GlideUrl { 
    private String url; 
    private String cacheKey; 
    
    public GlideUrlWithCacheKey(String url, String cacheKey) { 
        super(url); 

        Preconditions.checkNotNull(url); 
        Preconditions.checkNotEmpty(url); 
        Preconditions.checkNotNull(cacheKey); 
        Preconditions.checkNotEmpty(cacheKey); 

        this.url = url; 
        this.cacheKey = cacheKey; 
    } 
        
    @Override 
    public String getCacheKey() { 
        return cacheKey; 
    } 
    
    @NonNull 
    @Override 
    public String toString() { 
        return url; 
    } 
}

// 사용
Glide.with(activity).load(new GlideUrlWithCacheKey(url, key))
```
- 개발자가 임의로 해당 이미지에 맞는 캐시 키를 구현해서 사용할 수 있다.

### [Resource Management](https://bumptech.github.io/glide/doc/caching.html#resource-management)

- Glide의 디스크 및 메모리 캐시는 LRU입니다. 즉, 한계에 도달하거나 한계 근처에서 지속적으로 사용할 한계에 도달할 때까지 점점 더 많은 메모리 및/또는 디스크 공간을 차지합니다. 유연성을 추가하기 위해 Glide는 응용 프로그램에서 사용하는 리소스를 관리할 수 있는 몇 가지 추가 방법을 제공합니다.
- 더 큰 메모리 캐시, 비트맵 풀 및 디스크 캐시는 일반적으로 적어도 어느 정도는 더 나은 성능을 제공합니다. 캐시 크기를 변경하는 경우 성능/크기 균형이 합당한지 확인하기 위해 변경 전후의 성능을 주의 깊게 측정해야 합니다.

**Memory Cache**
- 기본적으로 Glide의 메모리 캐시와 BitmapPool은 ComponentCallbacks2에 응답하고 프레임워크가 제공하는 수준에 따라 다양한 정도로 콘텐츠를 자동으로 축출합니다. 결과적으로 일반적으로 캐시 또는 BitmapPool을 동적으로 모니터링하거나 지울 필요가 없습니다. 그러나 필요한 경우 Glide는 몇 가지 수동 옵션을 제공합니다.

- 영구적인 크기 변경
    - 응용 프로그램에서 Glide에 사용할 수 있는 RAM의 양을 변경하려면 [구성 페이지를 참조](https://bumptech.github.io/glide/doc/configuration.html#memory-cache)하십시오.

- 일시적인 크기 변경
    - 일시적으로 Glide가 앱의 특정 부분에서 더 많거나 적은 메모리를 사용하도록 허용하려면 `setMemoryCategory`를 사용할 수 있습니다.

    ```java
    Glide.get(context).setMemoryCategory(MemoryCategory.LOW);
    // Or:
    Glide.get(context).setMemoryCategory(MemoryCategory.HIGH);
    ```
    
    - 앱의 메모리 또는 성능에 민감한 영역을 떠날 때 메모리 범주를 다시 재설정해야 합니다.

    ```java
    Glide.get(context).setMemoryCategory(MemoryCategory.NORMAL);
    ```
- 메모리 지우기
    - Glide의 메모리 캐시 및 BitmapPool을 간단히 지우려면 clearMemory를 사용하십시오.

    ```java
    // This method must be called on the main thread.
    Glide.get(context).clearMemory();
    ```
    - 모든 메모리를 지우는 것은 특히 효율적이지 않으며 버벅거림과 로딩 시간 증가를 피하기 위해 가능한 한 피해야 합니다.


**Disk Cache**
> Glide는 런타임에 디스크 캐시 크기에 대해 제한된 제어만 제공하지만 크기와 구성은 AppGlideModule에서 변경할 수 있습니다.

- 영구적인 크기 변경
    - 응용 프로그램 전체에서 `Glide`의 디스크 캐시에 사용할 수 있는 `sdcard` 공간의 양을 변경하려면 [구성 페이지를 참조](https://bumptech.github.io/glide/doc/configuration.html#disk-cache)하십시오.

- 디스크 캐시 지우기
    - 디스크 캐시의 모든 항목을 지우려면 `clearDiskCache`를 사용할 수 있습니다.

    ```java
    new AsyncTask<Void, Void, Void> {
       @Override
        protected Void doInBackground(Void... params) {
            // This method must be called on a background thread.
            Glide.get(applicationContext).clearDiskCache();
            return null;
        }
    }
    ```
