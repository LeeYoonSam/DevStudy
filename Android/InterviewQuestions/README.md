# 통합 안드로이드 인터뷰 질문 및 답변 (최종 정리)

---

## I. Android Fundamentals (기본 개념)

**1. 안드로이드 4대 컴포넌트**
안드로이드의 4대 컴포넌트를 모두 고르세요.
* ✓ Activity
* ✓ Service
* Fragment
* ✓ Content Provider
* ✓ Broadcast Receiver
* Intent

**2. AndroidManifest.xml 역할**
AndroidManifest.xml 파일의 역할에 대한 설명 중 올바른 것을 고르세요.
* ✓ 앱의 필수 정보(패키지 이름, 컴포넌트, 권한 등)를 시스템에 제공한다.
* 앱의 UI 레이아웃을 정의한다.
* 앱의 비즈니스 로직을 포함한다.
* 앱의 리소스(문자열, 이미지 등)를 저장한다.

**3. 안드로이드 리소스 참조**
안드로이드에서 리소스를 참조하는 올바른 방법을 고르세요.
* ✓ XML에서는 @string/app_name, 코드에서는 R.string.app_name
* XML에서는 R.string.app_name, 코드에서는 @string/app_name
* XML에서는 @string.app_name, 코드에서는 R.string/app_name
* XML에서는 #string/app_name, 코드에서는 Resources.string.app_name

**4. Intent의 종류**
Intent의 종류에 관한 설명 중 올바른 것을 고르세요.
* ✓ 명시적 인텐트는 특정 컴포넌트를 직접 지정하고, 암시적 인텐트는 작업을 지정하여 시스템이 적절한 컴포넌트를 찾도록 한다.
* 명시적 인텐트는 작업을 지정하고, 암시적 인텐트는 특정 컴포넌트를 지정한다.
* 명시적 인텐트와 암시적 인텐트는 동일한 방식으로 작동한다.
* 암시적 인텐트는 항상 명시적 인텐트보다 빠르다.

**5. Intent와 Intent Filter**
Intent와 Intent Filter에 관한 설명 중 올바른 것을 모두 고르세요.
* ✓ 명시적 인텐트는 특정 컴포넌트를 직접 지정한다.
* ✓ 암시적 인텐트는 수행할 작업을 지정하며, 시스템이 적절한 컴포넌트를 찾는다.
* Intent Filter는 항상 AndroidManifest.xml이 아닌 코드에서 동적으로 등록해야 한다.
* ✓ Intent Filter는 암시적 인텐트를 수신할 수 있는 컴포넌트의 기능을 설명한다. (Action, Data, Category)
* 하나의 Intent는 여러 개의 Action을 가질 수 있다. (하나의 Action만 가능)

**6. Service 종류**
Service의 종류에 관한 설명 중 올바른 것을 모두 고르세요.
* ✓ Foreground Service는 사용자가 인지할 수 있는 작업을 수행하며, 상태 표시줄에 알림을 표시해야 한다.
* Background Service는 Android 8.0 (API 26)부터 제한 없이 자유롭게 실행될 수 있다.
* ✓ Bound Service는 다른 컴포넌트가 바인딩하여 상호작용할 수 있는 인터페이스를 제공하며, 모든 클라이언트가 언바인드되면 소멸될 수 있다.
* IntentService는 여러 작업을 동시에 병렬로 처리한다. (순차적으로 처리)
* ✓ Started Service는 `startService()`로 시작되며, 자신을 시작한 컴포넌트와 독립적으로 실행될 수 있다.

**7. BroadcastReceiver Types (브로드캐스트 리시버 종류)**
What is the key difference between a normal broadcast and an ordered broadcast? Select one.
* Normal broadcasts are asynchronous; ordered broadcasts are synchronous.
* ✓ Only ordered broadcasts can be aborted by a receiver.
* Normal broadcasts require a specific permission; ordered broadcasts do not.
* Ordered broadcasts can only be sent using explicit Intents.

**8. ContentProvider Permissions (콘텐츠 프로바이더 권한)**
How can you grant temporary access to data in your ContentProvider to another app without requiring a permanent permission declaration in its manifest? Select one.
* ✓ By using the `android:grantUriPermissions="true"` attribute in the `<provider>` tag and setting `FLAG_GRANT_READ_URI_PERMISSION` or `FLAG_GRANT_WRITE_URI_PERMISSION` on the Intent.
* By defining a custom permission with `protectionLevel="signature"`.
* By sending the data via a BroadcastReceiver instead.
* By requiring the client app to use `ContentResolver.openFileDescriptor()` with a special mode.

**9. Non-App Components (앱 컴포넌트가 아닌 것)**
Which of the following are NOT Android app components? Select all that apply.
* ✓ Fragment
* ✓ Intent
* ✓ View
* Service
* Activity
* Content Provider

**10. Context Types (컨텍스트 종류)**
Which statement accurately describes the difference between Application Context and Activity Context? Select one.
* Application Context is tied to the Activity lifecycle, while Activity Context is tied to the Application lifecycle.
* Activity Context should be used for operations lasting beyond the Activity's scope, like initializing singletons.
* Application Context provides access to UI-related resources like themes and dialogs.
* ✓ Application Context lives as long as the application process, while Activity Context is destroyed when the Activity is destroyed.

**11. Android Runtime (ART) (안드로이드 런타임)**
Which statements accurately describe the Android Runtime (ART) compared to the older Dalvik runtime? Select all that apply.
* ✓ ART uses Ahead-Of-Time (AOT) compilation during app installation, translating bytecode into native machine code.
* ✓ Dalvik used Just-In-Time (JIT) compilation, compiling code as it was executed.
* ✓ ART generally leads to faster app startup times and improved performance compared to Dalvik.
* ART uses less storage space for compiled code than Dalvik's optimized DEX files. (AOT typically results in larger code size on disk)
* Garbage collection mechanisms are identical in both ART and Dalvik. (ART has improved GC)

**12. Parcelable**
Parcelable 인터페이스에 대한 설명 중 올바른 것을 모두 고르세요.
* Parcelable은 데이터를 JSON으로 직렬화하는 데 사용된다.
* ✓ Parcelable은 Java 객체를 마샬링(marshalling)하고 언마샬링(unmarshalling)하여 IPC(프로세스 간 통신)를 통해 전송할 수 있도록 설계된 안드로이드 특정 인터페이스이다.
* ✓ Serializable보다 성능이 우수하다.
* Parcelable 구현은 리플렉션을 사용한다. (리플렉션 사용 안함)
* ✓ `writeToParcel`과 `CREATOR` 필드를 구현해야 한다.

