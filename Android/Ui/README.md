# Android UI - Views

안드로이드 UI는 안드로이드 개발의 핵심으로, 애플리케이션의 구조와 상호작용성을 형성하는 화면, 레이아웃, 위젯 및 다양한 컴포넌트를 디자인하는 도구를 제공합니다. 개발의 다른 측면도 중요하지만, UI 시스템은 첫인상을 정의하고 의미 있는 사용자 상호작용을 용이하게 하므로 상당한 위치를 차지합니다. 시각적으로 매력적이고 반응성이 뛰어난 애플리케이션을 만들기 위해서는 안드로이드 UI에 대한 깊은 이해가 필수적입니다.

오늘날, [Jetpack Compose](https://developer.android.com/compose) 생태계는 빠르게 성장하여 현재 안드로이드 애플리케이션에서 운영 환경에 적합한(production-ready) UI를 구축하는 데 널리 채택되고 있습니다. Jetpack Compose가 안드로이드 UI 개발의 미래를 대표한다고 해도 과언이 아닙니다. 만약 안드로이드를 처음 시작하는 신규 개발자라면, 전통적인 뷰(View) 시스템을 먼저 배울 필요 없이 바로 _1장: Jetpack Compose 인터뷰 질문_ 으로 넘어갈 수 있습니다.

그렇긴 하지만, 일부 대기업들은 여전히 안드로이드 뷰 시스템에 크게 의존하고 있는데, 이는 Jetpack Compose로의 마이그레이션이 어렵고 단기 전략과 부합하지 않을 수 있기 때문입니다. 만약 이러한 회사들의 기술 면접을 준비하고 있다면, 전통적인 뷰 시스템에 대한 확실한 이해가 여전히 필수적일 수 있습니다.

안드로이드 뷰에서는, 모든 UI 요소가 기본적으로 메인 스레드에서 실행되기 때문에 고성능 애플리케이션을 구축하기 위해서는 뷰 생명주기(View lifecycle)와 일반적으로 사용되는 UI 컴포넌트에 대한 확실한 이해가 필수적입니다. 또한, 윈도우(Window)나 텍스트 단위(text units)와 같은 안드로이드 UI 시스템의 핵심 원칙을 이해하는 것은 개발자가 애플리케이션을 올바르게 구축하기 위한 정보에 입각한 결정을 내리는 데 도움이 됩니다.

많은 경우, 디자인팀의 복잡한 디자인 명세를 충족시키기 위해 사용자 정의 뷰(custom views)를 만들어야 합니다. 따라서 안드로이드 UI 시스템을 깊이 이해하는 것은 효율적인 개발을 위한 핵심 기술이며, 안드로이드 프레임워크를 따르는 훌륭한 안드로이드 개발자가 되기 위한 다음 단계입니다.

---

- [뷰(View) 생명주기 설명](./ViewLifecycle/README.md)
- [뷰(View)와 뷰그룹(ViewGroup)의 차이점은 무엇인가요?](./View-ViewGroup/README.md)
- [ViewStub을 사용해 본 적이 있으며, 이를 사용하여 UI 성능을 어떻게 최적화하나요?](./ViewStub/README.md)
- [사용자 정의 뷰(Custom View)는 어떻게 구현하나요?](./CustomView/README.md)
- [캔버스(Canvas)란 무엇이며 어떻게 활용하나요?](./Canvas/README.md)
- [뷰 시스템에서의 무효화(Invalidation)란 무엇인가요?](./Invalidation/README.md)
- [ConstraintLayout이란 무엇인가요?](./ConstraintLayout/README.md)
- [SurfaceView와 TextureView는 언제 각각 사용해야 하나요?](./SurfaceView-TextureView/README.md)
- [RecyclerView는 내부적으로 어떻게 작동하나요?](./RecyclerView/README.md)
- [Dp와 Sp의 차이점은 무엇인가요?](./Dp-Sp/README.md)
- [Nine-Patch 이미지의 용도는 무엇인가요?](./NinePatch/README.md)
- [드로어블(Drawable)이란 무엇이며, UI 개발에서 어떻게 활용되나요?](./Drawable/README.md)
- [안드로이드의 비트맵(Bitmap)이란 무엇이며, 큰 비트맵을 효율적으로 어떻게 처리하나요?](./Bitmap/README.md)
- [애니메이션은 어떻게 구현하나요?](./Animation/README.md)
- [윈도우(Window)란 무엇인가?](./Window/README.md)
- [웹 페이지는 어떻게 렌더링하나요?](./WebView/README.md)
