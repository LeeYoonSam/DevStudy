# Android Framework

- [Android란 무엇인가요?](./Android/README.md)
- [Intent란 무엇인가요?](./Intent/README.md)
- [Pending Intent 의 목적은 무엇인가요?](./PendingIntent/README.md)
- [Serializable 과 Parcelable 의 차이점은 무엇인가요?](./SerializableParcelable/README.md)
- [Context란 무엇이며 어떤 유형의 컨텍스트가 있나요?](./Context/README.md)
- [Application class란 무엇인가요?](./Application/README.md)
- [AndroidManifest 파일의 용도는 무엇인가요?](./Manifest/README.md)
- [액티비티 생명주기 설명](./Activity/README.md)
- [프래그먼트 생명주기 설명](./Fragment/README.md)
- [서비스(Service)란 무엇인가?](./Service/README.md)
- [BroadcastReceiver(브로드캐스트 리시버)란 무엇인가?](./BroadcastReceiver/README.md)
- [콘텐츠 제공자(ContentProvider)의 목적은 무엇이며, 애플리케이션 간의 안전한 데이터 공유를 어떻게 용이하게 하나요?](./ContentProvider/README.md)

---

# ANR 오류의 주요 원인은 무엇이며, 발생을 어떻게 예방할 수 있나요?
ANR (Application Not Responding, 애플리케이션 응답 없음) 은 안드로이드에서 앱의 메인 스레드(UI 스레드) 가 너무 오랫동안(일반적으로 5초 이상) 차단(blocked)될 때 발생하는 오류입니다. ANR이 발생하면 안드로이드는 사용자에게 앱을 닫거나 응답을 기다릴지 묻는 메시지를 표시합니다. ANR은 사용자 경험을 저하시키며 다음과 같은 다양한 요인에 의해 발생할 수 있습니다.
 * 메인 스레드에서 5초 이상 걸리는 과도한(무거운) 연산 수행
 * 오래 실행되는 네트워크 또는 데이터베이스 작업
 * UI를 차단하는 작업 (예: UI 스레드에서의 동기(synchronous) 작업)
ANR 예방 방법
ANR을 예방하려면 무겁거나 시간이 오래 걸리는 작업을 다른 곳으로 옮겨 메인 스레드의 응답성을 유지하는 것이 중요합니다. 다음은 몇 가지 모범 사례입니다.
 * 집중적인 작업을 메인 스레드 밖으로 옮기기: 파일 입출력, 네트워크 요청 또는 데이터베이스 작업과 같은 작업을 처리하기 위해 백그라운드 스레드(예: AsyncTask(현재 deprecated), Executors, Thread)를 사용하세요. 더 현대적이고 안전한 접근 방식을 원한다면, 효율적인 백그라운드 작업 관리를 위해 Dispatchers.IO와 함께 코틀린 코루틴(Kotlin Coroutines) 을 활용하세요.
 * 지속적인 작업에 WorkManager 사용: 데이터 동기화와 같이 백그라운드에서 실행되어야 하는 작업에는 WorkManager(카테고리 3: 비즈니스 로직에서 나중에 다룰 내용)를 사용하세요. 이 API는 작업이 메인 스레드 외부에서 예약되고 실행되도록 보장합니다.
 * 데이터 가져오기 최적화: Paging 라이브러리를 구현하여 대규모 데이터 세트를 효율적으로 처리하세요. 데이터를 관리 가능한 작은 단위(청크)로 가져와 UI 과부하를 방지하고 성능을 개선합니다.
 * 구성 변경 시 UI 작업 최소화: ViewModel을 활용하여 UI 관련 데이터를 유지하고, 화면 회전과 같은 구성 변경 중에 불필요한 UI 재로드를 피하세요.
 * Android Studio로 모니터링 및 프로파일링: Android Studio의 프로파일러(Profiler) 도구를 활용하여 CPU, 메모리 및 네트워크 사용량을 모니터링하세요. 이러한 도구는 ANR을 유발할 수 있는 성능 병목 현상을 식별하고 해결하는 데 도움이 됩니다.
 * 차단 호출 피하기: 부드러운 앱 성능을 보장하기 위해 메인 스레드에서 긴 루프, sleep 호출 또는 동기식 네트워크 요청과 같은 차단(blocking) 작업을 피하세요.
 * 짧은 지연에 Handler 사용: 반응성 좋은 앱 경험을 위해 Thread.sleep() 대신 Handler.postDelayed()를 사용하여 메인 스레드를 차단하지 않고 짧은 지연을 도입하세요.
