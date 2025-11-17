# 읽고 쓰기 관찰하기 (Observing reads and writes)

**Compose Runtime**에는 관찰된 상태 값이 변경될 때 **recomposition**을 트리거하는 기능이 있습니다. 이전 장에서 이미 설명한 기계 장치인 **상태 스냅샷 시스템**에 어떻게 연결되어 있는지 선행적으로 이해하신다면 도움이 됩니다. 먼저 읽기를 관찰하는 방법부터 살펴보도록 하겠습니다.

---

## 읽기 관찰 (Observing Reads)

### ReadonlySnapshot과 readObserver

스냅샷을 찍을 때마다(예를 들어 `Snapshot.takeSnapshot()` 함수 호출 등을 통해), 우리가 반환받는 것은 `ReadonlySnapshot`입니다. 이 스냅샷의 상태 객체는 수정할 수 없고 읽는 것만 가능하므로, 스냅샷의 모든 상태는 삭제될 때까지 보존됩니다.

`takeSnapshot` 함수를 사용하면 **`readObserver`**(선택적인 매개변수)를 전달할 수 있습니다. 이 관찰자는 `enter` 호출 내에서 스냅샷에서 상태 객체를 읽을 때마다 알림을 받습니다.

```kotlin
// simple observer to track the total number of reads
val snapshot = Snapshot.takeSnapshot { reads++ }
// ...
snapshot.enter { /* some state reads */ }
// ...
```

### snapshotFlow 예시

이에 대한 한 가지 예는 `snapshotFlow` 함수입니다:

```kotlin
fun <T> snapshotFlow(block: () -> T): Flow<T>
```

이 함수는 `State<T>` 객체를 `Flow`로 변환합니다. 값을 수집하면 `block` 람다식을 실행하고 그 람다식 안에서 읽은 `State` 객체의 결과를 내보냅니다. 읽어들이는 `State` 객체 중 하나가 변경되면 `Flow`는 새 값을 관찰자 측에 전달합니다.

이 동작을 달성하려면 상태 객체가 변경될 때마다 `block` 람다식을 다시 실행할 수 있도록 모든 상태에 대해 읽어들이는 동작을 기록해야 합니다. 이 동작에서 읽기를 추적하기 위해 읽기 전용 스냅샷을 만들고, 읽기 작업을 위한 관찰자를 전달하여 `Set`에 저장합니다.

```kotlin
fun <T> snapshotFlow(block: () -> T): Flow<T> {
  // ...
  snapshot.takeSnapshot { readSet.add(it) }
  // ...
  // Do something with the Set
}
```

### 중첩된 스냅샷의 읽기 전파

읽기 전용 스냅샷은 일부 상태를 읽을 때 값을 관찰하는 `readObserver`에 알릴 뿐만 아니라, **상위 스냅샷의 `readObserver`에도 알립니다**. 중첩된 스냅샷에 대한 읽기 작업은 모든 상위 스냅샷과 관련된 관찰자에게 전달되어야 하므로, **스냅샷 트리의 모든 관찰자는 읽어들이는 상태 값의 변경에 대해 알림을 받습니다**.

---

## 쓰기 관찰 (Observing Writes)

### MutableSnapshot과 writeObserver

관찰자는 값 쓰기(상태 업데이트)에 대해서도 연동이 가능하고, **`writeObserver`는 가변적인 스냅샷을 생성할 때만 전달할 수 있습니다**. 가변적인 스냅샷은 보유한 상태값을 변경할 수 있는 스냅샷입니다.

`Snapshot.takeMutableSnapshot()`을 호출하여 가변적인 스냅샷을 생성할 수 있습니다. 여기서 값을 읽어들이거나 쓰는 작업에 대한 알림을 받기 위해 선택적으로 읽기 및 쓰기 작업에 대한 관찰자를 전달할 수 있습니다.

---

## Recomposer에서의 활용

### 읽기 및 쓰기 추적

읽기 및 쓰기 작업을 관찰하는 것에 대한 좋은 예는 **composition에 대한 모든 읽기 및 쓰기를 추적하여 필요할 때 자동으로 recomposition을 트리거할 수 있는 `Recomposer`** 입니다. 아래의 `Recomposer`에 구현된 일부 메서드를 통해서 확인할 수 있습니다.

