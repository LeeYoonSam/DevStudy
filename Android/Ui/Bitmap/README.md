# 안드로이드의 비트맵(Bitmap)이란 무엇이며, 큰 비트맵을 효율적으로 어떻게 처리하나요?

**비트맵(Bitmap)** 은 메모리에서 이미지를 표현하는 방식입니다. 이는 픽셀 데이터를 저장하며, 리소스, 파일 또는 원격 소스에서 가져온 이미지를 화면에 렌더링하는 데 자주 사용됩니다. 비트맵 객체는 특히 고해상도 이미지의 경우 대량의 픽셀 데이터를 보유하므로, 부적절하게 처리하면 쉽게 메모리 고갈(memory exhaustion) 및 `OutOfMemoryError`가 발생할 수 있습니다.

### 큰 비트맵의 문제점

많은 이미지(예: 카메라로 찍은 사진이나 인터넷에서 다운로드한 이미지)는 이를 표시하는 UI 컴포넌트가 요구하는 크기보다 훨씬 큽니다. 이러한 전체 해상도 이미지를 불필요하게 로드하면 다음과 같은 문제가 발생합니다.

* **과도한 메모리 소비**
* **성능 오버헤드 발생**
* 메모리 압박으로 인한 **앱 비정상 종료 위험**

---

### 메모리 할당 없이 비트맵 크기 읽기

비트맵을 로드하기 전에, 전체 로드가 정당한지 결정하기 위해 해당 비트맵의 크기를 검사하는 것이 중요합니다. `BitmapFactory.Options` 클래스를 사용하면 `inJustDecodeBounds = true`로 설정하여 픽셀 데이터에 대한 메모리를 할당하지 않으면서 이미지 메타데이터만 디코딩할 수 있습니다.

```kotlin
val options = BitmapFactory.Options().apply {
    inJustDecodeBounds = true // true로 설정하면 실제 비트맵은 로드하지 않고 크기 정보만 가져옴
}
BitmapFactory.decodeResource(resources, R.drawable.myimage, options) // R.id.myimage가 아닌 R.drawable.myimage 여야 함

val imageWidth = options.outWidth   // 이미지 너비
val imageHeight = options.outHeight  // 이미지 높이
val imageType = options.outMimeType // 이미지 MIME 타입
```
이 단계는 이미지 크기가 디스플레이 요구 사항과 일치하는지 평가하여 불필요한 메모리 할당을 방지하는 데 도움이 됩니다.

---

### 샘플링을 통한 축소된 비트맵 로드

크기를 알게 되면 `inSampleSize` 옵션을 사용하여 비트맵을 대상 크기에 맞게 축소할 수 있습니다. 이는 이미지를 2배, 4배 등으로 서브샘플링(subsampling)하여 메모리 사용량을 줄입니다. 예를 들어, 2048×1536 이미지를 `inSampleSize = 4`로 로드하면 512×384 크기의 비트맵이 됩니다.

```kotlin
fun calculateInSampleSize(options: BitmapFactory.Options, reqWidth: Int, reqHeight: Int): Int {
    // 원본 이미지의 높이와 너비
    val height = options.outHeight
    val width = options.outWidth
    var inSampleSize = 1

    if (height > reqHeight || width > reqWidth) {
        val halfHeight = height / 2
        val halfWidth = width / 2

        // 요청된 너비와 높이보다 커질 때까지 inSampleSize를 2의 거듭제곱으로 계산
        while ((halfHeight / inSampleSize) >= reqHeight && (halfWidth / inSampleSize) >= reqWidth) {
            inSampleSize *= 2
        }
    }
    return inSampleSize
}
```
이는 이미지 품질과 메모리 효율성 사이의 균형을 맞추는 데 도움이 됩니다.

---

### 서브샘플링을 사용한 전체 디코딩 과정

`calculateInSampleSize`를 사용하여 두 단계로 비트맵을 디코딩할 수 있습니다.

