package com.sky.controller.admin;

import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.SetmealService;
import com.sky.vo.SetmealVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/setmeal")
@Slf4j
public class SetmealController {

    private final SetmealService setmealService;

    public SetmealController(SetmealService setmealService) {
        this.setmealService = setmealService;
    }

    @PostMapping
    public Result save(@RequestBody SetmealDTO setmealDTO) {
        log.info("save {}", setmealDTO);
        setmealService.save(setmealDTO);
        return Result.success();
    }

    @GetMapping("/page")
    public Result<PageResult> pageQuery(SetmealPageQueryDTO dto) {
        log.info("setmeal page query {}", dto);
        PageResult pageResult =setmealService.pageQuery(dto);
        return Result.success(pageResult);
    }

    @GetMapping("/{id}")
    public Result<SetmealVO> getById(@PathVariable Long id) {
        log.info("setmeal get by id {}", id);
        SetmealVO setmealVO =setmealService.getById(id);
        return Result.success(setmealVO);
    }

    @PutMapping
    public Result update(@RequestBody SetmealDTO setmealDTO) {
        log.info("update {}", setmealDTO);
        setmealService.update(setmealDTO);
        return Result.success();
    }

    @PostMapping("/status/{status}")
    public Result startOrStop(@PathVariable Integer status, @RequestParam Long id) {
        log.info("setmeal {} start or stop {}", id, status);
        setmealService.startOrStop(status, id);
        return Result.success();
    }

    @DeleteMapping
    public Result delete(@RequestParam List<Long> ids) {
        log.info("delete {}", ids);
        setmealService.deleteBatch(ids);
        return Result.success();
    }

}
