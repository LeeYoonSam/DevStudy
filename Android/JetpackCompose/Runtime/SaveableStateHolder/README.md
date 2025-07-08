# SaveableStateHolder란 무엇인가?

`SaveableStateHolder`는 내비게이션이나 화면 전환 시 컴포지션에서 일시적으로 사라지는 컴포저블의 상태(예: 스크롤 위치, 텍스트 입력)를 보존하고 복원해주는 Jetpack Compose API입니다. 고유한 키를 사용하여 각 화면의 상태를 독립적으로 관리함으로써, 사용자가 화면을 오갈 때 데이터가 초기화되는 것을 방지하고 원활한 사용자 경험을 보장합니다.

-----

Jetpack Compose에서 동적 또는 다중 화면을 구현할 때, 내비게이션이나 구성 변경 중에 개별 화면의 상태를 보존하고 복원하는 것은 어려울 수 있습니다. 바로 이때 [**`SaveableStateHolder`**](https://developer.android.com/reference/kotlin/androidx/compose/runtime/saveable/SaveableStateHolder) 가 사용됩니다. 이는 내비게이션이나 화면 전환과 같이 컴포저블이 컴포지션에서 일시적으로 제거될 때도 해당 컴포저블이 상태를 유지하도록 보장합니다.

`SaveableStateHolder`는 고유한 키와 연결된 컴포저블의 상태를 관리하고 보존하기 위해 `rememberSaveable`과 함께 작동하는 Compose 런타임 API입니다. 컴포저블이 컴포지션에서 제거될 때(예: 새 화면으로 이동할 때) 그 상태는 저장되고, 컴포지션에 다시 진입할 때 자동으로 복원됩니다.

### 예시: 내비게이션과 함께 SaveableStateHolder 사용하기

아래는 내비게이션 설정에서 상태를 관리하기 위해 `SaveableStateHolder`를 사용하는 실용적인 예시입니다. 각 화면은 앞뒤로 이동할 때 자신의 상태를 독립적으로 유지합니다.

```kotlin
/**
 * 현재 화면에 대한 컴포저블을 호스팅하고,
 * 표시되지 않는 화면의 상태를 저장하는 간단한 내비게이션 컴포저블
 */
@Composable
fun <T : Any> Navigation(
    currentScreen: T,
    modifier: Modifier = Modifier,
    content: @Composable (T) -> Unit
) {
    // SaveableStateHolder를 생성하고 remember.
    val saveableStateHolder = rememberSaveableStateHolder()

    Box(modifier) {
        // `currentScreen`을 나타내는 콘텐츠를 `SaveableStateProvider` 안에 래핑합니다.
        saveableStateHolder.SaveableStateProvider(currentScreen) {
            content(currentScreen)
        }
    }
}

@Composable
fun SaveableStateHolderExample() {
    // 현재 표시될 화면의 상태를 rememberSaveable로 관리
    var screen by rememberSaveable { mutableStateOf("screen1") }

    Column {
        // 내비게이션 버튼
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(onClick = { screen = "screen1" }) { Text("Screen 1으로 가기") }
            Button(onClick = { screen = "screen2" }) { Text("Screen 2로 가기") }
        }

        // SaveableStateHolder를 사용한 내비게이션
        Navigation(screen, Modifier.fillMaxSize()) { currentScreen ->
            if (currentScreen == "screen1") {
                Screen1()
            } else {
                Screen2()
            }
        }
    }
}

@Composable
fun Screen1() {
    var counter by rememberSaveable { mutableStateOf(0) }
    Column {
        Text("Screen 1")
        Button(onClick = { counter++ }) {
            Text("카운터: $counter")
        }
    }
}

@Composable
fun Screen2() {
    var text by rememberSaveable { mutableStateOf("") }
    Column {
        Text("Screen 2")
        TextField(
            value = text,
            onValueChange = { text = it },
            label = { Text("텍스트 입력") }
        )
    }
}
```

#### 예시의 핵심 개념

  * **`Navigation` 래퍼:** `Navigation` 컴포저블은 현재 화면을 받아 `SaveableStateProvider`로 감쌉니다. 이는 각 화면의 상태가 화면 키를 기반으로 독립적으로 저장되고 복원되도록 보장합니다.
  * **상태 유지:** 각 화면(`Screen1` 및 `Screen2`)은 `rememberSaveable`을 사용하여 자신의 상태를 유지합니다. 예를 들어, `Screen1`은 카운터를 유지하고 `Screen2`는 텍스트 입력을 유지합니다.
  * **동적 상태 처리:** 내비게이션 버튼을 사용하여 화면을 전환할 때, 각 화면의 상태는 보존되어 데이터 손실을 방지합니다. `Screen1`에서 카운터를 5로 만든 후 `Screen2`로 갔다가 다시 `Screen1`로 돌아오면 카운터는 여전히 5입니다.

-----

### SaveableStateHolder의 장점

  * **화면 간 상태 유지:** 내비게이션으로 다른 화면으로 이동했다가 돌아와도 각 화면의 상태를 유지합니다.
  * **단순화된 상태 관리:** 상태 저장 및 복원 작업을 자동으로 처리하여 상용구 코드(boilerplate)를 줄입니다.
  * **구성 변경 처리:** `rememberSaveable`과 원활하게 작동하여 화면 회전과 같은 구성 변경 시에도 상태를 보존합니다.

-----

## 요약

`SaveableStateHolder`를 내비게이션 설정에 통합함으로써, 추가적인 복잡성 없이 개별 화면의 상태가 보존되도록 보장할 수 있습니다. 이를 통해 상태 유지와 사용자 경험이 모두 우선시되는 동적인 다중 화면 앱을 구축할 수 있습니다. Jetpack Navigation 라이브러리를 사용할 때조차, `SaveableStateHolder`는 화면 간 전환이나 구성 변경을 처리할 때 여러 UI 상태가 유지되어야 하는 시나리오에서 여전히 가치가 있습니다. 이는 서로 다른 UI 컴포넌트들이 원활하게 상태를 유지할 수 있도록 보장하기 때문입니다.

-----

## Q. 여러 화면으로 구성된 탭 인터페이스에서, Jetpack Navigation 라이브러리를 사용하지 않고 각 탭이 화면 전환 시 스크롤 위치나 입력 상태를 유지하도록 어떻게 구현하겠습니까?

Jetpack Navigation 라이브러리를 사용하지 않고 탭 인터페이스에서 각 탭의 상태(스크롤 위치, 입력 값 등)를 유지하려면 **`SaveableStateHolder`** 를 사용하는 것이 가장 이상적인 방법입니다. `SaveableStateHolder`는 각 탭을 고유한 **`key`** 로 식별하여, 화면에 보이지 않는 탭의 상태도 메모리에 보존했다가 다시 화면에 나타날 때 복원해주는 역할을 합니다.

-----

## 1. 구현 전략: `SaveableStateHolder`와 `rememberSaveable`의 조합

핵심 아이디어는 다음과 같습니다.

1.  **`rememberSaveableStateHolder()`** 를 호출하여 상태를 저장하고 복원하는 주체인 `SaveableStateHolder` 인스턴스를 생성합니다.
2.  현재 선택된 탭을 나타내는 상태 변수(예: `selectedTabIndex`)를 만듭니다.
3.  현재 선택된 탭의 콘텐츠를 표시할 때, **`saveableStateHolder.SaveableStateProvider(key = ...)`** 컴포저블로 감싸줍니다. 이때 `key`로는 각 탭을 고유하게 식별할 수 있는 값(예: 탭의 인덱스 또는 고유한 이름)을 전달합니다.
4.  각 탭의 화면 내부에서는, 유지하고자 하는 상태(스크롤 위치, 텍스트 필드 값 등)를 **`rememberSaveable`** 을 사용하여 선언합니다.

-----

## 2. 코드 구현 예시

아래는 두 개의 탭("스크롤 탭", "입력 탭")을 가진 간단한 UI 예시입니다. 각 탭은 다른 탭으로 전환되었다가 돌아와도 자신의 상태를 그대로 유지합니다.

```kotlin
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.saveable.rememberSaveableStateHolder
import androidx.compose.ui.Modifier

@Composable
fun TabbedScreenWithStateRetention() {
    // 1. 현재 선택된 탭의 인덱스를 상태로 관리
    var selectedTabIndex by rememberSaveable { mutableStateOf(0) }
    val tabs = listOf("스크롤 탭", "입력 탭")

    // 2. SaveableStateHolder 인스턴스 생성
    val saveableStateHolder = rememberSaveableStateHolder()

    Column {
        // 3. 탭 로우 (TabRow)
        TabRow(selectedTabIndex = selectedTabIndex) {
            tabs.forEachIndexed { index, title ->
                Tab(
                    selected = selectedTabIndex == index,
                    onClick = { selectedTabIndex = index },
                    text = { Text(title) }
                )
            }
        }

        // 4. 현재 선택된 탭의 콘텐츠를 SaveableStateProvider로 감싸기
        //    key로는 각 탭의 고유한 인덱스를 사용
        saveableStateHolder.SaveableStateProvider(key = selectedTabIndex) {
            when (selectedTabIndex) {
                0 -> ScrollableTabContent()
                1 -> InputTabContent()
            }
        }
    }
}

@Composable
fun ScrollableTabContent() {
    // 5. 스크롤 위치 상태를 rememberSaveable로 선언
    val scrollState = rememberLazyListState()

    LazyColumn(state = scrollState, modifier = Modifier.fillMaxSize()) {
        items(100) { index ->
            Text(text = "아이템 #$index", modifier = Modifier.padding(16.dp))
        }
    }
}

@Composable
fun InputTabContent() {
    // 5. 텍스트 입력 상태를 rememberSaveable로 선언
    var text by rememberSaveable { mutableStateOf("") }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        TextField(
            value = text,
            onValueChange = { text = it },
            label = { Text("여기에 텍스트를 입력하세요") },
            modifier = Modifier.fillMaxWidth()
        )
    }
}
```

-----

## 3. 작동 원리

1.  사용자가 "스크롤 탭"에서 스크롤을 내리면, `ScrollableTabContent`의 `scrollState`가 `rememberLazyListState()`에 의해 업데이트됩니다.
2.  사용자가 "입력 탭"으로 전환하면, `selectedTabIndex`가 0에서 1로 변경됩니다.
3.  이때 `SaveableStateProvider(key = 0)`는 컴포지션에서 사라집니다. `SaveableStateHolder`는 `key = 0`에 해당하는 모든 `rememberSaveable` 상태(이 경우 `scrollState`)를 **내부적으로 저장**합니다.
4.  `SaveableStateProvider(key = 1)`이 컴포지션에 추가되고, `InputTabContent`가 표시됩니다.
5.  사용자가 "입력 탭"에 텍스트를 입력하면, `InputTabContent`의 `text` 상태가 업데이트됩니다.
6.  사용자가 다시 "스크롤 탭"으로 돌아오면, `selectedTabIndex`가 1에서 0으로 변경됩니다.
7.  `SaveableStateProvider(key = 1)`이 사라지면서 `text` 상태가 저장됩니다.
8.  `SaveableStateProvider(key = 0)`이 다시 컴포지션에 추가됩니다. `SaveableStateHolder`는 이전에 `key = 0`으로 저장해 두었던 **`scrollState`를 복원**합니다.
9.  결과적으로 `ScrollableTabContent`는 사용자가 떠났던 바로 그 스크롤 위치에서 다시 표시됩니다.

이러한 방식으로 `SaveableStateHolder`는 각 탭의 UI 상태를 고유한 `key`와 연결하여, 탭이 보이지 않을 때 상태를 저장하고 다시 보일 때 복원함으로써 원활한 사용자 경험을 제공합니다.