# Activity and Fragment
- [Activity 생명주기](#activity-생명주기)

- [onSavedInstanceState onRestoreInstanceState](#onsavedinstancestate-onrestoreinstancestate)

- [프래그먼트 생명주기](#프래그먼트-생명주기)

- [Fragment 사용하는 이유](#fragment-사용하는-이유)

- [언제 Activity가 아닌 Fragment를 사용 하는지?](#언제-activity가-아닌-fragment를-사용-하는지)

- [FragmentPagerAdapter와 FragmentStatePagerAdapter의 차이점](#fragmentpageradapter와-fragmentstatepageradapter의-차이점)

- [Fragment 백스택에서 Add, Replace 차이점](#fragment-백스택에서-add-replace-차이점)

- [기본 생성자만 사용하여 fragment 를 만드는것이 권장되는 이유](#기본-생성자만-사용하여-fragment-를-만드는것이-권장되는-이유)

- [두 fragment 간 데이터 전달 방법](#두-fragment-간-데이터-전달-방법)

- [retained Fragment 란?](#retained-fragment-란)

- [Fragment transaction 을 커밋하는 동안 addToBackStack()의 목적은?](#fragment-transaction-을-커밋하는-동안-addtobackstack의-목적은)

- [액티비티 launchMode 설명](#액티비티-launchmode-설명)

---

## [Activity 생명주기](https://developer.android.com/guide/components/activities/activity-lifecycle?hl=ko)

**순서**
- onCreate : 액티비티가 생성될때 한번 실행
- onStart : 액티비티가 시작될때 호출되는 콜백
- onResume : 액티비티가 재개됨 상태에 들어가면 포그라운드에 표시되고 시스템이 onResume 콜백을 호출
- onPause : 사용자가 액티비티를 떠나는 것을 나타낼때 첫번째 신호로 이 메서드를 호출 (잠시 후 다시 시작할 작업을 일시중지하거나 조정), 배터리 수명에 영향을 미칠 수 있는 모든 리소스를 해제하는 것이 좋음
- onStop : 액티비티가 사용자에게 더 이상 표시되지 않으면 중단됨 상태에 들어가고. 해당 콜백을 호출, 화면 전체를 차지할 경우에 적용
- onDestroy : 액티비티가 소멸되기 전에 호출, finish로 액티비티 종료, 구성 변경(기기 회전, 멀티 윈도우 모드) 로 인해 시스템이 일시적으로 액티비티를 소멸

**화면이 꺼졌다가 다시 들어올때**
- onPause
- onStop
- onRestart
- onStart
- onResume

## onSavedInstanceState onRestoreInstanceState
- onSavedInstanceState() - 액티비티가 일시 중지하기 전에 데이터를 저장하는 데 사용
- onRestoreInstanceState() - 액티비티가 소멸된 후 다시 생성될때 액티비티의 저장된 상태를 복구하는 데 사용, 따라서 onRestoreInstanceState()는 인스턴스 상태 정보가 포함된 번들을 수신

## [프래그먼트 생명주기](https://developer.android.com/guide/components/fragments#java)
- onAttach : 프래그먼트가 액티비티에 추가 되었음을 알기위해 호출, 프래그먼트를 호스팅할 액티비티를 전달
- onCreate : 프래그먼트 인스턴스가 초기화될 때 호출
- onCreateView : 프래그먼트가 처음으로 사용자 인터페이스를 그릴 때 호출, UI를 그리려면 이 메서드에서 프래그먼트 레이아웃의 루트인 View 컴포넌트를 반환해야 한다.
- onActivityCreated : Activity가 onCreate 메서드를 완료할 때 호출
- onStart : 프래그먼트가 표시될 때 호출
- onResume : 프래그먼트가 표시되고 사용자가 프래그먼트와 상호 작용할 수 있을 때 호출, 액티비티가 재개된 후에만 프래그먼트가 재개된다.
- onPause : 프래그먼트가 사용자의 상호작용을 허용하지 않을 때 호출, 다른 프래그먼트와 함께 변경되거나 일시 중지라는 액티비티 또는 프래그먼트의 액티비티에서 제거된다.
- onStop : 프래그먼트가 더 이상 표시되지 않을 때 호출, 다른 프래그먼트와 함께 변경되거나 stop 이라는 액티비티 또는 프래그먼트의 활동에서 제거된다.
- onDestroyView : onCreateView 에서 생성된 뷰와 관련 리소스가 액티비티의 뷰 계층 구조에서 제거되고 파괴딜 때 호출 
- onDestroy : 이 메서드는 프래그먼트가 최종 정리를 수행할 때 호출
- onDetach: 프래그먼트가 액티비티에서 분리될 때 호출

참고: [Fragment 생명주기, Activity 생명주기와의 관계](https://ddangeun.tistory.com/50)

## [Fragment 사용하는 이유](https://stackoverflow.com/a/10515807)
- 재사용 가능한 사용자 인터페이스를 만들기 위한 안드로이드 솔루션
- 액티비티와 layout 을 사용하여 동일한 작업을 수행할 수 있다.
- FragmentManager 를 사용해서 뒤로가기 처리를 할수 있다.(이전 Activity 가 아닌 Fragment 상태로 돌아가는 것을 의미)

## 언제 Activity가 아닌 Fragment를 사용 하는지?
- 다양한 액티비티에서 사용해야 할 UI 컴포넌트가 있는 경우
- ViewPager 처럼 여러 뷰를 나란히 표시할 수 있는 경우

## FragmentPagerAdapter와 FragmentStatePagerAdapter의 차이점
- FragmentPagerAdapter: 사용자가 방문한 각 Fragment는 메모리에 저장되지만 View는 소멸됩니다. 페이지를 다시 방문하면 Fragment의 인스턴스가 아닌 View가 생성됩니다.
- FragmentStatePagerAdapter: 여기서 Fragment 인스턴스는 저장된 Fragment 저장된 상태를 제외하고 사용자에게 표시되지 않을 때 소멸됩니다.

## [Fragment 백스택에서 Add, Replace 차이점](https://stackoverflow.com/a/24466345)
- `replace`는 기존 Fragment 를 제거하고 새로운 Fragment 를 추가
- `add`는 기존 Fragment 를 유지하고 기존 Fragment가 활성화되고 '일시 중지' 상태가 되지 않는다는 것을 의미하는 새로운 Fragment 를 추가하므로 뒤로 버튼으로 누르면 onCreateView()가 새로운 Fragment 가 추가되기 전에 기존 Fragment에서 호출되지 않는다.

## 기본 생성자만 사용하여 fragment 를 만드는것이 권장되는 이유
- 안드로이드 프레임워크에서 Fragment 를 재생성 한다.
- 화면 방향 변경
- 안드로이드에서 Fragment 의 인수가 없는 생성자를 호출
- 우리가 만든 생성자가 무엇인지 모른다.

### Fragment 의 새로운 인스턴스를 생성하는 권장 방법
- 안드로이드 프레임워크는 번들을 추출하고 저장할 수 있다.
- 방향이 변경된 경우 안드로이드  프레임워크는 인수가 없는 생성자를 사용해서 새로운 fragment를 다시 만들고 번들을 저장한 대로 fragment에 첨부할 수 있다. 
- 이후에 이와같은 게이트 인수를 사용하여 onCreate 메소드에서 해당 데이터에 액세스 할 수 있으므로 시스템이 fragment 를 복원할때 자동으로 번들을 복원하고 fragment가 초기화된 상태와 동일한 상태로 fragment를 복원할 수 있다. 

## 두 fragment 간 데이터 전달 방법
### Shared ViewModel 사용
- fragment 간에 데이터를 공유하려면 모든 fragment 간에 공유되는 공유 ViewModel 을 사용
- Shared ViewModel 을 만들고 두 fragment 간에 공유되는 ViewModel 내의 데이터를 업데이트하고 다른 fragment 는 해당 데이터의 변경 사항을 관찰

**구현코드**
```kotlin
class SharedViewModel : ViewModel() {
    val message = MutableLiveData<String>()

    fun sendMessage(text: String) {
        message.value = text
    }
}
```

```kotlin
class MessageReceiverFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_receiver, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val model = ViewModelProvider(requireActivity()).get(SharedViewModel::class.java)
        model.message.observe(viewLifecycleOwner, Observer {
            textViewReceiver.text = it
        })
    }
}
```
- 이 Fragment 는 MessageSenderFragment 에서 보내는 메시지를 수신

```kotlin
class MessageSenderFragment : Fragment() {
    lateinit var model: SharedViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_sender, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        model = ViewModelProvider(requireActivity()).get(SharedViewModel::class.java)
        button.setOnClickListener { model.sendMessage("MindOrks") }
    }
}
```
- 이 Fragment 는 MessageReceiveFragment 에서 수신할 메시지를 보낼 것이다.

<br>

**동일한 액티비티가 두 fragment의 호스트다.**

**두 fragment 에서 소유자와 동일한 단일 Activity를 사용하는 것과 동일한 객체인 SharedViewModel 의 객체를 생성했기 때문에 공유가 되는 것이다.**

### Interface 를 사용
- 인터페이스를 사용하여 프래그먼트간에 통신

하나의 액티비티가 있고 해당 액티비티에서 두 개의 fragment 가 있다.
목표는 Interface 의 도움을 받아 한 fragment 에서 다른 fragment 로 데이터를 보내는 것이다.

1. FragmentA 에서 interface 를 만들기
2. Activity 에서 FragmentA 인터페이스 구현
3. Activity 에서 Interface 함수 호출
4. Activity에서 FragmentB 를 호출하여 필요한 변경작업을 수행

**구현코드**
```kotlin
class FragmentA : Fragment() {

        lateinit var mCallback: TextClickedListener

        //defining Interface
        interface TextClickedListener {
            fun sendText(text: String)
        }

        fun setOnTextClickedListener(callback: TextClickedListener) {
            this.mCallback = callback
        }

        fun yourMethodofSendingText() {
            //here you can get the text from the edit text or can use this method according to your need
            mCallback.sendText("YOUR TEXT")
        }

    }
}
```

```kotlin
class MainActivity :Activity(), FragmentA.TextClicked {
    fun onAttachFragment(fragment: Fragment) {
        if (fragment is FragmentA) {
            fragment.setOnTextClickedListener(this)
        }
    }
    override fun sendText(text: String) {
        // Get FragmentB
        val callingFragment = getSupportFragmentManager().findFragmentById(R.id.fragment_b) as FragmentB
        //calling the updateText method of the FragmentB
        callingFragment.updateText(text)
    }
}
```

```kotlin
class FragmentB : Fragment() {

    fun updateText(text: String) {
        // Here you can perform the required action
        //this  action can be updating the text of the TextView with the "text" string
        //or any relevant action
     }
}
```

### 참고
- [Shared ViewModel](https://blog.mindorks.com/shared-viewmodel-in-android-shared-between-fragments)
- [How to communicate between fragments?](https://blog.mindorks.com/how-to-communicate-between-fragments)

## retained Fragment 란?
- 기본적으로 프래그먼트는 구성 변경이 발생하면 부모 액티비티와 함께 ​​소멸되고 다시 생성됩니다. 
- setRetainInstance(true)를 호출하면 파괴 및 재생성 주기를 우회하여 액티비티가 다시 생성될 때 프래그먼트의 현재 인스턴스를 유지하도록 시스템에 신호를 보낼 수 있습니다.

## [Fragment transaction 을 커밋하는 동안 addToBackStack()의 목적은?](https://stackoverflow.com/a/22985052)
- addToBackStack()을 호출하면 replace transactrion 이 백스택에 저장되므로 사용자는 뒤로 버튼을 눌러 트랜잭션을 되돌리고 이전 fragment 를 다시 가져올 수 있다.

## [액티비티 launchMode 설명](https://blog.mindorks.com/android-activity-launchmode-explained-cbc6cf996802)