**13. Accessibility Services (접근성 서비스)**
How do Accessibility Services interact with an Android application's UI? Select one.
* By directly manipulating the View objects in the app's layout.
* ✓ By receiving information about UI events and window content (if the app provides it via AccessibilityNodeInfo) and potentially performing actions on behalf of the user.
* By reading the application's source code.
* By intercepting all touch events before they reach the application.

---

## II. Activity & Fragment Lifecycles (액티비티 & 프래그먼트 생명주기)

**14. Activity 생명주기**
Activity 생명주기 메소드의 올바른 순서를 고르세요.
* ✓ onCreate() → onStart() → onResume() → onPause() → onStop() → onDestroy()
* onCreate() → onStart() → onPause() → onResume() → onStop() → onDestroy()
* onCreate() → onResume() → onStart() → onPause() → onDestroy() → onStop()
* onCreate() → onStart() → onResume() → onStop() → onPause() → onDestroy()

**15. Fragment 생명주기**
Fragment가 Activity에 추가될 때 호출되는 생명주기 메소드의 올바른 순서를 고르세요.
* ✓ onAttach() → onCreate() → onCreateView() → onViewCreated() → onStart() → onResume()
* onCreate() → onAttach() → onCreateView() → onStart() → onResume() → onViewCreated()
* onAttach() → onCreateView() → onCreate() → onResume() → onStart() → onViewCreated()
* onCreateView() → onAttach() → onCreate() → onViewCreated() → onStart() → onResume()

**16. Fragment 생명주기 순서 확인**
Fragment 생명주기 메소드의 호출 순서가 올바른 것을 고르세요. (Activity에 추가되고 화면에 보이는 과정)
* onAttach() -> onCreate() -> onCreateView() -> onStart() -> onResume() -> onViewCreated()
* ✓ onAttach() -> onCreate() -> onCreateView() -> onViewCreated() -> onStart() -> onResume()
* onCreate() -> onAttach() -> onCreateView() -> onViewCreated() -> onStart() -> onResume()
* onCreateView() -> onViewCreated() -> onAttach() -> onCreate() -> onStart() -> onResume()

**17. Activity Launch Modes (액티비티 실행 모드)**
Which of the following Activity launch modes is correctly described? Select one.
* Standard: Always creates a new instance of the activity.
* ✓ SingleTop: Creates a new instance unless one already exists at the top of the stack.
* SingleTask: Creates multiple instances within the same task. (Creates a new task or brings existing task to front)
* SingleInstance: Allows other activities to be part of its task. (Only activity in its task)

---

## III. UI & Jetpack Compose (UI 및 Jetpack Compose)

**18. UI 스레드 (메인 스레드)**
안드로이드 UI 스레드에 관한 설명 중 올바른 것을 고르세요.
* ✓ UI 스레드는 모든 UI 업데이트와 사용자 상호작용을 처리한다.
* 네트워크 요청과 같은 시간이 오래 걸리는 작업은 UI 스레드에서 수행해야 한다.
* 모든 안드로이드 앱은 여러 개의 UI 스레드를 가진다.
* UI 스레드에서의 작업은 ANR(Application Not Responding)을 절대 발생시키지 않는다.

**19. 레이아웃 종류**
안드로이드에서 사용되는 기본적인 레이아웃 유형을 모두 고르세요.
* ✓ LinearLayout
* ✓ RelativeLayout
* ✓ FrameLayout
* ✓ ConstraintLayout
* GridLayout (TableLayout, GridLayout 등이 있지만, 기본적인 것 위주로)
* ViewPagerLayout

**20. RecyclerView 기초**
RecyclerView에 관한 설명 중 올바른 것을 모두 고르세요.
* ✓ RecyclerView는 대규모 데이터 세트를 효율적으로 표시하기 위한 유연한 뷰이다.
* ✓ ViewHolder 패턴을 사용하여 뷰 스크롤 성능을 향상시킨다.
* RecyclerView는 LinearLayoutManager만 사용할 수 있다.
* ✓ LayoutManager는 아이템 뷰의 배치 방식을 결정한다.
* ✓ Adapter는 데이터 세트를 ViewHolder에 바인딩한다.

**21. RecyclerView vs ListView**
RecyclerView와 ListView에 대한 설명 중 올바른 것을 모두 고르세요.
* ✓ RecyclerView는 ViewHolder 패턴 사용을 강제하여 성능을 개선한다.
* ListView는 아이템 뷰의 레이아웃을 더 유연하게 관리할 수 있다. (LayoutManager 덕분에 RecyclerView가 더 유연함)
* ✓ RecyclerView는 아이템 추가/삭제 애니메이션을 쉽게 구현할 수 있다.
* ListView는 항상 RecyclerView보다 메모리를 적게 사용한다.
* ✓ RecyclerView는 LayoutManager를 통해 다양한 레이아웃(리스트, 그리드, 스태거드)을 지원한다.

**22. RecyclerView LayoutManagers (리사이클러뷰 레이아웃 매니저)**
Besides `LinearLayoutManager`, what other built-in LayoutManagers does RecyclerView provide for arranging items? Select all that apply.
* `TableLayoutManager`
* ✓ `GridLayoutManager`
* ✓ `StaggeredGridLayoutManager`
* ✓ `FlexboxLayoutManager` (available as a separate library, but commonly associated)
* `ConstraintLayoutManager`

**23. Data Binding**
데이터 바인딩에 관한 설명 중 올바른 것을 모두 고르세요.
* ✓ 데이터 바인딩은 XML 레이아웃 파일에서 직접 데이터를 참조할 수 있게 한다.
* ✓ 데이터 바인딩은 `findViewById()` 호출을 제거하여 보일러플레이트 코드를 감소시킨다.
* 데이터 바인딩은 항상 ViewModel과 함께 사용해야만 한다.
* ✓ 양방향 데이터 바인딩은 `@={}` 구문을 사용한다.
* 데이터 바인딩은 컴파일 시간에 레이아웃 오류를 감지하지 못한다. (컴파일 시간 감지)

