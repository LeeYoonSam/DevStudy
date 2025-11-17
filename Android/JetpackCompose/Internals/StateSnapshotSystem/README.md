# 5. 상태 스냅샷 시스템 (State snapshot system)

Jetpack Compose에는 상태를 표현하고 상태 변경 사항을 전파하여 궁극적으로 반응형 경험을 제공하는 상태 스냅샷 시스템이라는 특별한 방법이 있습니다. 이 반응형 모델을 사용하면 구성 요소가 입력값을 기반으로 필요한 경우에만 자동적으로 recomposition 할 수 있습니다. 따라서, 변경 사항을 수동으로 알려야 하는 경우(과거에 Android View 시스템에서 해왔던 것처럼) 요구되는 모든 보일러 플레이트를 피할 수 있으며, 이는 코드를 더욱 강력하고 간결하게 합니다.

---

## 관련 문서

- [스냅샷 상태란 (What snapshot state is)](./SnapshotState/README.md)
- [동시성 제어 시스템 (Concurrency Control Systems)](./ConcurrencyControlSystems/README.md)
- [다중 버전 동시성 제어 (Multiversion Concurrency Control)](./MultiversionConcurrencyControl/README.md)
- [스냅샷 (The Snapshot)](./Snapshot/README.md)
- [스냅샷 트리 (The Snapshot Tree)](./SnapshotTree/README.md)
- [스냅샷과 쓰레딩 (Snapshots and Threading)](./SnapshotsThreading/README.md)
- [읽고 쓰기 관찰하기 (Observing reads and writes)](./ObservingReadsWrites/README.md)

