# Spotless
대규모 코드 베이스에서 일관된 코드 스타일을 보장하는 코드 포맷팅 도구

포맷팅 규칙을 자동으로 적용하여 깔끔하고 읽기 쉬운 코드를 유지 관리하기 쉽게 해줍니다.

- | -
--- | ---
Code Formattion | Code Cleanup
CI/CD Integration | Customizable Configuration
IDE Support | Gradle Task Integration

## 설정

1. libs.versions.toml
- version 추가 (사용할 버전에 맞게 수정)
  - spotless = "6.25.0"
- plugin 추가
  ```
  plugins {
    ...
    spotless = { id = "com.diffplug.spotless", version.ref = "spotless" }
  }
  ```

2. build.gradle.kts
- `alias(libs.plugins.spotless) apply false` 추가
- subprojects 추가
  ```
  subprojects {
    apply<SpotlessPlugin>()
    configure<SpotlessExtension> {
      kotlin {
        target("src/**/*.kt")
        targetExclude("build/**/*.kt")
        ktlint()
          .editorConfigOverride(
            mapOf(
              "max_line_length" to "140",
              "ij_kotlin_allow_trailing_comma" to "false",
              "ktlint_standard_filename" to "disabled",
              "ij_kotlin_allow_trailing_comma_on_call_site" to "false",
              "ij_kotlin_line_break_after_multiline_when_entry" to "false",
              "ktlint_standard_no-empty-first-line-in-method-block" to "enabled",
              "ktlint_fuction_signature_body_expression_wrapping" to "multiline",
              "ktlint_fuction_signature_rule_force_multiline_when_parameter_count_greater_or_equal_than" to "2",
              "ktlint_fuction_naming_ignore_when_annotated_with" to "Composable, Test",
            )
          )
      }
      
      kotlinGradle {
        target("*.kts")
        ktlint()
      }
    }

    afterEvaluate {
      tasks.withType<KotlinCompile> {
        finalizedBy("spotlessApply")
      }
    }
  }
  ```

### configure
**kotlin**
- src 폴더에 있는 모든 Kotlin 파일이 대상
- 빌드 폴더에 있는 Kotlin 파일 제외

**Android 프로젝트에서 사용자 정의 스타일 규칙을 적용하도록 Kotlin 구성**
- ktlint().editorConfigOverride
- 다양한 코딩 규칙을 사용자 정의 가능
- [Ktlint](https://pinterest.github.io/ktlint/1.5.0/)

**kotlinGradle**
- .kts extension 파일이 대상
- Gradle 스타일 규칙을 강제 적용하는 ktlint() 추가

**afterEvaluate**
- Kotlin 컴파일 작업 후에 완벽한 서식이 자동으로 적용

### 설정 확인
```
./gradlew spotlessApply
```