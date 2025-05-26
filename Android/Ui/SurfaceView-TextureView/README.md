# SurfaceView와 TextureView는 언제 각각 사용해야 하나요?

**SurfaceView(서피스뷰)** 는 렌더링이 **별도의 스레드에서 처리**되는 시나리오를 위해 설계된, **전용 그리기 표면(dedicated drawing surface)** 을 제공하는 특수한 뷰(View)입니다. 이는 비디오 재생, 사용자 정의 그래픽 렌더링 또는 성능이 매우 중요한 게이밍과 같은 작업에 일반적으로 사용됩니다. SurfaceView의 핵심 특징은 메인 UI 스레드 외부에 별도의 서피스를 생성하여, 다른 UI 작업을 차단하지 않고 효율적인 렌더링을 가능하게 한다는 것입니다.

서피스는 `SurfaceHolder` 콜백 메서드를 통해 생성되고 관리되며, 필요에 따라 렌더링을 시작하고 중지할 수 있습니다. 예를 들어, 저수준 API를 사용하여 비디오를 재생하거나 게임 루프에서 지속적으로 그래픽을 그리는 데 SurfaceView를 사용할 수 있습니다.

```kotlin
class CustomSurfaceView(context: Context) : SurfaceView(context), SurfaceHolder.Callback {
    init {
        holder.addCallback(this) // SurfaceHolder에 콜백 등록
    }

    override fun surfaceCreated(holder: SurfaceHolder) {
        // 여기서 렌더링 또는 그리기 시작
        // 예: 별도의 렌더링 스레드 시작
    }

    override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {
        // 서피스의 변경 사항 처리 (크기 변경 등)
    }

    override fun surfaceDestroyed(holder: SurfaceHolder) {
        // 여기서 렌더링 중지 또는 리소스 해제
        // 예: 렌더링 스레드 안전하게 종료
    }
}
```

SurfaceView는 연속적인 렌더링에는 효율적이지만, 크기 조절(scaling)이나 회전과 같은 변환에는 제한이 있어 고성능 사용 사례에는 적합하지만 동적인 UI 상호작용에는 유연성이 떨어집니다.

반면에, **TextureView(텍스처뷰)** 는 콘텐츠를 오프스크린(offscreen)에 렌더링하는 또 다른 방법을 제공하지만, SurfaceView와 달리 **UI 계층 구조에 원활하게 통합**됩니다. 이는 TextureView가 변환되거나 애니메이션 처리될 수 있음을 의미하며, 회전, 크기 조절, 알파 블렌딩과 같은 기능을 허용합니다. 실시간 카메라 미리보기를 표시하거나 사용자 정의 변환을 적용하여 비디오를 렌더링하는 작업에 자주 사용됩니다.

SurfaceView와 달리 TextureView는 **메인 스레드에서 작동**합니다. 이로 인해 연속적인 렌더링에는 약간 덜 효율적일 수 있지만, 다른 UI 컴포넌트와의 더 나은 통합을 가능하게 하고 실시간 변환을 지원합니다.

```kotlin
class CustomTextureView(context: Context) : TextureView(context), TextureView.SurfaceTextureListener {
    init {
        surfaceTextureListener = this // SurfaceTextureListener 등록
    }

    override fun onSurfaceTextureAvailable(surface: SurfaceTexture, width: Int, height: Int) {
        // 여기서 렌더링 시작 또는 SurfaceTexture 사용
        // 예: 이 SurfaceTexture를 사용하여 카메라 미리보기 시작 또는 OpenGL 렌더링
    }

    override fun onSurfaceTextureSizeChanged(surface: SurfaceTexture, width: Int, height: Int) {
        // 서피스 크기 변경 처리
    }

    override fun onSurfaceTextureDestroyed(surface: SurfaceTexture): Boolean {
        // 리소스 해제 또는 렌더링 중지
        return true // 서피스 텍스처가 해제되었음을 시스템에 알림
    }

    override fun onSurfaceTextureUpdated(surface: SurfaceTexture) {
        // 서피스 텍스처 업데이트 처리 (예: 새 프레임 도착 시)
    }
}
```

TextureView는 비디오 스트림을 애니메이션 처리하거나 UI 내에서 콘텐츠를 동적으로 혼합하는 등 시각적 변환이 필요한 사용 사례에 특히 유용합니다.

