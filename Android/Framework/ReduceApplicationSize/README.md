# 안드로이드 애플리케이션 크기 줄이기

안드로이드 애플리케이션의 크기를 최적화하는 것은 특히 저장 공간이 제한적이거나 인터넷 연결이 느린 사용자의 사용자 경험을 개선하는 데 필수적입니다. 기능 저하 없이 애플리케이션 크기를 줄이기 위해 여러 전략을 사용할 수 있습니다.

### 사용하지 않는 리소스 제거

이미지, 레이아웃 또는 문자열과 같이 사용하지 않는 리소스는 APK 또는 AAB 크기를 불필요하게 늘립니다. Android Studio의 Lint와 같은 도구는 이러한 리소스를 식별하는 데 도움이 될 수 있습니다. 사용하지 않는 리소스를 제거한 후 `build.gradle` 파일에서 `shrinkResources`를 활성화하여 빌드 과정에서 사용하지 않는 리소스를 자동으로 제거합니다.

```gradle
android {
    buildTypes {
        release {
            minifyEnabled true
            shrinkResources true
        }
    }
}
```

### R8로 코드 축소 활성화

Android의 기본 코드 축소기 및 최적화 도구인 R8은 사용하지 않는 클래스와 메소드를 제거합니다. 또한 코드를 난독화하여 더 작게 만듭니다. 적절한 ProGuard 규칙은 중요한 코드나 리플렉션 기반 라이브러리가 제거되지 않도록 보장합니다.

```grale
android {
    buildTypes {
        release {
            minifyEnabled true
            proguardFiles getDefaultProguardFile('proguard-android-optimize.txt'), 'proguard-rules.pro'
        }
    }
}
```

### 리소스 최적화 사용

이미지 및 XML 파일과 같은 리소스를 최적화하면 앱 크기를 크게 줄일 수 있습니다.

  * **벡터 드로어블:** 래스터 이미지(예: PNG, JPEG)를 공간을 덜 차지하는 확장 가능한 그래픽인 벡터 드로어블로 교체합니다.
  * **이미지 압축:** TinyPNG 또는 ImageMagick과 같은 도구를 사용하여 눈에 띄는 품질 저하 없이 래스터 이미지를 압축합니다.
  * **WebP 형식:** 이미지를 PNG 또는 JPEG보다 압축률이 좋은 WebP 형식으로 변환합니다.

```gradle
android {
    defaultConfig {
        vectorDrawables.useSupportLibrary = true
    }
}
```

### Android App Bundle (AAB) 사용

Android App Bundle (AAB) 형식으로 전환하면 Google Play에서 개별 기기에 맞춰 최적화된 APK를 제공할 수 있습니다. 이렇게 하면 특정 구성(예: 화면 밀도, CPU 아키텍처 또는 언어)에 필요한 리소스와 코드만 포함하여 앱 크기가 줄어듭니다.

```gradle
android {
    bundle {
        density {
            enableSplit true
            enableSplit true
        }
        abi {
            enableSplit true
        }
        language {
            enableSplit true
        }
    }
}
```

### 불필요한 의존성 제거

프로젝트의 의존성을 검토하고 사용하지 않거나 중복되는 라이브러리를 제거합니다. Android Studio의 Gradle Dependency Analyzer를 사용하여 무거운 라이브러리와 전이 의존성을 식별할 수 있습니다.

### 네이티브 라이브러리 최적화

앱에 네이티브 라이브러리가 포함된 경우 다음 전략을 사용하여 영향을 줄입니다.

  * **사용하지 않는 아키텍처 제외:** `build.gradle` 파일의 `abiFilters` 옵션을 사용하여 필요한 ABI만 포함합니다.
  * **디버그 심볼 제거:** `stripDebugSymbols`를 사용하여 네이티브 라이브러리에서 디버깅 심볼을 제거합니다.

```gradle
android {
    defaultConfig {
        ndk {
            abiFilters "armeabi-v7a", "arm64-v8a" // Include only required ABIs
        }
    }
    packagingOptions {
        exclude "**/lib/**/*.so.debug"
    }
}
```

### Proguard 규칙 구성을 통한 디버그 정보 축소

디버깅 메타데이터는 최종 APK 또는 AAB에 불필요한 무게를 더합니다. `proguard-rules.pro` 파일을 구성하여 이러한 정보를 제거합니다.

