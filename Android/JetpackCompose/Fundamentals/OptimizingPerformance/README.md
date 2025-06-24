# 안정성 향상을 통해 Compose 성능을 최적화한 경험이 있나요?

Jetpack Compose에서 성능 최적화는 컴포저블 함수를 **안정화(stabilizing)** 하고 **불필요한 리컴포지션(recomposition)을 최소화**하는 것에 달려 있습니다. 안정성은 Compose가 리컴포지션 중에 어떤 함수를 건너뛸지 효과적으로 결정할 수 있도록 보장하여 효율성을 향상시킵니다. 아래는 안정성을 보장하고 고급 컴파일러 기능을 활용하여 성능을 향상시키는 주요 전략들입니다.

-----

## 불변 컬렉션 (Immutable Collections)

`List`나 `Map`과 같은 읽기 전용 컬렉션은 그 기저(내부) 구현이 수정을 허용할 수 있기 때문에 Compose 컴파일러에 의해 **불안정한(unstable)** 것으로 처리됩니다. 예를 들어, 아래의 훌륭한 예시를 상상해 볼 수 있습니다.

```kotlin
internal var mutableUserList: MutableList<User> = mutableListOf()
public val userList: List<User> = mutableUserList
```

위 예시에서 `userList`는 본질적으로 읽기 전용인 `List`로 선언되었습니다. 그러나 이는 `MutableList`로부터 인스턴스화될 수 있어, 내부 구현이 변경 가능하게 됩니다. Compose 컴파일러는 이 리스트가 진정으로 불변인지 판단할 수 없기 때문에, 리컴포지션의 정확성을 보장하기 위해 이러한 인스턴스를 불안정한 것으로 취급합니다.

