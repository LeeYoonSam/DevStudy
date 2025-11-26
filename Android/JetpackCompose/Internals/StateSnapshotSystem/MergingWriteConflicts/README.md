# 쓰기 충돌 병합하기 (Merging write conflicts)

병합을 수행하기 위해 가변적인 스냅샷은 수정된 상태(로컬 변경) 목록을 순회하고, 모든 변경에 대해 아래 동작들을 수행합니다.

## 병합 프로세스

1. **현재 값 조회**: 부모 스냅샷 또는 전역 상태에서 현재 값(상태 기록)을 얻습니다.
2. **이전 값 조회**: 변경을 적용하기 전의 이전 값을 얻습니다.
3. **적용 후 상태 조회**: 변경을 적용한 후의 상태 객체가 가질 상태를 얻습니다.
4. **자동 병합 시도**: 이 세 가지를 자동으로 병합하려고 시도합니다. 이는 제공된 **병합 정책**에 의존하는 상태 객체에 위임됩니다.

## 현재 런타임의 충돌 처리

현재 런타임에는 적절한 병합을 지원하는 정책이 없으므로, 충돌하는 업데이트는 문제를 사용자에게 알리는 **런타임 예외**를 발생시키고 있습니다.

### 충돌 방지 전략

Compose는 다음과 같은 방법으로 충돌을 방지합니다:

- **고유 키 사용**: 상태 객체에 접근할 때 고유한 키를 사용하여 충돌이 불가능하도록 보장합니다
  - Composable 함수에서 기억된 상태 객체는 종종 고유한 접근 속성을 가집니다
- **StructuralEqualityPolicy**: `mutableStateOf`는 병합을 위해 `StructuralEqualityPolicy`를 사용
  - 객체의 두 버전을 깊은 동등 비교(`==`)를 통해 비교
  - 고유한 객체 키를 포함한 모든 속성이 비교되어 두 객체가 충돌할 수 없습니다

> 충돌하는 변경사항의 자동 병합은 Compose가 아직 사용하지 않는 잠재적인 최적화로 추가되었지만, 다른 라이브러리에서는 사용할 수 있습니다.

## 사용자 정의 SnapshotMutationPolicy

`SnapshotMutationPolicy` 인터페이스를 구현하여 사용자 정의 정책을 제공할 수 있습니다.

### CounterPolicy 예제

`MutableState<Int>`를 카운터로 처리하는 정책이 있습니다. 이 정책은 상태 값을 동일하게 변경하는 것에 대하여 변경으로 간주하지 않으므로, `counterPolicy`가 있는 가변적인 상태에 대한 변경은 절대 적용 사항에 대한 충돌을 일으키지 않습니다.

```kotlin
fun counterPolicy(): SnapshotMutationPolicy<Int> = object : SnapshotMutationPolicy<Int> {
  override fun equivalent(a: Int, b: Int): Boolean = a == b
  override fun merge(previous: Int, current: Int, applied: Int) =
    current + (applied - previous)
}
```

#### 동작 원리

- **equivalent 함수**: 두 값이 동일할 때만 가변적인 상태 값이 동등하다고 간주하기 때문에, 현재의 값이 유지될 수 있습니다
- **merge 함수**: 새로 적용된 값과 이전 값 사이의 차이를 가산함으로써 병합을 수행하므로, 현재 값은 항상 값이 변경된 총량을 반영합니다

### CounterPolicy 사용 예제

정책의 이름에서 알 수 있듯이, 스냅샷 내에서 소비되거나 생성된 자원의 양을 추적하는 등, 무언가를 계산할 때 유용할 수 있습니다.

```kotlin
val state = mutableStateOf(0, counterPolicy())
val snapshot1 = Snapshot.takeMutableSnapshot()
val snapshot2 = Snapshot.takeMutableSnapshot()
try {
  snapshot1.enter { state.value += 10 }
  snapshot2.enter { state.value += 20 }
  snapshot1.apply().check()
  snapshot2.apply().check()
} finally {
  snapshot1.dispose()
  snapshot2.dispose()
}

// State is now 30 as the changes made in the snapshots are added together.
```

**시나리오 설명**:
- 스냅샷 A가 10개의 객체를 생성
- 스냅샷 B가 20개의 객체를 생성
- A와 B를 모두 적용한 결과는 30개의 객체가 생성

카운터 정책을 사용하는 단일의 가변적인 상태와, 해당 상태를 수정하고 변경 사항을 적용하려는 몇 개의 스냅샷을 이용합니다. 이것은 충돌에 대한 완벽한 시나리오이지만, 카운터 정책을 감안할 때 모든 충돌을 완전히 피할 수 있습니다.

## 기타 충돌 없는 데이터 타입

충돌이 불가능한 구현의 다른 예시들:

- **추가 전용 Set**: 요소를 제거할 수 없고 오직 추가만 가능한 Set
- **Rope**: 특정 제약 조건을 감안할 때 충돌이 없는 데이터 타입으로 변환될 수 있는 유용한 타입

## 정책 선택 옵션

| 옵션 | 설명 |
|------|------|
| **기본 정책 사용** | 기본 정책들에 대한 충돌을 있는 그대로 받아들이고 사용 |
| **사용자 정의 정책** | `merge` 함수를 사용하여 데이터를 병합함으로써 충돌 문제를 해결하는 사용자 정의 정책을 따로 만들어서 제공 |

## 요약

- 가변적인 스냅샷의 병합 프로세스는 현재 값, 이전 값, 적용 후 상태를 조회하고 자동으로 병합을 시도합니다
- 현재 Compose 런타임은 적절한 병합 정책이 없어 충돌 시 런타임 예외를 발생시키며, 고유 키와 `StructuralEqualityPolicy`를 사용하여 충돌을 방지합니다
- `SnapshotMutationPolicy` 인터페이스를 구현하여 사용자 정의 병합 정책을 만들 수 있으며, `CounterPolicy`는 값의 변경 총량을 추적하는 대표적인 예제입니다
- `CounterPolicy`의 `merge` 함수는 차이를 가산하여 병합하므로, 여러 스냅샷의 변경사항을 충돌 없이 합칠 수 있습니다
- 추가 전용 Set이나 Rope와 같은 특정 제약 조건을 가진 데이터 타입도 충돌 없는 구현이 가능합니다
- 기본 정책을 사용하거나 사용자 정의 정책을 만들어 충돌 문제를 해결할 수 있습니다