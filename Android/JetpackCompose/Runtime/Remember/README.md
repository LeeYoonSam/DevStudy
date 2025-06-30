# remember와 rememberSaveable의 차이점은 무엇인가요?

Jetpack Compose에서 상태 관리는 UI가 데이터 변경에 동적으로 반응할 수 있게 하는 핵심 개념입니다. **`remember`** 와 **`rememberSaveable`** 은 모두 리컴포지션(recomposition) 시에도 상태를 유지하는 API이지만, 서로 다른 목적을 가지며 별개의 시나리오에 적합합니다.

-----

## `remember` 이해하기

### 목적

`remember` API는 값을 메모리에 저장하고 **리컴포지션 간에 그 값을 유지**합니다. 그러나 화면 회전과 같은 **구성 변경(configuration change)이나 프로세스 재시작 중에는 상태를 유지하지 못합니다.**

### 사용 사례

상태가 구성 변경 후에도 유지될 필요가 없을 때 `remember`를 사용합니다. 예를 들어, 화면 회전 시 초기화되어도 괜찮은 임시 카운터가 있습니다.

```kotlin
@Composable
fun RememberExample() {
    var count by remember { mutableStateOf(0) }

    Button(onClick = { count++ }) {
        Text("Clicked $count times")
    }
}
```

### 동작

`remember`는 현재 컴포지션 생명주기 내에서만 상태를 저장하기 때문에, 기기가 회전되면 `count` 변수는 0으로 재설정됩니다.

-----

## `rememberSaveable` 이해하기

### 목적

`rememberSaveable` API는 **구성 변경 시에도 상태를 유지**함으로써 `remember`의 기능을 확장합니다. 이는 `Bundle`에 저장될 수 있는 값들을 자동으로 저장하고 복원합니다.

### 사용 사례

양식(form)의 입력값이나 내비게이션 상태와 같이 구성 변경 후에도 유지되어야 하는 상태에 `rememberSaveable`을 사용합니다.

```kotlin
@Composable
fun RememberSaveableExample() {
    var text by rememberSaveable { mutableStateOf("") }

    OutlinedTextField(
        value = text,
        onValueChange = { text = it },
        label = { Text("Enter text") }
    )
}
```

### 동작

`text` 값은 화면 회전이나 구성 변경 시에도 유지되어 원활한 사용자 경험을 보장합니다.

-----

## 주요 차이점

| 특징 | `remember` | `rememberSaveable` |
| --- | --- | --- |
| **유지 범위** | 현재 컴포지션 생명주기 동안에만 상태 유지 | 컴포지션 및 구성 변경 시에도 상태 유지 |
| **저장 위치** | 메모리에 값 저장 | 메모리에 값을 저장하고 `Bundle`에도 저장함 |
| **사용자 정의 Saver 지원** | 해당 없음 | 복잡한 객체를 위한 사용자 정의 Saver 지원 |

-----

## 언제 사용해야 하나요?

  * **`remember` 사용 시점:** 애니메이션 상태나 임시 UI 상태와 같이 현재 컴포지션을 넘어서까지 유지될 필요가 없는 일시적인 상태에 사용합니다.
  * **`rememberSaveable` 사용 시점:** 사용자 입력, 선택 상태, 또는 양식 데이터와 같이 구성 변경 후에도 유지되어야 하는 상태에 사용합니다.

-----

## 요약

`remember`와 `rememberSaveable`은 Jetpack Compose에서 상태를 관리하기 위한 필수적인 API입니다. `remember`는 단일 컴포지션 내의 임시 상태에 적합한 반면, `rememberSaveable`은 구성 변경 시에도 상태 유지를 보장합니다. 그러나 `rememberSaveable`의 추가 기능에는 약간의 오버헤드가 따르므로, 모든 시나리오에 항상 최선의 선택은 아닐 수 있습니다. 둘의 차이점을 이해하고 각각을 적절하게 사용하면 애플리케이션의 요구 사항에 따라 가장 효율적인 API를 선택하는 데 도움이 됩니다.

-----

## 💡 프로 팁: remember와 rememberSaveable 내부 구현

`remember`와 `rememberSaveable`의 목적을 탐색했으니, 이제 그 내부 구현을 검토하여 더 깊이 파고들어 보겠습니다.