요약
ANR(Application Not Responding)은 앱의 메인 스레드(UI 스레드)가 일반적으로 5초 이상 차단될 때 발생하는 안드로이드 오류이며, 사용자 경험을 저하시키고 현재 사용자의 모든 상태를 잃게 만듭니다. ANR을 예방하려면 네트워크에서 데이터 요청, 데이터베이스 쿼리, 무거운 연산 작업 수행 등 집중적인 작업을 백그라운드 스레드로 옮겨 메인 스레드를 가볍게 유지해야 합니다. 또한 데이터 작업을 최적화하고 Android Studio 프로파일러를 사용하여 앱을 프로파일링할 수 있습니다. 자세한 내용은 ANR에 대한 공식 안드로이드 문서를 참조하세요.

## Q. ANR을 어떻게 탐지 및 진단하고 앱 성능을 개선할 수 있나요?
ANR(애플리케이션 응답 없음) 탐지 및 진단 방법
ANR이 발생하는 것을 감지하고 그 원인을 진단하는 방법은 다음과 같습니다.
 * 사용자 보고 및 Google Play Console (Android Vitals):
   * 사용자가 직접 "앱이 응답하지 않습니다" 대화 상자를 경험하고 보고하는 것이 가장 직접적인 신호입니다.
   * Google Play Console의 'Android Vitals' 섹션은 실제 사용자 환경에서 발생한 ANR 데이터를 자동으로 수집하고 집계해 보여줍니다. ANR 발생률, 유사한 ANR 클러스터, 발생 시점의 스택 트레이스(메인 스레드뿐 아니라 관련 스레드 정보 포함), 앱 버전, 기기, OS 버전별 통계를 제공하여 운영 환경에서의 ANR 문제를 파악하는 데 가장 중요합니다.
 * Firebase Crashlytics:
   * 주로 비정상 종료(Crash) 리포팅 도구이지만, ANR 발생 시 관련 정보(스택 트레이스, 기기 정보 등)를 함께 리포팅하도록 설정할 수 있어 통합적인 분석에 도움이 됩니다.
 * Android Studio CPU Profiler:
   * 개발 및 테스트 단계에서 CPU 프로파일러를 사용하여 앱의 스레드 활동을 모니터링합니다. 앱이 응답하지 않을 때 메인 스레드(보통 'main' 또는 'UI Thread'로 표시됨)의 상태를 확인합니다. 메인 스레드에서 오랜 시간 동안 멈춰 있거나 특정 메서드가 길게 실행되는 구간이 보인다면 ANR의 원인일 가능성이 높습니다. 'Sampled (Java)' 또는 'Trace System Calls' 모드를 활용하여 분석할 수 있습니다.
 * /data/anr/ traces.txt 파일 분석 (ADB 또는 루팅 필요):
   * 기기에서 ANR이 발생하면 시스템은 /data/anr/ 디렉토리에 traces.txt (또는 유사한 이름) 파일을 생성합니다. 이 파일에는 ANR 발생 시점의 모든 스레드에 대한 스택 트레이스가 포함되어 있어 가장 상세한 진단 정보를 제공합니다. 특히 메인 스레드가 어떤 작업을 하고 있었는지, 어떤 락(lock)을 기다리고 있었는지, 다른 스레드(특히 메인 스레드가 필요로 하는 락을 점유 중인 스레드)는 무엇을 하고 있었는지 분석하는 데 유용합니다. adb bugreport 명령어나 루팅된 기기에서 직접 접근하여 파일을 얻을 수 있습니다.
 * StrictMode:
   * 개발용 빌드에서 프로그래밍 방식(StrictMode.setThreadPolicy, StrictMode.setVmPolicy)이나 개발자 옵션을 통해 StrictMode를 활성화합니다. 이는 메인 스레드에서의 디스크 I/O나 네트워크 호출과 같은 잠재적인 차단 작업을 감지하여 로그를 남기거나 앱을 강제 종료(디버그 빌드에서)시켜 개발 초기에 ANR 유발 요인을 찾는 데 도움을 줄 수 있습니다.
 * 수동 테스트:
   * 느린 네트워크 환경, 대용량 데이터 처리, 복잡한 사용자 인터랙션 등 다양한 엣지 케이스(edge case)를 직접 테스트하며 앱이 응답하지 않는 상황이 발생하는지 확인합니다.
