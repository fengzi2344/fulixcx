package com.gys.fulixcx.dao;

import com.gys.fulixcx.mode.CallCompanyMode;
import com.gys.fulixcx.mode.CallCompanyPhoneMode;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Map;

@Transactional
public interface CallCompanyPhoneDao extends CrudRepository<CallCompanyPhoneMode, Integer> {
    CallCompanyPhoneMode findById(int id);

    CallCompanyPhoneMode findByCompanyIdAndPhoneId(int cid,int id);


    @Query(nativeQuery = true, value = "SELECT C.staff_id,(select staff_name from call_staff where id = C.staff_id) staff_name,C.up_time,C.task_id,P.* FROM call_company_phone C,call_phone P" +
            " WHERE C.company_id = ?1 AND C.phone_id = P.id order by P.id desc limit ?2,?3")
    List<Map<String,String>> findListbycomId(int id,int index,int num);


    @Query(nativeQuery = true, value = "SELECT COUNT(*) FROM call_company_phone WHERE company_id = ?1")
    int findNumbycomId(int id);

    @Query(nativeQuery = true, value = "SELECT " +
            "T.*,P.phone_number,P.carrieroperator,P.attribution " +
            "FROM call_company_phone T,call_phone P WHERE " +
            "T.task_id = ?1 AND T.phone_id = P.id")
    List<Map<String,String>> findTaskPhone(int taskId);
    @Query(nativeQuery = true,value = "select count(*) from call_company_phone where company_id = ?1 and task_id = 0")
    int findDistriNum(int comId);

    @Modifying
    @Query(nativeQuery = true,value = "UPDATE call_company_phone t SET t.task_id = ?1 WHERE " +
            "EXISTS (SELECT 1 FROM (SELECT id FROM call_company_phone s WHERE s.task_id = 0 AND s.company_id = ?2 LIMIT ?3) tmp WHERE " +
            "tmp.id = t.id)")
    int setTask(int taskId,int comId,int num);


    @Query(nativeQuery = true, value = "SELECT * from call_company_phone where company_id = ?1 task_id = 0 limit ?2")
    List<CallCompanyPhoneMode> findListbyNum(int id,int num);
    @Query(nativeQuery = true, value = "select count(*) dialall,ifnull(sum(converse_time),0) alltime," +
            "ifnull(sum(case when converse_time>0 then 1 ELSE 0 end),0) effective," +
            "ifnull((sum(converse_time)/sum(case when converse_time>0 then 1 ELSE 0 end)),0) a from " +
            "call_company_phone WHERE " +
            "dial_type >0 and dial_time > ?1 and dial_time <?2 AND company_id = ?3")
    Map<String,String> findSum(String starttime,String endtime,int comId);
}
