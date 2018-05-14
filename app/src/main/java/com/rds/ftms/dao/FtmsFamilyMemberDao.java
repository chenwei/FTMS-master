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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FtmsFamilyMemberDao {
    private Context context;
    private Dao<FtmsFamilyMemberBean, Integer> familyMemberDaoOpe;
    private DatabaseHelper helper;

    public FtmsFamilyMemberDao(Context context) {
        this.context = context;
        try {
            helper = DatabaseHelper.getHelper(context);
            familyMemberDaoOpe = helper.getDao(FtmsFamilyMemberBean.class);
//
//            familyMemberDaoOpe.executeRaw("ALTER TABLE `tb_family_member` ADD COLUMN sampling TEXT DEFAULT '';");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<FtmsFamilyMemberBean> queryByNameOrCard(String params) {
        try {

//            String sql = "SELECT * FROM `tb_family` where name like '%"+params+"%' LIMIT "+10+" OFFSET "+(pageNo-1)*10;
            Map<String, Object> m_result = new HashMap<>();

            QueryBuilder builder = familyMemberDaoOpe.queryBuilder();
//            if(params!=null&&params.length()!=0)
            builder.where().like("name", "%" + params + "%").or().like("idcard", "%" + params + "%");

            List<FtmsFamilyMemberBean> result = builder.query();


            return result;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Map<String, Object> queryByNameOrCard1(int pageNo, String params) {
        try {

//            String sql = "SELECT * FROM `tb_family` where name like '%"+params+"%' LIMIT "+10+" OFFSET "+(pageNo-1)*10;
            Map<String, Object> m_result = new HashMap<>();

            QueryBuilder builder = familyMemberDaoOpe.queryBuilder();
//            if(params!=null&&params.length()!=0)
            builder.where().like("name", "%" + params + "%").or().like("idcard", "%" + params + "%");
            builder.offset((pageNo - 1) * 10);
            builder.limit(pageNo * 10);

            List<FtmsFamilyBean> result = builder.query();
            m_result.put("result", result);

            builder = familyMemberDaoOpe.queryBuilder();
//            if(params!=null&&params.length()!=0)
            builder.where().like("name", "%" + params + "%").or().like("idcard", "%" + params + "%");
            m_result.put("total", builder.countOf());

            return m_result;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 增加一个用户
     *
     * @param member
     * @throws SQLException
     */
    public void add(FtmsFamilyMemberBean member) throws SQLException {
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
            familyMemberDaoOpe.create(member);
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }

    }

    /**
     * 增加一个用户
     *
     * @param member
     * @throws SQLException
     */
    public void update(FtmsFamilyMemberBean member) throws SQLException {
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
            familyMemberDaoOpe.update(member);
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
    }

    /**
     * 增加一个用户
     *
     * @throws SQLException
     */
    public void update1() throws SQLException {
        try {
            familyMemberDaoOpe.updateRaw("update tb_family_member set sex = '男'");
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
    }

    public FtmsFamilyMemberBean get(int id) {
        try {
            return familyMemberDaoOpe.queryForId(id);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 合并
     *
     * @throws SQLException
     */
    public void update(int p_fid,int p_pid,int c_fid) throws SQLException {

        try {
            familyMemberDaoOpe.updateRaw("update tb_family_member set pid=? where fid = ? and pid=0", p_pid+"",c_fid+"");
            familyMemberDaoOpe.updateRaw("update tb_family_member set fid=? where fid = ?", p_fid+"",c_fid+"");
            FtmsFamilyDao dao = new FtmsFamilyDao(context);
            FtmsFamilyBean bean = new FtmsFamilyBean();
            bean.setFid(c_fid);
            dao.delete(bean);
//            familyDaoOpe.updateRaw(sql,"");
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }

    }

    public List<FtmsFamilyMemberBean> queryAll() {
        try {
            return familyMemberDaoOpe.queryForAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<FtmsFamilyMemberBean> queryMemberById(int id) {
        try {
            return familyMemberDaoOpe.queryBuilder().where().eq("id", id).query();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Map<String, Object>> queryByFamily(int fid) {
        try {
            QueryBuilder builder = familyMemberDaoOpe.queryBuilder();
            builder.where().eq("fid", fid);
            builder.orderBy("regtime",true);
            List<FtmsFamilyMemberBean> result = builder.query();

            List<Map<String, Object>> query_list = new ArrayList<>();
            for (FtmsFamilyMemberBean bean : result) {
                Map<String, Object> item = new HashMap<>();
                item.put("memberId", bean.getId() + "");
                item.put("memberName", bean.getName().trim());
                item.put("memberImg", "");
                item.put("call", "");
                item.put("sex", bean.getSex().equals("男") ? "1" : "2");
                item.put("birthday", bean.getBirthday());
                item.put("spouseId", "");
                item.put("fatherId", bean.getPid() == 0 ? "" : bean.getPid());
                item.put("motherId", "");
                item.put("fathersId", "");
                item.put("mothersId", "");
                query_list.add(item);
            }
            return query_list;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public long getCount() {
        try {
//            GenericRawResults<String[]> rawResults = familyMemberDaoOpe.queryRaw("select count( * ) from tb_family_member");
//            String result = rawResults.getFirstResult()[0];
//            return result == null ? 0 : Integer.parseInt(result);

            return familyMemberDaoOpe.queryBuilder().countOf();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * 增加一个用户
     *
     * @param member
     * @throws SQLException
     */
    public void delete(FtmsFamilyMemberBean member) throws SQLException {
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
            familyMemberDaoOpe.delete(member);
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
    }

    /**
     * 导出数据
     */
    public void ExportToCSV(String fileName) throws Exception {

        List<FtmsFamilyMemberBean> list = this.queryAll();
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
            sb.append("id,sex,nation,birthday,address,idcard,office,effective,residence,regtime,fid,pid,sampling");
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
