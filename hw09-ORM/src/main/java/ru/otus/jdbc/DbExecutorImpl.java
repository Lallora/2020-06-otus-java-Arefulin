package ru.otus.jdbc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

public class DbExecutorImpl<T> implements DbExecutor<T> {
    private static final Logger logger = LoggerFactory.getLogger(DbExecutorImpl.class);

    @Override
    public long executeInsert(Connection connection, String sql, List<Object> params) throws SQLException {
        Savepoint savePoint = connection.setSavepoint("savePointName");
        try (var pst = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            for (int i = 0; i < params.size(); i++) {
                pst.setObject(i + 1, params.get(i));
            }
            pst.executeUpdate();
            try (ResultSet rs = pst.getGeneratedKeys()) {
                rs.next();
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            connection.rollback(savePoint);
            logger.error(e.getMessage(), e);
            throw e;
        }
    }

    @Override
    public long executeGetMaxNumberOfRecordsSQL(Connection connection, String sql) throws SQLException {
        try (var pst = connection.prepareStatement(sql)) {
            ResultSet rs = pst.executeQuery();
            if (rs.next())
                return rs.getInt("TOTAL_COUNT");
        } catch (SQLException ex) {
            ex.printStackTrace();
            logger.error(ex.getMessage(), ex);
            throw ex;
        }
        return 0;
    }

    @Override
    public Optional<T> executeSelect(Connection connection, String sql, long id, Function<ResultSet, T> rsHandler) throws SQLException {
        try (PreparedStatement pst = connection.prepareStatement(sql)) {
            pst.setLong(1, id);
            try (ResultSet rs = pst.executeQuery()) {
                return Optional.ofNullable(rsHandler.apply(rs));
            }
        }
    }

    @Override
    public boolean executeUpdate(Connection connection, String sql, List<Object> params) throws SQLException {
        Savepoint savePoint = connection.setSavepoint("savePointName");
        int result;
        try (var pst = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            for (int i = 0; i < params.size(); i++) {
                pst.setObject(i + 1, params.get(i));
            }
            result = pst.executeUpdate();
            if (result > 0) {
                return true;
            }
        } catch (SQLException e) {
            connection.rollback(savePoint);
            logger.error(e.getMessage(), e);
            throw e;
        }
        return false;
    }
}

