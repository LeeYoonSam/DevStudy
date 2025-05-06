# 번들(Bundle)의 목적은 무엇인가요?
번들(Bundle) 은 액티비티(Activity), 프래그먼트(Fragment), 서비스(Service)와 같은 컴포넌트 간에 데이터를 전달하는 데 사용되는 키-값 쌍(key-value pair) 데이터 구조입니다. 앱 내에서 적은 양의 데이터를 효율적으로 전송하는 데 일반적으로 사용됩니다. 번들은 가볍고(lightweight), 안드로이드 운영체제가 쉽게 관리하고 전송할 수 있는 형식으로 데이터를 직렬화(serialize) 하도록 설계되었습니다.

## 번들의 일반적인 사용 사례
 * **액티비티 간 데이터 전달**: 새 액티비티를 시작할 때, Intent에 번들을 첨부하여 대상 액티비티로 데이터를 전달할 수 있습니다.
 * **프래그먼트 간 데이터 전달**: 프래그먼트 트랜잭션(fragment transaction)에서, setArguments()와 getArguments() 메서드를 사용하여 프래그먼트 간에 데이터를 보내는 데 번들이 사용됩니다.
 * **인스턴스 상태 저장 및 복원**: onSaveInstanceState() 및 onRestoreInstanceState() (또는 onCreate)와 같은 생명주기 메서드에서 구성 변경(configuration change) 중에 임시 UI 상태를 저장하고 복원하는 데 번들이 사용됩니다.
 * **서비스로 데이터 전달**: 서비스를 시작하거나 바인드된 서비스(bound service)에 데이터를 전달할 때 번들을 사용할 수 있습니다.

## 번들의 작동 방식
번들은 데이터를 키-값 구조로 직렬화하여 작동합니다. 키는 문자열(String)이며, 값은 기본 타입(primitive types), Serializable 또는 Parcelable 객체, 또는 다른 번들(Bundle) 객체가 될 수 있습니다. 이를 통해 데이터를 효율적으로 저장하고 전송할 수 있습니다.

예시: 액티비티 간 데이터 전달
```kotlin
// 데이터 보내는 액티비티
val intent = Intent(this, TargetActivity::class.java)
intent.putExtra("USER_NAME", "John Doe") // 내부적으로 번들에 데이터를 담음
intent.putExtra("USER_ID", 123)
startActivity(intent)
```

```kotlin
// 데이터 받는 액티비티 (TargetActivity)
val userName = intent.getStringExtra("USER_NAME")
val userId = intent.getIntExtra("USER_ID", -1) // 기본값 지정 가능
```
이 예시에서는 데이터가 Intent.putExtra()를 통해 내부적으로 번들에 패키징됩니다.

예시: 프래그먼트 간 데이터 전달
```kotlin
// 데이터를 보내는 측 (프래그먼트 생성 시)
val fragment = MyFragment()
val args = Bundle()
args.putString("ARG_PARAM1", "value1")
fragment.arguments = args // setArguments() 사용
```

```kotlin
// 데이터를 받는 프래그먼트 (MyFragment)
override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    arguments?.let { // getArguments()로 번들 가져오기
        val param1 = it.getString("ARG_PARAM1")
        // param1 사용
    }
}
```

예시: 상태 저장 및 복원
```kotlin
class MyActivity : AppCompatActivity() {
    private var userScore = 0
    private val SCORE_KEY = "user_score"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // ... 레이아웃 설정 ...

        // 상태 복원 (액티비티가 재생성될 때)
        if (savedInstanceState != null) {
            userScore = savedInstanceState.getInt(SCORE_KEY, 0)
        }
    }

    // 상태 저장 (액티비티가 소멸되기 전 호출될 수 있음)
    override fun onSaveInstanceState(outState: Bundle) {
        outState.putInt(SCORE_KEY, userScore) // 번들에 데이터 저장
        super.onSaveInstanceState(outState)
    }

    // 예시: 점수 업데이트 로직
    fun updateUserScore(newScore: Int) {
        userScore = newScore
    }
}
```
이 경우, 번들은 화면 회전과 같은 구성 변경 중에 사용자 입력(이 예에서는 userScore)이 보존되도록 보장합니다.

