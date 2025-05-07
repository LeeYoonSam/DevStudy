# 액티비티(Activity) 또는 프래그먼트(Fragment) 간에 데이터를 어떻게 전달하나요?

액티비티 또는 프래그먼트 간의 데이터 전송은 상호작용적이고 동적인 화면을 만드는 데 중요합니다. 안드로이드는 앱의 아키텍처를 준수하면서 원활한 통신을 보장하는 다양한 메커니즘을 제공합니다.

### 액티비티 간 데이터 전달

한 액티비티에서 다른 액티비티로 데이터를 전달하는 가장 일반적으로 사용되는 메커니즘은 **인텐트(Intent)** 입니다. 데이터는 키-값 쌍(`putExtra()`)을 사용하여 인텐트에 추가되며, 수신 액티비티는 `getIntent()`를 사용하여 이를 검색합니다.

```kotlin
// 데이터 보내는 액티비티 (SourceActivity.kt)
val intent = Intent(this, DestinationActivity::class.java)
intent.putExtra("USER_ID", 123) // 키-값 쌍으로 데이터 추가
intent.putExtra("USER_NAME", "John Doe")
startActivity(intent)

// 데이터 받는 액티비티 (DestinationActivity.kt)
val userId = intent.getIntExtra("USER_ID", 0) // 기본값 지정 가능
val userName = intent.getStringExtra("USER_NAME")
```

### 프래그먼트 간 데이터 전달

프래그먼트 간 통신에는 **번들(Bundle)** 을 사용할 수 있습니다. 데이터를 보내는 프래그먼트는 키-값 쌍으로 번들을 생성하고 `arguments`를 통해 수신 프래그먼트로 전달합니다.

```kotlin
// 데이터를 보내는 프래그먼트 (SenderFragment.kt)
val receiverFragment = ReceiverFragment()
val bundle = Bundle()
bundle.putString("MESSAGE_KEY", "Hello from SenderFragment")
receiverFragment.arguments = bundle // setArguments()로 번들 설정

// 데이터를 받는 프래그먼트 (ReceiverFragment.kt)
override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    arguments?.let { // getArguments()로 번들 가져오기
        val message = it.getString("MESSAGE_KEY")
        // 메시지 사용
    }
}
```

### Jetpack Navigation 라이브러리를 이용한 프래그먼트 간 데이터 전달

Safe Args 플러그인과 함께 Jetpack Navigation 라이브러리를 사용하는 경우, 목적지 간 타입 세이프(type-safe, 유형 안전) 내비게이션을 가능하게 하는 direction 및 argument 클래스를 생성할 수 있습니다.

1.  **내비게이션 그래프(navigation graph)에 인수(arguments) 정의**
    ```xml
    <fragment
        android:id="@+id/myFragment"
        android:name="com.example.MyFragment"
        android:label="MyFragment">
        <argument
            android:name="userId"
            app:argType="integer" />
    </fragment>
    ```

2.  **소스 프래그먼트에서 데이터 전달**
    Safe Args 플러그인은 컴파일 타임에 destination 객체와 빌더 클래스를 생성하여, 아래 예시와 같이 안전하고 명시적으로 인수를 전달할 수 있게 합니다.
    ```kotlin
    // SourceFragment.kt
    val userId = 123
    // 생성된 Directions 클래스 사용
    val action = MyFragmentDirections.actionMyFragmentToDestinationFragment(userId)
    findNavController().navigate(action)
    ```

3.  **목적지 프래그먼트에서 데이터 검색**
    마지막으로, 아래 코드와 같이 전달된 인수에서 데이터를 검색할 수 있습니다.
    ```kotlin
    // DestinationFragment.kt
    // 생성된 Args 클래스 사용
    private val args: DestinationFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val userId = args.userId // 타입 세이프로 인수 접근
        // userId 사용
    }
    ```
    Safe Args를 사용하여 강력한 타입의 인수를 정의하고 검색함으로써, 런타임 오류를 줄이고 프래그먼트 간 가독성을 향상시킬 수 있습니다.

### 공유 ViewModel 사용

프래그먼트가 동일한 액티비티 내에서 통신해야 할 때, **공유 ViewModel(Shared ViewModel)** 이 권장되는 접근 방식입니다. 공유 ViewModel은 동일한 액티비티 내의 여러 프래그먼트 간에 공유되는 ViewModel 인스턴스를 의미합니다. 이는 Jetpack의 `androidx.fragment:fragment-ktx` 패키지에서 제공하는 `activityViewModels()` 메서드를 사용하여 달성됩니다. 이 메서드는 ViewModel의 범위를 액티비티로 한정하여 프래그먼트가 동일한 ViewModel 인스턴스에 접근하고 공유할 수 있게 합니다. 이 방법은 프래그먼트 간의 강한 결합(tightly coupling)을 피하고 라이프사이클을 인식하는(lifecycle-aware) 반응형(reactive) 데이터 공유를 가능하게 합니다.

