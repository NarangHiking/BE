package com.naranghiking.mtn.service;

import com.naranghiking.common.service.R2Service;
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
    private final R2Service r2Service;

    @Override
    public List<Mtn> selectAll() {
        List<Mtn> mtns = mtnDao.selectAll();
        mtns.forEach(m -> m.setImageUrl(r2Service.getPublicUrl(m.getStoredFilename())));
        return mtns;
    }

    @Override
    public Mtn select(Long id) {
        Mtn mtn = mtnDao.select(id);
        if (mtn != null) {
            mtn.setImageUrl(r2Service.getPublicUrl(mtn.getStoredFilename()));
        }
        return mtn;
    }

    @Override
    public List<Mtn> selectByRecommend(Integer limit) {
        List<Mtn> mtns = mtnDao.selectByRecommend(limit);
        mtns.forEach(m -> m.setImageUrl(r2Service.getPublicUrl(m.getStoredFilename())));
        return mtns;
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
    public void delete(Long id) {
        mtnDao.delete(id);
    }
}