1.  경계(크기)만 디코딩합니다.
2.  계산된 `inSampleSize`를 설정하고 축소된 비트맵을 디코딩합니다.

```kotlin
fun decodeSampledBitmapFromResource(
    res: Resources,
    resId: Int,
    reqWidth: Int,
    reqHeight: Int
): Bitmap {
    // 먼저 inJustDecodeBounds=true로 크기만 확인
    return BitmapFactory.Options().run {
        inJustDecodeBounds = true
        BitmapFactory.decodeResource(res, resId, this)

        // 계산된 inSampleSize 설정
        inSampleSize = calculateInSampleSize(this, reqWidth, reqHeight)

        // inJustDecodeBounds=false로 설정하고 실제 비트맵 디코딩
        inJustDecodeBounds = false
        BitmapFactory.decodeResource(res, resId, this)
    }
}
```

이를 `ImageView`와 함께 사용하려면 간단히 호출합니다.
```kotlin
imageView.setImageBitmap(
    decodeSampledBitmapFromResource(resources, R.drawable.myimage, 100, 100) // 목표 크기 100x100
)
```
이 접근 방식은 UI가 최적화된 메모리 사용량으로 적절한 크기의 이미지를 얻도록 보장합니다.

---

### 요약

비트맵은 안드로이드에서 메모리를 많이 사용하는 이미지 표현 방식입니다. 성능 문제와 비정상 종료를 피하려면, 먼저 `inJustDecodeBounds`를 사용하여 이미지 크기를 검사하고, 그 다음 `inSampleSize`로 큰 비트맵을 서브샘플링하여 필요한 만큼만 로드한 후, 마지막으로 크기 조정을 위한 2단계 전략을 사용하여 효율적으로 디코딩하는 것이 중요합니다. 이 과정은 메모리가 제한된 기기에서 이미지가 많은 애플리케이션을 견고하게 구축하는 데 필수적입니다.

---

## Q. 큰 비트맵을 메모리에 로드할 때의 위험 요소는 무엇이며, 이를 어떻게 피할 수 있나요?

안드로이드 애플리케이션에서 이미지를 다루는 것은 흔한 일이지만, 특히 고해상도의 큰 비트맵(Bitmap)을 메모리에 로드할 때는 여러 가지 위험 요소가 따릅니다. 이러한 위험을 이해하고 적절히 회피하는 것은 앱의 안정성과 성능을 위해 매우 중요합니다.

### 1. 서론: 비트맵과 메모리

비트맵은 이미지를 픽셀 데이터 형태로 메모리에 저장하는 객체입니다. 이미지의 해상도(가로 픽셀 수 x 세로 픽셀 수)와 픽셀당 바이트 수(예: ARGB_8888 설정은 픽셀당 4바이트)에 따라 메모리 사용량이 결정되므로, 큰 이미지는 상당한 양의 메모리를 차지할 수 있습니다.

### 2. 대형 비트맵 로딩 시 주요 위험 요소

1.  **`OutOfMemoryError` (OOM) 발생 (가장 치명적):**
    * 안드로이드 앱은 각 프로세스별로 할당된 힙(heap) 메모리 크기에 제한이 있습니다.
    * 큰 비트맵(들)을 아무런 처리 없이 원본 크기 그대로 로드하려고 하면 이 제한을 쉽게 초과하여, 앱이 더 이상 메모리를 할당받지 못하고 `OutOfMemoryError`를 발생시키며 비정상 종료될 수 있습니다.

