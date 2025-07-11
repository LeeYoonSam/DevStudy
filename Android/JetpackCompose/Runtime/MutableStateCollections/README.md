# `mutableStateListOf`와 `mutableStateMapOf`란 무엇인가?

`State`는 그 값이 변경될 때마다 리컴포지션을 트리거하여 UI를 동적으로 업데이트하는 데 핵심적인 도구입니다. 그러나 `List`나 `Map`과 같은 컬렉션으로 작업할 때, 표준 수정 메서드(예: `add`, `remove`)는 `State`에 변경 사항을 알리지 않습니다. 결과적으로, `State` 내의 변경 가능한(mutable) 컬렉션에 대한 직접적인 업데이트는 리컴포지션을 트리거하지 않아, 내부 수준(아이템)의 변경을 추적할 수 없게 됩니다. 다음 예시를 고려해 보세요.

```kotlin
// 이 방식은 recomposition을 트리거하지 않습니다.
val mutableList by remember { mutableStateOf(mutableListOf("skydoves", "android")) }

LazyColumn {
    item {
        Button(
            onClick = { mutableList.add("kotlin") } // 이 코드는 리컴포지션을 트리거하지 않음
        ) {
            Text(text = "Add")
        }
    }

    items(items = mutableList) { item ->
        Text(text = item)
    }
}
```

이 경우, `mutableList`에 아이템을 추가해도 Compose에 변경 사항이 통지되지 않아 UI가 예상대로 업데이트되지 않습니다. 컬렉션의 아이템 변경을 올바르게 추적하기 위해, Compose는 **`mutableStateListOf`** 와 **`mutableStateMapOf`** 를 제공합니다. 이러한 특화된 상태 보유 컬렉션들은 Compose의 스냅샷 시스템(snapshot system)과 통합되어, 요소가 변경될 때 리컴포지션이 발생하도록 보장합니다. 이 API들을 사용하면 반응형 UI 업데이트를 유지하면서 관찰 가능한(observable) 리스트와 맵을 효율적으로 관리할 수 있습니다.

-----

## mutableStateListOf

`mutableStateListOf`는 **`SnapshotStateList`** 를 생성하며, 이는 일반적인 `MutableList`처럼 동작하지만 Compose에 최적화되어 있습니다. 이 리스트에 대한 모든 변경 사항은 데이터가 사용되는 곳에서만 리컴포지션을 트리거합니다.

```kotlin
val items = mutableStateListOf("android", "kotlin", "skydoves")
```

아이템을 추가하거나 제거하는 등 리스트를 수정하면, Compose는 변경을 감지하고 상태와 UI를 그에 맞게 업데이트합니다.

```kotlin
items.add("Jetpack Compose") // UI에 "Jetpack Compose"가 추가되어 리컴포지션됨
items.removeAt(0)          // UI에서 "android"가 제거되어 리컴포지션됨
```

-----

## mutableStateMapOf

`mutableStateMapOf`는 **`SnapshotStateMap`** 을 생성하며, `MutableMap`과 유사하게 작동하면서 데이터가 변경될 때 상태 및 UI 업데이트를 보장합니다.

```kotlin
val userSettings = mutableStateMapOf("theme" to "dark", "notifications" to "enabled")
```

값을 업데이트하면 의존하는 UI 요소에서 리컴포지션이 트리거됩니다.

```kotlin
userSettings["theme"] = "light"      // UI 업데이트 트리거
userSettings.remove("notifications") // UI 업데이트 트리거
```

-----

## 일반적인 사용 사례

`mutableStateListOf`와 `mutableStateMapOf`는 Compose에서 반응성을 유지하면서 UI 관련 상태를 보유하는 데 일반적으로 사용됩니다. 이들은 Compose의 스냅샷 시스템과 통합되므로, 필요한 UI 컴포넌트만 리컴포지션되도록 보장합니다.

```kotlin
// ViewModel 내에서 사용 예시 1
class UserViewModel : ViewModel() {
    private val mutableUserList = mutableStateListOf("skydoves", "kotlin", "android")
    // ViewModel 외부로 안전하게 노출하기 위해 불변 List를 방출하는 StateFlow 사용 가능
    val userList: List<String> get() = mutableUserList

    fun addUser(user: String) {
        mutableUserList.add(user)
    }

    fun removeUser(user: String) {
        mutableUserList.remove(user)
    }
}

@Composable
fun UserList() {
    val userViewModel = viewModel<UserViewModel>()
    // ViewModel의 리스트를 직접 사용
    val userList = userViewModel.userList
    // ...
}

// ViewModel 내에서 사용 예시 2 (StateFlow와 함께)
class SettingsViewModel : ViewModel() {
    private val mutableSettingMap = mutableStateMapOf("theme" to "light", "language" to "English")
    // StateFlow로 노출할 경우, map의 변경을 감지하고 새 Map 객체를 방출해야 함
    val settings: StateFlow<Map<String, String>> = snapshotFlow { mutableSettingMap.toMap() }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), mutableSettingMap.toMap())


    fun updateTheme(theme: String) {
        mutableSettingMap["theme"] = theme
    }

    fun removeSetting(key: String) {
        mutableSettingMap.remove(key)
    }
}

@Composable
fun Settings() {
    val settingsViewModel = viewModel<SettingsViewModel>()
    val settings by settingsViewModel.settings.collectAsState()
    // ...
}
```

