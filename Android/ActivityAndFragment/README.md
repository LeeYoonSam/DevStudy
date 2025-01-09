# Activity and Fragment
- [안드로이드 액티비티: 앱 화면의 기본 단위](#안드로이드-액티비티-앱-화면의-기본-단위)
- [Fragment:앱 UI의 모듈화된 구성 요소](#fragment앱-ui의-모듈화된-구성-요소)
- [## FragmentFactory와 ViewModel, 그리고 특수한 상황에서의 생성자 사용](#fragmentfactory와-viewmodel-그리고-특수한-상황에서의-생성자-사용)
- [FragmentPagerAdapter와 FragmentStatePagerAdapter의 차이점](#fragmentpageradapter와-fragmentstatepageradapter의-차이점)
- [Fragment 백스택에서 Add, Replace 차이점](#fragment-백스택에서-add-replace-차이점)
- [왜 프래그먼트는 기본 생성자만 사용해야 할까요?](#왜-프래그먼트는-기본-생성자만-사용해야-할까요)
- [retained Fragment 란?](#retained-fragment-란)
- [Fragment transaction 을 커밋하는 동안 addToBackStack()의 목적은?](#fragment-transaction-을-커밋하는-동안-addtobackstack의-목적은)
- [액티비티 launchMode 설명](#액티비티-launchmode-설명)
- [by viewModels 가 해주는 일](#by-viewmodels-가-해주는-일)

---

## 안드로이드 액티비티: 앱 화면의 기본 단위
**액티비티(Activity)** 는 안드로이드 앱에서 사용자가 상호작용하는 하나의 화면을 나타내는 기본 구성 요소입니다. 앱을 실행하면 처음 보이는 화면부터 각 기능을 수행하는 화면까지 모두 액티비티로 구성됩니다.

### 액티비티의 역할
- 사용자 인터페이스 제공: 사용자가 앱과 상호작용할 수 있는 화면을 제공합니다.
- 화면 전환 관리: 다른 액티비티로 이동하거나 현재 액티비티로 되돌아오는 등의 화면 전환을 관리합니다.
- 앱의 실행 단위: 앱의 각 기능을 수행하는 독립적인 단위로 작동합니다.

### 액티비티의 구성 요소
- 레이아웃(Layout): XML 파일로 정의되며, 액티비티의 UI 구성을 담당합니다. 버튼, 텍스트 뷰 등 다양한 UI 요소를 배치하고 스타일을 지정할 수 있습니다.
- Java/Kotlin 클래스: 액티비티의 동작을 정의하는 클래스입니다. 사용자의 입력을 처리하고, 다른 액티비티를 호출하는 등의 로직을 구현합니다.

### 액티비티 생명 주기
액티비티는 생성, 시작, 중지, 파괴 등 다양한 상태를 거치며, 이러한 상태 변화를 생명 주기라고 합니다. 액티비티의 생명 주기 메서드를 오버라이드하여 각 상태에서 원하는 동작을 구현할 수 있습니다.

- onCreate(): 액티비티가 처음 생성될 때 호출되는 메서드입니다. UI 초기화, 데이터 로딩 등을 수행합니다.
- onStart(): 액티비티가 사용자에게 보여지기 직전에 호출됩니다.
- onResume(): 액티비티가 포어그라운드로 전환되어 사용자와 상호작용할 준비가 완료되었을 때 호출됩니다.
- onPause(): 다른 액티비티로 포커스가 이동할 때 호출됩니다.
- onStop(): 액티비티가 더 이상 사용자에게 보이지 않을 때 호출됩니다.
- onDestroy(): 액티비티가 완전히 파괴되기 직전에 호출됩니다.

### 액티비티 간 이동
- startActivity(): 새로운 액티비티를 시작합니다.
- startActivityForResult(): 다른 액티비티로부터 결과를 받기 위해 사용합니다.
- Intent: 액티비티 간에 데이터를 전달하는 데 사용됩니다.

### 액티비티와 프래그먼트의 차이점
- 액티비티: 앱의 화면을 나타내는 기본 단위이며, 독립적인 생명 주기를 가집니다.
- 프래그먼트: 액티비티 내에서 UI의 일부를 구성하는 모듈입니다. 액티비티의 생명 주기를 따르며, 더 유연한 UI 구성을 가능하게 합니다.

### 액티비티 사용 시 주의 사항
- 메모리 관리: 액티비티가 많아지면 메모리 부족 현상이 발생할 수 있으므로 필요한 액티비티만 유지해야 합니다.
- 생명 주기 관리: 액티비티의 생명 주기를 정확히 이해하고, 각 상태에서 적절한 작업을 수행해야 합니다.
- 백 스택: 사용자가 뒤로 가기 버튼을 누를 때 이전 액티비티로 돌아가기 위해 백 스택을 효율적으로 관리해야 합니다.

### 결론
액티비티는 안드로이드 앱 개발에서 가장 기본적인 구성 요소입니다. 액티비티에 대한 이해는 안드로이드 앱 개발의 기반이 되므로 꼼꼼히 학습하는 것이 중요합니다.

---

## Fragment:앱 UI의 모듈화된 구성 요소
Fragment는 안드로이드 앱에서 UI를 구성하는 모듈화된 단위입니다. 하나의 액티비티 안에서 여러 개의 프래그먼트를 사용하여 다양한 화면 구성을 만들고, 각 프래그먼트를 독립적으로 관리할 수 있습니다. 마치 레고 블록처럼 UI를 조립하는 것과 같다고 생각하면 됩니다.

### 왜 Fragment를 사용해야 할까요?
- 재사용성: 한 번 작성한 프래그먼트를 여러 액티비티에서 재사용할 수 있습니다.
- 유연성: 다양한 화면 크기와 방향에 맞춰 UI를 동적으로 조절할 수 있습니다.
- 백 스택 관리: FragmentTransaction을 통해 백 스택을 관리하여 사용자가 뒤로 가기 버튼을 누를 때 이전 화면으로 자연스럽게 이동할 수 있습니다.
- 테스트 용이성: 각 프래그먼트를 독립적으로 테스트할 수 있어 앱의 안정성을 높일 수 있습니다.

### Fragment의 주요 특징
- 자체 수명 주기: Fragment는 Activity의 수명 주기를 따르지만, 자체적인 수명 주기 메서드를 가지고 있어 독립적인 관리가 가능합니다.
- 레이아웃: 각 Fragment는 자체 레이아웃을 가지며, 이 레이아웃은 Activity의 레이아웃에 포함됩니다.
- 동적 추가/삭제: Fragment는 런타임 중에 추가, 삭제, 교체될 수 있습니다.
- 백 스택: FragmentTransaction을 통해 백 스택에 추가되어 사용자가 뒤로 가기 버튼을 누를 때 이전 상태로 복원될 수 있습니다.

### Fragment의 사용 예시
- 탭 레이아웃: 각 탭에 다른 Fragment를 할당하여 탭 내용을 변경합니다.
- 네비게이션 드로어: 측면 메뉴를 통해 다양한 화면을 보여줄 때 각 화면을 Fragment로 구현합니다.
- 마스터-디테일 화면: 마스터 목록과 디테일 화면을 각각 Fragment로 구현하여 유연하게 화면을 구성합니다.
- 다양한 화면 크기 지원: 태블릿과 같은 큰 화면에서는 두 개 이상의 Fragment를 나란히 배치하여 더 많은 정보를 보여줄 수 있습니다.

### Fragment의 생명 주기
Fragment의 생명 주기는 Activity의 생명 주기와 밀접한 관련이 있습니다. 주요 생명 주기 메서드는 다음과 같습니다.

- onAttach(): Fragment가 Activity에 연결될 때 호출됩니다.
- onCreate(): Fragment가 생성될 때 호출됩니다.
- onCreateView(): Fragment의 UI를 생성할 때 호출됩니다.
- onActivityCreated(): Activity의 onCreate()가 호출된 후에 호출됩니다.
- onStart(): Fragment가 사용자에게 보여지기 직전에 호출됩니다.
- onResume(): Fragment가 사용자와 상호 작용할 준비가 완료되었을 때 호출됩니다.
- onPause(): 다른 Activity로 포커스가 이동할 때 호출됩니다.
- onStop(): Fragment가 더 이상 사용자에게 보이지 않을 때 호출됩니다.
- onDestroyView(): Fragment의 UI가 제거될 때 호출됩니다.
- onDestroy(): Fragment가 완전히 파괴되기 직전에 호출됩니다.
- onDetach(): Fragment가 Activity에서 분리될 때 호출됩니다.

### FragmentTransaction을 이용한 Fragment 관리
FragmentTransaction을 사용하여 Fragment를 추가, 제거, 교체하는 등 다양한 작업을 수행할 수 있습니다.

- add(): Fragment를 Container에 추가합니다.
- replace(): 기존 Fragment를 새로운 Fragment로 교체합니다.
- remove(): Fragment를 Container에서 제거합니다.
- addToBackStack(): 백 스택에 Fragment를 추가하여 뒤로 가기 버튼을 누르면 이전 Fragment로 돌아갈 수 있도록 합니다.

### 결론
Fragment는 안드로이드 앱 개발에서 매우 유용한 도구입니다. 복잡한 UI를 모듈화하여 관리하고, 다양한 화면 크기와 방향에 맞춰 유연하게 대응할 수 있도록 도와줍니다. Navigation Component와 함께 사용하면 더욱 강력한 앱을 개발할 수 있습니다.

---

## FragmentPagerAdapter와 FragmentStatePagerAdapter의 차이점
- FragmentPagerAdapter: 사용자가 방문한 각 Fragment는 메모리에 저장되지만 View는 소멸됩니다. 페이지를 다시 방문하면 Fragment의 인스턴스가 아닌 View가 생성됩니다.
- FragmentStatePagerAdapter: 여기서 Fragment 인스턴스는 저장된 Fragment 저장된 상태를 제외하고 사용자에게 표시되지 않을 때 소멸됩니다.

---

## [Fragment 백스택에서 Add, Replace 차이점](https://stackoverflow.com/a/24466345)
- `replace`는 기존 Fragment 를 제거하고 새로운 Fragment 를 추가
- `add`는 기존 Fragment 를 유지하고 기존 Fragment가 활성화되고 '일시 중지' 상태가 되지 않는다는 것을 의미하는 새로운 Fragment 를 추가하므로 뒤로 버튼으로 누르면 onCreateView()가 새로운 Fragment 가 추가되기 전에 기존 Fragment에서 호출되지 않는다.

---

## 왜 프래그먼트는 기본 생성자만 사용해야 할까요?
안드로이드 개발에서 프래그먼트를 생성할 때 기본 생성자만 사용하는 것을 권장하는 이유는 프래그먼트의 생명 주기와 밀접한 관련이 있습니다.

### 프래그먼트의 생명 주기와 기본 생성자
- 프래그먼트의 재생성: 안드로이드 시스템은 화면 회전, 메모리 부족 등 다양한 상황에서 프래그먼트를 파괴하고 다시 생성할 수 있습니다. 이때 시스템은 프래그먼트의 기본 생성자를 호출하여 새로운 인스턴스를 생성합니다.
- 인자 전달: 만약 프래그먼트에 인자를 전달하는 생성자를 사용한다면, 시스템이 프래그먼트를 재생성할 때 이 인자를 어떻게 전달해야 할지 알 수 없어 예상치 못한 오류가 발생할 수 있습니다.

### 기본 생성자를 사용해야 하는 이유
- 안정적인 프래그먼트 관리: 시스템이 프래그먼트를 임의로 재생성하더라도 항상 일관된 상태로 복원될 수 있도록 합니다.
- 예측 가능한 동작: 프래그먼트의 생성 과정이 명확해져 코드의 예측 가능성을 높입니다.
- 데이터 전달: 프래그먼트에 데이터를 전달해야 할 경우, setArguments() 메서드를 사용하여 Bundle 객체를 통해 전달합니다. 이렇게 하면 시스템이 프래그먼트를 재생성할 때도 Bundle에 저장된 데이터를 복원할 수 있습니다.

### 예시
```kotlin
class MyFragment : Fragment() {
    companion object {
        fun newInstance(data: String): MyFragment {
            val args = Bundle()
            args.putString("data", data)
            val fragment = MyFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // 여기서 arguments를 통해 전달받은 데이터를 사용하여 UI를 구성
        val data = arguments?.getString("data")
        // ...
    }
}
```
위 예시처럼 newInstance() 메서드를 통해 Bundle 객체에 데이터를 담아 전달하고, onCreateView()에서 이 데이터를 사용합니다.

### 정리
프래그먼트는 시스템에 의해 임의로 재생성될 수 있기 때문에, 예측 가능하고 안정적인 동작을 위해 기본 생성자를 사용하는 것이 좋습니다. setArguments() 메서드를 활용하여 필요한 데이터를 전달하고, onCreateView()에서 이 데이터를 사용하여 UI를 구성하는 것이 일반적인 방법입니다.

---

## FragmentFactory와 ViewModel, 그리고 특수한 상황에서의 생성자 사용

### 1. FragmentFactory: 프래그먼트 생성 과정의 커스터마이징
FragmentFactory는 AndroidX에서 제공하는 인터페이스로, 프래그먼트를 생성하는 과정을 커스터마이징할 수 있도록 해줍니다. 기본적으로 시스템은 프래그먼트의 기본 생성자를 호출하여 인스턴스를 생성하지만, FragmentFactory를 사용하면 생성 과정에 개입하여 더욱 복잡한 의존성 주입이나 생성 로직을 구현할 수 있습니다.

### 왜 FragmentFactory가 필요할까요?

- 의존성 주입: Dagger, Hilt 등의 의존성 주입 라이브러리와 함께 사용하여 프래그먼트에 의존성을 주입할 수 있습니다.
- 복잡한 생성 로직: 프래그먼트 생성 시 필요한 추가적인 로직을 구현할 수 있습니다.
- 테스트 용이성: 테스트를 위한 모킹 프래그먼트를 생성할 수 있습니다.

### FragmentFactory 사용 예시:

```kotlin
class MyFragmentFactory : FragmentFactory() {
    override fun instantiate(classLoader: ClassLoader, className: String): Fragment {
        val fragment = super.instantiate(classLoader, className)
        // 의존성 주입 등 추가적인 로직 수행
        return fragment
    }
}
```

### FragmentFactory 설정 방법:

```kotlin
supportFragmentManager.fragmentFactory = MyFragmentFactory()
```

### 2. ViewModel과 Fragment의 관계
ViewModel은 UI와 데이터를 분리하여 관리하는 디자인 패턴인 MVVM에서 사용되는 클래스입니다. 액티비티 또는 프래그먼트의 생명 주기보다 오래 유지되어 데이터를 유지하고, UI에 제공합니다.

- 데이터 공유: ViewModel은 여러 프래그먼트에서 공유되는 데이터를 저장하고 관리할 수 있습니다.
- 생명 주기 관리: ViewModel은 Configuration 변경(예: 화면 회전)에도 살아남아 데이터를 유지합니다.
- 테스트 용이성: UI와 로직을 분리하여 테스트하기 쉽습니다.

### ViewModel과 Fragment의 관계:
- ViewModel은 프래그먼트에서 참조되어 데이터를 제공하고 업데이트합니다.
- 프래그먼트는 ViewModel의 데이터를 관찰하여 UI를 업데이트합니다.
- ViewModel은 LiveData를 사용하여 데이터 변경을 알리고, 프래그먼트는 LiveData를 관찰합니다.

### 3. 특수한 상황에서 기본 생성자를 사용하지 않아도 되는 경우
일반적으로 프래그먼트는 기본 생성자를 사용하는 것이 좋지만, 다음과 같은 특수한 경우에는 예외적으로 다른 생성자를 사용할 수 있습니다:

- 단위 테스트: 테스트를 위해 특정 값으로 초기화된 프래그먼트를 생성해야 할 경우, 생성자에 인자를 전달하여 테스트를 간편하게 만들 수 있습니다.
- 라이브러리 내부 구현: 일부 라이브러리에서는 프래그먼트 생성 시 추가적인 인자를 요구할 수 있습니다.

주의: 위와 같은 경우에도 시스템이 프래그먼트를 재생성할 때는 항상 기본 생성자를 호출한다는 점을 명심해야 합니다.

### 결론
FragmentFactory는 프래그먼트 생성 과정을 커스터마이징하고, ViewModel은 UI와 데이터를 분리하여 관리하는 데 사용됩니다. 일반적으로 프래그먼트는 기본 생성자를 사용하는 것이 좋지만, 특정 상황에서는 예외적으로 다른 생성자를 사용할 수 있습니다.

---

## retained Fragment 란?
- 기본적으로 프래그먼트는 구성 변경이 발생하면 부모 액티비티와 함께 ​​소멸되고 다시 생성됩니다. 
- setRetainInstance(true)를 호출하면 파괴 및 재생성 주기를 우회하여 액티비티가 다시 생성될 때 프래그먼트의 현재 인스턴스를 유지하도록 시스템에 신호를 보낼 수 있습니다.

## [Fragment transaction 을 커밋하는 동안 addToBackStack()의 목적은?](https://stackoverflow.com/a/22985052)
- addToBackStack()을 호출하면 replace transactrion 이 백스택에 저장되므로 사용자는 뒤로 버튼을 눌러 트랜잭션을 되돌리고 이전 fragment 를 다시 가져올 수 있다.

## [액티비티 launchMode 설명](https://blog.mindorks.com/android-activity-launchmode-explained-cbc6cf996802)

## by viewModels 가 해주는 일
기본적으로 범위가 지정된 ViewModel에 액세스하기 위한 속성 대리자(delegate)를 반환합니다.

```kotlin
package androidx.fragment.app

@MainThread
inline fun <reified VM : ViewModel> Fragment.viewModels(
    noinline ownerProducer: () -> ViewModelStoreOwner = { this },
    noinline factoryProducer: (() -> Factory)? = null
) = createViewModelLazy(VM::class, { ownerProducer().viewModelStore }, factoryProducer)
```

이 속성은 이 Fragment가 연결된 후에만 액세스할 수 있습니다. 즉, Fragment.onAttach() 이후에 액세스할 수 있으며 그 이전에 액세스하면 IllegalArgumentException이 발생합니다.

```kotlin
@MainThread
fun <VM : ViewModel> Fragment.createViewModelLazy(
    viewModelClass: KClass<VM>,
    storeProducer: () -> ViewModelStore,
    factoryProducer: (() -> Factory)? = null
): Lazy<VM> {
    val factoryPromise = factoryProducer ?: {
        defaultViewModelProviderFactory
    }
    return ViewModelLazy(viewModelClass, storeProducer, factoryPromise)
}
```

factoryProducer 가 null 이면 DefaultViewModelProviderFactory 를 사용

### ViewModelProvider.Factory getDefaultViewModelProviderFactory()
ViewModelProvider 생성자에 사용자 지정 팩토리가 제공되지 않을 때 사용해야 하는 기본 ViewModelProvider.Factory를 반환합니다.

이것이 처음 호출될 때 Fragment의 인수는 이 팩토리를 사용하여 생성된 뷰 모델에 전달된 모든 androidx.lifecycle.SavedStateHandle의 기본값으로 사용됩니다.

```kotlin
package androidx.fragment.app;

@NonNull
    @Override
    public ViewModelProvider.Factory getDefaultViewModelProviderFactory() {
        if (mFragmentManager == null) {
            throw new IllegalStateException("Can't access ViewModels from detached fragment");
        }
        if (mDefaultFactory == null) {
            Application application = null;
            Context appContext = requireContext().getApplicationContext();
            while (appContext instanceof ContextWrapper) {
                if (appContext instanceof Application) {
                    application = (Application) appContext;
                    break;
                }
                appContext = ((ContextWrapper) appContext).getBaseContext();
            }
            if (application == null && FragmentManager.isLoggingEnabled(Log.DEBUG)) {
                Log.d(FragmentManager.TAG, "Could not find Application instance from "
                        + "Context " + requireContext().getApplicationContext() + ", you will "
                        + "not be able to use AndroidViewModel with the default "
                        + "ViewModelProvider.Factory");
            }
            mDefaultFactory = new SavedStateViewModelFactory(
                    application,
                    this,
                    getArguments());
        }
        return mDefaultFactory;
    }
```


### ViewModelLazy
androidx.fragment.app.Fragment.viewModels 및 androidx.activity.ComponentActivity.viewmodels에서 사용하는 Lazy 구현입니다.

`storeProducer`는 초기화 중에 호출되는 람다이며 VM은 반환된 ViewModelStore 범위에서 생성됩니다.

`factoryProducer`는 초기화 중에 호출되는 람다이며 반환된 ViewModelProvider.Factory는 VM 생성에 사용됩니다.

```kotlin
package androidx.lifecycle

@MainThread
public inline fun <reified VM : ViewModel> ViewModelProvider.get(): VM = get(VM::class.java)

public class ViewModelLazy<VM : ViewModel> (
    private val viewModelClass: KClass<VM>,
    private val storeProducer: () -> ViewModelStore,
    private val factoryProducer: () -> ViewModelProvider.Factory
) : Lazy<VM> {
    private var cached: VM? = null

    override val value: VM
        get() {
            val viewModel = cached
            return if (viewModel == null) {
                val factory = factoryProducer()
                val store = storeProducer()
                ViewModelProvider(store, factory).get(viewModelClass.java).also {
                    cached = it
                }
            } else {
                viewModel
            }
        }

    override fun isInitialized(): Boolean = cached != null
}
```

주어진 팩토리를 통해 ViewModels를 생성하고 그것을 주어진 스토어에 유지하는 ViewModelProvider를 생성합니다.
매개변수:
`store` – ViewModel이 저장될 ViewModelStore.
`factory` – 새 ViewModel을 인스턴스화하는 데 사용되는 팩토리

```kotlin
public ViewModelProvider(@NonNull ViewModelStore store, @NonNull Factory factory) {
    mFactory = factory;
    mViewModelStore = store;
}
```


기존 ViewModel을 반환하거나 이 ViewModelProvider와 연결된 범위(일반적으로 Fragment 또는 Activity)에 새 ViewModel을 만듭니다.

생성된 ViewModel은 주어진 범위와 연결되며 범위가 살아있는 한 유지됩니다(예: Activity인 경우 완료되거나 프로세스가 종료될 때까지).

매개변수:
`key` – ViewModel을 식별하는 데 사용할 키입니다.
`modelClass` – 존재하지 않는 경우 인스턴스를 생성하기 위한 ViewModel의 클래스입니다.

유형 매개변수:
`<T>` – ViewModel의 유형 매개변수입니다.

보고:
지정된 유형 T의 인스턴스인 ViewModel입니다.

```kotlin
@NonNull
@MainThread
public <T extends ViewModel> T get(@NonNull String key, @NonNull Class<T> modelClass) {
    ViewModel viewModel = mViewModelStore.get(key);

    if (modelClass.isInstance(viewModel)) {
        if (mFactory instanceof OnRequeryFactory) {
            ((OnRequeryFactory) mFactory).onRequery(viewModel);
        }
        return (T) viewModel;
    } else {
        //noinspection StatementWithEmptyBody
        if (viewModel != null) {
            // TODO: log a warning.
        }
    }
    if (mFactory instanceof KeyedFactory) {
        viewModel = ((KeyedFactory) mFactory).create(key, modelClass);
    } else {
        viewModel = mFactory.create(modelClass);
    }
    mViewModelStore.put(key, viewModel);
    return (T) viewModel;
}
```