package com.naranghiking.mtn.dao;

import com.naranghiking.mtn.dto.Mtn;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface MtnDao {

    List<Mtn> selectAll();

    List<Mtn> selectByName(String name);

    Mtn select(Long id);

    int insert(Mtn mtn);

    int update(Mtn mtn);

    void delete(Long id);
}