```
-dontwarn com.example.unusedlibrary.**
-keep class com.example.important.** { *; }
```

### Dynamic Features 사용

동적 기능 모듈을 사용하면 자주 사용하지 않는 기능을 주문형 모듈로 분리하여 앱을 모듈화할 수 있습니다. 이렇게 하면 초기 다운로드 크기가 줄어듭니다.

```
dynamicFeatures = [":feature1", ":feature2"]
```

### 앱 내 대용량 애셋 방지

  * 비디오 또는 고해상도 이미지와 같은 대용량 애셋은 콘텐츠 전송 네트워크(CDN)에 호스팅하고 런타임에 동적으로 로드합니다.
  * 앱에 번들로 제공하는 대신 미디어 콘텐츠에 스트리밍을 사용합니다.

### 요약

Android 애플리케이션 크기를 줄이는 것은 사용하지 않는 리소스 제거, 코드 축소를 위한 R8 활성화, 리소스 최적화, App Bundle과 같은 최신 형식 활용 등 여러 전략의 조합을 포함합니다. 또한 의존성 검토, 네이티브 라이브러리 최적화, 기능 모듈화는 앱 크기를 더욱 최소화할 수 있습니다. 이러한 관행은 우수한 사용자 경험을 제공하는 가볍고 성능 좋은 애플리케이션을 보장합니다.

----

## Q. 앱에 고해상도 이미지가 포함되어 있어 APK/AAB 크기가 크게 증가합니다. 시각적 품질을 유지하면서 이미지 리소스를 최적화하는 방법과 최대 효율성을 위해 어떤 형식을 사용해야 할까요?

1.  **WebP 형식 사용 (가장 권장):**
    * **손실(Lossy) WebP:** JPEG와 유사하거나 더 나은 시각적 품질을 제공하면서 파일 크기는 일반적으로 25-34% 더 작습니다.
    * **무손실(Lossless) WebP:** PNG보다 파일 크기가 최대 26% 더 작으면서도 투명도(알파 채널)를 지원합니다.
    * **애니메이션 WebP:** 애니메이션 GIF를 대체하여 더 작은 파일 크기와 더 풍부한 색상을 제공할 수 있습니다.
    * **효율성:** 현재 대부분의 이미지에 대해 **가장 효율적인 형식**으로 간주됩니다. Android 4.2.1 (API 17) 이상에서 지원됩니다 (무손실 및 투명도는 Android 4.3, API 18 이상).

2.  **벡터 드로어블 (Vector Drawable) 활용:**
    * **대상:** 아이콘, 로고, 단순한 일러스트레이션 등 복잡하지 않은 그래픽.
    * **장점:** SVG(Scalable Vector Graphics) 기반의 XML 형식으로, 어떤 화면 밀도에서도 해상도 손실 없이 선명하게 표시되며 파일 크기가 매우 작습니다.
    * **효율성:** 래스터 이미지(PNG, JPEG)를 여러 해상도별로 준비할 필요가 없어 APK/AAB 크기를 크게 줄일 수 있습니다.

3.  **적절한 이미지 크기 조절 (Resizing) 및 다운샘플링 (Downsampling):**
    * **원본 크기 그대로 사용 지양:** 이미지가 UI에 표시될 실제 크기보다 훨씬 큰 원본 해상도로 앱에 포함되지 않도록 합니다.
    * **서버 측 리사이징:** 가능하다면 서버에서 각 기기 화면 크기나 UI 요소 크기에 맞는 여러 버전의 이미지를 제공하는 것이 가장 좋습니다.
    * **앱 내 리사이징/다운샘플링:** Glide나 Coil과 같은 이미지 로딩 라이브러리는 이미지를 `ImageView` 또는 Composable에 로드할 때 대상 뷰의 크기에 맞춰 자동으로 이미지를 다운샘플링하여 메모리 사용량을 줄여줍니다. 하지만 이는 로딩 시점의 메모리 문제에 대한 것이고, APK/AAB 크기 자체를 줄이려면 애초에 포함되는 이미지 리소스의 크기를 줄여야 합니다.
    * **빌드 시점 처리:** 앱 빌드 시 다양한 화면 밀도에 맞는 리소스를 자동으로 생성하는 기능을 활용하거나, 필요한 해상도의 이미지만 포함하도록 관리합니다.

