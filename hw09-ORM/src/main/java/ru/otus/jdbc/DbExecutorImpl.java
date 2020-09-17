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
            for (int idx = 0; idx < params.size(); idx++) {
                pst.setObject(idx + 1, params.get(idx));
            }
            pst.executeUpdate();
            try (ResultSet rs = pst.getGeneratedKeys()) {
                rs.next();
                return rs.getInt(1);
            }
        } catch (SQLException ex) {
            connection.rollback(savePoint);
            logger.error(ex.getMessage(), ex);
            throw ex;
        }
    }

    @Override
    public boolean executeUpdate(Connection connection, String sql, List<Object> params) throws SQLException {
        Savepoint savePoint = connection.setSavepoint("savePointName");
        int result;
        try (var pst = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            for (int idx = 0; idx < params.size(); idx++) {
                pst.setObject(idx + 1, params.get(idx));
            }
            result = pst.executeUpdate();
            if (result > 0) {
                return true;
            }
        } catch (SQLException ex) {
            connection.rollback(savePoint);
            logger.error(ex.getMessage(), ex);
            throw ex;
        }
        return false;
    }

    @Override
    public Optional<T> executeSelect(Connection connection, String sql, long id,
                                     Function<ResultSet, T> rsHandler) {
        try (var pst = connection.prepareStatement(sql)) {
            pst.setLong(1, id);
            try (var rs = pst.executeQuery()) {
                return Optional.ofNullable(rsHandler.apply(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