```kotlin
// MySharedViewModel.kt
class MySharedViewModel : ViewModel() {
    val sharedData = MutableLiveData<String>()

    fun updateData(newData: String) {
        sharedData.value = newData
    }
}

// FragmentA.kt
class FragmentA : Fragment() {
    // 액티비티 범위의 ViewModel 가져오기
    private val sharedViewModel: MySharedViewModel by activityViewModels()

    fun sendDataToB(data: String) {
        sharedViewModel.updateData(data)
    }
}

// FragmentB.kt
class FragmentB : Fragment() {
    // 동일한 액티비티 범위의 ViewModel 가져오기
    private val sharedViewModel: MySharedViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sharedViewModel.sharedData.observe(viewLifecycleOwner) { data ->
            // 데이터 변경 시 UI 업데이트
            Log.d("FragmentB", "Received data: $data")
        }
    }
}
```

### 요약

* 액티비티 간에는 `putExtra()`와 `getIntent()`를 사용하여 **인텐트**로 데이터를 전달합니다.
* 프래그먼트 간에는 일반적으로 `arguments` 속성을 통해 **번들**을 사용하여 데이터를 전달합니다.
* 생성된 direction 및 argument 클래스를 통해 프래그먼트 간 타입 세이프 인수 전달을 활성화하려면 **Safe Args 플러그인과 함께 Jetpack Navigation**을 사용하세요.
* 동일한 액티비티 내의 프래그먼트 간 데이터 공유에는 **공유 ViewModel**이 라이프사이클을 인식하고 디커플링된(decoupled) 솔루션을 제공합니다. 각 방법에는 고유한 사용 사례가 있으며, 선택은 애플리케이션의 특정 요구 사항에 따라 달라집니다.

---

## Q. 공유 ViewModel은 동일한 액티비티 내의 프래그먼트 간 통신을 어떻게 용이하게 하며, 번들이나 직접적인 프래그먼트 트랜잭션을 사용하는 것보다 어떤 장점을 제공하나요?

동일한 액티비티 내의 프래그먼트 간 통신에서 **공유 ViewModel(Shared ViewModel)** 은 매우 효과적이고 권장되는 방식입니다. 이는 번들(Bundle)이나 직접적인 프래그먼트 트랜잭션(예: 인터페이스 콜백) 방식에 비해 여러 가지 장점을 제공합니다.

### 공유 ViewModel의 통신 방식

1.  **액티비티 범위(Activity Scope):** 공유 ViewModel은 일반적으로 호스팅하는 액티비티의 생명주기 범위(scope)에 맞춰 생성됩니다. 즉, 해당 액티비티에 연결된 모든 프래그먼트는 **동일한 ViewModel 인스턴스**에 접근할 수 있습니다.
2.  **인스턴스 접근:** 프래그먼트들은 `activityViewModels()` 딜리게이트(Kotlin `androidx.fragment:fragment-ktx` 확장 기능)를 사용하여 이 액티비티 범위의 ViewModel 인스턴스를 가져옵니다.
3.  **데이터 홀더 및 관찰:**
    * ViewModel은 공유할 데이터를 `LiveData`나 `StateFlow`와 같은 관찰 가능한(observable) 형태로 보관합니다.
    * 데이터를 **생산하는 프래그먼트**(또는 액티비티)는 ViewModel 내의 데이터를 업데이트합니다. (예: ViewModel의 메서드를 호출하여 `LiveData` 값을 변경)
    * 데이터를 **소비하는 다른 프래그먼트(들)** 는 동일한 ViewModel 인스턴스로부터 이 `LiveData` 또는 `StateFlow`를 관찰(observe)합니다. 데이터가 변경되면, 관찰자(observer)가 알림을 받고 UI를 업데이트하는 등 적절히 반응합니다.
4.  **디커플링(Decoupling):** 프래그먼트들은 서로 직접 통신하지 않습니다. 대신 ViewModel을 **중개자**로 사용하여 통신합니다. ViewModel은 공유 데이터 및 관련 비즈니스 로직의 중앙 허브 역할을 합니다.

### 번들 또는 직접적인 프래그먼트 트랜잭션 방식 대비 장점