앱 성능 개선 방법 (ANR 예방 및 사용자 경험 향상)
앱 성능을 개선하는 것은 ANR을 예방하고 전반적인 사용자 경험을 향상시키는 핵심입니다.
 * 메인 스레드 작업 부하 줄이기 (가장 중요):
   * 코틀린 코루틴: I/O 작업에는 Dispatchers.IO, CPU 집약적 작업에는 Dispatchers.Default를 사용하여 백그라운드에서 실행합니다. viewModelScope나 lifecycleScope 등 적절한 CoroutineScope를 사용하여 생명주기를 관리합니다.
   * WorkManager: 앱이 종료되어도 안정적으로 실행되어야 하는 백그라운드 작업(데이터 동기화 등)에 사용합니다.
   * 스레드 풀/스레드: 기타 백그라운드 작업에는 Executors나 Thread를 사용하되, 생명주기 관리에 주의합니다.
 * UI 렌더링 최적화:
   * 뷰 계층 구조를 단순화하고 ConstraintLayout을 효율적으로 사용합니다.
   * 측정 및 레이아웃 단계(onMeasure, onLayout)에서 복잡한 계산을 피합니다.
   * RecyclerView를 효율적으로 사용합니다 (ListAdapter와 DiffUtil 사용, 아이템 레이아웃 최적화, onBindViewHolder 로직 간소화).
   * Jetpack Compose 사용을 고려하여 선언적 UI 방식으로 상태 관리 및 리컴포지션(recomposition)을 최적화합니다.
 * 데이터베이스 및 네트워크 작업 최적화:
   * 모든 DB 및 네트워크 호출은 반드시 백그라운드 스레드에서 수행합니다.
   * 효율적인 DB 쿼리(인덱싱 활용, 필요한 컬럼만 조회)를 사용하고 Room 라이브러리 사용을 권장합니다.
   * Retrofit, OkHttp 등 비동기 호출을 지원하는 네트워킹 라이브러리를 사용합니다.
   * 메모리 및 디스크 캐싱 전략을 구현하여 불필요한 데이터 요청을 줄입니다.
   * 대용량 데이터 로딩에는 Paging 라이브러리를 사용합니다.
 * 앱 시작 시간 단축:
   * 앱 시작 시 메인 스레드를 차단하는 작업이 너무 길면 ANR의 원인이 될 수 있습니다. App Startup 라이브러리를 사용하거나 Application.onCreate 내 초기화 로직을 최적화합니다. Baseline Profile 적용을 고려합니다.
 * 메모리 관리:
   * 메모리 누수를 방지하고 비트맵 사용을 최적화하여 잦은 GC로 인한 멈춤 현상을 줄입니다.
 * 주기적인 프로파일링:
   * 개발 중 Android Studio 프로파일러(CPU, 메모리, 네트워크, 에너지)를 꾸준히 사용하여 성능 병목 지점을 미리 찾아냅니다. 메인 스레드에서 오래 걸리는 메서드, 과도한 메모리 할당 등을 중점적으로 확인합니다.
 * 성능 모니터링 도구 활용:
   * Firebase Performance Monitoring과 같은 도구를 통합하여 운영 환경 사용자로부터 실제 성능 데이터(특정 작업 소요 시간, 네트워크 요청 시간 등)를 수집하고 분석합니다.
