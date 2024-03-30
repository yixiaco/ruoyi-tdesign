package org.dromara.common.mybatis.helper;

import cn.hutool.core.convert.Convert;
import com.mybatisflex.core.dialect.DbType;
import com.mybatisflex.core.dialect.DialectFactory;
import com.mybatisflex.spring.boot.MybatisFlexProperties;
import org.dromara.common.core.utils.spring.SpringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 数据库助手
 *
 * @author hexm
 * @date 2024/03/30 10:38
 */
public class DbTypeHelper {

    private static final MybatisFlexProperties properties = SpringUtils.getBean(MybatisFlexProperties.class);


    public static DbType getDbType() {
        DbType dbType = DialectFactory.getHintDbType();
        if (dbType == null) {
            dbType = DialectFactory.getGlobalDbType();
        }
        return dbType;
    }

    public static boolean isMySql() {
        return DbType.MYSQL == getDbType();
    }

    public static boolean isOracle() {
        return DbType.ORACLE == getDbType();
    }

    public static boolean isPostgerSql() {
        return DbType.POSTGRE_SQL == getDbType();
    }

    public static boolean isSqlServer() {
        return DbType.SQLSERVER == getDbType();
    }

    public static String findInSet(Object var1, String var2) {
        DbType dbType = getDbType();
        String var = Convert.toStr(var1);
        if (dbType == DbType.SQLSERVER) {
            // charindex(',100,' , ',0,100,101,') <> 0
            return "charindex(',%s,' , ','+%s+',') <> 0".formatted(var, var2);
        } else if (dbType == DbType.POSTGRE_SQL) {
            // (select position(',100,' in ',0,100,101,')) <> 0
            return "(select position(',%s,' in ','||%s||',')) <> 0".formatted(var, var2);
        } else if (dbType == DbType.ORACLE) {
            // instr(',0,100,101,' , ',100,') <> 0
            return "instr(','||%s||',' , ',%s,') <> 0".formatted(var2, var);
        }
        // find_in_set(100 , '0,100,101')
        return "find_in_set('%s' , %s) <> 0".formatted(var, var2);
    }

    /**
     * 获取当前加载的数据库名
     */
    public static List<String> getDataSourceNameList() {
        return new ArrayList<>(properties.getDatasource().keySet());
    }
}
