# Android

## [Framework](./Framework/README.md)

## [Android UI](./UI/README.md)

## [JetpackLibrary](./JetpackLibrary/README.md)

## [BusinessLogic](./BusinessLogic/README.md)

## [Core Android](./CoreAndroid/README.md)

## [Architecture](./Architecture)
- [Architecture 란?](./Architecture#architecture-란)
- [아키텍처 패턴](./Architecture#아키텍처-패턴)
- [MVVM Architecture](./Architecture#mvvm-architecture)
- [MVP Architecture](./Architecture#mvp-architecture)
- [MVC Architecture](./Architecture#mvc-architecture)
- [MVI Architecture](./Architecture#mvi-architecture)
- [Clean Architecture](./Architecture#clean-architecture)
- [모바일 클린 아키텍처](./Architecture#모바일-클린-아키텍처)
- [Clean Code](./Architecture#clean-code)

## [Library](./Library)
- [Dagger Hilt Koin 비교](./Library/README.md#dagger-hilt-koin-비교)
- [RxJava](./Library/README.md#rxjava)
- [RxJava Hot, Cold Observable](./Library/README.md#rxjava-hot-cold-observable)
- [RxJava Backpressure(배압)](./Library/README.md#rxjava-backpressure배압)
- [Glide](./Library/README.md#glide)

---

## 용어 정리

1.  [**AOT (Ahead-of-Time) 컴파일**](./Framework/ART-Dalvik-Dex/README.md#2-aot-ahead-of-time-컴파일---art-방식)
    AOT(사전 컴파일)는 코드가 런타임 이전에 머신 코드로 컴파일되는 과정으로, 실행 중 JIT(Just-in-Time) 컴파일의 필요성을 제거합니다. 이 접근 방식은 최적화된, 미리 컴파일된 바이너리를 생성하여 성능을 향상시키고 런타임 오버헤드를 줄입니다.

2.  [**JIT (Just-in-Time) 컴파일**](./Framework/ART-Dalvik-Dex/README.md#1-jit-just-in-time-컴파일---달빅dalvik-방식)
    JIT(실시간 컴파일)는 바이트코드가 실행 직전에 동적으로 머신 코드로 변환되는 런타임 과정입니다. 이를 통해 런타임 환경은 실제 실행 패턴을 기반으로 코드를 최적화하여, 자주 사용되는 코드 경로의 성능을 향상시킬 수 있습니다.

3.  [**프로세스 간 통신 (IPC, Inter-process communication)**](./Framework/SerializableParcelable/README.md)
    프로세스 간 통신(IPC)은 서로 다른 프로세스가 서로 통신하고 데이터를 공유할 수 있게 하는 메커니즘으로, 별개의 애플리케이션이나 시스템 서비스 간의 협업을 가능하게 합니다. 안드로이드에서는 `Binder`, 인텐트(`Intents`), 콘텐츠 제공자(`ContentProviders`), 메신저(`Messenger`)와 같은 컴포넌트를 통해 IPC가 이루어지며, 이는 프로세스 간에 데이터를 안전하고 효율적으로 교환하도록 돕습니다.

4.  [**마샬링 (Marshaling)**](./Framework/SerializableParcelable/README.md#-프로-팁-parcel-and-parcelable)
    마샬링은 객체나 데이터 구조를 네트워크를 통해 전송하거나 저장한 후 나중에 재구성할 수 있는 형식으로 변환하는 과정입니다. 안드로이드에서는 주로 프로세스 간 통신(IPC)에서 사용되며, 이때 데이터는 `Binder`와 같은 메커니즘을 통해 전송되기 위해 직렬화(serialized)됩니다.

5.  [**언마샬링 (Unmarshaling)**](./Framework/SerializableParcelable/README.md#-프로-팁-parcel-and-parcelable)
    언마샬링은 직렬화된 형식의 데이터나 객체를 다시 원래 형태로 재구성하는 과정입니다. 안드로이드에서는 종종 프로세스 간 통신(IPC)에서 발생하며, `Binder`와 같은 메커니즘을 통해 전송된 데이터가 수신 프로세스에서 사용되기 위해 역직렬화(deserialized)됩니다.

6.  **IBinder**
    `IBinder`는 프로세스 간 통신(IPC)을 위한 안드로이드의 핵심 인터페이스입니다. 이는 클라이언트와 서비스 같은 서로 다른 컴포넌트 간의 저수준(low-level) 통신 다리 역할을 하여, 이들이 데이터를 교환하거나 원격으로 메서드를 호출하며 상호작용할 수 있도록 합니다.

7.  **어노테이션 처리 (Annotation processing)**
    어노테이션 처리는 자바와 코틀린의 컴파일 시점 메커니즘으로, 어노테이션을 사용하여 APT(Annotation Processing Tool)나 KAPT(Kotlin Annotation Processing Tool)와 같은 도구를 통해 추가 코드를 생성하거나 기존 코드를 검증하는 데 사용됩니다.

8.  **콜드 플로우 (Cold flow)**
    콜드 플로우는 수집(collected)되기 전까지 비활성 상태로 유지되는 플로우입니다. 실행 로직은 구독자(collector)가 구독을 시작할 때만 시작되어, 활성 구독자가 없으면 불필요한 작업이 수행되지 않도록 보장합니다.

9.  **핫 플로우 (Hot flow)**
    핫 플로우는 활성 구독자의 존재 여부와 관계없이 적극적으로 값을 방출(emit)하는 플로우입니다. 상태를 유지하며 계속해서 데이터를 생성하므로, `StateFlow`나 `SharedFlow`와 같이 공유되고 지속적으로 실행되는 데이터 스트림에 적합합니다.