# Recomposer 생성 (Spawning the Recomposer)

> 플랫폼별 진입점에서 `Composition`의 부모로 `Recomposer`를 연결해 수명 주기를 인지하고 안전하게 `Recomposition`을 수행합니다.

## 개요

- `setContent`를 통해 컴포즈를 시작할 때, 루트 `Composition`의 부모가 `Recomposer`여야 하는 경우가 있습니다.
- 안드로이드에서는 `Compose UI`가 `Composition`(내부적으로 `Composer` 포함)과 그 부모로 사용할 `Recomposer`를 함께 생성합니다.
- 각 플랫폼은 자체 `Composition`을 생성하듯, 보통 자체 `Recomposer`도 생성합니다.

## Android의 연결 지점: WindowRecomposerFactory

- `ViewGroup.setContent(...)`는 내부적으로 `WindowRecomposerFactory.LifecycleAware`를 통해 상위 컨텍스트에 연결 가능한 `Recomposer`를 생성합니다.

```kotlin
  fun interface WindowRecomposerFactory {
    fun createRecomposer(windowRootView: View): Recomposer

    companion object {
      val LifecycleAware: WindowRecomposerFactory = WindowRecomposerFactory { rootView ->
        rootView.createLifecycleAwareViewTreeRecomposer()
      }
    }
  }
```

- `createRecomposer` 호출 시 루트 `View`를 전달합니다. 생성된 `Recomposer`는 `ViewTreeLifecycleOwner`에 연결되어 수명 주기를 인지합니다.
- 뷰 트리가 분리되면 `Recomposer`가 취소되어 누수를 방지합니다.

## AndroidUiDispatcher와 FrameClock

- Compose UI에서 발생하는 일은 `AndroidUiDispatcher`를 통해 조정·전달됩니다.
- `AndroidUiDispatcher`는 `Choreographer`와 메인 `Looper`의 `Handler`, 그리고 `MonotonicFrameClock`과 연결됩니다.
- 창이 보이지 않는 동안 프레임 생성을 중단할 수 있도록 `PausableMonotonicFrameClock`을 사용합니다.

| 요소 | 설명 |
| --- | --- |
| `AndroidUiDispatcher` | 메인 스레드에서 UI 이벤트를 디스패치, 프레임 단계와 연동 |
| `Choreographer` | 시스템 프레임 스케줄링 제공 |
| `MonotonicFrameClock` | 프레임 동기화 일시정지/재개의 기준 시간원 제공 |
| `PausableMonotonicFrameClock` | `withFrameNanos` 전달을 일시 정지/재개 가능한 래퍼 |

## Recomposer 생성 컨텍스트 구성

- `Recomposer` 인스턴스화에는 `CoroutineContext`가 필요합니다. 일반적으로 현재 스레드 컨텍스트 + (일시 정지 가능한) 프레임 클록을 결합합니다.

```kotlin
  val contextWithClock = currentThreadContext + (pausableClock ?: EmptyCoroutineContext)
  val recomposer = Recomposer(effectCoroutineContext = contextWithClock)
```

- 이 컨텍스트는 변경 사항 적용 및 `LaunchedEffect` 등 이펙트 실행의 기본 컨텍스트로 사용됩니다.
- `Recomposer`가 종료되면 연결된 작업(Job)과 이펙트가 함께 취소되어 누수를 방지합니다.

## Recomposition 작업 실행과 수명 주기 연동

- 동일한 컨텍스트로 `CoroutineScope`를 만들고 수명 주기에 따라 재구성 작업을 실행합니다.

```kotlin
  viewTreeLifecycleOwner.lifecycle.addObserver(
    object : LifecycleEventObserver {
      override fun onStateChanged(lifecycleOwner: LifecycleOwner, event: Lifecycle.Event) {
        val self = this
        when (event) {
          Lifecycle.Event.ON_CREATE ->
            runRecomposeScope.launch(start = CoroutineStart.UNDISPATCHED) {
              try {
                recomposer.runRecomposeAndApplyChanges()
              } finally {
                lifecycleOwner.lifecycle.removeObserver(self)
              }
            }
          Lifecycle.Event.ON_START -> pausableClock?.resume()
          Lifecycle.Event.ON_STOP -> pausableClock?.pause()
          Lifecycle.Event.ON_DESTROY -> recomposer.cancel()
        }
      }
    }
  )
```

- `ON_CREATE`에서 `recomposer.runRecomposeAndApplyChanges()`를 시작합니다.
- `ON_START`/`ON_STOP`에서 프레임 클록을 재개/일시정지하여 불필요한 프레임 생성을 막습니다.
- `ON_DESTROY`에서 `Recomposer`를 취소합니다.

## setContent와 Recomposer 전달

- `setContent` 호출 시 `parent` 인자로 `Recomposer`가 전달됩니다.

```kotlin
  internal fun ViewGroup.setContent(
    parent: CompositionContext, // Recomposer 전달
    content: @Composable () -> Unit
  ): Composition {
    val composeView = /* ... */
    return doSetContent(composeView, parent, content)
  }

  private fun doSetContent(
    owner: AndroidComposeView,
    parent: CompositionContext,
    content: @Composable () -> Unit
  ): Composition {
    val original = Composition(UiApplier(owner.root), parent)
    val wrapped = owner.view.getTag(R.id.wrapped_composition_tag)
      as? WrappedComposition ?: WrappedComposition(owner, original).also {
        owner.view.setTag(R.id.wrapped_composition_tag, it)
      }
    wrapped.setContent(content)
    return wrapped
  }
```

## 동작 다이어그램

```mermaid
flowchart TD
  A[ViewGroup.setContent] --> B[WindowRecomposerFactory.LifecycleAware]
  B --> C[createLifecycleAwareViewTreeRecomposer]
  C --> D[Recomposer 생성]
  D --> E[CoroutineScope(contextWithClock)]
  E --> F[LifecycleEventObserver 등록]
  F --> G[runRecomposeAndApplyChanges]
  F --> H[pause/resume FrameClock]
  F --> I[cancel on DESTROY]
```

## 핵심 포인트

- **루트 뷰**를 통해 `Recomposer`를 생성하고 `ViewTreeLifecycleOwner`에 연결합니다.
- `AndroidUiDispatcher`와 **프레임 클록**을 통해 시스템 프레임과 **정확히 동기화**합니다.
- 수명 주기 이벤트로 `runRecomposeAndApplyChanges()` 실행/정지/취소를 **명확히 제어**합니다.
- `setContent`의 `parent`로 `Recomposer`를 전달하여 **플랫폼-런타임 통합**을 완성합니다.

## 요약
- 수명 주기 인지형 `Recomposer`를 생성해 누수 없이 재구성을 구동합니다.
- `AndroidUiDispatcher`와 `PausableMonotonicFrameClock`으로 프레임 동기화·일시정지를 제어합니다.
- `LifecycleEventObserver`에서 `runRecomposeAndApplyChanges()`를 실행하고 종료 시 안전하게 취소합니다.
- `setContent(parent = Recomposer)`로 플랫폼과 런타임을 연결합니다.
