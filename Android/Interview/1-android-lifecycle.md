# 안드로이드 액티비티 생명주기와 그 활용 방법
- 질문: 안드로이드 액티비티 생명주기에 대해 설명해주시고, 각 생명주기 메소드를 어떤 상황에서 활용하는지 예를 들어 설명해주세요.
- 답변: 안드로이드 액티비티 생명주기는 앱이 사용자와 상호작용하는 과정에서 액티비티의 상태가 변화하는 순서를 정의합니다. 각 생명주기 메소드는 특정 시점에 시스템에 의해 호출되며, 개발자는 이를 재정의하여 적절한 동작을 구현할 수 있습니다.

## 액티비티 생명주기 메소드 순서:
- `onCreate()`: 액티비티가 생성될 때 호출되며, 필수적인 컴포넌트를 초기화하고 레이아웃을 설정합니다.
- `onStart()`: 액티비티가 사용자에게 보이기 직전에 호출됩니다.
- `onResume()`: 액티비티가 사용자와 상호작용을 시작할 때 호출됩니다.
- `onPause()`: 다른 액티비티가 포커스를 얻을 때 호출됩니다. 여기서 변경사항을 저장하거나 리소스를 해제하기 시작합니다.
- `onStop()`: 액티비티가 더 이상 보이지 않을 때 호출됩니다.
- `onRestart()`: 정지된 액티비티가 다시 시작되기 전에 호출됩니다.
- `onDestroy()`: 액티비티가 완전히 소멸되기 전에 호출됩니다.

![액티비티 라이프 사이클](./activity-lifecycle.svg)

## 각 생명주기 메소드의 활용 사례:

### onCreate()
```kotlin
override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)
    
    // View 초기화
    recyclerView = findViewById(R.id.recyclerView)
    adapter = UserAdapter()
    recyclerView.adapter = adapter
    
    // ViewModel 초기화
    viewModel = ViewModelProvider(this).get(UserViewModel::class.java)
    
    // 저장된 상태 복원
    savedInstanceState?.let {
        currentPosition = it.getInt("SCROLL_POSITION", 0)
        recyclerView.scrollToPosition(currentPosition)
    }
}
```
**활용 사례:**
- 뷰 초기화 및 바인딩
- ViewModel과 LiveData 설정
- 이전 상태 복원(savedInstanceState)
- 의존성 주입
- 앱이 시작될 때 필요한 데이터 로딩

### onStart()
```kotlin
override fun onStart() {
    super.onStart()
    
    // UI 업데이트를 위한 Observer 등록
    viewModel.users.observe(this, Observer { users ->
        adapter.submitList(users)
    })
    
    // 브로드캐스트 리시버 등록
    registerReceiver(networkReceiver, IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION))
}
```
**활용 사례:**

- LiveData 관찰 시작
- 브로드캐스트 리시버 등록
- 서비스 바인딩
- 화면이 표시될 때 필요한 리소스 초기화

### onResume()
```kotlin
override fun onResume() {
    super.onResume()
    
    // 실시간 업데이트 시작
    locationManager.requestLocationUpdates(provider, 0, 0, locationListener)
    
    // 사용자 활동 추적 시작
    analyticsTracker.trackScreenView("Main Screen")
    
    // 애니메이션, 비디오 재생 시작
    videoView.start()
}
```
**활용 사례:**

- 실시간 업데이트 시작 (위치, 센서 등)
- 애니메이션 재생 시작
- 미디어 재생 시작
- 포그라운드에서만 필요한 리소스 초기화
- 분석 트래킹 시작

### onPause()
```kotlin
override fun onPause() {
    // 실시간 업데이트 중지
    locationManager.removeUpdates(locationListener)
    
    // 미디어 재생 일시 중지
    videoView.pause()
    
    // 사용자 입력 저장
    viewModel.saveUserInput(editText.text.toString())
    
    super.onPause()
}
```
**활용 사례:**

- 실시간 업데이트 중지
- 미디어 재생 일시 중지
- 사용자 입력 저장
- 애니메이션 일시 중지
- 포그라운드 전용 리소스 해제
- 중요 데이터 저장 (항상 onPause에서 수행해야 함, onStop은 호출되지 않을 수 있음)

### onStop()
```kotlin
override fun onStop() {
    // 브로드캐스트 리시버 해제
    unregisterReceiver(networkReceiver)
    
    // 서비스 언바인딩
    unbindService(serviceConnection)
    
    // 무거운 작업 취소
    coroutineScope.cancel()
    
    super.onStop()
}
```
**활용 사례:**