1.  **생명주기 인식(Lifecycle Awareness):**
    * **ViewModel:** ViewModel은 본질적으로 생명주기를 인식합니다. `LiveData`나 `StateFlow`(특히 `collectAsStateWithLifecycle` 또는 `LifecycleOwner`와 함께 관찰될 때)는 프래그먼트가 활성 생명주기 상태(예: `STARTED` 또는 `RESUMED`)일 때만 업데이트를 전달합니다. 이는 프래그먼트의 뷰가 소멸되었을 때 UI를 업데이트하려다 발생하는 비정상 종료나 문제를 방지합니다.
    * **번들/직접 트랜잭션:** 번들을 통한 데이터 전달은 일반적으로 일회성이며 초기 설정에 적합합니다. 수신 프래그먼트가 적절한 상태가 아닐 때 데이터를 처리하기가 더 복잡할 수 있습니다. 인터페이스 콜백과 같은 직접적인 트랜잭션은 분리된(detached) 프래그먼트나 뷰가 파괴된 프래그먼트의 메서드를 호출하지 않도록 수동으로 생명주기를 신중하게 관리해야 합니다.

2.  **구성 변경(Configuration Changes) 시 데이터 유지:**
    * **ViewModel:** 액티비티 범위의 ViewModel은 화면 회전과 같은 구성 변경에도 살아남습니다. 데이터가 ViewModel 내에 안전하게 유지되므로 `onSaveInstanceState`를 사용하여 데이터를 저장하고 복원하는 번거로운 과정이 줄어듭니다.
    * **번들/직접 트랜잭션:** 번들(`setArguments`)은 초기 데이터 전달에는 좋지만, 프래그먼트가 생성된 후 변경되는 데이터를 구성 변경 시 유지하려면 `onSaveInstanceState`를 사용해야 하며, 복잡한 데이터의 경우 번거롭습니다. 직접적인 트랜잭션 방식은 본질적으로 상태 유지를 지원하지 않습니다.

3.  **디커플링(느슨한 결합) 및 테스트 용이성:**
    * **ViewModel:** 프래그먼트들이 서로를 알 필요 없이 오직 공유 ViewModel만 알면 되므로, 프래그먼트 간의 결합도를 낮춥니다. 이는 각 프래그먼트를 더 모듈화하고 재사용하기 쉽게 만들며, 개별적으로 테스트하기 용이하게 합니다. ViewModel 자체도 독립적으로 테스트할 수 있습니다.
    * **번들/직접 트랜잭션:** 인터페이스를 통한 직접적인 통신은 프래그먼트 간 또는 프래그먼트와 액티비티 간의 강한 결합을 유발할 수 있어 유지보수와 테스트가 더 어려워질 수 있습니다.

4.  **복잡하거나 지속적인 데이터 공유에 적합:**
    * **ViewModel:** 시간이 지남에 따라 변경될 수 있는 복잡한 데이터 객체나 데이터 스트림(예: 사용자 선택, 필터 상태, 저장소로부터의 지속적인 업데이트)을 공유하는 데 이상적입니다. `LiveData`와 `StateFlow`는 이러한 반응형 패턴을 자연스럽게 지원합니다.
    * **번들:** 주로 프래그먼트 초기 설정 시 간단한 일회성 데이터 전달에 가장 적합하며, 지속적인 업데이트나 관찰이 필요한 복잡한 객체에는 부적합합니다.

5.  **보일러플레이트 코드 감소 (지속적인 통신 시):**
    * **ViewModel:** 일단 설정되면 데이터 관찰 및 업데이트가 간단합니다.
    * **번들/직접 트랜잭션:** 인터페이스를 사용하여 복잡한 통신 흐름을 관리하면, 특히 많은 프래그먼트가 통신해야 하거나 액티비티가 복잡한 중개자 역할을 해야 하는 경우 더 많은 보일러플레이트 코드가 발생할 수 있습니다.

6.  **명확한 데이터 소유권:**
    * **ViewModel:** ViewModel이 공유 데이터를 명확하게 소유하고 관리합니다. 프래그먼트는 ViewModel의 정의된 API(메서드 및 관찰 가능한 필드)를 통해 이 데이터의 소비자 또는 수정자 역할을 합니다.
    * **번들/직접 트랜잭션:** 데이터 소유권이 덜 명확해질 수 있으며, 특히 여러 프래그먼트가 액티비티를 통해 데이터를 주고받을 때 복잡해질 수 있습니다.

