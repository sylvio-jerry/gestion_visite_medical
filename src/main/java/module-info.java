module com.hitsabo {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.persistence;
    requires org.hibernate.orm.core;

    requires java.sql;
    requires org.postgresql.jdbc;
    opens com.hitsabo.entity to org.hibernate.orm.core;
    opens com.hitsabo.controller to javafx.fxml;
    exports com.hitsabo;
    exports com.hitsabo.controller;
    exports com.hitsabo.entity;
}