### SurfaceView와 TextureView의 차이점

주요 차이점은 이러한 컴포넌트가 렌더링 및 UI 통합을 처리하는 방식에 있습니다.
**SurfaceView**는 별도의 스레드에서 작동하므로 비디오 재생이나 게이밍과 같은 연속적인 렌더링 작업에 효율적입니다. 또한 렌더링을 위해 별도의 윈도우를 생성하여 성능을 보장하지만 변환되거나 애니메이션 처리되는 능력은 제한됩니다.
반대로, **TextureView**는 다른 UI 컴포넌트와 동일한 윈도우를 공유하므로 크기 조절, 회전 또는 애니메이션 처리가 가능하여 UI 관련 사용 사례에 더 유연합니다. 그러나 메인 스레드에서 작동하기 때문에 고빈도 렌더링이 필요한 작업에는 그다지 효율적이지 않을 수 있습니다.

| 특징 | SurfaceView | TextureView |
| :--- | :--- | :--- |
| **렌더링 스레드** | 별도 스레드 | 메인 스레드 (UI 스레드) |
| **윈도우** | 자체 별도 윈도우 사용 (UI 계층 위에 떠 있음) | 앱의 UI 윈도우 공유 (일반 뷰처럼 동작) |
| **변환/애니메이션** | 제한적 (거의 불가능) | 가능 (크기 조절, 회전, 알파 등 일반 뷰처럼 처리) |
| **성능 (고빈도 렌더링)** | 우수 | 상대적으로 떨어짐 |
| **UI 통합성** | 떨어짐 (다른 뷰 위에 항상 그려짐) | 우수 (다른 뷰와 겹치거나 투명도 적용 용이) |
| **주요 사용 사례** | 게임, 고성능 비디오 재생, 카메라 미리보기(단순 표시) | UI 효과가 포함된 비디오 재생, 애니메이션 처리된 카메라 미리보기 |


### 요약

**SurfaceView**는 게이밍이나 연속적인 비디오 렌더링과 같이 **성능이 가장 중요한 시나리오**에 가장 적합합니다. 반면에, **TextureView**는 비디오 애니메이션 처리나 실시간 카메라 피드 표시와 같이 **원활한 UI 통합 및 시각적 변환이 필요한 사용 사례**에 더 적합합니다. 둘 중 어느 것을 선택할지는 애플리케이션이 성능을 우선시하는지 아니면 UI 유연성을 우선시하는지에 따라 달라집니다.


## Q. SurfaceView의 생명주기를 적절히 관리하여 효율적인 리소스 관리 및 메모리 누수 방지를 어떻게 보장하나요?

`SurfaceView`는 별도의 스레드에서 고성능 그래픽 렌더링(예: 게임, 비디오 재생, 카메라 미리보기)을 위해 설계된 특별한 뷰입니다. 일반 뷰와 달리 렌더링을 위한 자체적인 서피스(Surface)를 가지므로, 이 서피스의 생명주기를 올바르게 관리하는 것이 효율적인 리소스 사용과 메모리 누수 방지에 매우 중요합니다.

핵심은 **`SurfaceHolder.Callback` 인터페이스**를 구현하고, 그 콜백 메서드 내에서 리소스 초기화 및 해제 로직을 정확히 처리하는 것입니다.

### 1. `SurfaceHolder.Callback` 구현 및 등록

`SurfaceView`의 서피스 생명주기 이벤트를 받으려면, `SurfaceHolder.Callback` 인터페이스를 구현하고 `SurfaceView`의 `SurfaceHolder`에 콜백을 등록해야 합니다.

