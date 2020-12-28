# MessageServer
**Цель**
Научиться разрабатывать сетевые приложения.

**Требования**
- Cервер из предыдущего ДЗ про MessageSystem разделить на три приложения:
▪ MessageServer
▪ Frontend
▪ DBServer
- Сделать MessageServer сокет-сервером, Frontend и DBServer клиентами.
- Пересылать сообщения с Frontend на DBService через MessageServer.
- Запустить приложение с двумя Frontend и двумя DBService (но на одной базе данных) на разных портах.
- Frontend и DBService запускать "руками"
- По желанию, можно сделать запуск Frontend и DBServer из MessageServer.
- Такой запуск должен быть "отчуждаемый", т.е. "сборка" должна запускаться на другом компьютере без особых хлопот.
