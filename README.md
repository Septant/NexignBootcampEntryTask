# NexignBootcampEntryTask
Входное задание для Nexign bootcamp

Все пояснения и документация к проекту даны на английском языке.

## [Задание](https://github.com/NexignBootcamp/Test-for-all-data-record/blob/main/task.txt)

## [Пример отчета](https://github.com/NexignBootcamp/Test-for-all-data-record/blob/main/report_example.txt)

## [Тестовые данные](/data.test.txt)

## [Отчеты](/reports)

## [Классы](src/main/java/org/application) :
 -[Main](src/main/java/org/application/Main.java):
 
 Содержит инициализации вспомогательных списков, получение данных и и инициализацию списка CDR. Генерирует отчёты.
 
 -[UtilFuncs](src/main/java/org/application/UtilFuncs.java) :
 
 Содержит различные вспомогательные функции использумые в классах CDR|CDRs|Main.
 
 -[Tariff](src/main/java/org/application/Tariff.java) :
 
 Включает в себя 3 тарифа из задания и их параметры.
 
 -[CDR](src/main/java/org/application/CDR.java) : 
 
 Класс, описывающий основную сущность программы - запись информации о звонке (Call Data Record). Конструируется по строке заданного формата(см.[Тестовые данные](/data.test.txt)). Включает в себя геттеры для полей, кастомные функции преобразования к long и рассчёта длительности звонка.
 
 -[CDRs](src/main/java/org/application/CDRs.java): Класс предоставляющий возможность работать со списком CDR. Имеется 2 конструктора для пустого списка и списка по получаемому массиву строк, геттеры и сеттеры для полей. 

Функции:

генерация отфильтрованного сортированного по дате начала звонка списка, сама сортировка, рассчёт стоимости звонка, вспомогательную функцию для неё, а также функцию рассчёта итоговой цены за рассчётный период.
