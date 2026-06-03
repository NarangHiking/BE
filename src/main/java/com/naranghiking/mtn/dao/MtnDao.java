package com.naranghiking.mtn.dao;

import com.naranghiking.mtn.dto.Mtn;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface MtnDao {

    List<Mtn> selectAll();

    Mtn select(Integer id);

    int insert(Mtn mtn);

    int update(Mtn mtn);

    void delete(Integer id);
}
