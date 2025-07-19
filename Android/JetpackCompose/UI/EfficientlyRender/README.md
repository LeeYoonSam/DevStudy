# UI 버벅임(jank)을 피하면서 수백 개의 아이템을 리스트로 효율적으로 렌더링하려면 어떻게 해야 할까요?

Jetpack Compose에서 수백 또는 수천 개의 아이템을 표시할 때, `Column`과 같은 표준 레이아웃을 사용하면 불필요한 컴포지션 및 렌더링으로 인해 성능 문제가 발생할 수 있습니다. UI 버벅임을 피하고 효율성을 향상시키기 위해, Jetpack Compose는 필요에 따라 아이템을 동적으로 컴포즈하고 재활용하는 **`LazyColumn`**, **`LazyRow`**, **`LazyGrid`** 와 같은 [**레이지 리스트(Lazy List)**](https://developer.android.com/develop/ui/compose/lists#lazy) 라는 최적화된 리스트 컴포넌트를 제공합니다.

### 수직 리스트를 위한 LazyColumn 사용

`LazyColumn`은 **보이는 아이템만 컴포즈하고 화면 밖의 아이템은 재활용**하여 대규모 리스트를 효율적으로 렌더링하도록 설계되었습니다. 이는 메모리 사용량을 크게 줄이고 스크롤 성능을 향상시킵니다.

```kotlin
@Composable
fun ItemList() {
    LazyColumn {
        items(1000) { index ->
            Text(text = "Item #$index", modifier = Modifier.padding(8.dp))
        }
    }
}
```

이 예시에서:

  * `LazyColumn`은 보이는 아이템만 로드하여 불필요한 컴포지션을 방지합니다.
  * `items` 함수는 1000개의 아이템을 한 번에 모두 미리 로드하지 않고 동적으로 생성합니다.

### 수평 리스트를 위한 LazyRow 사용

수평으로 스크롤되는 리스트의 경우, `LazyRow`는 `LazyColumn`과 유사하게 작동하여 필요한 아이템만 컴포즈되도록 보장합니다.

```kotlin
@Composable
fun HorizontalItemList() {
    LazyRow {
        items(500) { index ->
            Text(text = "Item #$index", modifier = Modifier.padding(8.dp))
        }
    }
}
```

이 접근 방식은 많은 수의 수평 아이템을 다룰 때 UI 지연(lag)을 방지합니다.

### 그리드 레이아웃을 위한 LazyVerticalGrid 사용

그리드 구조가 필요한 레이아웃의 경우, `LazyVerticalGrid`는 아이템들을 열(column)로 구성하면서 `LazyColumn`과 유사한 효율적인 렌더링을 제공합니다.

```kotlin
@Composable
fun GridItemList() {
    LazyVerticalGrid(
        columns = GridCells.Fixed(3), 
        modifier = Modifier.fillMaxSize()
    ) {
        items(300) { index ->
            Text(text = "Item #$index", modifier = Modifier.padding(8.dp))
        }
    }
}
```

이는 주어진 시간에 보이는 그리드 아이템만 컴포즈되도록 보장합니다. 수평 그리드 레이아웃을 효율적으로 구현하려면, `LazyHorizontalGrid`를 사용할 수 있습니다.

### `key`를 사용한 성능 최적화

기본적으로 각 아이템의 상태는 리스트나 그리드에서의 **위치(position)** 와 연관됩니다. 그러나 아이템이 삽입, 제거 또는 순서 변경될 때와 같이 데이터셋이 변경되면, 아이템들의 위치가 바뀌기 때문에 기억된 상태(remembered state)를 잃을 수 있습니다.

변경 시에도 상태를 보존하려면, **각 아이템에 안정적인 `key`를 할당**하여 리스트 내 위치가 수정되더라도 상태가 일관되게 유지되도록 하고 불필요한 리컴포지션을 방지합니다.

```kotlin
@Composable
fun KeyedItemList(items: List<Item>) {
    LazyColumn {
        items(items, key = { it.id }) { item ->
            Text(text = item.name, modifier = Modifier.padding(8.dp))
        }
    }
}
```

이 예시에서 `key = { it.id }`는 각 아이템에 고유 식별자를 할당하여, 리스트가 변경될 때 불필요한 리컴포지션을 방지합니다. 레이지 리스트 최적화에 대한 더 자세한 내용과 모범 사례는 공식 문서인 [레이지 레이아웃의 key 사용](https://developer.android.com/develop/ui/compose/performance/bestpractices#use-lazylist-keys)을 참조하세요.

### 요약

Jetpack Compose에서 대규모 리스트를 효율적으로 렌더링하려면, `Column`이나 `Row`와 같은 표준 레이아웃 대신 `LazyColumn`, `LazyRow`, `LazyGrid`와 같은 레이지 리스트를 사용해야 합니다. 성능을 더욱 최적화하려면, 동적 리스트를 처리할 때 **`key`를 적용**하여 최소한의 리컴포지션을 보장해야 합니다. 이러한 최적화 기법을 활용하면 UI 버벅임을 피하고, 부드럽고 효율적인 리스트 렌더링을 구현할 수 있습니다.

---

## Q. 실시간 메시지가 표시되는 채팅 화면을 구축해야 한다고 가정해 봅시다. 부드러운 스크롤과 최소한의 리컴포지션 오버헤드를 보장하기 위해 레이아웃을 어떻게 구조화하겠습니까?

실시간 채팅 화면을 효율적으로 구축하려면, **`LazyColumn`** 을 **`reverseLayout = true`** 로 설정하여 새 메시지가 하단에 표시되도록 하고, 각 메시지에 **고유하고 안정적인 `key`를 제공**하여 불필요한 리컴포지션을 방지합니다. 데이터는 `ViewModel`의 **`StateFlow`** 를 통해 비동기적으로 수신하며, **`ImmutableList`** 와 같은 안정적인 컬렉션을 사용하여 상태를 관리합니다. 마지막으로, 새 메시지가 도착했을 때 **`LaunchedEffect`** 와 `LazyListState.animateScrollToItem()`을 사용하여 자동으로 스크롤을 최하단으로 이동시켜 부드러운 사용자 경험을 보장합니다.

### 1. `LazyColumn`과 `reverseLayout` 사용 📜

채팅 화면은 메시지가 계속 누적되므로 대규모 리스트를 효율적으로 처리할 수 있는 **`LazyColumn`** 을 사용하는 것이 필수적입니다. 또한, 채팅 UI의 일반적인 경험(새 메시지가 아래에 추가되고 항상 최신 메시지를 보여주는 것)을 위해 다음과 같이 설정합니다.

  * **`reverseLayout = true`**: 이 속성은 아이템들을 아래에서 위로 채우고, 스크롤 시작 위치를 리스트의 맨 아래로 설정합니다. 이는 새로운 메시지가 항상 목록의 하단에 보이도록 하는 데 완벽합니다.

-----

### 2. 안정적인 `key` 제공 및 불변 리스트 활용 🔑

리컴포지션 오버헤드를 최소화하기 위해 두 가지 핵심 전략을 사용합니다.

  * **안정적인 `key` 제공:** `LazyColumn`의 `items` 블록에 각 메시지를 고유하게 식별할 수 있는 안정적인 `key`(예: `message.id`)를 제공합니다. 이를 통해 Compose는 새 메시지가 추가되거나 기존 메시지가 변경될 때, 전체 리스트를 다시 그리는 대신 변경된 부분만 효율적으로 처리하고 자연스러운 애니메이션(`animateItemPlacement`)을 적용할 수 있습니다.
  * **불변 리스트(`ImmutableList`) 사용:** `ViewModel`에서 메시지 목록 상태를 관리할 때, 표준 `List` 대신 `kotlinx.collections.immutable`의 `ImmutableList`를 사용합니다. 이는 `LazyColumn`을 포함하는 상위 컴포저블의 불필요한 리컴포지션을 방지하여 성능을 최적화합니다.

-----

### 3. `ViewModel`과 `StateFlow`를 이용한 데이터 스트림 관리 🔄

`ViewModel`은 채팅 메시지 데이터를 관리하고 UI에 노출하는 책임을 집니다.

  * `ViewModel`은 웹소켓(WebSocket), Firestore 리스너 등으로부터 실시간 메시지를 수신합니다.
  * 메시지 목록은 `MutableStateFlow<ImmutableList<Message>>`와 같이 안정적인 컬렉션을 사용하는 `StateFlow`로 관리합니다.
  * 새 메시지가 도착하면, 기존 리스트에 새 메시지를 추가한 **새로운 불변 리스트 인스턴스**를 생성하여 `StateFlow`의 값을 업데이트합니다.

-----

### 4. 새 메시지 도착 시 자동 스크롤 구현

새 메시지가 도착했을 때 사용자가 항상 최신 메시지를 볼 수 있도록 자동으로 스크롤을 맨 아래로 이동시키는 기능은 필수적입니다.

  * **`rememberLazyListState()`** 를 사용하여 `LazyColumn`의 스크롤 상태를 제어할 수 있는 `LazyListState` 인스턴스를 생성합니다.
  * **`LaunchedEffect`** 를 사용하여 메시지 목록의 크기 변경을 감지합니다. 목록의 크기가 커지면(즉, 새 메시지가 추가되면), `lazyListState.animateScrollToItem(0)`을 호출하여 목록의 맨 아래(index 0, `reverseLayout=true`이므로)로 부드럽게 스크롤합니다.

-----

### 5. 전체 구조 예시 코드

아래는 위 전략들을 통합한 채팅 화면의 구조 예시입니다.

```kotlin
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
// ... 기타 import

// 데이터 모델
data class Message(val id: String, val text: String, val timestamp: Long)

// ViewModel
class ChatViewModel : ViewModel() {
    private val _messages = MutableStateFlow<ImmutableList<Message>>(emptyList<Message>().toImmutableList())
    val messages: StateFlow<ImmutableList<Message>> = _messages

    // 새 메시지가 도착했을 때 호출되는 함수 (예: 웹소켓 리스너)
    fun onNewMessageReceived(message: Message) {
        // 기존 리스트에 새 메시지를 추가한 새로운 불변 리스트를 생성하여 상태 업데이트
        _messages.value = (_messages.value + message).toImmutableList()
    }
}

// Composable UI
@Composable
fun ChatScreen(viewModel: ChatViewModel) {
    val messages by viewModel.messages.collectAsStateWithLifecycle()
    val lazyListState = rememberLazyListState()

    // 메시지 리스트의 크기가 변경될 때마다 자동 스크롤
    LaunchedEffect(messages.size) {
        // 새 메시지가 추가된 후 리스트의 맨 아래로 스크롤
        if (messages.isNotEmpty()) {
            lazyListState.animateScrollToItem(0)
        }
    }

    LazyColumn(
        state = lazyListState,
        reverseLayout = true, // 아래에서부터 아이템을 쌓고, 스크롤 시작 위치를 맨 아래로
        modifier = Modifier.fillMaxSize()
    ) {
        items(
            items = messages,
            key = { message -> message.id } // 각 메시지에 고유하고 안정적인 key 제공
        ) { message ->
            // 각 채팅 메시지 아이템 UI
            ChatMessageItem(message, Modifier.animateItemPlacement())
        }
    }
}
```

### 6. 결론

실시간 채팅 화면을 부드럽고 효율적으로 구현하기 위해서는 위에서 설명한 전략들의 조합이 중요합니다. **`LazyColumn`과 `reverseLayout`** 으로 기본적인 채팅 UI 구조를 만들고, **안정적인 `key`와 `ImmutableList`** 로 리컴포지션을 최적화하며, **`ViewModel`과 `StateFlow`** 로 데이터 흐름을 관리하고, 마지막으로 **`LaunchedEffect`를 이용한 자동 스크롤**로 사용자 경험을 향상시키는 것이 현대적인 Jetpack Compose 앱에서 권장되는 견고한 구현 방식입니다.


## Q. `LazyColumn`이나 `LazyGrid`에서 `key`를 사용하는 것이 리스트가 업데이트될 때 UI 성능과 안정성을 유지하는 데 어떻게 도움이 되나요?

`LazyColumn`이나 `LazyGrid`에서 **`key`** 를 사용하는 것은 리스트가 업데이트될 때 **어떤 아이템이 추가, 제거, 또는 이동되었는지** Compose가 정확하게 식별할 수 있게 해줍니다. 이를 통해 **불필요한 리컴포지션을 방지**하고, 기존 아이템의 상태를 유지하며, 자연스러운 애니메이션을 가능하게 하여 UI 성능과 안정성을 크게 향상시킵니다.

-----

### 1. `key`의 역할: 아이템의 고유 신원(Identity) 제공

`key`의 핵심 역할은 각 아이템에 **위치(index)와는 별개인 고유하고 안정적인 신원(identity)** 을 부여하는 것입니다.

  * **`key`가 없을 때:** Compose는 아이템을 오직 **위치(index)** 로만 구분합니다.
  * **`key`가 있을 때:** Compose는 아이템을 개발자가 제공한 **고유한 `key`** 로 구분합니다.

-----

### 2. `key` 사용이 UI 성능과 안정성을 유지하는 방식

#### 2.1. 불필요한 리컴포지션(Recomposition) 방지 ⚡️

  * **`key`가 없을 때의 문제점:**
    리스트의 맨 앞에 아이템이 하나 추가되면, 기존의 모든 아이템들의 인덱스가 1씩 밀려납니다. Compose는 이를 "모든 아이템이 새로운 위치에 있는 새로운 데이터"로 인식하여, 화면에 보이는 거의 모든 아이템들을 **불필요하게 다시 그립니다(리컴포지션).**
  * **`key`를 사용했을 때의 해결책:**
    새로운 아이템이 추가되어도 기존 아이템들의 `key`는 그대로 유지됩니다. Compose는 `key`를 통해 "이 아이템들은 단지 위치만 이동했을 뿐, 동일한 아이템이다"라고 인지합니다. 따라서 **새로 추가된 아이템만 새로 그리고, 기존 아이템들은 리컴포지션하지 않고 재사용**하여 성능을 크게 향상시킵니다.

#### 2.2. 아이템 상태(State) 유지 💾

  * **`key`가 없을 때의 문제점:**
    각 아이템 컴포저블 내에서 `remember`로 관리되는 상태(예: 체크박스의 체크 상태, 확장/축소 상태)는 아이템의 위치에 연결됩니다. 리스트 순서가 변경되어 아이템의 위치가 바뀌면, 해당 아이템은 자신의 상태를 잃어버리고 다른 아이템의 상태를 물려받는 것처럼 보일 수 있습니다.
  * **`key`를 사용했을 때의 해결책:**
    `remember`로 관리되는 상태는 아이템의 위치가 아닌 **고유한 `key`에 연결**됩니다. 따라서 리스트 순서가 바뀌거나 아이템이 다른 위치로 이동하더라도, **각 아이템은 자신의 상태를 그대로 유지**하여 UI의 안정성과 일관성을 보장합니다.

#### 2.3. 자연스러운 아이템 애니메이션 활성화 🎬

  * **`key`가 없을 때의 문제점:**
    Compose는 어떤 아이템이 추가/삭제/이동되었는지 정확히 알 수 없으므로, 아이템 위치 변경에 대한 자연스러운 애니메이션(예: `Modifier.animateItemPlacement()`)을 적용할 수 없습니다.
  * **`key`를 사용했을 때의 해결책:**
    `key`를 통해 각 아이템의 신원을 정확히 추적할 수 있으므로, 아이템이 리스트 내에서 다른 위치로 이동할 때 **부드럽게 미끄러지는 듯한 애니메이션**이 가능해집니다. 이는 사용자 경험을 크게 향상시킵니다.

-----

### 3. 구현 예시

```kotlin
// 데이터 모델 (고유한 id 포함)
data class Message(val id: String, val text: String)

@Composable
fun ChatList(messages: ImmutableList<Message>) {
    LazyColumn {
        items(
            items = messages,
            key = { message -> message.id } // message.id를 고유하고 안정적인 key로 사용
        ) { message ->
            MessageItem(message = message, modifier = Modifier.animateItemPlacement())
        }
    }
}
```

위 예시에서 `message.id`를 `key`로 제공함으로써, `messages` 리스트에 새로운 메시지가 추가되거나 삭제될 때 Compose는 최소한의 작업으로 UI를 업데이트하고, 필요한 경우 부드러운 애니메이션을 적용할 수 있습니다.

-----

### 4. 결론

`LazyColumn`이나 `LazyGrid`에서 **`key`를 사용하는 것은 선택이 아닌 필수적인 최적화 기법**입니다. `key`는 Compose가 동적인 리스트 변경을 지능적으로 이해하고 처리할 수 있도록 하는 핵심 정보를 제공합니다. 이를 통해 불필요한 리컴포지션을 막고, 아이템의 상태를 안정적으로 유지하며, 부드러운 애니메이션을 가능하게 하여 고성능의 안정적인 UI를 구축하는 데 결정적인 역할을 합니다.