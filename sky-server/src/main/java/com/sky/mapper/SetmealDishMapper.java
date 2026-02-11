package com.sky.mapper;

import com.sky.entity.SetmealDish;
import com.sky.vo.DishItemVO;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface SetmealDishMapper {

    @Select("select * from setmeal_dish where dish_id = #{id}")
    List<SetmealDish> getByDishId(Long id);

    /**
     * 根据套餐id查询菜品选项
     * @param setmealId
     * @return
     */
    @Select("select sd.name, sd.copies, d.image, d.description " +
            "from setmeal_dish sd left join dish d on sd.dish_id = d.id " +
            "where sd.setmeal_id = #{setmealId}")
    List<DishItemVO> getDishItemBySetmealId(Long setmealId);

    @Insert("insert into setmeal_dish (setmeal_id, dish_id, name, price, copies) " +
            "values (#{setmealId}, #{dishId}, #{name}, #{price}, #{copies})")
    void insert(SetmealDish dish);

    @Select("select * from setmeal_dish where setmeal_id = #{id}")
    List<SetmealDish> getBySetmealId(Long id);

    void insertBatch(List<SetmealDish> setmealDishes);

    void deleteBySetmealId(Long id);
}
