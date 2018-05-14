package com.rds.ftms.dao;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.GenericRawResults;
import com.j256.ormlite.stmt.QueryBuilder;
import com.rds.ftms.bean.FtmsFamilyBean;
import com.rds.ftms.bean.FtmsFamilyMemberBean;
import com.rds.ftms.db.DatabaseHelper;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FtmsFamilyDao {
    private Context context;
    private Dao<FtmsFamilyBean, Integer> familyDaoOpe;
    private DatabaseHelper helper;

    public FtmsFamilyDao(Context context) {
        this.context = context;
        try {
            helper = DatabaseHelper.getHelper(context);
            familyDaoOpe = helper.getDao(FtmsFamilyBean.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * 增加一个用户
     *
     * @param family
     * @throws SQLException
     */
    public void add(FtmsFamilyBean family) throws SQLException {
        /*//事务操作
        TransactionManager.callInTransaction(helper.getConnectionSource(),
				new Callable<Void>()
				{

					@Override
					public Void call() throws Exception
					{
						return null;
					}
				});*/
        try {
            familyDaoOpe.create(family);
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
    }

    /**
     * 增加一个用户
     *
     * @param family
     * @throws SQLException
     */
    public void update(FtmsFamilyBean family) throws SQLException {
        /*//事务操作
        TransactionManager.callInTransaction(helper.getConnectionSource(),
				new Callable<Void>()
				{

					@Override
					public Void call() throws Exception
					{
						return null;
					}
				});*/
        try {
//            familyDaoOpe.update(family);

//            String sql = "update tb_family set fname='"+family.getFname()+"',ffirstname='"+family.getFfirstname()+"',fsubdivision='"+family.getFsubdivision()+"',fremark='"+family.getFremark()+"' where fid = "+family.getFid()+" ";
            familyDaoOpe.updateRaw("update tb_family set fname=?,ffirstname=?,fsubdivision=?,fremark=? where fid = ?",
                    family.getFname(), family.getFfirstname(), family.getFsubdivision(), family.getFremark(), family.getFid() + "");
//            familyDaoOpe.updateRaw(sql,"");
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }

    }

    public FtmsFamilyBean get(int id) {
        try {
            return familyDaoOpe.queryForId(id);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Map<String, Object> queryByName(int pageNo, String params) {
        try {

//            String sql = "SELECT * FROM `tb_family` where name like '%"+params+"%' LIMIT "+10+" OFFSET "+(pageNo-1)*10;
            Map<String, Object> m_result = new HashMap<>();

            QueryBuilder builder = familyDaoOpe.queryBuilder();
//            if(params!=null&&params.length()!=0)
            builder.where().like("fname", "%" + params + "%");
            builder.offset((pageNo - 1) * 10);
            builder.limit(pageNo * 10);

            List<FtmsFamilyBean> result = builder.query();
            m_result.put("result", result);

            builder = familyDaoOpe.queryBuilder();
//            if(params!=null&&params.length()!=0)
            builder.where().like("fname", "%" + params + "%");
            m_result.put("total", builder.countOf());

            return m_result;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<FtmsFamilyBean> queryAll() {
        try {
            return familyDaoOpe.queryForAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Integer getCount() {
        try {
            GenericRawResults<String[]> rawResults = familyDaoOpe.queryRaw("select count( * ) from tb_family");
            String result = rawResults.getFirstResult()[0];
            return result == null ? 0 : Integer.parseInt(result);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 增加一个用户
     *
     * @param family
     * @throws SQLException
     */
    public void delete(FtmsFamilyBean family) throws SQLException {
        /*//事务操作
        TransactionManager.callInTransaction(helper.getConnectionSource(),
				new Callable<Void>()
				{

					@Override
					public Void call() throws Exception
					{
						return null;
					}
				});*/
        try {
            familyDaoOpe.delete(family);
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
    }

    public void ExportToCSV(String fileName) throws Exception {

        List<FtmsFamilyBean> list = this.queryAll();
        FileWriter fw;
        File sdCardDir = Environment.getExternalStorageDirectory();
        if (!sdCardDir.exists()) {
            return;
        }
        File saveFile = new File(sdCardDir, fileName);
        try {
            saveFile.createNewFile();
            fw = new FileWriter(saveFile);
            StringBuffer sb = new StringBuffer(1000);
            sb.append("fid,fname,ffirstname,fsubdivision,findate,fremark");
            // 写好每条记录后换行
            fw.write(sb.toString());
            fw.write("\r\n");
            // 写入数据
            for (int i = 0; i < list.size(); i++) {
                sb = new StringBuffer(1000);
                sb.append(list.get(i).toString());
                // 写好每条记录后换行
                fw.write(sb.toString());
                fw.write("\r\n");
            }
            // 将缓存数据写入文件
            fw.flush();
//            bfw.flush();
            // 释放缓存
//            bfw.close();
            fw.close();
            // Toast.makeText(mContext, "导出完毕！", Toast.LENGTH_SHORT).show();
            Log.v("导出数据", "导出完毕！");
        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        }
    }

}