2.  **성능 저하 (버벅임 및 느린 반응):**
    * **잦은 가비지 컬렉션(GC)으로 인한 UI 멈춤:** 큰 비트맵 객체는 메모리를 많이 차지하므로, 이러한 객체들이 자주 생성되고 해제되면 가비지 컬렉터가 더 자주, 그리고 더 오래 작동하게 됩니다. GC가 실행되는 동안에는 앱의 메인 스레드가 일시적으로 멈출 수 있어 UI 버벅임(jank), 프레임 드롭 현상이 발생하고 전반적인 앱 반응성이 떨어집니다.
    * **느린 이미지 로딩 시간:** 디스크나 네트워크로부터 큰 이미지 파일을 읽고 디코딩하는 작업은 CPU와 I/O를 많이 사용하는 무거운 작업입니다. 만약 이 작업이 메인 스레드에서 수행되면 UI 렌더링과 사용자 상호작용을 직접적으로 차단하여 ANR(Application Not Responding)을 유발하거나 사용자가 앱이 느리다고 느끼게 만듭니다.
    * **CPU 사용량 증가 및 배터리 소모:** 큰 이미지를 디코딩하고 처리하는 데는 상당한 CPU 자원이 소모되며, 이는 전반적인 앱 반응성 저하뿐만 아니라 기기의 배터리 소모를 증가시키는 원인이 됩니다.

3.  **사용자 경험(UX) 저해:**
    * OOM으로 인한 앱의 잦은 비정상 종료는 최악의 사용자 경험을 제공합니다.
    * 느린 UI, 이미지 로딩 지연, 버벅이는 스크롤 등은 사용자를 매우 불편하게 만듭니다.

4.  **다른 앱/시스템에 대한 리소스 부족 유발:**
    * 특정 앱이 과도하게 메모리를 사용하면 시스템 전체의 가용 메모리가 줄어들어, 다른 앱들이나 시스템 자체의 성능에 부정적인 영향을 미치고, 로우 메모리 킬러(Low Memory Killer, LMK)에 의해 다른 앱들이 더 자주 종료될 수 있습니다.

### 3. 이러한 위험 요소를 피하기 위한 전략

대형 비트맵으로 인한 문제를 피하고 앱의 성능과 안정성을 유지하기 위한 주요 전략은 다음과 같습니다.

#### 3.1. 필요한 크기로만 로드 (다운샘플링 - Downsampling)

화면에 표시할 크기보다 훨씬 큰 이미지를 원본 그대로 로드하는 것은 메모리 낭비입니다. 따라서 이미지를 실제 표시될 UI 요소의 크기에 맞춰 축소해서 로드해야 합니다.

1.  **`BitmapFactory.Options`와 `inJustDecodeBounds = true`로 크기 먼저 확인:**
    비트맵을 실제로 메모리에 로드하기 전에, `BitmapFactory.Options` 객체의 `inJustDecodeBounds` 속성을 `true`로 설정하여 `BitmapFactory.decodeXXX` 메서드(예: `decodeResource`, `decodeFile`, `decodeStream`)를 호출합니다. 이렇게 하면 실제 비트맵 객체는 생성하지 않고 이미지의 원본 너비(`outWidth`), 높이(`outHeight`), MIME 타입(`outMimeType`) 등의 메타데이터만 가져올 수 있습니다.
2.  **적절한 `inSampleSize` 계산:**
    원본 이미지 크기와 이미지가 표시될 `ImageView` 등의 목표 크기를 비교하여 적절한 `inSampleSize` 값을 계산합니다. `inSampleSize`는 이미지를 얼마나 축소할지를 결정하는 값으로, 2의 거듭제곱 값(1, 2, 4, 8...)이어야 합니다. 예를 들어, `inSampleSize`가 2이면 너비와 높이가 각각 1/2로 줄어들어 전체 픽셀 수는 1/4, 메모리 사용량도 약 1/4로 줄어듭니다.
3.  **축소된 비트맵 디코딩:**
    계산된 `inSampleSize` 값을 `BitmapFactory.Options`의 `inSampleSize` 속성에 설정하고, `inJustDecodeBounds`를 다시 `false`로 변경한 후 `BitmapFactory.decodeXXX` 메서드를 호출하여 축소된 비트맵 객체를 메모리에 로드합니다.

#### 3.2. 효율적인 비트맵 설정(Configuration) 사용

