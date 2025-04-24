package cn.edu.usst.cs.campusAid.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Optional;

@Mapper
public interface ExceptionMapper {
    @Insert("insert into error(content) values(#{exception})")
    void insertException(String exception);

    @Select("select content from error where id = (#{id})")
    String queryException(long id);

    @Select("select id from error where content=#{errorContent}")
    List<Long> getExceptionId(String errorContent);

    default long submitException(String exception) {
        insertException(exception);
        List<Long> id = getExceptionId(exception);
        Optional<Long> optional = id.stream().max(Long::compare);
        return optional.orElse(-1L);
    }
}
