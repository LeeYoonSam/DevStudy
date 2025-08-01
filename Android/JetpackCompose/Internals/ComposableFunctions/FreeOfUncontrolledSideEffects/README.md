# 통제되지 않은 사이드 이펙트 방지 (Free of uncontrolled side effects)

`사이드 이펙트(side effect)`는 **호출된 함수의 제어를 벗어나서 발생할 수 있는 예상치 못한 모든 동작**을 의미합니다. 로컬 캐시에서 데이터 읽기, 네트워크 요청 작업, 전역 변수 설정과 같은 것들이 사이드 이펙트로 간주됩니다. 이들은 호출하는 함수가 그 동작에 영향을 미칠 수 있는 외부 요인에 의존하게 만듭니다. 가령, 다른 스레드에서 작성될 수 있는 외부 상태, 예외를 던질 수 있는 서드파티 API 등이 사이드 이펙트에 해당합니다. 다시 말해, **함수는 결과를 생성하기 위해서 입력값에만 의존하게 되는 것이 아니라, 외부 요인에도 의존**하게 됩니다.


이러한 사이드 이펙트는 모호함의 근원이라고 볼 수 있습니다. Compose Runtime은 Composable 함수가 예측 가능하도록(결정론적인) 기대하기 때문에 사이드 이펙트가 포함된 Composable 함수는 예측이 어려워지고, 결과적으로 Compose에게 좋지 않습니다. 이 말인 즉, 사이드 이펙트는 Compose 내에서 아무 통제를 받지 않고 여러 번 실행될 수 있습니다. **Composable 함수가 사이드 이펙트를 실행한다면 매 함수 호출 시마다 새로운 프로그램 상태를 생성할 수 있으므로, Compose Runtime에게 필수적인 멱등성을 따르지 않게 됩니다.**


Composable 함수 내에서 직접 네트워크 요청을 실행하는 상황을 상상해 보겠습니다.

```kotlin
@Composable fun EventsFeed(networkService: EventsNetworkService) {
  val events = networkService.loadAllEvents()

  LazyColumn {
    items(events) { event ->
      Text(text = event.name)
    }
  }
}
```

위의 예시는 상당히 위험할 수 있습니다. 그 이유는, Composable 함수는 근본적으로 Compose Runtime에 의해 짧은 시간 내에 여러 번 다시 실행될 수 있으며, 이로 인해 네트워크 요청이 여러 번 수행되어 우리의 제어를 벗어날 수 있습니다. 더 최악의 상황은 이러한 사이드 이펙트가 아무 조건 없이 다른 스레드에서 실행될 수 있다는 것입니다.


> Compose Runtime은 Composable 함수에 대한 실행 전략을 선택할 권한을 보유하고 있습니다. 이는 하드웨어의 멀티 코어의 이점을 활용하기 위해 recomposition을 다른 스레드로 이전시킬 수 있거나, 필요성이나 우선순위에 따라 임의의 순서로 실행할 수 있습니다(예시로, 화면에 보이지 않는 Composable은 낮은 우선순위로 할당될 수 있습니다).


다른 일반적인 사이드 이펙트 유의사항은 우리가 Composable 함수를 다른 Composable 함수의 결과에 의존하도록 만들고, 순서에 어떠한 관계를 부여하는 것입니다. 이런 로직은 무슨 수를 써서라도 피해야 합니다. 아래의 샘플을 예로 살펴볼 수 있습니다.

```kotlin
@Composable
fun MainScreen() {
  Header()
  ProfileDetail()
  EventList()
}
```

이 코드에서, Header, ProfileDetail, EventList는 Composable 함수는 Compose Compiler에 의해 어떤 순서로든, 심지어 병렬로도 실행될 수 있습니다. 따라서 특정 실행 순서를 가정하는 로직을 작성해서는 안 됩니다. 가령, Header에서 외부 변수를 업데이트하고 ProfileDetail에서 그 외부 변수를 읽어 들이는 경우입니다.

일반적으로 말하자면, 사이드 이펙트는 Composable 함수에서 이상적이지 않습니다. **우리는 모든 Composable 함수를 stateless(무상태, 상태를 보존하지 않음)하게 만들려고 노력해야 합니다.** 그래서 Composable 함수는 모든 입력값은 매개변수로서 받고, 결과를 생성하기 위해 주어진 입력값만을 사용합니다. 이것은 **Composable을 더 어리석어 보일 정도로 단순하고, 높은 재사용성**을 갖게 합니다. 그러나, stateful(상태유지, 상태를 보유하는) 프로그램을 개발하기 위해서는 사이드 이펙트가 필요하므로, 특정 단계에서는 우리가 사이드 이펙트를 실행해야만 합니다 (주로 Composable 트리의 루트에서 빈번하게 실행됩니다). 프로그램은 네트워크 요청을 실행하고, 데이터베이스에 정보를 저장하고, 메모리 캐시를 사용하는 등의 작업이 필요합니다.


이러한 이유로, Jetpack Compose는 **Composable 함수에서 안전하고 통제된 환경 내에서 이펙트(effect)7를 호출할 수 있는 이펙트 핸들러(effect handlers)와 같은 메커니즘을 제공**합니다.


`이펙트 핸들러`는 사이드 이펙트가 Composable의 라이프사이클을 인식하도록 하여, 해당 **라이프사이클에 의해 제한되거나 실행**될 수 있게 합니다. 또한, **Composable 노드가 트리를 떠날 때 자동으로 이펙트를 해제하거나 취소**할 수 있게 하고, 이펙트에 주어진 입력값이 변경되면 재실행시키거나, 심지어 **동일한 이펙트를 recomposition 과정에서 유지시키고 한 번만 호출**되게 할 수 있습니다. 추후에 이에 대해서는 자세히 다룰 예정입니다. 이펙트 핸들러는 **Composable 함수 내에서 아무런 제어를 받지 못하고 직접적으로 이펙트가 호출되는 것을 방지**하는 역할을 수행합니다.


## 요약
* 컴포저블 함수는 **멱등성(Idempotent)** 을 유지하기 위해 **통제되지 않은 사이드 이펙트(side effect)가 없어야 합니다.** 사이드 이펙트는 함수의 입력값 외에 외부 상태에 의존하거나 수정하는 모든 동작을 의미합니다.
* Compose 런타임은 최적화를 위해 컴포저블을 **언제든, 어떤 순서로든, 심지어 병렬로도 재실행(리컴포지션)** 할 수 있습니다. 만약 함수 내에 직접적인 사이드 이펙트(예: 네트워크 요청)가 있다면, 이는 예측 불가능하게 여러 번 호출될 수 있습니다.
* 이상적인 컴포저블은 **상태가 없고(stateless)**, 오직 파라미터로 전달된 입력값에만 의존하여 UI를 기술해야 합니다. 이는 재사용성을 높이고 예측 가능하게 만듭니다.
* 실제 앱에서 꼭 필요한 사이드 이펙트를 처리하기 위해, Jetpack Compose는 **이펙트 핸들러(Effect handlers)**(예: `LaunchedEffect`, `DisposableEffect`)를 제공합니다. 이 핸들러들은 사이드 이펙트가 컴포저블의 생명주기에 맞춰 안전하고 통제된 환경에서 실행되도록 보장합니다.