* 이미지에 알파 투명도가 필요 없고 색상 표현이 아주 정밀하지 않아도 된다면, `BitmapFactory.Options.inPreferredConfig`를 `Bitmap.Config.RGB_565` (픽셀당 2바이트)로 설정하는 것을 고려할 수 있습니다. 기본값인 `Bitmap.Config.ARGB_8888` (픽셀당 4바이트)에 비해 메모리 사용량을 절반으로 줄일 수 있습니다. 단, 색상 표현 범위가 줄어들고 알파 채널이 없으므로 신중하게 사용해야 합니다.

#### 3.3. 캐싱(Caching) 전략 적극 활용

자주 사용되는 이미지를 매번 디코딩하는 것은 비효율적이므로 캐싱 전략을 사용합니다.

* **인메모리 캐시 (`LruCache`):** 최근에 사용된 (그리고 적절히 축소된) 비트맵들을 메모리에 캐싱합니다. `LruCache`는 사용 가능한 힙 메모리의 일정 부분(예: 1/8)을 할당받아, 메모리가 부족해지면 가장 오래전에 사용된 아이템부터 제거합니다. 이를 통해 자주 접근하는 이미지의 로딩 속도를 크게 향상시킬 수 있습니다.
* **디스크 캐시 (`DiskLruCache` 또는 유사 라이브러리):** 처리된(축소된) 비트맵을 디스크에 저장합니다. 앱 세션 간에 이미지를 유지하거나 인메모리 캐시에서 제거된 이미지를 빠르게 다시 로드하는 데 유용합니다. 네트워크에서 다시 다운로드하거나 원본에서 다시 처리하는 것보다 빠릅니다.

#### 3.4. 백그라운드 스레드에서 비트맵 로딩 및 처리

* 이미지 파일을 읽거나 네트워크에서 다운로드하고 디코딩하는 작업은 시간이 오래 걸릴 수 있으므로 **절대로 메인(UI) 스레드에서 수행하면 안 됩니다.** 이는 UI를 차단하여 ANR을 유발할 수 있습니다.
* 코틀린 코루틴 (`Dispatchers.IO` 사용), `AsyncTask`(deprecated), `Executors` 프레임워크, 또는 `WorkManager` 등을 사용하여 비트맵 로딩 및 처리 작업을 백그라운드 스레드로 옮깁니다.
* 비트맵 처리가 완료되면, 그 결과를 메인 스레드로 전달하여 `ImageView`를 업데이트합니다 (예: 코루틴에서 `withContext(Dispatchers.Main)` 사용, `Handler`, `Activity.runOnUiThread`).

#### 3.5. (직접 관리 시) 불필요한 비트맵 명시적 해제

* 이미지 로딩 라이브러리를 사용하지 않고 비트맵 객체를 직접 관리하며, 해당 비트맵이 더 이상 필요 없거나 화면에서 보이지 않게 되면, 해당 비트맵 객체에 대한 모든 강한 참조(strong reference)를 제거(`myBitmap = null`)하여 가비지 컬렉터가 메모리를 더 빨리 회수할 수 있도록 돕는 것이 중요합니다.
* 구버전 안드로이드(API 10 이전)에서는 네이티브 메모리에 할당된 비트맵 데이터 해제를 위해 `bitmap.recycle()`을 명시적으로 호출하는 것이 중요했지만, 최신 버전에서는 GC가 더 잘 관리하므로 `recycle()`의 필요성은 줄었습니다. 하지만 여전히 큰 비트맵을 다룰 때는 참조 관리에 주의해야 합니다.

#### 3.6. 이미지 로딩 라이브러리 사용 (권장)

대부분의 경우, **Glide, Coil (코틀린 우선), Picasso**와 같은 잘 만들어진 이미지 로딩 라이브러리를 사용하는 것이 가장 좋은 방법입니다. 이러한 라이브러리들은 다음과 같은 복잡한 작업들을 자동으로 처리해 줍니다.

* 백그라운드 스레드에서의 이미지 로딩 및 디코딩.
* `ImageView` 크기에 맞는 자동 다운샘플링 및 리사이징.
* 효율적인 메모리 및 디스크 캐싱.
* 비트맵 풀링(Bitmap pooling)을 통한 메모리 재사용.
* 이미지 요청의 생명주기 관리.

