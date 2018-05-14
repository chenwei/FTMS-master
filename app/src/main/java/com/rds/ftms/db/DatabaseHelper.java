package com.rds.ftms.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import com.rds.ftms.bean.Article;
import com.rds.ftms.bean.FtmsFamilyBean;
import com.rds.ftms.bean.FtmsFamilyMemberBean;
import com.rds.ftms.bean.Student;
import com.rds.ftms.bean.User;
import com.rds.ftms.utils.Constants;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * chenwei
 * 数据库操作底层类
 */

public class DatabaseHelper extends OrmLiteSqliteOpenHelper {
    private static final String TABLE_NAME = Constants.DB;

    private Map<String, Dao> daos = new HashMap<String, Dao>();

    private DatabaseHelper(Context context) {
        super(context, TABLE_NAME, null, 4);
    }

    /**
     * 创建表
     * @param database
     * @param connectionSource
     */
    @Override
    public void onCreate(SQLiteDatabase database,
                         ConnectionSource connectionSource) {
        try {
            TableUtils.createTable(connectionSource, FtmsFamilyBean.class);
            TableUtils.createTable(connectionSource, FtmsFamilyMemberBean.class);
//            TableUtils.createTable(connectionSource, User.class);
//            TableUtils.createTable(connectionSource, Article.class);
//            TableUtils.createTable(connectionSource, Student.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * 更新表
     * @param database
     * @param connectionSource
     * @param oldVersion
     * @param newVersion
     */
    @Override
    public void onUpgrade(SQLiteDatabase database,
                          ConnectionSource connectionSource, int oldVersion, int newVersion) {
        try {
            TableUtils.createTable(connectionSource, FtmsFamilyBean.class);
            TableUtils.createTable(connectionSource, FtmsFamilyMemberBean.class);
//            TableUtils.dropTable(connectionSource, User.class, true);
//            TableUtils.dropTable(connectionSource, Article.class, true);
//            TableUtils.dropTable(connectionSource, Student.class, true);
            onCreate(database, connectionSource);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static DatabaseHelper instance;

    /**
     * 单例获取该Helper
     *
     * @param context
     * @return
     */
    public static synchronized DatabaseHelper getHelper(Context context) {
        context = context.getApplicationContext();
        if (instance == null) {
            synchronized (DatabaseHelper.class) {
                if (instance == null)
                    instance = new DatabaseHelper(context);
            }
        }

        return instance;
    }

    public synchronized Dao getDao(Class clazz) throws SQLException {
        Dao dao = null;
        String className = clazz.getSimpleName();

        if (daos.containsKey(className)) {
            dao = daos.get(className);
        }
        if (dao == null) {
            dao = super.getDao(clazz);
            daos.put(className, dao);
        }
        return dao;
    }

    /**
     * 释放资源
     */
    @Override
    public void close() {
        super.close();

        for (String key : daos.keySet()) {
            Dao dao = daos.get(key);
            dao = null;
        }
    }

}