### `remember` 내부 구현 이해하기

`remember` 함수의 내부 구현은 다음과 같습니다.

```kotlin
@Composable
inline fun <T> remember(crossinline calculation: @DisallowComposableCalls () -> T): T =
    currentComposer.cache(false, calculation)
```

코드에서 볼 수 있듯이, `remember`는 내부적으로 `Composer` 인스턴스의 `cache` 함수를 호출합니다. `cache` 함수가 구현된 방식은 다음과 같습니다.

```kotlin
@ComposeCompilerApi
inline fun <T> Composer.cache(invalid: Boolean, block: @DisallowComposableCalls () -> T): T {
    @Suppress("UNCHECKED_CAST")
    return rememberedValue().let {
        if (invalid || it === Composer.Empty) {
            val value = block()
            updateRememberedValue(value)
            value
        } else it
    } as T
}
```

이 코드는 `remember`가 내부적으로 어떻게 작동하는지 보여줍니다. 이는 Compose 컴파일러 플러그인 API와 상호작용하여 컴포지션 데이터에 값을 캐시합니다. 구체적으로, 값이 유효하지 않거나 초기화되지 않았는지(`Composer.Empty`로 표시됨) 확인합니다. 만약 그렇다면, 제공된 블록을 사용하여 값을 계산하고, 이를 컴포지션 데이터에 저장한 후 반환합니다. 그렇지 않으면, 이전에 기억된 값을 단순히 검색하여 반환합니다.

이 메커니즘은 값이 리컴포지션 간에 효율적으로 유지되면서 중복 재계산을 피하도록 보장합니다. 이는 Compose 컴파일러 플러그인이 그러한 최적화가 안전하다고 판단할 때 `remember` 호출을 최적화하는 데 사용됩니다.

### `rememberSaveable` 내부 구현 이해하기

`rememberSaveable` 함수의 내부 구현은 다음과 같습니다.

```kotlin
@Composable
fun <T : Any> rememberSaveable(
    vararg inputs: Any?,
    saver: Saver<T, out Any> = autoSaver(),
    key: String? = null,
    init: () -> T
): T {
    val compositeKey = currentCompositeKeyHash
    // key는 사용자가 제공한 것이나 compose 런타임이 생성한 것
    val finalKey = if (!key.isNullOrEmpty()) {
        key
    } else {
        compositeKey.toString(MaxSupportedRadix)
    }
    @Suppress("UNCHECKED_CAST")
    (saver as Saver<T, Any>)

    val registry = LocalSaveableStateRegistry.current

    val holder = remember {
        // 값은 레지스트리를 사용해 복원되거나 [init] 람다를 통해 생성됨
        val restored = registry?.consumeRestored(finalKey)?.let {
            saver.restore(it)
        }
        val finalValue = restored ?: init()
        SaveableHolder(saver, registry, finalKey, finalValue, inputs)
    }

    val value = holder.getValueIfInputsDidntChange(inputs) ?: init()
    SideEffect {
        holder.update(saver, registry, finalKey, value, inputs)
    }

    return value
}
```

처음에는 약간 복잡해 보일 수 있지만, 단계별로 분석해 보겠습니다. `rememberSaveable` 함수는 구성 변경 및 프로세스 종료 시에도 상태를 저장하고 복원하는 지원을 추가하여 `remember`의 기능을 확장합니다. 아래는 그 내부 구현에 대한 분석입니다.

  * **키 생성 (Key Generation):**
    `key` 파라미터는 사용자가 사용자 정의 키를 제공할 수 있게 합니다. 제공되지 않으면, 현재 컴포지션의 해시를 사용하여 복합 키가 자동으로 생성됩니다.

    ```kotlin
    val compositeKey = currentCompositeKeyHash
    val finalKey = if (!key.isNullOrEmpty()) {
        key
    } else {
        compositeKey.toString(MaxSupportedRadix)
    }
    ```

  * **상태 복원 (State Restoration):**
    `LocalSaveableStateRegistry`는 주어진 키에 대해 이전에 저장된 값을 검색하는 데 사용됩니다. 저장된 값이 존재하면, 제공된 `Saver`를 사용하여 복원됩니다.

    ```kotlin
    val registry = LocalSaveableStateRegistry.current
    val restored = registry?.consumeRestored(finalKey)?.let {
        saver.restore(it)
    }
    ```

  * **기본값 초기화 (Default Value Initialization):**
    복원된 값이 없으면, `init` 람다를 사용하여 기본값이 초기화됩니다.

    ```kotlin
    val finalValue = restored ?: init()
    ```

  * **Saveable 홀더 (Saveable Holder):**
    `SaveableHolder`는 상태, saver, 레지스트리 및 입력을 관리하기 위해 생성됩니다.

    ```kotlin
    SaveableHolder(saver, registry, finalKey, finalValue, inputs)
    ```

  * **입력 변경 처리 (Input Change Handling):**
    `rememberSaveable`에 대한 입력이 변경되면, 상태는 무효화되고 값은 다시 초기화됩니다.

    ```kotlin
    val value = holder.getValueIfInputsDidntChange(inputs) ?: init()
    ```

  * **부작용 (Side Effects):**
    `SideEffect`는 리컴포지션 중에 업데이트된 상태가 레지스트리에 저장되도록 보장합니다.

    ```kotlin
    SideEffect {
        holder.update(saver, registry, finalKey, value, inputs)
    }
    ```