- 브로드캐스트 리시버 해제
- 서비스 언바인딩
- 리소스 집약적인 작업 중지
- 네트워크 콜 취소
- UI 관련 업데이트 중지

### onRestart()
```kotlin
override fun onRestart() {
    super.onRestart()
    
    // 사용자 세션 검증
    viewModel.validateUserSession()
    
    // 최신 데이터 확인
    viewModel.checkForUpdates()
}
```
**활용 사례:**

- 다시 시작될 때 필요한 데이터 새로고침
- 사용자 세션 유효성 확인
- 앱 상태 복원 준비

### onDestroy()
```kotlin
override fun onDestroy() {
    // 작업 취소
    job.cancel()
    
    // 메모리 해제
    adapter = null
    binding = null
    
    // 싱글톤 인스턴스 정리
    MyApplication.getInstance().clearTemporaryData()
    
    super.onDestroy()
}
```
**활용 사례:**

- 실행 중인 스레드 정리
- 코루틴 작업 취소
- 데이터베이스 연결 종료
- 메모리 누수 방지를 위한 리소스 해제
- 임시 파일 정리

## 꼬리 질문 1: 화면 회전 시 액티비티 생명주기가 어떻게 변화하고, 이를 어떻게 처리하나요?

**화면 회전 시 기본적으로 액티비티는 다음 순서로 생명주기가 변화합니다:**

- onPause()
- onStop()
- onDestroy()
- onCreate()
- onStart()
- onResume()

이는 액티비티가 완전히 파괴되고 다시 생성되기 때문에 발생합니다. 이 과정에서 다음과 같은 문제가 발생할 수 있습니다:

- 상태 손실: 현재 입력 데이터, 스크롤 위치 등이 손실됨
- 리소스 소모: 모든 초기화 로직이 다시 실행됨
- 네트워크 요청 중복: 진행 중이던 네트워크 요청이 다시 시작될 수 있음

**해결 방법:**
1. savedInstanceState 활용:
```kotlin
override fun onSaveInstanceState(outState: Bundle) {
    super.onSaveInstanceState(outState)
    outState.putInt("SCROLL_POSITION", recyclerView.computeVerticalScrollOffset())
    outState.putString("USER_INPUT", editText.text.toString())
}
```

2. ViewModel 사용:
```kotlin
// ViewModel은 화면 회전에도 유지됩니다
class MainViewModel : ViewModel() {
    val userInput = MutableLiveData<String>()
    val searchResults = MutableLiveData<List<User>>()
    
    // 비즈니스 로직...
}
```

3. onConfigurationChanged 처리:
```kotlin
// AndroidManifest.xml에 configChanges 설정 필요
override fun onConfigurationChanged(newConfig: Configuration) {
    super.onConfigurationChanged(newConfig)
    
    if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
        // 가로 모드 UI 조정
    } else {
        // 세로 모드 UI 조정
    }
}
```

4. Fragment 활용:
```kotlin
// Activity에서 Fragment를 사용하면 구성 변경에 더 유연하게 대응할 수 있습니다
supportFragmentManager.beginTransaction()
    .add(R.id.fragment_container, MainFragment(), "main_fragment")
    .commit()
```


## 꼬리 질문 2: 액티비티 A에서 액티비티 B를 호출하고 결과를 받아올 때 생명주기는 어떻게 동작하나요?

액티비티 A에서 B를 호출하고 결과를 받는 과정의 생명주기 흐름:

- 액티비티 A: onPause()
- 액티비티 B: onCreate(), onStart(), onResume()
- (사용자가 B에서 작업 후 결과를 반환하고 종료)
- 액티비티 B: onPause()
- 액티비티 A: onActivityResult(), onResume()
- 액티비티 B: onStop(), onDestroy()

**최신 방식 (ActivityResultLauncher):**
```kotlin
// 액티비티 A에서:
private val getContent = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
    if (result.resultCode == Activity.RESULT_OK) {
        val data: Intent? = result.data
        // 데이터 처리
        textView.text = data?.getStringExtra("RESULT_KEY")
    }
}

// 버튼 클릭 시 실행
button.setOnClickListener {
    val intent = Intent(this, ActivityB::class.java)
    getContent.launch(intent)
}

// 액티비티 B에서:
button.setOnClickListener {
    val resultIntent = Intent()
    resultIntent.putExtra("RESULT_KEY", "결과 데이터")
    setResult(Activity.RESULT_OK, resultIntent)
    finish()
}
```
- `onSaveInstanceState()`는 시스템이 액티비티를 파괴할 수 있지만 백 스택에 남아있는 경우에 호출됩니다
- `onActivityResult()`는 최신 안드로이드에서는 권장되지 않으며, `registerForActivityResult()`를 사용해야 합니다
- 액티비티 A의 `onPause()`와 액티비티 B의 `onCreate()` 사이에 중요한 작업이 있다면 조심해야 합니다
- 액티비티 B가 결과를 반환하고 종료될 때 액티비티 A는 `onResume()` 상태로 돌아갑니다


