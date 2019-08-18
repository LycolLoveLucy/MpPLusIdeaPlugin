package com.personal.lycol.plugin.gen;

import com.baomidou.mybatisplus.generator.config.ITypeConvert;
import com.baomidou.mybatisplus.generator.config.converts.SqlServerTypeConvert;

import javax.sql.DataSource;
import java.sql.SQLException;

public abstract class ColumnProcessFactory {


    public static ITypeConvert getItypeConvert(String jdbcUrl) throws SQLException {
        if (jdbcUrl.startsWith("jdbc:mysql:") || jdbcUrl.startsWith("jdbc:cobar:")
                || jdbcUrl.startsWith("jdbc:log4jdbc:mysql:")) {
            return new MysqlColumnProcessTypeConvert();

        } else if (jdbcUrl.startsWith("jdbc:oracle:") || jdbcUrl.startsWith("jdbc:log4jdbc:oracle:")) {
            return new OracelColumnProcessTypeConvert();
        }
        return  new SqlServerTypeConvert();
    }
}
