# Concurrency (동시성)

## Mutex

- 공유 데이터를 멀티 스레드가 동시에 접근하는것을 막기위해 사용하는 클래스
- locking method
    - lock: mutex 를 잠그고 mutex 를 사용할수 없는 경우 차단
    - try_lock: mutex 를 잠그도록 시도하고 mutex 를 사용할수 없는경우 반환
    - unlock: mutex 를 잠금해제

### 여러 스레드가 하나의 Resource 에 접근해서 작업하는 예
- `Resource`
    - share memory
    - file access
    - job queue
- 처음 접근하는 스레드가 `Resource` 에 접근해서 read, write, delete 를 할수 있다.
- 다음 스레드가 이 `Resource` 에 접근해서 동시에 read, write, delete 를 할수 있을때 이런 상태를 [Race Condition](./../Parallel#Race-Condition)으로 볼수 있다.

<br/>

- Mutex 로 이러한 문제를 방지
    - 첫번째 스레드가 `Resource` 안으로 들어가기 직전에 mutex Lock 을 걸어놓고 원하는 작업을 수행
    - 다음 스레드가 `Resource` 에 접근하려고 할때 이미 mutex Lock 이 방어막을 치고 있기 때문에 안으로 들어가지 못한다.
        - 첫번째 스레드가 걸어놓은 mutex 와 같은 mutex 에 두번째 스레드도 Lock 을 걸수 있는데 이미 걸려서 락을 걸수 없기 때문에 이 스레드는 wait 상태로 넘어가게됨
    - 첫번째 스레드는 작업을 마치게되면 mutex.unlock 을 해준다.
    - wait 상태에 있던 unlock 이 된 mutex 를 가져와서 다시 lock 을 걸어주고 작업을 진행
    - try_lock
        - 다른 스레드가 `Resource` 에 lock 이 걸려있을때 wait 상태로 넘어가지 않고 Lock 이 실패하면 자기 상태로 돌아갈수 있는 명령어
    - 순서를 보장하진 않지만 무엇이 되던 하나의 스레드만 `Resource`  에 접근할수 있도록 보장한다.

<br/>

- 사실은 mutex lock, unlock 행위자체가 무거운 행위
    - 사용을 최대한 배제해야한다 (lock free)
    - 피할수있으면 최대한 피하는것이 좋다.