## 요약
번들은 안드로이드에서 컴포넌트와 생명주기 이벤트 전반에 걸쳐 데이터를 효율적으로 전달하고 보존하기 위한 중요한 구성 요소입니다. 그 가볍고 유연한 구조는 애플리케이션 상태 및 데이터 전송 관리에 필수적인 도구입니다.


## Q. onSaveInstanceState()는 구성 변경 중에 UI 상태를 보존하기 위해 번들을 어떻게 사용하며, 번들에는 어떤 유형의 데이터를 저장할 수 있나요?

### 1. onSaveInstanceState()가 번들(Bundle)을 사용하여 UI 상태를 보존하는 방식
onSaveInstanceState()는 안드로이드 액티비티(Activity)나 프래그먼트(Fragment)의 생명주기 콜백 메서드 중 하나입니다. 주된 목적은 구성 변경(Configuration Change) (예: 화면 회전, 언어 변경, 키보드 상태 변경 등)이나 시스템에 의해 앱 프로세스가 종료되었다가 다시 시작될 때, 사용자가 보던 임시적인 UI 상태(transient UI state) 를 소량 저장할 수 있도록 하는 것입니다.

**작동 메커니즘**:
 * 호출 시점: 사용자가 명시적으로 액티비티를 종료(예: 뒤로 가기 버튼)하는 경우가 아닌, 구성 변경 등으로 액티비티/프래그먼트가 소멸(destroy)되기 전에 시스템에 의해 호출됩니다. (정확히는 onStop() 전에 호출되지만 항상 보장되지는 않습니다.)
 * 번들 제공: 시스템은 onSaveInstanceState(Bundle outState) 메서드를 호출하면서 비어있는 Bundle 객체(outState)를 파라미터로 전달합니다.
 * 데이터 저장: 개발자는 이 outState 번들에 보존해야 할 UI 관련 데이터를 키-값 쌍으로 저장합니다. 예를 들어 EditText의 텍스트, ScrollView의 스크롤 위치, CheckBox의 체크 상태 등입니다. (outState.putString("KEY_TEXT", editText.text.toString())와 같이 putXXX() 메서드를 사용합니다.)
   * 주의: 이 메서드는 영구 데이터를 저장하기 위한 것이 아닙니다. 영구 데이터는 데이터베이스, SharedPreferences, 파일 등에 저장해야 합니다. 번들에는 UI 상태 복원에 필요한 최소한의 임시 데이터만 넣어야 합니다.
 * 번들 보존: 시스템은 개발자가 데이터를 채운 outState 번들을 보관합니다.
 * 재생성 및 복원: 액티비티/프래그먼트가 구성 변경이나 프로세스 종료 후 다시 생성될 때, 시스템은 보관했던 번들을 다시 전달해 줍니다.
   * 액티비티: onCreate(Bundle savedInstanceState) 메서드의 savedInstanceState 파라미터로 전달됩니다.
   * 프래그먼트: onCreate(Bundle savedInstanceState), onCreateView(..., Bundle savedInstanceState), onViewCreated(..., Bundle savedInstanceState) 등의 메서드의 savedInstanceState 파라미터로 전달됩니다.
 * 데이터 복원: 개발자는 이 savedInstanceState 번들이 null이 아닌지 확인하고, null이 아니라면 저장했던 데이터를 getXXX() 메서드(예: savedInstanceState.getString("KEY_TEXT"))를 사용하여 꺼내와 UI 상태를 복원합니다. (예: editText.setText(savedText))

