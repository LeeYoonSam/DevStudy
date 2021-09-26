# Android

### Core Android
- [안드로이드 어플리케이션 컴포넌트](#안드로이드-어플리케이션-컴포넌트)

- [매니페스트 파일](#매니페스트-파일)

- [안드로이드 어플리케이션의 프로젝트 구조](#안드로이드-어플리케이션의-프로젝트-구조)

- [Android Context](#android-context)

### Activity and Fragment
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

### Views and ViewGroups
- [안드로이드에서 View 란?](#안드로이드에서-view-란)
- [Custom View 만들기](#custom-view-만들기)
- [ViewGroups 이란 무엇이며 View와 어떻게 다른지?](#viewgroups-이란-무엇이며-view와-어떻게-다른지)
- [ConstraintLayout 이란?](#constraintlayout-이란)
- [ViewTreeObserver가 무엇인지?](#viewtreeobserver가-무엇인지)

### Displaying Lists of Content
- [ListView 와 RecyclerView 의 차이점](#listview-와-recyclerview-의-차이점)

### Useful Kotlin Extension
- [유용하게 사용하는 Extension](#유용하게-사용하는-extension)

---
# Core Android
## [안드로이드 어플리케이션 컴포넌트](https://developer.android.com/guide/components/fundamentals.html#Components)

### Activity
- 화면 하나를 표현하며, 사용자와 상호작용하기 위한 진입점
- 사용자가 현재 관심을 가지고 있는 사항(화면에 표시된 것)을 추적하여 액티비티를 호스팅하는 프로세스를 시스템에서 계속 실행하도록 합니다.
- 이전에 사용한 프로세스에 사용자가 다시 찾을 만한 액티비티(중단된 액티비티)가 있다는 것을 알고, 해당 프로세스를 유지하는 데 더 높은 우선순위를 부여합니다.
- 앱이 프로세스를 종료하도록 도와서 이전 상태가 복원되는 동시에 사용자가 액티비티로 돌아갈 수 있게 합니다.
- 앱이 서로 사용자 플로우를 구현하고 시스템이 이러한 플로우를 조정하기 위한 수단을 제공합니다.

### Service
- 서비스는 여러 가지 이유로 백그라운드에서 앱을 계속 실행하기 위한 다목적 진입점입니다. 
- 백그라운드에서 실행되는 구성 요소로, 오랫동안 실행되는 작업을 수행하거나 원격 프로세스를 위한 작업을 수행
- 서비스는 사용자 인터페이스를 제공하지 않습니다.
- 음악 재생은 사용자가 바로 인식할 수 있는 작업입니다. 따라서 앱은 사용자에게 이와 관련된 알림을 보내고 음악 재생을 포그라운드로 옮기라고 시스템에 지시합니다. 이 경우, 시스템은 이 서비스의 프로세스가 계속 실행되도록 많은 노력을 기울여야 합니다. 이 서비스가 사라지면 사용자가 불만을 느낄 것이기 때문입니다.
- 정기적인 백그라운드 서비스는 사용자가 실행되고 있다고 직접 인식할 수 없는 작업이므로 시스템은 좀 더 자유롭게 프로세스를 관리할 수 있습니다. 사용자와 좀 더 직접적인 관련이 있는 작업에 RAM이 필요할 경우 이 서비스를 종료할 수도 있습니다(그런 다음, 나중에 서비스를 다시 시작할 수도 있습니다).

### Broadcast Receiver
- Broadcast Receiver는 시스템이 정기적인 사용자 플로우 밖에서 이벤트를 앱에 전달하도록 지원하는 구성 요소
- 앱이 시스템 전체의 브로드캐스트 알림에 응답할 수 있게 합니다
- 예를 들어 앱이 사용자에게 예정된 이벤트에 대해 알리는 알림을 게시하기 위한 알람을 예약할 경우, 그 알람을 앱의 Broadcast Receiver에 전달하면 알람이 울릴 때까지 앱을 실행하고 있을 필요가 없습니다
- 화면이 꺼졌거나 배터리가 부족하거나 사진을 캡처했다고 알리는 브로드캐스트
- Broadcast Receiver는 사용자 인터페이스를 표시하지 않지만, 상태 표시줄 알림을 생성하여 사용자에게 브로드캐스트 이벤트가 발생했다고 알릴 수 있습니다.
- JobService를 예약하여 시작하여 JobScheduler가 포함된 이벤트를 기초로 어떤 작업을 수행하게 할 수 있습니다.

### Content Provider
- 콘텐츠 제공자는 파일 시스템, SQLite 데이터베이스, 웹상이나 앱이 액세스할 수 있는 다른 모든 영구 저장 위치에 저장 가능한 앱 데이터의 공유형 집합을 관리
- 다른 앱은 콘텐츠 제공자를 통해 해당 데이터를 쿼리하거나, 콘텐츠 제공자가 허용할 경우에는 수정도 가능
- 시스템의 경우 콘텐츠 제공자는 URI 구성표로 식별되고 이름이 지정된 데이터 항목을 게시할 목적으로 앱에 진입하기 위한 입구
- URI를 할당하더라도 앱을 계속 실행할 필요가 없으므로 URI를 소유한 앱이 종료된 후에도 URI를 유지할 수 있습니다. 시스템은 URI를 소유한 앱이 해당 URI에서 앱의 데이터를 검색할 때만 실행되도록 하면 됩니다.
- 이 URI는 중요하고 조밀한 보안 모델을 제공합니다. 예를 들어 앱은 클립보드에 있는 이미지에 URI를 할당하고 콘텐츠 제공자가 검색하도록 하여, 다른 앱이 자유롭게 이미지에 액세스하지 못하게 막을 수 있습니다. 두 번째 앱이 클립보드에서 해당 URI에 액세스하려고 시도하면 시스템에서는 임시 URI 권한을 부여하여 그 앱이 데이터에 액세스하도록 허용할 수 있습니다. 따라서 두 번째 앱에서는 URI 뒤에 있는 데이터 외에 다른 것에는 액세스할 수 없습니다.
- 콘텐츠 제공자는 앱 전용이어서 공유되지 않는 데이터를 읽고 쓰는 데도 유용합니다.
 
### 구성 요소 활성화
구성 요소 유형 네 가지 중 세 가지(액티비티, 서비스, Broadcast Receiver)는 인텐트라는 비동기식 메시지로 활성화됩니다. 인텐트는 런타임에서 각 구성 요소를 서로 바인딩합니다. 이것은 일종의 메신저라고 생각하면 됩니다. 즉 구성 요소가 어느 앱에 속하든 관계없이 다른 구성 요소로부터 작업을 요청하는 역할을 합니다.
인텐트는 Intent 객체로 생성되며, 이것이 특정 구성 요소(명시적 인텐트)를 활성화할지 아니면 구성 요소의 특정 유형(암시적 인텐트)을 활성화할지 나타내는 메시지를 정의합니다.

각 유형의 구성 요소를 활성화하는 데는 각기 별도의 메서드가 있습니다.

- 액티비티를 시작하거나 새로운 작업을 배정하려면 Intent를 startActivity() 또는 startActivityForResult()에 전달하면 됩니다(액티비티가 결과를 반환하기를 원하는 경우).
- Android 5.0(API 레벨 21) 이상에서는 JobScheduler 클래스를 사용하여 작업을 예약할 수 있습니다. 초기 Android 버전의 경우 Intent를 startService()에 전달하여 서비스를 시작할 수 있습니다(또는 진행 중인 서비스에 새로운 지침을 전달할 수 있습니다). Intent를 bindService()에 전달하여 서비스에 바인딩할 수도 있습니다.
- sendBroadcast(), sendOrderedBroadcast(), 또는 sendStickyBroadcast()와 같은 메서드에 Intent를 전달하면 브로드캐스트를 시작할 수 있습니다.
- 콘텐츠 제공자에 쿼리를 수행하려면 ContentResolver에서 query()를 호출하면 됩니다.

## [매니페스트 파일](https://developer.android.com/guide/components/fundamentals.html#Manifest)

모든 앱 프로젝트는 프로젝트 소스 세트의 루트에 AndroidManifest.xml 파일(정확히 이 이름)이 있어야 합니다. 매니페스트 파일은 Android 빌드 도구, Android 운영체제 및 Google Play에 앱에 관한 필수 정보를 설명합니다.

매니페스트 파일은 다른 여러 가지도 설명하지만 특히 다음과 같은 내용을 선언해야 합니다.

- 앱의 패키지 이름(일반적으로 코드의 네임스페이스와 일치). Android 빌드 도구는 프로젝트를 빌드할 때 이 이름으로 코드 엔터티의 위치를 확인합니다. 앱을 패키징할 때 빌드 도구가 이 값을 Gradle 빌드 파일의 애플리케이션 ID로 대체합니다. 이는 시스템과 Google Play에서 고유한 앱 식별자로 사용됩니다. 패키지 이름과 앱 ID에 대해 자세히 알아보세요.
- 앱의 구성 요소(모든 액티비티, 서비스, Broadcast Receiver, 콘텐츠 제공자 포함). 각 구성 요소는 Kotlin이나 Java 클래스의 이름과 같은 기본 속성을 정의해야 합니다. 또한 자신이 처리할 수 있는 기기 구성의 종류, 그리고 구성 요소가 어떻게 시작되는지 설명하는 인텐트 필터와 같은 기능을 선언할 수도 있습니다. 앱 구성 요소에 대해 자세히 알아보세요.
- 앱이 시스템 또는 다른 앱의 보호된 부분에 액세스하기 위해 필요한 권한. 이것은 다른 앱이 이 앱의 콘텐츠에 액세스하고자 하는 경우 반드시 있어야 하는 모든 권한도 선언합니다. 권한에 대해 자세히 알아보세요.
- 앱에 필요한 하드웨어 및 소프트웨어 기능으로, 이에 따라 앱을 Google Play에서 설치할 수 있는 기기의 종류가 달라집니다. 기기 호환성에 대해 자세히 알아보세요.


## [안드로이드 어플리케이션의 프로젝트 구조](https://developer.android.com/studio/projects)

## [Android Context](https://blog.mindorks.com/understanding-context-in-android-application-330913e32514)

The Context in Android is actually the context of what we are talking about and where we are currently present. This will become more clear as we go along with this.

Few important points about the context:

- It is the context of the current state of the application. (컨텍스트는 애플리케이션의 현재 상태)
- It can be used to get information regarding the activity and application. (액티비티와 애플리케이션의 정보를 가져오는데 사용)
- It can be used to get access to resources, databases, and shared preferences, and etc. (리소스에 접근하거나 데이터베이스, 쉐어드 프리퍼런스 등에 접근할때 사용)
- Both the Activity and Application classes extend the Context class. (액티비티와 애플리케이션 클래스를 구현할때 컨텍스트를 확장)
- Context is almost everywhere in Android Development and it is the most important thing in Android Development, so we must understand to use it correctly.(컨텍스트는 거의 모든 안드로이드 개발에 사용하고 가장 중요하다.)
- Wrong use of Context can easily lead to memory leaks in an android application. (컨텍스트를 잘못 사용하면 안드로이드 애플리케이션에서 메모리 릭이 발생하기 쉽다.)

### Application Context
- 컨텍스트는 애플리케이션의 수명 주기와 연결됩니다. 애플리케이션 컨텍스트는 라이프사이클이 현재 컨텍스트와 분리된 컨텍스트가 필요하거나 활동 범위를 넘어 컨텍스트를 전달할 때 사용할 수 있습니다.

### Activity Context
- 이 컨텍스트는 액티비티의 수명 주기와 연결됩니다. 액티비티 범위에서 컨텍스트를 전달하거나 현재 컨텍스트에 라이프사이클이 연결된 컨텍스트가 필요할 때 액티비티 컨텍스트를 사용해야 합니다.

# Activity and Fragment
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

# Views and ViewGroups
## [안드로이드에서 View 란?](https://blog.mindorks.com/create-your-own-custom-view)
- 앱의 레이아웃을 디자인하기 위해 제공하는 구성요소
- 뷰 내부에 일부 요소를 포함할 직사각형 영역으로 이해
- 모든 UI 구성 요소의 슈퍼 클래스

### ViewGroup
- View 여러개가 모인것을 ViewGroup 이라고 한다.
- 최상위 ViewGroup 은 부모이고 그 아래에 이쓴 모든 뷰 및 기타 뷰 그룹은 자식이다.

## Custom View 만들기
- 안드로이드에서 제공하지 않아서 Custom View 나 재사용 가능한 View 를 만들어야 할때 사용

### Custom Views type
- Custom Views: 모든 것을 그리는 것
- Custom View Groups: 기존 위젯을 사용하여 inflate 가능한 xml 파일을 형성하는 것

### Custom View 를 사용해야 하는 이유
- UI 구성 요소를 재사용
- Android 에서 제공하지 않는 새로운 interaction 추가

### Android 는 어떻게 UI를 그리는지?
> onMeasure() -> onLayout() -> onDraw()

- onMeasure: 위에서 아래로 UI 크기를 측정하는 곳에서 호출되고, 부모 컨테이너를 사용, onMeasure() 에서 child 는 부모가 제공한 제약 조건을 받는다.
- onLayout(): 위젯의 위치를 플로팅하기 위해 호출
- onDraw(): UI 렌더링

### Custom View 만드는 단계
1. 기존 위젯 클래스 확장(예: Button, TextView)
2. View 클래스 확장(예: View)

```kotlin
class MyCustomView : View
```
- 적어도 하나의 생성자를 재정의 해야 한다.

```kotlin
class MyCustomView(context: Context) : View(context)
```
- draw 를 위해 기본 activity 컨텍스트가 필요합니다.

```kotlin
class MyCustomView(context: Context, attrs: AttributeSet) : View(context, attrs)
```
- XML 에서 새 View 인스턴스 생성

```kotlin
class MyCustomView(context: Context, attrs: AttributeSet, defstyleAttr:Int) : View(context, attrs, defstyeAttr)
```
- 디자인 속성 사용

```kotlin
class MyCustomView(context: Context, attrs: AttributeSet, defstyleAttr:Int, defStyleRes:Int) : View(context, attrs, defstyeAttr,defStyleRes)
```
- theme 속성 사용
<br>

**invalidate(): View 에게 업데이트해야 함을 알림.**

### onMeasure()
```kotlin
override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
    super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    size = Math.min(measuredWidth, measuredHeight)
    setMeasuredDimension(newsize, newsize)
}
```
- onMeasure()에서 widthMeasureSpec 및 heightMeasureSpec을 매개변수로 얻습니다. 이 매개변수는 측정 사양의 크기 및 모드로 구성됩니다.

```kotlin
int mode = MeasureSpec.getMode(widthMeasureSpec);
int size = MeasureSpec.getSize(widthMeasureSpec);
```

**MeasureSpec Type**
- MeasureSpec.Exactly : view가 layout_width = "100dp"와 같이 XML에 지정된 정확한 크기여야 함을 지정합니다.
- MeasureSpec.AT_MOST : wrap_content를 사용하는 동안 뷰가 최대일 수 있음을 지정합니다.
- MeasureSpec.UNSPECIFIED : 뷰가 원하는 만큼의 공간을 차지할 수 있습니다.

## ViewGroups 이란 무엇이며 View와 어떻게 다른지?

### View
- View 객체는 Android에서 UI(User Interface) 요소의 기본 빌딩 블록
- View는 사용자의 행동에 반응하는 간단한 사각형 상자입니다.
- 예를들면 EditText, Button, CheckBox 등입니다.
- View는 모든 UI 클래스의 기본 클래스인 android.view.View 클래스를 참조합니다.

### ViewGroups
- ViewGroup은 보이지 않는 컨테이너입니다. 
- View 및 ViewGroup을 보유합니다.
- 예를 들어 LinearLayout은 Button(View) 및 기타 레이아웃을 포함하는 ViewGroup입니다.
- ViewGroup은 레이아웃의 기본 클래스입니다.

## [ConstraintLayout 이란?](https://blog.mindorks.com/using-constraint-layout-in-android-531e68019cd)
- ConstraintLayout은 단순하고 표현력이 뛰어나며 유연한 레이아웃 시스템과 Android Studio Designer 도구에 내장된 강력한 기능을 결합합니다. 
- 다양한 화면 크기와 변화하는 장치 방향에 자동으로 적응하는 반응형 사용자 인터페이스 레이아웃을 더 쉽게 만들 수 있습니다.

### ConstraintLayout 이 필요한 이유?
- 복잡한 화면 디자인을 더 쉽게 처리 할수 있고 레이아웃의 성능을 향상 시킨다.
- 중첩된 레이아웃이 있는 복잡한 화면 디자인도 빠르게 처리할 수 있다.
- 단일 레이아웃 인스턴스로 이전 레이아웃의 많은 기능을 달성할 수 있는 유연성 수준을 제공
- 평면 또는 얕은 레이아웃 계층 구조를 설계할 수 있다.
- 레이아웃이 덜 복잡해지고 런타임 시 사용자 인터페이스 렌더링 성능이 향상된다.
- 다양한 Android 기기 화면 크기를 처리하기 위해 구현되었다.

### 참고
[Learn Android ConstraintLayout - Part 1 - Everything you need to know](https://youtu.be/P2XHNDHI4PQ)
[Learn Android ConstraintLayout - Part 2 - Guideline, Barrier and Group](https://youtu.be/tUn_eGBr1n4)

## ViewTreeObserver가 무엇인지?
- 뷰 트리 옵저버는 뷰 트리의 전역 변경 사항을 알릴 수 있는 리스너를 등록하는 데 사용됩니다. 
- 전역 이벤트에는 전체 트리의 레이아웃, 드로잉 패스의 시작, 터치 모드 변경이 포함되지만 이에 국한되지는 않습니다. 
- ViewTreeObserver는 뷰 계층 구조에서 제공하는 것처럼 애플리케이션에서 인스턴스화해서는 안 됩니다. 

# Displaying Lists of Content

## ListView 와 RecyclerView 의 차이점
1. 스크롤시 셀을 재활용 - ListView Adapter 에서도 ViewHolder 를 사용하면 가능하지만 옵션 사항이고, RecyclerView 는 adapter 구현시 기본으로 적용되는 방법이다.
2. 컨테이너에서 List를 분리 - LayoutManager를 설정하여 런타임에 다른 컨테이너(linearLayout, horizontal, gridLayout)에 List를 쉽게 넣을 수 있다.
```java
mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
//or
mRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
```
3. 일반적인 List 작업에 애니메이션을 적용 - 애니메이션은 분리되어 ItemAnimator에 위임된다.

# Useful Kotlin Extension
## 유용하게 사용하는 Extension
### 각 타입별 null or 초기화 값 할당
- Int.orZero(), Long.orZero()

```kotlin
fun Int?.orZero() = this ?: 0
```

### 색상 및 drawable Resource 가져오기
```kotlin
fun Context.getCompatColor(@ColorRes colorId: Int) = ResourcesCompat.getColor(resources, colorId, null)
fun Context.getCompatDrawable(@DrawableRes drawableId: Int) = AppCompatResources.getDrawable(this, drawableId)!!

// Usage
// Activity
getCompatColor(R.color.white)

// Fragment
requireContext().getCompatColor(R.color.white)
```

### Permission 확인
```kotlin
fun Context.hasPermissions(vararg permissions: String) = permissions.all { permission ->
    ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED
}

// Usage
// Traditional way (Activity)
(ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
   != PackageManager.PERMISSION_GRANTED) && (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
   != PackageManager.PERMISSION_GRANTED)

// Extension (Activity)
hasPermissions(Manifest.permission.CAMERA, Manifest.permission.ACCESS_FINE_LOCATION)
```

### Clipboard 에 복사
```kotlin
fun Context.copyToClipboard(content: String) {
    val clipboardManager = ContextCompat.getSystemService(this, ClipboardManager::class.java)!!
    val clip = ClipData.newPlainText("clipboard", content)
    clipboardManager.setPrimaryClip(clip)
}
```

### Uri 에 해당하는 인텐트를 처리할 앱이 없을때 처리
```kotlin
fun Context.isResolvable(intent: Intent) = intent.resolveActivity(packageManager) != null

fun Context.view(uri: Uri, onAppMissing: () -> Unit) {
    val intent = Intent(Intent.ACTION_VIEW, uri)

    if (!isResolvable(intent)) {
        onAppMissing()
        return
    }

    startActivity(intent)
}
```

### 참고
[Useful Kotlin Extensions for Android](https://medium.com/nerd-for-tech/useful-kotlin-extensions-for-android-d8652f64bca8)
