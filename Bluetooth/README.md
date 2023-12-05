# Bluetooth LE(Low Energy)

## [Bluetooth LE 소개](./introduction)
### Bluetooth LE
- 블루투스는 클래식과 저에너지로 구분
- Bluetooth LE는 최소한의 전력으로 작동하고 소량의 데이터만 전송해야 하는 **배터리 작동식 장치**에 더 적합
- 블루투스는 프로토콜 스택 아키텍처는 크게 Application, Host, Controller 3가지로 구분
### GAP: Device roles and topologies
- GAP 계층은 Bluetooth LE 네트워크의 노드에 대한 특정 장치 역할을 정의
- Bluetooth LE 프로토콜은 연결 지향 통신과 브로드캐스트 통신이라는 두 가지 통신 스타일을 지원
- 두 Bluetooth LE 장치가 서로 연결하려면 한 장치가 자신의 존재와 연결 의사를 광고하고 다른 장치가 그러한 장치를 검색해야 합니다.
### ATT & GATT: Data representation and exchange
- `ATT(속성 프로토콜)` 계층과 그 바로 위에 있는 `GATT(일반 속성 프로토콜)` 계층은 Bluetooth LE 장치 간에 데이터가 표현되고 교환되는 방식을 정의
- 일반 속성 프로파일(GATT) 계층은 ATT 계층 바로 위에 위치하며, 프로파일, 서비스 및 특성으로 속성을 계층적으로 분류하여 그 위에 구축됩니다. GATT 계층은 이러한 개념을 사용하여 Bluetooth LE 장치 간의 데이터 전송을 관리
### PHY: Radio modes