**24. View Binding vs Data Binding**
Which statements about View Binding and Data Binding are correct? Select all that apply.
* ✓ View Binding generates binding classes with direct references to views.
* ✓ Data Binding supports expressions in layout files.
* View Binding requires manual configuration in build.gradle. (Enabled in module's build.gradle, but less config than Data Binding)
* ✓ Data Binding allows observing LiveData directly in XML.
* View Binding provides better performance than findViewById() but worse than Data Binding. (Both eliminate findViewById; performance difference is negligible for most cases, Data Binding has slightly more overhead due to expression evaluation)

**25. Custom View 성능 최적화**
Custom View의 성능 최적화 방법에 대한 설명 중 올바른 것을 모두 고르세요.
* ✓ `onDraw` 메소드 내에서 객체 생성을 피한다.
* ✓ `invalidate()` 호출 횟수를 최소화한다.
* ✓ 복잡한 계산은 `onDraw` 외부에서 미리 수행한다.
* 모든 Custom View는 하드웨어 가속을 비활성화해야 한다.
* ✓ `clipRect()`를 사용하여 불필요한 영역의 그리기를 방지할 수 있다.

**26. Jetpack Compose 기초**
Jetpack Compose에 관한 설명 중 올바른 것을 모두 고르세요.
* ✓ Jetpack Compose는 선언적 UI 툴킷이다.
* ✓ Compose는 상태 변경에 따라 UI를 자동으로 업데이트한다.
* Compose를 사용하려면 기존 XML 레이아웃을 모두 마이그레이션해야 한다.
* ✓ `@Composable` 함수는 UI 요소를 생성하는 함수이다.
* Compose는 Android API 레벨 30 이상에서만 사용할 수 있다. (API 21 이상 지원)

**27. Compose 최적화**
Jetpack Compose 최적화에 관한 설명 중 올바른 것을 모두 고르세요.
* ✓ `remember`와 `derivedStateOf`를 사용하여 불필요한 재구성을 방지할 수 있다.
* ✓ `CompositionLocal`은 컴포저블 트리 전체에 데이터를 전달하는 데 사용될 수 있다.
* ✓ `key` 파라미터를 사용하여 컴포저블의 상태를 보존할 수 있다.
* LazyColumn은 항상 RecyclerView보다 성능이 우수하다.
* Compose는 XML 레이아웃보다 항상 더 빠르게 렌더링된다.

**28. Compose State Management (컴포즈 상태 관리)**
Which techniques are used for state management in Jetpack Compose? Select all that apply.
* ✓ `remember` / `rememberSaveable`
* ✓ State hoisting
* ✓ Using ViewModel with LiveData or StateFlow
* Direct manipulation of Composable functions
* ✓ `derivedStateOf`

**29. Compose Side Effects (컴포즈 부수 효과)**
In Jetpack Compose, what is the purpose of side-effect handlers like `LaunchedEffect`, `DisposableEffect`, and `SideEffect`? Select one.
* To perform UI updates outside the scope of a composable function.
* To manage state that needs to survive configuration changes.
* ✓ To execute non-composable code (like launching coroutines, registering listeners, or interacting with non-Compose systems) in response to lifecycle events or state changes within a composition.
* To define the layout structure of composable elements.

**30. Compose Modifiers (컴포즈 Modifier)**
What is the role of `Modifier` in Jetpack Compose? Select one.
* To manage the state of a composable function.
* To define the data type used by a composable.
* ✓ To decorate or add behavior to a composable element, such as size, padding, background, click handling, etc.
* To trigger recomposition when data changes.

**31. CompositionLocal Usage (CompositionLocal 사용)**
When would you typically use `CompositionLocal` in Jetpack Compose? Select the best description.
* To pass simple data like a String or Int between adjacent composables.
* ✓ To provide implicit dependencies (like themes, locales, or service locators) down the composable tree without passing them explicitly through every function.
* To manage mutable state that frequently changes within a single composable.
* To trigger side effects when a composable enters the composition.

**32. Material Design Theming (머티리얼 디자인 테마)**
Which components typically define a Material Design theme in an Android app (using either XML themes or Compose Theming)? Select all that apply.
* ✓ Color palette (primary, secondary, surface, error colors, etc.)
* ✓ Typography scales (headline, subtitle, body text styles, etc.)
* ✓ Shape system (corner rounding styles for components)
* Icon packs
* Animation timings

**33. CameraX Library (CameraX 라이브러리)**
What are the main advantages of using the Jetpack CameraX library over the older Camera1 or Camera2 APIs? Select all that apply.
* ✓ It provides a simpler, use-case-based API (Preview, Image Analysis, Image Capture).
* ✓ It handles device-specific compatibility issues automatically.
* It offers built-in support for QR code scanning. (Extensions exist, but not core)
* It guarantees higher image resolution than Camera2.
* ✓ It integrates easily with lifecycle awareness.

---

## IV. Background Processing & Concurrency (백그라운드 처리 및 동시성)

**34. Coroutines 기초**
Kotlin Coroutines에 관한 설명 중 올바른 것을 모두 고르세요.
* ✓ 코루틴은 비동기 코드를 단순화하기 위한 기능이다.
* 코루틴은 항상 새로운 스레드를 생성한다.
* ✓ `suspend` 함수는 코루틴 내에서 호출될 수 있으며, 실행을 일시 중단할 수 있다.
* ✓ `viewModelScope`는 ViewModel의 생명주기와 연결된 코루틴 스코프이다.
* 코루틴은 콜백 지옥(Callback Hell) 문제를 해결할 수 없다.

**35. Kotlin Coroutine Dispatchers (코루틴 디스패처)**
Which of the following Coroutine Dispatchers are provided by Kotlin? Select all that apply.
* ✓ Dispatchers.Main
* ✓ Dispatchers.IO
* Dispatchers.Network
* ✓ Dispatchers.Default
* ✓ Dispatchers.Unconfined
* Dispatchers.Background

**36. Kotlin Coroutines 스코프**
코루틴 스코프에 관한 설명 중 올바른 것을 모두 고르세요.
* ✓ `viewModelScope`는 ViewModel의 수명 주기와 연결된 코루틴 스코프이다.
* ✓ `lifecycleScope`는 Lifecycle 객체의 수명 주기와 연결된 코루틴 스코프이다.
* GlobalScope는 메모리 누수 걱정 없이 항상 안전하게 사용할 수 있다.
* ✓ `coroutineScope` 함수는 모든 자식 코루틴이 완료될 때까지 기다린다.
* suspend 함수 내에서는 코루틴 스코프를 사용할 수 없다.

**37. Kotlin Flow 기초**
Kotlin Flow에 관한 설명 중 올바른 것을 모두 고르세요.
* ✓ Flow는 비동기적으로 계산된 여러 값을 순차적으로 내보내는 데이터 스트림이다.
* ✓ Flow는 코루틴을 기반으로 구축되었다.
* Flow는 RxJava와 달리 연산자 체인을 지원하지 않는다.
* ✓ StateFlow는 항상 값을 가지며 현재 상태와 업데이트를 관찰할 수 있다.
* Flow 수집은 메인 스레드에서만 가능하다. (Dispatcher 변경 가능)

**38. RxJava vs Flow**
RxJava와 Kotlin Flow의 비교 설명 중 올바른 것을 모두 고르세요.
* ✓ Flow는 코루틴을 기반으로 하며, 구조화된 동시성을 지원한다.
* RxJava는 Kotlin으로만 작성된 프로젝트에서만 사용할 수 있다.
* ✓ Flow는 Cold Stream이 기본이며, Backpressure를 자연스럽게 지원한다. (RxJava도 지원)
* ✓ Flow는 LifecycleScope, viewModelScope 등 안드로이드 생명주기와 통합이 더 용이하다.
* RxJava는 Flow보다 연산자(Operator) 종류가 훨씬 적다. (RxJava가 훨씬 많음)

**39. Coroutine vs RxJava 비교**
Kotlin Coroutine과 RxJava에 대한 설명 중 올바른 것을 모두 고르세요.
* ✓ 둘 다 비동기 프로그래밍을 위한 라이브러리이다.
* Coroutine은 스레드를 전혀 사용하지 않는다.
* ✓ Coroutine은 `suspend` 함수를 사용하여 비동기 코드를 동기 코드처럼 작성할 수 있게 한다.
* RxJava는 Kotlin 언어에서만 사용할 수 있다.
* ✓ Flow는 Coroutine 기반의 리액티브 스트림 구현이다.

**40. Kotlin Channels vs Flow**
While both Kotlin Channels and Flows deal with streams of data, what is a key difference in their typical usage or nature? Select one.
* Flows are "hot" (produce data regardless of collectors), while Channels are "cold" (produce data only when collected). (Flows are typically cold, Channels can be hot or cold depending on implementation)
* ✓ Channels are well-suited for communication *between* coroutines (often acting like a queue), while Flows are typically used for emitting a stream of data *from* a source (like a producer) to a collector.
* Flows require explicit cancellation, while Channels are tied to the coroutine scope.
* Channels cannot handle backpressure, while Flows can. (Both can handle backpressure, Flow has more built-in operators for it)

**41. StateFlow vs SharedFlow**
What is the key difference between `StateFlow` and `SharedFlow` in Kotlin Coroutines? Select the best description.
* `StateFlow` is hot, `SharedFlow` is cold.
* `SharedFlow` always replays the last emitted value to new collectors, while `StateFlow` does not.
* ✓ `StateFlow` always holds a current value and only emits distinct updates, making it suitable for representing observable state. `SharedFlow` is more general, allowing configurable replay cache and emission strategies, suitable for events.
* `StateFlow` can only have one collector, while `SharedFlow` supports multiple collectors.

**42. WorkManager 기초**
WorkManager에 관한 설명 중 올바른 것을 모두 고르세요.
* ✓ WorkManager는 지연 가능하고 보장된 백그라운드 작업을 위한 라이브러리이다.
* WorkManager는 즉시 실행되어야 하는 짧은 작업을 처리하는 데 가장 적합하다.
* ✓ WorkManager는 제약 조건(예: 네트워크 연결 상태, 충전 상태)에 따라 작업을 실행할 수 있다.
* ✓ 앱이 종료되거나 기기가 재부팅되어도 작업 실행을 보장한다.
* WorkManager는 Android 5.0 (API 21) 이전 버전에서는 사용할 수 없다. (API 14부터 호환성 제공)

**43. WorkManager 요청 타입**
WorkManager에 관한 설명 중 올바른 것을 모두 고르세요.
* ✓ WorkManager는 지연 가능한 백그라운드 작업을 위한 API이다.
* ✓ WorkManager는 앱이 종료되거나 시스템이 재부팅되어도 작업이 실행되도록 보장한다.
* WorkManager는 즉시 실행되어야 하는 포그라운드 서비스를 대체한다.
* ✓ OneTimeWorkRequest와 PeriodicWorkRequest 두 가지 유형의 작업 요청이 있다.
* WorkManager는 API 레벨 21 이상에서만 사용할 수 있다. (API 14부터 호환성 제공)

**44. Service 기초**
Service에 대한 설명 중 올바른 것을 모두 고르세요.
* ✓ Service는 사용자 인터페이스 없이 백그라운드에서 오래 실행되는 작업을 수행하는 컴포넌트이다.
* ✓ `startService()`로 시작된 서비스는 자신을 시작한 컴포넌트가 종료되어도 계속 실행될 수 있다.
* 모든 서비스는 항상 별도의 스레드에서 실행된다. (기본은 메인 스레드)
* ✓ foreground Service는 사용자에게 알림을 표시해야 한다.
* 바인드된 서비스는 Android 8.0(API 레벨 26) 이상에서 백그라운드 제한의 영향을 받지 않는다. (영향을 받음, 포그라운드로 전환 필요할 수 있음)

**45. Bound Services Lifecycle (바인드 서비스 생명주기)**
Which lifecycle methods are specific to a Bound Service and are called when clients bind and unbind? Select two.
* `onCreate()`
* `onStartCommand()`
* ✓ `onBind()`
* ✓ `onUnbind()`
* `onDestroy()`

**46. Service IPC (AIDL)**
Which of the following are characteristics of using AIDL (Android Interface Definition Language) for Inter-Process Communication (IPC)? Select all that apply.
* ✓ It automatically handles marshalling and unmarshalling of primitive types and Parcelable objects across processes.
* AIDL interfaces run on the main thread by default in the client process. (Calls are synchronous and block the client's thread, which might be the main thread)
* It's primarily used for communication *within* the same application process.
* ✓ It allows defining a programming interface that both the client and service agree upon for IPC.
* AIDL is simpler to implement than using a Messenger. (Generally more complex than Messenger for simple cases)

**47. Handler and Looper (핸들러와 루퍼)**
Which statement best describes the relationship between Handler, Looper, and MessageQueue? Select one.
* A Handler sends messages to a Looper, which stores them in its own internal queue.
* ✓ A Looper continuously checks its associated MessageQueue for new Messages and dispatches them to the target Handler for processing on the Looper's thread.
* A MessageQueue creates Loopers for different threads, and Handlers pull messages directly from the queue.
* Each Handler has its own Looper and MessageQueue to manage asynchronous tasks.

**48. Background Execution Limits (백그라운드 실행 제한)**
Starting from Android 8.0 (Oreo), what are the main restrictions placed on background services? Select all that apply.
* ✓ Services started via `startService()` when the app is in the background have a limited execution window before being stopped.
* ✓ Apps can no longer register implicit broadcast receivers in the Manifest (with some exceptions).
* ✓ Background apps have reduced access to location updates.
* ✓ `WorkManager` is the recommended solution for guaranteed background execution.
* ✓ Foreground services are unaffected by these limits (but require a notification).

---

## V. Data Storage & Persistence (데이터 저장 및 영속성)

**49. SharedPreferences**
SharedPreferences에 관한 설명 중 올바른 것을 고르세요.
* ✓ SharedPreferences는 키-값 쌍으로 간단한 데이터를 저장하는 데 사용된다.
* SharedPreferences는 대용량 데이터나 구조화된 데이터를 저장하는 데 적합하다.
* SharedPreferences는 앱이 삭제되어도 데이터가 유지된다.
* SharedPreferences는 기본적으로 비동기적으로 작동한다. (apply는 비동기, commit은 동기)

**50. Room Persistence Library 기초**
Room 라이브러리에 관한 설명 중 올바른 것을 모두 고르세요.
* ✓ Room은 SQLite 데이터베이스 위에 추상화 계층을 제공하여 데이터베이스 액세스를 용이하게 한다.
* ✓ `@Entity` 어노테이션은 데이터베이스 테이블로 매핑될 클래스를 정의한다.
* ✓ `@Dao` (Data Access Object)는 데이터베이스에 접근하는 메소드를 포함하는 인터페이스 또는 추상 클래스이다.
* Room은 기본적으로 메인 스레드에서 데이터베이스 쿼리를 허용한다. (허용 안함, 에러 발생)
* ✓ 컴파일 타임에 SQL 쿼리의 유효성을 검사한다.

**51. Room Database 기능**
Room 데이터베이스에 관한 설명 중 올바른 것을 모두 고르세요.
* ✓ Room은 SQLite 데이터베이스 위에 추상화 계층을 제공한다.
* ✓ `@Entity` 어노테이션은 데이터베이스 테이블로 매핑될 클래스를 정의한다.
* Room은 메인 스레드에서 데이터베이스 작업을 기본적으로 허용한다. (허용 안함, 에러 발생)
* ✓ LiveData 또는 Flow와 함께 사용하면 데이터베이스 변경 사항을 UI에 자동으로 반영할 수 있다.
* Room은 복잡한 조인 쿼리를 지원하지 않는다. (지원함)

**52. DataStore**
Jetpack DataStore에 관한 설명 중 올바른 것을 모두 고르세요.
* ✓ DataStore는 SharedPreferences를 개선한 데이터 저장 솔루션이다.
* DataStore는 관계형 데이터를 저장하는 데 사용된다.
* ✓ Proto DataStore는 프로토콜 버퍼를 사용하여 타입-세이프하게 데이터를 저장한다.
* ✓ Preferences DataStore는 키-값 쌍으로 데이터를 저장하며, SharedPreferences와 유사하지만 비동기 API(Flow)를 제공한다.
* DataStore는 메인 스레드에서 동기적으로 데이터를 읽을 수 있다. (비동기 API 사용 권장)

**53. Paging 3 Library (`PagingSource`)**
What is the role of the `PagingSource` component in the Jetpack Paging 3 library? Select one.
* It caches data loaded from the network or database in memory.
* ✓ It defines how to load pages (snapshots) of data from a single source (network, database, etc.).
* It connects the UI (RecyclerView) to the Paging data stream.
* It handles UI state related to loading, errors, and empty lists.

**54. Android Architecture Components (데이터 관련)**
Which of the following are part of Android Architecture Components related to data? Select all that apply.
* ✓ LiveData
* ✓ ViewModel (데이터 홀더 역할)
* AndroidX
* ✓ Room
* ✓ Paging
* Picasso

**55. ViewModel과 LiveData**
ViewModel과 LiveData에 관한 설명 중 올바른 것을 모두 고르세요.
* ✓ ViewModel은 UI 관련 데이터를 저장하고 관리하며, 구성 변경(예: 화면 회전)에도 데이터를 유지한다.
* LiveData는 모든 종류의 데이터를 저장할 수 있는 일반적인 데이터 홀더 클래스이다.
* ✓ LiveData는 관찰 가능한(Observable) 데이터 홀더 클래스로, 생명주기를 인지한다.
* ViewModel은 백그라운드 스레드에서 실행되도록 설계되었다.
* ✓ LiveData는 액티비티나 프래그먼트가 활성 상태일 때만 관찰자에게 업데이트를 알린다.

---

## VI. Architecture & Dependency Injection (아키텍처 및 의존성 주입)

**56. ViewModel 기초**
ViewModel에 관한 설명 중 올바른 것을 모두 고르세요.
* ✓ ViewModel은 UI 관련 데이터를 저장하고 관리하도록 설계되었다.
* ✓ ViewModel은 구성 변경(예: 화면 회전)에도 데이터를 유지한다.
* ViewModel은 Activity나 Fragment의 Context를 직접 참조해서는 안 된다. (메모리 누수 유발 가능, 단 ApplicationContext는 `AndroidViewModel` 통해 가능)
* ✓ `onCleared()` 메소드는 ViewModel이 더 이상 사용되지 않을 때 호출된다.
* ViewModel은 항상 Hilt나 Dagger와 같은 DI 프레임워크와 함께 사용해야 한다.

**57. ViewModels and SavedStateHandle (ViewModel과 SavedStateHandle)**
How does `SavedStateHandle` enhance `ViewModel`? Select one.
* It allows ViewModels to directly access SharedPreferences.
* ✓ It provides a mechanism for the ViewModel to save and restore instance state, surviving not just configuration changes but also process death.
* It automatically saves the entire ViewModel state to disk.
* It enables ViewModels to observe database changes directly.

**58. 의존성 주입 (Dependency Injection) 기초**
의존성 주입(DI)에 관한 설명 중 올바른 것을 고르세요.
* ✓ 의존성 주입은 객체가 필요로 하는 의존성(다른 객체)을 외부에서 제공하는 디자인 패턴이다.
* 의존성 주입은 코드의 결합도(Coupling)를 높인다.
* ✓ Hilt, Dagger 등은 안드로이드에서 널리 사용되는 의존성 주입 라이브러리이다.
* ✓ 의존성 주입은 코드의 테스트 용이성을 향상시킨다.
* 의존성 주입은 앱의 성능을 항상 저하시킨다.

**59. 의존성 주입 (DI) 특징**
의존성 주입(DI)에 대한 설명 중 올바른 것을 모두 고르세요.
* ✓ DI는 객체가 의존하는 다른 객체를 외부에서 주입하는 디자인 패턴이다.
* ✓ DI는 클래스 간의 결합도를 낮춘다.
* ✓ DI는 코드의 테스트 용이성을 향상시킨다.
* Hilt는 Dagger보다 설정이 더 복잡하다. (Hilt가 더 간단함)
* ✓ `@Inject` 어노테이션은 의존성 주입을 요청하는 데 사용될 수 있다. (생성자, 필드, 메소드 주입)

**60. Hilt DI (Hilt 의존성 주입)**
Which Hilt annotations are fundamental for setting up dependency injection in an Android app? Select all that apply.
* ✓ `@HiltAndroidApp` (on Application class)
* ✓ `@AndroidEntryPoint` (on Activity/Fragment/etc.)
* ✓ `@Module` (to provide bindings)
* ✓ `@Provides` (within a Module to provide instances)
* `@Singleton` (Scope annotation)

**61. Hilt Scopes (Hilt 스코프)**
What is the purpose of scope annotations like `@Singleton`, `@ActivityScoped`, `@ViewModelScoped` in Hilt? Select the best description.
* To specify the visibility of the provided dependency (public or private).
* To group related dependencies together within a module.
* ✓ To control the lifecycle and reuse of injected instances within a specific Hilt component (e.g., `@Singleton` lives as long as the application, `@ActivityScoped` lives as long as the Activity).
* To mark dependencies that should be provided asynchronously.

**62. 아키텍처 패턴 (MVVM, MVI)**
MVVM(Model-View-ViewModel)과 MVI(Model-View-Intent) 아키텍처 패턴에 대한 설명 중 올바른 것을 모두 고르세요.
* ✓ MVVM은 데이터 바인딩이나 LiveData/StateFlow 관찰을 통해 View와 ViewModel 간의 결합도를 낮춘다.
* ✓ MVI는 View에서 발생하는 모든 사용자 상호작용을 Intent(의도)로 변환하여 처리한다.
* ✓ MVI는 상태(State)를 단일 객체로 관리하여 상태 변화를 예측 가능하게 만드는 데 중점을 둔다.
* ✓ MVVM의 ViewModel은 View의 상태와 비즈니스 로직을 처리한다.
* MVVM은 모든 안드로이드 앱에 항상 가장 적합한 아키텍처 패턴이다.
* 아키텍처 패턴은 앱 성능에 영향을 미치지 않는다. (잘못된 구조는 성능 저하 유발 가능)

**63. MVVM 아키텍처 패턴**
MVVM(Model-View-ViewModel) 아키텍처 패턴에 관한 설명 중 올바른 것을 모두 고르세요.
* ✓ View는 ViewModel을 관찰하고 UI를 업데이트한다.
* ViewModel은 View를 직접 참조한다. (참조하지 않음)
* ✓ ViewModel은 Model로부터 데이터를 가져오거나 조작한다.
* ✓ 데이터 바인딩 또는 StateFlow/LiveData는 MVVM 패턴 구현을 용이하게 한다.
* ✓ Model은 비즈니스 로직과 데이터를 포함한다. (Data Layer + Domain Layer일 수 있음)

**64. Clean Architecture**
Clean Architecture에 대한 설명 중 올바른 것을 모두 고르세요.
* ✓ 계층(Layer) 분리를 통해 관심사를 분리하는 것을 목표로 한다. (Presentation, Domain, Data)
* 데이터 흐름은 항상 외부 계층에서 내부 계층으로만 향한다. (의존성 규칙: 내부->외부 안됨)
* ✓ Domain 계층은 앱의 핵심 비즈니스 로직(Use Cases, Entities)을 포함하며, 다른 프레임워크(Android SDK 등)에 의존하지 않는다.
* Presentation 계층은 Data 계층을 직접 참조한다. (Domain 계층 통해 참조)
* ✓ 의존성 규칙(Dependency Rule)은 내부 계층이 외부 계층에 대해 알지 못하도록 강제한다.

**65. Navigation Component 기초**
Navigation Component에 관한 설명 중 올바른 것을 모두 고르세요.
* ✓ Navigation Component는 앱 내의 화면 간 이동(Navigation)을 단순화하는 라이브러리이다.
* ✓ Navigation Graph는 앱의 네비게이션 구조를 시각적으로 표현하는 XML 파일이다.
* Navigation Component는 Fragment 간의 이동만 지원한다. (Activity, Dialog 등도 지원)
* ✓ Safe Args Gradle 플러그인을 사용하여 대상 간에 타입-세이프(Type-safe)하게 데이터를 전달할 수 있다.
* Navigation Component를 사용하면 `startActivityForResult`를 더 이상 사용할 필요가 없다. (대체 가능하지만, 완전히 불필요한 것은 아님)

**66. Navigation Component 기능**
Navigation Component에 관한 설명 중 올바른 것을 모두 고르세요.
* ✓ Navigation Component는 프래그먼트 간의 전환을 처리하는 라이브러리이다.
* ✓ Navigation Graph는 앱의 모든 대상과 작업을 시각적으로 표현한다.
* Navigation Component는 Activity 간의 전환은 지원하지 않는다. (지원함)
* ✓ SafeArgs 플러그인은 유형 안전 탐색과 인수 전달을 지원한다.
* Navigation Component는 모든 종류의 애니메이션 효과를 자동으로 제공한다. (기본 제공 및 커스텀 가능)

**67. 멀티 모듈 아키텍처**
멀티 모듈 아키텍처의 장점으로 올바른 것을 모두 고르세요.
* ✓ 빌드 시간 단축 (변경된 모듈만 재빌드)
* ✓ 코드 재사용성 및 유지보수성 향상
* ✓ 기능별 분리를 통한 팀 간 협업 용이성 증대
* 앱의 최종 APK 크기 증가 (관리 안하면 증가 가능성 있음)
* ✓ 기능의 동적 로딩(Dynamic Feature Module) 가능

**68. Retrofit Basics (레트로핏 기초)**
What is the primary role of the Retrofit library in Android development? Select the best description.
* To perform database operations asynchronously.
* To parse JSON/XML data into Kotlin/Java objects.
* ✓ To turn REST APIs into type-safe Kotlin/Java interfaces, simplifying network request execution and response handling.
* To manage background tasks and scheduling.

**69. Dynamic Feature Modules (동적 기능 모듈)**
What is the primary benefit of using Dynamic Feature Modules? Select one.
* To improve build times by compiling modules independently.
* ✓ To allow certain features of your app to be downloaded and installed on demand, reducing the initial download size.
* To enforce stricter separation of concerns between different parts of the application.
* To enable code sharing between different applications.

---

## VII. Kotlin Language Specifics (코틀린 언어)

**70. Kotlin 확장 함수 (Extension Function)**
Kotlin 확장 함수에 대한 설명 중 올바른 것을 모두 고르세요.
* ✓ 기존 클래스를 상속하지 않고도 새로운 함수를 추가할 수 있게 한다.
* 확장 함수는 클래스의 private 멤버에 접근할 수 있다. (접근 불가)
* ✓ 수신 객체 타입(Receiver Type)을 명시하여 정의한다. (예: `fun String.lastChar(): Char`)
* ✓ 확장 함수는 정적 메소드로 컴파일된다.
* 확장 함수는 기존 클래스의 메소드를 오버라이드할 수 있다.

**71. Kotlin Sealed Classes (실드 클래스)**
What is a primary use case for Sealed Classes in Kotlin, particularly in Android development? Select one.
* To create singleton objects easily.
* ✓ To define a restricted class hierarchy, often used to represent states (e.g., Loading, Success, Error) in a type-safe way.
* To automatically generate `equals()`, `hashCode()`, and `toString()`.
* To enable extension functions for existing Java classes.

**72. Kotlin Inline Functions (인라인 함수)**
What is the main benefit of using the `inline` keyword for functions in Kotlin, especially with higher-order functions (functions taking lambdas)? Select one.
* ✓ It improves runtime performance by copying the function's bytecode and the lambda's bytecode directly into the call site, avoiding the overhead of function calls and object creation for lambdas.
* It allows functions to access private members of the class they are called from.
* It makes the function automatically asynchronous.
* It enables the use of `reified` type parameters. (인라인 함수에서 reified 사용 가능)

**73. Kotlin Data Classes (데이터 클래스)**
What features does the Kotlin compiler automatically generate for a class marked with the `data` keyword? Select all that apply.
* ✓ `equals()` / `hashCode()` pair.
* ✓ `toString()` in a standard format.
* ✓ `componentN()` functions corresponding to properties in declaration order.
* ✓ `copy()` method.
* A default constructor with no arguments. (모든 프로퍼티 기본값 있으면 생성)

---

## VIII. Build, Delivery & Optimization (빌드, 배포 및 최적화)

**74. Android App Bundle (.aab)**
Android App Bundle(.aab)에 대한 설명 중 올바른 것을 모두 고르세요.
* ✓ AAB는 앱의 모든 컴파일된 코드와 리소스를 포함하지만, APK 생성 및 서명은 Google Play에 위임하는 게시 형식이다.
* AAB를 사용하면 사용자는 앱의 모든 기능을 한 번에 다운로드해야 한다.
* ✓ Google Play는 AAB를 사용하여 각 사용자의 기기 설정에 최적화된 APK(예: 언어, 화면 밀도, ABI)를 생성하고 제공한다. (Dynamic Delivery)
* AAB 파일은 APK 파일보다 항상 크기가 크다. (업로드 파일은 클 수 있으나, 사용자 다운로드 APK는 작아짐)
* ✓ AAB는 동적 기능 모듈(Dynamic Feature Module)을 지원한다.

**75. APK Size Optimization (APK 크기 최적화)**
Which methods help in reducing the APK size? Select all that apply.
* ✓ Enabling code shrinking and obfuscation (R8/ProGuard)
* ✓ Using vector drawables instead of multiple PNG files
* ✓ Using Android App Bundles instead of APKs
* Including all language resources for global accessibility (필요한 것만 포함)
* ✓ Removing unused resources with resource shrinking

**76. ProGuard/R8 Rules (`-keep`)**
What is the purpose of `-keep` rules in a ProGuard or R8 configuration file (`proguard-rules.pro`)? Select one.
* ✓ To specify which classes or members should *not* be removed (shrunk) or renamed (obfuscated) during the build process.
* To define custom optimizations for specific code sections.
* To exclude certain libraries from the build entirely.
* To add debugging symbols to the release build.

**77. Gradle Build Types & Product Flavors (빌드 타입과 프로덕트 플레이버)**
What is the difference between Build Types and Product Flavors in a Gradle build configuration? Select one.
* ✓ Build Types (e.g., debug, release) define packaging and signing settings, while Product Flavors (e.g., free, paid) allow creating different versions of the app with varying features or resources.
* Product Flavors control signing keys, while Build Types control API endpoints.
* Build Types are used for different app versions (free/paid), while Product Flavors are for debug/release builds.
* They are interchangeable concepts in Gradle.

**78. Gradle Dependency Configurations (`implementation` vs `api`)**
What is the difference between the `implementation` and `api` dependency configurations in Gradle? Select one.
* `implementation` dependencies are faster to compile than `api` dependencies.
* ✓ `api` configuration exposes the dependency's transitive dependencies to consumers of the module, while `implementation` hides them, improving build times and encapsulation.
* `implementation` is used for testing libraries, while `api` is for runtime libraries.
* `api` dependencies are downloaded from Maven Central, while `implementation` dependencies are local JARs.

**79. Custom Gradle Plugin (커스텀 그래들 플러그인)**
Custom Gradle Plugin을 작성하는 이유로 올바른 것을 모두 고르세요.
* ✓ 반복적인 빌드 로직을 캡슐화하고 재사용하기 위해
* ✓ 빌드 프로세스를 커스터마이징하고 확장하기 위해
* 앱의 런타임 성능을 향상시키기 위해
* ✓ 여러 프로젝트에서 공통 빌드 설정을 공유하기 위해
* 안드로이드 스튜디오의 기능을 변경하기 위해

**80. 성능 프로파일링 도구**
안드로이드 앱의 성능 문제를 분석하기 위해 사용할 수 있는 도구를 모두 고르세요.
* ✓ Android Studio Profiler (CPU, Memory, Network, Energy)
* ✓ Systrace
* ✓ Perfetto
* Logcat
* ✓ Layout Inspector

**81. 로그 출력 (Logcat)**
안드로이드에서 로그를 출력하는 데 사용되는 클래스는 무엇인가요?
* ✓ Log
* System.out
* Toast
* Debugger

---

## IX. Testing (테스팅)

**82. 테스트 전략**
안드로이드 테스트 전략에 관한 설명 중 올바른 것을 모두 고르세요.
* ✓ 단위 테스트(Unit Test)는 특정 클래스나 메소드의 기능을 검증한다. (JUnit, Mockito/MockK)
* ✓ 통합 테스트(Integration Test)는 여러 컴포넌트 간의 상호작용을 검증한다.
* ✓ UI 테스트(UI Test)는 사용자 인터페이스의 동작을 검증한다. (Espresso, UI Automator)
* 테스트 피라미드는 UI 테스트를 가장 많이 작성할 것을 권장한다. (단위 테스트를 가장 많이 권장)
* ✓ 테스트는 코드의 안정성을 높이고 리팩토링을 용이하게 한다.

**83. Testing in Android (안드로이드 테스팅 도구)**
Which statements about Android testing are correct? Select all that apply.
* ✓ JUnit is used for unit testing individual components.
* ✓ Espresso is used for UI testing within a single app.
* MockK and Mockito cannot be used together in the same project. (사용 가능)
* ✓ Robolectric allows running Android framework dependent tests on JVM without emulator/device.
* UI tests are always more reliable than unit tests. (Unit tests tend to be more stable)

**84. Test Doubles (Mock vs Stub) (테스트 더블)**
In testing, what is the difference between a Mock and a Stub? Select one.
* Mocks are used in UI tests, while Stubs are used in unit tests.
* ✓ Stubs provide canned answers to calls made during the test, while Mocks also verify interactions (e.g., checking if a method was called).
* Mocks are created using Mockito, while Stubs are created using JUnit.
* Stubs replace entire classes, while Mocks only replace specific methods.

**85. UI Automator**
What type of testing is UI Automator primarily used for in Android? Select one.
* Unit testing individual classes in isolation.
* Integration testing interactions between different components within the same app.
* ✓ Black-box UI testing that simulates user interactions across multiple apps or interacting with system UI elements.
* Performance testing and memory profiling.

**86. Toast 메시지**
Toast 메시지에 관한 설명 중 올바른 것을 고르세요.
* ✓ Toast는 사용자에게 간단한 메시지를 짧게 표시하는 데 사용된다.
* Toast는 사용자의 입력을 받을 수 있다.
* Toast 메시지는 앱이 백그라운드에 있을 때도 항상 표시된다. (표시되지 않을 수 있음)
* Toast는 레이아웃 파일을 커스터마이징할 수 없다. (커스텀 가능)

---

## X. Performance, Security & Permissions (성능, 보안 및 권한)

**87. 메모리 누수 (Memory Leak)**
안드로이드에서 메모리 누수가 발생할 수 있는 상황을 모두 고르세요.
* ✓ static 변수가 Activity Context를 참조할 때
* ✓ 내부 클래스(Inner Class)가 외부 클래스(Activity 등)의 참조를 암묵적으로 가질 때 (특히 비동기 작업 콜백)
* ✓ 등록된 리스너나 브로드캐스트 리시버를 해제하지 않았을 때
* ViewModel을 사용할 때 (ViewModel 자체는 생명주기를 관리하므로 일반적으로 누수를 방지함)
* ✓ Context 대신 ApplicationContext를 사용해야 할 때 Activity Context를 사용할 경우 (예: 싱글톤 초기화)

**88. Memory Management (메모리 관리)**
Which statements about Android memory management are correct? Select all that apply.
* ✓ Memory leaks often occur when holding references to objects tied to the Activity lifecycle.
* ✓ Using WeakReferences can help prevent certain types of memory leaks.
* The Android system automatically cleans up all memory leaks.
* ✓ Static variables with Context references can cause memory leaks.
* Garbage collection guarantees immediate memory reclamation. (보장하지 않음)

**89. Media Player 리소스 해제**
아래와 유사한 코드에서 `onPause()`의 역할로 가장 중요한 것은 무엇인가요? (MediaPlayer 리소스 해제 관련)
```java
// onResume()에서 MediaPlayer 생성 및 시작 가정...
// onPause()에서...
if (mp != null) {
    if (mp.isPlaying()) { mp.stop(); }
    mp.release(); // <--- 이 부분의 중요성
    mp = null;
}
```
* 액티비티 전환 시 부드러운 전환 효과 제공
* 미디어 파일의 저작권 정보 확인
* ✓ 액티비티가 비활성화될 때 MediaPlayer 리소스를 해제하여 메모리 누수 및 불필요한 배터리 소모 방지
* 다음 액티비티로 미디어 재생 상태 전달

**90. ANR Causes (ANR 원인)**
Which of the following actions performed on the main (UI) thread are common causes of "Application Not Responding" (ANR) errors? Select all that apply.

* ✓ Performing complex calculations or long loops.
* ✓ Executing synchronous network requests.
* ✓ Performing slow disk I/O operations (e.g., database queries).
* Updating a TextView with a short string.
* Responding to a broadcast Intent quickly.

**91. 앱 보안 기초**
안드로이드 앱 보안에 관한 설명 중 올바른 것을 모두 고르세요.

* ✓ ProGuard/R8은 코드 난독화와 최적화를 제공한다.
* ✓ 안드로이드 키스토어 시스템은 암호화 키를 안전하게 저장하는 데 사용될 수 있다.
* ✓ Network Security Config를 사용하여 앱의 네트워크 보안 설정을 구성할 수 있다.
* 모든 중요한 데이터는 SharedPreferences에 안전하게 저장할 수 있다. (암호화 필요)
* SSL 인증서 피닝은 모든 종류의 네트워크 공격을 방지할 수 있다. (중간자 공격 방어에 도움)

**92. Security Best Practices (보안 권장사항)**
Which Android security best practices are recommended? Select all that apply.

* ✓ Using the Android Keystore for storing cryptographic keys.
* Storing sensitive information in SharedPreferences with private mode. (암호화된 SharedPreferences 사용 권장)
* ✓ Implementing certificate pinning for network connections.
* ✓ Using encryption for sensitive data stored on device.
* Using Intent filters to prevent unauthorized access. (컴포넌트 접근 제어는 exported 속성 및 권한으로)

**93. Network Security Config Details (네트워크 보안 구성)**
What can you configure using the Network Security Configuration file (network_security_config.xml)? Select all that apply.

* ✓ Define trust anchors (custom Certificate Authorities).
* ✓ Disable cleartext (HTTP) traffic for the entire app or specific domains.
* ✓ Configure certificate pinning.
* Set network timeout durations.
* Specify proxy settings for all network requests.

**94. Runtime Permissions (런타임 권한)**
What is the correct procedure for handling dangerous permissions (like Camera or Location) introduced in Android 6.0 (API 23)? Select one.

* Declare all required permissions in the Manifest, and the system grants them automatically upon installation.
* ✓ Declare the permission in the Manifest, check if the permission is already granted using checkSelfPermission(), and if not, request it from the user using requestPermissions().
* Only request permissions at runtime; Manifest declarations are no longer needed.
* Use an Intent to delegate permission requests to a system Activity.

**95. Handling Runtime Permission Rationale (런타임 권한 요청 사유 설명 처리)**
When should you typically show a rationale (explanation) to the user before requesting a dangerous permission again? Select the best situation.

* Always show the rationale before the first time you request the permission.
* Only show the rationale if the user explicitly asks for it.
* ✓ Show the rationale if the user has previously denied the permission request and the shouldShowRequestPermissionRationale() method returns true.
* Show the rationale only after the user has permanently denied the permission ("Don't ask again").
