# @InternalComposeApi

Compose에서 **일부 외부로 노출된 API 표면이 더 이상 변경되지 않고, stable 버전에 포함되었음에도 불구하고 내부적으로는 변화가 발생할 수 있음으로 internal(내부)로 지정**됩니다. 이 어노테이션은 **Kotlin이 지원하지 않는 개념인 모듈 전체에서의 사용을 허용하므로 Kotlin의 internal 키워드보다 더 넓은 범위**를 갖습니다.