이것이 `rememberSaveable`의 내부 구현입니다. `remember`와 달리, `LocalSaveableStateRegistry`를 사용하여 화면 회전과 같은 구성 변경 시에도 상태를 유지함으로써 한 단계 더 나아갑니다. 또한, `saver` 파라미터는 개발자가 사용자 정의 직렬화 및 역직렬화 로직을 정의할 수 있게 하여, 복잡한 객체를 원활하게 처리하는 데 이상적입니다.

-----

## Q. 어떤 상황에서 `remember` 대신 `rememberSaveable`을 사용하는 것이 선호되며, 어떤 트레이드오프를 고려해야 하나요?

Jetpack Compose에서 상태를 관리할 때 `remember`와 `rememberSaveable`은 모두 리컴포지션(recomposition) 간에 상태를 유지하는 중요한 역할을 하지만, 그 **유지 범위**와 **작동 방식**에 근본적인 차이가 있습니다. 어떤 것을 선택할지는 해당 상태가 얼마나 오래 유지되어야 하는지에 따라 결정됩니다.

-----

### 1. `rememberSaveable`을 사용하는 것이 선호되는 시나리오

`rememberSaveable`은 `remember`의 기능을 포함하면서, 추가적으로 **예기치 않은 UI 컨트롤러(액티비티, 프래그먼트)의 재생성에도 상태를 보존**해야 할 때 사용됩니다.

#### 1.1. 구성 변경(Configuration Changes) 시 상태 유지

  * **시나리오:** 사용자가 기기를 회전시키거나, 다크 모드로 전환하거나, 언어를 변경하는 등 구성 변경이 발생하면 안드로이드 시스템은 일반적으로 액티비티를 파괴하고 다시 생성합니다.
  * **사용 이유:** `remember`로 저장된 상태는 이러한 구성 변경 시 사라지지만, **`rememberSaveable`로 저장된 상태는 그대로 유지**됩니다.
  * **주요 예시:**
      * 사용자가 양식(form)에 입력한 텍스트.
      * `Slider`의 현재 위치 값.
      * `LazyColumn`이나 `LazyRow`의 현재 스크롤 위치.
      * 현재 선택된 탭(Tab)의 인덱스.

#### 1.2. 시스템에 의한 프로세스 종료(Process Death) 후 상태 복원

  * **시나리오:** 사용자가 앱을 백그라운드로 보낸 상태에서, 안드로이드 시스템이 메모리 확보를 위해 앱의 프로세스를 강제로 종료할 수 있습니다. 이후 사용자가 다시 앱으로 돌아오면 시스템은 앱을 새로운 프로세스에서 다시 시작합니다.
  * **사용 이유:** `rememberSaveable`은 내부적으로 상태를 `Bundle`에 저장하는 메커니즘을 사용하므로, 시스템에 의해 프로세스가 종료되었다가 복원될 때도 **상태를 복원**할 수 있습니다. `remember`는 프로세스 종료 시 상태를 완전히 잃어버립니다.

요약하자면, 사용자가 화면과 상호작용하며 만든 **UI 상태가 예기치 않은 소멸(화면 회전 등) 이후에도 그대로 유지되기를 기대하는 모든 경우**에 `rememberSaveable`을 사용하는 것이 선호됩니다.