-----

## 요약

`mutableStateListOf`와 `mutableStateMapOf`는 Jetpack Compose에서 효율적인 리컴포지션을 가능하게 하는 상태 인식 컬렉션을 제공합니다. 이들은 종종 네트워크 응답이나 데이터베이스 쿼리와 같은 도메인 로직에서 가져온 데이터를 보유하면서 리스트나 맵으로서 상태를 관리하는 데 사용되며, 컴포저블 함수에서 원활한 관찰 및 UI 업데이트를 보장합니다.

-----

## Q. 왜 `mutableStateOf`로 감싼 일반 `mutableListOf`를 수정하는 것이 Compose에서 리컴포지션을 트리거하지 않나요? 이 문제를 어떻게 해결하겠습니까?

`mutableStateOf`는 **객체의 참조(reference)가 바뀔 때**만 변경을 감지하기 때문입니다. `mutableListOf`의 내용물을 `.add()` 등으로 수정해도 리스트 객체 자체의 참조는 동일하게 유지되므로, Compose는 상태가 변경되었다고 인지하지 못해 리컴포지션을 트리거하지 않습니다.

이 문제는 **리스트의 내용물 변경을 직접 관찰하는 `mutableStateListOf`를 사용**하여 해결할 수 있습니다.

-----

## 1. 문제의 원인: 참조(Reference) vs. 내용물(Content)

Jetpack Compose에서 `mutableStateOf`는 자신이 감싸고 있는 값의 변경을 추적하여 리컴포지션을 유발하는 강력한 도구입니다. 하지만 이 "변경"의 의미를 정확히 이해하는 것이 중요합니다.

`mutableStateOf`는 자신의 `.value` 프로퍼티에 **새로운 객체 참조가 할당될 때** 변경이 일어났다고 판단합니다.

#### 문제가 되는 코드

```kotlin
// 👎 이 방식은 리컴포지션을 유발하지 않습니다.
var userList by remember { mutableStateOf(mutableListOf("skydoves", "android")) }

Button(
    onClick = {
        // 'userList'가 가리키는 리스트 객체의 *내용물*만 수정합니다.
        // 'userList' 변수 자체에 새로운 리스트 객체를 할당하는 것이 아닙니다.
        userList.add("kotlin") 
    }
) {
    Text("Add User")
}
```

위 코드에서 `userList.add("kotlin")`을 호출하면, `userList` 변수가 들고 있는 `MutableList` 객체의 내부 데이터는 변경되지만, `userList` 변수 자체가 가리키는 **메모리 주소(객체의 참조)**는 그대로입니다. Compose의 상태 시스템은 이 참조값의 변경을 감지하지 못하므로, UI를 다시 그릴 필요가 없다고 판단하여 리컴포지션을 실행하지 않습니다.

-----

## 2. 해결 방법

이 문제를 해결하고 리스트의 내용물 변경에 따라 UI가 반응하도록 하는 방법은 두 가지가 있습니다.

### 해결책 1: `mutableStateListOf` 사용 (권장) 👍

Compose는 이러한 컬렉션 상태 관리를 위해 전용 API를 제공합니다. `mutableStateListOf`는 **`SnapshotStateList`** 라는 특별한 종류의 리스트를 생성합니다.

  * **작동 방식:** `SnapshotStateList`는 Compose의 스냅샷 시스템과 직접 통합되어 있습니다. `add()`, `remove()`, 또는 `list[index] = newValue`와 같이 **리스트의 내용물을 변경하는 모든 작업**은 Compose에 즉시 상태 변경으로 통지되어, 해당 상태를 구독하는 컴포저블의 리컴포지션을 올바르게 트리거합니다.

  * **수정된 코드:**

    ```kotlin
    // ✅ 이 방식이 올바른 해결책입니다.
    val userList = remember { mutableStateListOf("skydoves", "android") }

    Button(
        onClick = {
            // 이 .add() 호출은 SnapshotStateList의 변경을 유발하여
            // Compose가 감지하고 리컴포지션을 트리거합니다.
            userList.add("kotlin")
        }
    ) {
        Text("Add User")
    }
    ```

