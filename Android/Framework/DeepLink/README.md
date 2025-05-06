# 딥 링크(Deep Link)를 어떻게 처리하나요?
딥 링크는 사용자가 URL이나 알림과 같은 외부 소스에서 앱 내의 특정 화면이나 기능으로 직접 이동할 수 있게 해줍니다. 딥 링크를 처리하는 것은 AndroidManifest.xml 파일에 이를 정의하고, 해당 액티비티(Activity)나 프래그먼트(Fragment)에서 수신 인텐트(intent)를 처리하는 과정을 포함합니다.

## 1단계: 매니페스트(Manifest)에 딥 링크 정의하기
딥 링크를 활성화하려면, 딥 링크를 처리해야 하는 액티비티에 대해 AndroidManifest.xml 파일에 인텐트 필터(intent filter) 를 선언합니다. 인텐트 필터는 앱이 응답할 URL 구조나 스키마(scheme)를 지정합니다.

```xml
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
```
이 설정은 https://example.com/deepLink와 같은 URL이 MyDeepLinkActivity를 열도록 허용합니다.

## 2단계: 액티비티에서 딥 링크 처리하기
액티비티 내부에서는 수신된 인텐트 데이터를 검색하고 처리하여 적절한 화면으로 이동하거나 특정 동작을 수행합니다.

```kotlin
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
```

## 3단계: 딥 링크 테스트하기
딥 링크를 테스트하려면 아래의 adb 명령어를 사용할 수 있습니다.
```shell
adb shell am start -a android.intent.action.VIEW \
    -d "https://example.com/deepLink?id=123" \
    com.example.myapp # 앱의 패키지 이름
```

이 명령어는 딥 링크를 시뮬레이션하여 앱이 이를 처리하도록 엽니다.

### 추가 고려 사항
 * 사용자 정의 스키마(Custom Schemes): 내부 링크에는 myapp://과 같은 사용자 정의 스키마를 사용할 수 있지만, 더 넓은 호환성을 위해 HTTP(S) URL을 선호하는 것이 좋습니다.
 * 내비게이션(Navigation): 딥 링크 데이터에 기반하여 앱 내의 다른 액티비티나 프래그먼트로 이동하기 위해 인텐트를 사용합니다. Jetpack Navigation 컴포넌트를 사용하면 딥 링크 처리를 더 쉽게 구현할 수 있습니다.
 * 폴백 처리(Fallback Handling): 딥 링크 데이터가 유효하지 않거나 불완전한 경우 앱이 이를 적절히 처리하도록 보장해야 합니다 (예: 기본 화면으로 이동).
 * 앱 링크(App Links): HTTP(S) 딥 링크가 브라우저 대신 앱에서 바로 열리도록 하려면 앱 링크(App Links)를 설정해야 합니다. (서버 측 설정 필요)

## 요약
딥 링크 처리는 AndroidManifest.xml에서 URL 패턴을 정의하고 대상 액티비티에서 데이터를 처리하는 것을 포함합니다. 딥 링크 데이터를 추출하고 해석함으로써 사용자를 앱의 특정 기능이나 콘텐츠로 안내하여 사용자 경험과 참여(engagement)를 향상시킬 수 있습니다.

## Q. 안드로이드에서 딥 링크를 어떻게 테스트할 수 있으며, 다양한 기기와 시나리오에서 올바르게 작동하는지 확인하기 위한 일반적인 디버깅 기법은 무엇인가요?
안드로이드에서 딥 링크(Deep Link)를 테스트하고 다양한 환경에서 올바르게 작동하는지 확인하기 위한 방법과 디버깅 기법은 다음과 같습니다.

### 딥 링크 테스트 방법
 * ADB (Android Debug Bridge) 사용: 개발 중 가장 직접적이고 편리한 방법입니다. 터미널이나 명령 프롬프트에서 아래 명령어를 실행하여 특정 딥 링크 URL로 인텐트를 보내는 것을 시뮬레이션할 수 있습니다.
  ```shell
  adb shell am start -a android.intent.action.VIEW \
    -d "딥링크_URL_입력" 패키지_이름
  ```
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
   `<category android:name="android.intent.category.DEFAULT" /> 와 <category android:name="android.intent.category.BROWSABLE" />` 카테고리가 포함되어 있는지 확인합니다.
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