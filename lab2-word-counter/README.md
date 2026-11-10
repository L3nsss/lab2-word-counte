# Лабораторна робота 2 — Частотний аналізатор тексту

Проєкт **lab2-word-counter** до дисципліни «Технології розробки на платформі Java». Реалізовано частотний аналіз тексту за допомогою стандартних колекцій Java, збірку Gradle, керування версіями залежностей через Version Catalog і модульні тести JUnit 5.

## Розрахунок варіанта

Номер залікової книжки: **N = 4115**.

```text
V = N mod 3
4115 = 3 × 1371 + 2
V = 2
```

Завдання варіанта 2 — створити клас `WordCounter` для підрахунку входжень слів і отримання множини унікальних слів.

## Вимоги до середовища

- JDK 17 або новіший; код компілюється з `--release 17`.
- Доступ до інтернету під час першого запуску для завантаження Gradle та залежностей.
- Окремо встановлювати Gradle не потрібно: у репозиторії є Gradle Wrapper **9.7.0**.

У `gradle/libs.versions.toml` зафіксовано JUnit **5.13.4** і JaCoCo **0.8.14**. Залежності JUnit підключаються через каталог версій і JUnit BOM.

## Реалізація

Клас розташований у пакеті `ua.kpi.comsys.collections`:

```java
public Map<String, Integer> countWords(List<String> textLines)
public Set<String> getUniqueWords(List<String> textLines)
```

`countWords` повертає відображення «слово → кількість входжень», а `getUniqueWords` — множину всіх знайдених слів. Словом вважається непорожня послідовність символів, що відповідає регулярному виразу `[A-Za-z0-9]+`. Пробіли, розділові знаки, підкреслення, кирилиця та інші символи розділяють слова. Наприклад, `java17` — одне слово, а `one_two` — два.

Слова приводяться до нижнього регістру за допомогою `Locale.ROOT`, тому результат не залежить від мовних налаштувань системи. Порожні рядки й рядки без слів не додають записів. Передавання `null` замість списку спричиняє `IllegalArgumentException` з повідомленням `textLines cannot be null`. Окремі `null`-елементи списку пропускаються.

Для зберігання результатів використано `LinkedHashMap` і `LinkedHashSet` зі стандартної бібліотеки Java. Порядок результатів відповідає першій появі слів у тексті. Методи не змінюють вхідний список; кожен виклик повертає незалежну колекцію.

Очікувана часова складність — **O(L)**, де L — загальна кількість символів у вхідному тексті. Додаткова пам’ять містить **O(U)** записів для U унікальних слів і символи самих слів.

## Приклад

```java
WordCounter counter = new WordCounter();
List<String> lines = Arrays.asList(
        "Java java GRADLE",
        "gradle Java collections",
        "Java17, java17; 123",
        "",
        null
);

System.out.println(counter.countWords(lines));
System.out.println(counter.getUniqueWords(lines));
```

Очікуваний результат:

```text
{java=3, gradle=2, collections=1, java17=2, 123=1}
[java, gradle, collections, java17, 123]
```

Для використання прикладу потрібні імпорти `java.util.Arrays`, `java.util.List` і `ua.kpi.comsys.collections.WordCounter`.

## Збірка і запуск тестів

Виконуйте команди з кореневого каталогу проєкту.

Windows PowerShell:

```powershell
.\gradlew.bat clean build
.\gradlew.bat test
```

Linux або macOS:

```sh
chmod +x gradlew
./gradlew build
./gradlew test
```

Команда `chmod +x gradlew` потрібна, якщо після розпакування ZIP-архіву файл не має права на виконання. Для повної повторної збірки можна виконати `./gradlew clean build`.

Завдання `build` запускає компіляцію, тести та перевірки `check`. До `check` підключено `jacocoTestCoverageVerification`: збірка вимагає **100% покриття рядків і 100% покриття гілок** класу `WordCounter`. Звіт покриття також створюється після запуску тестів. Для примусового повторного виконання тестів використовуйте `test --rerun-tasks`.

Звіти після виконання:

- HTML-звіт JUnit: `build/reports/tests/test/index.html`.
- XML-результати JUnit: `build/test-results/test/`.
- HTML-звіт JaCoCo: `build/reports/jacoco/test/html/index.html`.
- XML-звіт JaCoCo: `build/reports/jacoco/test/jacocoTestReport.xml`.
- JAR-файл: `build/libs/lab2-word-counter-1.0.0.jar`.

## Перевірені тестами сценарії

У `WordCounterTest` підготовлено 11 тестів. Вони перевіряють відхилення `null`-списку обома методами, порожній список, повтори слів у різних рядках, різний регістр, розділові знаки, цифри, порожні рядки, символи поза `[A-Za-z0-9]`, пропуск `null`-елементів, незалежність від турецької локалі та незалежність повернених колекцій від вхідних даних і наступних викликів.

Перевірено 10 листопада 2026 року на Windows з Oracle JDK 25.0.1: `clean build` і окремий `test --rerun-tasks` завершилися успішно. Усі 11 тестів пройдено без помилок і пропусків. JaCoCo зафіксував 16 із 16 рядків, 8 із 8 гілок і 62 із 62 інструкцій байткоду класу `WordCounter` — 100% покриття за цими метриками.

## Структура проєкту

```text
lab2-word-counter/
├── build.gradle
├── settings.gradle
├── gradlew
├── gradlew.bat
├── gradle/
│   ├── libs.versions.toml
│   └── wrapper/
│       ├── gradle-wrapper.jar
│       └── gradle-wrapper.properties
└── src/
    ├── main/java/ua/kpi/comsys/collections/WordCounter.java
    └── test/java/ua/kpi/comsys/collections/WordCounterTest.java
```

## Публікація на GitHub

Створіть у своєму акаунті GitHub порожній **публічний** репозиторій `lab2-word-counter`, без автоматичного додавання README чи інших файлів. Замість `<username>` у команді нижче підставте свій логін. Для локального Git мають бути налаштовані ваші ім’я та електронна адреса автора комітів.

Якщо проєкт отримано як ZIP-архів, ініціалізуйте локальний репозиторій:

```sh
git init
```

Далі збережіть файли в коміті й надішліть їх у створений репозиторій:

```sh
git add .
git commit -m "Implement WordCounter with Gradle and JUnit tests"
git branch -M main
git remote add origin https://github.com/<username>/lab2-word-counter.git
git push -u origin main
```

Після публікації прикріпіть посилання на репозиторій у Classroom відповідно до умов лабораторної роботи.