### 해결책 2: 새로운 리스트 인스턴스 할당 (비권장) ⚠️

리스트를 수정할 때마다 완전히 새로운 리스트 인스턴스를 생성하여 `mutableStateOf`에 할당하는 방법도 있습니다.

  * **작동 방식:** 기존 리스트를 복사하여 새 아이템을 추가한 후, 이 **새로운 리스트 객체**를 상태 변수의 `value`에 할당합니다. 이렇게 하면 객체의 참조가 변경되므로, `mutableStateOf`가 변경을 감지하고 리컴포지션을 트리거합니다.

  * **수정된 코드:**

    ```kotlin
    var userList by remember { mutableStateOf(listOf("skydoves", "android")) }

    Button(
        onClick = {
            // 기존 리스트에 새 아이템을 추가한 '새로운' 리스트를 생성하여 할당합니다.
            userList = userList + "kotlin"
        }
    ) {
        Text("Add User")
    }
    ```

  * **왜 비권장되는가?:** 이 방법은 아이템을 하나 추가/삭제할 때마다 **전체 리스트를 복사**하는 새로운 인스턴스를 생성합니다. 리스트가 클 경우, 이는 상당한 **메모리 할당과 성능 저하**를 유발할 수 있습니다. 따라서 `mutableStateListOf`를 사용하는 것이 훨씬 효율적입니다.

-----

## 3. 결론

Jetpack Compose에서 `List`, `Map`과 같은 컬렉션의 **내용물 변경**에 따라 UI를 반응적으로 업데이트하려면, 일반적인 `mutableStateOf(mutableListOf(...))` 대신 **Compose에 최적화된 `mutableStateListOf()` 또는 `mutableStateMapOf()`를 사용**해야 합니다. 이는 컬렉션의 내부적인 변경 사항을 Compose의 상태 시스템이 직접 관찰할 수 있게 하여, 의도한 대로 리컴포지션을 정확하게 트리거하는 가장 효율적이고 올바른 방법입니다.


## Q. `LazyColumn`에서 동적으로 리스트 아이템을 추가하거나 제거할 때, UI 업데이트를 효율적으로 어떻게 추적하겠습니까?

`LazyColumn`에서 동적으로 리스트 아이템을 추가하거나 제거할 때 UI 업데이트를 효율적으로 추적하려면, **`kotlinx.collections.immutable`의 `ImmutableList`와 같은 안정적인(stable) 리스트**를 사용하고, `LazyColumn`의 `items` 블록에 각 아이템을 고유하게 식별할 수 있는 **안정적인 `key`를 제공**하는 것이 가장 중요합니다.

`key`를 사용하면 Compose는 아이템이 추가, 제거, 또는 이동되었을 때 **어떤 아이템이 변경되었는지 정확히 인지**하여, 전체 목록을 다시 그리는 대신 필요한 부분만 최소한으로 업데이트하고 자연스러운 애니메이션을 적용할 수 있습니다.

-----

## 1. 문제점: `key`가 없을 때의 비효율성

만약 `LazyColumn`의 `items` 블록에 `key`를 제공하지 않으면, Compose는 **아이템의 위치(index)를 기본 키로 사용**합니다. 이 방식은 리스트의 시작이나 중간에서 아이템이 추가되거나 삭제될 때 다음과 같은 비효율을 초래합니다.

  * **상황:** 리스트의 맨 앞에 새 아이템이 추가됩니다.
  * **`key`가 없을 때의 동작:**
    1.  새 아이템이 0번 인덱스에 추가됩니다.
    2.  기존의 모든 아이템들의 인덱스가 1씩 밀려납니다 (0→1, 1→2, ...).
    3.  Compose는 인덱스가 변경된 모든 아이템들을 "새로운" 아이템으로 간주합니다.
    4.  결과적으로, 데이터 내용이 전혀 바뀌지 않았음에도 불구하고, 화면에 보이는 **거의 모든 아이템들이 불필요하게 리컴포지션(recomposition)** 됩니다.
  * **결과:** 이는 성능 저하를 유발하고, 아이템 추가/삭제/이동 시 부드러운 애니메이션(`animateItemPlacement`)을 불가능하게 만듭니다.

-----

## 2. 해결 전략: 안정적인 `key` 제공 및 불변 리스트 사용

### 2.1. `key` 파라미터의 역할 🔑

