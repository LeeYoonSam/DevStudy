# Performance

## 앱 크기 줄이기
사용자는 특히 기기가 불안정한 2G 및 3G 네트워크에 연결되거나 데이터 한도가 있는 요금제를 사용하는 신흥 시장의 경우 용량이 커 보이는 앱을 다운로드하는 것을 꺼리는 경우가 많습니다. 이 페이지에서는 더 많은 사용자가 앱을 다운로드하도록 앱의 다운로드 크기를 줄이는 방법에 관해 설명합니다.

### Android App Bundle로 앱 업로드
Android App Bundle은 앱의 컴파일된 코드와 리소스를 모두 포함하지만 APK 생성 및 Google Play 서명은 지연시키는 업로드 형식입니다.

App Bundle을 사용하여 각 사용자의 기기 설정에 최적화된 APK를 생성하고 제공하므로 사용자는 앱을 실행하는 데 필요한 코드와 리소스만 다운로드할 수 있습니다. 다양한 기기를 지원하기 위해 여러 개의 APK를 빌드, 서명, 관리할 필요가 없으며 사용자는 더 작고 최적화된 앱을 다운로드하게 됩니다.

- App Bundle로 게시되는 앱에 압축 다운로드 크기 제한을 200MB로 시행
- 서명된 APK를 업로드하여 Google Play에 게시하는 앱의 경우 압축 다운로드 크기가 100MB로 제한


### APK 구조 이해
- APK 파일은 앱을 구성하는 모든 파일이 포함된 ZIP 파일로 구성
- 파일에는 Java 클래스 파일, 리소스 파일 및 컴파일된 리소스가 들어 있는 파일이 포함

APK는 다음 디렉토리를 포함합니다.
- `META-INF/`: `CERT.SF` 및 `CERT.RSA` 서명 파일은 물론 `MANIFEST.MF` 매니페스트 파일도 포함합니다.
- `assets/`: 앱이 `AssetManager` 객체를 사용하여 검색할 수 있는 앱의 애셋을 포함합니다.
- `res/`: `resources.arsc`로 컴파일되지 않은 리소스를 포함합니다.
- `lib/`: 프로세서의 소프트웨어 레이어와 관련된 컴파일된 코드를 포함합니다. 이 디렉터리에는 `armeabi`, `armeabi-v7a`, `arm64-v8a`, `x86`, `x86_64`, `mips` 등 각 플랫폼 유형의 하위 디렉터리가 포함됩니다.

APK에는 다음 파일도 포함됩니다. AndroidManifest.xml만 필수입니다.

- `resources.arsc`: 컴파일된 리소스를 포함합니다. 이 파일에는 res/values/ 폴더에 있는 모든 구성의 XML 콘텐츠가 포함됩니다. 패키징 도구는 이 XML 콘텐츠를 추출하여 바이너리 형식으로 컴파일하고 콘텐츠를 보관합니다. 이 콘텐츠에는 언어 문자열 및 스타일뿐만 아니라 레이아웃 파일 및 이미지와 같이 resources.arsc 파일에 직접 포함되지 않은 콘텐츠의 경로가 포함됩니다.
- `classes.dex`: Dalvik 또는 ART 가상 머신에서 인식하는 DEX 파일 형식으로 컴파일된 클래스를 포함합니다.
- `AndroidManifest.xml`: 핵심 Android 매니페스트 파일을 포함합니다. 이 파일에는 앱의 이름, 버전, 액세스 권한 및 참조된 라이브러리 파일이 나열됩니다. 파일은 Android의 바이너리 XML 형식을 사용합니다.

### 리소스 개수 및 크기 줄이기
- APK의 크기는 앱의 로드 속도, 사용되는 메모리 용량 및 소비되는 전력에 영향을 줍니다. 
- APK에 포함되는 리소스의 수와 크기를 줄여 APK를 더 작게 만들 수 있습니다. 
- 특히 앱에서 더 이상 사용하지 않는 리소스를 삭제할 수 있으며 이미지 파일 대신 확장 가능한 Drawable 객체를 사용할 수 있습니다. 

