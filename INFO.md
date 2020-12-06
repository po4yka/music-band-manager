# Information for project

## SQLite

- [Внутренние таблицы](https://habr.com/ru/post/223451/)

Вывести все таблицы в БД:

```
sqlite> .tables
```

Информация о конкретной таблице:

```
.schema <table_name>
```

The 6th column of the result of the TABLE_INFO pragma is true for primary key columns and false for other columns.

```
sqlite> PRAGMA table_info(performer);
0|full_name|text|1||0
1|birthday|numeric|1||0
2|group_name|text|1||1
3|amplua|text|0||0
sqlite> 

```