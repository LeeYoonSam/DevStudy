# Long-running Operations

- [ANR 이 무엇이며 어떻게 방지할 수 있는지?](#anr-이-무엇이며-어떻게-방지할-수-있는지)
- [AsyncTask 의 생명 주기와 액티비티의 관계 및 발생할수 있는 문제](#asynctask-의-생명-주기와-액티비티의-관계-및-발생할수-있는-문제)
- [Looper, Handler and HandlerThread](#looper-handler-and-handlerthread)
- [RxJava, Coroutine 비교](#rxjava-coroutine-비교)

--- 

## [ANR 이 무엇이며 어떻게 방지할 수 있는지?](https://developer.android.com/topic/performance/vitals/anr)
- Android 앱의 UI 스레드가 너무 오랫동안 차단되면 'ANR(애플리케이션 응답 없음)' 오류가 트리거 된다.
- 앱이 포그라운드에 있으면 시스템에서 사용자에게 대화상자를 표시
- ANR 은 UI 업데이트를 담당하는 앱의 기본 스레드가 사용자 입력 이벤트 또는 그림을 처리하지 못하여 사용자 불만을 초래

### 발생조건
- 액티비티가 포그라운드에 있는 동안 앱이 입력 이벤트 또는 `BroadcastReceiver`에 5초 이내에 응답하지 않을때
- 포그라운드 액티비티가 없을 때 `BroadcastReceiver`가 상당한 시간 내에 실행을 완료하지 못했을때

### 문제 감지 및 진단
- 앱을 이미 게시한 경우 Android vitals 에서 개발자에게 문제가 발생했다고 알릴수 있다.

**Android vitals**
- 앱에서 ANR 이 과도하게 발생하는 경우 PlayConsole 을 통해 알림을 보냄으로써 앱의 성능을 개선할 수 있다.
- ANR 이 과도하다고 간주하는 조건
    - 일일 세션의 0.47% 이상에서 한 번 이상의 ANR이 나타난다.
    - 일일 세션의 0.24% 이상에서 두 번 이상의 ANR이 나타난다.

### ANR 진단
ANR 을 진단할 때 다음과 같은 일반 패턴을 찾아야 한다.
1. 앱이 기본 스레드에서 I/O 관련된 느린 작업을 실행 중이다.
2. 앱이 기본 스레드에서 긴 계산을 실행 중이다.
3. 기본 스레드에서 다른 프로세스에 관한 동기 바인더 호출을 실행 중이고 다른 프로세스가 반환하는 데 오랜 시간이 걸린다.
4. 다른 스레드에서 발생하는 긴 작업을 위해 동기화된 블록을 대기하는 동안 기본 스레드가 차단되었다.
5. 기본 스레드가 프로세스에서 또는 바인더 호출을 통해 다른 스레드와 교착 상태에 있고, 기본 스레드가 긴 작업이 완료될 때까지 대기하는 것만이 아니라 교착 상태에 있다.

### ANR 을 유발하는 원인을 파악하는 기법
**엄격 모드**
- `StrictMode`를 사용하면 앱을 개발하는 동안 기본 스레드에서 실수로 인한 I/O 작업을 찾을 수 있다.
- 애플리케이션 또는 액티비티 수준에서 `StrictMode`를 사용할 수 있다.

**백그라운드 ANR 대화상자 사용 설정**
- 개발자 옵션에서 모든 ANR 표시가 사용 설정된 경우에만 브로드캐스트 메시지를 처리하는 데 너무 오래 걸리는 앱에 관한 ANR 대화상자를 표시
- 백그라운드 ANR 대화상자가 항상 사용자에게 표시되지는 않지만 여전히 앱에 성능 문제가 발생할 수 있다.

**Traceview**
- Traceview를 사용하여 사용 사례를 진행하는 동안 실행중인 앱의 트레이스를 가져오고 기본 스레드를 사용중인 위치를 식별할 수 있다.

## AsyncTask 의 생명 주기와 액티비티의 관계 및 발생할수 있는 문제
- AsyncTask는 액티비티의 생명 주기에 연결되지 않습니다. 예를 들어, Activity 내에서 AsyncTask를 시작하고 사용자가 장치를 회전하면 Activity는 소멸되지만(새 Activity 인스턴스가 생성됨) AsyncTask는 죽지 않고 대신 완료될 때까지 계속 살아 있습니다.
- 그런 다음 AsyncTask가 완료되면 새 Activity의 UI를 업데이트하는 대신 Activity의 이전 인스턴스(즉, 생성되었지만 더 이상 표시되지 않는 인스턴스)를 업데이트합니다. 이로 인해 예외가 발생할 수 있습니다(예: 활동 내에서 보기를 검색하기 위해 findViewById를 사용하는 경우 java.lang.IllegalArgumentException: 보기가 창 관리자에 첨부되지 않음).
- AsyncTask가 Activity에 대한 참조를 유지하기 때문에 이로 인해 메모리 누수가 발생할 가능성도 있습니다. 이는 AsyncTask가 살아 있는 한 Activity가 가비지 수집되는 것을 방지합니다.
- 장기 실행 백그라운드 작업에 AsyncTasks를 사용하는 것은 일반적으로 좋지 않습니다. 오히려 장기 실행 백그라운드 작업의 경우 다른 메커니즘(예: 서비스)을 사용해야 합니다.
- AsyncTask는 기본적으로 직렬 실행기를 사용하여 단일 스레드에서 실행됩니다. 즉, 스레드가 하나만 있고 각 작업이 차례로 실행됩니다.

## [Looper, Handler and HandlerThread](https://blog.mindorks.com/android-core-looper-handler-and-handlerthread-bd54d69fe91a)
- Looper, Handler 및 HandlerThread는 비동기 프로그래밍의 문제를 해결하는 Android 에서 사용하는 방법이다.
- 복잡한 Android 프레임워크가 구축된 깔끔한 구조다.

### 사용 사례
1. Android의 메인 스레드는 Looper와 Handler로 빌드됩니다. 따라서 차단되지 않은 반응형 UI를 만들기 위해서는 이에 대한 이해가 필수적입니다.
2. 라이브러리를 작성하는 개발자는 라이브러리 크기 때문에 타사 라이브러리를 사용할 여유가 없습니다. 따라서 그들에게 가장 좋은 방법은 기존의 가용 자원을 활용하는 것입니다. 자체 솔루션을 작성하는 것이 항상 그 수준의 효율성과 최적화를 얻을 수 있는 것은 아닙니다.
3. SDK를 출하하는 기업/개인도 같은 주장을 할 수 있습니다. 클라이언트는 다양한 구현을 가질 수 있지만 모든 클라이언트는 공통 Android 프레임워크 API를 공유합니다.
4. 그것들을 완전히 이해하면 일반적으로 Android SDK 및 패키지 클래스를 따를 수 있는 능력이 향상됩니다.

### Java 스레드의 문제점은?
- Java 스레드는 일회성이며 실행 메소드를 실행한 후 종료된다.

### 개선할수 있는지?
- 스레드는 양날의 검입니다. 
- 실행 스레드 간에 작업을 분산하여 실행 속도를 높일 수 있지만 스레드가 초과되면 속도를 늦출 수도 있습니다. 
- 스레드 생성 자체가 오버헤드입니다. 
- 최상의 옵션은 최적의 스레드 수를 갖고 작업 실행에 재사용하는 것입니다.

### 스레드 재사용을 위한 모델
1. 스레드는 run() 메서드를 통해 루프에서 활성 상태로 유지됩니다. 
2. 작업은 해당 스레드에 의해 순차적으로 실행되고 대기열(MessageQueue)에서 유지됩니다. 
3. 스레드는 완료되면 종료되어야 합니다.

### Android 에서 재사용하는 방법은?
- Android의 메인 스레드는 Looper, Handler, HandlerThread를 통해 구현됩니다.

1. `MessageQueue`는 처리해야 하는 메시지라는 작업이 있는 대기열입니다.
2. `Handler`는 Looper를 사용하여 MessageQueue에 작업을 대기열에 추가하고 작업이 MessageQueue에서 나올 때 실행합니다.
3. `Looper`는 스레드를 활성 상태로 유지하고 MessageQueue를 순환하며 처리할 해당 핸들러에 메시지를 보내는 작업자입니다.
4. 마지막으로 `Thread`는 Looper의 `quit()` 메서드를 호출하여 종료됩니다.

- 하나의 스레드는 하나의 고유한 루퍼만 가질 수 있으며 연관된 많은 고유한 핸들러를 가질 수 있습니다.

### 스레드에 대한 루퍼 및 MessageQueue 생성
- 스레드는 실행 후 Looper.prepare()를 호출하여 Looper와 MessageQueue를 가져옵니다. 
- Looper.prepare()는 호출 스레드를 식별하고 Looper 및 MessageQueue 객체를 생성하고 스레드를 ThreadLocal 스토리지 클래스에 연결합니다. 
- 관련 루퍼를 시작하려면 Looper.loop()를 호출해야 합니다. 
- 유사하게, 루퍼는 looper.quit()를 통해 명시적으로 종료되어야 합니다.

```java
class LooperThread extends Thread {
      public Handler mHandler; 

      public void run() { 
          Looper.prepare();

          mHandler = new Handler() { 
              public void handleMessage(Message msg) { 
                 // process incoming messages here
                 // this will run in non-ui/background thread
              } 
          }; 

          Looper.loop();
      } 
  }
```

### 스레드에 대한 핸들러 생성
`Handler`는 스레드의 루퍼를 통해 인스턴스화하는 스레드와 암시적으로 연결되지만 핸들러의 생성자에서 스레드의 루퍼를 전달하여 스레드에 명시적으로 연결할 수 있습니다.

```java
handler = new Handler() {
@Override
public void handleMessage(Message msg) {
        // process incoming messages here
        // this will run in the thread, which instantiates it
    }
};
```

- Handler를 통해 MessageQueue에 메시지를 보내는 것은 두 가지 모드로 수행할 수 있습니다.

1. `Message` : 메시지 데이터를 처리하기 위한 다양한 유용한 메소드를 정의하는 클래스입니다. 객체를 보내기 위해 obj 변수를 설정합니다.

```java
Message msg = new Message();
msg.obj = "Ali send message";
handler.sendMessage(msg);
```

2. `Runnable`: 실행 파일을 MessageQueue에 게시할 수도 있습니다. 예: 메인 스레드에서 작업 게시 및 실행.

```java
new Handler(Looper.getMainLooper()).post(new Runnable() {
@Override
public void run() {
        // this will run in the main thread
    }
});
```

- Android는 프로세스를 간소화하기 위해 HandlerThread(Thread의 하위 클래스)를 제공했습니다. 내부적으로는 우리가 했던 것과 동일한 일을 하지만 강력한 방식으로 수행합니다. 따라서 항상 HandlerThread를 사용하십시오.

**HandlerThread를 생성하는 방법 중 하나는 그것을 서브클래스화하는 것이며 대부분의 경우 이 방법을 사용할 것입니다.**

```java
private class MyHandlerThread extends HandlerThread {

    Handler handler;

    public MyHandlerThread(String name) {
        super(name);
    }

    @Override
    protected void onLooperPrepared() {
        handler = new Handler(getLooper()) {
            @Override
            public void handleMessage(Message msg) {
                // process incoming messages here
                // this will run in non-ui/background thread
            }
        };
    }
}
```

### 참고
- [Android project uses Looper, Handler, and HandlerThread to simulate a Post Office.](https://github.com/MindorksOpenSource/post-office-simulator-looper-example)


## RxJava, Coroutine 비교

### 안드로이드에 비동기 프로그래밍이 필요한 이유?
- 새로고침 빈도
    - 안드로이드에서 최소로 보장하는 Refresh frequency -> 최소 60Hz(16ms), 90Hz(11ms)~120Hz(8ms)
    - 1초에 60회 새로고침, 1회 새로고침하는데 16ms -> 사용자들이 볼수있는 화면을 그리는데 16ms 가 걸림
- 화면을 그리기 위해서 앱에서 하는 작업
    - xml 코드 파싱
    - xml 코드 inflate
    - 화면을 그림
    - 위 작업들과 동시에 사용자들이 할수 있는 interaction 을 Listening

- 안드로이드에서는 이와 같은 작업을 `Main Thread` 에서 진행
- 화면에 그리는 작업 외에 데이터를 가져오는 작업 등 모든 작업을 메인 스레드에서 하게 되면 16ms 안에 작업을 끝내지 못하면 지연이되어 결국 ANR 을 발생 시키는 원인이 된다.
- 이러한 문제를 해결하기 위해 `Worker Thread` 가 존재
- 하나의 메인 스레드, 다수의 워크 스레드 형태, 스레드가 하나 이상이라서 멀티쓰레딩 문제 발생 (Context Switching 비용 발생, 데드락 문제 발생) 
- 멀티쓰레딩 문제를 해결하기 위해 나온 효과적이고 최신기술 `RxJava`, `Coroutine` 

### RxJava
> ReactiveX는 관찰 가능한 시퀀스를 사용하여 비동기 및 이벤트 기반 프로그램을 구성하기 위한 라이브러리입니다.
데이터/이벤트의 시퀀스를 지원하도록 관찰자 패턴을 확장하고 낮은 수준의 스레딩, 동기화, 스레드 안전성 및 동시 데이터 구조와 같은 문제를 추상화하면서 선언적으로 시퀀스를 함께 구성할 수 있는 연산자를 추가합니다.

**특징**
- 다양한 오퍼레이터가 존재하고 사용법에 맞게 사용해야 함
- 러닝커브는 크지만 익숙하면 쉽게 변경이 가능
- 로직이 변경될수 있는 상황에서 유연하게 대처할 수 있다. 일반적으로 여러줄로 처리하는 작업을 한줄로 처리가 가능할 수도 있다.
- 데이터 소비, 에러 처리를 할때 스트림 구조의 오퍼레이터 안에서 쉽게 처리가 가능하다.

**주의할점**
- Subscribe 는 Disposable 을 반환하게 되어있고, 메모리에서 해제를 하기 위해 dispose 를 해줘야 한다.

### Coroutine
> 코루틴은 비동기적으로 실행되는 코드를 간소화하기 위해 Android에서 사용할 수 있는 동시 실행 설계 패턴입니다. 코루틴은 Kotlin 버전 1.3에 추가되었으며 다른 언어에서 확립된 개념을 기반으로 합니다.
Android에서 코루틴은 기본 스레드를 차단하여 앱이 응답하지 않게 만들 수도 있는 장기 실행 작업을 관리하는 데 도움이 됩니다.

**특징**
- 경량: 코루틴을 실행 중인 스레드를 차단하지 않는 정지를 지원하므로 단일 스레드에서 많은 코루틴을 실행할 수 있습니다. 정지는 많은 동시 작업을 지원하면서도 차단보다 메모리를 절약합니다.
- 메모리 누수 감소: 구조화된 동시 실행을 사용하여 범위 내에서 작업을 실행합니다.
기본으로 제공되는 취소 지원: 실행 중인 코루틴 계층 구조를 통해 자동으로 취소가 전달됩니다.
- Jetpack 통합: 많은 Jetpack 라이브러리에 코루틴을 완전히 지원하는 확장 프로그램이 포함되어 있습니다. 일부 라이브러리는 구조화된 동시 실행에 사용할 수 있는 자체 코루틴 범위도 제공합니다.
- RxJava 처럼 스트림이나 오퍼레이터를 사용하지 않아서 소스코드를 이해하기가 조금 더 쉽다.
- 순차적인 코드 구조로 되어있어서 버그 수정 및 요구사항에 유연하게 대처가 가능하다.
- RxJava 에 비해 에러 처리가 유연하지 않다. try catch 로 구현