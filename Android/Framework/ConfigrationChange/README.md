# 구성 변경 시 액티비티(Activity)에는 어떤 일이 발생하나요?

안드로이드에서 **구성 변경(configuration change)** 이 발생하면 (예: 화면 회전, 테마 변경, 글꼴 크기 조정 또는 언어 업데이트), 시스템은 새 구성을 적용하기 위해 현재 **액티비티(Activity)를 소멸(destroy)시키고 다시 생성(recreate)** 할 수 있습니다. 이 동작은 앱의 리소스가 업데이트된 구성을 반영하도록 다시 로드되도록 보장합니다.

### 구성 변경 시 기본 동작

1.  **액티비티 소멸 및 재생성:** 구성 변경이 발생하면 액티비티는 소멸된 후 다시 생성됩니다. 이 과정은 다음 단계를 포함합니다.
    * 시스템은 현재 액티비티의 `onPause()`, `onStop()`, `onDestroy()` 메서드를 호출합니다.
    * 액티비티는 새 구성으로 `onCreate()` 메서드가 호출되면서 재생성됩니다.

2.  **리소스 다시 로드:** 시스템은 화면 방향, 테마 또는 로케일(locale)과 같은 변경 사항에 앱이 적응할 수 있도록 새 구성에 따라 리소스(예: 레이아웃, 드로어블 또는 문자열)를 다시 로드합니다.

3.  **데이터 손실 방지:** 재생성 중 데이터 손실을 방지하기 위해 개발자는 `onSaveInstanceState()` 및 `onRestoreInstanceState()` (또는 `onCreate`에서 번들 확인) 메서드를 사용하거나 `ViewModel`을 활용하여 인스턴스 상태를 저장하고 복원할 수 있습니다.

```kotlin
override fun onSaveInstanceState(outState: Bundle) {
    super.onSaveInstanceState(outState)
    outState.putString("user_input", editText.text.toString())
}

override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)

    val restoredInput = savedInstanceState?.getString("user_input")
    editText.setText(restoredInput)
}
```

### 재생성을 유발하는 주요 구성 변경

* **화면 회전:** 화면 방향을 세로 모드와 가로 모드 사이에서 변경하며, 새 크기에 맞게 레이아웃이 다시 로드됩니다.
* **다크/라이트 테마 변경:** 사용자가 다크 모드와 라이트 모드 사이를 전환하면 앱은 테마별 리소스(예: 색상 및 스타일)를 다시 로드합니다.
* **글꼴 크기 변경:** 기기의 글꼴 크기 설정 조정은 새 배율을 반영하도록 텍스트 리소스를 다시 로드합니다.
* **언어 변경:** 시스템 언어 업데이트는 지역화된 리소스(예: 다른 언어의 문자열) 로드를 트리거합니다.

### 액티비티 재생성 피하기

액티비티를 다시 시작하지 않고 구성 변경을 처리하려면, 매니페스트(Manifest)에서 `android:configChanges` 속성을 사용할 수 있습니다. 이 접근 방식은 개발자에게 변경 사항을 프로그래밍 방식으로 처리할 책임을 위임합니다.

```xml
<activity
    android:name=".MyActivity"
    android:configChanges="orientation|screenSize|keyboardHidden|uiMode|fontScale|locale"
    android:label="@string/app_name">
    </activity>
```

이 시나리오에서는 시스템이 액티비티를 소멸시키고 다시 생성하지 않습니다. 대신, `onConfigurationChanged()` 메서드가 호출되어 개발자가 변경 사항을 수동으로 처리할 수 있게 합니다.

```kotlin
override fun onConfigurationChanged(newConfig: Configuration) {
    super.onConfigurationChanged(newConfig)
    // 새 구성에 따라 UI 업데이트 또는 리소스 재설정
    if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
        // 가로 모드 처리
    } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
        // 세로 모드 처리
    }
    // 테마, 글꼴 크기, 언어 변경 등도 newConfig 객체를 통해 확인 가능
}
```

### 요약

구성 변경이 발생하면 기본 동작은 액티비티를 소멸시키고 다시 생성하여 새 구성에 적응하도록 리소스를 다시 로드하는 것입니다. 개발자는 임시 UI 상태를 보존하기 위해 `onSaveInstanceState()`를 사용하거나, UI가 아닌 상태(데이터)에는 `ViewModel`을 사용할 수 있습니다. 재생성을 피하려면 매니페스트에서 `android:configChanges` 속성을 사용하여 개발자가 변경 사항 처리 책임을 지도록 할 수 있습니다.

---

## Q. 개발자는 구성 변경으로 인한 액티비티 재생성 중 데이터 손실을 어떻게 방지할 수 있으며, 임시 상태와 영구 상태를 처리하는 방법은 무엇인가요?

구성 변경(예: 화면 회전, 언어 변경)으로 인해 액티비티(Activity)가 다시 생성될 때 데이터 손실을 방지하고, 다양한 유형의 상태(임시 상태 및 영구 상태)를 처리하는 방법은 다음과 같습니다.

### 1. 임시 상태(Transient State) 처리 및 데이터 손실 방지

임시 상태는 사용자의 현재 상호작용과 관련된 UI 상태(예: 입력 필드의 텍스트, 스크롤 위치, 현재 선택된 항목 등)를 의미하며, 액티비티가 예기치 않게 (구성 변경 등으로) 다시 생성될 때 복원되어야 합니다.