4.  **이미지 압축 도구 사용:**
    * **WebP, PNG, JPEG 공통:** 이미지를 앱에 포함하기 전에 이미지 압축 도구를 사용하여 파일 크기를 추가적으로 줄입니다.
    * **도구 예시:**
        * TinyPNG / TinyJPG (온라인 도구)
        * ImageOptim (Mac용)
        * Squoosh (웹 기반 도구)
        * Android Studio 내의 "Convert to WebP" 기능 (PNG, JPEG 등을 WebP로 변환하며 압축률 조절 가능)
    * 이러한 도구는 시각적 품질 저하를 최소화하면서 파일 크기를 줄여줍니다.

5.  **9-Patch 이미지 사용:**
    * 버튼 배경이나 특정 영역만 늘어나야 하는 채팅 말풍선처럼 내용에 따라 **신축적으로 늘어나는 부분과 고정되는 부분을 정의**할 수 있는 PNG 이미지입니다.
    * 이미지의 특정 부분만 늘어나므로, 다양한 크기에 대응하기 위해 여러 버전의 이미지를 만들 필요가 없어 리소스 크기를 줄일 수 있습니다.

6.  **리소스 공유 및 재사용:**
    * 유사하지만 약간 다른 여러 이미지를 사용하는 대신, 하나의 기본 이미지에 색상 필터(`tint`)나 변형을 코드에서 적용하여 재사용합니다.
    * 상태(예: 버튼의 normal, pressed, disabled)에 따라 다른 이미지를 사용하는 대신, 상태 목록 드로어블(`StateListDrawable`)이나 색상 상태 목록(`ColorStateList`)을 활용하여 리소스 수를 줄입니다.

**최대 효율성을 위한 형식 및 전략 요약:**

* **일반 사진 및 복잡한 그래픽:** **WebP** (손실 또는 무손실)를 최우선으로 고려합니다.
* **아이콘 및 단순 그래픽:** **벡터 드로어블**을 사용합니다.
* **내용에 따라 늘어나는 배경 등:** **9-Patch 이미지**를 사용합니다.
* **모든 래스터 이미지:**
    * UI에 필요한 **최적의 해상도로 미리 리사이징**합니다.
    * **이미지 압축 도구**를 사용하여 파일 크기를 최소화합니다.
* **Android App Bundle (AAB) 사용:** Google Play가 사용자 기기 설정(화면 밀도 등)에 맞는 리소스만 포함된 최적화된 APK를 제공하도록 하여 최종 다운로드 크기를 줄입니다.
* **`shrinkResources true` 설정:** 빌드 시 사용되지 않는 리소스를 제거합니다.

이러한 방법들을 조합하여 사용하면 시각적 품질을 최대한 유지하면서 앱의 이미지 리소스 크기를 효과적으로 줄일 수 있습니다.


## Q. 애플리케이션에 여러 기능이 포함되어 있지만, 대부분의 사용자는 그중 일부만 드물게 사용합니다. 초기 앱 크기를 줄이면서도 필요할 때 해당 기능을 사용할 수 있도록 하는 솔루션을 어떻게 구현하시겠습니까?

**A.** 이 문제를 해결하기 위한 가장 효과적인 방법은 **Play Feature Delivery를 활용한 동적 기능 모듈(Dynamic Feature Modules)을 구현**하는 것입니다. 이를 통해 사용자가 실제로 기능을 필요로 할 때만 해당 기능 모듈을 다운로드하고 설치하도록 하여 초기 앱 설치 크기를 크게 줄일 수 있습니다.

**구현 단계 및 전략:**

1.  **기능 모듈화 (Modularization):**

      * 먼저, 앱의 기능들을 분석하여 핵심 기능과 부가 기능(또는 드물게 사용되는 기능)으로 나눕니다.
      * 자주 사용되지 않는 기능들을 각각 별도의 **동적 기능 모듈(Dynamic Feature Module)** 로 분리합니다. 각 모듈은 자체 코드, 리소스, 에셋 등을 가집니다.
      * 예를 들어, 사진 편집 앱이라면 "기본 편집 도구"는 기본 앱 모듈에 두고, "고급 필터 팩", "전문가용 보정 도구", "3D 변환 기능" 등은 각각 별도의 동적 기능 모듈로 만들 수 있습니다.