이러한 라이브러리를 사용하면 개발자는 비트맵 관리의 복잡성에서 벗어나 핵심 기능 개발에 더 집중할 수 있으며, 이미 검증된 최적화 기법들을 쉽게 활용할 수 있습니다.

### 4. 결론

대형 비트맵을 효율적으로 처리하는 것은 안드로이드 앱의 성능과 안정성을 위해 매우 중요합니다. 필요한 크기만 로드하고, 적절한 비트맵 설정을 사용하며, 캐싱 전략을 구현하고, 백그라운드 스레드에서 작업을 처리하며, 가능하면 검증된 이미지 로딩 라이브러리를 활용하는 것이 이러한 위험을 피하고 사용자에게 쾌적한 경험을 제공하는 핵심 전략입니다.

---

## 💡 프로 팁: 사용자 정의 이미지 로딩 시스템에서 큰 비트맵을 위한 캐싱은 어떻게 구현하나요?

특히 이미지 목록, 그리드 또는 캐러셀을 처리할 때, 큰 비트맵을 효율적으로 관리하는 것은 부드럽고 메모리 안전한 안드로이드 애플리케이션을 구축하는 데 필수적입니다. 안드로이드는 두 가지 효과적인 전략을 제공합니다: `LruCache`를 사용한 인메모리 캐싱과 `DiskLruCache`를 사용한 디스크 기반 캐싱입니다.

### 인메모리 캐싱 (In-Memory Caching) - `LruCache` 사용

`LruCache`는 비트맵에 대한 선호되는 인메모리 캐싱 솔루션입니다. 최근 사용된 항목에 대한 강한 참조(strong references)를 유지하고 메모리가 부족할 때 가장 오래전에 사용된(LRU, Least Recently Used) 항목을 자동으로 제거(evict)합니다. 작동 방식은 다음과 같습니다.

```kotlin
object LruCacheManager {
    // 앱에서 사용 가능한 최대 메모리 (KB 단위)
    val maxMemory = (Runtime.getRuntime().maxMemory() / 1024).toInt()
    // 캐시 크기로 사용 가능한 메모리의 1/8 사용 (일반적인 권장 사항)
    val cacheSize = maxMemory / 8

    val memoryCache = object : LruCache<String, Bitmap>(cacheSize) {
        // 아이템 크기를 KB 단위로 반환 (비트맵의 바이트 수 기준)
        override fun sizeOf(key: String, bitmap: Bitmap): Int {
            return bitmap.byteCount / 1024
        }
    }
}
```
안전을 위해 사용 가능한 메모리의 약 1/8을 할당합니다. 이 설정은 이미지에 빠르게 접근하고 중복 디코딩을 피하게 해줍니다. 사용 방법은 다음과 같습니다.

```kotlin
fun loadBitmap(imageId: Int, imageView: ImageView, context: Context) { // context 추가
    val key = imageId.toString()
    // 메모리 캐시에서 비트맵 가져오기 시도
    LruCacheManager.memoryCache.get(key)?.let {
        imageView.setImageBitmap(it) // 캐시에 있으면 바로 사용
    } ?: run {
        // 캐시에 없으면 플레이스홀더 이미지를 설정하고 백그라운드 작업으로 로드
        imageView.setImageResource(R.drawable.image_placeholder)

        val workRequest = OneTimeWorkRequestBuilder<BitmapDecodeWorker>()
            .setInputData(workDataOf("imageId" to imageId, "imageKey" to key)) // imageKey도 전달
            .build()
        WorkManager.getInstance(context).enqueue(workRequest)
    }
}
```

