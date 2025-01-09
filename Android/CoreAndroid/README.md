# Core Android

- [Asynchronous IO](#asynchronous-io)
- [ViewBinding 과 DataBinding 차이점](#viewbinding-과-databinding-차이점)
- [ViewModel 과 SharedViewModel 의 차이와 구현 방법](#viewmodel-과-sharedviewmodel-의-차이와-구현-방법)
- [Android Jetpack이란](#android-jetpack이란)
- [Android 애플리케이션의 성능을 최적화하기 위한 전략](#android-애플리케이션의-성능을-최적화하기-위한-전략)
- [안드로이드에서 비동기 처리가 필요한 이유에 및 Rxjava, Coroutine 의 특징과 차이점, 장단점](#안드로이드에서-비동기-처리가-필요한-이유에-및-rxjava-coroutine-의-특징과-차이점-장단점)
- [안드로이드에서 의존성 주입이란?](#안드로이드에서-의존성-주입이란)
- [Android에서 Parcelable 인터페이스의 목적은 무엇이며 Serializable 사용성 차이](#android에서-parcelable-인터페이스의-목적은-무엇이며-serializable-사용성-차이)
- [Android의 콘텐츠 제공자는 무엇이며 주요 목적](#android의-콘텐츠-제공자는-무엇이며-주요-목적은-무엇인가요-콘텐츠-제공자가-유용할-수-있는-예시-시나리오를-제공)
- [안드로이드 어플리케이션 컴포넌트](#안드로이드-어플리케이션-컴포넌트)
- [매니페스트 파일](#매니페스트-파일)
- [안드로이드 어플리케이션의 프로젝트 구조](#안드로이드-어플리케이션의-프로젝트-구조)
- [Android Context](#android-context)
- [Android Intent](#android-intent)
- [Intent Filter 활용 방법](#intent-filter-활용-방법)
- [PendingIntent란 무엇일까요?](#pendingintent란-무엇일까요)
- [Proguard란 무엇이며, 왜 사용할까요?](#proguard란-무엇이며-왜-사용할까요)
- [Navigation Component](#navigation-component)
- [Lifecycle](#lifecycle)
- [LifecycleOwner](#lifecycleowner)
- [LiveData vs ObservableField](#livedata-vs-observablefield)

---

## Asynchronous IO

비동기 입/출력(IO)은 메인 스레드의 실행을 차단하지 않고 IO 작업을 수행할 수 있는 프로그래밍 모델입니다.

- 파일에서 데이터를 읽거나 네트워크 요청을 하는 것과 같은 IO 작업은 완료하는 데 오랜 시간이 걸릴 수 있으며 작업이 완료되기를 기다리는 동안 기본 스레드를 차단하면 애플리케이션이 응답하지 않을 수 있습니다.

- 비동기 IO를 사용하면 IO 작업이 별도의 스레드 또는 백그라운드에서 실행되므로 기본 스레드가 IO 작업이 완료될 때까지 기다리지 않고 다른 작업을 계속 실행할 수 있습니다. IO 작업이 완료되면 결과는 콜백 함수 또는 Promise 를 통해 기본 스레드로 다시 전달됩니다.

- Android에서 비동기 IO는 일반적으로 네트워크 요청 및 데이터베이스 액세스에 사용됩니다. 이러한 작업을 완료하는 데 시간이 오래 걸리고 기본 스레드를 차단해서는 안 되기 때문입니다. 

- 비동기 IO는 스레드, 핸들러, 코루틴, RxJava와 같은 반응형 프로그래밍 라이브러리를 비롯한 여러 접근 방식을 사용하여 구현할 수 있습니다.

---

## ViewBinding 과 DataBinding 차이점

뷰 바인딩과 데이터 바인딩은 안드로이드에서 뷰를 데이터에 바인딩하는 두 가지 기술입니다.

### 뷰 바인딩 (View Binding)
- 목적: XML 레이아웃 파일에서 정의된 뷰를 코드에서 간편하게 참조하기 위한 방법입니다.
- 특징:
    - findViewById()를 대체하여 뷰를 찾는 과정을 간소화합니다.
    - null safety를 제공하여 NullPointerException 발생 가능성을 줄입니다.
    - 타입 안전성을 제공하여 형 변환 오류를 방지합니다.
    - 양방향 데이터 바인딩은 지원하지 않습니다.
- 사용법:
    - Gradle 설정 후, 각 레이아웃 파일마다 자동 생성되는 바인딩 클래스를 통해 뷰에 접근합니다.

### 데이터 바인딩 (Data Binding)
- 목적: 데이터와 UI를 연결하여 데이터 변경 시 UI가 자동으로 업데이트되도록 하는 방법입니다.
- 특징:
    - 뷰 바인딩의 기능을 포함하고 있으며, 추가적으로 데이터 바인딩 기능을 제공합니다.
    - 양방향 데이터 바인딩을 지원하여 데이터 변경 시 UI가 자동으로 반영됩니다.
    - 표현식을 사용하여 복잡한 로직을 처리할 수 있습니다.
    - 뷰 모델과 함께 사용하여 MVVM 패턴을 구현하기 좋습니다.
- 사용법:
    - layout 태그로 감싼 레이아웃에서 데이터를 바인딩하고, 바인딩 클래스를 통해 데이터를 설정합니다.

**뷰 바인딩 vs 데이터 바인딩 비교표**
| 기능 | 뷰 바인딩 | 데이터 바인딩 |
| --- | --- | --- |
| 목적 | 뷰 참조 | 데이터와 UI 연결 |
| 데이터 바인딩 | 지원하지 않음	| 지원 |
| 양방향 바인딩 | 지원하지 않음	| 지원 |
| 표현식 사용 |	지원하지 않음 | 지원 |
| 복잡성 | 간단	| 상대적으로 복잡 |
| 성능 | 빠름 | 상대적으로 느림 |

**어떤 것을 사용해야 할까요?**
- 간단한 뷰 참조: 뷰 바인딩
- 데이터와 UI를 연결하고 양방향 바인딩이 필요한 경우: 데이터 바인딩
- MVVM 패턴을 적용하고 싶은 경우: 데이터 바인딩

**결론**
뷰 바인딩과 데이터 바인딩은 각각의 장단점을 가지고 있으며, 개발 상황에 맞게 적절한 방법을 선택하는 것이 중요합니다. 일반적으로 간단한 뷰 참조에는 뷰 바인딩을, 데이터와 UI를 긴밀하게 연결하고 복잡한 로직을 처리해야 하는 경우에는 데이터 바인딩을 사용하는 것이 좋습니다.

---

## ViewModel 과 SharedViewModel 의 차이와 구현 방법

### ViewModel과 SharedViewModel 개요
`ViewModel`은 Android Jetpack의 구성 요소로, UI와 데이터를 분리하여 관리하고, 화면 회전과 같은 구성 변경 시 데이터 생존을 보장하는 데 사용됩니다. 즉, UI 컨트롤러(Activity, Fragment)의 생명주기에 의존하지 않고 데이터를 유지하여, 더욱 안정적인 앱을 개발할 수 있도록 돕습니다.

`SharedViewModel`은 ViewModel의 특수한 유형으로, 같은 Activity 내의 여러 Fragment에서 공유되는 데이터를 관리하기 위한 목적으로 사용됩니다. 즉, 여러 Fragment에서 동일한 ViewModel 인스턴스에 접근하여 데이터를 공유하고 업데이트할 수 있습니다.

`ViewModel과 SharedViewModel의 차이점`

| 특징 | ViewModel | SharedViewModel |
| --- | --- | --- |
| 범위 | 각 화면(Activity, Fragment) | 같은 Activity 내의 여러 Fragment |
| 데이터 공유 | 각각의 ViewModel이 독립적인 데이터를 관리 | 여러 Fragment에서 동일한 데이터를 공유 |
| 사용 시기 | 대부분의 화면에서 사용 | 여러 Fragment 간 데이터를 공유해야 할 때 |
| 구현 | ViewModel 클래스 상속 | ViewModel 클래스 상속 |

`ViewModel 구현 방법`

```kotlin
class MyViewModel : ViewModel() {
    val count = MutableLiveData<Int>()

    init {
        count.value = 0
    }

    fun increment() {
        count.value = count.value?.plus(1)
    }
}
```
- LiveData: 데이터의 변화를 관찰하고 UI를 업데이트하는 데 사용됩니다.
- ViewModelProvider: ViewModel 인스턴스를 얻기 위해 사용됩니다.

```kotlin
class MyActivity : AppCompatActivity() {
    private lateinit var viewModel: MyViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // ...

        viewModel = ViewModelProvider(this)[MyViewModel::class.java]
        viewModel.count.observe(this) {
            // UI 업데이트
        }
    }
}
```

`SharedViewModel 구현 방법`

```kotlin
class SharedViewModel : ViewModel() {
    val sharedData = MutableLiveData<String>()
}
```

```kotlin
class FragmentA : Fragment() {
    private val sharedViewModel: SharedViewModel by activityViewModels()

    // sharedViewModel 사용
}

class FragmentB : Fragment() {
    private val sharedViewModel: SharedViewModel by activityViewModels()

    // sharedViewModel 사용
}
```
- **activityViewModels()**: 같은 Activity 내의 Fragment에서 SharedViewModel 인스턴스를 얻기 위해 사용됩니다.

### SharedViewModel 사용 시 주의사항
- **생명주기**: SharedViewModel은 Activity의 생명주기에 의존하므로, Activity가 종료되면 데이터가 소멸됩니다.
- **데이터 유출**: 민감한 데이터를 SharedViewModel에 저장하는 것은 보안상 위험할 수 있습니다.
- **복잡한 데이터 공유**: 매우 복잡한 데이터 공유가 필요한 경우에는 다른 방법(LiveDataBus, EventBus 등)을 고려할 수 있습니다.

### 언제 SharedViewModel을 사용해야 할까요?
- **같은 Activity 내의 여러 Fragment에서 데이터를 공유해야 할 때**: 예를 들어, 쇼핑몰 앱에서 장바구니 정보를 여러 Fragment에서 공유해야 하는 경우
- **Fragment 간의 긴밀한 상호 작용이 필요할 때**: 예를 들어, 마스터-디테일 구조에서 마스터 Fragment에서 선택한 항목을 디테일 Fragment에서 보여줘야 하는 경우

### 결론
ViewModel과 SharedViewModel은 Android 앱 개발에서 데이터 관리를 위한 필수적인 도구입니다. 각각의 특징을 이해하고 적절하게 사용하여 더욱 안정적이고 유지보수가 용이한 앱을 개발할 수 있습니다.

### 참고:
- Android 개발자 문서: https://developer.android.com/topic/libraries/architecture/viewmodel
- 코드랩: https://developer.android.com/codelabs/basic-android-kotlin-training-shared-viewmodel?hl=ko

---

## Android Jetpack이란

- Android Jetpack은 개발자가 고품질 Android 앱을 더 쉽고 효율적으로 빌드할 수 있도록 Google에서 제공하는 라이브러리, 도구 및 아키텍처 지침 세트입니다.
- 젯팩에는 데이터 저장, UI 디자인, 백그라운드 처리와 같은 일반적인 작업을 위한 라이브러리와 테스트, 디버깅, 프로파일링을 위한 도구가 포함되어 있습니다.
- Jetpack은 모듈식으로 유연하게 설계되어 개발자가 자신의 필요에 가장 적합한 구성 요소를 선택할 수 있습니다.

---

## Android 애플리케이션의 성능을 최적화하기 위한 전략

- 스레드, 코루틴 또는 RxJava와 같은 비동기 프로그래밍 기법을 사용하여 메인 UI 스레드를 차단하지 않기
- 이미지 로딩이나 데이터베이스 쿼리와 같은 메모리 집약적인 작업의 사용 최소화하기
- 화면 크기, 해상도, 하드웨어 구성이 다른 다양한 기기에서 앱 테스트하기
- 레이아웃 계층 구조의 수를 줄이고 가능한 한 플랫 레이아웃 사용
- 불필요한 네트워크 또는 디스크 액세스를 피하기 위해 데이터 및 리소스 캐싱
- 장기 실행 작업에 비동기 처리를 사용하여 UI 스레드 차단 방지
- 리소스가 더 이상 필요하지 않을 때 해제하여 메모리 누수 방지
- 안드로이드 스튜디오의 CPU 프로파일러 및 메모리 프로파일러와 같은 프로파일링 툴을 사용하여 성능 병목 현상을 파악합니다.

## 안드로이드에서 비동기 처리가 필요한 이유에 및 Rxjava, Coroutine 의 특징과 차이점, 장단점

### 왜 안드로이드에서 비동기 처리가 필요할까요?
안드로이드 앱은 주로 메인 스레드(UI 스레드)에서 동작합니다. 메인 스레드에서 시간이 오래 걸리는 작업(네트워크 통신, 데이터베이스 쿼리 등)을 처리하면 앱이 멈추거나 느려지는 현상이 발생합니다. 이는 사용자 경험을 저하시키는 주요 원인이 됩니다.

비동기 처리는 이러한 문제를 해결하기 위해 시간이 오래 걸리는 작업을 백그라운드 스레드에서 처리하고, 결과가 나오면 메인 스레드에 알려 UI를 업데이트하는 방식입니다. 이를 통해 메인 스레드는 항상 응답성을 유지하고 사용자에게 부드러운 사용 경험을 제공할 수 있습니다.

### RxJava와 Coroutine의 특징
RxJava는 리액티브 프로그래밍 패러다임을 기반으로 한 라이브러리로, 비동기 데이터 스트림을 처리하는 데 특화되어 있습니다. RxJava를 사용하면 복잡한 비동기 작업을 함수형 스타일로 간결하게 표현할 수 있으며, 데이터 변환, 필터링, 병렬 처리 등 다양한 연산을 지원합니다.

Coroutine은 코틀린에서 제공하는 비동기 처리 기능으로, 경량 스레드라고 할 수 있습니다. Coroutine은 비동기 코드를 동기 코드처럼 자연스럽게 작성할 수 있도록 해주며, 복잡한 비동기 로직을 간단하게 처리할 수 있습니다.

### RxJava와 Coroutine의 차이점
특징 | RxJava | Coroutine
--- | --- | ---
패러다임 | 리액티브 프로그래밍 | 코루틴
학습 곡선 | 높음 | 낮음 (코틀린 기반)
표현 방식 | 함수형 스타일 | 동기 코드처럼 작성
스케줄링 | 다양한 스케줄러 제공 | Dispatchers 제공
에러 처리 | Observable.error() | try-catch 블록
성능 | 복잡한 연산 시 성능 저하 가능성 | 일반적으로 우수

### RxJava와 Coroutine의 장단점
**RxJava**
- 장점:
    - 복잡한 비동기 작업을 효과적으로 처리
    - 다양한 연산자 제공
    - 데이터 스트림 처리에 강점
- 단점:
    - 학습 곡선이 높음
    - 코드가 복잡해질 수 있음
    - 성능 오버헤드 발생 가능성

**Coroutine**
- 장점:
    - 간결하고 직관적인 코드 작성
    - 코틀린과의 통합이 자연스러움
    - 성능이 우수
- 단점:
    - RxJava만큼 다양한 연산자는 제공하지 않음
    - 아직까지는 생태계가 RxJava만큼 풍부하지 않음

### 어떤 것을 선택해야 할까요?
- **RxJava**: 복잡한 비동기 작업, 데이터 스트림 처리, 함수형 프로그래밍에 익숙한 개발자에게 적합합니다.
- **Coroutine**: 간결하고 효율적인 비동기 처리를 원하는 개발자, 코틀린을 사용하는 개발자에게 적합합니다.

**결론적으로,** 어떤 것을 선택할지는 프로젝트의 특성과 개발자의 선호도에 따라 달라집니다. 최근에는 코루틴이 안드로이드 개발에서 더욱 많이 사용되고 있으며, Jetpack 라이브러리들과의 통합도 잘되어 있어 코루틴을 선택하는 것이 일반적인 추세입니다.

---

## 안드로이드에서 의존성 주입이란?
**의존성 주입(Dependency Injection, DI)**은 객체 생성 시 필요한 의존 객체를 외부에서 주입하는 디자인 패턴입니다. 즉, 클래스 내부에서 객체를 생성하는 대신, 외부에서 생성된 객체를 주입받아 사용하는 방식입니다.

### 왜 의존성 주입을 사용할까요?
- **테스트 용이성**: 의존 객체를 모킹하여 단위 테스트를 쉽게 수행할 수 있습니다.
- **결합도 감소**: 클래스 간의 의존성을 줄여 코드의 유연성을 높이고, 유지보수를 용이하게 합니다.
- **재사용성 증가**: 의존 객체를 재사용하여 코드 중복을 줄이고, 코드 가독성을 향상시킵니다.

### 안드로이드에서 의존성 주입
안드로이드 개발에서 의존성 주입은 컴포넌트 간의 결합도를 낮추고, 테스트 가능성을 높이며, 코드의 유지보수성을 향상시키는 데 중요한 역할을 합니다. 대표적인 의존성 주입 라이브러리로 Dagger와 Hilt가 있습니다.

## Dagger와 Hilt 비교
### Dagger
특징:
- 안드로이드에서 가장 많이 사용되는 의존성 주입 라이브러리 중 하나입니다.
- 강력한 기능과 커스터마이징이 가능하지만, 설정이 복잡하고 학습 곡선이 높습니다.
- 컴파일 시점에 의존성 그래프를 생성하여 안전성을 높입니다.

장점:
- 유연하고 다양한 기능 제공
- 커뮤니티 지원이 활발

단점:
- 설정이 복잡하고, 많은 양의 보일러플레이트 코드를 작성해야 합니다.
- 학습 곡선이 높아 초심자가 접근하기 어렵습니다.

### Hilt
특징:
- Google에서 공식적으로 지원하는 안드로이드 전용 의존성 주입 라이브러리입니다.
- Dagger를 기반으로 하며, Dagger의 복잡성을 줄이고 사용하기 쉽도록 설계되었습니다.
- 안드로이드 생명주기에 맞춰 자동으로 의존성을 주입합니다.

장점:
- Dagger보다 훨씬 간단하고 직관적인 설정
- 안드로이드 생명주기와의 통합이 뛰어남
- 학습 곡선이 낮아 초심자도 쉽게 사용 가능

단점:
- Dagger에 비해 기능이 제한적일 수 있습니다.

### Dagger와 Hilt 선택 가이드
**프로젝트 규모:**
- 소규모 프로젝트: Hilt가 더 적합합니다. 간단하고 빠르게 설정할 수 있습니다.
- 대규모 프로젝트: Dagger가 더 유연하고 다양한 기능을 제공하여 복잡한 의존성 관리에 적합할 수 있습니다.
**팀의 기술 수준:**
- 초심자: Hilt가 학습하기 쉽고, 빠르게 적용할 수 있습니다.
- 경험자: Dagger를 사용하여 더욱 복잡한 의존성 관리를 할 수 있습니다.
**기존 프로젝트:**
- 기존에 Dagger를 사용하고 있다면, Hilt로 마이그레이션하는 것이 쉽습니다.

### 결론
Hilt는 Dagger의 장점을 유지하면서, 안드로이드 개발에 특화되어 더욱 간편하게 사용할 수 있도록 설계되었습니다. 일반적으로 새로운 프로젝트에서는 Hilt를 사용하는 것이 권장됩니다. 하지만 프로젝트의 규모나 팀의 기술 수준에 따라 Dagger를 선택할 수도 있습니다.

---

## Android에서 Parcelable 인터페이스의 목적은 무엇이며 Serializable 사용성 차이

### Parcelable 인터페이스의 목적
Parcelable 인터페이스는 안드로이드에서 객체를 직렬화하여 Intent를 통해 다른 컴포넌트(Activity, Fragment 등) 간에 데이터를 전달하거나, Bundle에 저장하여 상태를 유지하기 위한 메커니즘입니다.
- 직렬화: 객체를 일련의 바이트로 변환하여 저장하거나 전송할 수 있도록 만드는 과정입니다.
- Intent를 통한 데이터 전달: Activity 간, 또는 Activity와 Service 간에 데이터를 주고받을 때 사용됩니다.
- Bundle에 저장: Activity의 상태를 저장하고 복원할 때 사용됩니다.

### 왜 Parcelable이 필요할까요?
- 기본 타입 외의 데이터 전달: Intent는 기본 타입(int, String 등) 외에 커스텀 객체를 직접 전달할 수 없습니다. Parcelable을 구현하여 커스텀 객체를 직렬화하면 Intent를 통해 전달할 수 있습니다.
- 효율적인 직렬화: Serializable 인터페이스에 비해 Reflection을 사용하지 않아 성능이 좋고, 메모리 사용량이 적습니다.

### Parcelable과 Serializable의 차이점
특징 | Parcelable | Serializable
---  | ---  | ---
성능 | 빠름 (Reflection 사용 X) | 느림 (Reflection 사용)
메모리 사용량 | 적음 | 많음
안드로이드 최적화 | O | X
구현 복잡도 | 높음 (직접 구현해야 함) | 낮음 (자동 직렬화)

- Reflection: 런타임에 객체의 메타데이터를 동적으로 얻는 메커니즘입니다. Serializable은 Reflection을 사용하여 객체를 직렬화하기 때문에 성능이 느리고 메모리 사용량이 많습니다.
- Parcelable: 개발자가 직접 직렬화/역직렬화 로직을 구현해야 하므로 Serializable보다 구현이 복잡하지만, 안드로이드 환경에 최적화되어 있어 성능이 좋고 메모리 사용량이 적습니다.

### Parcelable 구현 예시
```kotlin
class MyData : Parcelable {
    var name: String? = null
    var age: Int = 0

    constructor(parcel: Parcel) {
        name = parcel.readString()
        age = parcel.readInt()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
        parcel.writeInt(age)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<MyData> {
        override fun createFromParcel(parcel: Parcel): MyData {
            return MyData(parcel)
        }

        override fun newArray(size: Int): Array<MyData?> {
            return arrayOfNulls(size)
        }
    }
}
```

- *writeToParcel*: 객체의 데이터를 Parcel 객체에 쓰는 메소드입니다.
- createFromParcel: Parcel 객체에서 데이터를 읽어와 객체를 생성하는 메소드입니다.
- describeContents: 객체의 내용을 설명하는 메소드로, 일반적으로 0을 반환합니다.
- CREATOR: Parcelable.Creator 인터페이스를 구현한 컴패니언 객체로, Parcel 객체에서 객체를 생성하는 데 사용됩니다.

### 언제 Parcelable을 사용해야 할까요?
- **성능이 중요한 경우**: 많은 양의 데이터를 주고받거나, 실시간 처리가 필요한 경우 Parcelable을 사용하여 성능을 향상시킬 수 있습니다.
- **커스텀 객체를 Intent나 Bundle에 전달해야 할 경우**: Parcelable을 구현하여 커스텀 객체를 직렬화하여 전달할 수 있습니다.
- **안드로이드 시스템과의 호환성이 중요한 경우**: 안드로이드 시스템은 Parcelable을 기본적으로 지원하며, Serializable에 비해 더 효율적으로 작동합니다.

### 결론
Parcelable은 안드로이드에서 객체를 직렬화하여 데이터를 전달하는 데 효율적인 방법입니다. Serializable에 비해 성능이 좋고 안드로이드 시스템에 최적화되어 있지만, 구현이 복잡하다는 단점이 있습니다. 따라서 프로젝트의 요구사항에 맞게 적절한 방법을 선택해야 합니다.

---

## Android의 콘텐츠 제공자는 무엇이며 주요 목적은 무엇인가요? 콘텐츠 제공자가 유용할 수 있는 예시 시나리오를 제시
**콘텐츠 제공자(Content Provider)**는 안드로이드 앱에서 데이터를 공유하고 관리하는 데 사용되는 핵심적인 컴포넌트입니다. 앱 간에 데이터를 안전하게 주고받으며, 데이터의 일관성과 보안을 유지하는 역할을 합니다.

### 콘텐츠 제공자의 주요 목적
- 앱 간 데이터 공유: 다른 앱에서 자신의 앱에 저장된 데이터에 접근하고 수정할 수 있도록 허용합니다.
- 데이터 보안: 앱의 데이터를 직접 노출하지 않고, 필요한 부분만 선택적으로 공개하여 데이터 보안을 유지합니다.
- 데이터 관리: 앱의 데이터를 체계적으로 관리하고, 다양한 형태의 데이터를 저장하고 검색할 수 있도록 지원합니다.

### 콘텐츠 제공자가 유용한 예시 시나리오
**연락처 앱:**
- 다른 앱에서 사용자의 연락처 정보에 접근하여 메시지를 보내거나 전화를 걸 수 있도록 합니다.

**캘린더 앱:**
- 다른 앱에서 사용자의 일정 정보를 읽어와 일정을 관리하거나 알림을 설정할 수 있도록 합니다.

**사진 앱:**
- 다른 앱에서 사용자가 촬영한 사진을 가져와 편집하거나 공유할 수 있도록 합니다.

**음악 앱:**
- 다른 앱에서 사용자의 음악 파일을 재생하거나 플레이리스트를 관리할 수 있도록 합니다.

### 콘텐츠 제공자의 작동 방식
- `ContentResolver`: 다른 앱에서 콘텐츠 제공자에 접근하기 위해 사용하는 객체입니다.
- `Content URI`: 콘텐츠 제공자의 데이터를 식별하는 고유한 URI입니다.
- `ContentProvider`: 실제 데이터를 관리하고, ContentResolver의 요청을 처리하는 클래스입니다.

### 콘텐츠 제공자를 사용하는 이유
- 데이터 공유: 앱 간 데이터 공유를 통해 기능을 확장하고 사용자 경험을 향상시킬 수 있습니다.
- 데이터 보안: 앱의 데이터를 직접 노출하지 않고, 필요한 부분만 선택적으로 공개하여 데이터 보안을 유지할 수 있습니다.
- 데이터 관리: 앱의 데이터를 체계적으로 관리하고, 다양한 형태의 데이터를 저장하고 검색할 수 있습니다.
- 시스템 서비스 통합: 시스템에서 제공하는 다양한 서비스(연락처, 캘린더 등)에 접근하여 앱의 기능을 확장할 수 있습니다.

**결론적으로,** 콘텐츠 제공자는 안드로이드 앱 개발에서 매우 중요한 역할을 합니다. 앱 간 데이터 공유를 위한 안전하고 효율적인 메커니즘을 제공하며, 다양한 앱 개발 시나리오에 활용될 수 있습니다.

### 참고
- [Android Developers: 콘텐츠 제공자](https://developer.android.com/guide/topics/providers/content-providers)

---

## [안드로이드 어플리케이션 컴포넌트](https://developer.android.com/guide/components/fundamentals.html#Components)

### Activity
- 화면 하나를 표현하며, 사용자와 상호작용하기 위한 진입점
- 사용자가 현재 관심을 가지고 있는 사항(화면에 표시된 것)을 추적하여 액티비티를 호스팅하는 프로세스를 시스템에서 계속 실행하도록 합니다.
- 이전에 사용한 프로세스에 사용자가 다시 찾을 만한 액티비티(중단된 액티비티)가 있다는 것을 알고, 해당 프로세스를 유지하는 데 더 높은 우선순위를 부여합니다.
- 앱이 프로세스를 종료하도록 도와서 이전 상태가 복원되는 동시에 사용자가 액티비티로 돌아갈 수 있게 합니다.
- 앱이 서로 사용자 플로우를 구현하고 시스템이 이러한 플로우를 조정하기 위한 수단을 제공합니다.

### Service

- 서비스는 여러 가지 이유로 백그라운드에서 앱을 계속 실행하기 위한 다목적 진입점입니다. 
- 백그라운드에서 실행되는 구성 요소로, 오랫동안 실행되는 작업을 수행하거나 원격 프로세스를 위한 작업을 수행
- 서비스는 사용자 인터페이스를 제공하지 않습니다.
- 음악 재생은 사용자가 바로 인식할 수 있는 작업입니다. 따라서 앱은 사용자에게 이와 관련된 알림을 보내고 음악 재생을 포그라운드로 옮기라고 시스템에 지시합니다. 이 경우, 시스템은 이 서비스의 프로세스가 계속 실행되도록 많은 노력을 기울여야 합니다. 이 서비스가 사라지면 사용자가 불만을 느낄 것이기 때문입니다.
- 정기적인 백그라운드 서비스는 사용자가 실행되고 있다고 직접 인식할 수 없는 작업이므로 시스템은 좀 더 자유롭게 프로세스를 관리할 수 있습니다. 사용자와 좀 더 직접적인 관련이 있는 작업에 RAM이 필요할 경우 이 서비스를 종료할 수도 있습니다(그런 다음, 나중에 서비스를 다시 시작할 수도 있습니다).

### Broadcast Receiver

- Broadcast Receiver는 시스템이 정기적인 사용자 플로우 밖에서 이벤트를 앱에 전달하도록 지원하는 구성 요소
- 앱이 시스템 전체의 브로드캐스트 알림에 응답할 수 있게 합니다
- 예를 들어 앱이 사용자에게 예정된 이벤트에 대해 알리는 알림을 게시하기 위한 알람을 예약할 경우, 그 알람을 앱의 Broadcast Receiver에 전달하면 알람이 울릴 때까지 앱을 실행하고 있을 필요가 없습니다
- 화면이 꺼졌거나 배터리가 부족하거나 사진을 캡처했다고 알리는 브로드캐스트
- Broadcast Receiver는 사용자 인터페이스를 표시하지 않지만, 상태 표시줄 알림을 생성하여 사용자에게 브로드캐스트 이벤트가 발생했다고 알릴 수 있습니다.
- JobService를 예약하여 시작하여 JobScheduler가 포함된 이벤트를 기초로 어떤 작업을 수행하게 할 수 있습니다.

### Content Provider
- 콘텐츠 제공자는 파일 시스템, SQLite 데이터베이스, 웹상이나 앱이 액세스할 수 있는 다른 모든 영구 저장 위치에 저장 가능한 앱 데이터의 공유형 집합을 관리
- 다른 앱은 콘텐츠 제공자를 통해 해당 데이터를 쿼리하거나, 콘텐츠 제공자가 허용할 경우에는 수정도 가능
- 시스템의 경우 콘텐츠 제공자는 URI 구성표로 식별되고 이름이 지정된 데이터 항목을 게시할 목적으로 앱에 진입하기 위한 입구
- URI를 할당하더라도 앱을 계속 실행할 필요가 없으므로 URI를 소유한 앱이 종료된 후에도 URI를 유지할 수 있습니다. 시스템은 URI를 소유한 앱이 해당 URI에서 앱의 데이터를 검색할 때만 실행되도록 하면 됩니다.
- 이 URI는 중요하고 조밀한 보안 모델을 제공합니다. 예를 들어 앱은 클립보드에 있는 이미지에 URI를 할당하고 콘텐츠 제공자가 검색하도록 하여, 다른 앱이 자유롭게 이미지에 액세스하지 못하게 막을 수 있습니다. 두 번째 앱이 클립보드에서 해당 URI에 액세스하려고 시도하면 시스템에서는 임시 URI 권한을 부여하여 그 앱이 데이터에 액세스하도록 허용할 수 있습니다. 따라서 두 번째 앱에서는 URI 뒤에 있는 데이터 외에 다른 것에는 액세스할 수 없습니다.
- 콘텐츠 제공자는 앱 전용이어서 공유되지 않는 데이터를 읽고 쓰는 데도 유용합니다.
 
### 구성 요소 활성화

구성 요소 유형 네 가지 중 세 가지(액티비티, 서비스, Broadcast Receiver)는 인텐트라는 비동기식 메시지로 활성화됩니다. 인텐트는 런타임에서 각 구성 요소를 서로 바인딩합니다. 이것은 일종의 메신저라고 생각하면 됩니다. 즉 구성 요소가 어느 앱에 속하든 관계없이 다른 구성 요소로부터 작업을 요청하는 역할을 합니다.
인텐트는 Intent 객체로 생성되며, 이것이 특정 구성 요소(명시적 인텐트)를 활성화할지 아니면 구성 요소의 특정 유형(암시적 인텐트)을 활성화할지 나타내는 메시지를 정의합니다.

각 유형의 구성 요소를 활성화하는 데는 각기 별도의 메서드가 있습니다.

- 액티비티를 시작하거나 새로운 작업을 배정하려면 Intent를 startActivity() 또는 startActivityForResult()에 전달하면 됩니다(액티비티가 결과를 반환하기를 원하는 경우).
- Android 5.0(API 레벨 21) 이상에서는 JobScheduler 클래스를 사용하여 작업을 예약할 수 있습니다. 초기 Android 버전의 경우 Intent를 startService()에 전달하여 서비스를 시작할 수 있습니다(또는 진행 중인 서비스에 새로운 지침을 전달할 수 있습니다). Intent를 bindService()에 전달하여 서비스에 바인딩할 수도 있습니다.
- sendBroadcast(), sendOrderedBroadcast(), 또는 sendStickyBroadcast()와 같은 메서드에 Intent를 전달하면 브로드캐스트를 시작할 수 있습니다.
- 콘텐츠 제공자에 쿼리를 수행하려면 ContentResolver에서 query()를 호출하면 됩니다.

---

## [매니페스트 파일](https://developer.android.com/guide/components/fundamentals.html#Manifest)

모든 앱 프로젝트는 프로젝트 소스 세트의 루트에 AndroidManifest.xml 파일(정확히 이 이름)이 있어야 합니다. 매니페스트 파일은 Android 빌드 도구, Android 운영체제 및 Google Play에 앱에 관한 필수 정보를 설명합니다.

매니페스트 파일은 다른 여러 가지도 설명하지만 특히 다음과 같은 내용을 선언해야 합니다.

- 앱의 패키지 이름(일반적으로 코드의 네임스페이스와 일치). Android 빌드 도구는 프로젝트를 빌드할 때 이 이름으로 코드 엔터티의 위치를 확인합니다. 앱을 패키징할 때 빌드 도구가 이 값을 Gradle 빌드 파일의 애플리케이션 ID로 대체합니다. 이는 시스템과 Google Play에서 고유한 앱 식별자로 사용됩니다. 패키지 이름과 앱 ID에 대해 자세히 알아보세요.
- 앱의 구성 요소(모든 액티비티, 서비스, Broadcast Receiver, 콘텐츠 제공자 포함). 각 구성 요소는 Kotlin이나 Java 클래스의 이름과 같은 기본 속성을 정의해야 합니다. 또한 자신이 처리할 수 있는 기기 구성의 종류, 그리고 구성 요소가 어떻게 시작되는지 설명하는 인텐트 필터와 같은 기능을 선언할 수도 있습니다. 앱 구성 요소에 대해 자세히 알아보세요.
- 앱이 시스템 또는 다른 앱의 보호된 부분에 액세스하기 위해 필요한 권한. 이것은 다른 앱이 이 앱의 콘텐츠에 액세스하고자 하는 경우 반드시 있어야 하는 모든 권한도 선언합니다. 권한에 대해 자세히 알아보세요.
- 앱에 필요한 하드웨어 및 소프트웨어 기능으로, 이에 따라 앱을 Google Play에서 설치할 수 있는 기기의 종류가 달라집니다. 기기 호환성에 대해 자세히 알아보세요.

---

## [안드로이드 어플리케이션의 프로젝트 구조](https://developer.android.com/studio/projects)

---

## [Android Context](https://lakue.tistory.com/82)
Context는 안드로이드 앱의 현재 상태와 환경에 대한 정보를 담고 있는 객체라고 할 수 있습니다. 쉽게 말해, 앱이 어떤 상황에서 실행되고 있는지에 대한 맥락(context)을 제공하는 것이죠.

### 왜 Context가 필요할까요?
- 리소스 접근: 문자열, 이미지, 레이아웃 등 앱의 리소스에 접근하기 위해 Context가 필요합니다.
- 시스템 서비스 접근: 시스템 서비스(예: 알람 관리자, 위치 관리자)를 사용하려면 Context가 필요합니다.
- Intent 생성: 다른 액티비티나 서비스를 시작하기 위한 Intent를 생성할 때 Context가 필요합니다.
- 뷰 생성: 레이아웃을 inflate하여 뷰를 생성할 때 Context가 필요합니다.

### Context의 종류
- Application Context: 앱 전체의 생명주기를 따라가는 Context입니다. 앱이 실행되는 동안 항상 존재하며, 앱 레벨의 작업(예: SharedPreferences, 데이터베이스 접근)에 사용됩니다.
- Activity Context: 특정 액티비티의 생명주기를 따르는 Context입니다. 액티비티와 관련된 작업(예: 뷰 생성, 다이얼로그 표시)에 사용됩니다.

### Context 사용 시 주의할 점
- 메모리 누수: Context는 메모리 누수의 주범이 될 수 있습니다. 특히, Activity Context를 장기간 유지하면 메모리 누수가 발생할 수 있습니다.
- 잘못된 사용: Context를 잘못 사용하면 예상치 못한 동작이 발생할 수 있습니다. 예를 들어, Activity Context를 ViewModel에서 사용하면 메모리 누수가 발생할 수 있습니다.

### Context 사용 예시
```kotlin
class MyActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // 리소스 접근
        val myString = getString(R.string.app_name)

        // 시스템 서비스 접근
        val getSystemService = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

        // Intent 생성
        val intent = Intent(this, SecondActivity::class.java)
        startActivity(intent)
    }
}
```

### Context를 올바르게 사용하는 방법
- 필요한 Context 사용: 작업의 종류에 따라 적절한 Context를 사용해야 합니다.
- 메모리 누수 방지: Context를 불필요하게 오래 유지하지 않도록 주의해야 합니다.
- ViewModel에서 Context 사용 시 주의: ViewModel에서는 Application Context를 사용하는 것이 일반적입니다.

---

## Android Intent
Intent는 안드로이드 앱의 다양한 구성 요소(Activity, Service, BroadcastReceiver 등) 간의 통신을 위한 메시지 객체라고 할 수 있습니다. 즉, 어떤 작업을 수행하라는 명령과 함께 필요한 데이터를 담아 다른 컴포넌트에게 전달하는 역할을 합니다.

### Intent의 주요 기능
- 액티비티 시작: 새로운 액티비티를 실행하거나 기존 액티비티로 이동할 때 사용합니다.
- 서비스 시작: 서비스를 시작하거나 바인딩할 때 사용합니다.
- 브로드캐스트 전달: 브로드캐스트를 전송하여 시스템이나 다른 앱에 특정 이벤트를 알릴 때 사용합니다.

### Intent의 종류
- 명시적 Intent: 특정 컴포넌트(Activity, Service 등)를 직접 지정하여 실행하는 Intent입니다.
- 암시적 Intent: 수행할 작업만 지정하고, 시스템에서 적합한 컴포넌트를 찾아 실행하는 Intent입니다.

### Intent의 구성 요소
- Action: 수행할 작업을 나타내는 문자열입니다. 예) ACTION_VIEW, ACTION_SEND 등
- Data: 작업에 필요한 데이터를 나타내는 URI입니다.
- Category: Intent의 추가적인 정보를 제공합니다. 예) CATEGORY_BROWSABLE, CATEGORY_LAUNCHER 등
- Extra: 추가적인 데이터를 key-value 형태로 전달할 때 사용합니다.

### Intent 사용 예시
```kotlin
// 명시적 Intent: 특정 Activity 시작
val intent = Intent(this, SecondActivity::class.java)
startActivity(intent)

// 암시적 Intent: 웹 브라우저 열기
val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.example.com"))
startActivity(intent)

// 데이터 전달
val intent = Intent(this, SecondActivity::class.java)
intent.putExtra("data", "Hello, World!")
startActivity(intent)
```

### Intent Filter
암시적 Intent를 사용할 때, 시스템은 어떤 컴포넌트가 이 Intent를 처리할 수 있는지 판단하기 위해 Intent Filter를 사용합니다. Intent Filter는 AndroidManifest.xml 파일에 정의되며, 해당 컴포넌트가 처리할 수 있는 Intent의 종류를 명시합니다.

### Intent의 활용
- 앱 간 데이터 공유: 다른 앱의 기능을 활용하거나, 데이터를 주고받을 때 사용합니다. (예: 사진 공유, 지도 앱에서 위치 검색)
- 시스템 서비스 호출: 시스템 서비스(예: 알람, 위치 정보)를 이용할 때 사용합니다.
- 앱 내부 컴포넌트 간 통신: 다양한 액티비티, 서비스, 브로드캐스트 리시버 간의 통신을 위해 사용합니다.

### 왜 Intent를 사용해야 할까요?
- 앱 구성 요소 간의 느슨한 결합: 각 컴포넌트는 Intent를 통해 서로를 명확하게 알 필요 없이 통신할 수 있습니다.
- 재사용성: Intent를 통해 다양한 앱에서 동일한 작업을 수행할 수 있습니다.
- 확장성: 새로운 기능을 추가할 때 기존 코드에 영향을 미치지 않고 Intent를 통해 연결할 수 있습니다.

**결론적으로,** Intent는 안드로이드 앱 개발에서 매우 중요한 개념입니다. 앱 구성 요소 간의 유연하고 효율적인 통신을 가능하게 하며, 앱의 확장성과 재사용성을 높이는 데 기여합니다.

---

## Intent Filter 활용 방법
Intent Filter는 안드로이드 앱에서 특정 컴포넌트(Activity, Service, BroadcastReceiver)가 처리할 수 있는 Intent의 종류를 명시하는 데 사용됩니다. 즉, 어떤 종류의 작업 요청을 받아들일 수 있는지를 정의하는 역할을 합니다.

### Intent Filter를 사용하는 이유
- 암시적 Intent 처리: 앱 외부에서 특정 작업을 요청할 때, 시스템은 Intent Filter를 참고하여 해당 작업을 처리할 수 있는 컴포넌트를 찾습니다.
- 앱 간 통합: 다른 앱에서 제공하는 기능을 활용하거나, 자신의 앱 기능을 다른 앱에 제공할 수 있도록 합니다.
- 시스템 서비스 연동: 시스템 서비스(예: 알람, 위치 서비스)를 사용할 때, Intent Filter를 통해 해당 서비스와 통신할 수 있습니다.

### Intent Filter 구성 요소
- action: 수행할 작업을 나타내는 문자열 (예: ACTION_VIEW, ACTION_SEND)
- data: 작업에 필요한 데이터를 나타내는 URI
- category: Intent의 추가적인 정보를 제공 (예: CATEGORY_BROWSABLE, CATEGORY_LAUNCHER)

### Intent Filter 설정 예시
```xml
<activity android:name=".MainActivity">
    <intent-filter>
        <action android:name="android.intent.action.VIEW" />
        <category android:name="android.intent.category.DEFAULT" />
        <data android:mimeType="text/plain" />
    </intent-filter>
</activity>
```
위 예시는 MainActivity가 "text/plain" 형식의 데이터를 보여주는 작업(ACTION_VIEW)을 처리할 수 있음을 나타냅니다.

### Intent Filter 활용 시 주의사항
- 정확한 설정: 잘못된 Intent Filter 설정은 의도하지 않은 결과를 초래할 수 있습니다.
- 보안: 암시적 Intent를 사용할 때는 보안에 유의해야 합니다. 악의적인 앱이 임의의 Intent를 보내 앱의 기능을 오용할 수 있기 때문입니다.

---

## PendingIntent란 무엇일까요?
PendingIntent는 Intent를 감싸서 특정 조건이나 시간에 실행될 수 있도록 만든 객체입니다. 즉, 지금 당장 실행되지 않고, 나중에 특정한 상황에서 실행될 Intent를 미리 준비해 놓는다고 생각하면 됩니다.

### PendingIntent를 사용하는 이유
- 알림: 알림을 클릭했을 때 특정 액티비티를 실행하거나 브로드캐스트를 전송할 때 사용합니다.
- 위젯: 위젯에서 버튼을 클릭했을 때 특정 작업을 수행하도록 설정할 때 사용합니다.
- 알람: 특정 시간에 작업을 수행하도록 예약할 때 사용합니다.

### PendingIntent 생성 방법
```kotlin
val pendingIntent = PendingIntent.getActivity(
    this,
    0,
    intent,
    PendingIntent.FLAG_UPDATE_CURRENT
)
```
- getActivity: 액티비티를 시작하는 PendingIntent 생성
- getService: 서비스를 시작하는 PendingIntent 생성
- getBroadcast: 브로드캐스트를 전송하는 PendingIntent 생성

### PendingIntent 사용 예시
- 알림: NotificationManager를 통해 Notification을 생성하고, PendingIntent를 사용하여 알림을 클릭했을 때 실행될 Intent를 설정합니다.
- AlarmManager: AlarmManager를 통해 특정 시간에 PendingIntent를 실행하여 작업을 수행하도록 예약합니다.

### PendingIntent와 Intent의 차이점

구분 | Intent | PendingIntent
--- | --- | ---
설명 | 즉시 실행되는 메시지 | 나중에 실행될 메시지를 감싸는 객체
용도 | 컴포넌트 간 통신 | 알림, 위젯, 알람 등
생성 방식 | Intent 객체 생성 | PendingIntent.getActivity(), PendingIntent.getService() 등


### 결론
Intent Filter는 앱의 기능을 확장하고 다른 앱과의 연동을 가능하게 합니다. PendingIntent는 특정 조건이나 시간에 Intent를 실행할 수 있도록 하여 앱의 기능을 더욱 풍부하게 만들어 줍니다.

---

## ProGuard란 무엇이며, 왜 사용할까요?
ProGuard는 자바 코드를 축소, 최적화, 난독화하는 오픈 소스 도구입니다. 특히 안드로이드 앱 개발에서 APK 파일의 크기를 줄이고, 역공학을 어렵게 만들어 앱 보안을 강화하는 데 널리 사용됩니다.

### ProGuard의 주요 기능
- 축소(Shrink): 앱에서 실제로 사용되지 않는 클래스, 메서드, 필드 등을 제거하여 APK 파일의 크기를 줄입니다.
- 최적화(Optimize): 바이트코드를 분석하여 불필요한 코드를 제거하고, 실행 속도를 향상시킵니다.
- 난독화(Obfuscate): 클래스, 메서드, 필드의 이름을 짧고 의미 없는 문자열로 변경하여 역공학을 어렵게 만듭니다.

### ProGuard를 사용하는 이유
- APK 파일 크기 감소: 불필요한 코드를 제거하여 APK 파일의 크기를 줄여 다운로드 시간을 단축하고, 사용자 디바이스의 저장 공간을 절약합니다.
- 앱 보안 강화: 난독화를 통해 코드를 읽기 어렵게 만들어 역공학을 통한 앱 분석을 어렵게 합니다. 이는 앱의 지적 재산을 보호하고, 불법적인 수정이나 복제를 방지하는 데 도움이 됩니다.
- 성능 향상: 최적화를 통해 앱의 실행 속도를 향상시킬 수 있습니다.

### ProGuard 사용 시 주의사항
- 잘못된 설정: ProGuard 설정을 잘못하면 앱이 정상적으로 작동하지 않을 수 있습니다. 특히, 사용자 정의 클래스나 라이브러리에 대한 예외 처리가 필요할 수 있습니다.
- 완벽한 보안 보장 X: ProGuard는 앱을 완벽하게 보호하는 것은 아닙니다. 결정적인 보안은 서버 측 보안과 결합하여 구현해야 합니다.
- 난독화의 한계: 난독화는 코드를 읽기 어렵게 만들지만, 충분한 시간과 노력을 들이면 역공학이 가능합니다.

### ProGuard 설정
ProGuard는 proguard-rules.pro 파일에서 설정합니다. 이 파일에서 keep 규칙을 통해 보호해야 할 클래스, 메서드, 필드 등을 지정할 수 있습니다.
```
-keep public class * extends android.app.Activity
-keep public class * extends android.app.Application
-keep public class * extends android.app.Service
```
위 예시는 Activity, Application, Service 클래스를 보호하는 규칙입니다.

### 결론
ProGuard는 안드로이드 앱 개발에서 필수적인 도구입니다. APK 파일의 크기를 줄이고, 앱의 보안을 강화하며, 성능을 향상시키는 데 큰 도움을 줍니다. 하지만 ProGuard 설정에 주의를 기울여야 하고, 앱의 특성에 맞는 적절한 설정을 해야 합니다.

---

### 인텐트(Intent)

> 인텐트는 액션, 카테고리 등등 정해진 형식의 데이터와 번들(Bundle) 형태로 포장된 임의의 데이터를 포함할 수 있습니다. 그리고 플랫폼에서 제공하는 API 를 활용해 프로세스를 건너 다른 애플리케이션 구성요소로 전달될 수 있습니다. 특히, 서비스와 액티비티 간의 통신을 위해서는 인텐트와 startService 그리고 sendBroadcast 메서드를 조합하여 사용할 수 있습니다.

**서비스와 액티비티 간 데이터 통신을 활용하는 예**

- 백그라운드 파일 다운로드처럼 비교적 UI와의 연결고리가 약하고 비동기로 작업을 수행하는 서비스는 이 방법을 활용 가능합니다. 액티비티가 필요한 요청이 있을 때마다 startService 를 통해 다운로드할 파일 정보를 전달하고, 다운로드가 진행될 때마다, 브로드캐스트 인텐트를 통해 액티비티에 다운로드 진척상황을 전달합니다. 특히, 인텐트를 수신해 작업을 처리하는데 특화된 [인텐트 서비스(IntentService)](https://developer.android.com/reference/android/app/IntentService.html) 클래스를 활용하면 좀 더 쉽게 이러한 구조를 갖는 앱을 구현할 수 있습니다.

### Bundle

> IPC 를 위한 직렬화 객체,Parcelable 을 상속받아 구현되었기 때문에 Parcelable 객체이기도 하다.

**장점**

- 번들 클래스는 안드로이드 SDK 에 포함되어 있기 때문에 클래스 파일 자체를 배포할 필요가 없다.
- 번들은 모든 타입의 직렬화 객체를 담을 수 있을 뿐만 아니라 Parcel 과는 달리 읽고 쓰는 순서를 일치시킬 필요가 없다.

### 메신저(Messenger)

> 백그라운드 서비스와 액티비티가 매우 빈번하게 데이터를 주고받아야 하는 경우에 인텐트와 브로드캐스트 리시버를 활용하는 것은 너무 무겁게 느껴질 수 있습니다. 두 구성 요소 간 커뮤니케이션이 빈번하지만, 서로 주고받는 메시지의 종료가 복잡하지 않고 작업 요청과 결과 수신을 비동기식으로 처리할 수 있다면, 안드로이드 플랫폼이 제공하는 또 하나의 IPC 수단인 메신저 객체를 활용할 수 있습니다.
메신저는 안드로이드는 핸들러(Handler)를 활용하여 구현되었습니다. 일반적으로 핸들러는 서로 다른 스레드 간에 메시지를 주고받을 때 사용됩니다. 메신저 객체를 활용해 프로세스의 경계를 넘어 서로 다른 프로세스에 존재하는 핸들러로 메시지를 전달하거나 받을 수 있습니다. 메신저는 특정 핸들러 인스턴스 참조를 갖고 있으면서, 동시에 Parcelable 인터페이스를 구현하고 있고, RPC 형태로 호출될 수 있는 간단한 바인더 메서드를 포함하고 있습니다.
특정 핸들러 인스턴스를 기반으로 메신저 객체를 생성한 후, 해당 객체를 다른 프로세스로 전달하고, 이후 메신저의 send() 메서드를 이용해 프로세스 너머에 있는 핸들러에 메시지를 전달할 수 있습니다.

**구체적인 구현 방법 - API 데모에 포함된 샘플 코드**

- [MessengerService](https://android.googlesource.com/platform/development/+/master/samples/ApiDemos/src/com/example/android/apis/app/MessengerService.java)

- [MessengerServiceActivities](https://android.googlesource.com/platform/development/+/master/samples/ApiDemos/src/com/example/android/apis/app/MessengerServiceActivities.java)

### AIDL(Android Interface Definition Language)

> 백그라운드 서비스와 액티비티 간에 주고받아야 하는 메시지의 종류가 다양하고 특히 동기식으로 함수 호출의 결과를 바로 받아야 할 필요가 있는 경우, 메신저를 활용하는데 어려움이 있을 수 있습니다.
메시지 종류가 많아지면, 필연적으로 이를 처리하는 핸들러 구현도 복잡해지며, 메신저를 통해서는 일반 함수 호출과 같이 바로 특정 함수의 호출 결과를 확인할 수 없습니다. 이런 경우 AIDL을 이용해 바인더 인터페이스를 정의하고, 해당 바인더를 전달받은 액티비티가 다른 프로세스에서 동작하고 있는 서비스에서 제공하는 API를 RPC 형식으로 바로 호출할 수 있도록 구현할 수 있습니다.

- 다양한 종류의 함수와 `동기식/비동기식` 호출 방법을 모두 제공하는 안드로이드 시스템 서비스들이 이러한 방식으로 구현되어 있습니다. 서비스가 제공해야 하는 API 명세를 `AIDL` 스펙에 맞게 정의하면, 안드로이드 개발 도구를 활용해 이를 `바인더 인터페이스와 Stub 추상 클래스로 변환`할 수 있습니다. 이후, `Stub` 클래스를 구현하고 서비스 `bind/unbind` 주기에 맞춰 해당 바인더를 연결된 액티비티에 넘겨주면, 액티비티는 마치 안드로이드 시스템 서비스를 사용하는 것처럼, 자유롭게 프로세스를 건너 `AIDL`에 정의된 함수를 호출할 수 있습니다.

- `AIDL`을 사용하는 데는 어느 정도의 코드 작업이 필요하긴 하지만, 안드로이드 스튜디오에서는 비교적 수월하게 이를 구현할 수 있습니다. `File > NEW > AIDL` 메뉴를 통해 새로운 AIDL 파일을 추가하면 자동으로 필요한 폴더와 참고할 수 있는 코드 Fragment이 포함된 `AIDL` 파일이 생성됩니다. 필요한 인터페이스를 정의한 후 `compileDebugAidl` Gradle Task 를 수행하면 자동으로 Stub 객체가 생성됩니다. 백그라운드 서비스에서 해당 Stub 객체의 실제 로직을 구현하고, onBind 콜백에서 이를 넘겨 줍니다. `AIDL` 을 사용하는 방법에 관해서는 [자세한 가이드](http://developer.android.com/guide/components/aidl.html)가 제공되고 있고 [API Demo 샘플](https://android.googlesource.com/platform/development/+/master/samples/ApiDemos/src/com/example/android/apis/app/RemoteService.java)에도 관련 예제가 첨부되어 있습니다.

### 그 외 영구적으로 저장되는 데이터들

1. [SharedPreference](https://developer.android.com/reference/android/content/SharedPreferences.OnSharedPreferenceChangeListener.html)
2. [FileObserver](https://developer.android.com/reference/android/os/FileObserver.html)
3. [ContentProvider와 ContentObserver](http://developer.android.com/reference/android/database/ContentObserver.html)

---

## Navigation Component

Navigation Component는 안드로이드 Jetpack Compose 라이브러리의 일부로, 앱 내에서 다양한 화면 간의 이동을 관리하고, 딥 링크, 백 스택 등 복잡한 네비게이션 시나리오를 효율적으로 처리하기 위한 도구입니다.

### 왜 Navigation Component를 사용해야 할까요?
- 간편한 화면 전환: XML 파일이나 Kotlin 코드를 통해 간단하게 화면 전환을 정의할 수 있습니다.
- 백 스택 관리: 시스템이 자동으로 백 스택을 관리하여 사용자가 뒤로 가기 버튼을 누를 때 이전 화면으로 자연스럽게 이동합니다.
- 딥 링크 지원: 앱 외부에서 특정 화면으로 바로 이동하는 딥 링크를 쉽게 설정할 수 있습니다.
- Safe Args: 화면 간에 데이터를 안전하게 전달할 수 있도록 지원합니다.
- Jetpack Compose와의 완벽한 통합: Jetpack Compose와 함께 사용하여 더욱 직관적인 UI를 구축할 수 있습니다.

### Navigation Component의 주요 구성 요소
- NavHost: 앱 내에서 네비게이션이 발생하는 공간을 나타냅니다.
- NavGraph: NavHost에서 사용되는 네비게이션 그래프를 정의합니다.
- NavHostController: NavGraph를 통해 화면 전환을 제어하는 클래스입니다.
- Destination: NavGraph에서 정의된 각 화면을 나타냅니다.
- Action: 한 Destination에서 다른 Destination으로 이동하는 동작을 나타냅니다.

### Navigation Component 사용 예시
```kotlin
// XML 레이아웃에서 NavHost 설정
<androidx.navigation.fragment.NavHostFragment
    android:id="@+id/nav_host_fragment"
    android:name="androidx.navigation.fragment.NavHostFragment"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    app:defaultNavHost="true"
    app:navGraph="@navigation/nav_graph" />

// Kotlin 코드에서 NavHostController 가져오기
val navController = findNavController(R.id.nav_host_fragment)

// 다른 화면으로 이동
navController.navigate(R.id.secondFragment)
```

### Navigation Component의 장점
- 코드 가독성 향상: 복잡한 화면 전환 로직을 간결하게 표현할 수 있습니다.
- 유지보수성 향상: 네비게이션 관련 코드를 한 곳에서 관리할 수 있어 유지보수가 용이합니다.
- 안정적인 화면 전환: 시스템에서 백 스택을 자동으로 관리하여 예기치 않은 문제 발생 가능성을 줄입니다.
- Jetpack Compose와의 시너지: Jetpack Compose와 함께 사용하면 더욱 유연하고 강력한 UI를 구축할 수 있습니다.

### 결론
Navigation Component는 안드로이드 앱의 네비게이션을 효율적으로 관리하고 개발 생산성을 향상시키는 데 큰 도움을 줍니다. 특히 Jetpack Compose와의 결합을 통해 더욱 강력한 기능을 제공합니다.

### 참고

- [Navigation](https://developer.android.com/guide/navigation)
- [Navigation Component 시작하기](https://developer.android.com/guide/navigation/navigation-getting-started)
- [Navigation Guide](https://developer.android.com/guide/navigation/navigation-navigate)
- [Safe Arguments](https://developer.android.com/guide/navigation/navigation-pass-data#Safe-args)

---

## Lifecycle
안드로이드 앱의 구성 요소(Activity, Fragment, Service 등)는 사용자의 상호 작용이나 시스템 이벤트에 따라 상태가 변화합니다. 이러한 상태 변화를 추적하고 관리하는 것을 **라이프사이클(Lifecycle)**이라고 합니다.

### 왜 라이프사이클을 이해해야 할까요?
- 자원 관리: 앱이 실행되는 동안 시스템 자원을 효율적으로 관리하기 위해서는 각 구성 요소의 상태에 따라 적절한 자원을 할당하고 해제해야 합니다.
- 데이터 유지: 사용자가 앱을 잠시 떠났다가 다시 돌아왔을 때, 이전 상태를 복원하기 위해서는 라이프사이클 이벤트를 활용해야 합니다.
- 예외 처리: 예상치 못한 상황(예: 화면 회전, 메모리 부족)이 발생했을 때, 앱이 안정적으로 동작하도록 하기 위해 라이프사이클 메서드를 오버라이드하여 처리해야 합니다.

### Activity 라이프사이클 예시
Activity는 가장 흔하게 사용되는 앱 구성 요소이며, 다음과 같은 주요 라이프사이클 메서드를 가지고 있습니다.
- onCreate(): Activity가 처음 생성될 때 호출됩니다. UI 초기화, 데이터 로딩 등을 수행합니다.
- onStart(): Activity가 사용자에게 보여지기 직전에 호출됩니다.
- onResume(): Activity가 사용자와 상호 작용할 준비가 완료되었을 때 호출됩니다.
- onPause(): 다른 Activity로 포커스가 이동할 때 호출됩니다.
- onStop(): Activity가 더 이상 사용자에게 보이지 않을 때 호출됩니다.
- onDestroy(): Activity가 완전히 파괴되기 직전에 호출됩니다.

### Fragment 라이프사이클
Fragment는 Activity 내에서 독립적인 UI를 제공하는 구성 요소입니다. Activity 라이프사이클과 유사한 메서드를 가지며, 추가적으로 Activity와의 상호 작용에 따른 메서드가 있습니다.

### Service 라이프사이클
Service는 백그라운드에서 오랫동안 실행되는 작업을 수행하는 구성 요소입니다. Activity와 달리 UI를 가지지 않으며, onStartCommand() 메서드를 통해 시작됩니다.

### 라이프사이클 관리
- onSaveInstanceState(): Activity가 비정상적으로 종료될 경우, 상태를 저장하기 위해 호출됩니다.
- onRestoreInstanceState(): 저장된 상태를 복원하기 위해 호출됩니다.
- ViewModel: UI와 데이터를 분리하여 상태를 관리하는 패턴입니다. LiveData를 사용하여 데이터의 변화를 관찰하고 UI를 업데이트할 수 있습니다.

### 라이프사이클 관련 중요 개념
- 백 스택: Activity가 이동할 때 시스템은 백 스택에 이전 Activity를 저장합니다. 사용자가 뒤로 가기 버튼을 누르면 백 스택에서 이전 Activity를 복원합니다.
- 프로세스: 안드로이드 시스템은 메모리 부족 상황에서 프로세스를 종료할 수 있습니다. 이 경우, Activity가 다시 생성될 때 이전 상태를 복원해야 합니다.

### 라이프사이클 관리의 중요성
- 메모리 누수 방지: 더 이상 사용하지 않는 객체를 해제하여 메모리 누수를 방지합니다.
- 안정적인 앱 구현: 예상치 못한 상황에 대비하여 앱이 안정적으로 동작하도록 합니다.
- 사용자 경험 향상: 매끄러운 화면 전환과 데이터 유지를 통해 사용자 경험을 향상시킵니다.

### 참고
- [Lifecycle](https://developer.android.com/reference/androidx/lifecycle/Lifecycle)
- [생명주기에 대해서 - 안드로이드 개발자 인민군](https://androidpangyo.tistory.com/52)
- [안드로이드 생명주기(Lifecycle) 가 뭐에요?](https://won-percent.tistory.com/145)

---

## LifecycleOwner
LifecycleOwner는 안드로이드 앱의 구성 요소(Activity, Fragment 등)의 생명 주기를 나타내는 인터페이스입니다. 
쉽게 말해, 어떤 컴포넌트가 현재 어떤 상태인지 (생성되었는지, 파괴되었는지 등)를 알려주는 역할을 합니다.

### 왜 LifecycleOwner가 필요할까요?
- LiveData와의 연동: LiveData는 LifecycleOwner와 함께 사용되어 데이터의 생명 주기를 관리합니다. 즉, LifecycleOwner의 상태에 따라 LiveData의 값이 업데이트되거나 관찰이 해제됩니다.
- 안전한 데이터 바인딩: 데이터 바인딩 시 LifecycleOwner를 사용하면 구성 요소가 파괴된 후에도 데이터가 업데이트되는 것을 방지하여 메모리 누수를 예방할 수 있습니다.
- MVVM 패턴: MVVM 패턴에서 ViewModel과 View를 연결하는 데 사용됩니다. ViewModel은 LiveData를 통해 데이터를 제공하고, View는 LifecycleOwner를 통해 LiveData를 관찰합니다.

### LifecycleOwner와 LiveData의 관계
LiveData는 데이터를 저장하고, 관찰자(Observer)에게 데이터 변경 사항을 알리는 객체입니다. LifecycleOwner는 LiveData가 어떤 객체를 관찰해야 하는지를 알려주는 역할을 합니다.

- LiveData.observe(LifecycleOwner, Observer): 이 메서드를 통해 LiveData는 LifecycleOwner의 생명 주기를 관찰하고, LifecycleOwner가 활성 상태일 때만 Observer에게 데이터를 전달합니다.
- LifecycleOwner가 파괴될 때: LiveData는 자동으로 Observer를 제거하여 메모리 누수를 방지합니다.

### LifecycleOwner와 viewLifecycleOwner의 차이점
- LifecycleOwner: Activity나 Fragment 전체의 생명 주기를 나타냅니다.
- viewLifecycleOwner: Fragment의 View의 생명 주기를 나타냅니다. Fragment의 View가 생성되고 파괴될 때 사용됩니다.

### 언제 LifecycleOwner를 사용해야 할까요?
- LiveData를 사용할 때: LiveData와 함께 사용하여 데이터를 안전하게 관리하고, 불필요한 메모리 누수를 방지해야 할 때 사용합니다.
- MVVM 패턴을 사용할 때: ViewModel과 View를 연결하여 데이터를 바인딩할 때 사용합니다.
- 뷰의 생명 주기를 관리해야 할 때: Fragment에서 View의 생명 주기를 관리해야 할 때 viewLifecycleOwner를 사용합니다.

### 예시 코드
```kotlin
class MyFragment : Fragment() {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val viewModel: MyViewModel = ViewModelProvider(this).get(MyViewModel::class.java)
        viewModel.data.observe(viewLifecycleOwner) { data ->
            // 데이터가 변경될 때 UI 업데이트
        }
    }
}
```

### 정리
LifecycleOwner는 안드로이드 앱의 구성 요소의 생명 주기를 나타내는 인터페이스이며, LiveData와 함께 사용하여 안전하고 효율적인 데이터 관리를 가능하게 합니다. 
특히 MVVM 패턴에서 중요한 역할을 하며, 메모리 누수를 방지하고 코드의 가독성을 높이는 데 기여합니다.

### 참고

- [LifecycleOwner](https://developer.android.com/reference/androidx/lifecycle/LifecycleOwner)

---

## LiveData vs ObservableField
안드로이드 앱 개발에서 데이터 바인딩을 사용할 때 자주 마주치는 LiveData와 ObservableField는 비슷한 목적을 가지고 있지만, 몇 가지 중요한 차이점이 있습니다.

### LiveData
- 생명 주기 인식: LiveData는 Android Architecture Components의 일부로, 컴포넌트(Activity, Fragment 등)의 생명 주기를 인식합니다. 즉, 컴포넌트가 파괴되면 자동으로 관찰을 중단하여 메모리 누수를 방지합니다.
- 데이터 변경 감지: 데이터가 변경될 때 자동으로 UI를 업데이트합니다.
- 백그라운드 스레드 지원: postValue() 메서드를 통해 백그라운드 스레드에서 안전하게 데이터를 업데이트할 수 있습니다.
- 다양한 기능: Transformations, MediatorLiveData 등 다양한 기능을 제공하여 복잡한 데이터 변환과 조합을 지원합니다.

### ObservableField
- 단순한 데이터 바인딩: 데이터 바인딩을 위한 기본적인 기능만 제공합니다.
- 생명 주기 인식 X: LiveData와 달리 컴포넌트의 생명 주기를 인식하지 않기 때문에 수동으로 구독을 해지해야 합니다.
- 백그라운드 스레드 지원 X: 백그라운드 스레드에서 데이터를 업데이트할 때 주의해야 합니다.

### 둘의 차이점 요약
특징 | LiveData | ObservableField
---|---|---
생명 주기 인식 | O | X
데이터 변경 감지 | O | O
백그라운드 스레드 지원 | O | X
기능 | 다양한 기능 제공 | 기본적인 기능만 제공
권장 | LiveData | LiveData

### 왜 LiveData를 더 많이 사용해야 할까요?
- 안전성: LiveData는 컴포넌트의 생명 주기를 인식하여 메모리 누수를 방지하고, 백그라운드 스레드에서 안전하게 데이터를 업데이트할 수 있습니다.
- 편의성: 다양한 기능을 제공하여 복잡한 데이터 처리를 쉽게 구현할 수 있습니다.
- 정형화된 패턴: MVVM 패턴에서 LiveData는 데이터를 관리하는 표준적인 방법으로 자리 잡았습니다.

### 언제 LiveData를 사용해야 할까요?
- UI를 데이터에 자동으로 바인딩해야 할 때
- 백그라운드 스레드에서 데이터를 업데이트하고 UI를 동기화해야 할 때
- 복잡한 데이터 변환과 조합이 필요할 때
- MVVM 패턴을 사용하여 앱을 개발할 때

### 언제 ObservableField를 사용할까요?
- 간단한 데이터 바인딩이 필요하고 생명 주기 관리를 직접 하고 싶을 때
- LiveData를 사용하기에는 오버헤드가 크다고 판단될 때

**요약하면,** LiveData는 안드로이드 앱 개발에서 데이터 바인딩을 위한 더 안전하고 효율적인 선택입니다.

### 결론
LiveData는 ObservableField보다 더 안전하고 편리하며, 현대적인 안드로이드 앱 개발에서 더 많이 사용되는 것이 좋습니다. ObservableField는 간단한 데이터 바인딩이 필요할 때 사용될 수 있지만, 대부분의 경우 LiveData를 사용하는 것이 권장됩니다.

### 언제 어떤것을 사용하는게 좋은가?
- 앱이 백그라운드에 있을 때 ViewModel에서 계단식 데이터 변경을 트리거할 수 있는 외부 이벤트가 있다면 LiveData 를 사용하는 것이 좋을것 같다.
- 즉시 백그라운드 스레드에 대한 값 업데이트가 필요 하다면 Observable 사용하는 것이 좋을것 같다.

### 참고

- https://medium.com/androiddevelopers/android-data-binding-library-from-observable-fields-to-livedata-in-two-steps-690a384218f2
- https://developer.android.com/topic/libraries/data-binding/architecture#observable-viewmodel
- https://developer.android.com/topic/libraries/data-binding/architecture#livedata