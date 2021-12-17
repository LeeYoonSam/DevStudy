# Todo
나중에 알아보고 싶은 주제를 기록

## 안드로이드 기술 관련
### 현재 구현된 UseCase 의 동작을 설명
### 코루틴에서 viewModelScope 를 사용하는 이유는? 다른 CoroutineScope 를 사용하면 안되는지?
### NetworkModule 에서 Object, provide 모두 SingleTon을 사용한 이유는?
### Kotlin Scope Function 설명
### executePendingBindings 을 사용한 이유는?
### LiveData 와 ObservableField 를 섞어 사용하는 이유?
### ObservableField 를 사용할때 화면 구성이 변경되었을때 데이터가 어떻게 되는지?

## 안드로이드 View 관련
### FrameLayout 과 ConstraintLayout 차이

### 느린 렌더링
- [느린 렌더링](https://developer.android.com/topic/performance/vitals/render?hl=fr#visual-inspection)
- [GPU 렌더링](https://developer.android.com/topic/performance/rendering/inspect-gpu-rendering?hl=fr#profile_rendering)
- [프로필 GPU 렌더링으로 분석](https://developer.android.com/topic/performance/rendering/profile-gpu?hl=fr)
- [오버 드로우 줄이기](https://developer.android.com/topic/performance/rendering/overdraw?hl=fr)
- Glide 에서 기본적으로 로드하는 방식을 사용하면 이미지가 길거나 클때 자동으로 리사이즈를 처리

### Glide 에서 리사이즈를 처리하지 않게 하는 방법 2가지
1. Target.SIZE_ORIGINAL 사용
```
.override(view.context.getDisplayInfo().widthPixels, Target.SIZE_ORIGINAL)
```
- Target.SIZE_ORIGINAL 을 사용해서 실제 이미지의 height 에 맞춰서 세팅하도록 설정

- 일반적으로는 큰 이슈가 없을것으로 예상되나, 큰 이미지를 여러개 보여줘야 한다면 GPU 렌더링시 문제 발생

2. CustomTarget 을 사용해서 비트맵을 직접 사용
```
.into(object : CustomTarget<Bitmap>() {
    override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
        resource.run {
            prepareToDraw()
            view.setImageBitmap(resource)
        }
    }

    override fun onLoadCleared(placeholder: Drawable?) {
        view.setImageDrawable(placeholder)
    }
})
```

- 비트맵을 로드하기전 prepareToDraw() 를 사용해서 이미지 디코딩된 캐시를 활용
- override 로 사이즈를 조절하지 않고 받은 비트맵을 그대로 사용
- scaleType 등이 적용되면 이미지가 작게 보일수 있음
- android:adjustViewBounds="true" 를 적용해서 종횡비에 맞게 뷰크기를 결정

### prepareToDraw

그리는 데 사용되는 비트맵과 연결된 캐시를 만듭니다.

Build.VERSION_CODES.N부터 이 호출은 Bitmap이 아직 업로드되지 않은 경우 RenderThread의 GPU에 대한 비동기 업로드를 시작합니다. 하드웨어 가속을 사용하면 비트맵이 렌더링되기 위해 GPU에 업로드되어야 합니다. 이것은 기본적으로 비트맵을 처음 그릴 때 수행되지만 비트맵의 크기에 따라 프로세스에 몇 밀리초가 걸릴 수 있습니다. Bitmap을 수정하고 다시 그릴 때마다 다시 업로드해야 합니다.

이 메서드를 미리 호출하면 사용되는 첫 번째 프레임에서 시간을 절약할 수 있습니다. 예를 들어 디코딩된 비트맵이 표시될 때 이미지 디코딩 작업자 스레드에서 이것을 호출하는 것이 좋습니다. 이 메서드를 호출하기 전에 Bitmap을 미리 그리기 수정하는 것이 좋습니다. 그러면 캐시되고 업로드된 사본을 다시 업로드하지 않고도 재사용할 수 있습니다.
