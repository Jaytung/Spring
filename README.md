1. 建立DB
```SQL
create database demo
    with owner postgres;
```
2. 修改連線設定
src/main/resources/application.properties
```
spring.datasource.url=jdbc:postgresql://localhost:5432/demo  
spring.datasource.username=postgres
//改成目前DB的帳號密碼
spring.datasource.password=密碼
```
3. 啟動專案
4. 新增使用者
```SQL
INSERT INTO system_user (_id, account, password, name)
VALUES ('123e4567-e89b-12d3-a456-426614174000', 'admin', 'aa123', 'admin');

INSERT INTO system_user (_id, account, password, name)
VALUES ('123e4567-e89b-12d3-a456-426614174999', 'user1', 'aa123', 'user1');
```
5. 輸入url開啟swagger `http://localhost:8080/swagger-ui/index.html#/`
6. 使用auth/login取得token
7. 在Authorize輸入token開始測試