이러한 진단 및 개선 방법을 통해 ANR 발생 가능성을 줄이고 사용자에게 더 빠르고 안정적인 앱 경험을 제공할 수 있습니다.

---

# 딥 링크(Deep Link)를 어떻게 처리하나요?
딥 링크는 사용자가 URL이나 알림과 같은 외부 소스에서 앱 내의 특정 화면이나 기능으로 직접 이동할 수 있게 해줍니다. 딥 링크를 처리하는 것은 AndroidManifest.xml 파일에 이를 정의하고, 해당 액티비티(Activity)나 프래그먼트(Fragment)에서 수신 인텐트(intent)를 처리하는 과정을 포함합니다.

## 1단계: 매니페스트(Manifest)에 딥 링크 정의하기
딥 링크를 활성화하려면, 딥 링크를 처리해야 하는 액티비티에 대해 AndroidManifest.xml 파일에 인텐트 필터(intent filter) 를 선언합니다. 인텐트 필터는 앱이 응답할 URL 구조나 스키마(scheme)를 지정합니다.

<activity
    android:name=".MyDeepLinkActivity"
    android:exported="true">
    <intent-filter>
        <action android:name="android.intent.action.VIEW" />
        <category android:name="android.intent.category.DEFAULT" />
        <category android:name="android.intent.category.BROWSABLE" />
        <data
            android:scheme="https" <!- URL 스키마 지정 (예: https) -->
            android:host="example.com" <!- 도메인 지정 (예: example.com) -->
            android:pathPrefix="/deepLink" /> <!- URL 내 경로 정의 (예: /deepLink) -->
    </intent-filter>
</activity>

이 설정은 https://example.com/deepLink와 같은 URL이 MyDeepLinkActivity를 열도록 허용합니다.

## 2단계: 액티비티에서 딥 링크 처리하기
액티비티 내부에서는 수신된 인텐트 데이터를 검색하고 처리하여 적절한 화면으로 이동하거나 특정 동작을 수행합니다.

class MyDeepLinkActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_deep_link)

        val data: Uri? = intent.data // 수신 인텐트에서 데이터(URI) 가져오기

        if (data != null) {
            // URI에서 필요한 정보 추출 (예: 쿼리 파라미터)
            val id = data.getQueryParameter("id")
            // 추출된 데이터를 사용하여 특정 화면으로 이동하거나 작업 수행
            navigateToFeature(id)
        }
    }
    // ...
}

## 3단계: 딥 링크 테스트하기
딥 링크를 테스트하려면 아래의 adb 명령어를 사용할 수 있습니다.
adb shell am start -a android.intent.action.VIEW \
    -d "https://example.com/deepLink?id=123" \
    com.example.myapp # 앱의 패키지 이름

이 명령어는 딥 링크를 시뮬레이션하여 앱이 이를 처리하도록 엽니다.

추가 고려 사항
 * 사용자 정의 스키마(Custom Schemes): 내부 링크에는 myapp://과 같은 사용자 정의 스키마를 사용할 수 있지만, 더 넓은 호환성을 위해 HTTP(S) URL을 선호하는 것이 좋습니다.
 * 내비게이션(Navigation): 딥 링크 데이터에 기반하여 앱 내의 다른 액티비티나 프래그먼트로 이동하기 위해 인텐트를 사용합니다. Jetpack Navigation 컴포넌트를 사용하면 딥 링크 처리를 더 쉽게 구현할 수 있습니다.
 * 폴백 처리(Fallback Handling): 딥 링크 데이터가 유효하지 않거나 불완전한 경우 앱이 이를 적절히 처리하도록 보장해야 합니다 (예: 기본 화면으로 이동).
 * 앱 링크(App Links): HTTP(S) 딥 링크가 브라우저 대신 앱에서 바로 열리도록 하려면 앱 링크(App Links)를 설정해야 합니다. (서버 측 설정 필요)

