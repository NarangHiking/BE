CREATE DATABASE IF NOT EXISTS NarangHiking;
create user 'hiking'@'%' identified by 'hiking';
grant all privileges on NarangHiking.* To 'hiking'@'%';
flush privileges;