**사용하지 않는 리소스 삭제**
Android 스튜디오에 포함된 정적 코드 분석기인 `lint` 도구는 코드가 참조하지 않는 `res/` 폴더의 리소스를 감지합니다. `lint` 도구는 프로젝트에서 잠재적으로 사용되지 않는 리소스를 발견하면 다음 예와 같은 메시지를 출력합니다.

```
res/layout/preferences.xml: Warning: The resource R.layout.preferences appears
    to be unused [UnusedResources]
```

참고: `lint` 도구는 `assets/` 폴더, 리플렉션을 통해 참조되는 애셋 또는 앱에 연결한 라이브러리 파일을 검색하지 않습니다. 또한 리소스를 삭제하지 않습니다. 이러한 존재에 대해서만 알립니다.

코드에 추가하는 라이브러리에는 사용되지 않는 리소스가 포함될 수 있습니다. 앱의 `build.gradle.kts` 파일에서 `shrinkResources`를 사용 설정하면 Gradle이 개발자를 대신해 자동으로 리소스를 삭제할 수 있습니다.

```
android {
    // Other settings.

    buildTypes {
        getByName("release") {
            minifyEnabled = true
            shrinkResources = true
            proguardFiles(getDefaultProguardFile('proguard-android.txt'), "proguard-rules.pro")
        }
    }
}
```
- `shrinkResources를` 사용하려면 코드 축소를 사용 설정하세요. 
- 빌드 프로세스 중에 먼저 R8은 사용되지 않는 코드를 삭제합니다. 그런 다음, Android Gradle 플러그인은 사용되지 않는 리소스를 삭제합니다.
- Android Gradle 플러그인 7.0 이상에서는 앱에서 지원하는 구성을 선언할 수 있습니다. 
  - Gradle은 `resourceConfigurations` 버전과 `defaultConfig` 옵션을 사용하여 이 정보를 빌드 시스템에 전달합니다. 그러면 빌드 시스템은 지원되지 않는 다른 구성의 리소스가 APK에 나타나는 것을 방지하므로 APK의 크기가 줄어듭니다.

### 드로어블 객체 사용
일부 이미지에는 정적 이미지 리소스가 필요하지 않습니다. 대신 프레임워크는 런타임 시 동적으로 이미지를 그릴 수 있습니다. Drawable 객체(또는 XML의 <shape>)는 APK에서 매우 작은 공간을 차지합니다. 또한 XML Drawable 객체는 Material Design 가이드라인을 준수하는 단색 이미지를 생성합니다.

### 리소스 재사용
동일한 이미지의 색조, 명암 또는 회전된 버전과 같은 이미지의 변형에 관한 별도의 리소스를 포함할 수 있습니다. 그러나 동일한 리소스 집합을 재사용하면서 런타임 시 필요에 따라 맞춤설정하는 것이 좋습니다.

Android에서는 `android:tint` 및 `tintMode` 속성을 사용하여 애셋의 색상을 변경하는 여러 유틸리티를 제공합니다.

또한 회전되었을 뿐 다른 리소스와 동일한 리소스를 삭제할 수도 있습니다. 다음 코드 스니펫은 이미지 중간에서 피봇하고 180도 회전하여 '좋아요'를 '싫어요'로 전환하는 예를 보여줍니다.

```xml
<?xml version="1.0" encoding="utf-8"?>
<rotate xmlns:android="http://schemas.android.com/apk/res/android"
    android:drawable="@drawable/ic_thumb_up"
    android:pivotX="50%"
    android:pivotY="50%"
    android:fromDegrees="180" />
```