## 요약
딥 링크 처리는 AndroidManifest.xml에서 URL 패턴을 정의하고 대상 액티비티에서 데이터를 처리하는 것을 포함합니다. 딥 링크 데이터를 추출하고 해석함으로써 사용자를 앱의 특정 기능이나 콘텐츠로 안내하여 사용자 경험과 참여(engagement)를 향상시킬 수 있습니다.

## Q. 안드로이드에서 딥 링크를 어떻게 테스트할 수 있으며, 다양한 기기와 시나리오에서 올바르게 작동하는지 확인하기 위한 일반적인 디버깅 기법은 무엇인가요?
안드로이드에서 딥 링크(Deep Link)를 테스트하고 다양한 환경에서 올바르게 작동하는지 확인하기 위한 방법과 디버깅 기법은 다음과 같습니다.

딥 링크 테스트 방법
 * ADB (Android Debug Bridge) 사용: 개발 중 가장 직접적이고 편리한 방법입니다. 터미널이나 명령 프롬프트에서 아래 명령어를 실행하여 특정 딥 링크 URL로 인텐트를 보내는 것을 시뮬레이션할 수 있습니다.
   adb shell am start -a android.intent.action.VIEW \
    -d "딥링크_URL_입력" 패키지_이름

   * 딥링크_URL_입력: 테스트하려는 딥 링크 URL (예: https://example.com/products/123 또는 myapp://details?id=456)
   * 패키지_이름: 앱의 고유 패키지 이름 (예: com.example.myapp)
 * 클릭 가능한 링크 사용:
   * 웹 페이지: 간단한 HTML 파일에 딥 링크를 포함하는 <a> 태그를 만들고(<a href="딥링크_URL">테스트 링크</a>), 이 파일을 기기의 웹 브라우저에서 열어 링크를 클릭합니다.
   * 메시징/메모 앱: Slack, 카카오톡, SMS, 이메일 또는 메모 앱 등에 딥 링크 URL을 보내거나 입력하여 클릭 가능한 링크로 렌더링되면 이를 클릭합니다. (앱마다 링크 처리 방식이 다를 수 있습니다.)
 * Android Studio 도구 활용:
   * App Links Assistant: (주로 App Links 설정용이지만 유용) 매니페스트 설정 확인 및 테스트 URL 생성에 도움을 줄 수 있습니다.
   * Navigation Editor: (Jetpack Navigation 사용 시) 시각적으로 딥 링크를 정의하고, 딥 링크에 의해 트리거되는 내비게이션 로직을 테스트하는 데 도움이 될 수 있습니다.
 * QR 코드 사용:
   * 딥 링크 URL을 QR 코드로 생성한 후, 기기의 카메라나 QR 코드 스캐너 앱으로 스캔합니다. 실제 환경에서 사용자가 QR 코드를 통해 앱에 접근하는 시나리오를 테스트할 수 있습니다.
 * 알림(Notification) 사용:
   * Firebase Cloud Messaging(FCM) 등을 이용하여 딥 링크 정보를 포함하는 인텐트를 가진 푸시 알림을 보냅니다. 알림을 클릭했을 때 올바른 목적지로 이동하는지 테스트합니다.
 * 자동화된 테스트:
   * Espresso나 UI Automator 같은 테스트 프레임워크를 사용하여 딥 링크를 여는 인텐트를 시뮬레이션하고, 올바른 액티비티/프래그먼트가 실행되고 예상된 콘텐츠가 표시되는지 검증하는 UI 테스트 코드를 작성합니다.
일반적인 디버깅 기법 (다양한 기기/시나리오 대응)
 * 매니페스트(Manifest) 설정 확인:
   * AndroidManifest.xml 파일의 <intent-filter> 설정을 꼼꼼히 확인합니다. android:scheme, android:host, android:pathPrefix/path/pathPattern 값이 정확한지, 오타는 없는지 확인합니다.
   * 대상 액티비티에 android:exported="true" 속성이 있는지 확인합니다. (Android 12 이상 타겟 시 필수일 수 있음)
   * <category android:name="android.intent.category.DEFAULT" /> 와 <category android:name="android.intent.category.BROWSABLE" /> 카테고리가 포함되어 있는지 확인합니다.
 * 인텐트 처리 로직 검증:
   * 딥 링크를 처리하는 액티비티의 onCreate() 또는 onNewIntent() (액티비티의 launchMode가 standard가 아닐 경우) 메서드 내부에 중단점(breakpoint)을 설정하거나 상세한 로그를 추가합니다.
   * 수신된 인텐트의 intent.action과 intent.data (URI 객체) 값을 로그로 출력하여 확인합니다.
   * URI에서 경로 세그먼트(uri.pathSegments)나 쿼리 파라미터(uri.getQueryParameter("파라미터명"))를 추출하는 로직이 다양한 형식의 URI, 누락된 파라미터, 잘못된 값 등을 올바르게 처리하는지 검증합니다.
 * 다양한 실행 모드(Launch Mode) 테스트:
   * 액티비티의 launchMode(singleTop, singleTask 등)가 딥 링크 처리에 미치는 영향을 이해하고 테스트합니다. 특히 앱이 이미 실행 중일 때, 백그라운드에 있을 때, 해당 액티비티가 이미 스택에 존재할 때 딥 링크를 열어봅니다. 필요한 경우 onNewIntent()에서의 처리가 올바른지 확인합니다.
 * 유효하지 않거나 불완전한 데이터 처리:
   * 딥 링크 URI 형식이 잘못되었거나, 예상한 경로 세그먼트가 없거나, 필수 쿼리 파라미터가 누락된 경우 어떻게 동작하는지 테스트합니다. 오류 처리 로직(예: 사용자에게 메시지 표시)이나 대체 경로(예: 기본 화면으로 이동)가 잘 구현되어 있는지 확인합니다.
 * 다양한 안드로이드 버전 및 기기 테스트:
   * OS 버전에 따라 딥 링크 처리나 시스템 동작에 미묘한 차이가 있을 수 있으므로 가능한 여러 API 레벨에서 테스트합니다.
   * 제조사별로 브라우저 동작이나 기본 앱 설정이 달라 링크가 열리는 방식에 영향을 줄 수 있으므로 다양한 기기에서 테스트하는 것이 좋습니다.
 * 경쟁 앱 확인:
   * 테스트 기기에 동일한 사용자 정의 스키마나 URL 패턴을 사용하는 다른 앱이 설치되어 있지 않은지 확인합니다. 경쟁 앱이 있으면 안드로이드 시스템의 앱 선택(Disambiguation) 대화상자가 나타날 수 있습니다. HTTP/S 링크의 경우, 브라우저에서 열리는 대신 앱에서 바로 열리도록 하려면 App Links 설정을 확인합니다.
 * 앱 링크(App Links) 관련 디버깅 (해당 시):
   * 웹사이트의 /.well-known/assetlinks.json 파일이 올바르게 호스팅되고 내용이 정확한지 확인합니다.
   * Google의 생성기 및 테스터 도구나 DigitalAssetLinks.verify() API를 사용하여 검증합니다.
   * adb shell dumpsys package d 명령어로 도메인 인증 상태를 확인합니다.
 * 로깅(Logging) 활용:
   * 딥 링크 수신 및 처리 과정 전반에 걸쳐 상세한 로그를 남기도록 구현합니다. 이는 개발 및 테스트 환경뿐만 아니라 운영 환경에서 발생할 수 있는 문제를 추적하고 원인을 파악하는 데 매우 중요합니다. Android Studio의 Logcat을 적극 활용합니다.
이러한 테스트와 디버깅 과정을 통해 다양한 환경에서 딥 링크가 안정적으로 작동하도록 보장할 수 있습니다.

---



