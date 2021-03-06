package net.orekyuu.workbench.entity.dao;

import net.orekyuu.workbench.entity.OpenTicket;
import org.seasar.doma.*;
import org.seasar.doma.boot.ConfigAutowireable;

import java.util.Optional;
import java.util.stream.Collector;

@ConfigAutowireable
@Dao
public interface OpenTicketDao {

    @Insert(sqlFile = true)
    int insert(OpenTicket ticket);

    @Update(sqlFile = true)
    int update(OpenTicket ticket);

    @Select(strategy = SelectType.COLLECT)
    <RESULT> RESULT findByProject(String projectId, Collector<OpenTicket, ?, RESULT> collector);

    @Delete(sqlFile = true)
    int deleteByProject(String projectId);

    @Select
    Optional<OpenTicket> findByProjectAndNum(String projectId, int ticketNum);
}
