===================================================================
 1. JSONPath (Точечная выборка и фильтрация)
===================================================================
• Все занятия в понедельник:
  $.schedule.Monday

• Все занятия во вторник:
  $.schedule.Tuesday

• Список всех предметов:
  $.schedule..subject

• Список всех типов занятий (лекции, лабораторные...):
  $.schedule..type

• Номера всех аудиторий:
  $.schedule..room

• Время проведения онлайн-пар:
  $.schedule..[?(@.room == 'онлайн')].time

• Все лабораторные работы:
  $.schedule..[?(@.type == 'лабораторная')]

• Все практические занятия:
  $.schedule..[?(@.type == 'практика')]

• Все лекционные занятия:
  $.schedule..[?(@.type == 'лекция')]

• Все занятия в аудитории 401С:
  $.schedule..[?(@.room == '401С')]

• Все занятия по направлению NoSQL:
  $.schedule..[?(@.subject =~ /NoSQL/i)]


===================================================================
 2. MongoDB (Простые поиск и фильтрация)
===================================================================
// Выгрузить массив пар на Вторник
db.schedule.find({}, { "schedule.Tuesday": 1, _id: 0 })

// Выгрузить массив пар на Четверг
db.schedule.find({}, { "schedule.Thursday": 1, _id: 0 })

// Найти документ, в котором есть расписание на Пятницу
db.schedule.find({ "schedule.Friday": { $exists: true } })

// Найти расписание с онлайн-занятиями в Четверг
db.schedule.find({ "schedule.Thursday.room": "онлайн" })

// Найти документы, где есть занятия в аудитории 404С
db.schedule.find({
  "$or": [
    { "schedule.Monday.room": "404С" },
    { "schedule.Tuesday.room": "404С" }
  ]
})


===================================================================
 3. PostgreSQL JSONB (Работа с ключами и индексами)
===================================================================
-- Получить весь массив пар на Понедельник
SELECT data->'schedule'->'Monday' FROM schedule_table;

-- Вторая пара Вторника (индекс массива = 1)
SELECT data->'schedule'->'Tuesday'->1 FROM schedule_table;

-- Время первой пары Четверга (индекс = 0, вывод строкой)
SELECT data->'schedule'->'Thursday'->0->>'time' FROM schedule_table;

-- Аудитория первой пары Пятницы
SELECT data->'schedule'->'Friday'->0->>'room' FROM schedule_table;

-- Проверить наличие пар в Среду (вернет true/false)
SELECT data->'schedule' ? 'Wednesday' FROM schedule_table;


===================================================================
 4. JavaScript (Манипуляции с объектом data)
===================================================================
// Список всех учебных дней в объекте
const days = Object.keys(data.schedule);

// Количество пар в Понедельник
const mondayCount = data.schedule.Monday.length;

// Данные о первом занятии в Пятницу
const firstFridayClass = data.schedule.Friday[0];

// Список всех предметов без повторений
const subjects = [...new Set(Object.values(data.schedule).flat().map(s => s.subject))];

// Массив всех онлайн-занятий
const onlineClasses = Object.values(data.schedule).flat().filter(s => s.room === 'онлайн');

// Проверить, есть ли занятия раньше 09:00
const hasEarlyClasses = Object.values(data.schedule).flat().some(s => s.time < '09:00');