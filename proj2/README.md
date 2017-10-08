# Project 2: Database
This is a project from CS61B, Data Structures, Spring 2017. It is aimed at building a small version of relational database management system, as well as a DSL (Domain Specific Language) with which a user can interact with the database. The full specs of this project are listed in the course website [Project 2: Database, version 1.0](http://datastructur.es/sp17/materials/proj/proj2/proj2.html). The commands supported by this database are quoted as follows.

## Commands

> ### Create Table  
> There are two variants of the create table command, explained below.  
> ```
> create table <table name> (<column0 name> <type0>, <column1 name> <type1>, ...)
> ```
> Create a table with the given name. The names and types of the columns of the new table are supplied in a parenthesized list, in order. This defines the column order for this table.  
> ```
> create table <table name> as <select clause>
> ```
> Create a table with the given name. The columns, content and types of columns of the table are those of the intermediate table created by the result of executing the select clause.
> 
> It is an error to create a table with no columns and it is also an error to create a table that already exists.
>
> Create Table should return the empty String on success, or an appropriate error message otherwise.  
>   
> ### Load
> ```
> load <table name>
> ```
> Load the table stored in the file `<table name>`.tbl into memory, giving it the name `<table name>`. The row order of the table is defined as the order in which the rows are listed in the TBL file. If a table with the same name already exists, it should be replaced. If the relevant table file is an invalid table, it is an error.
> 
> Load should return the empty String on success, or an appropriate error message otherwise.
> ### Store
> ```
> store <table name>
> ```
> Write the contents of a database table to the file `<table name>.tbl`. If the TBL file already exists, it should be overwritten.
> 
> Store should return the empty String on success, or an appropriate error message otherwise.
> ### Drop Table
> ```
> drop table <table name>
> ```
> Delete the table from the database.
> 
> Drop Table should return the empty String on success, or an appropriate error message otherwise.
> ### Insert Into
> ```
> insert into <table name> values <literal0>,<literal1>,...
> ```
> Insert the given row (the list of literals) to the named table. The table must already be in the DB and the provided values must match the columns of that table. If a provided value cannot be parsed into the type of the column it is listed in, it is an error. The given row is appended to the table, becoming the last row in its row order.
> 
> It is an error to insert a row that does not match the given table.
> 
> Insert Into should return the empty String on success, or an appropriate error message otherwise.
> ### Print  
> ```
> print <table name>
> ```
> Print should return the String representation of the table, or an appropriate error message otherwise.
> ### Select
> 
> Select statements are used to extract data from the database in a programmatic fashion. Instead of simply writing to and printing individual tables, select statements allow you to form more complicated requests. They take the form below:
> ```
> select <column expr0>,<column expr1>,... from <table0>,<table1>,... where <cond0> and <cond1> and ...
> ```
> The result of a select statement is a new table that has been formed from the join of the given table(s), filtered by the conditional statement(s), and selected from with the column expression(s). The order in which these operations happen is up to your implementation, as long as the output is correct. The joining of tables is optional, i.e. selecting from a single table is valid. The conditional statements are also optional, so a select could be as simple as:
> ```
> select <column expr> from <table0>
> ```
> The order of the columns in the new table is defined by the order they are listed in the select. In the case that all columns are selected (with the `*` operator), the order is defined by the column order for the join.
> 
> It is an error to write a select statement that involved no columns of the listed tables.
> 
> Select should return the String representation of the produced table, or an appropriate error message otherwise. 

