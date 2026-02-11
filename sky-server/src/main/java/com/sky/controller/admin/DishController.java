package com.sky.controller.admin;

import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

@Slf4j
@Controller
@ResponseBody
@RequestMapping("/admin/dish")
@Api(tags = "菜品相关接口")
public class DishController {

    @Autowired
    private DishService dishService;

    @ApiOperation("新增菜品")
    @PostMapping
    @CacheEvict(cacheNames = "DishVO", key = "#dishDTO.getCategoryId()")
    public Result save(@RequestBody DishDTO dishDTO) {
        log.info("新增菜品: {}", dishDTO);
        dishService.save(dishDTO);
        return Result.success();
    }

    @ApiOperation("分页查询")
    @GetMapping("/page")
    public Result<PageResult> pageQuery(DishPageQueryDTO dishPageQueryDTO) {
        log.info("分页查询: {}", dishPageQueryDTO);
        PageResult pageResult = dishService.pageQuery(dishPageQueryDTO);
        return Result.success(pageResult);
    }

    @ApiOperation("批量删除菜品")
    @DeleteMapping
    @CacheEvict(cacheNames = "DishVO", allEntries = true)
    public Result deleteBatch(Long[] ids) {
        log.info("批量删除菜品: {}", Arrays.toString(ids));
        dishService.deleteBatch(ids);
        return Result.success();
    }

    @ApiOperation("菜品查询回显")
    @GetMapping("/{id}")
    public Result<DishVO> getById(@PathVariable Long id) {
        log.info("菜品查询回显: {}", id);
        DishVO dishVO = dishService.getById(id);
        return Result.success(dishVO);
    }

    @ApiOperation("修改菜品")
    @PutMapping
    @CacheEvict(cacheNames = "DishVO",  allEntries = true)
    public Result update(@RequestBody DishDTO dishDTO) {
        log.info("修改菜品 {}", dishDTO);
        dishService.update(dishDTO);

        return Result.success();
    }

    @CacheEvict(cacheNames = "DishVO", allEntries = true)
    @ApiOperation("起售/停售")
    @PostMapping("/status/{status}")
    public Result startOrStop(@PathVariable Integer status, Long id) {
        log.info("{} 菜品 {}", status == 1 ? "起售" : "停售", id);
        dishService.startOrStop(status, id);

        return Result.success();
    }

    @GetMapping("/list")
    public Result<List<Dish>> getByCategoryId(@RequestParam("categoryId") Long id) {
        log.info("{}", id);
        List<Dish> list = dishService.getByCategoryId(id);
        return Result.success(list);
    }

}
