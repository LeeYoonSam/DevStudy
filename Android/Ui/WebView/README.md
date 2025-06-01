# 웹 페이지는 어떻게 렌더링하나요?

**WebView(웹뷰)** 는 앱 내에서 직접 웹 콘텐츠를 표시하고 상호작용할 수 있게 해주는 다목적 안드로이드 컴포넌트입니다. 애플리케이션에 내장된 미니 브라우저처럼 작동하여, 개발자가 웹 페이지를 렌더링하거나, HTML 콘텐츠를 로드하거나, 심지어 JavaScript를 실행할 수도 있게 합니다. 앱이 실행 중인 기기에서 최신 WebView 기능을 안전하게 활용하려면, [**AndroidX Webkit 라이브러리**](https://developer.android.com/reference/androidx/webkit/package-summary) 사용을 고려하세요. 이 라이브러리는 하위 호환성을 갖는 API를 제공하여 기기의 안드로이드 버전에 관계없이 최신 기능에 접근할 수 있도록 보장합니다.

### 1. WebView 초기화

WebView를 사용하려면 레이아웃 파일에 포함시키거나 프로그래밍 방식으로 생성합니다. 아래는 XML 레이아웃에 WebView를 추가하는 예시입니다.

```xml
<WebView
    android:id="@+id/webView"
    android:layout_width="match_parent"
    android:layout_height="match_parent" />
```

필요한 경우 프로그래밍 방식으로 생성할 수도 있습니다.

**WebView.kt (프로그래밍 방식 생성 예시)**

```kotlin
// Activity 내에서
val webView = WebView(this)
setContentView(webView) // 액티비티의 전체 내용을 웹뷰로 설정
```

-----

### 2. 웹 페이지 로드하기

웹 페이지를 로드하려면 WebView 인스턴스의 `loadUrl()` 메서드를 사용합니다. 만약 페이지가 인터넷 접근을 필요로 한다면 안드로이드 매니페스트에 필요한 권한을 활성화해야 합니다.

**WebView.kt (웹 페이지 로드 예시)**

```kotlin
val webView: WebView = findViewById(R.id.webView)
webView.loadUrl("https://www.example.com")
```

인터넷 접근을 허용하려면 `AndroidManifest.xml`에 다음 권한을 추가하세요.

```xml
<uses-permission android:name="android.permission.INTERNET" />
```

-----

### 3. JavaScript 활성화하기

만약 웹 콘텐츠가 JavaScript를 필요로 한다면, `WebSettings`를 수정하여 활성화합니다.

**WebView.kt (JavaScript 활성화 예시)**

```kotlin
val webView: WebView = findViewById(R.id.webView) // 이미 초기화되었다고 가정
val webSettings = webView.settings
webSettings.javaScriptEnabled = true
```

-----

### 4. WebView 동작 사용자 정의하기

WebView는 이벤트를 처리하고 동작을 사용자 정의하는 메서드들을 제공합니다.

#### 4.1. 페이지 내비게이션 가로채기

`WebViewClient`를 사용하여 외부 브라우저에서 페이지를 여는 대신 WebView 내에서 페이지 내비게이션을 처리합니다.

**Navigation.kt (페이지 내비게이션 처리 예시)**

```kotlin
webView.webViewClient = object : WebViewClient() {
    override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
        view.loadUrl(url) // 현재 WebView에서 URL 로드
        return true // WebView가 URL 로딩을 처리했음을 알림
    }
    // 최신 API 레벨에서는 shouldOverrideUrlLoading(WebView view, WebResourceRequest request) 사용 권장
}
```

#### 4.2. 다운로드 처리

`DownloadListener`를 사용하여 WebView에 의해 시작된 파일 다운로드를 관리합니다.

**DownloadListener.kt (다운로드 리스너 예시)**

```kotlin
webView.setDownloadListener { url, userAgent, contentDisposition, mimeType, contentLength ->
    // 파일 다운로드 처리 로직 (예: DownloadManager 사용)
    Log.d("WebViewDownload", "URL: $url, MimeType: $mimeType")
    // val request = DownloadManager.Request(Uri.parse(url))
    // ... DownloadManager 설정 ...
    // val dm = getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
    // dm.enqueue(request)
}
```

#### 4.3. WebView에서 JavaScript 실행하기

`evaluateJavascript` 또는 `loadUrl("javascript:...")`를 사용하여 JavaScript 코드를 주입합니다.

**JavaScript.kt (JavaScript 실행 예시)**

```kotlin
// API 19 이상 권장
webView.evaluateJavascript("document.body.style.backgroundColor = 'red';") { result ->
    Log.d("WebView", "JavaScript 실행 결과: $result")
}

// 또는 이전 방식
// webView.loadUrl("javascript:document.body.style.backgroundColor = 'red';")
```

-----

### 5. JavaScript를 안드로이드 코드에 바인딩하기: 종합 가이드

JavaScript와 안드로이드 코드를 통합하면 클라이언트 측 스크립트와 안드로이드의 네이티브 기능 간의 원활한 상호작용을 허용하여 하이브리드 웹 애플리케이션을 향상시킬 수 있습니다. 이는 특히 안드로이드 앱 내의 WebView에서 실행되는 웹 애플리케이션이 JavaScript를 사용하여 안드로이드 특정 기능을 활용할 수 있게 하는 데 유용합니다. 예를 들어, JavaScript의 `alert()` 함수에 의존하는 대신 네이티브 안드로이드 다이얼로그나 토스트 메시지를 트리거할 수 있습니다.

이러한 상호작용을 구축하려면 `addJavascriptInterface()` 메서드를 사용합니다. 이 메서드는 자바 객체를 WebView의 JavaScript 컨텍스트에 바인딩하여, 인터페이스 이름을 지정함으로써 JavaScript를 통해 해당 객체의 메서드에 접근할 수 있게 만듭니다.

다음은 JavaScript를 안드로이드에 바인딩하는 예시입니다.

**WebAppInterface.kt (자바스크립트 인터페이스 예시)**

```kotlin
// 인터페이스 정의 및 WebView에 바인딩
class WebAppInterface(private val context: Context) {

    // JavaScript에 노출할 메서드
    @JavascriptInterface // 이 어노테이션 필수
    fun showToast(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }
}

// WebView 설정 및 인터페이스 바인딩 (Activity 또는 Fragment 내)
// val webView: WebView = findViewById(R.id.webView) // 또는 R.id.webview
// webView.settings.javaScriptEnabled = true
// webView.addJavascriptInterface(WebAppInterface(this), "Android") // "Android"는 JS에서 사용할 이름
```

이 예시에서 `WebAppInterface` 클래스는 `@JavascriptInterface` 어노테이션이 붙은 `showToast` 메서드를 노출합니다. `addJavascriptInterface()` 메서드는 이 인터페이스를 "Android"라는 이름으로 WebView에 바인딩합니다. 이제 WebView에서 실행되는 JavaScript가 이 메서드를 호출할 수 있습니다.

HTML 측에서는 다음 스크립트가 JavaScript에서 `showToast` 메서드를 호출하는 방법을 보여줍니다.

```html
<!DOCTYPE html>
<html>
<head>
    <title>WebView 예시</title>
</head>
<body>
    <button onclick="callAndroidFunction()">여기를 클릭하세요</button>
    <script type="text/javascript">
        function callAndroidFunction() {
            // "Android"는 addJavascriptInterface에서 지정한 이름
            Android.showToast("자바스크립트에서 보낸 메시지!");
        }
    </script></body>
</html>
```

버튼을 클릭하면 JavaScript 함수 `callAndroidFunction()`이 안드로이드 인터페이스의 `showToast` 메서드를 호출하여 네이티브 토스트 메시지를 표시합니다.

`addJavascriptInterface()`는 유용한 기능이지만 상당한 **보안 위험**을 수반합니다. WebView가 신뢰할 수 없거나 동적인 HTML 콘텐츠를 로드하는 경우, 공격자가 악성 JavaScript를 주입하여 노출된 인터페이스를 악용하고 의도하지 않은 안드로이드 코드를 실행할 가능성이 있습니다.

-----

### 6. 보안 고려 사항

  * JavaScript는 보안 위험에 앱을 노출시킬 수 있으므로 **필요하지 않으면 활성화하지 마세요.**
  * 파일에 대한 무단 접근을 방지하기 위해 `setAllowFileAccess()` 및 `setAllowFileAccessFromFileURLs()`를 신중하게 사용하세요.
  * 사이트 간 스크립팅(XSS) 또는 URL 스푸핑과 같은 공격을 방지하기 위해 항상 사용자 입력을 유효성 검사하고 URL을 살균(sanitize)하세요.
  * `@JavascriptInterface`를 통해 노출된 메서드가 보안 취약점을 야기하지 않도록 확인하세요.

-----

### 요약

WebView는 안드로이드 애플리케이션에서 웹 콘텐츠를 렌더링하기 위한 기본적인 구성 요소입니다. `WebViewClient`를 사용하여 동작을 사용자 정의하고 필요할 때 JavaScript와 같은 기능을 활성화함으로써 사용자에게 원활한 경험을 제공할 수 있습니다. 그러나 앱에 웹 콘텐츠를 통합할 때는 항상 보안 및 성능에 미치는 영향을 고려해야 합니다. 추가적인 세부 정보는 [WebView에서 웹 앱 빌드하기](https://developer.android.com/develop/ui/views/layout/webapps/webview#kotlin) 가이드를 참조하세요.

-----

## Q. 사용자가 외부 링크를 클릭할 때 앱을 벗어나는 것을 방지하기 위해 WebView 내비게이션을 효과적으로 어떻게 처리할 수 있나요?

## WebView 내비게이션 효과적 처리: 앱 이탈 방지 및 외부 링크 관리

사용자가 `WebView` 내의 링크를 클릭했을 때 앱을 벗어나 외부 브라우저로 이동하는 것을 방지하고 내비게이션 흐름을 효과적으로 제어하려면, **`WebViewClient`** 를 사용하고 주요 메서드를 재정의(override)하는 것이 핵심입니다.

---
### 1. 핵심 메커니즘: `WebViewClient`와 `shouldOverrideUrlLoading()`

기본적으로 `WebView` 내에서 링크를 클릭하면, 안드로이드 시스템은 해당 URL을 처리할 수 있는 앱(일반적으로 기기의 기본 웹 브라우저)을 찾아 실행하려고 합니다. 이 동작을 변경하여 모든 탐색을 `WebView` 내에서 처리하거나 특정 조건에 따라 다르게 동작하도록 하려면 사용자 정의 `WebViewClient`를 설정해야 합니다.

가장 중요한 메서드는 `shouldOverrideUrlLoading()`입니다.

* **`shouldOverrideUrlLoading(WebView view, String url)`:** (API 레벨 24 미만에서 주로 사용)
* **`shouldOverrideUrlLoading(WebView view, WebResourceRequest request)`:** (API 레벨 24 이상에서 권장)

이 메서드는 `WebView` 내에서 새로운 URL이 로드되려고 할 때 (예: 사용자가 링크를 클릭했을 때, 페이지 리디렉션 발생 시) 호출됩니다. 이 메서드의 **반환 값**이 매우 중요합니다.

* **`true`를 반환하면:** "애플리케이션(개발자 코드)이 URL 로딩을 직접 처리했습니다."라는 의미입니다. 따라서 `WebView`는 해당 URL에 대한 기본 로딩 동작을 수행하지 않습니다. 개발자는 이 메서드 내에서 `view.loadUrl(request.getUrl().toString())`과 같이 명시적으로 URL을 로드하거나, 다른 방식(예: 외부 브라우저로 열기)으로 처리해야 합니다.
* **`false`를 반환하면:** "애플리케이션이 URL 로딩을 처리하지 않았습니다."라는 의미입니다. `WebView`는 기본 동작에 따라 URL을 로드합니다. (대부분의 경우 현재 `WebView`에서 로드되지만, 특정 스킴이나 새 창 요청 등은 외부 앱을 실행할 수 있습니다.)

---
### 2. WebView 내에서 모든 링크를 처리하여 앱 이탈 방지하기

모든 링크 클릭이 현재 `WebView` 내에서 열리도록 하여 사용자가 앱을 벗어나지 않게 하려면, `shouldOverrideUrlLoading()` 메서드 내에서 전달받은 URL을 현재 `WebView`에 다시 로드하고 `true`를 반환합니다.

```kotlin
// Activity 또는 Fragment 내에서 WebView 설정
val webView: WebView = findViewById(R.id.myWebView)
webView.settings.javaScriptEnabled = true // 필요에 따라 JavaScript 활성화

webView.webViewClient = object : WebViewClient() {
    // API 레벨 24 이상용
    override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
        val url = request?.url?.toString()
        if (url != null) {
            view?.loadUrl(url) // 현재 WebView에서 URL을 로드합니다.
            return true       // URL 로딩을 앱이 처리했음을 시스템에 알립니다.
        }
        return false // URL이 null인 경우 기본 WebView 동작에 맡깁니다.
    }

    // API 레벨 24 미만용 (하위 호환성을 위해 함께 구현)
    @Deprecated("Deprecated in Java")
    override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
        if (url != null) {
            view?.loadUrl(url)
            return true
        }
        return false
    }
}

// 초기 페이지 로드
webView.loadUrl("https://initial-page.com")
```

이 설정을 통해 `WebView` 내의 모든 링크 클릭은 외부 브라우저를 실행하지 않고 현재 `WebView` 내에서 새로운 페이지로 이동하게 됩니다.

---
### 3. (선택적) 특정 외부 링크만 외부 브라우저로 열기 (조건부 처리)

모든 링크를 `WebView` 내에서 처리하는 대신, 특정 도메인(예: 현재 앱과 관련된 도메인)의 링크는 `WebView` 내에서 열고, 그 외의 외부 도메인 링크는 사용자의 기본 웹 브라우저로 열도록 조건부 로직을 구현할 수 있습니다.

```kotlin
webView.webViewClient = object : WebViewClient() {
    override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
        val url = request?.url?.toString()
        if (url != null) {
            val uri = Uri.parse(url)
            if (uri.host == "www.my-app-domain.com" || uri.host == "internal.my-app-domain.com") {
                // 우리 앱 관련 도메인이면 WebView 내에서 로드
                view?.loadUrl(url)
                return true // 앱이 처리함
            } else {
                // 외부 도메인이면 외부 브라우저로 열기 시도
                try {
                    val intent = Intent(Intent.ACTION_VIEW, uri)
                    view?.context?.startActivity(intent)
                    return true // 앱이 처리함 (외부 앱 실행)
                } catch (e: ActivityNotFoundException) {
                    // 해당 URL을 처리할 수 있는 앱이 없는 경우
                    Toast.makeText(view?.context, "연결된 앱을 찾을 수 없습니다.", Toast.LENGTH_LONG).show()
                    return true // 오류도 앱이 처리한 것으로 간주
                }
            }
        }
        return false // URL이 null이거나 기타 경우 WebView 기본 동작
    }
    // API 24 미만용 shouldOverrideUrlLoading(view, url)도 유사하게 구현
}
```

---
### 4. 추가 고려 사항

* **뒤로 가기 버튼 처리:** 모든 링크를 `WebView` 내에서 로드하면 `WebView` 자체적으로 방문 기록(history) 스택을 갖게 됩니다. 사용자가 기기의 뒤로 가기 버튼을 눌렀을 때, `WebView`의 이전 페이지로 이동하게 하려면 액티비티의 `onBackPressed()` 메서드를 재정의해야 할 수 있습니다.
    ```kotlin
    override fun onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack() // WebView가 뒤로 갈 페이지가 있으면 이전 페이지로 이동
        } else {
            super.onBackPressed() // 없으면 액티비티의 기본 뒤로가기 동작 수행
        }
    }
    ```

* **로딩 진행 상태 표시:** 페이지가 로드되는 동안 사용자에게 시각적인 피드백을 주기 위해 `WebChromeClient`의 `onProgressChanged(WebView view, int newProgress)` 콜백을 사용하여 로딩 진행률(ProgressBar 등)을 표시할 수 있습니다.

* **오류 처리:** `WebViewClient`의 `onReceivedError()` (네트워크 오류 등) 또는 `onReceivedHttpError()` (HTTP 오류 코드) 콜백을 재정의하여 페이지 로딩 중 발생하는 오류를 사용자 친화적으로 처리할 수 있습니다.

---
### 5. 결론

`WebViewClient`의 `shouldOverrideUrlLoading()` 메서드를 올바르게 구현하는 것은 `WebView` 내비게이션을 효과적으로 제어하고 사용자가 의도치 않게 앱을 벗어나는 것을 방지하는 핵심입니다. 이를 통해 앱 내에서 웹 콘텐츠를 탐색하는 경험을 더욱 매끄럽고 통합적으로 만들 수 있습니다.