**ViewModel과의 관계**: 
- 현대 안드로이드 개발에서는 ViewModel을 사용하여 구성 변경에도 데이터를 유지하는 것이 권장됩니다. 
- ViewModel은 구성 변경 시 파괴되지 않으므로 onSaveInstanceState보다 더 많은 양의 데이터를 저장하고 관리하기에 적합합니다. 하지만 ViewModel은 시스템에 의한 프로세스 종료 시에는 파괴될 수 있으므로, onSaveInstanceState는 여전히 프로세스 종료 복원 및 ViewModel이 직접 관리하지 않는 간단한 UI 상태(예: 스크롤 위치)를 저장하는 데 유용하게 사용됩니다. 종종 ViewModel과 onSaveInstanceState를 함께 사용합니다.

### 2. 번들(Bundle)에 저장할 수 있는 데이터 유형
번들은 키(String 타입)와 값으로 이루어진 데이터를 저장하며, 안드로이드 시스템이 프로세스 간 통신(IPC)이나 상태 저장/복원을 위해 효율적으로 직렬화(serialize) 및 역직렬화(deserialize)할 수 있는 타입들만 값으로 저장할 수 있습니다.

**저장 가능한 주요 데이터 유형**:
 * 기본(원시) 타입 (Primitive Types): boolean, byte, char, short, int, long, float, double 및 이들의 배열 (boolean[], int[], double[] 등)
 * 문자열 관련: String, String[] (문자열 배열), CharSequence, CharSequence[]
 * Parcelable 인터페이스 구현 객체:
   * 안드로이드 프레임워크가 IPC 및 데이터 전달을 위해 권장하는 직렬화 방식입니다. Serializable보다 일반적으로 성능이 우수합니다.
   * 많은 안드로이드 기본 클래스(Bitmap, Intent, Bundle 자신 등)가 Parcelable을 구현합니다.
   * 사용자가 직접 만든 클래스도 Parcelable 인터페이스를 구현하여 번들에 넣을 수 있습니다.
   * Parcelable[] (배열), ArrayList<Parcelable> (정확히는 ArrayList<? extends Parcelable>), SparseArray<? extends Parcelable>
 * Serializable 인터페이스 구현 객체:
   * 자바 표준 직렬화 인터페이스입니다. 구현이 간단할 수 있으나(implements Serializable 추가), Parcelable보다 느리고 임시 객체를 더 많이 생성하는 경향이 있습니다.
   * 외부 라이브러리 객체 등 직접 수정할 수 없거나 성능이 크게 중요하지 않은 경우 사용될 수 있습니다.
 * 기타 배열 및 리스트: `ArrayList<Integer>`, `ArrayList<String>`, `ArrayList<CharSequence>`
 * 번들(Bundle): 다른 Bundle 객체를 값으로 포함할 수 있습니다.
 * 기타: Size, SizeF (API 21+), IBinder 등 일부 안드로이드 특정 클래스

**저장할 수 없거나 저장해서는 안 되는 것**:
 * Parcelable 또는 Serializable을 구현하지 않은 임의의 객체.
 * 매우 큰 데이터: 번들은 트랜잭션 크기 제한이 있으므로 대용량 데이터를 저장하기에 적합하지 않습니다.
 * 컨텍스트(Context), 뷰(View), 드로어블(Drawable), 리스너(Listener), 콜백(Callback) 등: 특정 액티비티/프래그먼트 인스턴스에 종속적인 객체를 번들에 저장하면 메모리 누수(Memory Leak)의 원인이 되므로 절대 저장해서는 안 됩니다.

**요약**
- onSaveInstanceState는 구성 변경 시 파괴될 수 있는 액티비티/프래그먼트의 임시 UI 상태를 번들에 저장하여, 재생성될 때 해당 번들을 받아 UI 상태를 복원하는 메커니즘입니다. 
- 번들에는 기본 타입, 문자열, Parcelable, Serializable 객체 등 정해진 타입의 데이터만 저장할 수 있습니다.