## 꼬리 질문 3: 액티비티 생명주기와 관련된 메모리 누수 문제는 어떻게 예방하고 디버깅하나요?
액티비티 생명주기와 관련한 메모리 누수의 주요 원인과 해결책:

**1. 정적 참조 문제:**
```kotlin
// 문제가 있는 코드
companion object {
    private lateinit var activityInstance: MainActivity
}

override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    activityInstance = this // 메모리 누수 발생
}
```

**해결책: 정적 참조 피하기, Weak 참조 사용, 애플리케이션 컨텍스트 사용**
```kotlin
companion object {
    private var activityRef: WeakReference<MainActivity>? = null
}

override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    activityRef = WeakReference(this)
}
```

**2. 익명 클래스의 참조 문제:**
```kotlin
// 문제가 있는 코드
override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    
    val timer = Timer()
    timer.schedule(object : TimerTask() {
        override fun run() {
            // 이 익명 클래스는 Activity에 대한 암시적 참조를 가짐
            runOnUiThread { updateUI() }
        }
    }, 1000, 1000)
}
```

**해결책: 정적 중첩 클래스 사용, 생명주기 인식 컴포넌트 사용**
```kotlin
private class MyTimerTask(activity: MainActivity) : TimerTask() {
    private val activityRef = WeakReference(activity)
    
    override fun run() {
        val activity = activityRef.get() ?: return
        activity.runOnUiThread { activity.updateUI() }
    }
}

// 사용 시
timer.schedule(MyTimerTask(this), 1000, 1000)
```

**3. 리스너 등록/해제 불균형:**
```kotlin
// 문제가 있는 코드
override fun onResume() {
    super.onResume()
    sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL)
}

// onPause()에서 해제하지 않음
```

**해결책: 등록/해제 쌍을 맞추기**
```kotlin
override fun onResume() {
    super.onResume()
    sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL)
}

override fun onPause() {
    sensorManager.unregisterListener(this)
    super.onPause()
}
```

**4. 비동기 콜백 문제:**
```kotlin
// 문제가 있는 코드
override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    
    apiService.fetchData(object : Callback<Data> {
        override fun onResponse(call: Call<Data>, response: Response<Data>) {
            // 액티비티가 이미 파괴된 후에도 호출될 수 있음
            textView.text = response.body()?.message
        }
        // ...
    })
}
```

**해결책: 생명주기 인식 컴포넌트 사용, 취소 가능한 비동기 작업 사용**
```kotlin
private val apiCall = apiService.fetchData()
private val job = CoroutineScope(Dispatchers.IO).launch {
    try {
        val response = apiCall.await()
        withContext(Dispatchers.Main) {
            textView.text = response.message
        }
    } catch (e: Exception) {
        // 오류 처리
    }
}

override fun onDestroy() {
    job.cancel() // 작업 취소
    super.onDestroy()
}
```

### 메모리 누수 디버깅 방법:
1. **LeakCanary 라이브러리 사용:**
```
// build.gradle (app)
dependencies {
    debugImplementation 'com.squareup.leakcanary:leakcanary-android:2.10.0'
}
```

2. **Android Profiler 활용:**
- Android Studio의 Profiler 도구를 사용하여 메모리 사용량 모니터링
- 힙 덤프를 생성하고 분석하여 메모리 누수 발견

3. **생명주기 인식 컴포넌트 활용:**
```kotlin
class MyViewModel : ViewModel() {
    // ViewModel은 액티비티/프래그먼트 생명주기보다 오래 지속됨
    // 하지만 onCleared()에서 리소스 정리 가능
    
    override fun onCleared() {
        // 리소스 정리
        super.onCleared()
    }
}
```

이처럼 안드로이드 액티비티 생명주기를 깊이 이해하고 적절히 활용하면 메모리 누수를 방지하고, 사용자 경험을 향상시키며, 앱의 성능을 최적화할 수 있습니다.
