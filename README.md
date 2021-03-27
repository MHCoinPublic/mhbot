<p align="center">
<img width="20%" src="https://cdn.discordapp.com/attachments/824242242151383090/825362477473857546/322c936a8c8be1b803cd94861bdfa868.png">
<br>

<h1 align="center"> mhbot </h1>
<h1 align="center"> <a href="https://discord.com/oauth2/authorize?client_id=822815690729586688&permissions=8&scope=bot">Ссылка на бота</a> </h1>

## Компиляция mhbot

### `0.` Зависимости

* PowerShell (Windows) или Terminal (Linux).
> ⚠️ Коммандная строка Windows может не работать, лучше используйте PowerShell!
* Вам нужно иметь [Java Development Kit](https://adoptopenjdk.net/) установленной на вашей машине. Минимальная версия для компиляции и запуска mhbot это JDK 15.
* Вам нужно иметь Git установленной на вашей машине.
* Проверьте чтобы `JAVA_HOME` был установлен правильно, новые JDK версии скачанные с AdoptOpenJDK могут уже иметь правильно установленную переменную. Вы можете проверить это командой `echo $env:JAVA_HOME` в PowerShell.

### `1.` 🧹 Подготовка среды

* Сколинуйте репозиторий с помощью git:
```bash
git clone https://github.com/Ivanhai/mhbot.git
```

### `2.` 💻 Компиляция
* Зайдите в папку с исходниками и откройте PowerShell или терминал внутри.
* Билд mhbot с Gradle:
```bash
./gradlew shadowJar
```

* Если билд успешный, то поздравляю 🎉! Вы успешно скомпилировали mhbot!
* Финальный билд будет в папке `build/libs/*.jar`.

## 🚀 Самохостинг mhbot (Discord)

### `0.` Зависимости

* Те же что при компилирование.

### `1.` 🧹 Подготовка среды
* Создайте пустую папку где-нибудь в вашей ОС, почему? Просто чтобы все было в порядке! :3
* Сгенерируйте публичные и приватные ключи данными коммандами
```
$ openssl genrsa -out keypair.pem 2048
Generating RSA private key, 2048 bit long modulus
............+++
................................+++
e is 65537 (0x10001)
$ openssl rsa -in keypair.pem -outform DER -pubout -out public.der
writing RSA key
$ openssl pkcs8 -topk8 -nocrypt -in keypair.pem -outform DER -out private.der
```
* Следуйте указанием в [FIREBASE.md](FIREBASE.md)

### `2.` 📥 Получение необходимых JAR-ников

#### Если вы скомпилировали сами...

**mhbot Discord JAR**: `mhcoin/build/libs/` 

#### Если вы слишком ленивый и не хотите компилить...
**Вы можете найти уже готовые здесь:** https://github.com/ivanhai/mhbot/releases
### `3.` 🧹 Подготовка среды²
* Скопируйте все созданные и скаченные ключи в созданную папку
* Скопируйте `mhcoin-*-all.jar` в вашу созданную папку.
* Создай `config.json` в созданной папки, с этими данными.
```ascii
{
  "prefix": "prefix",
  "token": "token",
  "firebase": {
    "id": "dbid",
    "key": "serviceAccountKey.json"
  },
  "keys": {
    "public": "public.der",
    "private": "private.der"
  }
}
```
* Если вы все сделали правильно, то у вас есть...
* * Файл с именем `mhcoin-*-all.jar`.
* * Файл с именем `config.json`.
* * Наши ключи.

### `4.` 🚶 Запуск бота

* Обнови config.json своими данными.
* Запусти mhbot коммандой `java -jar mhcoin-*-all.jar` (замени имя JAR-ника вашем именем)

#### ⚠️ Значения которые вам нужно изменить перед запуском mhbot.
```ascii
📄 config.json
└── prefix // префикс бота
└── token // токен вашего бота
└── firebase
    ├── id // айди дб firebase
    │   
    └── key // имя ключа
└── keys
    ├── public // имя публичного ключа
    │   
    └── private // имя приватного ключа
```