`LazyColumn`의 `items` 함수는 각 아이템에 대한 고유하고 안정적인 식별자를 제공할 수 있도록 `key` 파라미터를 받습니다.

  * **고유하고 안정적인 키:** 일반적으로 데이터 모델의 고유 ID(예: 데이터베이스의 `primary key`, API 응답의 `id` 필드)를 사용합니다. 이 키는 리컴포지션 간에도 동일한 아이템에 대해 항상 같은 값을 가져야 합니다.
  * **동작 방식:**
    1.  리스트가 업데이트되면, Compose는 `key`를 사용하여 이전 리스트와 새 리스트를 비교합니다.
    2.  이를 통해 어떤 아이템이 새로 추가되었는지(새로운 key), 삭제되었는지(사라진 key), 또는 위치만 이동했는지(key는 같지만 위치가 다름) 정확하게 파악합니다.
    3.  결과적으로, Compose는 **위치가 이동한 아이템의 컴포저블은 그대로 재사용**하고, 실제로 추가되거나 내용이 변경된 아이템만 리컴포지션하여 작업을 최소화합니다.

### 2.2. 안정적인(Stable) 리스트 사용

`key`를 사용하는 것과 더불어, `LazyColumn`에 전달하는 리스트 자체도 **안정적인(Stable) 타입**을 사용하는 것이 중요합니다. 이는 `LazyColumn`을 포함하는 부모 컴포저블의 불필요한 리컴포지션을 막아줍니다.

  * **전략:** 표준 `List<T>` 대신, **`kotlinx.collections.immutable` 라이브러리의 `ImmutableList<T>`** 를 사용합니다. Compose 컴파일러는 `ImmutableList`를 안정적인 타입으로 인식합니다.

### 2.3. 구현 예시

```kotlin
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList

// 각 아이템을 나타내는 데이터 클래스 (고유한 id 포함)
data class MyItem(val id: Int, val text: String)

@Composable
fun MyDynamicList(viewModel: MyViewModel) {
    // ViewModel은 ImmutableList를 StateFlow로 노출한다고 가정
    val items by viewModel.items.collectAsStateWithLifecycle()

    LazyColumn(modifier = Modifier.fillMaxSize()) {
        // 'items' 함수에 key를 명시적으로 제공
        items(
            items = items,
            key = { item -> item.id } // 각 아이템의 고유 id를 key로 사용
        ) { item ->
            // animateItemPlacement() Modifier를 사용하면
            // 아이템 위치 변경 시 부드러운 애니메이션이 자동으로 적용됨
            MyListItem(item = item, modifier = Modifier.animateItemPlacement())
        }
    }
}
```

**ViewModel 예시:**

```kotlin
class MyViewModel : ViewModel() {
    private val _items = MutableStateFlow<ImmutableList<MyItem>>(emptyList<MyItem>().toImmutableList())
    val items: StateFlow<ImmutableList<MyItem>> = _items.asStateFlow()

    fun addItem() {
        val newItem = MyItem(id = Random.nextInt(), text = "새 아이템")
        // 새 인스턴스를 생성하여 StateFlow 업데이트
        _items.value = (_items.value + newItem).toImmutableList()
    }

    fun removeItem(item: MyItem) {
        _items.value = _items.value.remove(item)
    }
}
```

-----

### 3. (참고) Jetpack Paging과의 연동

Jetpack Paging 3 라이브러리를 `LazyColumn`과 함께 사용할 때, `collectAsLazyPagingItems()` 확장 함수는 내부적으로 아이템의 인덱스를 기반으로 안정적인 키를 자동으로 처리해 줍니다. 따라서 개발자가 별도로 `key`를 지정하지 않아도 효율적인 업데이트가 가능합니다. 물론, 더 정확한 키(예: 데이터베이스 ID)가 있다면 직접 지정하는 것도 좋습니다.

-----

### 4. 결론

`LazyColumn`에서 동적인 리스트 변경을 효율적으로 추적하고 부드러운 UI 업데이트를 구현하기 위한 핵심 전략은 두 가지입니다.

1.  **아이템에 안정적인 `key` 제공:** `items` 블록에 각 아이템을 고유하게 식별할 수 있는 키(예: `item.id`)를 반드시 제공합니다.
2.  **안정적인 컬렉션 사용:** `LazyColumn`에 전달하는 리스트 자체를 `ImmutableList`와 같이 안정적인 타입으로 사용합니다.

이 두 가지 전략을 함께 사용하면, Jetpack Compose는 리스트의 변경 사항을 최소한으로 계산하여 꼭 필요한 리컴포지션만 수행하고, 아이템 이동 시 자연스러운 애니메이션을 제공하여 사용자에게 쾌적하고 반응성이 뛰어난 경험을 선사할 수 있습니다.