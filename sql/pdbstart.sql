create user novel IDENTIFIED by 123456;

grant  resource to novel;
grant  connect to novel;
grant create tablespace to  novel;
GRANT CREATE SESSION TO novel;
CREATE TABLESPACE novel_space
DATAFILE '/opt/oracle/oradata/ORCLCDB/novel_space.dbf'
SIZE 500M
AUTOEXTEND ON
NEXT 10M MAXSIZE UNLIMITED;
alter user novel quota  unlimited  on novel_space;

