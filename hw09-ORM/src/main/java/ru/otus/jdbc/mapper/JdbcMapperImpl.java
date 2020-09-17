package ru.otus.jdbc.mapper;

import ru.otus.core.exception.DAOException;
import ru.otus.jdbc.DbExecutorImpl;
import ru.otus.jdbc.mapper.interfaces.EntityClassMetaData;
import ru.otus.jdbc.mapper.interfaces.EntitySQLMetaData;
import ru.otus.jdbc.mapper.interfaces.JdbcMapper;
import ru.otus.jdbc.sessionmanager.SessionManagerJdbc;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class JdbcMapperImpl<T> implements JdbcMapper<T> {
    private final DbExecutorImpl<T> dbExecutor;
    private final SessionManagerJdbc sessionManager;

    public JdbcMapperImpl(SessionManagerJdbc sessionManager) {
        dbExecutor = new DbExecutorImpl<>();
        this.sessionManager = sessionManager;
    }

    @Override
    public void insert(T objectData) {
        EntityClassMetaDataImpl<T> entityClassMetaData = new EntityClassMetaDataImpl(objectData.getClass());
        EntitySQLMetaData entitySQLMetaData = new EntitySQLMetaDataImpl<>(entityClassMetaData);
        String insert = entitySQLMetaData.getInsertSql();
        sessionManager.beginSession();
        try {
            dbExecutor.executeInsert(getConnection(), insert, getParams(entityClassMetaData, objectData));
            sessionManager.commitSession();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(T objectData) {
        EntityClassMetaDataImpl<T> entityClassMetaData = new EntityClassMetaDataImpl(objectData.getClass());
        EntitySQLMetaData entitySQLMetaData = new EntitySQLMetaDataImpl<>(entityClassMetaData);
        String update = "";
        Object id = null;
        try {
            update = entitySQLMetaData.getUpdateSql();
            Field idField = entityClassMetaData.getIdField();
            idField.setAccessible(true);
            id = idField.get(objectData);
        } catch (IllegalAccessException | DAOException e) {
            e.printStackTrace();
        }
        sessionManager.beginSession();
        List<Object> params = getParams(entityClassMetaData, objectData);
        params.add(id);
        try {
            dbExecutor.executeUpdate(getConnection(), update, params);
            sessionManager.commitSession();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

    }

    @Override
    public void insertOrUpdate(T objectData) {
        EntityClassMetaDataImpl<T> entityClassMetaData = new EntityClassMetaDataImpl(objectData.getClass());
        T object = null;
        try {
            Field idField = entityClassMetaData.getIdField();
            idField.setAccessible(true);
            object = findById((long) idField.get(objectData), (Class<T>) objectData.getClass());
        } catch (IllegalAccessException | DAOException e) {
            e.printStackTrace();
        }

        if (object == null) {
            insert(objectData);
            return;
        }

        update(objectData);
    }

    @Override
    public T findById(long id, Class<T> clazz) {
        EntityClassMetaDataImpl entityClassMetaData = new EntityClassMetaDataImpl(clazz);
        EntitySQLMetaData entitySQLMetaData = new EntitySQLMetaDataImpl<>(entityClassMetaData);
        String select = null;
        try {
            select = entitySQLMetaData.getSelectByIdSql();
        } catch (DAOException e) {
            e.printStackTrace();
        }
        sessionManager.beginSession();
        Optional<T> findObject = dbExecutor.executeSelect(getConnection(), select, id, resultSet -> {
            try {
                if (resultSet.next()) {
                    return (T) createNewObject(resultSet, entityClassMetaData);
                }
            } catch (SQLException | DAOException | NoSuchMethodException throwables) {
                throwables.printStackTrace();
            }
            return null;
        });
        return findObject.orElse(null);
    }

    private List<Object> getParams(EntityClassMetaDataImpl<T> entityClassMetaData, T object) {
        List<Object> listParams = new ArrayList<>();
        for (Field field : entityClassMetaData.getFieldsWithoutId()) {
            field.setAccessible(true);
            try {
                listParams.add(field.get(object));
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return listParams;
    }

    private T createNewObject(ResultSet resultSet, EntityClassMetaData<T> entityClassMetaData) throws DAOException, NoSuchMethodException {
        Constructor<T> constructor = entityClassMetaData.getConstructor();
        try {
            T object = constructor.newInstance();
            for (Field field : entityClassMetaData.getAllFields()) {
                field.setAccessible(true);
                field.set(object, resultSet.getObject(field.getName()));
            }
            return object;
        } catch (InstantiationException | InvocationTargetException | IllegalAccessException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private Connection getConnection() {
        return sessionManager.getCurrentSession().getConnection();
    }
}