```kotlin
private fun readObserverOf(composition: ControlledComposition): (Any) -> Unit {
  return { value -> composition.recordReadOf(value) } // recording reads
}

private fun writeObserverOf(
  composition: ControlledComposition,
  modifiedValues: IdentityArraySet<Any>?
): (Any) -> Unit {
  return { value ->
    composition.recordWriteOf(value) // recording writes
    modifiedValues?.add(value)
  }
}

private inline fun <T> composing(
  composition: ControlledComposition,
  modifiedValues: IdentityArraySet<Any>?,
  block: () -> T
): T {
  val snapshot = Snapshot.takeMutableSnapshot(
    readObserverOf(composition),
    writeObserverOf(composition, modifiedValues)
  )
  try {
    return snapshot.enter(block)
  } finally {
    applyAndCheck(snapshot)
  }
}
```

### composing 함수의 역할

`composing` 함수는 **초기 composition을 생성할 때와 모든 recomposition에 대해 호출됩니다**. 이 로직은 상태를 읽을 수 있을 뿐만 아니라 쓸 수도 있도록 허용하는 `MutableSnapshot`을 사용하며, `block` 람다식을 통해 모든 읽기 또는 쓰기 작업이 composition에 의해 추적(통지)됩니다.

여기에 전달된 `block` 람다식은 본질적으로 **composition 또는 recomposition 자체를 실행하는 코드**이므로, 트리에서 모든 **Composable** 함수를 실행하여 변경 사항 목록을 계산합니다. 이는 `enter` 함수 내에서 발생하므로 모든 읽기 또는 쓰기 작업들이 자동으로 추적됩니다.

### Recomposition 트리거 메커니즘

스냅샷 상태 쓰기 작업이 composition을 통해 추적될 때마다, 동일한 해당 스냅샷 상태를 읽어들이는 **`RecomposeScope`가 무효화(invalidate)되고 recomposition이 트리거됩니다**.

결국 `applyAndCheck(snapshot)` 호출은 **composition 중 발생하는 모든 변경 사항을 다른 스냅샷 및 전역 상태에 전파**합니다.

---

## Observer 구현

### 간단한 함수 형태

관찰자는 실로 간단한 함수로 구현되어 있으며, 코드에서는 아래와 같은 형태입니다:

```kotlin
readObserver: ((Any) -> Unit)?
writeObserver: ((Any) -> Unit)?
```

### Snapshot.observe 유틸리티

현재 스레드에서 읽기 및 쓰기 관찰을 시작하는 몇 가지 유틸리티 기능이 있습니다. 그중 `Snapshot.observe(readObserver, writeObserver, block)`가 있습니다.

이 함수는 **`derivedStateOf`에서 제공된 람다식의 모든 객체를 읽어들이고 반응하는 데 사용됩니다**. 이 함수는 `TransparentObserverMutableSnapshot`을 사용하는 유일한 함수입니다(이전 섹션에서 살펴본 사용 가능한 Snapshot 유형 중 하나입니다).

이 유형의 상위(루트) 스냅샷은 이전 섹션에서 설명한 대로 관찰자에게 읽기 작업을 알리는 유일한 목적으로 생성됩니다. 이 스냅샷은 특정 상황에 대해 스냅샷에 콜백 목록이 필요하지 않도록 구현되었습니다.

---

## 요약

- **`ReadonlySnapshot`** 은 `readObserver`를 통해 상태 읽기를 추적하며, 스냅샷 트리의 상위 관찰자에게도 알림을 전파합니다.
- **`snapshotFlow`** 는 `State<T>`를 `Flow`로 변환하며, 읽기 관찰자를 사용하여 상태 변경을 추적하고 새 값을 방출합니다.
- **`MutableSnapshot`** 은 `writeObserver`를 통해 상태 쓰기 작업을 추적할 수 있으며, `Snapshot.takeMutableSnapshot()`으로 생성합니다.
- **`Recomposer`** 는 읽기/쓰기 관찰자를 활용하여 composition의 모든 상태 접근을 추적하고, 필요 시 자동으로 recomposition을 트리거합니다.
- **`composing` 함수**는 초기 composition과 모든 recomposition에서 호출되며, `MutableSnapshot`을 사용하여 모든 상태 변경을 추적합니다.
- 상태 쓰기가 추적되면 해당 상태를 읽는 **`RecomposeScope`가 무효화**되어 recomposition이 트리거됩니다.
- 관찰자는 `((Any) -> Unit)?` 형태의 간단한 함수로 구현되며, `Snapshot.observe` 유틸리티로 스레드별 관찰을 시작할 수 있습니다.
- **`derivedStateOf`**는 `TransparentObserverMutableSnapshot`을 사용하여 읽기 작업을 효율적으로 추적합니다.