```kotlin
class MyCustomSurfaceView(context: Context, attrs: AttributeSet? = null) : SurfaceView(context, attrs), SurfaceHolder.Callback {

    private var renderingThread: RenderingThread? = null // 렌더링 스레드 (예시)

    init {
        holder.addCallback(this) // SurfaceHolder에 콜백 리스너 등록
    }

    // ... SurfaceHolder.Callback 메서드 구현 ...

    // 예시 렌더링 스레드 클래스 (내부 또는 별도 파일)
    inner class RenderingThread(private val surfaceHolder: SurfaceHolder) : Thread() {
        @Volatile var isRunning = false // 스레드 실행 상태 플래그
        override fun run() {
            while (isRunning) {
                var canvas: Canvas? = null
                try {
                    canvas = surfaceHolder.lockCanvas() // 캔버스 잠금
                    if (canvas != null) {
                        synchronized(surfaceHolder) {
                            // 여기에 그리기 로직 구현
                            drawContent(canvas)
                        }
                    }
                } finally {
                    if (canvas != null) {
                        surfaceHolder.unlockCanvasAndPost(canvas) // 캔버스 잠금 해제 및 화면에 표시
                    }
                }
                // 프레임 속도 제어 (예: 60FPS 목표 시 약 16ms 대기)
                try {
                    sleep(16)
                } catch (e: InterruptedException) {
                    // 스레드 중단 처리
                }
            }
        }
        fun drawContent(canvas: Canvas) {
            // 예: 화면을 파란색으로 칠하기
            canvas.drawColor(Color.BLUE)
            // 실제 그리기 작업 수행
        }
    }
}
```

### 2. `SurfaceHolder.Callback` 메서드별 리소스 관리

#### 2.1. `surfaceCreated(SurfaceHolder holder)`

* **호출 시점:** 서피스가 처음 생성되어 그리기에 사용할 준비가 되었을 때 호출됩니다.
* **주요 작업 (리소스 초기화):**
    * **렌더링 스레드 시작:** 만약 별도의 렌더링 스레드를 사용한다면 (SurfaceView의 일반적인 패턴), 이 시점에서 스레드를 생성하고 시작합니다.
        ```kotlin
        override fun surfaceCreated(holder: SurfaceHolder) {
            Log.d("SurfaceViewLifecycle", "surfaceCreated")
            renderingThread = RenderingThread(holder)
            renderingThread?.isRunning = true
            renderingThread?.start()
        }
        ```
    * **그리기 관련 리소스 초기화:** 유효한 서피스가 필요한 리소스(예: OpenGL의 EGL 컨텍스트, 미디어 플레이어 설정)나 드로잉 파라미터 등을 초기화합니다.

#### 2.2. `surfaceChanged(SurfaceHolder holder, int format, int width, int height)`

* **호출 시점:** `surfaceCreated()` 직후, 그리고 서피스의 크기나 포맷이 변경될 때마다 (예: 화면 회전, 레이아웃 변경) 호출됩니다.
* **주요 작업 (리소스 재설정):**
    * 전달받은 새로운 `width`와 `height`에 맞춰 렌더링 파라미터를 업데이트합니다.
    * OpenGL 사용 시 뷰포트(viewport)를 업데이트합니다.
    * 크기 의존적인 그리기 로직을 조정합니다.
    * **주의:** 이 메서드에서 렌더링 스레드를 반복적으로 시작하거나 중지하는 것은 피해야 합니다. 스레드는 유지하되, 그리기 영역의 크기만 업데이트하는 것이 좋습니다.
        ```kotlin
        override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {
            Log.d("SurfaceViewLifecycle", "surfaceChanged: format=$format, width=$width, height=$height")
            // 렌더링 스레드에 크기 변경 알림 (필요시)
            // renderingThread?.updateSurfaceDimensions(width, height)
        }
        ```

#### 2.3. `surfaceDestroyed(SurfaceHolder holder)`

