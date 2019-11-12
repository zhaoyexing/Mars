package com.st.service.impl;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.st.enums.ErrorCodeEnum;
import com.st.mapper.*;
import com.st.model.*;
import com.st.service.UserGroupService;
import com.st.utils.*;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.collections.map.HashedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.*;
import java.sql.Timestamp;
import java.util.*;

/**
 * 获取用户群组筛选条件
 * Created by zhaoyx on 2016/11/8.
 */
@Service
public class UserGroupServiceImpl implements UserGroupService {
    @Value("${user.draw.url}")
    private String urlValue;
    @Value("${user.draw.short_url}")
    private String shortUrl;
    @Value("${local.path}")
    private String localPath;
    @Autowired
    private MarsTagsMapper marsTagsMapper;
    @Autowired
    private MarsTagClassMapper marsTagClassMapper;
    @Autowired
    private MarsTagClassTmpMapper marsTagClassTmpMapper;
    @Autowired
    private MarsTagsTmpMapper marsTagsTmpMapper;
    @Autowired
    private MarsUserGroupMapper marsUserGroupMapper;

    /**
     * 获取筛选条件
     *
     * @return
     */
//    @Override
//    public OpResult<Map<String, Object>> getScreeningConditions() {
//        Map<String, Object> returnMap = new HashMap<>();
//        try {
//            for (int j = 1; j <= 6; j++) {
//                //查询顶结点集合
//                List<MarsTagClassTmp> marsTagClassTmpList = marsTagClassTmpMapper.getMarsTagClassTmpList(j);
//                //返回结果集
//                List<Map<String, Object>> marsTagClassList = new ArrayList<>();
//                for (MarsTagClassTmp marsTagClassTmp : marsTagClassTmpList) {
//                    Map<String, Object> map = new HashMap<>();
//                    //根据父类别id查询子类集合
//                    List<MarsTagsTmp> marsTagsTmpList = marsTagsTmpMapper.getMarsTagTmpList(marsTagClassTmp.getClassId());
//                    if (null != marsTagsTmpList && marsTagsTmpList.size() > 0) {
//                        //名字
//                        map.put("name", marsTagClassTmp.getClassName());
//                        map.put("classId", marsTagClassTmp.getClassId());
//                        //根据父类别id查询子类集合
//                        List<Map<String, Object>> marsTagList = new ArrayList<>();
//                        //遍历子类集合
//                        marsTagsTmpList.forEach(marsTagsTmp -> {
//                            Map<String, Object> marsMap = new HashMap<>();
//                            //子类名字
//                            marsMap.put("name", marsTagsTmp.getTagName());
//                            //子类Id
//                            marsMap.put("id", marsTagsTmp.getTagId());
//                            boolean flag = false;
//                            //如果类型为1，说明是活跃度参数，需要补充值区别0-1
//                            if (marsTagsTmp.getType().equals("1")) {
//                                List<Map<String, Object>> lists = new ArrayList<>();
//                                marsMap = new HashMap<>();
//                                //循环十次，间隔0.1，前台传过来的id，要在缓存中查找对应的活跃度。
//                                flag = true;
//                                for (double i = 0; i < 10; i++) {
//                                    Map<String, Object> value = new HashMap<>();
//                                    value.put("id", i);
//                                    //此处需要转换处理，因为进制原因
//                                    value.put("name", String.format("%.1f", i * 0.1) + "-" + String.format("%.1f", (i * 0.1 + 0.1)));
//                                    lists.add(value);
//                                    //添加子类value
//                                    marsMap.put("value", lists);
//                                    marsMap.put("name", marsTagsTmp.getTagName());
//                                    marsMap.put("classId", marsTagsTmp.getTagId());
//                                    marsTagClassList.add(marsMap);
//                                }
//                            }
//                            if (!flag)
//                                marsTagList.add(marsMap);
//                        });
//                        //设置value
//                        if (null!= marsTagList && marsTagList.size()>0){
//                            map.put("value", marsTagList);
//                            marsTagClassList.add(map);
//                        }
//                    }
//                }
//                returnMap.put(String.valueOf(j), marsTagClassList);
//            }
//            //构建返回值
//            return OpResult.createSucResult(returnMap);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return OpResult.createFailResult(ErrorCodeEnum.USER_COUNT_EXCEPTION);
//    }
    @Override
    public OpResult<Map<String, Object>> getScreeningConditions() {
        Map<String, Object> returnMap = new HashMap<>();
        try {
            for (int j = 1; j <= 6; j++) {
                //查询顶结点集合
                List<MarsTagClassTmp> marsTagClassTmpList = marsTagClassTmpMapper.getMarsTagClassTmpList(j);
                //返回结果集
                List<Map<String, Object>> marsTagClassList = new ArrayList<>();
                for (MarsTagClassTmp marsTagClassTmp : marsTagClassTmpList) {
                    Map<String, Object> map = new HashMap<>();
                    //根据父类别id查询子类集合
                    List<MarsTagsTmp> marsTagsTmpList = marsTagsTmpMapper.getMarsTagTmpList(marsTagClassTmp.getClassId());
                    if (null != marsTagsTmpList && marsTagsTmpList.size() > 0) {
                        //名字
                        map.put("name", marsTagClassTmp.getClassName());
                        map.put("classId", marsTagClassTmp.getClassId());
                        //根据父类别id查询子类集合
                        List<Map<String, Object>> marsTagList = new ArrayList<>();
                        //遍历子类集合
                        marsTagsTmpList.forEach(marsTagsTmp -> {
                            Map<String, Object> marsMap = new HashMap<>();
                            //子类名字
                            marsMap.put("name", marsTagsTmp.getTagName());
                            //子类Id
                            marsMap.put("id", marsTagsTmp.getTagId());
                            boolean flag = false;
                            //如果类型为1，说明是活跃度参数，需要补充值区别0-1
                            if (marsTagsTmp.getType().equals("1")) {
                                List<Map<String, Object>> lists = new ArrayList<>();
                                marsMap = new HashMap<>();
                                //循环十次，间隔0.1，前台传过来的id，要在缓存中查找对应的活跃度。
                                flag = true;
                                for (double i = 0; i < 10; i++) {
                                    Map<String, Object> value = new HashMap<>();
                                    value.put("id", i);
                                    //此处需要转换处理，因为进制原因
                                    value.put("name", String.format("%.1f", i * 0.1) + "-" + String.format("%.1f", (i * 0.1 + 0.1)));
                                    lists.add(value);
                                }
                                //添加子类value
                                marsMap.put("value", lists);
                                marsMap.put("name", marsTagsTmp.getTagName());
                                marsMap.put("classId", marsTagsTmp.getTagId());
                                marsTagClassList.add(marsMap);
                            }
                            if (!flag)
                                marsTagList.add(marsMap);
                        });
                        //设置value
                        if (null!= marsTagList && marsTagList.size()>0){
                            map.put("value", marsTagList);
                            marsTagClassList.add(map);
                        }
                    }
                }
                returnMap.put(String.valueOf(j), marsTagClassList);
            }
            //构建返回值
            return OpResult.createSucResult(returnMap);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return OpResult.createFailResult(ErrorCodeEnum.USER_COUNT_EXCEPTION);
    }

    /**
     * 删除用户群组
     * @param id
     * @return
     */
    @Override
    public OpResult<Map<String, Object>> deleteUserGroup(String id) {

        try {
            Map<String, Object> map = new HashMap<>();
            MarsUserGroup userGroup = marsUserGroupMapper.getUserGroupConditionsById(id);
            String name = userGroup.getGroupName();
            marsUserGroupMapper.delete(id);
            File file = new File(localPath+name+".xls");
            if (file.exists()){
                file.delete();
            }
            Integer count = marsUserGroupMapper.getUserGroupCount();
            map.put("total", count);
            return OpResult.createSucResult(map);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return OpResult.createFailResult(ErrorCodeEnum.USER_COUNT_EXCEPTION);
    }

    /**
     * 修改用户群组
     * @param id
     * @param lifeCycle
     * @param groupName
     * @param conditions
     * @return
     */
    @Override
    public OpResult<String> updateUserGroup(String id,String lifeCycle, String groupName, String conditions) {
        try {
            if (null==lifeCycle){
                lifeCycle = "微信用户";
            }
            MarsUserGroup userGroup = marsUserGroupMapper.getUserGroupConditionsById(id);
            String name = userGroup.getGroupName();
            MarsUserGroup marsUserGroup = new MarsUserGroup( Integer.parseInt(id),  groupName,  conditions,  new Timestamp(new Date().getTime()),lifeCycle);
            marsUserGroupMapper.updateUserGroup(marsUserGroup);

            File file = new File(localPath+name+".zip");
            if (file.exists()){
                file.delete();
            }
            sheetUtil(groupName, lifeCycle,conditions);
            return OpResult.createSucResult("Succeed");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return OpResult.createFailResult(ErrorCodeEnum.USER_COUNT_EXCEPTION);

    }

    /**
     * 共同调用的方法
     * @param groupName
     * @param conditions
     * @throws IOException
     */
    private void sheetUtil(String groupName, String lifeCycle,String conditions) throws IOException {
        Map<String,String> mapes = new HashedMap();
        mapes = CombineSendJson.combineJson(conditions, null);
        Map<String, String> data = new HashMap<>();
        data.put("appid", "35");
        data.put("passwd", "8344202725");
        data.put("appkey", "172205760");
        data.put("method", "getListByCollectionQuery");
        data.put("page", "1");
        data.put("pageSize", "1");
        data.put("sortFlag", "true");
        data.put("sortType", "desc");
        data.put("sortPropertyName", "loyalty_val");
        data.put("tagCondition", mapes.get("tagCondition"));
        if (!"".equals(mapes.get("propertyCondition"))){
            data.put("propertyCondition",mapes.get("propertyCondition"));
        }
        String returnJson = OkHttpUtils.post(shortUrl,data);
        //String returnJsons = OkHttpUtils.get(urlValue+"getListByCollectionQuery&page=1&pageSize=10&sortFlag=true&sortType=desc&sortPropertyName=loyalty_val&tagCondition=" + jsons);
        JSONObject jsonObject = JSONObject.fromObject(returnJson);
        JSONObject datas = JSONObject.fromObject(jsonObject.get("datas"));
        int totalRecordSizes = datas.getInt("totalRecordSize");
        List<String> list1 = new ArrayList<>();
        int page = totalRecordSizes/10000+1;
        int bili = 0;
        for (int s =1;s<=page;s++){
            List<UserGroup> listing = new ArrayList<>();
            Map<String, String> dataes = new HashMap<>();
            dataes.put("appid", "35");
            dataes.put("passwd", "8344202725");
            dataes.put("appkey", "172205760");
            dataes.put("method", "getListByCollectionQuery");
            dataes.put("page", String.valueOf(s));
            dataes.put("pageSize", "10000");
            dataes.put("sortFlag", "true");
            dataes.put("sortType", "desc");
            dataes.put("sortPropertyName", "loyalty_val");
            dataes.put("tagCondition", mapes.get("tagCondition"));
            if (!"".equals(mapes.get("propertyCondition"))){
                dataes.put("propertyCondition",mapes.get("propertyCondition"));
            }
            String returnJsons = OkHttpUtils.post(shortUrl,dataes);
            JSONObject jsonObject1 = JSONObject.fromObject(returnJsons);
            JSONObject datas1 = JSONObject.fromObject(jsonObject1.get("datas"));
            JSONArray a = datas1.getJSONArray("detail");
            for (int i=0;i<a.size();i++){
                String b =String.valueOf(a.getJSONObject(i));
                Map<String, String> tagMap = new Gson().fromJson(b, new TypeToken<Map<String, String>>() {}.getType());
                if ("unknown".equals(tagMap.get("openid"))){
                    tagMap.put("openid","未知");
                }
                if ("unknown".equals(tagMap.get("uid"))){
                    tagMap.put("uid","未知");
                }
                if ("unknown".equals(tagMap.get("nick_name"))){
                    tagMap.put("nick_name","未知");
                }
                if ("unknown".equals(tagMap.get("province"))){
                    tagMap.put("province","未知");
                }
                UserGroup userGroup = new UserGroup();
                userGroup.setProvice(tagMap.get("province"));
                userGroup.setNikeName(tagMap.get("nick_name"));
                userGroup.setuId(tagMap.get("uid"));
                userGroup.setOpenId(tagMap.get("openid"));
                listing.add(userGroup);
            }
            bili++;
            if ("电商用户".equals(lifeCycle)){
                String[] air = {"买家ID","买家姓名","地址"};
                String path = ManipulationUtil.toExcel(listing,localPath,bili,groupName,lifeCycle,air);
                list1.add(path);
            }else {
                String[] air = {"OpenId","微信昵称","地区"};
                String path = ManipulationUtil.toExcel(listing,localPath,bili,groupName,lifeCycle,air);
                list1.add(path);
            }

        }
        File zip = new File(localPath + groupName+".zip");
        File srcfile[] = new File[list1.size()];
        for (int i = 0, n = list1.size(); i < n; i++) {
            srcfile[i] = new File(list1.get(i));
        }
        ManipulationUtil.ZipFiles(srcfile, zip);
//        FileInputStream inStream = new FileInputStream(zip);
//        byte[] buf = new byte[4096];
//        int readLength;
//        while (((readLength = inStream.read(buf)) != -1)) {
//            out.write(buf, 0, readLength);
//        }
//        inStream.close();

    }

    /**添加用户群组
     *
     * @param groupName
     * @param lifeCycle
     * @param conditions
     * @return
     */
    @Override
    public OpResult<String> saveUserGroup(String groupName,String lifeCycle, String conditions) {
        try {
            if (null==lifeCycle){
                lifeCycle = "微信用户";
            }
            MarsUserGroup marsUserGroup=new MarsUserGroup(groupName, conditions,new Timestamp(new Date().getTime()),new Timestamp(new Date().getTime()),lifeCycle);
            marsUserGroupMapper.saveUserGroupBatch(marsUserGroup);
            sheetUtil(groupName, lifeCycle,conditions);
            return OpResult.createSucResult("Succeed");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return OpResult.createFailResult(ErrorCodeEnum.USER_COUNT_EXCEPTION);
    }

    /**
     * 根据id获取用户群组
     * @param id
     * @return
     */
    @Override
    public MarsUserGroup getMarsUserGroup(String id) {
        MarsUserGroup marsUserGroup = marsUserGroupMapper.getUserGroupConditionsById(id);
        return marsUserGroup;
    }

    @Override
    public OpResult<String> getFileExcis(String id) {

            MarsUserGroup userGroup = getMarsUserGroup(id);
            String filePath = localPath + userGroup.getGroupName()+".zip";
            File file = new File(filePath);
            if (file.exists()){
                return OpResult.createSucResult("Succeed");
            }else {
                return OpResult.createFailResult(ErrorCodeEnum.USER_COUNT_EXCEPTION);
            }
    }


    @Override
    public OpResult<List<Map<String, Object>>> selectUserGroup(int pageNo, int pageSize) {
        try {
            List<MarsUserGroups> lists = new ArrayList<>();
            int noPage = (pageNo - 1) * pageSize;
            List<MarsUserGroup> userLsit = marsUserGroupMapper.getUserGroups(noPage, pageSize);
            for (MarsUserGroup m :userLsit){
                MarsUserGroups marsUserGroups = new MarsUserGroups();
                Date dat=new Date(m.getCreateTime().getTime());
                GregorianCalendar gc = new GregorianCalendar();
                gc.setTime(dat);
                java.text.SimpleDateFormat format = new java.text.SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                String sb=format.format(gc.getTime());
                marsUserGroups.setCreateTime(sb);
                marsUserGroups.setGroupName(m.getGroupName());
                marsUserGroups.setId(m.getId());
                marsUserGroups.setQueryCondition(m.getQueryCondition());
                marsUserGroups.setLifeCycle(m.getLifeCycle());
                lists.add(marsUserGroups);
            }
            Integer countPage = marsUserGroupMapper.getUserGroupCount();
            Map<String, Object> maps = new HashMap<>();
            List<Map<String, Object>> list = new ArrayList<>();
            lists.sort((o1, o2) ->String.valueOf(o2.getCreateTime()).compareTo(String.valueOf(o1.getCreateTime())));
            maps.put("value", lists);
            maps.put("countPage", countPage);
            list.add(maps);
            return OpResult.createSucResult(list);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return OpResult.createFailResult(ErrorCodeEnum.USER_COUNT_EXCEPTION);
    }

    /**
     * 生成csv文件的方法
     * @param filePath
     * @param dataList
     */
//    public void exportCsv(String filePath, List<UserGroup> dataList){
//
//        FileOutputStream out=null;
//        OutputStreamWriter osw=null;
//        BufferedWriter bw=null;
//        try {
//            out = new FileOutputStream(filePath);
//            osw = new OutputStreamWriter(out);
//            bw =new BufferedWriter(osw);
//            if(dataList!=null && !dataList.isEmpty()){
//                String header = "买家ID,买家姓名,地域,用户类型,手机号\r\n";
//                bw.append(header);
//                for (UserGroup userGroup:dataList){
//                    bw.append(userGroup.getuId());
//                    bw.append(",");
//                    bw.append(userGroup.getNickName());
//                    bw.append(",");
//                    bw.append(userGroup.getCity());
//                    bw.append(",");
//                    bw.append(userGroup.getLifeCycle());
//                    bw.append(",");
//                    bw.append(userGroup.getContact()).append("\r\n");
//                }
////                for(String data : dataList){
////                    bw.append(data).append("\r\n");
////                }
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }finally{
//            if(bw!=null){
//                try {
//                    bw.close();
//                    bw=null;
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//            if(osw!=null){
//                try {
//                    osw.close();
//                    osw=null;
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//            if(out!=null){
//                try {
//                    out.close();
//                    out=null;
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//    }

}
