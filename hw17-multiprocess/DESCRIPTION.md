## Описание

Проект состоит из 5 модулей:

1. MessageServer - сервер обработки сообщений клиентов. Реализован на базе MessageSystem.
2. MessageClient - вспомогательный модуль, реализующий логику обмена с MessageServer. Включается в клиентов MessageServer.
3. MessageSystem - реализует механизм системы сообщений
4. DBServer - сервис хранения и обработки данных. Включает MessageClient. Является клиентом MessageServer.
5. Frontend - является клиентом MessageServer.

**Описание межпроцессного обмена**

Межпроцессный обмен реализован на базе сокетов и NIO. Для обмена используются JSON сообщения.
Базовый формат сообщения
```json
{"from":"db","to":"front", "data":"payload"}
```
На каждое сообщение MessageServer отвечает результатом обработки

```json
{"response":"REGISTRED"}
{"response":"RECIVED"}
{"response":"ERROR"}
```

При первом подключении к MessageServer клиент должен зарегистрироваться. Для этого отправляется базовое сообщение с 
любым содержанием "data". MessageServer обрабатывает поле "from" и регистрирует нового клиента в MessageSystem. 
При успешной регистрации MessageServer отправляет ответ "REGISTRED". После регистрации на все остальные сообщения 
MessageServer отправляет "RECIVED". В случае невалидного JSON или ошибки обработки сервер отправляет "ERROR".

Если к MessageServer будут подключены несколько клиентов с одинаковыми именами, то сообщения будут отправлены им всем. 
Например, если запустить два DBServer и два Frontend, то при оправке сообщения от Frontend оно попадет двум DBServer и 
наоборот.

**Сборка**

gradle hw17-multiprocess:DBServer:ShadowJar

gradle hw17-multiprocess:Frontend:build

gradle hw17-multiprocess:MessageServer:ShadowJar