그리고 워커(Worker)에서는:
```kotlin
class BitmapDecodeWorker(
    val context: Context, // context를 멤버 변수로 변경
    workerParams: WorkerParameters
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result {
        val imageId = inputData.getInt("imageId", -1)
        val imageKey = inputData.getString("imageKey") // imageKey 받기
        if (imageId == -1 || imageKey == null) return Result.failure()

        // decodeSampledBitmapFromResource는 이전 예시에서 정의된 함수라고 가정
        val bitmap = decodeSampledBitmapFromResource(
            res = applicationContext.resources,
            resId = imageId,
            reqWidth = 100, // 실제로는 ImageView 크기 등을 전달해야 함
            reqHeight = 100
        )

        bitmap?.let {
            LruCacheManager.memoryCache.put(imageKey, it) // 키를 사용하여 캐시에 저장
            // UI 업데이트가 필요하다면 여기서 EventBus, Broadcast, LiveData 등을 사용할 수 있음
            // 직접 ImageView를 업데이트하려면 메인 스레드로 전환 필요 (WorkManager는 백그라운드)
            return Result.success()
        }

        return Result.failure()
    }
}
```
`SoftReference`나 `WeakReference`는 공격적인 가비지 컬렉션으로 인해 더 이상 캐싱에 신뢰할 수 없으므로 사용을 피하세요.

---

### 디스크 캐싱 (Disk Caching) - `DiskLruCache` 사용

