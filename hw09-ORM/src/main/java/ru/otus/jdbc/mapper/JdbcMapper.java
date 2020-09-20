package ru.otus.jdbc.mapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.exceptions.ExcCantExecuteSqlStatement;
import ru.otus.exceptions.ExcCantGetValueOfField;
import ru.otus.exceptions.ExcCantInstantiateNewObject;
import ru.otus.exceptions.ExcDAO;
import ru.otus.jdbc.DbExecutor;
import ru.otus.jdbc.IDbExecutor;
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

public class JdbcMapper<T> implements IJdbcMapper<T> {

    private Class<T> mappingClass;
    private SessionManager sessionManager;
    private EntitySQLMetaData<T> sqlGenerator;
    private EntityClassMetaData<T> metaData;
    private IDbExecutor<T> sqlExecutor;

    private static Logger logger = LoggerFactory.getLogger(JdbcMapper.class);

    public JdbcMapper(Class<T> clazz, SessionManager manager) {
        mappingClass = clazz;
        sessionManager = manager;
        sqlGenerator = new EntitySQLMetaData<T>(clazz);
        metaData = sqlGenerator.getClassMetaData();
        sqlExecutor = new DbExecutor<T>();
    }

    @Override
    public void insert(T objectData) {
        String insertSql = sqlGenerator.getInsertSql();
        try {
            List<Object> params = new ArrayList<>();
            params = getFieldValuesWithoutIdOf(objectData);
            Connection connection = sessionManager.getCurrentSession().getConnection();
            sqlExecutor.executeInsert(
                    connection,
                    insertSql,
                    params);
            sessionManager.commitSession();
        } catch (SQLException throwables) {
            throw new ExcCantExecuteSqlStatement(throwables);
        }
    }

    @Override
    public void update(T objectData) {
        String updateSql = sqlGenerator.getUpdateSql();
        List<Object> params = new ArrayList<>(getFieldValuesWithoutIdOf(objectData));
        Field idField = metaData.getIdField();
        String idColumnName = idField.getName();
        try {
            Object idColumnValue = idField.get(objectData);
            params.add(idColumnValue);

            sqlExecutor.executeInsert(
                    sessionManager.getCurrentSession().getConnection(),
                    updateSql,
                    params
            );
            sessionManager.commitSession();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            var exc = new ExcCantGetValueOfField();
            exc.addSuppressed(e);
            throw exc;
        } catch (SQLException e) {
            throw new ExcCantExecuteSqlStatement(e);
        }
    }

    @Override
    public void insertOrUpdate(T objectData) {
        Field idField = metaData.getIdField();
        try {
            long idValue = (long) idField.get(objectData);

            boolean isIdPresenceInDatabase =
                    findById(idValue, (Class<T>) objectData.getClass()) != null;

            if (isIdPresenceInDatabase) {
                update(objectData);
            } else {
                insert(objectData);
            }

        } catch (IllegalAccessException e) {
            e.printStackTrace();
            var exc = new ExcCantGetValueOfField();
            exc.addSuppressed(e);
            throw exc;
        }
    }

    @Override
    public T findById(long id, Class<T> clazz) {
        EntityClassMetaData entityClassMetaData = new EntityClassMetaData(clazz);
        EntitySQLMetaData entitySQLMetaData = new EntitySQLMetaData(clazz);
        String select = null;
        try {
            select = entitySQLMetaData.getSelectByIdSql();
        } catch (ExcDAO e) {
            e.printStackTrace();
        }
        try {
            Optional<T> findObject = sqlExecutor.executeSelect(
                    sessionManager.getCurrentSession().getConnection(),
                    select,
                    id,
                    resultSet -> {
                        try {
                            if (resultSet.next()) return (T) createNewObject(resultSet, entityClassMetaData);
                        } catch (SQLException | ExcDAO | NoSuchMethodException throwables) {
                            throwables.printStackTrace();
                        }
                        return null;
                    });
            return findObject.orElse(null);
        } catch (Exception exc) {
            exc.printStackTrace();
        }
        return null;
    }

    private List<Object> getFieldValuesWithoutIdOf(T objectData) {
        List<Field> fields = metaData.getFieldsWithoutId();
        List<Object> params = new ArrayList<>(fields.size());
        for (Field field : fields) {
            try {
                field.setAccessible(true);
                params.add(field.get(objectData));
            } catch (IllegalAccessException e) {
                var exc = new ExcCantGetValueOfField();
                exc.addSuppressed(e);
                throw exc;
            }
        }
        return params;
    }

    private T createNewObject(ResultSet resultSet, EntityClassMetaData<T> entityClassMetaData) throws ExcDAO, NoSuchMethodException {
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

    private T createObjectFromResultSet(ResultSet resultSet) {
        Constructor<T> constructor = metaData.getConstructor();
        List<Field> fields = metaData.getAllFields();
        try {
            T obj = constructor.newInstance();
            for (Field field : fields) {
                field.set(obj, resultSet.getObject(field.getName()));
            }

            return obj;
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new ExcCantInstantiateNewObject(e);
        } catch (SQLException throwables) {
            throw new ExcCantInstantiateNewObject(throwables);
        }
    }
}