* **호출 시점:** 서피스가 소멸되기 직전에 호출됩니다. 이는 `SurfaceView`가 뷰 계층에서 제거되거나, 이를 포함하는 액티비티/프래그먼트가 `onPause()`, `onStop()`, `onDestroy()` 상태로 전환될 때 발생할 수 있습니다.
* **주요 작업 (리소스 해제 - 메모리 누수 방지의 핵심):**
    * **렌더링 스레드 안전하게 중지 및 조인(join):** `surfaceCreated()`에서 렌더링 스레드를 시작했다면, 이 시점에서 **반드시** 해당 스레드를 안전하게 중지시키고 완료될 때까지 기다려야 합니다 (`thread.join()`). 그렇지 않으면 스레드가 유효하지 않은 서피스에 그리려고 시도하여 충돌이 발생하거나, 스레드 리소스가 계속 남아 메모리 누수를 유발할 수 있습니다.
        ```kotlin
        override fun surfaceDestroyed(holder: SurfaceHolder) {
            Log.d("SurfaceViewLifecycle", "surfaceDestroyed")
            var retry = true
            renderingThread?.isRunning = false // 스레드 루프 중지 플래그 설정
            while (retry) {
                try {
                    renderingThread?.join() // 스레드가 완전히 종료될 때까지 대기
                    retry = false
                } catch (e: InterruptedException) {
                    // 조인 중 인터럽트 발생 시 재시도 로직 (필요에 따라)
                    Log.e("SurfaceViewLifecycle", "Interrupted while joining rendering thread", e)
                }
            }
            renderingThread = null
        }
        ```
    * **네이티브 리소스 해제:** 서피스와 관련된 네이티브 리소스(예: EGL 컨텍스트, 미디어 플레이어 리소스)를 해제합니다.
    * **기타 그리기 관련 리소스 해제:** 대형 비트맵, 셰이더 등 직접 관리하는 리소스가 있다면 해제합니다.
    * **참조 정리:** 다른 곳에서 `Surface`나 `SurfaceHolder`에 대한 참조를 저장하고 있다면, 해당 참조를 정리하여 가비지 컬렉션이 가능하도록 합니다.

### 3. 액티비티/프래그먼트 생명주기와의 연동

`SurfaceView`의 서피스 생명주기는 뷰의 윈도우 가시성과 밀접하게 연관되어 있으며, 이는 호스팅하는 액티비티나 프래그먼트의 생명주기에 영향을 받습니다.

* **`Activity.onPause()` / `Fragment.onPause()`:**
    * 만약 렌더링이 지속적인 작업(게임, 애니메이션 등)이라면, 앱이 포그라운드에 있지 않을 때 배터리 및 리소스 절약을 위해 렌더링 스레드를 **일시 중지**하는 것이 좋습니다. `surfaceDestroyed()`가 즉시 호출되지 않을 수도 있지만, 시스템 리소스를 선제적으로 관리하는 좋은 습관입니다.
    * 렌더링 스레드 내부에 일시 중지 플래그를 두고, `onPause()`에서 이 플래그를 설정하여 스레드가 대기하도록 할 수 있습니다.

* **`Activity.onResume()` / `Fragment.onResume()`:**
    * `onPause()`에서 렌더링을 일시 중지했다면, 이 시점에서 다시 **재개**합니다. 서피스가 이미 생성되어 있거나, `surfaceCreated()`와 `surfaceChanged()`가 다시 호출되어 준비될 것입니다.
    * 렌더링 스레드의 일시 중지 플래그를 해제하여 다시 그리도록 합니다.

### 4. 일반적인 함정 피하기

* **`surfaceDestroyed()`에서 렌더링 스레드를 중지하지 않는 것:** 소멸된 서피스에 그리려고 시도하여 발생하는 충돌의 주된 원인이자, 스레드가 계속 실행되어 발생하는 리소스 누수의 원인입니다.
* **잘못된 스레드에서 서피스 접근:** `SurfaceHolder.lockCanvas()`를 통해 얻은 `Canvas`에 대한 모든 그리기 작업은 해당 렌더링 스레드 내에서 이루어져야 합니다.
* **렌더링 스레드 내 컨텍스트 또는 다른 리소스 누수:** 렌더링 스레드가 `SurfaceView`가 소멸된 후에도 살아남아야 하는 객체(예: 액티비티 컨텍스트)에 대한 강한 참조를 계속 가지고 있지 않도록 주의해야 합니다. 필요한 경우 `WeakReference`를 사용하거나, 스레드 종료 시 명확하게 참조를 정리해야 합니다.

이러한 원칙들을 따르면 `SurfaceView`를 사용하면서 리소스를 효율적으로 관리하고 메모리 누수 없이 안정적인 고성능 그래픽 렌더링을 구현할 수 있습니다.


## Q. 회전 및 크기 조절과 같은 변환이 적용된 실시간 카메라 미리보기를 표시해야 하는 요구 사항이 주어졌을 때, SurfaceView와 TextureView 중 어떤 컴포넌트를 선택하시겠습니까? 구현 시 고려 사항과 함께 선택을 정당화해 주세요.

실시간 카메라 미리보기에 회전 및 크기 조절과 같은 변환을 적용해야 하는 요구 사항이 있다면, 저는 **`TextureView`를 선택하겠습니다.**