요약하자면, 공유 ViewModel은 생명주기 안전성, 구성 변경 시 데이터 유지, 향상된 디커플링 및 테스트 용이성, 복잡한 데이터 처리의 용이성 측면에서 번들이나 직접적인 프래그먼트 인터페이스 방식보다 우수한 장점을 제공하여, 동일 액티비티 내 프래그먼트 간의 통신을 위한 강력하고 현대적인 솔루션입니다.

---

## 💡 프로 팁: 프래그먼트 결과 API (Fragment Result API)

경우에 따라 프래그먼트는 다른 프래그먼트나 프래그먼트와 호스트 액티비티 간에 **일회성 값**을 전달해야 합니다. 예를 들어, QR 코드 스캔 프래그먼트는 스캔된 데이터를 이전 프래그먼트로 다시 보내야 할 수 있습니다.

프래그먼트 버전 1.3.0 이상에서는 각 `FragmentManager`가 `FragmentResultOwner`를 구현하므로, 프래그먼트가 서로 직접 참조할 필요 없이 결과 리스너를 통해 통신할 수 있습니다. 이는 데이터 전달을 단순화하면서 느슨한 결합(loose coupling)을 유지합니다.

프래그먼트 B(발신자)에서 프래그먼트 A(수신자)로 데이터를 전달하려면 다음 단계를 따르세요.

1.  **프래그먼트 A(결과를 받는 프래그먼트)에 결과 리스너 설정**
2.  **프래그먼트 B에서 동일한 `requestKey`를 사용하여 결과 전송**

**프래그먼트 A에 결과 리스너 설정하기**

프래그먼트 A는 `setFragmentResultListener()`를 사용하여 리스너를 등록해야 하며, 이를 통해 프래그먼트가 `STARTED` 상태가 되었을 때 결과를 수신하도록 보장합니다.

```kotlin
// FragmentA.kt (수신자)
override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    // "requestKey"로 결과 리스너 설정. 콜백은 STARTED 상태일 때 호출됨.
    setFragmentResultListener("requestKey") { requestKey, bundle ->
        val result = bundle.getString("bundleKey")
        // 결과 처리
        Log.d("FragmentA", "Received result for $requestKey: $result")
    }
}
```

**프래그먼트 B에서 결과 전송하기**

프래그먼트 B는 `setFragmentResult()`를 사용하여 결과를 전송하며, 프래그먼트 A가 활성화될 때 데이터를 검색할 수 있도록 보장합니다.

```kotlin
// FragmentB.kt (발신자)
// ... 어떤 작업 후 ...
val result = "여기 결과 데이터"
// "requestKey"를 사용하여 결과 설정. bundleOf는 ktx 헬퍼 함수.
setFragmentResult("requestKey", bundleOf("bundleKey" to result))
parentFragmentManager.popBackStack() // 예시: 이전 프래그먼트로 돌아감
```
`setFragmentResult("requestKey", bundleOf("bundleKey" to result))`는 지정된 키를 사용하여 `FragmentManager`에 결과를 저장합니다. 만약 프래그먼트 A가 활성 상태가 아니라면, 프래그먼트 A가 재개되고 리스너를 등록할 때까지 결과가 저장됩니다.

### 프래그먼트 결과의 동작

* **키당 단일 리스너:** 각 키는 한 번에 하나의 리스너와 하나의 결과만 가질 수 있습니다.
* **보류 중인 결과는 덮어쓰여짐:** 리스너가 활성화되기 전에 여러 결과가 설정되면 최신 결과만 저장됩니다.
* **결과는 소비 후 지워짐:** 프래그먼트가 결과를 수신하고 처리하면 해당 결과는 `FragmentManager`에서 제거됩니다.
* **백 스택의 프래그먼트는 결과를 받지 않음:** 프래그먼트는 백 스택에서 빠져나와(popped) `STARTED` 상태가 되어야 결과를 받을 수 있습니다.
* **`STARTED` 상태의 리스너는 즉시 트리거됨:** 프래그먼트 B가 결과를 설정할 때 프래그먼트 A가 이미 활성 상태이면 리스너가 즉시 호출됩니다.

### 요약

프래그먼트 결과 API는 직접 참조 없이 프래그먼트 간에 일회성 값을 간단하게 전달하는 방법을 제공합니다. `FragmentManager`를 활용하여 결과는 수신 프래그먼트가 활성화될 때까지 안전하게 저장되어, 디커플링되고 라이프사이클을 인식하는 통신 메커니즘을 보장합니다. 이 접근 방식은 QR 코드 스캔, 사용자 입력 다이얼로그 또는 양식 제출과 같은 다양한 시나리오에서 유용하며, 프래그먼트 기반 내비게이션을 더 효율적이고 유지보수하기 쉽게 만듭니다.