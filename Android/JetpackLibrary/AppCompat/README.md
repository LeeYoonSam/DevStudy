# AppCompat 라이브러리란 무엇인가?

[**AppCompat 라이브러리**](https://developer.android.com/jetpack/androidx/releases/appcompat)는 안드로이드 Jetpack 제품군의 일부로, 개발자가 **이전 버전의 안드로이드와의 호환성**을 유지하도록 돕기 위해 설계되었습니다. 이를 통해 최신 기능과 디자인 패턴을 앱에 구현하면서도 이전 안드로이드 버전과의 하위 호환성을 보장할 수 있습니다. 이 라이브러리는 특히 다양한 안드로이드 버전을 가진 광범위한 기기를 대상으로 하는 개발자에게 유용합니다.

AppCompat 라이브러리는 앱 기능과 디자인 일관성을 향상시키는 다양한 하위 호환 컴포넌트 및 유틸리티를 제공합니다. 주요 기능은 다음과 같습니다.

---

### AppCompat 라이브러리의 주요 기능

#### 1. UI 컴포넌트에 대한 하위 호환성
이 라이브러리는 `FragmentActivity`를 확장하고 이전 버전의 안드로이드와의 호환성을 보장하는 `AppCompatActivity`와 같은 최신 UI 컴포넌트를 도입합니다. 이를 통해 개발자는 이전 안드로이드 버전을 실행하는 기기에서도 액션 바(action bar)와 같은 기능을 사용할 수 있습니다.

#### 2. 머티리얼 디자인(Material Design) 지원
AppCompat을 사용하면 개발자는 이전 안드로이드 버전을 실행하는 기기에도 머티리얼 디자인 원칙을 통합할 수 있습니다. 여기에는 `AppCompatButton`, `AppCompatTextView` 등과 같은 위젯이 포함되며, 이 위젯들은 기기의 API 레벨에 따라 모양과 동작을 자동으로 조정합니다.

#### 3. 테마 및 스타일링 지원
AppCompat을 사용하면 `Theme.AppCompat`과 같은 테마를 사용하여 모든 API 레벨에서 일관된 모양을 보장할 수 있습니다. 이러한 테마는 벡터 드로어블(vector drawables) 지원과 같은 최신 스타일링 기능을 이전 안드로이드 버전에 제공합니다.

#### 4. 동적 기능 지원
라이브러리는 동적 리소스 로딩 및 벡터 드로어블 지원을 제공하여, 하위 호환성을 유지하면서 최신 디자인 요소를 효율적으로 구현하기 더 쉽게 만듭니다.

---

### AppCompat을 사용하는 이유

AppCompat 라이브러리를 사용하는 주된 이유는 최신 안드로이드 기능과 UI 컴포넌트가 지원되는 모든 API 레벨에서 일관되게 작동하도록 보장하기 위해서입니다. 이는 개발자가 최신 기능이 풍부한 앱을 구축하는 데 집중하면서 이전 버전의 안드로이드를 실행하는 기기에 대한 호환성 유지의 복잡성을 줄여줍니다.

---

### AppCompatActivity 사용 예시

아래는 안드로이드 앱에서 `AppCompatActivity`를 사용하는 간단한 예시입니다.

**그림 130. AppCompatExampleActivity.kt**
```kotlin
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}
```
이 예시에서 `AppCompatActivity`는 액티비티가 이전 안드로이드 버전을 실행하는 기기에서도 액션 바와 같은 기능을 사용할 수 있도록 보장합니다.

---

### 요약

AppCompat 라이브러리는 광범위한 기기 및 API 레벨과 호환되는 안드로이드 애플리케이션을 구축하는 데 유용합니다. 하위 호환 컴포넌트, 머티리얼 디자인 지원, 일관된 테마 적용을 제공함으로써 개발을 단순화하는 동시에 이전 기기에서의 사용자 경험을 향상시킵니다.

---

## Q. AppCompat 라이브러리는 이전 안드로이드 버전에서 머티리얼 디자인 지원을 어떻게 가능하게 하며, 이로 인해 이점을 얻는 주요 UI 컴포넌트에는 어떤 것들이 있나요?

`AppCompat` 라이브러리는 안드로이드 개발에서 이전 버전의 운영체제에서도 최신 디자인 가이드라인인 **머티리얼 디자인(Material Design)** 의 주요 요소들을 사용할 수 있도록 지원하는 핵심적인 역할을 합니다. 이를 통해 개발자는 다양한 안드로이드 버전의 기기 사용자에게 일관된 시각적 경험과 현대적인 UI를 제공할 수 있습니다.

---
### 1. AppCompat 라이브러리가 이전 버전에서 머티리얼 디자인을 지원하는 방식

머티리얼 디자인은 안드로이드 5.0 (롤리팝, API 레벨 21)에서 공식적으로 도입되었습니다. `AppCompat` 라이브러리는 그 이전 버전(주로 API 레벨 14 또는 그 이상)에서도 머티리얼 디자인의 핵심적인 시각적 요소와 동작을 구현할 수 있도록 다음과 같은 방법들을 사용합니다.

#### 1.1. AppCompat 테마 (`Theme.AppCompat.*`) 제공
* `AppCompat`은 `Theme.AppCompat.Light`, `Theme.AppCompat.DarkActionBar` 등 다양한 머티리얼 디자인 기반 테마를 제공합니다.
* 이 테마들은 머티리얼 디자인의 색상 팔레트(예: `colorPrimary`, `colorAccent`), 타이포그래피 규칙, 기본 위젯 스타일 등을 이전 버전에서도 유사하게 적용할 수 있도록 미리 정의된 값과 스타일을 포함합니다.
* 앱 전체 또는 특정 액티비티에 이 테마를 적용하는 것이 `AppCompat`을 통한 머티리얼 디자인 지원의 가장 기본적인 단계입니다.

#### 1.2. 위젯 대체 및 래핑 (Widget Replacement/Wrapping)
* `AppCompat`은 표준 안드로이드 프레임워크 위젯에 대응하는 자체 버전의 위젯들을 제공합니다. 예를 들어, `<Button>`, `<TextView>`, `<EditText>`, `<CheckBox>`, `<RadioButton>`, `<Spinner>`, `<ImageView>` 등을 사용할 때, `AppCompat` 테마가 적용된 환경에서는 레이아웃 인플레이션(layout inflation) 시점에 이들이 자동으로 각각 `AppCompatButton`, `AppCompatTextView`, `AppCompatEditText` 등으로 대체되거나 래핑(wrapping)될 수 있습니다.
* 이 `AppCompat` 버전의 위젯들은 이전 API 레벨에서도 머티리얼 디자인의 스타일(또는 최대한 유사한 모습)과 일부 동작(예: 버튼의 그림자 효과, 텍스트 필드의 밑줄 색상 변화, 틴팅(tinting))을 렌더링하도록 내부적으로 구현되어 있습니다.

#### 1.3. 툴바 (`androidx.appcompat.widget.Toolbar`) 제공
* 이전 버전 안드로이드의 네이티브 `ActionBar`를 대체할 수 있는 매우 유연하고 사용자 정의가 용이한 `Toolbar` 위젯을 제공합니다.
* `Toolbar`를 사용하면 모든 `AppCompat` 지원 버전에서 머티리얼 디자인 가이드라인에 맞는 일관된 앱 바(app bar)와 액션 바 기능을 구현할 수 있습니다. 이는 머티리얼 디자인 도입 초기에 매우 중요한 기능이었습니다.

#### 1.4. 벡터 드로어블(VectorDrawable) 백포팅(Backporting)
* `VectorDrawable`은 크기 조절 시에도 깨지지 않는 선명한 아이콘과 이미지를 제공하는 머티리얼 디자인의 중요한 요소입니다. `VectorDrawable`은 API 레벨 21부터 네이티브로 지원되었지만, `AppCompat`은 라이브러리 차원에서 이를 이전 버전(주로 API 레벨 14 이상, 특정 조건 하에서는 API 레벨 7 이상)에서도 사용할 수 있도록 지원합니다.

#### 1.5. 틴팅(Tinting) 및 테마 속성 지원
* `AppCompat`은 `colorPrimary`, `colorAccent`, `android:textColorPrimary` 등 다양한 머티리얼 디자인 테마 속성들을 이전 버전에서도 인식하고, 자신의 위젯들에 이러한 테마 색상을 일관되게 적용할 수 있도록 지원합니다.
* 또한, 프레임워크의 드로어블 틴팅 기능이 제한적이었던 이전 버전에서도 `ImageView`나 버튼 배경 등에 테마 색상에 맞춰 틴팅을 적용하는 기능을 백포팅합니다.

#### 1.6. 다이얼로그 (`AlertDialog.Builder` from `androidx.appcompat.app`)
* `AppCompat`은 `androidx.appcompat.app.AlertDialog`와 같이 머티리얼 디자인 스타일이 적용된 다이얼로그를 제공하여, 이전 버전에서도 일관된 모습의 다이얼로그를 사용할 수 있게 합니다.

#### 1.7. Material Components 라이브러리의 기반 역할
* 더 나아가, `AppCompat`은 더욱 풍부하고 다양한 머티리얼 디자인 컴포넌트(예: `FloatingActionButton`, `Snackbar`, `TabLayout`, `NavigationView` 등)를 제공하는 **Material Components for Android 라이브러리**의 기반이 됩니다. Material Components 라이브러리는 `AppCompat`이 제공하는 기본적인 테마와 호환성 위에서 작동합니다.

---
### 2. AppCompat의 머티리얼 디자인 지원으로 이점을 얻는 주요 UI 컴포넌트

`AppCompat` 라이브러리를 사용함으로써 머티리얼 디자인의 이점을 이전 버전에서도 누릴 수 있는 주요 UI 컴포넌트들은 다음과 같습니다.

* **`Toolbar` (액션 바로 사용 시):** 일관된 머티리얼 디자인 스타일의 앱 바 및 액션 바.
* **`Button` (내부적으로 `AppCompatButton`으로 처리):** 머티리얼 디자인의 버튼 스타일(입체감 있는 버튼, 플랫 버튼, 색상이 적용된 버튼), 물결 효과(ripple effect) 또는 그에 준하는 피드백.
* **`EditText` (내부적으로 `AppCompatEditText`로 처리):** 머티리얼 디자인 텍스트 입력 필드 스타일 (예: 포커스 시 밑줄 색상 변경, 커서/핸들 틴팅). 종종 Material Components 라이브러리의 `TextInputLayout`과 함께 사용되어 완전한 머티리얼 텍스트 필드 동작을 구현합니다.
* **`TextView` (내부적으로 `AppCompatTextView`로 처리):** 머티리얼 디자인 타이포그래피 및 텍스트 외형 속성 일관성 있게 적용.
* **`CheckBox` (내부적으로 `AppCompatCheckBox`로 처리):** 머티리얼 디자인 체크박스 스타일 및 틴팅.
* **`RadioButton` (내부적으로 `AppCompatRadioButton`로 처리):** 머티리얼 디자인 라디오 버튼 스타일 및 틴팅.
* **`Spinner` (내부적으로 `AppCompatSpinner`로 처리):** 머티리얼 디자인 드롭다운/스피너 스타일.
* **`ImageView` (내부적으로 `AppCompatImageView`로 처리):** 벡터 드로어블 및 기타 드로어블 유형에 대한 일관된 틴팅 지원.
* **`AlertDialog` (`androidx.appcompat.app.AlertDialog` 사용 시):** 머티리얼 디자인 스타일의 다이얼로그.
* **기타 입력 위젯:** `SeekBar`(내부적으로 `AppCompatSeekBar`), `RatingBar`(내부적으로 `AppCompatRatingBar`), `Switch`(내부적으로 `SwitchCompat`) 등도 머티리얼 디자인과 유사한 스타일 및 동작을 제공받습니다.

---
### 3. 결론

`AppCompat` 라이브러리는 안드로이드 개발자에게 강력한 도구로, 다양한 API 레벨에 걸쳐 일관된 사용자 경험과 현대적인 머티리얼 디자인을 적용할 수 있도록 지원합니다. 이를 통해 개발자는 하위 호환성 문제에 대한 부담을 줄이고, 더 매력적이고 사용하기 쉬운 애플리케이션을 만드는 데 집중할 수 있습니다.