-----

### 2. 고려해야 할 트레이드오프(Trade-offs)

`rememberSaveable`은 더 강력한 기능을 제공하지만, `remember`에 비해 다음과 같은 비용과 제약사항이 따릅니다.

#### 2.1. 성능 및 메모리 오버헤드

  * **작동 방식:** `rememberSaveable`은 상태를 `Bundle`에 저장하기 위해 **직렬화(Serialization)** 및 **역직렬화(Deserialization)** 과정을 거칩니다.
  * **비용:** 이 과정은 단순한 메모리 참조보다 더 많은 CPU 연산을 필요로 하므로 약간의 성능 오버헤드가 발생할 수 있습니다. 특히 복잡한 객체를 사용자 정의 `Saver`를 통해 저장할 경우 이 비용은 더 커질 수 있습니다.
  * **`remember`와의 비교:** `remember`는 단순히 객체를 컴포지션의 슬롯 테이블(메모리)에 유지하므로 매우 빠르고 오버헤드가 거의 없습니다. `rememberSaveable`은 `remember`의 기능에 추가적으로 `Bundle` 저장/복원 비용이 더해진 것입니다.

#### 2.2. 저장 가능한 데이터 타입 및 크기 제한

  * **작동 방식:** `Bundle`에 저장하는 메커니즘에 의존하기 때문에, `rememberSaveable`은 **`Bundle`에 저장할 수 있는 타입**만 자동으로 처리할 수 있습니다.
  * **지원 타입:** `Int`, `String`과 같은 기본 타입과 `Parcelable` 또는 `Serializable`을 구현한 객체들이 기본적으로 지원됩니다.
  * **미지원 타입 및 해결책:** 기본적으로 지원되지 않는 사용자 정의 객체를 저장하려면, 해당 객체를 `Bundle`에 저장 가능한 형태로 변환하는 방법을 알려주는 **사용자 정의 `Saver`를 직접 구현**해야 합니다. 이는 추가적인 코드를 필요로 합니다.
  * **크기 제한:** `Bundle`은 트랜잭션 데이터 크기에 제한이 있으므로, `rememberSaveable`은 대용량 데이터(예: 이미지 비트맵, 긴 동영상 목록)를 저장하는 데 적합하지 않습니다.
  * **`remember`와의 비교:** `remember`는 타입이나 크기에 제한 없이 (메모리에 들어갈 수만 있다면) 어떤 종류의 객체든 참조를 유지할 수 있습니다.

#### 2.3. 구현 복잡성 증가 (사용자 정의 타입의 경우)

  * 위에서 언급했듯이, 지원되지 않는 타입을 저장하기 위해 `Saver`를 작성해야 하는 것은 `remember`를 사용하는 것보다 구현 복잡성을 증가시킵니다.

-----

### 3. 요약: 언제 무엇을 선택해야 하는가?

| 상황 | `remember` 사용 | `rememberSaveable` 사용 |
| :--- | :--- | :--- |
| **상태의 생존 범위** | 리컴포지션 간에만 유지되면 충분할 때 | 구성 변경 및 프로세스 종료 후에도 유지가 필요할 때 |
| **데이터의 성격** | - 일시적인 UI 상태 (예: 애니메이션 값)</br>- 재계산 비용이 저렴한 상태</br>- ViewModel과 같은 상위 상태 홀더로부터 파생된 상태 | - 사용자의 직접적인 입력 값</br>- 스크롤 위치, 탭 선택 등 UI 세션 상태</br>- 복원되지 않으면 사용자 경험을 해치는 중요한 UI 상태 |
| **성능 및 복잡도** | 오버헤드가 거의 없고 사용이 매우 간단 | 약간의 성능/메모리 오버헤드 존재. 커스텀 타입 저장 시 복잡도 증가. |


**권장 접근 방식:**
**기본적으로 `remember`를 사용**하되, **화면 회전과 같은 구성 변경에도 반드시 유지되어야 하는 상태**라고 판단될 때만 **`rememberSaveable`로 전환**하는 것이 좋습니다. ViewModel이 관리하는 데이터는 이미 구성 변경 시 유지되므로, UI에서 해당 데이터를 다시 `rememberSaveable`로 감쌀 필요는 없습니다.