### 선택 이유 및 정당화

가장 결정적인 이유는 **변환(Transformation)의 용이성** 때문입니다.

1.  **`TextureView`의 장점 (변환 및 UI 통합):**
    * `TextureView`는 일반적인 `View`처럼 동작하며, 안드로이드의 표준 뷰 계층 구조에 완벽하게 통합됩니다.
    * 이는 `TextureView`에 `setRotation()`, `setScaleX()`, `setScaleY()`, `setAlpha()`, `setTranslationX()` 등과 같은 `View`의 모든 표준 변환 메서드를 직접 적용할 수 있음을 의미합니다. 애니메이션 프레임워크와도 원활하게 작동합니다.
    * 카메라 미리보기의 내용을 회전시키거나, 특정 비율로 확대/축소하거나, UI 내 다른 요소들과 자연스럽게 겹치게 하는 등의 시각적 처리가 매우 쉽고 직관적입니다.

2.  **`SurfaceView`의 한계점 (변환 및 UI 통합):**
    * `SurfaceView`는 성능을 위해 자체적인 별도의 윈도우(Surface)에 직접 렌더링합니다. 이 서피스는 앱의 뷰 계층 구조의 윈도우 "위에 떠 있거나(overlay)" "아래에 뚫려있는(hole-punching)" 형태로 존재합니다.
    * 이러한 구조 때문에 `SurfaceView` 자체에 표준 `View` 변환(회전, 크기 조절 등)을 적용해도 그 내용물(카메라 미리보기)에는 직접적으로, 그리고 기대하는 방식으로 적용되지 않습니다. `SurfaceView`의 *컨테이너*는 변환될 수 있지만, 서피스 *콘텐츠*는 뷰 계층의 그리기 과정에 일반 뷰처럼 참여하지 않습니다.
    * 물론 카메라 데이터를 받아서 OpenGL ES 등을 사용하여 `SurfaceView`에 그릴 때 변환을 직접 구현할 수는 있지만, 이는 `TextureView`를 사용하는 것보다 훨씬 복잡합니다.

결론적으로, 실시간 카메라 미리보기의 **고성능 렌더링** 자체는 `SurfaceView`가 약간의 이점을 가질 수 있지만 (별도 스레드에서 UI 스레드와 독립적으로 렌더링 가능), 요구사항에 **회전 및 크기 조절과 같은 명시적인 뷰 변환**이 포함되어 있다면, `TextureView`가 훨씬 적합하고 구현이 용이합니다. `TextureView`는 약간의 렌더링 성능 오버헤드(UI 스레드에서 최종 컴포지션)를 감수하는 대신 뛰어난 UI 통합성과 변환 유연성을 제공합니다.

### 구현 시 고려 사항 (`TextureView` 선택 시)

`TextureView`를 사용하여 변환이 적용된 실시간 카메라 미리보기를 구현할 때 다음 사항들을 고려해야 합니다.

1.  **`TextureView.SurfaceTextureListener` 구현:**
    `TextureView`의 서피스 텍스처 생명주기 이벤트를 처리하기 위해 이 리스너를 구현해야 합니다.
    * **`onSurfaceTextureAvailable(surface: SurfaceTexture, width: Int, height: Int)`:**
        * `SurfaceTexture`가 사용 가능해지면 호출됩니다.
        * 이 `SurfaceTexture`로부터 `Surface` 객체를 생성합니다.
        * 이 `Surface`를 카메라 API(CameraX, Camera2 등)에 전달하여 카메라 미리보기 출력을 설정하고 미리보기를 시작합니다.
        * 필요한 초기 변환(예: 디바이스 방향에 맞춘 회전)을 `TextureView`에 적용할 수 있습니다.
    * **`onSurfaceTextureSizeChanged(surface: SurfaceTexture, width: Int, height: Int)`:**
        * `TextureView`의 크기가 변경될 때 호출됩니다.
        * 카메라 미리보기의 해상도나 종횡비를 업데이트해야 할 수 있습니다.
        * 크기 변경에 따라 변환(예: 스케일링)을 재조정해야 할 수 있습니다.
    * **`onSurfaceTextureDestroyed(surface: SurfaceTexture): Boolean`:**
        * `SurfaceTexture`가 소멸되기 직전에 호출됩니다.
        * **매우 중요:** 이 시점에서 반드시 카메라 미리보기를 중지하고, 카메라 리소스를 **반드시 해제(release)**해야 합니다. 그렇지 않으면 다른 앱이나 이후 앱 실행 시 카메라를 사용할 수 없는 심각한 오류가 발생합니다.
        * `true`를 반환하여 `SurfaceTexture`가 시스템에 의해 해제될 수 있음을 알려야 합니다.
    * **`onSurfaceTextureUpdated(surface: SurfaceTexture)`:**
        * `SurfaceTexture`의 내용이 업데이트될 때(즉, 새 카메라 프레임이 도착했을 때) 호출됩니다. 대부분의 경우 카메라 API가 직접 `SurfaceTexture`에 렌더링하므로 이 콜백에서 특별한 작업을 할 필요는 없지만, 추가적인 프레임별 처리가 필요하다면 사용할 수 있습니다.

