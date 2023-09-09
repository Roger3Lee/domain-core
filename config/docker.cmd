docker run -d -p 3306:3306 --name mysql -e MYSQL_ROOT_PASSWORD=123456 -v D:\github\domain-generator\config\mysql/:/var/lib/mysql -v D:\github\domain-generator\config\mysql-files:/var/lib/mysql-files/ -d de764ad211de
docker exec -it mysql ./bin/bash

mysql -uroot -p123456
use mysql
ALTER USER 'root'@'%' IDENTIFIED WITH mysql_native_password BY 'root';
flush privileges;

mysql> ALTER USER 'root'@'%' IDENTIFIED WITH mysql_native_password BY 'password';
mysql> flush privileges;