-----

### 4. 결론

`remember`와 `rememberSaveable`의 가장 큰 차이점은 **상태 유지 범위**입니다. `remember`는 컴포지션이 활성 상태일 때만 데이터를 유지하는 가벼운 메모리 캐시인 반면, `rememberSaveable`은 `Bundle`을 활용하여 구성 변경과 같은 더 긴 생존 주기에 걸쳐 상태를 보존하는 더 강력한 메커니즘입니다. 따라서 개발자는 상태의 중요도와 생존 요구사항을 고려하여 두 API 중 더 적합한 것을 선택해야 하며, 이 과정에서 발생하는 성능 및 구현 복잡성의 트레이드오프를 이해하는 것이 중요합니다.


## Q. 기본적으로 지원되지 않는 사용자 정의 비-원시(non-primitive) 상태를 `rememberSaveable`로 저장하려면 어떻게 해야 하나요?

Jetpack Compose의 `rememberSaveable`은 기본적으로 `Int`, `String`, `Boolean`과 같은 기본(primitive) 타입과 `Parcelable` 인터페이스를 구현한 객체의 상태를 자동으로 저장하고 복원할 수 있습니다. 하지만, 이러한 기본 지원 타입에 해당하지 않는 사용자 정의 클래스나 외부 라이브러리의 객체 상태를 유지하려면, 개발자가 직접 해당 객체를 **어떻게 저장하고 복원할지**에 대한 방법을 `rememberSaveable`에게 알려주어야 합니다.

이때 사용되는 핵심 메커니즘이 바로 **`Saver`** 입니다.

-----

### 1. 문제점: 기본적으로 지원되지 않는 사용자 정의 타입

`rememberSaveable`은 내부적으로 상태를 안드로이드의 `Bundle`에 저장하여 구성 변경이나 프로세스 종료 시에도 데이터를 보존합니다. 따라서 `Bundle`에 저장할 수 없는 형식의 객체는 `rememberSaveable`이 자동으로 처리할 수 없습니다.

예를 들어, 다음과 같은 사용자 정의 데이터 클래스가 있다고 가정해 봅시다.

```kotlin
data class ImageState(val url: String, val caption: String)
```

이 `ImageState` 클래스는 `Parcelable`이나 `Serializable`을 구현하지 않았으므로, `rememberSaveable`은 이 객체를 어떻게 `Bundle`에 넣고 꺼내야 할지 알지 못합니다.

-----

### 2. 해결책: `Saver`를 이용한 상태 저장 및 복원 로직 정의

#### 2.1. `Saver`란 무엇인가?

`Saver`는 사용자 정의 타입(`Original`)을 `Bundle`에 저장 가능한 타입(`Saveable`)으로 변환하고, 그 반대로 `Saveable` 타입에서 다시 `Original` 타입으로 복원하는 **양방향 변환 규칙을 정의하는 객체**입니다.

`Saver`는 두 개의 핵심 함수를 가집니다.

1.  **`save`:** `Original` 타입의 객체를 `Saveable` 타입으로 변환하는 방법을 정의합니다.
2.  **`restore`:** `Saveable` 타입의 데이터를 다시 `Original` 타입의 객체로 복원하는 방법을 정의합니다.

#### 2.2. 구현 단계

**1단계: 사용자 정의 데이터 클래스 정의**
먼저 저장하고자 하는 데이터 클래스를 정의합니다.

```kotlin
data class ImageState(val url: String, val caption: String)
```

**2단계: `Saver` 객체 생성**
`ImageState` 객체를 `Bundle`에 저장 가능한 형태(예: `List<String>`)로 변환하고 복원하는 로직을 담은 `Saver`를 만듭니다. `listSaver`는 이를 간편하게 만들어주는 헬퍼 함수입니다.

```kotlin
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.listSaver

// ImageState 객체를 List<String>으로, List<String>을 다시 ImageState 객체로 변환하는 Saver
val ImageStateSaver = listSaver<ImageState, Any>(
    save = { imageState ->
        // ImageState 객체의 프로퍼티들을 List로 변환
        listOf(imageState.url, imageState.caption)
    },
    restore = { list ->
        // List로부터 ImageState 객체 복원
        ImageState(url = list[0] as String, caption = list[1] as String)
    }
)
```

