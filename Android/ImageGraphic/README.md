# ImageGraphic
- [비트맵 처리](#비트맵-처리)

---

## [비트맵 처리](https://developer.android.com/topic/performance/graphics)

### Android 앱에서 비트맵을 로드하는 것이 까다로운 데에는 다음과 같은 몇 가지 이유
- 비트맵은 앱의 메모리 예산을 매우 쉽게 소모할 수 있습니다. 예를 들어, Pixel 스마트폰의 카메라는 최대 4048x3036 픽셀(12 메가픽셀)의 사진을 찍습니다. 사용된 비트맵 구성이 [ARGB_8888](https://developer.android.com/reference/android/graphics/Bitmap.Config)인 경우 Android 2.3(API 수준 9) 이상에서 사진 한 장을 메모리로 로드하는 데 기본적으로 약 48MB(4048*3036*4 바이트)의 메모리가 소모됩니다. 이렇게 대량의 메모리를 요구하면 앱에서 사용할 수 있는 모든 메모리가 즉시 소모될 수 있습니다.
- 비트맵을 UI 스레드로 로드하면 앱의 성능이 저하되어 응답이 느려지거나 ANR 메시지가 전송될 수 있습니다. 따라서 비트맵을 사용할 때는 스레드를 적절하게 관리하는 것이 중요합니다.
- 앱이 메모리에 여러 개의 비트맵을 로드한다면 메모리와 디스크 캐싱을 능숙하게 관리해야 합니다. 그러지 않으면, 앱 UI의 반응 능력과 유동성에 좋지 않은 영향을 미칠 수 있습니다.

### Glide
- 대부분의 경우 `Glide` 라이브러리를 사용하여 앱에서 비트맵을 가져오고 디코딩하고 표시하는 것을 추천합니다. 
- `Glide`는 이러한 작업을 비롯하여, Android에서 비트맵과 기타 이미지를 사용하는 것과 관련된 다른 작업을 처리할 때 대부분의 복잡성을 추상화합니다.

### Android 프레임워크에 내장된 하위 레벨 API를 이용하여 직접 작업
- [큰 비트맵을 효율적으로 로드](https://developer.android.com/topic/performance/graphics/load-bitmap)
- [비트맵 캐싱](https://developer.android.com/topic/performance/graphics/cache-bitmap)
- [비트맵 메모리 관리](https://developer.android.com/topic/performance/graphics/manage-memory)