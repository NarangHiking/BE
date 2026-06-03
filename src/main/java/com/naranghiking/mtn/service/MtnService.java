package com.naranghiking.mtn.service;

import com.naranghiking.mtn.dto.Mtn;

import java.util.List;

public interface MtnService {

    List<Mtn> selectAll();

    Mtn select(Integer id);

    int insert(Mtn mtn);

    int update(Mtn mtn);

    void delete(Integer id);
}
