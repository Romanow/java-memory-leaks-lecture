[![License: CC BY-NC-ND 4.0](https://img.shields.io/badge/License-CC%20BY--NC--ND%204.0-lightgrey.svg)](https://creativecommons.org/licenses/by-nc-nd/4.0/)
[![pre-commit](https://img.shields.io/badge/pre--commit-enabled-brightgreen?logo=pre-commit)](https://github.com/pre-commit/pre-commit)

# Делаем утечки памяти в Java быстро и без регистрации

## Аннотация

Все знают, что в Java есть сборщик мусора, и она сама очищает неиспользуемую память. Но этот механизм может работать не
всегда идеально: можно написать код таким образом, что Java не сможет очистить память и она будет копиться пока
приложение не упадет по OutOfMemory. На простых примерах рассмотрим несколько случаев, когда это может произойти.

## План

1. Как устроена память в Java.
2. Как в Java осуществляется поиск неиспользуемых объектов?
3. Рассмотрим несколько примеров, когда возникает утечка памяти:
    * неверная реализация `equals` и `hashCode`;
    * cтатические переменные;
    * ThreadLocal переменные при использовании пула потоков;
    * Пул строк (String interning).
4. Как найти утечку памяти?
5. Выводы: список простых правил как бороться с утечками памяти.

## Доклад

### Как устроена память в Java

![Java Memory](images/Java%20Memory.png)

Память делится на **Heap**, **Metaspace** и **Stack**.

* **Heap** – основной сегмент памяти, используется для выделения памяти под объекты и JRE классы. Создание нового
  объекта происходит в Heap, здесь работает GC.
* **Metaspace** – хранятся метаданные о классе и статические поля: там хранятся это либо примитивы, либо ссылки на
  объекты/массивы, которые сами по себе аллоцированы в **Heap**. **Metaspace** в Java 8 пришел на замену **PermGen**,
  основное отличие которой — возможность динамически расширятся, ограниченная по умолчанию только размером нативной
  памяти. Опционально можно задать размер через аргумент`-XX:MaxMetaspaceSize`. В боевых окружениях желательно всегда
  задавать размер **Metaspace**. В случае возникновения ошибки, лечится увеличением **Metaspace**, либо добавлением
  памяти.
* **Stack** – стековая память в Java работает по схеме LIFO: всякий раз, когда вызывается метод, в памяти стека
  создается новый блок, который содержит примитивы и _ссылки_ на другие объекты в методе. Каждый поток имеет свой стек,
  примитивы и ссылки на локальные переменные хранятся в стеке. Как только метод заканчивает работу, блок также перестает
  использоваться, тем самым предоставляя доступ для следующего метода. Объекты в куче доступны с любой точки программы,
  в то время как стековая память не может быть доступна для других потоков.

> On-heap memory is memory in the Java heap, which is a region of memory managed by the garbage collector. Java objects
> reside in the heap. The heap can grow or shrink while the application runs. When the heap becomes full, garbage
> collection is performed: The JVM identifies the objects that are no longer being used (unreachable objects) and
> recycles their memory, making space for new allocations.
> Off-heap memory is memory outside the Java heap. To invoke a function or method from a different language such as C
> from a Java application, its arguments must be in off-heap memory. Unlike heap memory, off-heap memory is not subject
> to garbage collection when no longer needed. You can control how and when off-heap memory is deallocated.

### Как в Java осуществляется поиск неиспользуемых объектов?

В Java процесс работы с памятью скрыт от программиста: JVM сама занимается выделением памяти и ее очисткой. Процесс
очистки памяти называется Garbage Collection. Из названия следует, что GC занимается очисткой памяти, т.е. удаляет
неиспользуемые объекты из памяти. Весь процесс состоит из двух частей:

* _mark_ – обход дерева объектов и поиск достижимых ссылок из корневых объектов (GC Roots).
* _sweep_ – удаление неиспользуемых объектов.

![Mark & Sweep](images/Mark%20&%20Sweep%20GC.png)

Поиск мусора:

1. Reference counting – у каждого объекта счетчик ссылок. Когда он равен нулю, объект считается мусором. В случае
   обнаружения цикличных ссылок, объекты считаются недостижимыми, если на них не ссылаются никакие другие объекты.
2. Tracing - объект считается не мусором, если до него можно добраться с корневых точек (GC Roots).

Корневые точки (GC Roots):

* Классы, загруженные системным ClassLoader'ом. Эти классы никогда не могут быть выгружены.
* Активные потоки.
* Локальные переменные, параметры методов.
* Объекты, используемые в мониторе для синхронизации.
* JNI (Java Native Interface).
* Объекты, огражденные от сборки мусора самим JVM.

### Примеры

* OutOfMemoryError: Java heap space. <-- рассмотрим этот класс ошибок.
* OutOfMemoryError: Metaspace.
* OutOfMemoryError: Requested array size exceeds VM limit.
* OutOfMemoryError: Unable to create new native thread.
* OutOfMemoryError: GC Overhead limit exceeded.

#### Неверная реализация `equals` и `hashCode`

Пример: [EqualsAndHashCodeExample](src/main/java/ru/romanow/memory/leaks/EqualsAndHashCodeExample.java).
Запуск: `./gradlew runEqualsAndHashCodeExample`.

#### Статические переменные

Пример: [StaticResourcesExample](src/main/java/ru/romanow/memory/leaks/StaticResourcesExample.java).
Запуск: `./gradlew runStaticResourcesExample`.

#### ThreadLocal переменные при использовании пула потоков

Пример: [ThreadLocalExample](src/main/java/ru/romanow/memory/leaks/ThreadLocalExample.java).
Запуск: `./gradlew runThreadLocalExample`.

> By definition, a reference to a ThreadLocal value is kept until the "owning" thread dies or if the ThreadLocal itself
> is no longer reachable.

#### Пул строк (String interning)

Пример: [InternalStringsExample](src/main/java/ru/romanow/memory/leaks/InternalStringsExample.java).
Запуск: `./gradlew runInternalStringsExample`.

> Prior to Java 7 interned strings were allocated in PermGen space. This would become a garbage collector issue once
> your string is of no more use in application, since the interned string pool is a static member of the String class
> and will never be garbage collected. From Java 7 onward the interned strings are allocated on the Heap and are subject
> to garbage collection.

### Как найти утечку памяти?

Какого-то универсального алгоритма поиска утечки памяти нет, но вот список основных действий, которые нужно выполнить:

1. Включить параметр JWM `-XX:+HeapDumpOnOutOfMemoryError` для получения heap dump при падении по OutOfMemoryError.
   После этого загрузить результат в JProfiler или подобный инструмент и посмотреть какие **ваши** объекты занимают
   много памяти (хотя не должны).
2. Провести статический анализ кода на предмет утечек памяти (современные анализаторы умеют искать причины OOM).
3. Возможно, просто нужно увеличить ресурсы приложения: возросла нагрузка или усложнились бизнес-операции.
4. Если вы используете JNI (Java Native Interface) или другие средства нативного взаимодействия с памятью, то
   постарайтесь уйти от этого. Возможно, лучше написать сервер на C++ и коммуницировать с ним через socket, чем напрямую
   работать с памятью из Java (она для этого не приспособлена).

### Выводы: список простых правил как бороться с утечками памяти

Keep it simple.