2.  **주문형(On-Demand) 모듈로 설정:**

      * 동적 기능 모듈을 주문형으로 설정하면, 사용자가 해당 기능을 사용하려고 시도하는 시점에 Google Play를 통해 모듈을 다운로드하고 설치하도록 할 수 있습니다.
      * **구현:**
          * `build.gradle` 파일 (동적 기능 모듈의)에서 `<dist:module dist:onDemand="true" ...>` 와 같이 설정합니다.
          * 앱 코드 내에서 사용자가 특정 기능을 요청할 때 `SplitInstallManager` (Play Core Library 또는 Play Feature Delivery 라이브러리 제공)를 사용하여 해당 모듈의 설치를 요청합니다.
            ```kotlin
            val splitInstallManager = SplitInstallManagerFactory.create(context)

            // 특정 기능 모듈 설치 요청
            val request = SplitInstallRequest.newBuilder()
                .addModule("my_dynamic_feature") // 모듈 이름
                .build()

            splitInstallManager.startInstall(request)
                .addOnSuccessListener { sessionId -> /* 설치 시작 성공 */ }
                .addOnFailureListener { exception -> /* 설치 시작 실패 */ }

            // 설치 상태 모니터링 및 완료 후 기능 실행
            splitInstallManager.registerListener { state ->
                if (state.sessionId() == mySessionId) { // mySessionId는 startInstall에서 받은 값
                    when (state.status()) {
                        SplitInstallSessionStatus.INSTALLED -> {
                            // 모듈 설치 완료, 이제 기능 실행 가능
                            launchFeature()
                        }
                        SplitInstallSessionStatus.FAILED -> {
                            // 설치 실패 처리
                        }
                        // 다른 상태들 (DOWNLOADING, INSTALLING 등) 처리
                    }
                }
            }
            ```

3.  **조건부(Conditional) 모듈 (선택 사항):**

      * 특정 조건(예: 사용자 국가, 기기 기능 지원 여부, API 레벨)을 만족하는 사용자에게만 앱 설치 시 또는 추후에 자동으로 특정 기능 모듈이 설치되도록 설정할 수 있습니다.
      * **구현:** 동적 기능 모듈의 `AndroidManifest.xml` 내 `<dist:module ...>` 태그 안에 `<dist:conditions> ... </dist:conditions>`를 사용하여 조건을 명시합니다.

4.  **설치 시(Install-Time) 모듈 (초기 크기 감소 효과는 적음):**

      * 동적 기능 모듈이지만 앱 설치 시점에 함께 설치되도록 할 수도 있습니다. 이 경우 초기 크기 감소 효과는 없지만, 나중에 필요 없다면 제거할 수 있는 옵션을 제공할 수 있습니다 (제거 가능한 모듈).

**장점:**

  * **초기 다운로드 크기 대폭 감소:** 사용자는 앱의 핵심 기능만 먼저 다운로드하므로 설치 장벽이 낮아지고 설치율을 높일 수 있습니다.
  * **사용자 저장 공간 절약:** 사용하지 않는 기능이 기기 저장 공간을 불필요하게 차지하는 것을 방지합니다.
  * **앱 로딩 속도 향상 가능성:** 초기 실행에 필요한 코드와 리소스만 포함되므로 앱 시작 속도에도 긍정적인 영향을 줄 수 있습니다.
  * **모듈 기반 개발:** 기능별 모듈화는 코드 관리, 빌드 시간 단축, 팀별 개발 용이성 등 개발 효율성 측면에서도 이점이 있습니다.

**고려사항:**

  * **네트워크 연결:** 주문형 모듈은 다운로드를 위해 네트워크 연결이 필요합니다. 오프라인 상태에서의 사용 흐름을 고려해야 합니다.
  * **다운로드 시간 및 사용자 경험:** 모듈 크기에 따라 다운로드 시간이 소요될 수 있으므로, 사용자에게 진행 상황을 명확히 알리고 자연스러운 UX를 제공해야 합니다.
  * **모듈 간 의존성 관리:** 모듈 간 의존성을 신중하게 설계해야 합니다.

동적 기능 모듈을 사용하면 앱의 초기 크기를 효과적으로 줄이면서도, 필요에 따라 모든 기능을 사용자에게 제공할 수 있는 유연한 애플리케이션을 구축할 수 있습니다.