### PNG 및 JPEG 파일 압축
[pngcrush](https://pmt.sourceforge.io/pngcrush/), [pngquant](https://pngquant.org/) 또는 [zopflipng](https://github.com/google/zopfli)와 같은 도구를 사용하여 이미지 품질을 유지하면서 PNG 파일 크기를 줄일 수 있습니다. 이러한 도구는 체감 이미지 품질을 유지하면서 PNG 파일 크기를 줄여 줍니다.

`pngcrush` 도구가 특히 효과적입니다. 이 도구는 PNG 필터 및 zlib(Deflate) 매개변수를 반복하여, 이미지를 압축하는 데 필터와 매개변수의 각 조합을 사용합니다. 그런 다음 압축된 가장 작은 출력을 생성하는 구성을 선택합니다.

JPEG 파일을 압축하려면 `packJPG` 및 `guetzli`와 같은 도구를 사용할 수 있습니다.

### 벡터 그래픽 사용
벡터 그래픽을 사용하여 해상도에 독립적인 아이콘 및 기타 확장 가능 미디어를 만들 수 있습니다. 이러한 그래픽을 사용하면 APK 공간을 크게 줄일 수 있습니다. 벡터 이미지는 Android에서 VectorDrawable 객체로 표시됩니다. VectorDrawable 객체를 사용하면 100바이트 파일이 화면 크기의 선명한 이미지를 생성할 수 있습니다.

하지만 시스템에서 각 VectorDrawable 객체를 렌더링하는 데 훨씬 더 많은 시간이 걸리고 큰 이미지가 화면에 표시되는 데는 더 많은 시간이 걸립니다. 따라서 `작은 이미지를 표시할 때만 이러한 벡터 그래픽을 사용하는 것이 좋습니다.`


## 네이티브 및 Java 코드 줄이기

### 불필요하게 생성된 코드 삭제
자동으로 생성되는 코드가 차지할 공간을 고려해야 합니다. 예를 들어 대부분의 프로토콜 버퍼 도구는 많은 수의 메서드와 클래스를 생성하므로 앱의 크기가 두 배 또는 세 배로 늘어날 수 있습니다.

### 열거형 삭제
- 하나의 enum으로 인해 앱의 `classes.dex` 파일이 약 1.0~1.4KB만큼 늘어날 수 있습니다. 이러한 용량 증가는 복잡한 시스템 또는 공유 라이브러리에서 빠르게 누적될 수 있습니다. 
- 가능하면 `@IntDef` 주석과 코드 축소를 사용하여 열거형을 삭제하고 정수로 변환하는 것이 좋습니다. 이러한 유형 변환을 거쳐도 enum과 관련한 모든 유형의 안전상 이점이 유지됩니다.

### 네이티브 바이너리 크기 줄이기
앱에서 네이티브 코드 및 Android NDK를 사용하는 경우 코드를 최적화하여 앱의 출시 버전 크기를 줄일 수도 있습니다. 이때 유용한 기법으로는 디버그 기호를 삭제하고 네이티브 라이브러리를 추출하지 않는 것이 있습니다.

### 디버그 기호 삭제
- 앱이 개발 중이며 계속 디버깅이 필요하다면 디버그 기호 사용은 타당합니다. 
- 그 밖에는 Android NDK에 제공된 `arm-eabi-strip` 도구를 사용하여 네이티브 라이브러리에서 불필요한 디버그 기호를 삭제하세요.

### 네이티브 라이브러리 추출 방지
- 앱의 출시 버전을 빌드할 때 앱의 `build.gradle.kts` 파일에서 `useLegacyPackaging을 false로 설정`하여, 압축되지 않은 `.so` 파일을 APK에 패키징하세요. 
- 이 플래그를 사용 중지하면 설치 중에 `PackageManager`가 `.so` 파일을 APK에서 파일 시스템으로 복사할 수 없습니다.


## Reference
- [Android Performance](https://developer.android.com/topic/performance/overview?hl=ko)