# AyuGram4A (Full Version for Android)

Исходный код последней релизной версии приложения **AyuGram for Android** (Universal APK), созданного на базе клиента **exteraGram**.

---

## 📌 Характеристики

| Параметр | Значение |
|---|---|
| **Версия** | Последняя релизная версия (Full Universal Build) |
| **Базовая платформа** | **exteraGram** (клиент построен на базе exteraGram) |
| **Базовая версия Telegram** | `12.5.1` (versionCode `65819`) |
| **AGP / SDK** | AGP `8.12.0` \| Target SDK `36` \| Min SDK `23` |
| **Архитектуры (.so)** | `arm64-v8a`, `armeabi-v7a`, `x86_64` (60 библиотек в `jniLibs`) |
| **Лицензия** | GNU General Public License v2.0 (GPL-2.0) |

---

## 🛡️ Безопасность: Скримеры удалены

В оригинальной закрытой сборке AyuGram присутствовал скрытый функционал скримеров (включение громкой связи на 100% громкости) и шуточный интент на удаление приложения для устройств Xiaomi. Сам скример и его триггеры были под обфускацией.

**В данном репозитории этот код полностью вырезан:**
* В `AudioOutputManager.java` отключен принудительный перехват аудиопотока и выкручивание громкости.
* В `AyuInAppHandlers.java` удален обработчик скримеров и системная команда удаления приложения (`handleXiaomi`).

---

## 📂 Структура проекта

```
AyuGram-src/
├── app/
│   ├── build.gradle
│   └── src/main/
│       ├── AndroidManifest.xml
│       ├── java/
│       │   ├── com/radolyn/ayugram/
│       │   ├── com/exteragram/
│       │   └── org/telegram/
│       ├── res/
│       └── jniLibs/
├── AyuGram-core/
├── build.gradle
└── README.md
```
