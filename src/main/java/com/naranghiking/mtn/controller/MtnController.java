package com.naranghiking.mtn.controller;

import com.naranghiking.common.dto.ApiResult;
import com.naranghiking.mtn.dto.Mtn;
import com.naranghiking.mtn.service.MtnService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/mtn")
@RequiredArgsConstructor
public class MtnController {
    private final MtnService mtnService;

    @GetMapping("/list")
    public ResponseEntity<ApiResult> selectAll() {
        List<Mtn> mtns = mtnService.selectAll();
        return ResponseEntity.ok(ApiResult.success(mtns));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResult> select(
            @PathVariable Integer id
    ) {
        Mtn mtn = mtnService.select(id);
        if (mtn == null) {
            throw new RuntimeException("해당 번호의 산이 존재하지 않습니다.");
        }
        return ResponseEntity.ok(ApiResult.success(mtn));
    }

    @PostMapping
    public ResponseEntity<ApiResult> insert(
            @RequestBody Mtn mtn
    ) {
        Mtn target = mtnService.select(mtn.getId());
        if (target != null) {
            throw new RuntimeException("해당 산이 이미 존재합니다.");
        }
        mtnService.insert(mtn);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResult.success(mtn));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResult> update(
            @PathVariable Integer id,
            @RequestBody Mtn mtn
    ) {
        Mtn target = mtnService.select(id);
        if (target == null) {
            throw new RuntimeException("해당 산이 존재하지 않습니다.");
        }
        mtnService.update(mtn);
        return ResponseEntity.status(HttpStatus.OK).body(ApiResult.success(mtn));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResult> delete(
            @PathVariable Integer id
    ) {
        mtnService.delete(id);
        return ResponseEntity.status(HttpStatus.OK).body(ApiResult.success(null));
    }

}