안정성을 보장하려면, 본질적으로 안정적인 [**`kotlinx.collections.immutable`**](https://github.com/Kotlin/kotlinx.collections.immutable) 또는 [Guava의 불변 컬렉션](https://github.com/google/guava/wiki/ImmutableCollectionsExplained)을 사용할 수 있습니다. 이러한 라이브러리들은 `ImmutableList`, `ImmutableSet`과 같은 불변 대안을 제공하여 컬렉션이 읽기 전용이고 효율적인 리컴포지션에 적합하도록 보장합니다.

> 💡 프로 팁: KnownStableConstructs.kt
>
> 컴파일러가 이러한 컬렉션들을 어떻게 구별하는지 더 깊이 이해하려면, Compose 컴파일러 라이브러리의 [`KnownStableConstructs.kt`](https://github.com/JetBrains/kotlin/blob/eadcce82e781d7be850118e82333893ab7cf10a9/plugins/compose/compiler-hosted/src/main/java/androidx/compose/compiler/plugins/kotlin/analysis/KnownStableConstructs.kt#L48) 파일을 참조하세요. 코드에서 볼 수 있듯이, Compose 컴파일러는 안정적인 것으로 처리해야 할 클래스에 대한 패키지 이름 목록을 명시적으로 유지하고 있습니다.

-----

## 람다 안정성 (Lambda Stability)

Compose는 람다 표현식이 외부 변수를 캡처하는지 여부에 따라 다르게 처리합니다.

  * **캡처하지 않는 람다 (Non-Capturing Lambdas):** 외부 변수에 의존하지 않는 람다는 안정적인 것으로 간주되며 **싱글톤으로 최적화**되어 불필요한 할당을 피합니다.
  * **캡처하는 람다 (Capturing Lambdas):** 외부 변수에 의존하는 람다는 변경에 동적으로 반응하기 위해 **`remember`를 사용하여 메모아이즈(memoized)** 됩니다. 변경 사항을 관찰하기 위해 외부 변수는 `remember` API에 `key` 파라미터로 전달됩니다. 이는 리컴포지션 중 안정성과 일관성을 보장합니다.

클로저(closure) 내에서 값을 캡처한다는 것은 람다 표현식이 주변 스코프의 변수에 의존한다는 것을 의미합니다. 람다가 외부 변수에 의존하지 않는다면, 아래 예시와 같이 캡처하지 않는 것으로 간주됩니다.

```kotlin
modifier.clickable {
    Log.d("Log", "이 람다는 어떤 값도 캡처하지 않습니다")
}
```

람다 파라미터가 어떤 값도 캡처하지 않을 때, 코틀린은 람다를 싱글톤으로 처리하여 불필요한 할당을 줄여 최적화합니다. 그러나 람다가 자신의 직속 스코프 외부의 변수에 의존한다면, 아래 예시와 같이 캡처하는 것으로 간주됩니다.

```kotlin
var sum = 0
ints.filter { it > 0 }.forEach {
    sum += it // 람다 외부의 'sum' 변수를 캡처함
}
```

-----

## 래퍼 클래스 (Wrapper Classes)

자신이 제어할 수 없는 불안정한 클래스(예: 타사 라이브러리)의 경우, 아래 예시와 같이 안정성 어노테이션을 가진 래퍼 클래스를 만들 수 있습니다.

```kotlin
@Immutable
data class ImmutableUserList(
    val users: List<User> // 불안정한 List를 감싸서 안정적인 클래스로 만듦
)
```

그런 다음, 아래 예시와 같이 컴포저블 함수에서 이 래퍼 클래스를 파라미터 타입으로 사용할 수 있습니다.

```kotlin
@Composable
fun UserAvatars(
    modifier: Modifier,
    userList: ImmutableUserList, // 안정적인 래퍼 클래스를 파라미터로 받음
) {
    // ...
}
```

-----

## 안정성 설정 파일 (Stability Configuration File)

Compose 컴파일러 1.5.5부터 [안정성 설정 파일](https://developer.android.com/develop/ui/compose/performance/stability/fix#configuration-file)에서 안정적인 클래스를 지정하는 기능이 도입되었습니다.

  * 안정적인 것으로 처리해야 할 클래스 목록을 `compose_compiler_config.conf` 파일에 작성합니다.
  * `build.gradle.kts`에서 해당 파일을 설정하여 Compose 컴파일러가 이 클래스들에 대한 리컴포지션을 건너뛸 수 있도록 합니다.

<!-- end list -->

```kotlin
// compose_compiler_config.conf
com.example.thirdparty.LibraryClass
```

이 기능은 타사 클래스나 사용자 정의 타입을 안정화시키는 데 특히 유용합니다. 이를 활성화하면, 래퍼 클래스를 수동으로 만들 필요 없이 프로젝트 내에서 전역적으로 특정 클래스를 안정적인 것으로 지정할 수 있습니다.

-----

## 강력한 건너뛰기 모드 (Strong Skipping Mode)

[**강력한 건너뛰기 모드**](https://developer.android.com/develop/ui/compose/performance/stability/strongskipping)는 Compose 컴파일러 1.5.4에서 도입된 실험적 기능으로, 불안정한 파라미터를 포함하더라도 재시작 가능한 컴포저블 함수의 리컴포지션을 건너뛸 수 있게 합니다.

  * 안정적인 파라미터를 가진 컴포저블 함수는 객체 동등성(`equals()`)을 사용하여 비교되는 반면, 불안정한 파라미터는 **인스턴스 동일성(`===`)** 을 사용하여 비교됩니다.
  * 이러한 동작에서 특정 함수를 제외하려면 `@NonSkippableComposable` 어노테이션을 사용합니다.

-----

## 요약

파라미터를 안정화하고, 불변 컬렉션을 사용하며, 람다를 최적화하고, 래퍼 클래스를 사용하며, 강력한 건너뛰기 모드와 같은 고급 기능을 활용함으로써 Jetpack Compose 애플리케이션의 성능을 크게 향상시킬 수 있습니다. 이러한 전략들은 불필요한 리컴포지션을 줄이고, UI 반응성을 개선하며, 더 부드러운 상호작용을 보장합니다. 이러한 기술들을 적용하면 현재 및 미래의 요구 사항에 모두 최적화된 애플리케이션을 보장할 수 있습니다. 더 깊은 통찰력을 원한다면 [Jetpack Compose에서 안정성을 마스터하여 앱 성능 최적화하기](https://proandroiddev.com/optimize-app-performance-by-mastering-stability-in-jetpack-compose-69f40a8c785d) 및 GitHub의 [compose-performance 저장소](https://github.com/skydoves/compose-performance)를 참조하세요.

-----

## Q. `List`를 파라미터로 받는 컴포저블 함수가 불필요한 리컴포지션을 유발할 때 이를 어떻게 최적화하겠습니까?

`List<T>`를 파라미터로 받는 컴포저블 함수가 데이터 내용이 동일함에도 불구하고 불필요하게 리컴포지션(recomposition)되는 것은 Jetpack Compose 성능 최적화에서 매우 흔하게 마주치는 문제입니다. 이 문제의 원인을 이해하고 올바른 전략을 적용하면 앱의 성능을 크게 향상시킬 수 있습니다.

-----

### 1. 문제의 원인: `List`는 왜 불안정한(Unstable) 타입인가?

Jetpack Compose 컴파일러는 리컴포지션을 건너뛸지(skip) 여부를 결정하기 위해 컴포저블에 전달되는 파라미터가 **안정적인지(stable)** 여부를 판단합니다.

  * **안정성(Stability)의 조건:** 타입의 public 프로퍼티가 변경되지 않음을 보장하거나, 변경될 경우 컴포지션에 알릴 수 있어야 합니다.
  * **`List<T>`의 문제:** 코틀린에서 `List<T>`는 **인터페이스**입니다. 인터페이스는 그 자체로 읽기 전용이지만, 실제 런타임 인스턴스는 변경 가능한 `ArrayList<T>`일 수 있습니다. 컴파일러는 `List`라는 타입 정보만으로는 그 내부 구현이 정말로 불변(immutable)인지 보장할 수 없습니다.
  * **컴파일러의 판단:** 이러한 불확실성 때문에, Compose 컴파일러는 안전을 위해 **`List<T>` 타입을 불안정한(unstable) 것으로 간주**합니다.
  * **결과:** 컴포저블에 불안정한 타입의 파라미터가 전달되면, **스마트 리컴포지션(Smart Recomposition)** 의 건너뛰기 최적화가 비활성화됩니다. 따라서 부모 컴포저블이 리컴포지션될 때, `List`의 내용이 실제로 변경되지 않았더라도 해당 `List`를 파라미터로 받는 자식 컴포저블은 **무조건 다시 리컴포지션**됩니다. 이것이 불필요한 성능 저하의 원인이 됩니다.

-----

### 2. 해결 전략: 불필요한 리컴포지션 최적화 방법

이 문제를 해결하고 불필요한 리컴포지션을 방지하기 위한 주요 전략은 컴포저블에 **안정적인 타입의 컬렉션을 전달**하는 것입니다.

#### 2.1. 불변 컬렉션(Immutable Collections) 사용 (가장 권장되는 방법) 🥇

코틀린에서 공식적으로 제공하는 불변 컬렉션 라이브러리를 사용하는 것이 가장 직접적이고 확실한 해결책입니다.

1.  **의존성 추가:** 모듈의 `build.gradle.kts` 파일에 `kotlinx-collections-immutable` 라이브러리를 추가합니다.
    ```kotlin
    implementation("org.jetbrains.kotlinx:kotlinx-collections-immutable:0.3.7") // 최신 버전 확인
    ```
2.  **타입 변경:** `ViewModel`의 상태 홀더나 컴포저블의 파라미터 타입을 `List<T>`에서 **`ImmutableList<T>`** 로 변경합니다.

**적용 예시:**

```kotlin
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList

// ViewModel
class MyViewModel : ViewModel() {
    private val _items = MutableStateFlow<ImmutableList<String>>(emptyList<String>().toImmutableList())
    val items: StateFlow<ImmutableList<String>> = _items.asStateFlow()

    fun loadItems() {
        viewModelScope.launch {
            val newList = repository.getItems() // repository가 List<String>을 반환한다고 가정
            _items.value = newList.toImmutableList() // ImmutableList로 변환하여 할당
        }
    }
}

// Composable
@Composable
fun MyItemList(items: ImmutableList<String>) { // 파라미터 타입을 ImmutableList로 변경
    Log.d("Recomposition", "MyItemList Recomposed")
    LazyColumn {
        items(items) { item ->
            Text(text = item)
        }
    }
}
```

  * **결과:** `ImmutableList`는 Compose 컴파일러가 **안정적인 타입**으로 인식합니다. 이제 `MyItemList`의 부모가 리컴포지션되더라도, `items` 파라미터의 인스턴스가 변경되지 않았다면 `MyItemList`의 리컴포지션은 효율적으로 건너뛰게 됩니다.

#### 2.2. 래퍼(Wrapper) 클래스와 `@Immutable` 어노테이션 사용

외부 라이브러리 의존성을 추가하고 싶지 않거나, `List` 타입을 유지하면서 안정성을 보장하고 싶을 때 사용하는 방법입니다.

1.  `List`를 감싸는 간단한 `data class`를 만듭니다.
2.  해당 클래스에 **`@Immutable`** 어노테이션을 붙여, 개발자가 이 클래스의 불변성을 컴파일러에게 보증합니다.

**적용 예시:**

```kotlin
import androidx.compose.runtime.Immutable

@Immutable // 이 클래스는 불변임을 컴파일러에게 약속
data class StableListWrapper<T>(
    val items: List<T>
)

// ViewModel
class MyViewModel : ViewModel() {
    private val _itemsWrapper = MutableStateFlow(StableListWrapper(emptyList<String>()))
    val itemsWrapper: StateFlow<StableListWrapper<String>> = _itemsWrapper.asStateFlow()
    // ...
}

// Composable
@Composable
fun MyItemList(itemsWrapper: StableListWrapper<String>) {
    // ...
}
```

  * **결과:** `StableListWrapper`는 `@Immutable` 어노테이션 덕분에 안정적인 타입으로 취급됩니다.
  * **주의점:** 이 방법은 컴파일러와의 "약속"입니다. 만약 래퍼 클래스 외부에서 원본 리스트를 수정하는 등 약속을 어기면, UI가 갱신되지 않는 등의 미묘한 버그가 발생할 수 있습니다.

#### 2.3. `LazyColumn` / `LazyRow`의 `key` 파라미터 활용

목록 데이터를 다룰 때는 `LazyColumn`이나 `LazyRow`를 사용하게 되는데, 이들의 성능을 최적화하는 데 `key` 파라미터가 매우 중요합니다.

  * **해결 전략:** `items` 블록에 각 아이템을 고유하게 식별할 수 있는 **안정적인 `key`를 제공**합니다.
    ```kotlin
    @Composable
    fun MyItemList(items: ImmutableList<Item>) { // 여전히 ImmutableList 사용을 권장
        LazyColumn {
            items(
                items = items,
                key = { item -> item.id } // 각 아이템의 고유하고 안정적인 ID를 키로 지정
            ) { item ->
                ListItem(item)
            }
        }
    }
    ```
  * **결과:** `key`를 제공하면, 리스트가 변경(추가, 삭제, 순서 변경)될 때 Compose는 어떤 아이템이 추가/삭제/이동되었는지 명확히 인지할 수 있습니다. 이는 **불필요한 아이템 컴포저블의 소멸 및 재생성을 최소화**하고, 기존 컴포저블 인스턴스를 효율적으로 재사용(재배치)하게 하여 `LazyColumn`의 내부적인 리컴포지션 성능을 크게 향상시킵니다.
  * **중요:** 이 전략은 `LazyColumn` **내부의 아이템들**에 대한 리컴포지션을 최적화하는 것입니다. `MyItemList`라는 **부모 컴포저블 자체**의 불필요한 리컴포지션을 막으려면, 여전히 파라미터로 전달되는 `items` 리스트가 **안정적인 타입**(예: `ImmutableList`)이어야 합니다.

-----

### 3. 결론

`List`를 파라미터로 받는 컴포저블의 불필요한 리컴포지션을 최적화하는 가장 확실하고 권장되는 방법은 **`kotlinx.collections.immutable` 라이브러리의 `ImmutableList`를 사용**하여 파라미터 자체를 안정적인 타입으로 만드는 것입니다. 이것이 여의치 않다면 `@Immutable` 어노테이션을 붙인 래퍼 클래스를 사용하는 차선책이 있으며, `LazyColumn` 등을 사용할 때는 항상 고유한 `key`를 제공하여 내부적인 렌더링 효율성도 함께 높여야 합니다.


## Q. 앱에서 리컴포지션 효율성을 향상시키기 위해 어떤 API나 Compose 컴파일러 기능을 사용해 보았나요?

Jetpack Compose 앱의 리컴포지션(recomposition) 효율성을 향상시키기 위해서는 Compose 런타임과 컴파일러의 동작 방식을 이해하고, 제공되는 다양한 API와 컴파일러 기능을 전략적으로 활용해야 합니다. 다음은 제가 불필요한 리컴포지션을 줄이고 성능을 최적화하기 위해 사용해 본 주요 API 및 기능들입니다.

-----

### 1. 문제 식별: 최적화의 첫걸음 🧐

최적화를 시작하기 전, 먼저 어떤 컴포저블이 불필요하게 자주 리컴포지션되는지 식별해야 합니다.

  * **레이아웃 검사기(Layout Inspector):** Android Studio의 이 도구를 사용하여 각 컴포저블의 리컴포지션 횟수와 건너뛰기(skip) 횟수를 시각적으로 확인합니다. 비정상적으로 횟수가 높은 컴포저블이 주요 최적화 대상입니다.
  * **컴포지션 추적(Composition Tracing):** 더 심층적인 분석을 위해 시스템 트레이싱을 사용하여 어떤 상태 변경이 특정 리컴포지션을 유발했는지 추적합니다.

-----

### 2. 코드 수준에서의 최적화: 주요 API 및 패턴

#### 2.1. `remember(key)`: 불필요한 재계산 방지

`remember`는 리컴포지션 간에 값을 기억하는 가장 기본적인 API입니다. 특히 비용이 많이 드는 계산을 수행할 때, 이 계산이 의존하는 값들을 `key`로 지정하면 성능을 크게 향상시킬 수 있습니다.

  * **전략:** `remember` 블록은 `key` 파라미터 값이 변경될 때만 재실행됩니다. 따라서 비싼 연산(예: 리스트 정렬, 필터링, 복잡한 문자열 포맷팅)의 결과는 해당 연산의 입력값을 `key`로 하는 `remember` 블록 안에 두어 불필요한 재계산을 방지합니다.

<!-- end list -->

```kotlin
@Composable
fun UserProfile(user: User) {
    // user.id가 변경될 때만 introductionText를 다시 계산합니다.
    val introductionText = remember(user.id) {
        // 비용이 많이 드는 문자열 생성 작업이라고 가정
        formatIntroduction(user)
    }
    Text(text = introductionText)
}
```

#### 2.2. `derivedStateOf`: 파생 상태의 현명한 관찰

여러 상태 값으로부터 파생되는 상태를 계산할 때, `derivedStateOf`는 매우 효과적인 최적화 도구입니다.

  * **전략:** `remember { derivedStateOf { ... } }`를 사용하면, 내부 블록은 의존하는 `State`가 변경될 때만 재실행됩니다. 더 중요한 점은, 이 블록의 **계산 결과값이 실제로 변경될 때만** `derivedStateOf`의 결과를 읽는 컴포저블이 리컴포지션된다는 것입니다.
  * **사용 사례:** 스크롤 가능한 리스트에서 "맨 위로 가기" 버튼의 표시 여부를 결정할 때 유용합니다. 스크롤 위치(`firstVisibleItemIndex`)는 계속 바뀌지만, `firstVisibleItemIndex > 0` 이라는 조건의 결과(`true` 또는 `false`)는 자주 바뀌지 않기 때문에 불필요한 리컴포지션을 막아줍니다.

<!-- end list -->

```kotlin
val listState = rememberLazyListState()
// listState.firstVisibleItemIndex는 스크롤 시마다 계속 변경됩니다.
// 하지만 derivedStateOf의 결과값(true/false)은 0과 1 사이를 오갈 때만 변경됩니다.
val showScrollToTopButton by remember {
    derivedStateOf { listState.firstVisibleItemIndex > 0 }
}

if (showScrollToTopButton) {
    ScrollToTopButton(...)
}
```

#### 2.3. 불변 컬렉션: `List` 파라미터 안정성 확보

코틀린의 표준 `List<T>` 인터페이스는 Compose 컴파일러에 의해 불안정(unstable)하다고 간주되어 불필요한 리컴포지션을 유발합니다.

  * **전략:** **`kotlinx.collections.immutable`** 라이브러리의 `ImmutableList<T>`를 사용합니다. 이 타입은 컴파일러가 안정적이라고 인식하므로, 리스트 내용이 실제로 변경되지 않았다면 리컴포지션을 효과적으로 건너뛸 수 있습니다.

#### 2.4. 상태 대신 람다(Lambda) 전달: 리컴포지션 범위 최소화

하위 컴포저블이 상태 값을 직접 읽을 필요 없이 단지 상위 컴포저블의 상태를 변경하는 이벤트만 트리거해야 하는 경우, 상태 값 자체 대신 **이벤트를 처리하는 람다 함수**를 전달합니다.

  * **전략:** 이렇게 하면 하위 컴포저블은 상위의 상태 변화에 직접적으로 의존하지 않게 되어, 해당 상태가 변경되어도 리컴포지션되지 않습니다. 이는 리컴포지션 범위를 최소화하는 핵심적인 패턴입니다.

#### 2.5. `LazyColumn`/`LazyRow`의 `key` 사용: 아이템 재사용성 극대화

`LazyColumn`이나 `LazyRow`에 아이템 목록을 표시할 때, 각 아이템에 고유하고 안정적인 `key`를 제공하는 것은 매우 중요합니다.

  * **전략:** `items` 블록의 `key` 파라미터에 각 데이터 아이템의 고유 ID와 같은 안정적인 값을 지정합니다.
  * **효과:** 이를 통해 Compose는 리스트가 변경되었을 때 어떤 아이템이 추가, 삭제, 이동되었는지 정확히 파악할 수 있습니다. 이는 기존 컴포저블 인스턴스를 소멸시키고 새로 만드는 대신 효율적으로 재사용(재배치)하게 하여 `Lazy` 목록의 내부 성능을 크게 향상시킵니다.

-----

### 3. 컴파일러 수준에서의 최적화: 컴파일러에게 힌트 제공

#### 3.1. 안정성 어노테이션 (`@Immutable`, `@Stable`)

컴파일러가 안정성을 자동으로 추론할 수 없는 클래스(예: `List`를 포함한 클래스, 외부 라이브러리 클래스)에 대해, 개발자는 `@Immutable`이나 `@Stable` 어노테이션을 붙여 안정적임을 명시적으로 알릴 수 있습니다.

  * **전략:** 이 "약속"을 통해 개발자는 컴파일러가 해당 클래스를 안정적인 것으로 취급하고 리컴포지션 건너뛰기 최적화를 수행하도록 유도할 수 있습니다.

#### 3.2. 안정성 설정 파일 (Stability Configuration File)

직접 수정할 수 없는 타사 라이브러리의 클래스 등을 안정적인 것으로 취급하도록 프로젝트 전역에 걸쳐 설정할 수 있는 기능입니다.

  * **전략:** `compose_compiler_config.conf` 파일을 만들어 안정적으로 취급할 클래스의 전체 경로를 나열하고, `build.gradle.kts` 파일에서 이 설정 파일을 참조하도록 구성합니다. 이는 래퍼 클래스를 만드는 수고를 덜어줍니다.

#### 3.3. 강력한 건너뛰기 모드 (Strong Skipping Mode)

실험적인 기능으로, 불안정한 파라미터를 포함한 컴포저블에 대해서도 리컴포지션 건너뛰기를 더 공격적으로 시도하게 만드는 컴파일러 옵션입니다. 불안정한 파라미터에 대해서는 인스턴스 동일성(`===`) 비교를 통해 변경 여부를 판단합니다.

-----

### 4. 결론

효율적인 리컴포지션 관리는 단 하나의 방법만으로 해결되지 않습니다. **먼저 `레이아웃 검사기`와 같은 도구로 성능 병목 지점을 정확히 식별**하고, 이후 상황에 맞는 최적화 전략을 복합적으로 적용해야 합니다. 일반적으로 **상태 읽기를 최소화하고(`derivedStateOf` 사용, 람다 전달), 파라미터의 안정성을 보장(`ImmutableList`, `@Immutable` 어노테이션 사용)하며, `remember`와 `LazyColumn`의 `key`를 올바르게 활용**하는 것이 앱의 성능을 크게 향상시키는 가장 효과적인 방법들입니다.