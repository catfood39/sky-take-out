package com.sky.controller.admin;

import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.SetmealService;
import com.sky.vo.SetmealVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
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

    @CacheEvict(cacheNames = "Setmeal", key = "#setmealDTO.getCategoryId()")
    @PostMapping
    public Result save(@RequestBody SetmealDTO setmealDTO) {

        setmealService.save(setmealDTO);
        return Result.success();
    }

    @GetMapping("/page")
    public Result<PageResult> pageQuery(SetmealPageQueryDTO dto) {

        PageResult pageResult =setmealService.pageQuery(dto);
        return Result.success(pageResult);
    }

    @GetMapping("/{id}")
    public Result<SetmealVO> getById(@PathVariable Long id) {

        SetmealVO setmealVO =setmealService.getById(id);
        return Result.success(setmealVO);
    }

    @CacheEvict(cacheNames = "Setmeal", allEntries = true)
    @PutMapping
    public Result update(@RequestBody SetmealDTO setmealDTO) {

        setmealService.update(setmealDTO);
        return Result.success();
    }

    @CacheEvict(cacheNames = "Setmeal", allEntries = true)
    @PostMapping("/status/{status}")
    public Result startOrStop(@PathVariable Integer status, @RequestParam Long id) {

        setmealService.startOrStop(status, id);
        return Result.success();
    }

    @CacheEvict(cacheNames = "Setmeal", allEntries = true)
    @DeleteMapping
    public Result deleteBatch(@RequestParam List<Long> ids) {

        setmealService.deleteBatch(ids);
        return Result.success();
    }

}
