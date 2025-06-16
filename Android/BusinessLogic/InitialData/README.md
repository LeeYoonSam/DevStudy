# 초기 데이터 로딩 시점: LaunchedEffect vs. ViewModel.init()

> 💡 **참고:** 이 질문은 코틀린 코루틴과 Jetpack Compose에 관련된 기초 개념을 포함합니다. 만약 이러한 주제에 익숙하지 않다면, 먼저 코틀린과 *1장: Jetpack Compose 인터뷰 질문* 을 학습하는 것을 고려해 보세요. 이후에 이 카테고리를 다시 보면 더 명확하고 깊이 있는 이해를 얻는 데 도움이 될 것입니다.

안드로이드 개발에서 자주 논의되는 주제 중 하나는 **초기 데이터를 컴포저블(Composable)의 `LaunchedEffect`에서 로드할지, 아니면 `ViewModel`의 `init()` 블록 내에서 로드할지**입니다. [안드로이드 공식 문서](https://developer.android.com/kotlin/flow/stateflow-and-sharedflow#stateflow)와 [architecture-samples GitHub 저장소](https://github.com/android/architecture-samples/blob/130f5dbebd0c7b5ba195cc08f25802ed9f0237e5/app/src/main/java/com/example/android/architecture/blueprints/todoapp/addedittask/AddEditTaskViewModel.kt#L64)의 예제들은 더 나은 생명주기 관리와 구성 변경 시 데이터 유지를 위해 일반적으로 `ViewModel.init()` 내부에서 데이터를 로드할 것을 권장합니다. 안드로이드 커뮤니티가 일반적으로 초기 데이터를 어떻게 로드하는 것을 선호하는지 알아보기 위한 설문조사가 있었습니다. 결과는 다음과 같았습니다.

> **설문조사: 초기 데이터를 어디서 로드하나요?**
>
>   * `LaunchedEffect` 또는 다른 컴포저블 생명주기 API: 13.9%
>   * `ViewModel.init()` 블록: 65.5%
>   * 두 가지 접근 방식 모두 사용: 10.3%
>   * 두 가지 모두 안티패턴이라고 생각: 10.3%

설문조사 결과에서 볼 수 있듯이, 대부분의 개발자는 `ViewModel.init()` 내부에서 초기 데이터를 로드하는 것을 선호합니다. 한 숙련된 안드로이드 커뮤니티 회원은 생명주기 안정성과 영속적인 상태 관리를 강조하며, 왜 `ViewModel.init()`이 `LaunchedEffect`와 같은 컴포저블 함수를 사용하는 것보다 종종 더 나은 선택인지에 대한 설득력 있는 설명을 공유했습니다.

-----

## 커뮤니티의 관점들

### 관점 A: `ViewModel.init()` 선호

> **커뮤니티 사용자 A:** "만약 Jetpack Compose UI를 기본 애플리케이션 상태나 데이터의 시각적 표현으로 간주한다면, UI가 앱에게 무엇을 해야 할지 지시하도록 의존하는 것은 설계 결함으로 볼 수 있습니다. 이러한 관점에서, `ViewModel.init()`을 사용하는 것이 `LaunchedEffect` 내에서 직접 데이터를 가져오는 것보다 더 나은 **관심사 분리(separation of concerns)** 를 제공하며, 비즈니스 로직과 UI 상태 관리가 별개로 유지되도록 보장합니다."

### 관점 B: `ViewModel.init()` 비선호 및 대안 제시

> **커뮤니티 사용자 B:** "저는 `ViewModel.init()`에만 의존하는 것이 특정 동작이 트리거되는 시점에 대한 제어력을 감소시키고 유닛 테스트를 복잡하게 만들 수 있다고 생각합니다. 대신, 저는 지연 초기화(lazily initialized)될 수 있고 ViewModel 내에서 이벤트 기반 플로우를 관찰하여 트리거될 수 있는 독립적인 함수를 정의하는 것을 선호합니다. 이 접근 방식은 더 큰 유연성과 제어력을 제공하여, `LaunchedEffect`나 이벤트 트리거 액션과 같은 메서드가 데이터 로딩을 더 효과적으로 관리할 수 있게 합니다. 추가적인 장점은 `ViewModel.init` 블록에 전적으로 의존하는 대신, 사용자 상호작용이나 특정 이벤트에 기반하여 데이터를 다시 가져올 때 효율성이 향상된다는 것입니다. 이는 특히 애플리케이션에서 동적이거나 인터랙티브한 기능을 다룰 때 가치가 있습니다."

-----

## "둘 다 안티패턴이다": 지연 관찰(Lazy Observation) 방식

두 해결책 모두 주목할 만한 단점을 가지고 있습니다. 흥미롭게도, Google의 안드로이드 툴킷 팀의 **Ian Lake**는 두 접근 방식 모두 안드로이드 개발에서 **안티패턴(anti-patterns)** 으로 간주되며, 초기 데이터 로딩 관리에 더 견고한 대안이 필요하다고 지적했습니다.

  * `ViewModel.init()`에서 초기 데이터를 로드하는 것은 ViewModel 생성 중에 의도치 않은 부작용(side effects)을 일으킬 수 있으며, 이는 UI 관련 상태를 관리하는 본래의 역할을 벗어나고 생명주기 처리를 복잡하게 만듭니다.
  * 마찬가지로, Jetpack Compose의 `LaunchedEffect` 내에서 데이터를 초기화하는 것은 리컴포지션(recomposition) 중에 반복적으로 트리거될 위험이 있습니다. 만약 컴포저블 함수의 생명주기가 ViewModel의 생명주기와 다르면, 예기치 않은 동작을 초래하고 의도된 데이터 흐름을 방해할 수 있습니다.

이러한 우려를 해결하기 위해, Ian Lake는 **지연 초기화(lazy initialization)를 위해 콜드 플로우(cold flows)를 사용할 것을 권장**합니다. 이 접근 방식에서는, 플로우가 수집(collected)되기 시작할 때만 네트워크 요청이나 데이터베이스 쿼리와 같은 비즈니스 로직을 실행합니다. UI 레이어로부터 구독자가 생기기 전까지 플로우는 비활성 상태로 남아있어 불필요한 작업이 수행되지 않도록 보장합니다.

-----

## 지연 관찰(Lazy Observation)을 위한 모범 사례

이러한 접근 방식의 예는 아래 코드 조각에서 볼 수 있듯이 [Pokedex-Compose 프로젝트](https://github.com/skydoves/pokedex-compose)에서 찾을 수 있습니다.

```kotlin
// DetailsViewModel.kt
@HiltViewModel
class DetailsViewModel @Inject constructor(
    private val detailsRepository: DetailsRepository,
    private val savedStateHandle: SavedStateHandle,
) : ViewModel() {

    // 1. 초기 데이터 소스 (예: 이전 화면에서 전달받은 포켓몬 이름)
    private val pokemonName: StateFlow<String?> = savedStateHandle.getStateFlow(key, null)

    // 2. 콜드 플로우를 사용한 지연 관찰
    val pokemonInfo: StateFlow<PokemonInfo?> = pokemonName
        .filterNotNull() // pokemonName이 null이 아닐 때만 진행
        .flatMapLatest { name -> // 새로운 이름이 들어오면 이전 네트워크 요청은 취소하고 새 요청 시작
            detailsRepository.fetchPokemonInfo(name = name)
        }
        .stateIn( // 3. 콜드 플로우를 핫 StateFlow로 변환
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000), // 구독자가 있을 때만 업스트림 플로우 활성화 (5초 유지)
            initialValue = null, // 초기값
        )
}
```

위 코드에서 업스트림 호출(`detailsRepository.fetchPokemonInfo()`)은 첫 번째 구독자가 플로우를 수집하기 시작할 때만 트리거됩니다. 그 결과는 캐시되고 `stateIn` 메서드를 사용하여 상태로 변환되어, 효율적인 데이터 관리와 중복 작업 최소화를 보장합니다. `stateIn` 메서드는 콜드 Flow를 핫 `StateFlow`로 변환하며, 일부 상태의 값을 업데이트하는 콜드 플로우가 생성 또는 유지 비용이 많이 들지만 여러 구독자가 최신 상태 값을 수집해야 하는 상황에서 유용합니다.

궁극적으로, `pokemonInfo` 프로퍼티가 핫 플로우로 정의되더라도, 가장 최근에 방출된 값은 업스트림 플로우의 단일 실행 인스턴스에서 나옵니다. 이 인스턴스는 여러 다운스트림 구독자 간에 공유되며 지연 초기화되어, 효율적인 데이터 관리와 중복 실행 감소를 보장합니다.

코루틴과 Flow에 대해 더 자세히 탐색하고 싶다면 **Manifest Kotlin Interview for Android Developers** 책을 참조할 수 있습니다. 만약 지금 이러한 개념들을 파악하기 어렵다면, 1장을 학습한 후 이 카테고리를 다시 방문하여 더 명확한 이해를 얻는 것을 고려해 보세요.

-----

## 요약

안드로이드에서 초기 데이터를 로드하는 방법에는 Jetpack Compose의 `LaunchedEffect` 사용, `ViewModel`의 `init()` 메서드 사용, 또는 콜드 플로우를 통한 지연 관찰 등 여러 가지가 있습니다. 이 논의는 효율성과 부작용 방지를 위해 콜드 플로우를 활용하는 것을 제안하는 것으로 끝났습니다. 그러나 보편적인 해결책은 없으며, 각 프로젝트는 고유한 요구 사항을 가집니다. 앱의 특정 요구 사항을 이해하는 것이 가장 적합한 접근 방식을 선택하는 데 중요합니다. 이 논의는 애플리케이션의 맥락에 효과적으로 적용할 수 있는 실용적인 전략들을 강조합니다. 이 주제에 관심이 있다면, [Loading Initial Data in LaunchedEffect vs. ViewModel](https://proandroiddev.com/loading-initial-data-in-launchedeffect-vs-viewmodel-f1747c20ce62) 에서 더 많은 내용을 읽어볼 수 있습니다.

-----

## Q. Jetpack Compose에서 ViewModel.init()과 LaunchedEffect에서 초기 데이터를 로드하는 것의 장단점은 무엇이며, 언제 어떤 접근 방식을 선택하겠습니까? 만약 다른 해결책을 선호한다면, 그것은 무엇인가요?

Jetpack Compose 환경에서 화면에 필요한 초기 데이터를 언제 로드할 것인지는 아키텍처의 견고성과 효율성에 큰 영향을 미칩니다. `ViewModel`의 `init()` 블록을 사용하는 방식과 컴포저블의 `LaunchedEffect`를 사용하는 방식은 각각 명확한 장단점을 가지고 있으며, 최근에는 이 둘의 단점을 보완하는 제3의 방식이 권장되고 있습니다.

---
### 1. `ViewModel.init()`에서 데이터 로드

`ViewModel`의 `init` 블록은 `ViewModel` 인스턴스가 처음 생성될 때 단 한 번만 실행됩니다.

```kotlin
// ViewModel
class MyViewModel(private val repository: MyRepository) : ViewModel() {
    
    private val _uiState = MutableStateFlow<UiState>(UiState.Loading)
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    init {
        // ViewModel 생성 시 즉시 데이터 로딩 시작
        fetchInitialData()
    }

    fun fetchInitialData() {
        viewModelScope.launch {
            _uiState.value = UiState.Success(repository.getData())
        }
    }
}
```

#### 1.1. 장점
* **👍 관심사 분리 (Separation of Concerns):** 데이터 로딩을 시작하는 책임이 `ViewModel`에 있으므로, UI(컴포저블)는 단순히 상태를 표시하는 역할에만 집중할 수 있습니다. 이는 MVVM 아키텍처 원칙에 잘 부합합니다.
* **👍 생명주기 안정성:** `ViewModel`은 화면 회전과 같은 구성 변경에도 소멸되지 않고 유지됩니다. 따라서 `init` 블록은 구성 변경 시 다시 실행되지 않아, 불필요한 데이터 재요청을 방지하고 상태를 안정적으로 유지합니다.

#### 1.2. 단점
* **👎 제어력 부족 (즉시 로딩, Eager Loading):** `ViewModel`이 생성되는 즉시 데이터 로딩이 시작됩니다. 만약 해당 데이터가 특정 조건에서만 필요하거나, 로딩을 지연시키고 싶을 때 제어하기가 어렵습니다.
* **👎 테스트 복잡성 증가:** `ViewModel`의 유닛 테스트 시, 인스턴스를 생성하기만 해도 `init` 블록 내의 네트워크 요청이나 데이터베이스 접근이 즉시 실행됩니다. 이로 인해 테스트 환경 설정이 복잡해지고(예: `Dispatcher` 규칙, Mock 객체 설정), 순수한 `ViewModel` 로직 테스트가 어려워질 수 있습니다.
* **👎 생성자 내 부작용 (Side-effects in Constructor):** 생성 과정(`init` 블록 포함)에서 잠재적으로 실패할 수 있거나 오래 걸리는 작업을 수행하는 것은 일반적으로 안티패턴으로 간주됩니다.

---
### 2. `LaunchedEffect`에서 데이터 로드

`LaunchedEffect`는 컴포저블이 컴포지션에 처음 추가될 때 코루틴을 실행하고, 컴포지션에서 사라질 때 코루틴을 취소합니다.

```kotlin
// Composable
@Composable
fun MyScreen(viewModel: MyViewModel = hiltViewModel()) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    // MyScreen이 컴포지션에 추가될 때 데이터 로딩 시작
    LaunchedEffect(Unit) { // key가 Unit이므로 한 번만 실행됨
        viewModel.fetchInitialData()
    }

    // uiState를 사용하여 UI 그리기
}
```

#### 2.1. 장점
* **👍 트리거 시점 제어:** 데이터 로딩이 `ViewModel` 생성 시점이 아닌, 실제 UI가 화면에 표시될 때 시작되므로 로딩 시점을 더 세밀하게 제어할 수 있습니다. `key` 값을 변경하여 데이터 로딩을 다시 트리거할 수도 있습니다.
* **👍 UI 생명주기 연동:** 컴포저블의 생명주기에 정확히 맞춰 실행되므로, UI가 화면에 없을 때 불필요한 작업을 수행하지 않습니다.

#### 2.2. 단점
* **👎 관심사 분리 위배:** UI(컴포저블)가 `ViewModel`에게 "데이터를 로드하라"고 지시하는 형태가 됩니다. 이는 UI는 상태를 반영하기만 해야 한다는 MVVM의 단방향 데이터 흐름 원칙을 일부 위반하는 것입니다.
* **👎 반복 실행 위험:** `LaunchedEffect`의 `key`가 안정적이지 않거나, 컴포저블이 컴포지션에서 잠시 벗어났다가 다시 돌아오는 경우(예: 화면 전환 후 복귀), 데이터 로딩이 불필요하게 반복될 수 있습니다.

---
### 3. 언제 무엇을 선택해야 하는가?

* **`ViewModel.init()`이 적합한 경우:**
    * 해당 화면에 진입하면 데이터가 **항상, 그리고 즉시** 필요할 때.
    * 구성 변경 시 데이터가 안정적으로 유지되는 것이 매우 중요할 때.
    * 간단한 화면에서는 이 방식이 더 직관적이고 코드가 적을 수 있습니다.

* **`LaunchedEffect`가 적합할 수 있는 경우:**
    * 데이터 로딩이 아닌, **UI에만 종속적인 일회성 애니메이션**이나 작업에 더 적합합니다.
    * **초기 데이터 로딩 목적**으로는 일반적으로 안티패턴으로 간주되므로 신중하게 사용해야 합니다.

---
### 4. 더 나은 해결책: 지연 관찰(Lazy Observation)을 통한 콜드 플로우(Cold Flow) 활용

앞선 두 방식의 단점들을 보완하는, Google 엔지니어가 권장하는 현대적인 접근 방식은 **"지연 관찰"** 패턴입니다. `ViewModel` 내에 데이터 로딩 로직을 **콜드 플로우(Cold Flow)** 로 정의하고, UI가 이 플로우를 **수집(collect)하기 시작할 때만** 실제 데이터 로딩이 트리거되도록 하는 것입니다.

```kotlin
// ViewModel
class MyViewModel(private val repository: MyRepository) : ViewModel() {
    
    // 데이터 로딩 로직을 포함하는 콜드 플로우 정의
    // 이 플로우 자체는 즉시 실행되지 않음
    val dataFlow: Flow<MyData> = repository.getDataStream()

    // UI에서 관찰할 StateFlow. stateIn을 통해 콜드 플로우를 핫 플로우로 변환
    val uiState: StateFlow<UiState> = dataFlow
        .map { data -> UiState.Success(data) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000), // 구독자가 있을 때만 업스트림 활성화
            initialValue = UiState.Loading // 초기 상태
        )
}

// Composable
@Composable
fun MyScreen(viewModel: MyViewModel = hiltViewModel()) {
    // UI가 STARTED 상태일 때만 데이터를 수집(collect) 시작
    // 이때 비로소 viewModel의 dataFlow가 활성화되어 데이터 로딩이 시작됨
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    // uiState를 사용하여 UI 그리기
}
```

#### 이 방식의 장점
* **관심사 분리:** 데이터 로딩 로직은 `ViewModel`에 완벽하게 캡슐화됩니다.
* **지연 실행 및 효율성:** UI가 실제로 데이터를 필요로 할 때(수집을 시작할 때)만 데이터 로딩이 시작되어 불필요한 작업을 방지합니다.
* **생명주기 안전성:** `collectAsStateWithLifecycle()`과 같은 수집기를 사용하면 UI의 생명주기에 맞춰 안전하게 데이터 수집이 시작되고 중지됩니다.
* **구성 변경 안전성:** `stateIn`을 통해 생성된 `StateFlow`는 `viewModelScope` 내에서 상태를 유지하므로 구성 변경에도 데이터가 보존되며, 불필요한 데이터 재요청이 발생하지 않습니다.
* **테스트 용이성:** `init` 블록과 달리, 테스트 시 플로우를 수집하지 않으면 실제 데이터 로딩이 발생하지 않으므로 유닛 테스트가 더 간단해집니다.

---
### 5. 결론

`ViewModel.init()`과 `LaunchedEffect`는 각각 장단점이 있지만, **"지연 관찰(Lazy Observation)을 통한 콜드 플로우(Cold Flow) 활용"** 방식이 대부분의 현대적인 안드로이드 앱에서 초기 데이터를 로딩하는 가장 견고하고 유연하며 효율적인 방법입니다. 이는 관심사 분리, 생명주기 안정성, 테스트 용이성 등 여러 측면에서 다른 두 방식의 장점을 결합하고 단점을 보완합니다.


## Q. ViewModel.init()이나 LaunchedEffect와 비교하여 콜드 플로우를 사용한 지연 관찰 방식이 초기 데이터 로딩 시 효율성을 어떻게 향상시키나요? 이 접근 방식이 유용한 시나리오 예시를 제시해 주세요.

`ViewModel`의 `init()` 블록이나 컴포저블의 `LaunchedEffect`와 비교하여, **콜드 플로우(Cold Flow)를 사용한 지연 관찰(Lazy Observation)** 방식은 초기 데이터 로딩 시 자원 사용, 데이터 최신성, 생명주기 관리 측면에서 훨씬 뛰어난 효율성을 제공합니다.

---
### 1. 지연 관찰(Lazy Observation) 방식의 효율성 향상 원리

#### 1.1. 불필요한 데이터 로딩 방지 (자원 효율성) 🍃
* **`ViewModel.init()` (즉시 로딩, Eager Loading):**
    `ViewModel`이 생성되는 즉시 데이터 로딩을 시작합니다. 만약 사용자가 해당 화면에 진입했지만 UI가 완전히 그려지기 전에 바로 다른 화면으로 이동한다면, 이미 시작된 네트워크 요청은 불필요한 데이터와 배터리를 소모하게 됩니다.
* **`LaunchedEffect`:**
    컴포저블이 화면에 그려질 때(컴포지션에 추가될 때) 데이터 로딩을 시작하므로 `init()`보다는 낫습니다. 하지만 `ViewPager2`의 다른 탭에 있는 화면처럼, 당장 사용자에게 보이지는 않지만 미리 생성된 컴포저블의 경우에도 데이터 로딩이 시작될 수 있습니다.
* **지연 관찰 (Lazy Observation) 방식:**
    `ViewModel` 내의 데이터 로딩 로직은 **콜드 플로우**로 정의되어 그 자체로는 아무것도 실행하지 않습니다. 실제 데이터 로딩(업스트림 플로우 실행)은 UI가 `stateIn`이나 `shareIn`으로 변환된 **핫 플로우(`StateFlow` 등)를 구독(collect)하기 시작할 때 비로소 트리거**됩니다. `repeatOnLifecycle(Lifecycle.State.STARTED)`와 함께 사용하면, UI가 사용자에게 실제로 보일 때만 데이터 로딩이 시작되고, 화면을 벗어나면 정해진 시간(`SharingStarted.WhileSubscribed`의 타임아웃) 후에 자동으로 중단됩니다. 이는 **정말로 필요할 때만 자원을 사용**하도록 보장합니다.

#### 1.2. 오래된(Stale) 요청의 자동 취소 (데이터 최신성) 🔄
* **문제점:** 사용자가 검색창에 "안", "안드", "안드로이드"를 빠르게 연속으로 입력하는 경우, 세 번의 네트워크 요청이 모두 실행되는 것은 비효율적입니다. 마지막 "안드로이드"에 대한 검색 결과만 필요합니다.
* **지연 관찰 방식의 해결책 (`flatMapLatest`):**
    이 패턴은 종종 `flatMapLatest` 연산자와 함께 사용됩니다. 사용자의 입력(트리거)이 새로운 값으로 변경되면, `flatMapLatest`는 **이전에 실행 중이던 데이터 로딩(네트워크 요청) 코루틴을 자동으로 취소**하고, 최신 값을 기반으로 새로운 코루틴을 시작합니다. 이는 항상 가장 최신 요청에 대한 결과만 처리하도록 보장하여 불필요한 네트워크 트래픽과 서버 부하를 줄입니다. `init`이나 `LaunchedEffect`에서 이 로직을 직접 구현하려면 `Job` 객체를 수동으로 관리하고 취소하는 복잡한 코드가 필요합니다.

#### 1.3. 구성 변경 시 데이터 재요청 방지
* **`LaunchedEffect`:** `key` 값 관리를 신중하게 하지 않으면, 화면 회전과 같은 구성 변경으로 인해 컴포저블이 리컴포지션될 때 데이터 로딩이 다시 트리거될 수 있습니다.
* **지연 관찰 방식의 해결책 (`stateIn`):**
    `stateIn(viewModelScope, ...)` 연산자는 콜드 플로우를 `viewModelScope` 내에서 핫 `StateFlow`로 변환합니다. 이 `StateFlow`는 구성 변경에도 `ViewModel`과 함께 살아남습니다. UI가 재생성되어 다시 구독을 시작하면, **이미 진행 중이거나 완료된 데이터 스트림에 다시 연결**될 뿐, 근원적인 데이터 로딩(업스트림 플로우)을 **다시 트리거하지 않습니다.** 이는 `ViewModel.init()`의 생명주기 안정성 장점과 지연 로딩의 장점을 모두 취한 것입니다.

---
### 2. 지연 관찰 방식이 유용한 시나리오 예시

#### 2.1. 시나리오: 동적 검색 및 필터링 화면
사용자가 검색어를 입력하거나 필터 옵션을 변경할 때마다 API를 호출하여 상품 목록을 보여주는 화면을 상상해 봅시다.

* **ViewModel 구현:**
```kotlin
@HiltViewModel
class ProductSearchViewModel @Inject constructor(
    private val productRepository: ProductRepository
) : ViewModel() {

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _filters = MutableStateFlow<Set<Filter>>(emptySet())
    val filters: StateFlow<Set<Filter>> = _filters.asStateFlow()

    // 검색어와 필터가 변경될 때마다 새로운 데이터 스트림을 생성
    val products: StateFlow<PagingData<Product>> =
        combine(searchQuery, filters) { query, selectedFilters ->
            Pair(query, selectedFilters)
        }.flatMapLatest { (query, selectedFilters) ->
            // 검색어나 필터가 변경되면 이전 repository 호출은 취소되고 새로운 호출이 시작됨
            productRepository.getProductStream(query, selectedFilters)
        }
        .cachedIn(viewModelScope) // Paging 사용 시 결과 캐싱
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = PagingData.empty()
        )
    
    fun onQueryChanged(newQuery: String) {
        _searchQuery.value = newQuery
    }

    fun onFilterChanged(filter: Filter, isSelected: Boolean) {
        _filters.update { currentFilters ->
            if (isSelected) currentFilters + filter else currentFilters - filter
        }
    }
}
```

#### 2.2. 이점 분석
* **초기 로딩 지연:** 사용자가 검색어를 입력하거나 필터를 선택하기 전까지는 실제 네트워크 요청이 발생하지 않습니다. 초기 상태 값만 UI에 표시됩니다.
* **검색어 입력 시 효율성:** 사용자가 "노트북"을 빠르게 입력하는 동안 "노", "노트", "노트부"에 대한 네트워크 요청은 `flatMapLatest`에 의해 자동으로 취소되고, 최종적으로 "노트북"에 대한 요청만 서버로 전송됩니다. 이는 불필요한 API 호출을 막아 매우 효율적입니다.
* **필터 변경 시 즉각적인 반응:** 필터를 변경하면 `combine` 연산자에 의해 `searchQuery`와 `filters`가 조합되고, `flatMapLatest`는 즉시 새로운 조건으로 데이터 로딩을 다시 시작합니다.
* **화면이 보이지 않을 때의 자원 절약:** 만약 이 검색 화면이 `ViewPager2`의 여러 탭 중 하나에 포함되어 있다면, 사용자가 해당 탭을 보고 있을 때만 (`SharingStarted.WhileSubscribed` 정책에 따라) 데이터 로딩이 활성화됩니다. 다른 탭을 보고 있을 때는 데이터 스트림이 중단되어 자원을 낭비하지 않습니다.

---
### 3. 결론

`ViewModel.init()`은 간단하지만 제어가 어렵고, `LaunchedEffect`는 제어는 가능하지만 관심사 분리를 해치고 반복 실행의 위험이 있습니다. 반면, **콜드 플로우를 사용한 지연 관찰 방식**은 데이터 로딩 로직을 ViewModel 내에 캡슐화하여 **관심사를 분리**하면서도, UI가 실제로 데이터를 필요로 할 때만 작업을 시작하고, 불필요한 작업은 **자동으로 취소**하며, **구성 변경에도 강한** 특성을 보입니다. 따라서 복잡하고 동적인 화면의 초기 데이터를 로딩하는 데 있어 가장 정교하고 효율적이며 견고한 최신 접근 방식이라고 할 수 있습니다.