2.  **카메라 API 선택:**
    * **CameraX:** 권장되는 최신 Jetpack 라이브러리로, 사용하기 쉽고 생명주기 관리가 용이하며 많은 일반적인 작업을 단순화해줍니다. `TextureView`의 `SurfaceTexture`로부터 `SurfaceProvider`를 쉽게 얻어 사용할 수 있습니다.
    * **Camera2 API:** 더 세밀한 제어가 필요할 때 사용합니다. CameraX보다 복잡합니다.
    * (레거시 `Camera` API는 오래된 기기 지원이 필요한 극히 드문 경우를 제외하고는 권장되지 않습니다.)

3.  **스레딩(Threading):**
    * 카메라 초기화, 설정, 미리보기 시작/중지, 사진 촬영과 같은 카메라 제어 작업은 잠재적으로 시간이 오래 걸릴 수 있으므로 **백그라운드 스레드**에서 처리하여 ANR(Application Not Responding)을 방지해야 합니다.
    * `TextureView`에 대한 변환(`setRotation`, `setScaleX` 등)은 **UI 스레드**에서 호출해야 합니다.
    * 카메라로부터의 이벤트(예: 사진 촬영 완료)는 백그라운드 스레드에서 발생할 수 있으므로, 결과를 UI 스레드로 전달하여 UI를 업데이트해야 합니다 (예: `Handler`, 코루틴의 `withContext(Dispatchers.Main)` 사용).

4.  **변환 적용:**
    * **회전:** `textureView.rotation = 90f`와 같이 직접 설정하거나, `textureView.animate().rotation(90f).start()`를 사용하여 애니메이션 효과를 줄 수 있습니다. 기기 방향과 카메라 센서 방향을 고려하여 미리보기 화면이 올바르게 보이도록 회전 값을 설정해야 합니다.
    * **크기 조절:** `textureView.scaleX = 1.5f`, `textureView.scaleY = 1.5f` 등으로 설정합니다. 특정 지점을 중심으로 크기를 조절하려면 `textureView.pivotX`, `textureView.pivotY`를 설정합니다.

5.  **성능 고려:**
    * `TextureView`는 UI 스레드에서 뷰 계층의 일부로 렌더링되므로, 매우 높은 프레임률이나 해상도가 필요한 경우 `SurfaceView`보다 성능 부담이 있을 수 있습니다. 카메라 미리보기 해상도를 적절히 설정하고, `onSurfaceTextureUpdated`에서 과도한 작업을 피해야 합니다.
    * 만약 미리보기 프레임에 실시간으로 복잡한 이미지 처리를 적용해야 한다면, 해당 처리는 별도의 스레드에서 수행하고 그 결과를 `TextureView`에 업데이트하거나, OpenGL ES를 사용하여 `SurfaceTexture`에 직접 렌더링하는 방식을 고려할 수 있습니다.

6.  **권한(Permissions):**
    * `AndroidManifest.xml`에 `CAMERA` 권한을 선언하고, 런타임에 사용자로부터 권한을 요청하여 획득해야 합니다.

요약하자면, **변환 기능이 필수적인 실시간 카메라 미리보기에는 `TextureView`가 더 적합한 선택**입니다. 구현 시에는 생명주기 관리, 카메라 API의 올바른 사용, 스레딩, 그리고 성능 최적화를 신중하게 고려해야 합니다.