package ru.otus.jdbc.mapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.exceptions.ExcMapperException;
import ru.otus.jdbc.DbExecutor;
import ru.otus.jdbc.sessionmanager.SessionManager;

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

    private static final Logger logger = LoggerFactory.getLogger(JdbcMapperImpl.class);

    private final SessionManager sessionManager;
    private final DbExecutor<T> dbExecutor;
    private final EntityClassMetaData<T> classMetaData;
    private final EntitySQLMetaData sqlMetaData;


    public JdbcMapperImpl(SessionManager sessionManager,
                          DbExecutor<T> dbExecutor,
                          EntityClassMetaData<T> classMetaData,
                          EntitySQLMetaData sqlMetaData) {
        this.dbExecutor = dbExecutor;
        this.sessionManager = sessionManager;
        this.classMetaData = classMetaData;
        this.sqlMetaData = sqlMetaData;
    }

    @Override
    public Optional<T> findById(long id) throws RuntimeException {
        String strSelectSQL = null;
        try {
            strSelectSQL = sqlMetaData.getSelectByIdSql();
        } catch (RuntimeException e) {
            e.printStackTrace();
        }
        sessionManager.beginSession();
        Optional<T> findObject = null;
        try {
            findObject = dbExecutor.executeSelect(getConnection(),
                    strSelectSQL,
                    id,
                    resultSet -> {
                        try {
                            if (resultSet.next()) {
                                return (T) createNewObject(resultSet, classMetaData);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        return null;
                    });
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return findObject;
    }

    @Override
    public long insert(T objectData) throws RuntimeException {
        try {
            String strInsertSQL = sqlMetaData.getInsertSql();
            long id = dbExecutor.executeInsert(getConnection(),
                    strInsertSQL, getParams(classMetaData, objectData));
            sessionManager.commitSession();
            return id;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new ExcMapperException("Can not execute insert for " + objectData, e);
        }
    }

    @Override
    public void update(T objectData) throws RuntimeException {
        String strUpdateSQL = sqlMetaData.getUpdateSql();
        try {
            List<Object> params = getParams(classMetaData, objectData);
            params.add(classMetaData.getIdField().get(objectData));
            dbExecutor.executeUpdate(getConnection(), strUpdateSQL, params);
            sessionManager.commitSession();
        } catch (SQLException e) {
            throw new ExcMapperException("Can not update " + objectData, e);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void insertOrUpdate(T objectData) {

    }

    private List<Object> getParams(EntityClassMetaData<T> entityClassMetaData, T object) {
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

    private T createNewObject(ResultSet resultSet,
                              EntityClassMetaData<T> entityClassMetaData) throws Exception {
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