**3단계: `rememberSaveable`에 `Saver` 적용**
컴포저블 내에서 `rememberSaveable`을 호출할 때, `saver` 파라미터에 위에서 생성한 `ImageStateSaver`를 전달합니다.

```kotlin
@Composable
fun ProfileImage() {
    var imageState by rememberSaveable(stateSaver = ImageStateSaver) {
        mutableStateOf(ImageState("https://example.com/image.png", "초기 캡션"))
    }

    Column {
        Image(
            painter = rememberAsyncImagePainter(imageState.url),
            contentDescription = imageState.caption
        )
        Text(text = imageState.caption)
        
        // 예시: 캡션을 변경하는 버튼
        Button(onClick = { imageState = imageState.copy(caption = "변경된 캡션") }) {
            Text("캡션 변경")
        }
    }
}
```

  * **작동 방식:** 구성 변경이나 프로세스 종료가 예상될 때, `rememberSaveable`은 `ImageStateSaver`의 `save` 람다를 호출하여 `ImageState("...", "...")` 객체를 `listOf("...", "...")`로 변환하고 `Bundle`에 저장합니다. 이후 UI가 재생성될 때, `Bundle`에서 `List`를 꺼내 `restore` 람다를 호출하여 `ImageState` 객체를 다시 만들어냅니다.

-----

### 3. 다른 `Saver` 유형: `mapSaver`와 커스텀 `Saver`

  * **`mapSaver`:** 객체를 `List` 대신 `Map<String, Any>` 형태로 변환하여 저장하고 싶을 때 사용하는 헬퍼 함수입니다. 키-값 쌍이 더 명시적일 때 유용할 수 있습니다.
  * **`Saver`:** `listSaver`나 `mapSaver`로 표현하기 어려운 더 복잡한 변환 로직이 필요할 때, `Saver` 인터페이스를 직접 구현하여 사용자 정의할 수 있습니다.

-----

### 4. (참고) `@Parcelize` 어노테이션 활용

만약 사용자 정의 클래스를 **`Parcelable`** 로 만들 수 있다면, 훨씬 더 간단한 방법이 있습니다.

1.  **`kotlin-parcelize` 플러그인 추가:** `build.gradle.kts` 파일의 `plugins` 블록에 `id("kotlin-parcelize")`를 추가합니다.
2.  **클래스에 `@Parcelize` 어노테이션 추가:** 데이터 클래스에 `@Parcelize` 어노테이션을 붙이고 `Parcelable` 인터페이스를 구현합니다.

<!-- end list -->

```kotlin
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize // Parcelable 구현을 자동으로 생성
data class ImageState(val url: String, val caption: String) : Parcelable
```

  * **사용법:** 클래스가 `Parcelable`을 구현하면, **`rememberSaveable`은 이를 자동으로 인식하여 `Saver`를 명시적으로 전달할 필요 없이** 상태를 저장하고 복원할 수 있습니다. 이것이 사용자 정의 객체를 처리하는 가장 간편하고 권장되는 방법 중 하나입니다.

<!-- end list -->

```kotlin
@Composable
fun ProfileImage() {
    // Saver를 명시적으로 전달할 필요 없음
    var imageState by rememberSaveable {
        mutableStateOf(ImageState("https://example.com/image.png", "초기 캡션"))
    }
    // ...
}
```

-----

### 5. 결론

기본적으로 지원되지 않는 사용자 정의 객체를 `rememberSaveable`로 저장하기 위한 방법은 두 가지입니다.

1.  **`@Parcelize` 사용 (가장 간편하고 권장됨):** 객체를 `Parcelable`로 만들어 `rememberSaveable`이 자동으로 처리하도록 합니다.
2.  **사용자 정의 `Saver` 구현:** 객체를 `Parcelable`로 만들 수 없는 경우(예: 외부 라이브러리 클래스)나 다른 저장 방식으로 변환해야 할 때, `Saver` 객체(주로 `listSaver` 또는 `mapSaver` 사용)를 직접 구현하여 `rememberSaveable`의 `saver` 파라미터로 전달합니다.

이러한 방법들을 통해 개발자는 거의 모든 종류의 상태를 구성 변경 및 프로세스 종료에도 불구하고 안전하게 유지할 수 있습니다.