안드로이드에서 메모리는 제한적이고 휘발성입니다. 비트맵이 앱 세션 간에 유지되고 재계산을 피하도록 하려면, [`DiskLruCache` 라이브러리](https://github.com/JakeWharton/DiskLruCache)를 사용하여 비트맵을 디스크에 저장할 수 있습니다. 이는 리소스 집약적인 이미지나 스크롤 가능한 이미지 목록을 다룰 때 특히 유용합니다. (주의: `DiskLruCache`는 공식 안드로이드 SDK의 일부가 아니며, Jake Wharton이 만든 널리 사용되는 라이브러리입니다. 프로젝트에 의존성을 추가해야 합니다.)

먼저, 디코딩된 비트맵을 유지하기 위해 안전한 해싱 및 I/O 로직으로 `DiskLruCache`를 감싸는 `DiskCacheManager`를 작성할 수 있습니다.
```kotlin
import com.jakewharton.disklrucache.DiskLruCache // 의존성 추가 필요
import java.io.File
import java.security.MessageDigest

class DiskCacheManager(context: Context) {

    private val cacheDir = File(context.cacheDir, "images") // 캐시 디렉터리
    private val diskCacheSize = 10 * 1024 * 1024L // 10MB 디스크 캐시 크기
    private var diskLruCache: DiskLruCache

    init {
        if (!cacheDir.exists()) {
            cacheDir.mkdirs()
        }
        diskLruCache = DiskLruCache.open(cacheDir, 1, 1, diskCacheSize)
    }

    // 키를 안전한 파일 이름으로 변환 (SHA-1 해시 사용)
    private fun filenameForKey(key: String): String {
        return MessageDigest
            .getInstance("SHA-1")
            .digest(key.toByteArray())
            .joinToString(separator = "") { byte -> "%02x".format(byte) } // 16진수 문자열로 변환
    }

    // 디스크 캐시에서 비트맵 가져오기
    @Synchronized
    fun get(key: String): Bitmap? {
        val hashedKey = filenameForKey(key)
        var snapshot: DiskLruCache.Snapshot? = null
        return try {
            snapshot = diskLruCache.get(hashedKey)
            snapshot?.getInputStream(0)?.use { inputStream -> // use 확장 함수로 자동 close
                BitmapFactory.decodeStream(inputStream)
            }
        } catch (e: Exception) {
            Log.e("DiskCacheManager", "Error getting bitmap from disk cache", e)
            null
        } finally {
            snapshot?.close()
        }
    }

    // 디스크 캐시에 비트맵 저장
    @Synchronized
    fun put(key: String, bitmap: Bitmap) {
        val hashedKey = filenameForKey(key)
        var editor: DiskLruCache.Editor? = null
        try {
            editor = diskLruCache.edit(hashedKey)
            if (editor != null) {
                editor.newOutputStream(0).use { outputStream -> // use 확장 함수로 자동 close
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 85, outputStream) // 압축 형식 및 품질 설정
                    editor.commit()
                }
            } else {
                diskLruCache.flush() // editor가 null이면 flush 시도 (공간 부족 등)
            }
        } catch (e: Exception) {
            Log.e("DiskCacheManager", "Error putting bitmap to disk cache", e)
            editor?.abort()
        }
    }
}
```
이 클래스는 다음을 보장합니다.
* 디스크 안전한 SHA-1 기반 파일 이름 생성.
* 안전한 I/O 작업.
* 중복 쓰기 방지 (편집기를 통한).

다음으로, Jetpack WorkManager의 `CoroutineWorker`를 사용하여 메인 스레드 외부에서 디스크 캐싱을 수행하고, 메모리 및 디스크 전략을 안전하게 결합합니다.

```kotlin
class BitmapWorker( // 이전 예시와 병합된 형태
    val context: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(context, workerParams) {

    // DiskCacheManager 인스턴스는 Application 레벨에서 싱글톤으로 관리하는 것이 좋음
    private val diskCacheManager by lazy { DiskCacheManager(applicationContext) }

    override suspend fun doWork(): Result {
        val imageKey = inputData.getString("imageKey") ?: return Result.failure()
        val resId = inputData.getInt("imageId", -1) // imageId로 받음
        if (resId == -1) return Result.failure()

        // 1. 메모리 캐시 먼저 확인 (이 워커를 호출하기 전에 이미 확인했어야 함)
        // LruCacheManager.memoryCache.get(imageKey)?.let { return Result.success() } // 이미 있다면 성공

        // 2. 디스크 캐시 확인
        diskCacheManager.get(imageKey)?.let { bitmapFromDisk ->
            LruCacheManager.memoryCache.put(imageKey, bitmapFromDisk) // 메모리 캐시에도 추가
            // UI 업데이트 필요
            return Result.success()
        }

        // 3. 캐시에 없으면 디코딩하고 캐시에 추가
        val bitmap = decodeSampledBitmapFromResource(
            res = applicationContext.resources,
            resId = resId,
            reqWidth = 100, // 실제로는 ImageView 크기 등을 전달해야 함
            reqHeight = 100
        )

        return try {
            bitmap?.let {
                // 메모리 캐시에 추가
                LruCacheManager.memoryCache.put(imageKey, it)
                // 디스크 캐시에 추가
                diskCacheManager.put(imageKey, it)
                // UI 업데이트 필요
                Result.success()
            } ?: Result.failure()
        } catch (e: Exception) {
            Log.e("BitmapWorker", "Error caching bitmap", e)
            Result.failure()
        }
    }
}
```
이 워커는:
* 가능하면 디스크에서 읽습니다.
* 실패하면 디코딩으로 대체합니다.
* 결과를 메모리 및 디스크 캐시 모두에 저장합니다.
* 메인 스레드 외부에서 안전하게 실행됩니다.

*(주: 위 `BitmapWorker` 예시에서 `DiskCacheManager` 인스턴스는 워커가 실행될 때마다 새로 생성하면 디스크 캐시의 의미가 없습니다. `Application` 클래스 수준에서 싱글톤으로 관리하거나 의존성 주입(Dependency Injection)을 통해 동일한 인스턴스를 사용하도록 해야 합니다.)*

---

### 요약

안드로이드에서 큰 비트맵을 효율적으로 캐싱하려면, 빠른 최근 접근을 위해 **`LruCache`를 사용한 인메모리 캐싱**과 앱 세션 간 비트맵 유지를 위해 **`DiskLruCache`를 사용한 디스크 캐싱**을 활용하세요. 두 전략을 결합하고 구성 변경 시에도 메모리 캐시를 유지하여 원활한 경험을 제공하세요. `WorkManager`를 사용한 적절한 초기화 및 백그라운드 작업을 통해, 이 하이브리드 기능은 큰 비트맵으로 작업할 때 앱 성능과 사용자 경험을 향상시킬 수 있습니다.