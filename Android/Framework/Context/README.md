# 컨텍스트란 무엇이며 어떤 유형의 컨텍스트가 있나요?

- [Q. 안드로이드 애플리케이션에서 올바른 유형의 컨텍스트를 사용하는 것이 중요한 이유는 무엇이며, 액티비티 컨텍스트에 대한 수명이 긴 참조를 보유할 경우의 잠재적 위험은 무엇입니까?](#q-안드로이드-애플리케이션에서-올바른-유형의-컨텍스트를-사용하는-것이-중요한-이유는-무엇이며-액티비티-컨텍스트에-대한-수명이-긴-참조를-보유할-경우의-잠재적-위험은-무엇입니까)
- [프로 팁: 컨텍스트를 사용할 때 주의해야 할 점은 무엇입니까?](#-프로-팁-컨텍스트를-사용할-때-주의해야-할-점은-무엇입니까)
- [프로 팁: ContextWrapper란 무엇인가?](#-프로-팁-contextwrapper란-무엇인가)
- [프로 팁: Activity 에서 this 와  baseContext 인스턴스의 차이점은 무엇입니까?](#-프로-팁-activity-에서-this-와--basecontext-인스턴스의-차이점은-무엇입니까)

---

컨텍스트는 애플리케이션의 환경 또는 상태를 나타내며 애플리케이션별 리소스 및 클래스에 대한 액세스를 제공합니다. 앱과 안드로이드 시스템 사이의 다리 역할을 하여 구성 요소가 리소스, 데이터베이스, 시스템 서비스 등에 액세스할 수 있도록 합니다. 컨텍스트는 액티비티 시작, 에셋 접근, 레이아웃 인플래이팅과 같은 작업에 필수적입니다.

## Application Context

Application Context 는 응용 프로그램의 수명 주기와 연결되어 있습니다. 현재 액티비티 또는 프래그먼트와 무관한 전역적이고 오래 지속되는 컨텍스트가 필요할 때 사용됩니다. 이 컨텍스트는 getApplicationContext()를 호출하여 검색됩니다.

### Application Context 에 대한 사용 사례:

- 공유 환경 설정 또는 데이터베이스와 같은 응용 프로그램 전체 리소스에 액세스합니다.
- 전체 앱 수명 주기에 걸쳐 지속되어야 하는 브로드캐스트 수신기를 등록합니다.
- 앱 수명 주기 전반에 걸쳐 있는 라이브러리 또는 구성 요소를 초기화합니다.

## Activity Context

Activity Context(이 인스턴스의 액티비티)는 액티비티의 라이프사이클과 연결되어 있습니다. 리소스에 접근하고, 다른 액티비티를 시작하고, 액티비티에 특정한 레이아웃을 inflating 하는데 사용됩니다.

### 액티비티 상황에 대한 사용 사례:

- UI 구성 요소 생성 또는 수정.
- 다른 액티비티를 시작합니다.
- 현재 액티비티 범위가 지정된 리소스 또는 테마에 액세스합니다.

## Service Context

Service Context는 서비스의 라이프사이클과 연결되어 있습니다. 네트워크 작업을 수행하거나 음악을 재생하는 것과 같이 백그라운드에서 실행되는 작업에 주로 사용됩니다. 서비스에 필요한 시스템 수준 서비스에 대한 액세스를 제공합니다.

## Broadcast Context

Broadcast Context는 브로드캐스트 수신기가 호출될 때 제공됩니다. 그것은 수명이 짧으며 일반적으로 특정 방송에 응답하는 데 사용됩니다. 이 컨텍스트에서 오래 실행되는 작업을 수행하지 마십시오.

## Common Use Cases of Context

- 리소스 액세스: 컨텍스트는 getString() 또는 getDrawable()과 같은 메서드를 사용하여 문자열, 드로어블 및 dimen 과 같은 리소스에 대한 액세스를 제공합니다.
- 레이아웃 inflating: 컨텍스트를 사용하여 LayoutInflater를 사용하여 XML 레이아웃을 뷰로 inflating합니다.
- 액티비티 및 서비스 시작: 액티비티(startActivity()) 및 서비스(startService())를 시작하려면 컨텍스트가 필요합니다.
- 시스템 서비스 액세스: 컨텍스트는 getSystemService()를 통해 ClipboardManager 또는 ConnectivityManager와 같은 시스템 수준 서비스에 대한 액세스를 제공합니다.
- 데이터베이스 및 공유 환경 설정 액세스: 컨텍스트를 사용하여 SQLite 데이터베이스 또는 공유 환경 설정과 같은 영구 저장 메커니즘에 액세스합니다.

## 요약

컨텍스트는 안드로이드의 핵심 구성 요소로, 앱과 시스템 리소스 간의 상호 작용을 가능하게 합니다. Application Context, Activity Context, Service Context 와 같은 다양한 유형의 컨텍스트가 존재하며, 각각 다른 목적을 수행합니다. 컨텍스트의 적절한 사용은 효율적인 자원 관리를 보장하고 메모리 누출이나 충돌을 방지하므로 올바른 컨텍스트를 선택하고 불필요하게 유지하지 않도록 하는 것이 필수적입니다.


## Q. 안드로이드 애플리케이션에서 올바른 유형의 컨텍스트를 사용하는 것이 중요한 이유는 무엇이며, 액티비티 컨텍스트에 대한 수명이 긴 참조를 보유할 경우의 잠재적 위험은 무엇입니까?

안드로이드 애플리케이션에서 올바른 유형의 컨텍스트(Context)를 사용하는 것은 매우 중요하며, 특히 액티비티(Activity) 컨텍스트를 잘못 사용하면 심각한 문제를 일으킬 수 있습니다.

**1. 올바른 유형의 컨텍스트 사용이 중요한 이유**

컨텍스트는 안드로이드 앱 환경의 다양한 정보(리소스, 시스템 서비스 접근 등)에 대한 핸들(handle) 역할을 합니다. 컨텍스트에는 여러 종류가 있으며(주로 `Application` 컨텍스트와 `Activity` 컨텍스트), 각각의 수명 주기와 기능 범위가 다릅니다. 올바른 컨텍스트를 사용해야 하는 이유는 다음과 같습니다.

* **기능 접근:** 특정 작업은 특정 유형의 컨텍스트에서만 가능하거나 적합합니다.
    * **UI 관련 작업:** 레이아웃 인플레이션(Inflation), 다이얼로그(Dialog) 표시, 다른 액티비티 시작 등은 일반적으로 사용자 인터페이스 윈도우와 연결된 `Activity` 컨텍스트가 필요합니다. `Application` 컨텍스트로 이러한 작업을 시도하면 앱이 비정상 종료되거나 테마가 잘못 적용될 수 있습니다.
    * **리소스 및 시스템 서비스 접근:** 문자열, 드로어블 같은 리소스나 시스템 서비스(`getSystemService()`) 접근은 `Application` 컨텍스트나 `Activity` 컨텍스트 모두 가능할 때가 많습니다. 하지만 `Activity` 컨텍스트는 해당 액티비티의 테마를 따르므로, 테마에 따라 다른 리소스가 로드될 수 있습니다.
* **수명 주기 관리:** 컨텍스트는 자체적인 수명 주기를 가집니다. `Application` 컨텍스트는 애플리케이션 프로세스가 살아있는 동안 계속 유지되지만, `Activity` 컨텍스트는 해당 액티비티가 소멸(Destroy)되면 함께 사라집니다. 작업의 유효 범위에 맞는 컨텍스트를 사용해야 합니다. 예를 들어, 액티비티가 활성 상태일 때만 동작해야 하는 리스너 등록은 `Activity` 컨텍스트를, 앱 전체에서 유지되어야 하는 싱글톤 초기화 등에는 `Application` 컨텍스트를 사용해야 합니다.
* **메모리 누수 방지 (가장 중요):** 잘못된 컨텍스트 사용, 특히 `Activity` 컨텍스트의 부적절한 참조 유지는 메모리 누수(Memory Leak)의 주요 원인입니다.

**2. 액티비티 컨텍스트에 대한 수명이 긴 참조 보유의 잠재적 위험**

가장 큰 위험은 **메모리 누수(Memory Leak)** 입니다.

* **메모리 누수란?** `Activity`는 화면 구성(View 계층 구조), 내부 상태 등 많은 메모리를 차지하는 객체입니다. 사용자가 화면을 벗어나거나 화면 회전 등으로 인해 `Activity`가 시스템에 의해 소멸(Destroy)되어 더 이상 필요 없게 되었음에도 불구하고, 가비지 컬렉터(GC)가 이 `Activity` 객체를 메모리에서 해제하지 못하는 현상을 말합니다.
* **발생 원인:** `Activity`의 수명 주기보다 더 오래 살아남는 객체(예: 싱글톤, static 변수, 백그라운드 스레드)가 `Activity` 컨텍스트(`this` 또는 `ActivityName.this`)에 대한 참조를 계속 가지고 있을 때 발생합니다. 이 참조 때문에 GC는 `Activity` 객체가 여전히 사용 중이라고 판단하여 메모리에서 회수하지 못합니다.
    * **예시:**
        * 싱글톤 객체가 초기화될 때 `Activity` 컨텍스트를 전달받아 멤버 변수로 저장하는 경우.
        * 정적(static) 변수가 `Activity` 내부의 View나 Drawable을 참조하는 경우.
        * `Activity`의 비정적(non-static) 내부 클래스나 익명 클래스(예: 리스너, 핸들러)의 인스턴스가 백그라운드 스레드나 `Activity`보다 오래 지속되는 객체로 전달되는 경우 (이들은 암묵적으로 외부 클래스인 `Activity`의 참조를 가짐).
* **위험성/결과:** 메모리 누수가 발생하면 불필요한 `Activity` 객체들이 메모리에 계속 쌓이게 됩니다. 이는 가용 메모리 부족으로 이어져 결국 `OutOfMemoryError`를 발생시켜 앱이 강제 종료될 수 있습니다. 또한, 잦은 가비지 컬렉션 유발로 앱 성능 저하 및 버벅임(stuttering)의 원인이 됩니다.

**결론 및 권장 사항:**

* UI와 직접 관련 없거나, 앱 전역적으로 필요한 작업(예: 싱글톤 초기화, 시스템 서비스 접근, 파일 접근)에는 `getApplicationContext()`를 사용하여 `Application` 컨텍스트를 사용하세요.
* UI 관련 작업(레이아웃 인플레이션, 다이얼로그 생성, 다른 액티비티 시작)에는 반드시 해당 `Activity`의 컨텍스트(`this`)를 사용하세요.
* `Activity` 컨텍스트를 참조하는 객체의 수명 주기가 `Activity`의 수명 주기를 넘지 않도록 주의하세요. 필요하다면 `WeakReference` 사용을 고려하고, `onDestroy()` 등 적절한 시점에 리스너나 콜백 등록을 해제해야 합니다. 백그라운드 작업에는 `Activity` 컨텍스트를 직접 전달하지 않는 것이 안전합니다.


## 💡 프로 팁: 컨텍스트를 사용할 때 주의해야 할 점은 무엇입니까?

- 컨텍스트는 안드로이드에서 편리한 메커니즘이지만, 부적절한 사용은 메모리 누출, 충돌 또는 비효율적인 리소스 처리와 같은 심각한 문제로 이어질 수 있습니다. 이러한 함정을 피하려면 컨텍스트를 효과적으로 사용하기 위한 뉘앙스와 모범 사례를 이해하는 것이 중요합니다.
- 가장 일반적인 문제 중 하나는 컨텍스트, 특히 Activity 또는 Fragment 컨텍스트에 대한 참조가 라이프사이클을 벗어나 유지하는 것입니다. 이는 가비지 수집기가 컨텍스트 또는 관련 리소스에 대한 메모리를 회수할 수 없기 때문에 메모리 누출로 이어질 수 있습니다.

예를 들어, 아래의 예제 코드는 메모리 누수를 일으킬 것이다:

```kotlin
object Singleton {
    var context: Context? = null // This retains the context, causing a memory leak
}
```

대신, 컨텍스트가 필요한 수명이 긴 객체에 대해 애플리케이션 컨텍스트를 사용하세요.

```kotlin
object Singleton {
    lateinit var applicationContext: Context
}
```

아래 예는 Application Context 의 오용 사례를 보여줍니다.

```kotlin
val dialog = AlertDialog.Builder(context.getApplicationContext()) // Incorrect
val dialog = AlertDialog.Builder(activityContext) // Correct

val button = Button(activity)
activity.finish() // The activity is destroyed, but the button retains a reference
```

### 백그라운드 스레드에서 컨텍스트 사용을 피하십시오

컨텍스트는 메인 스레드, 특히 리소스에 접근하거나 UI와 상호 작용하기 위해 설계되었습니다. 백그라운드 스레드에서 사용하면 예기치 않은 충돌이나 스레딩 문제가 발생할 수 있습니다. UI 관련 컨텍스트 리소스와 상호 작용하기 전에 메인 스레드로 다시 전환합니다.

```kotlin
viewModelScope.launch {
    val data = fetchData()
    withContext(Dispatchers.Main) {
        Toast.makeText(context, "Data fetched", Toast.LENGTH_SHORT).show()
    }
}
```

## 요약

컨텍스트를 효과적으로 사용하려면 일반적인 함정을 피하기 위해 신중한 고려가 필요합니다. Activity 또는 Fragment 컨텍스트를 수명 주기 이후에 유지하지 않아야 합니다. 이렇게 하면 메모리 누수가 발생할 수 있습니다. 항상 특정 작업에 대해 올바른 유형의 컨텍스트를 선택하고 백그라운드 스레드에서 또는 관련 구성 요소가 파괴된 후에는 사용하지 마십시오. 또한 익명의 내부 클래스와 콜백은 실수로 컨텍스트에 대한 참조를 보유할 수 있으므로 주의하십시오. 적절한 컨텍스트 관리는 효율적인 리소스 사용을 보장하고 메모리 누출 또는 응용 프로그램 충돌을 방지하는 데 도움이 됩니다.

## 💡 프로 팁: ContextWrapper란 무엇인가?

ContextWrapper는 Context 객체를 래핑하고 래핑된 컨텍스트에 호출을 위임하는 기능을 제공하는 Android의 기본 클래스입니다. 원래 컨텍스트의 동작을 수정하거나 확장하는 중간 레이어 역할을 합니다. ContextWrapper를 사용하면 기본 컨텍스트를 변경하지 않고도 기능을 사용자 지정할 수 있습니다.

### 컨텍스트 래퍼의 목적

ContextWrapper는 기존 컨텍스트의 특정 동작을 개선하거나 재정의해야 할 때 사용됩니다. 이를 통해 원래 컨텍스트에 대한 호출을 가로채고 추가 기능 또는 사용자 지정 동작을 제공할 수 있습니다.

### 사용 사례

1. 사용자 지정 컨텍스트: 다른 테마를 적용하거나 리소스를 특수한 방식으로 처리하는 것과 같은 특정 목적을 위해 사용자 지정 컨텍스트를 생성해야 하는 경우.
2. 동적 리소스 처리: 컨텍스트를 래핑하여 문자열, 치수 또는 스타일과 같은 리소스를 동적으로 제공하거나 수정합니다.
3. 종속성 주입: Dagger 및 Hilt와 같은 라이브러리는 종속성 주입을 위해 구성 요소에 사용자 지정 컨텍스트를 첨부하기 위해 ContextWrapper를 만듭니다.

### ContextWrapper의 예

아래 코드는 ContextWrapper를 사용하여 사용자 지정 테마를 적용하는 방법을 보여줍니다.

```kotlin
class CustomThemeContextWrapper(base: Context) : ContextWrapper(base) {
    override fun getTheme(): Resources.Theme {
        val theme = super.getTheme()
        theme.applyStyle(R.style.CustomTheme, true) // Apply a custom theme
        return theme
    }
}
```

```kotlin
class MyActivity : AppCompatActivity() {
    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(CustomThemeContextWrapper(newBase))
    }
}
```

이 예에서 CustomThemeContextWrapper는 BaseContext의 기본 동작을 재정의하여 Activity에 특정 테마를 적용합니다.

### Key Benefits

- 재사용성: 사용자 지정 로직을 래퍼 클래스에 캡슐화하여 여러 구성 요소에서 재사용할 수 있습니다.
- 캡슐화: 원래 컨텍스트 구현을 변경하지 않고 동작을 향상 또는 수정합니다.
- 호환성: 기존 컨텍스트 객체와 원활하게 작동하여 이전 버전과의 호환성을 유지합니다.

## 요약

ContextWrapper는 Android에서 Context 동작을 사용자 정의하기 위한 유연하고 재사용 가능한 도구입니다. 이를 통해 개발자는 직접 변경하지 않고 원래 컨텍스트에 대한 호출을 가로채고 수정할 수 있으므로 모듈식 및 적응 가능한 애플리케이션을 구축하는 데 필수적인 클래스가 됩니다.

## 💡 프로 팁: Activity 에서 this 와  baseContext 인스턴스의 차이점은 무엇입니까?

액티비티에서 this 와 baseContext 모두 컨텍스트에 대한 액세스를 제공하지만 다른 목적을 제공하고 Android 컨텍스트 계층의 다른 수준을 나타냅니다. 각각을 언제 사용해야 하는지 아는 것은 코드에서 혼란이나 잠재적인 문제를 피하는 데 매우 중요합니다.

### this in an Activity

액티비티에서 this 키워드는 Activity 클래스의 현재 인스턴스를 나타냅니다. Activity는 ContextWrapper의 하위 클래스이기 때문에(따라서 간접적으로 Context의 하위 클래스), 이는 라이프사이클 관리 및 UI와의 상호 작용과 같은 추가 기능을 포함하여 액티비티의 특정 컨텍스트에 대한 액세스를 제공합니다.

액티비티에서 이를 사용하면 액티비티의 현재 컨텍스트를 가리키는 경우가 많으며, 이를 통해 해당 액티비티와 관련된 메서드를 호출할 수 있습니다. 예를 들어, 다른 액티비티를 시작하거나 이 특정 액티비티에 연결된 다이얼로그를 표시해야 하는 경우 이를 사용할 수 있습니다.

```kotlin
val intent = Intent(this, AnotherActivity::class.java)
startActivity(intent)

val dialog = AlertDialog.Builder(this)
    .setTitle("Example")
    .setMessage("This dialog is tied to this Activity instance.")
    .show()
```

### baseContext in an Activity

baseContext는 액티비티가 구축되는 기본 또는 "기본" 컨텍스트를 나타냅니다. Activity가 확장하는 ContextWrapper 클래스의 일부입니다. baseContext는 일반적으로 Context 메서드에 대한 핵심 구현을 제공하는 ContextImpl 인스턴스입니다.

baseContext는 일반적으로 getBaseContext() 메서드를 통해 액세스됩니다. 직접 사용하는 경우는 거의 없지만 사용자 지정 ContextWrapper 구현으로 작업하거나 래핑된 컨텍스트 뒤에 있는 원래 컨텍스트를 참조해야 할 때 관련성이 있습니다.

```kotlin
val systemService = baseContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE)
```

**this 와 baseContext의 주요 차이점**

1. 범위: this는 현재 Activity 인스턴스와 그 라이프사이클을 나타내며, baseContext는 Activity가 구축되는 하위 수준의 컨텍스트를 나타냅니다.
2. 사용법: this는 일반적으로 액티비티를 시작하거나 다이얼로그를 표시하는 것과 같이 액티비티의 수명 주기 또는 UI에 연결된 작업에 사용됩니다. baseContext는 일반적으로 컨텍스트의 핵심 구현과 상호 작용할 때, 특히 사용자 지정 ContextWrapper 시나리오에서 사용됩니다.
3. 계층 구조: baseContext는 액티비티의 기본 컨텍스트입니다. baseContext에 액세스하면 Activity가 ContextWrapper로 제공하는 추가 기능을 우회합니다.

**예: 사용자 지정 컨텍스트 래핑**

사용자 지정 ContextWrapper로 작업할 때, this 와 baseContext의 구별이 중요해집니다. 이 키워드는 여전히 액티비티 자체를 참조하는 반면, baseContext는 수정되지 않은 원본 컨텍스트에 대한 액세스를 제공합니다.

```kotlin
class CustomContextWrapper(base: Context) : ContextWrapper(base) {
     override fun getSystemService(name: String): Any? {
         // Example: Modify the LayoutInflater
         if (name == Context.LAYOUT_INFLATER_SERVICE) {
             val inflater = super.getSystemService(name) as LayoutInflater
             return inflater.cloneInContext(this)
         }
         return super.getSystemService(name)
     }
}

override fun attachBaseContext(newBase: Context) {
    super.attachBaseContext(CustomContextWrapper(newBase))
}
```

## 요약

- 액티비티에서 this 는 현재 액티비티 인스턴스를 참조하며 라이프사이클 및 UI별 기능을 갖춘 높은 수준의 컨텍스트를 제공합니다. 
- 반면에 baseContext는 액티비티가 구축되는 기본 컨텍스트를 나타내며, 종종 사용자 지정 ContextWrapper 구현과 같은 고급 시나리오에서 사용됩니다. 
- this 는 안드로이드 개발에서 가장 일반적으로 사용되는 반면, baseContext를 이해하는 것은 모듈식 재사용 가능한 구성 요소를 디버깅하고 만드는 데 도움이 됩니다.