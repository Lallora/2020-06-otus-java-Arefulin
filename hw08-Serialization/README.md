# Домашнее задание
### Cвой json object writer

Цель: Научиться сериализовывать объект в json, попрактиковаться в разборе структуры объекта.
Напишите свой json object writer (object to JSON string) аналогичный gson на основе javax.json.

Gson это делает так:
Gson gson = new Gson();
AnyObject obj = new AnyObject(22, "test", 10);
String json = gson.toJson(obj);

Сделайте так:
MyGson myGson = new MyGson();
AnyObject obj = new AnyObject(22, "test", 10);
String myJson = myGson.toJson(obj);

Должно получиться:
AnyObject obj2 = gson.fromJson(myJson, AnyObject.class);
System.out.println(obj.equals(obj2));

Поддержите:
- примитивные типы
- массивы примитивных типов
- коллекции (interface Collection)
не забываться, что obj может быть null :)
