package com.naranghiking.mtn.service;

import com.naranghiking.mtn.dto.Mtn;

import java.util.List;

public interface MtnService {

    List<Mtn> selectAll();

    Mtn select(Long id);

    int insert(Mtn mtn);

    int update(Mtn mtn);

    void delete(Long id);
}
