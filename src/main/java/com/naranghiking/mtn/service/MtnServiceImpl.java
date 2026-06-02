package com.naranghiking.mtn.service;

import com.naranghiking.mtn.dao.MtnDao;
import com.naranghiking.mtn.dto.Mtn;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class MtnServiceImpl implements MtnService {

    private final MtnDao mtnDao;

    @Override
    public List<Mtn> selectAll() {
        return mtnDao.selectAll();
    }

    @Override
    public Mtn select(Integer id) {
        return mtnDao.select(id);
    }

    @Override
    public int insert(Mtn mtn) {
        return mtnDao.insert(mtn);
    }

    @Override
    public int update(Mtn mtn) {
        return mtnDao.update(mtn);
    }

    @Override
    public void delete(Integer id) {
        mtnDao.delete(id);
    }
}
