package ru.otus.jdbc.mapper;

import ru.otus.exceptions.ExcMapperException;
import ru.otus.jdbc.DbExecutor;
import ru.otus.jdbc.sessionmanager.SessionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class JdbcMapperImpl<T> implements JdbcMapper<T> {
    private static final Logger logger = LoggerFactory.getLogger(JdbcMapperImpl.class);

    private final SessionManager sessionManager;
    private final DbExecutor<T> dbExecutor;
    private final EntityClassMetaData<T> classMetaData;
    private final EntitySQLMetaData sqlMetaData;

    public JdbcMapperImpl(SessionManager sessionManager, DbExecutor<T> dbExecutor,
                          EntityClassMetaData<T> classMetaData, EntitySQLMetaData sqlMetaData) {
        this.sessionManager = sessionManager;
        this.dbExecutor = dbExecutor;
        this.classMetaData = classMetaData;
        this.sqlMetaData = sqlMetaData;
    }

    @Override
    public Optional<T> findById(long id) {
        try {
            return dbExecutor.executeSelect(sessionManager.getCurrentSession().getConnection(),
                    sqlMetaData.getSelectByIdSql(), id, resultSet -> {
                        try {
                            return mapObject(resultSet);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        return null;
                    });
        } catch (SQLException e) {
            throw new ExcMapperException("Can not execute select for id " + id, e);
        }
    }

    @Override
    public void insert(T objectData) {
        try {
            var id = dbExecutor.executeInsert(sessionManager.getCurrentSession().getConnection(),
                    sqlMetaData.getInsertSql(), extractFields(objectData, classMetaData.getFieldsWithoutId()));
            injectId(objectData, id);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (SQLException ex) {
            throw new ExcMapperException("Can not execute insert for " + objectData, ex);
        }
    }

    @Override
    public void update(T objectData) {
        int id = 0;
        try {
            id = extractId(objectData);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        if (id == 0) {
            throw new ExcMapperException("Can not update object " + objectData + " which is not stored in database");
        }
        try {
            var params = extractFields(objectData, classMetaData.getFieldsWithoutId());
            params.add(id);
            dbExecutor.executeInsert(sessionManager.getCurrentSession().getConnection(), sqlMetaData.getUpdateSql(),
                    params);
        } catch (SQLException e) {
            throw new ExcMapperException("Can not update " + objectData, e);
        }
    }

    @Override
    public void insertOrUpdate(T objectData) {
        try {
            if (extractId(objectData) == 0) {
                insert(objectData);
            } else {
                update(objectData);
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public static <T> JdbcMapperImpl<T> forType(Class<T> type, SessionManager sessionManager, DbExecutor<T> executor) {
        EntityClassMetaDataImpl<T> classMetaData;
        classMetaData = EntityClassMetaDataImpl.create(type);
        var sqlMetaData = EntitySQLMetaDataImpl.createFromClass(classMetaData);
        var jdbcMapperImpl = new JdbcMapperImpl<>(sessionManager, executor, classMetaData, sqlMetaData);

        for (String s : Arrays.asList(
                "Name: " + classMetaData.getName(),
                "Select all: " + sqlMetaData.getSelectAllSql(),
                "Select by id: " + sqlMetaData.getSelectByIdSql(),
                "Insert: " + sqlMetaData.getInsertSql(),
                "Update: " + sqlMetaData.getUpdateSql())) {
            logger.debug(s);
        }
        return jdbcMapperImpl;
    }

    private T mapObject(ResultSet resultSet) throws Exception {
        try {
            if (!resultSet.next()) {
                return null;
            }
        } catch (SQLException e) {
            throw new ExcMapperException("Unexpected exception during mapping", e);
        }

        try {
            var object = classMetaData.getConstructor().newInstance();

            classMetaData.getAllFields().forEach(f -> {
                try {
                    var value = resultSet.getObject(f.getName());
                    f.set(object, value);
                } catch (SQLException e) {
                    throw new ExcMapperException("Can not get column " + f.getName() + " value from ResultSet", e);
                } catch (IllegalAccessException | IllegalArgumentException e) {
                    throw new ExcMapperException("Can not set field " + f.getName() + " of type " + object.getClass(), e);
                }
            });
            return object;
        } catch (ReflectiveOperationException e) {
            throw new ExcMapperException("Failed to instantiate object", e);
        }
    }

    private void injectId(T object, long id) throws IllegalAccessException {
        classMetaData.getIdField().set(object, id);
    }

    private int extractId(T object) throws IllegalAccessException {
        return ((Number) classMetaData.getIdField().get(object)).intValue();
    }

    private List<Object> extractFields(T object, List<Field> fields) {
        return fields.stream().map(f -> {
            try {
                return f.get(object);
            } catch (IllegalAccessException e) {
                throw new ExcMapperException("Cannot get value of " + f.getName() + " field", e);
            }
        }).collect(Collectors.toList());
    }

}
