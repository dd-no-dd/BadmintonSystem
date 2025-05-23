package com.wuli.badminton.dao;

import com.wuli.badminton.pojo.Post;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 帖子数据访问接口
 */
@Mapper
public interface PostMapper {
    /**
     * 插入新帖子
     * 
     * @param post 帖子对象
     * @return 影响的行数
     */
    int insert(Post post);
    
    /**
     * 根据ID查询帖子
     * 
     * @param id 帖子ID
     * @return 帖子对象
     */
    Post findById(Long id);
    
    /**
     * 更新帖子信息
     * 
     * @param post 帖子对象
     * @return 影响的行数
     */
    int update(Post post);
    
        /**
     * 设置帖子置顶状态
     * 
     * @param postId 帖子ID
     * @param isTop 置顶状态：0-不置顶，1-置顶
     * @return 影响的行数
     */
    int updateTopStatus(@Param("postId") Long postId, @Param("isTop") Integer isTop);

    /**
     * 删除帖子
     * 
     * @param id 帖子ID
     * @return 影响的行数
     */
    int deleteById(Long id);
    
    /**
     * 增加帖子浏览次数
     * 
     * @param id 帖子ID
     * @return 影响的行数
     */
    int incrementViews(Long id);
    
    /**
     * 更新帖子回复数和最后回复时间
     * 
     * @param id 帖子ID
     * @return 影响的行数
     */
    int updateRepliesAndLastReplyTime(Long id);
    
    /**
     * 查询帖子列表，支持分类和关键词过滤
     * 
     * @param categoryId 分类ID，为null时查询所有分类
     * @param keyword 搜索关键词，为null时不进行关键词过滤
     * @param offset 分页起始位置
     * @param limit 分页大小
     * @return 帖子列表
     */
    List<Post> findByFilter(@Param("categoryId") Long categoryId, 
                           @Param("keyword") String keyword,
                           @Param("offset") int offset, 
                           @Param("limit") int limit);
    
    /**
     * 统计符合条件的帖子总数
     * 
     * @param categoryId 分类ID，为null时查询所有分类
     * @param keyword 搜索关键词，为null时不进行关键词过滤
     * @return 符合条件的帖子总数
     */
    long countByFilter(@Param("categoryId") Long categoryId, 
                      @Param("keyword") String keyword);
    
    /**
     * 查询热门帖子（按照浏览次数和回复数排序）
     * 
     * @param limit 限制条数
     * @return 热门帖子列表
     */
    List<Post> findHotPosts(@Param("limit") int limit);

    /**
     * 根据用户ID查询帖子列表
     * 
     * @param userId 用户ID
     * @param offset 分页起始位置
     * @param limit 分页大小
     * @return 帖子列表
     */
    List<Post> findByUserId(@Param("userId") Long userId, 
                          @Param("offset") int offset, 
                          @Param("limit") int limit);

    /**
     * 统计用户的帖子总数
     * 
     * @param userId 用户ID
     * @return 该用户的帖子总数
     */
    long countByUserId(@Param("userId") Long userId);
} 