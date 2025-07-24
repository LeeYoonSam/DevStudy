# 화면 간 내비게이션(이동)은 어떻게 하나요?

최근 모범 사례가 단일 액티비티 구조를 선호함에 따라, XML 기반 프로젝트에서도 내비게이션은 현대 안드로이드 개발의 필수적인 부분입니다. Jetpack Compose에는 프래그먼트(Fragment)라는 개념이 없기 때문에, 내비게이션 스택, 컨트롤러, UI 상태를 관리하기 위한 전용 내비게이션 시스템이 필요합니다. Compose 프로젝트에서 내비게이션을 구현하는 데는 두 가지 주요 접근 방식이 있습니다: 수동 내비게이션 관리와, 스택 처리 및 상태 보존을 단순화하는 Jetpack Compose Navigation 라이브러리 사용입니다.

-----

## 수동 내비게이션 (Manual Navigation)

`SaveableStateHolder`를 사용하여 내비게이션 시스템을 수동으로 구현할 수 있습니다. `SaveableStateHolder`는 고유한 키를 기반으로 컴포저블의 상태를 관리하고 보존하기 위해 `rememberSaveable`과 함께 작동하는 Compose 런타임 API입니다. 새 화면으로 이동하는 것과 같이 컴포저블이 컴포지션에서 제거될 때, 그 상태는 자동으로 저장되고 컴포지션에 다시 진입할 때 복원됩니다.

아래 예시는 내비게이션 설정에서 상태 관리를 위해 `SaveableStateHolder`를 사용하는 방법을 보여주며, 각 화면이 앞뒤로 이동할 때 자신의 상태를 독립적으로 유지하도록 보장합니다.

```kotlin
@Composable
fun <T : Any> Navigation(
    currentScreen: T,
    modifier: Modifier = Modifier,
    content: @Composable (T) -> Unit
) {
    // SaveableStateHolder를 생성합니다.
    val saveableStateHolder = rememberSaveableStateHolder()

    Box(modifier) {
        // 다른 화면으로 이동할 때마다 전환 애니메이션을 제공합니다.
        AnimatedContent(currentScreen) {
            // `currentScreen`을 나타내는 콘텐츠를 `SaveableStateProvider` 안에 래핑합니다.
            saveableStateHolder.SaveableStateProvider(it) { content(it) }
        }
    }
}

@Composable
fun SaveableStateHolderExample() {
    var screen by rememberSaveable { mutableStateOf("screen1") }

    Column {
        // 내비게이션 버튼
        Row(horizontalArrangement = Arrangement.SpaceEvenly) {
            Button(onClick = { screen = "screen1" }) { Text("Go to screen1") }
            Button(onClick = { screen = "screen2" }) { Text("Go to screen2") }
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
            Text("Counter: $counter")
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
            label = { Text("Enter text") }
        )
    }
}
```

`SaveableStateHolder`와 `rememberSaveable`을 사용하여 내비게이션 시스템을 수동으로 구현할 수 있습니다. 그러나 실제 세계의 내비게이션은 훨씬 더 복잡합니다. 위로 이동(navigating up)하거나 특정 화면으로 전환하는 시나리오, 또는 딥 링킹 기능 등을 고려하면, 이러한 전환을 효율적으로 처리하기 위해서는 전용 내비게이션 시스템이 필요합니다.

또한, Jetpack ViewModel을 사용하는 경우 그 생명주기는 내비게이션 상태에 따라 관리됩니다. ViewModel이 의존성 주입 프레임워크와 통합되면 수동으로 관리하기가 더욱 어려워질 수 있습니다. 더 정교하고 확장 가능한 내비게이션, 특히 복잡한 사용 사례에서는 Jetpack Compose의 내비게이션 라이브러리를 사용하는 것이 권장됩니다.

-----

## Jetpack Compose Navigation