* **`onSaveInstanceState(Bundle outState)` 사용:**
    * **목적:** 액티비티가 소멸되기 직전에 호출되어 임시 UI 상태를 `Bundle` 객체에 저장할 기회를 제공합니다.
    * **작동 방식:**
        1.  시스템은 액티비티가 파괴되기 전에 `onSaveInstanceState()`를 호출하며, 데이터를 저장할 `Bundle` 객체(`outState`)를 전달합니다.
        2.  개발자는 이 `outState` 번들에 필요한 데이터(기본 타입, 문자열, `Parcelable`, `Serializable` 객체 등)를 키-값 쌍으로 저장합니다. (`outState.putString("KEY_USER_INPUT", editText.text.toString());`)
        3.  액티비티가 다시 생성될 때, 저장된 `Bundle`은 `onCreate(Bundle savedInstanceState)` (또는 `onRestoreInstanceState()`) 메서드의 `savedInstanceState` 파라미터로 전달됩니다.
        4.  개발자는 이 `savedInstanceState` 번들이 `null`이 아닌지 확인하고, 데이터를 추출하여 UI 상태를 복원합니다.
    * **적합한 데이터:** 사용자가 입력한 텍스트, 스크롤 위치, 현재 선택된 탭 등 간단한 UI 상태 정보.
    * **한계:** 대용량 데이터나 복잡한 객체 저장에는 적합하지 않으며(번들 크기 제한 및 직렬화 오버헤드), 사용자가 명시적으로 액티비티를 종료(예: 뒤로 가기)할 때는 호출되지 않을 수 있습니다.

* **`ViewModel` (Jetpack 아키텍처 컴포넌트) 사용:**
    * **목적:** UI 관련 데이터를 생명주기를 고려하여 저장하고 관리하도록 설계되었습니다. `ViewModel`은 구성 변경(예: 화면 회전) 시 **파괴되지 않고 유지**되므로, 데이터를 보존하는 데 매우 효과적입니다.
    * **작동 방식:**
        1.  UI 관련 데이터를 `ViewModel` 내에 보관합니다.
        2.  액티비티/프래그먼트가 구성 변경으로 다시 생성될 때, 기존의 `ViewModel` 인스턴스에 다시 연결됩니다. 따라서 `ViewModel` 내의 데이터는 `Bundle`을 통한 저장/복원 과정 없이 즉시 사용 가능합니다.
        3.  `LiveData`나 `StateFlow`와 함께 사용하여 데이터 변경을 관찰하고 UI를 반응적으로 업데이트하는 데 자주 사용됩니다.
    * **적합한 데이터:** 사용자의 프로필 정보, `RecyclerView`에 표시될 아이템 목록, 현재 필터 설정 등 UI에 표시되어야 하거나 UI 로직에 필요한 대부분의 데이터.
    * **한계:** `ViewModel`은 구성 변경에는 강하지만, 시스템에 의한 **프로세스 종료(Process Death)** 시에는 기본적으로 소멸됩니다. 이 경우를 대비하려면 `ViewModel`의 `SavedStateHandle`을 사용하거나 여전히 `onSaveInstanceState`를 보조적으로 활용해야 합니다.

### 2. 영구 상태(Persistent State) 처리

영구 상태는 사용자가 앱을 종료했다가 다시 시작하거나, 기기를 재부팅한 후에도 유지되어야 하는 데이터를 의미합니다.

* **`SharedPreferences`:**
    * **용도:** 앱의 설정 값, 사용자의 간단한 환경설정, 로그인 상태(토큰 등)와 같이 키-값 쌍으로 저장할 수 있는 비교적 적은 양의 원시 데이터(primitive data)를 저장하는 데 사용됩니다.
    * **작동 방식:** 데이터는 앱의 비공개 디렉터리에 XML 파일 형태로 저장됩니다.

* **내부/외부 저장소 (파일 시스템):**
    * **용도:** 이미지, 동영상, 문서, 캐시 데이터, 직렬화된 객체 등 더 큰 파일을 저장하는 데 사용됩니다.
    * **작동 방식:** 기기의 내부 저장소(앱 비공개) 또는 외부 저장소(공유 가능, 권한 필요)에 직접 파일을 읽고 씁니다.

* **SQLite 데이터베이스 (Room Persistence Library 권장):**
    * **용도:** 구조화된 데이터를 대량으로 저장하고 관리해야 할 때 사용합니다. 복잡한 데이터 조회, 인덱싱, 관계 설정 등이 필요할 때 이상적입니다.
    * **작동 방식:** Room은 SQLite 데이터베이스 위에 추상화 계층을 제공하여 데이터베이스 작업을 더 쉽고 강력하며 오류 발생 가능성을 줄여줍니다. 데이터는 앱의 비공개 디렉터리에 데이터베이스 파일로 저장됩니다.

* **클라우드/네트워크 저장소:**
    * **용도:** Firebase Realtime Database, Cloud Firestore, 자체 백엔드 서버 등 원격 서버에 데이터를 저장합니다. 이를 통해 여러 기기 간 데이터 동기화, 다른 사용자와의 데이터 공유, 데이터 백업 등이 가능합니다.
    * **작동 방식:** 네트워크 요청을 통해 원격 서버로 데이터를 보내고 받아옵니다.

### 결론

데이터 손실을 방지하기 위한 방법은 데이터의 성격과 생명주기 요구사항에 따라 달라집니다.
* **임시 UI 상태**는 `onSaveInstanceState()`를 이용한 `Bundle` 저장이나 `ViewModel`을 통해 효과적으로 관리할 수 있으며, `ViewModel`이 구성 변경에 더 강력한 솔루션입니다.
* **영구적으로 보존되어야 하는 데이터**는 `SharedPreferences`, 파일 시스템, SQLite 데이터베이스(Room 권장), 또는 클라우드 저장소와 같은 영구 저장 메커니즘을 사용해야 합니다.

이러한 방법들을 적절히 조합하여 사용하면 구성 변경 및 예기치 않은 상황에서도 사용자 데이터를 안전하게 보존하고 일관된 앱 경험을 제공할 수 있습니다.