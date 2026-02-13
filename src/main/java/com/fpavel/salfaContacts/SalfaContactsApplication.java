package com.fpavel.salfaContacts;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SalfaContactsApplication {
    public static void main(String[] args) {
        SpringApplication.run(SalfaContactsApplication.class, args);
    }
}

// TODO добавить валидацию полей
//  сделать ограничение по ролям
//  сделать докер
//  сделать тесты
//  сделать красивую документацию README и Swagger
//  убрать печать стектрейса из обработчика ошибок
//  заменить ошибку 403
//  при редактировании клиента не должны изменяться контакты
//  отдельный метод для смены пароля