Jetpack Compose는 Jetpack의 기존 내비게이션 시스템 인프라를 활용하면서 컴포저블 간의 원활한 내비게이션을 가능하게 하는 [**Compose용 내비게이션 라이브러리**](https://developer.android.com/develop/ui/compose/navigation)를 제공합니다. 이는 구조화된 내비게이션 관리, 상태 처리, 딥 링킹 기능, 그리고 세이프 아규먼트(safe arguments)를 제공하여, Compose로 빌드된 현대적인 안드로이드 애플리케이션을 위한 유용한 솔루션입니다.

Compose 내비게이션 시스템은 세 가지 주요 구성 요소로 이루어져 있습니다.

  * **`NavHost`**: 내비게이션 그래프를 정의하고 컴포저블 화면을 경로(route)와 연결합니다.
  * **`NavController`**: 내비게이션 스택을 관리하고 목적지 간의 이동을 처리합니다.
  * **`Composable Destinations`**: 내비게이션 그래프 내의 개별 화면들입니다.

이제 Jetpack Compose Navigation 라이브러리를 사용하여 내비게이션 시스템을 구현하는 방법을 살펴보겠습니다.

### 1. `NavHost`로 내비게이션 정의하기

`NavHost`는 내비게이션 그래프를 관리하는 책임을 집니다. 시작 목적지와 다른 화면 간의 가능한 경로를 정의합니다.

```kotlin
sealed interface PokedexScreen {

    @Serializable
    object Home: PokedexScreen

    @Serializable
    object Details: PokedexScreen
}

@Composable
fun AppNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
) {
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = PokedexScreen.Home // 시작 목적지
    ) {
        composable<PokedexScreen.Home> {
            HomeScreen(
                onNavigateToDetails = { navController.navigate(route = PokedexScreen.Details) }
            )
        }
        composable<PokedexScreen.Details> { 
            DetailsScreen()
        }
    }
}
```

  * `NavHost`는 `Home`을 시작 목적지로 하여 사용 가능한 목적지들을 지정하는 내비게이션 그래프를 정의합니다.
  * `composable<Home>`과 `composable<Details>`는 내비게이션 그래프 내의 개별 화면을 나타내며, 이들 간의 이동을 허용합니다.
  * `NavController`는 인수를 사용하여 특정 화면으로 이동하고, 백 스택과 현재 목적지를 관리하며, `NavHost` 내의 전반적인 내비게이션 동작을 제어합니다.

### 2. 화면 간 이동하기

`HomeScreen`은 제공된 내비게이션 콜백을 사용하여 `DetailsScreen`으로의 이동을 트리거하는 버튼을 포함합니다.

```kotlin
@Composable
fun HomeScreen(
    onNavigateToDetails: () -> Unit
) {
    Column {
        Button(onClick = onNavigateToDetails) {
            Text(text = "Navigate to Details")
        }
    }
}
```

  * `onNavigateToDetails` 람다는 `HomeScreen`에 파라미터로 전달되어 내비게이션 로직을 분리된 상태로 유지합니다.
  * 버튼이 클릭되면, `navController.navigate(route = PokedexScreen.Details)`가 사용자를 `DetailsScreen`으로 이동시킵니다.

Jetpack Compose Navigation 라이브러리는 전환 애니메이션, [딥 링크](https://developer.android.com/develop/ui/compose/navigation#deeplinks), [타입 세이프 경로](https://developer.android.com/guide/navigation/design/type-safety), [중첩 내비게이션](https://developer.android.com/develop/ui/compose/navigation#nested-nav), [테스트 전략](https://developer.android.com/develop/ui/compose/navigation#testing) 및 [Hilt 통합](https://developer.android.com/training/dependency-injection/hilt-jetpack#viewmodel-navigation)에 대한 지원을 포함한 광범위한 기능을 제공합니다. 추가적으로, 내비게이션 시스템 내에서 ViewModel 생명주기를 효율적으로 관리하기 위한 전용 `viewModelStore`를 제공합니다.

코틀린 멀티플랫폼(KMP) 사용자를 위해, JetBrains는 [KMP 기반 내비게이션](https://www.jetbrains.com/help/kotlin-multiplatform-dev/compose-navigation-routing.html)을 지원하도록 Jetpack Compose Navigation 라이브러리를 포크(fork)하여 여러 플랫폼에서 사용할 수 있게 만들었습니다.

-----

## 요약

내비게이션은 현대 안드로이드 개발에서 필수적인 기능이 되었습니다. 수동으로 구현할 수도 있지만, 딥 링크 지원, 타입 세이프 경로, Hilt 통합, 원활한 ViewModel 생명주기 관리와 같은 더 고급 기능을 위해서는 **Jetpack Compose Navigation 라이브러리**를 사용하는 것이 권장됩니다. Google의 공식 프로젝트인 GitHub의 [Now in Android 프로젝트](https://github.com/advocacies/nowinandroid/blob/984a08052e6b858cdbefa19bafcf345517133300/app/src/main/java/com/google/samples/apps/nowinandroid/navigation/NiaNavHost.kt#L48)에서 Jetpack Compose Navigation의 모범 사례를 보여주는 더 고급 구현을 탐색할 수 있습니다.

-----

## Q. 내비게이션 라이브러리를 사용하지 않고 다중 화면 Compose 앱에서 화면 내비게이션을 관리하고 화면 상태를 보존하려면 어떻게 해야 하나요?

내비게이션 라이브러리 없이 화면 이동을 관리하고 상태를 보존하려면, **현재 화면을 나타내는 상태 변수(예: `mutableStateOf`)를 직접 관리**하고, **`rememberSaveableStateHolder`** API를 사용하여 화면을 전환할 때 보이지 않는 화면의 상태를 저장하고 복원합니다. 이 조합을 통해 수동으로 간단한 내비게이션 시스템과 상태 유지 기능을 구현할 수 있습니다.

-----

### 1. 화면 내비게이션 관리 🧭

가장 기본적인 방법은 현재 어떤 화면을 보여줄지를 나타내는 상태 변수를 만드는 것입니다. 이 상태 변수의 값이 바뀔 때마다 `when` 문을 통해 다른 컴포저블을 보여주는 방식입니다.

  * **상태 정의:** `rememberSaveable`을 사용하여 현재 화면의 상태를 저장합니다. `String` 이나 `Enum`, 또는 `Sealed Class`를 사용하여 화면을 구분할 수 있습니다.
  * **조건부 컴포지션:** `when` 문을 사용하여 현재 화면 상태에 따라 적절한 화면 컴포저블을 호출합니다.

<!-- end list -->

```kotlin
// 화면을 나타내는 Sealed Class 정의 (더 타입 세이프한 방법)
sealed class Screen {
    data object Home : Screen()
    data object Profile : Screen()
}

@Composable
fun ManualNavigationApp() {
    // 1. 현재 화면 상태를 rememberSaveable로 관리하여 구성 변경에도 유지
    var currentScreen: Screen by rememberSaveable { mutableStateOf(Screen.Home) }

    Column {
        // 화면을 변경하는 버튼들
        Row {
            Button(onClick = { currentScreen = Screen.Home }) { Text("홈") }
            Button(onClick = { currentScreen = Screen.Profile }) { Text("프로필") }
        }

        // 2. 현재 상태에 따라 다른 화면을 보여줌
        when (currentScreen) {
            Screen.Home -> HomeScreen()
            Screen.Profile -> ProfileScreen()
        }
    }
}
```

-----

### 2. 화면 상태 보존 (`SaveableStateHolder`) 💾

위의 방식만으로는 사용자가 다른 탭이나 화면으로 이동했다가 돌아왔을 때, 이전 화면의 상태(예: 스크롤 위치, `TextField` 입력 값)가 초기화되는 문제가 있습니다. `SaveableStateHolder`는 이러한 문제를 해결합니다.

  * **역할:** `SaveableStateHolder`는 고유한 `key`와 연결된 컴포저블의 `rememberSaveable` 상태를 저장하고 복원하는 메커니즘을 제공합니다.
  * **`SaveableStateProvider(key)`:** `SaveableStateHolder` 내에서 이 컴포저블을 사용하여 현재 화면의 콘텐츠를 감쌉니다.
      * 컴포저블이 화면에 표시될 때: `key`에 해당하는 저장된 상태가 있다면 복원합니다.
      * 컴포저블이 화면에서 사라질 때: `key`와 연결된 모든 `rememberSaveable` 상태를 저장합니다.

-----

### 3. 구현 예시 코드

아래는 위 두 가지 개념을 결합하여, 각 화면이 자신의 스크롤 위치나 입력 상태를 유지하는 완전한 예시입니다.

```kotlin
import androidx.compose.runtime.saveable.rememberSaveableStateHolder
// ... 다른 import들

sealed class TabScreen(val title: String) {
    data object Music : TabScreen("음악")
    data object Photos : TabScreen("사진")
}

@Composable
fun TabbedApp() {
    var selectedTab: TabScreen by rememberSaveable { mutableStateOf(TabScreen.Music) }
    val tabs = listOf(TabScreen.Music, TabScreen.Photos)

    // 1. SaveableStateHolder 인스턴스 생성
    val saveableStateHolder = rememberSaveableStateHolder()

    Column {
        TabRow(selectedTabIndex = tabs.indexOf(selectedTab)) {
            tabs.forEach { tab ->
                Tab(
                    selected = selectedTab == tab,
                    onClick = { selectedTab = tab },
                    text = { Text(tab.title) }
                )
            }
        }

        // 2. 현재 탭의 콘텐츠를 SaveableStateProvider로 감싸기
        saveableStateHolder.SaveableStateProvider(key = selectedTab.title) {
            when (selectedTab) {
                TabScreen.Music -> MusicTab()
                TabScreen.Photos -> PhotosTab()
            }
        }
    }
}

@Composable
fun MusicTab() {
    // 3. 스크롤 위치 상태를 rememberSaveable로 선언
    val scrollState = rememberLazyListState()
    LazyColumn(state = scrollState) {
        items(100) { index ->
            Text(text = "음악 트랙 #$index", modifier = Modifier.padding(16.dp))
        }
    }
}

@Composable
fun PhotosTab() {
    // 3. 텍스트 입력 상태를 rememberSaveable로 선언
    var text by rememberSaveable { mutableStateOf("") }
    TextField(
        value = text,
        onValueChange = { text = it },
        label = { Text("사진 검색..") },
        modifier = Modifier.fillMaxWidth().padding(16.dp)
    )
}
```

**실행 결과:**

  * "음악" 탭에서 스크롤을 내린 후 "사진" 탭으로 이동했다가 다시 "음악" 탭으로 돌아오면, **스크롤 위치가 그대로 유지**됩니다.
  * "사진" 탭에 텍스트를 입력한 후 다른 탭으로 갔다가 돌아와도, **입력했던 텍스트가 그대로 유지**됩니다.

-----

### 4. 수동 구현의 한계

이 방식은 간단한 탭이나 하단 내비게이션 구조에서 효과적이지만, 다음과 같은 한계가 있습니다.

  * **백 스택 관리:** 이 구현에는 백 스택(Back Stack)이 없습니다. 사용자가 뒤로 가기 버튼을 누르면 이 화면 전체를 관리하는 액티비티가 종료됩니다. 수동으로 백 스택(예: `MutableList<Screen>`)을 구현하는 것은 매우 복잡합니다.
  * **인수 전달:** 화면 간에 데이터를 전달하는 표준화된 방법이 없어 직접 구현해야 합니다.
  * **딥 링크:** 딥 링크를 지원하지 않습니다.
  * **ViewModel 스코핑:** 내비게이션 경로에 맞춰 `ViewModel`의 생명주기를 관리하는 기능이 없습니다.
  * **확장성:** 화면과 내비게이션 경로가 많아질수록 상태 관리 로직이 기하급수적으로 복잡해집니다.

-----

### 5. 결론

**`SaveableStateHolder`** 와 **`rememberSaveable`** 을 사용하면 내비게이션 라이브러리 없이도 간단한 다중 화면 앱에서 화면 전환 시 상태를 효과적으로 보존할 수 있습니다. 하지만 백 스택, 인수 전달, 딥 링크 등 더 복잡하고 정교한 내비게이션 기능이 필요하다면, **Jetpack Navigation 라이브러리**를 사용하는 것이 훨씬 더 효율적이고 안정적인 방법입니다.


## Q. Jetpack Compose Navigation에서 NavHost와 NavController 시스템은 백 스택과 ViewModel 생명주기를 어떻게 처리하나요?

`NavHost`와 `NavController` 시스템은 **백 스택(Back Stack)을 자동으로 관리**하여 화면 이동 기록을 LIFO(후입선출) 구조로 유지하며, 각 내비게이션 목적지(`NavBackStackEntry`)에 **고유한 `ViewModelStoreOwner`를 제공**하여 **`ViewModel`의 생명주기를 해당 화면의 생명주기에 정확히 일치**시킵니다. 이를 통해 화면이 백 스택에서 제거될 때 관련 `ViewModel`도 자동으로 소멸되어 메모리 누수를 방지합니다.

---
### 1. 백 스택(Back Stack) 처리 방식

Jetpack Compose Navigation은 사용자의 화면 이동 경로를 백 스택으로 자동 관리하여 일관된 뒤로 가기 경험을 제공합니다.

#### 1.1. 자동 스택 관리
* **푸시(Push):** `navController.navigate()`를 호출하여 새로운 화면(destination)으로 이동하면, 해당 목적지는 백 스택의 맨 위에 쌓입니다.
* **팝(Pop):** 사용자가 시스템의 뒤로 가기 버튼을 누르거나 `navController.popBackStack()` 또는 `navController.navigateUp()`을 호출하면, 백 스택의 최상단에 있는 현재 목적지가 스택에서 제거되고 이전 화면이 나타납니다.
* **`NavHost`의 역할:** `NavHost`는 이러한 백 스택을 내부적으로 관리하는 컨테이너 역할을 합니다. 개발자는 복잡한 `FragmentManager` 트랜잭션 없이도 `NavController`를 통해 간단히 스택을 조작할 수 있습니다.

#### 1.2. `app:popUpTo`를 통한 제어
`Maps()` 호출 시, `app:popUpTo`와 같은 옵션을 통해 특정 목적지까지 스택을 제거하는 등 백 스택을 선언적으로 제어할 수 있어, 로그인 후 이전 화면으로 돌아가지 못하게 하는 등의 흐름을 쉽게 구현할 수 있습니다.

---
### 2. ViewModel 생명주기 처리 방식

Navigation 라이브러리는 각 내비게이션 목적지의 생명주기에 맞춰 `ViewModel`을 효과적으로 관리하는 강력한 메커니즘을 제공합니다.

#### 2.1. `NavBackStackEntry`와 `ViewModelStoreOwner`
* 내비게이션 백 스택에 있는 **각 화면(destination)은 `NavBackStackEntry`라는 객체로 표현**됩니다.
* 중요한 점은, 이 **`NavBackStackEntry`가 `ViewModelStoreOwner` 인터페이스를 구현**한다는 것입니다. 이는 백 스택의 각 화면이 자신만의 독립적인 `ViewModel` 저장소(`ViewModelStore`)를 가질 수 있음을 의미합니다.

#### 2.2. ViewModel의 생명주기 스코핑(Scoping)
* **화면 범위 ViewModel:** 컴포저블 내에서 `viewModel()`이나 Hilt의 `hiltViewModel()`을 사용하여 `ViewModel`을 생성하면, 해당 `ViewModel`은 현재 화면의 `NavBackStackEntry` 범위로 생성됩니다.
* **자동 소멸:** 사용자가 뒤로 가기 등으로 화면을 이동하여 **해당 화면이 백 스택에서 완전히 제거되면**, 그 화면에 해당하는 `NavBackStackEntry`도 함께 소멸됩니다. 이때, `NavBackStackEntry`가 소유하던 `ViewModelStore`도 정리(clear)되면서, 그 안에 있던 **`ViewModel`의 `onCleared()` 메서드가 자동으로 호출**됩니다.
* **결과:** 이 메커니즘은 `ViewModel`의 생명주기를 정확히 해당 화면의 생존 기간과 일치시켜, 화면이 더 이상 필요 없을 때 관련 데이터와 리소스가 안전하게 정리되도록 보장하고 **메모리 누수를 방지**합니다.

#### 2.3. 내비게이션 그래프를 이용한 공유 ViewModel
* 여러 프래그먼트가 하나의 액티비티 `ViewModel`을 공유하는 것처럼, **여러 컴포저블 화면이 하나의 공유 `ViewModel`을 가질 수 있습니다.**
* 이를 위해서는 내비게이션 그래프에서 여러 목적지를 포함하는 **중첩된 내비게이션 그래프(nested navigation graph)** 를 만들고, `ViewModel`을 개별 화면이 아닌 **해당 그래프의 `NavBackStackEntry` 범위**로 생성하면 됩니다. 이렇게 하면 해당 그래프 내의 모든 화면들이 동일한 `ViewModel` 인스턴스를 공유하게 됩니다.