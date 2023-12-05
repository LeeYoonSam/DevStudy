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