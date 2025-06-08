# Jetpack Navigation 라이브러리란 무엇인가?

[**Jetpack Navigation 라이브러리**](https://developer.android.com/guide/navigation)는 안드로이드에서 **앱 내 내비게이션(화면 이동)을 단순화하고 표준화**하기 위해 제공하는 프레임워크입니다. 이를 통해 개발자는 다양한 앱 화면 간의 내비게이션 경로와 전환(transition)을 **선언적으로 정의**하여 상용구 코드(boilerplate code)를 줄이고 전반적인 사용자 경험을 향상시킬 수 있습니다.

이 라이브러리는 액티비티, 프래그먼트, 컴포저블(composable)의 내비게이션을 관리하기 위한 도구를 제공하는 동시에, 딥 링크, 백 스택 관리, 애니메이션과 같은 고급 기능을 지원합니다. Jetpack Navigation 라이브러리는 내비게이션을 원활하게 처리하기 위해 함께 작동하는 몇 가지 필수 구성 요소로 이루어져 있습니다.

---
### Jetpack Navigation의 필수 구성 요소

#### 1. 내비게이션 그래프 (Navigation Graph)

내비게이션 그래프는 앱 목적지(화면) 간의 **내비게이션 흐름과 관계를 정의하는 XML 리소스**입니다. 각 목적지는 프래그먼트, 액티비티, 또는 사용자 정의 뷰와 같은 하나의 화면을 나타냅니다.

**`res/navigation/nav_graph.xml` 예시:**
```xml
<?xml version="1.0" encoding="utf-8"?>
<navigation xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    android:id="@+id/nav_graph"
    app:startDestination="@id/homeFragment">
    <fragment
        android:id="@+id/homeFragment"
        android:name="com.example.app.HomeFragment"
        android:label="Home">
        <action
            android:id="@+id/action_home_to_details"
            app:destination="@id/detailsFragment" />
    </fragment>

    <fragment
        android:id="@+id/detailsFragment"
        android:name="com.example.app.DetailsFragment"
        android:label="Details" />
</navigation>
```

#### 2. NavHostFragment

`NavHostFragment`는 **내비게이션 그래프를 위한 컨테이너 역할**을 하며, 목적지(destination)들을 호스팅하고 그 사이의 내비게이션을 관리합니다. 사용자가 화면을 이동할 때 컨테이너 내의 프래그먼트를 동적으로 교체합니다.

**레이아웃 XML에 `NavHostFragment` 추가 예시:**
```xml
<androidx.fragment.app.FragmentContainerView
    android:id="@+id/nav_host_fragment"
    android:name="androidx.navigation.fragment.NavHostFragment"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    app:defaultNavHost="true"
    app:navGraph="@navigation/nav_graph" />
```

#### 3. NavController

`NavController`는 **내비게이션 액션을 처리하고 백 스택을 관리**하는 책임을 집니다. 이를 사용하여 프로그래밍 방식으로 목적지 간에 이동하거나 내비게이션 흐름을 조작할 수 있습니다.

**`MainActivity.kt`에서의 `NavController` 사용 예시:**
```kotlin
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // NavHostFragment로부터 NavController 가져오기
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController

        // 버튼 클릭 시 내비게이션 액션 실행
        findViewById<Button>(R.id.navigateButton).setOnClickListener {
            navController.navigate(R.id.action_home_to_details)
        }
    }
}
```

---
### Jetpack Navigation의 주요 기능

#### Safe Args

Safe Args는 **타입 세이프(type-safe, 유형 안전) 내비게이션 및 인수 전달 코드**를 생성하는 Gradle 플러그인입니다. 목적지 간에 데이터를 전달할 때 수동으로 번들(Bundle)을 생성할 필요성을 없애줍니다.

**Safe Args를 사용한 데이터 전달:**
```kotlin
// HomeFragment에서 DetailsFragment로 itemId(42)를 전달
val action = HomeFragmentDirections.actionHomeToDetails(itemId = 42)
findNavController().navigate(action)
```

#### 딥 링킹 (Deep Linking)

이 라이브러리는 딥 링크를 지원하여 사용자가 URL이나 알림과 같은 외부 소스에서 특정 화면으로 직접 이동할 수 있게 합니다.

**내비게이션 그래프에 딥 링크 추가:**
```xml
<fragment
    android:id="@+id/detailsFragment"
    android:name="com.example.app.DetailsFragment">
    <deepLink
        app:uri="https://example.com/details/{itemId}" />
</fragment>
```

---
### Jetpack Navigation 라이브러리의 장점

* **중앙 집중식 내비게이션:** 모든 내비게이션 흐름을 하나의 XML 파일에서 관리하여 명확하고 유지보수 용이한 구조를 제공합니다.
* **타입 세이프 인수:** 생성된 Safe Args 클래스를 통해 목적지 간에 데이터를 안전하게 전달합니다.
* **백 스택 관리:** 백 스택 동작을 자동으로 처리하여 일관된 내비게이션을 보장합니다.
* **딥 링크 지원:** 외부 내비게이션 요청을 원활하게 처리하여 사용자 경험을 향상시킵니다.
* **Jetpack 컴포넌트와의 통합:** 프래그먼트, ViewModel, LiveData와 잘 작동하여 생명주기를 인식하는 내비게이션을 보장합니다.

---
### 요약

Jetpack Navigation 라이브러리는 내비게이션 경로, 전환, 인수를 관리하는 **선언적이고 중앙 집중적인 접근 방식**을 제공하여 안드로이드 애플리케이션 내의 내비게이션을 단순화합니다. 다른 Jetpack 컴포넌트와 원활하게 통합되고, 딥 링킹을 지원하며, 타입 세이프 인수 전달을 위한 Safe Args와 같은 도구를 제공합니다. 이 라이브러리는 상용구 코드를 줄이고 일관된 내비게이션 패턴을 보장하여 개발자 경험을 향상시킵니다.

---

## Q. Jetpack Navigation 라이브러리는 백 스택을 어떻게 처리하며, `NavController`를 사용하여 프로그래밍 방식으로 백 스택을 어떻게 조작할 수 있나요?

Jetpack Navigation 라이브러리는 앱 내 화면 이동(내비게이션) 시 **백 스택(Back Stack)** 을 자동으로 관리하여 개발 과정을 크게 단순화합니다. 동시에, 더 복잡한 사용자 흐름을 구현하기 위해 **`NavController`** 를 사용하여 프로그래밍 방식으로 백 스택을 직접 조작할 수 있는 강력한 기능도 제공합니다.

---
### 1. Jetpack Navigation의 기본 백 스택 처리 방식

#### 1.1. 스택(Stack) 기반 관리 (LIFO)
Navigation 라이브러리는 백 스택을 이름 그대로 **스택(Stack)** 자료구조, 즉 **후입선출(LIFO, Last-In, First-Out)** 원칙에 따라 관리합니다. 사용자가 새로운 화면(목적지, Destination)으로 이동할 때마다 해당 목적지가 스택의 맨 위에 쌓입니다.

#### 1.2. 기본 동작
* **화면 이동 시 (`Maps()`):** 개발자가 `navController.navigate(...)`를 호출하여 새로운 목적지로 이동하면, 라이브러리는 이 새로운 목적지를 백 스택의 최상단에 **푸시(push)** 합니다.
* **뒤로 가기 시:** 사용자가 시스템의 뒤로 가기 버튼을 누르거나, 개발자가 `navController.navigateUp()` 또는 `navController.popBackStack()`을 호출하면, 백 스택의 최상단에 있는 현재 목적지가 **팝(pop)** 되어 스택에서 제거되고 이전 목적지 화면이 나타납니다.

#### 1.3. 시작 목적지(Start Destination)
내비게이션 그래프(`navigation graph`)에서 `app:startDestination`으로 지정된 목적지는 백 스택의 첫 번째 아이템, 즉 루트(root)가 됩니다. 이 시작 목적지에서 뒤로 가기 버튼을 누르면 일반적으로 해당 `NavHost`를 포함하는 액티비티가 종료됩니다.

이러한 자동 관리는 A → B → C 와 같은 단순한 선형적 내비게이션 흐름에서 개발자가 `FragmentManager` 트랜잭션을 직접 관리할 필요 없이 매우 편리하게 작동합니다.

---
### 2. `NavController`를 사용한 프로그래밍 방식의 백 스택 조작

더 복잡한 내비게이션 시나리오(예: 로그인 후 뒤로 가기 막기, 중간 단계 건너뛰기 등)를 위해 `NavController`는 백 스택을 직접 조작하는 다양한 방법을 제공합니다.

#### 2.1. 기본 내비게이션 및 스택 팝(Pop)
* **`Maps()`:** 다음 목적지로 이동하며 스택에 추가합니다.
* **`MapsUp()`:** 계층 구조상 상위 목적지로 이동하거나, 상위 목적지가 없으면 `popBackStack()`과 동일하게 동작합니다. 일반적으로 툴바의 '위로 가기' 화살표와 연결됩니다.
* **`popBackStack()`:** 현재 목적지를 스택에서 제거하고 이전 목적지로 돌아갑니다.
* **`popBackStack(int destinationId, boolean inclusive)`:** 특정 목적지(`destinationId`)에 도달할 때까지 스택에서 목적지들을 제거합니다.
    * **`inclusive = true`**: `destinationId`로 지정된 목적지까지 **포함하여** 스택에서 제거합니다.
    * **`inclusive = false`**: `destinationId`로 지정된 목적지 **바로 위까지만** 스택에서 제거합니다 (`destinationId`는 남음).

#### 2.2. XML `<action>` 태그를 이용한 선언적 조작

내비게이션 그래프의 `<action>` 태그에 속성을 추가하여 `Maps()` 호출 시 백 스택 동작을 미리 정의할 수 있습니다.

* **`app:popUpTo="@id/destinationId"`:**
    해당 액션을 실행할 때, 지정된 `destinationId`에 도달할 때까지 백 스택에서 목적지들을 제거합니다.
* **`app:popUpToInclusive="true|false"`:**
    `popUpTo` 속성과 함께 사용됩니다. `true`로 설정하면 `popUpTo`로 지정된 목적지까지 포함하여 스택에서 제거합니다.
    * **(주요 사용 사례) 로그인 흐름:** 로그인 성공 후 홈 화면으로 이동할 때, 로그인 화면으로 다시 돌아갈 수 없도록 백 스택에서 로그인 관련 화면들을 모두 제거하는 데 사용됩니다.
        ```xml
        <action
            android:id="@+id/action_login_to_home"
            app:destination="@id/homeFragment"
            app:popUpTo="@id/loginFragment"
            app:popUpToInclusive="true" />
        ```
* **`app:launchSingleTop="true"`:**
    만약 이동하려는 목적지가 이미 백 스택의 최상단에 있다면, 새로운 인스턴스를 만들지 않고 기존 인스턴스를 재사용합니다. 이는 사용자가 같은 화면을 중복해서 스택에 쌓는 것을 방지합니다 (예: A → B → B → B 방지).

#### 2.3. `NavOptions`를 사용한 동적 조작 (코드 기반)

XML에 정의된 동작 외에, 코드에서 직접 내비게이션 옵션을 설정하여 백 스택을 조작할 수 있습니다. 이는 `NavOptions.Builder`를 통해 이루어집니다.

**예시: 로그인 성공 후 홈 화면으로 이동하며 로그인 플로우를 스택에서 완전히 제거**
```kotlin
// LoginFragment 내
val buttonLogin: Button = view.findViewById(R.id.button_login)
buttonLogin.setOnClickListener {
    // 로그인이 성공했다고 가정
    
    // NavOptions 설정: homeFragment로 이동하면서,
    // nav_graph 전체를 popUpTo 대상으로 하여 inclusive=true로 설정함으로써
    // 로그인 이전의 모든 스택을 제거.
    val navOptions = NavOptions.Builder()
        .setPopUpTo(R.id.nav_graph, true) // nav_graph의 시작 목적지까지 포함하여 모두 제거
        .build()
        
    // 설정된 옵션과 함께 홈 화면으로 이동
    findNavController().navigate(R.id.homeFragment, null, navOptions)
}
```
이 코드는 로그인 후 사용자가 뒤로 가기 버튼을 눌러도 로그인 화면으로 돌아가지 않고 앱이 종료되도록 합니다.

#### 2.4. 백 스택 정보 직접 접근

`NavController`는 현재 백 스택의 상태를 확인하는 메서드도 제공합니다.

* **`currentDestination`:** 현재 화면에 보이는 목적지에 대한 정보를 가져옵니다.
* **`previousBackStackEntry`:** 이전 백 스택 항목에 대한 정보를 가져옵니다.
* **`getBackStackEntry(int destinationId)`:** 특정 ID를 가진 목적지의 `NavBackStackEntry`를 가져옵니다. 이는 특히 `NavBackStackEntry`의 `SavedStateHandle`을 통해 프래그먼트 간에 결과를 전달하거나 데이터를 공유할 때 유용합니다.

### 3. 결론

Jetpack Navigation 라이브러리는 간단한 내비게이션에 대해서는 **백 스택을 자동으로 관리**하여 개발의 편리함을 제공하는 동시에, `popUpTo`와 같은 **선언적 옵션** 및 `NavController`의 **프로그래밍 방식 조작 메서드**들을 통해 **복잡한 내비게이션 시나리오**까지 유연하게 제어할 수 있는 강력한 기능을 갖추고 있습니다. 개발자는 이러한 기능들을 잘 활용하여 사용자의 내비게이션 흐름을 의도한 대로 정확하게 설계하고 구현할 수 있습니다.


## Q. Safe Args란 무엇이며, Jetpack Navigation Component에서 목적지 간에 데이터를 전달할 때 타입 안전성을 어떻게 향상시키나요?

**Safe Args**는 Jetpack Navigation Component와 함께 사용되는 **Gradle 플러그인**입니다. 주된 목적은 목적지(destination, 주로 프래그먼트) 간에 데이터를 전달할 때 발생할 수 있는 오류를 줄이고, 코드를 더 간결하고 안전하게 만드는 것입니다. 이를 위해 내비게이션 그래프에 정의된 인수(argument) 정보를 기반으로 **타입 세이프(type-safe, 유형 안전) 코드를 자동으로 생성**해 줍니다.

---
### 1. Safe Args란 무엇인가?

Safe Args는 개발자가 수동으로 `Bundle`을 만들고 키-값 쌍을 관리하는 대신, 생성된 간단한 객체와 메서드를 통해 데이터를 전달하고 수신할 수 있게 해주는 도구입니다. 이 모든 과정이 컴파일 시점에 검증되므로 런타임 오류의 가능성이 크게 줄어듭니다.

---
### 2. Safe Args가 타입 안전성을 향상시키는 방식 (전통적인 Bundle 방식과 비교)

Safe Args가 어떻게 타입 안전성을 향상시키는지 이해하려면, 먼저 전통적인 `Bundle`을 사용한 데이터 전달 방식의 문제점을 알아야 합니다.

#### 2.1. 전통적인 Bundle 방식의 문제점

`Bundle`을 직접 사용할 때는 다음과 같은 잠재적 오류가 발생할 수 있으며, 이들은 모두 앱 실행 중에만 발견됩니다.

* **타입 불일치 오류 (`ClassCastException`):**
    * 데이터를 보내는 쪽에서는 `"USER_ID"`라는 키에 정수(`Int`) 값을 넣었는데, 받는 쪽에서 실수로 문자열(`String`)로 꺼내려고 시도하면 런타임에 `ClassCastException`이 발생할 수 있습니다.
* **키(Key) 불일치 및 오타:**
    * 데이터를 보내는 쪽과 받는 쪽에서 사용하는 키 문자열(예: `"userId"`, `"user_id"`)이 서로 다르거나 오타가 있으면, 데이터를 제대로 찾지 못해 `null`이나 원치 않는 기본값이 반환되어 `NullPointerException` 또는 예기치 않은 동작으로 이어질 수 있습니다.
* **누락된 인수에 대한 Null 처리 부재:**
    * 반드시 전달되어야 하는 인수가 누락되었을 때, 이를 확인하는 로직이 없다면 앱이 비정상적으로 동작하거나 `NullPointerException`이 발생할 수 있습니다.

#### 2.2. Safe Args의 해결책 (작동 원리)

Safe Args는 이러한 문제들을 **컴파일 시점**에 해결합니다.

**1단계: 내비게이션 그래프에 인수 정의**
먼저, `res/navigation/nav_graph.xml` 파일에서 데이터를 **수신할 목적지**에 `<argument>` 태그를 사용하여 전달받을 인수의 이름(`android:name`), 타입(`app:argType`), 그리고 필요에 따라 기본값(`android:defaultValue`)을 정의합니다.
```xml
<fragment
    android:id="@+id/detailsFragment"
    android:name="com.example.app.DetailsFragment"
    android:label="Details">
    <argument
        android:name="itemId"
        app:argType="integer" />
    <argument
        android:name="itemName"
        app:argType="string"
        app:nullable="true"
        android:defaultValue="N/A" />
</fragment>
```

**2단계: 코드 자동 생성**
프로젝트를 빌드하면, Safe Args 플러그인이 이 XML 파일을 읽고 다음과 같은 클래스들을 자동으로 생성합니다.

* **`Directions` 클래스:** 데이터를 **보내는** 각 목적지에 대해 생성됩니다 (예: `HomeFragmentDirections`). 이 클래스 안에는 내비게이션 그래프에 정의된 각 `<action>`에 해당하는 타입 세이프 메서드가 포함됩니다.
* **`Args` 클래스:** 데이터를 **수신하는** 각 목적지에 대해 생성됩니다 (예: `DetailsFragmentArgs`). 이 클래스 안에는 `<argument>`로 정의된 각 인수에 해당하는 타입 세이프 프로퍼티가 포함됩니다.

**3단계: 타입 세이프 데이터 전송**
데이터를 보낼 때 더 이상 `Bundle`을 수동으로 만들지 않고, 생성된 `Directions` 클래스를 사용합니다.
```kotlin
// HomeFragment.kt 에서
val itemIdValue = 123
val action = HomeFragmentDirections.actionHomeToDetails(itemIdValue) // 생성된 메서드 사용
findNavController().navigate(action)
```
* **타입 안전성:** `actionHomeToDetails` 메서드는 `itemId` 파라미터로 `integer` 타입을 요구합니다. 만약 문자열이나 다른 타입을 전달하려고 하면 **컴파일 시점에 오류**가 발생합니다.
* **키 안전성:** 문자열 키를 직접 사용하지 않으므로 오타로 인한 문제가 원천적으로 차단됩니다.

**4단계: 타입 세이프 데이터 수신**
데이터를 받는 쪽에서는 `by navArgs()` KTX 딜리게이트를 사용하여 생성된 `Args` 클래스의 인스턴스를 쉽게 가져올 수 있습니다.
```kotlin
// DetailsFragment.kt 에서
private val args: DetailsFragmentArgs by navArgs() // KTX 확장 함수 사용

override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    
    val receivedItemId = args.itemId // 타입 캐스팅 불필요, 이미 Int 타입
    val receivedItemName = args.itemName // 타입 캐스팅 불필요, 이미 String? 타입
    
    // 데이터 사용
    Log.d("DetailsFragment", "Received ID: $receivedItemId")
}
```
* **타입 안전성:** `args.itemId`는 XML에 정의된 대로 이미 `Int` 타입입니다. 별도의 타입 캐스팅이 필요 없으며, 잘못된 타입으로 접근할 가능성이 없습니다.
* **Null 안전성:** `itemName` 인수가 XML에서 `app:nullable="true"`로 정의되었다면 `args.itemName`은 `String?` 타입으로 생성되어, 코틀린 컴파일러가 Null 안전 처리를 강제합니다. 만약 필수 인수(nullable이 아니며 기본값이 없는)가 전달되지 않으면, 런타임에 `IllegalArgumentException`이 발생하여 문제가 조기에 발견됩니다.

---
### 3. Safe Args 사용의 주요 이점

1.  **런타임 오류를 컴파일 시점 오류로 전환:**
    `findViewById()`와 유사하게, `Bundle` 사용 시 발생할 수 있는 `ClassCastException`이나 `NullPointerException`과 같은 런타임 오류들을 컴파일 시점에 발견하고 수정할 수 있게 하여 앱의 안정성을 크게 높입니다.

2.  **코드 가독성 및 명확성 향상:**
    `Directions` 클래스의 메서드 시그니처만 봐도 해당 목적지로 이동할 때 어떤 타입의 인수가 필요한지 명확하게 알 수 있습니다. 코드가 자기 설명적이 됩니다.

3.  **상용구 코드(Boilerplate Code) 감소:**
    `Bundle`을 직접 생성하고, 키를 사용하여 값을 넣고, 받는 쪽에서 다시 키로 값을 꺼내 타입 캐스팅과 Null 체크를 하는 반복적인 코드를 작성할 필요가 없습니다.

4.  **리팩토링 용이성:**
    만약 XML에서 인수의 이름이나 타입을 변경하면, 관련된 `Directions` 및 `Args` 클래스가 자동으로 업데이트됩니다. 따라서 이전 인수 이름을 사용하는 코드는 즉시 컴파일 오류를 발생시켜 변경 사항을 쉽게 추적하고 수정할 수 있습니다.

---
### 4. 결론

**Safe Args**는 Jetpack Navigation Component를 사용할 때 목적지 간 데이터 전달을 위한 필수적인 도구입니다. 이는 전통적인 `Bundle` 방식의 잠재적인 런타임 오류들을 **컴파일 시점에 잡아내어 타입 안전성과 Null 안전성을 보장**하고, 코드의 가독성과 유지보수성을 향상시켜 개발자가 더 견고하고 안정적인 내비게이션 흐름을 구축할 수 있도록 돕습니다.