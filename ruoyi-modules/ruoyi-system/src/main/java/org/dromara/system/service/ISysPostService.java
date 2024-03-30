package org.dromara.system.service;

import com.mybatisflex.core.service.IService;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.system.domain.SysPost;
import org.dromara.system.domain.bo.SysPostBo;
import org.dromara.system.domain.query.SysPostQuery;
import org.dromara.system.domain.vo.SysPostVo;

import java.util.List;

/**
 * 岗位信息 服务层
 *
 * @author Lion Li
 */
public interface ISysPostService extends IService<SysPost> {

    /**
     * 获取岗位列表
     *
     * @param post 岗位信息
     * @return 岗位列表
     */
    TableDataInfo<SysPostVo> selectPagePostList(SysPostQuery post);

    /**
     * 查询岗位信息集合
     *
     * @param query 岗位查询对象
     * @return 岗位列表
     */
    List<SysPostVo> selectPostList(SysPostQuery query);

    /**
     * 查询所有岗位
     *
     * @return 岗位列表
     */
    List<SysPostVo> selectPostAll();

    /**
     * 通过岗位ID查询岗位信息
     *
     * @param postId 岗位ID
     * @return 角色对象信息
     */
    SysPostVo selectPostById(Long postId);

    /**
     * 根据用户ID获取岗位选择框列表
     *
     * @param userId 用户ID
     * @return 选中岗位ID列表
     */
    List<Long> selectPostListByUserId(Long userId);

    /**
     * 校验岗位名称
     *
     * @param post 岗位信息
     * @return 结果
     */
    boolean checkPostNameUnique(SysPostBo post);

    /**
     * 校验岗位编码
     *
     * @param post 岗位信息
     * @return 结果
     */
    boolean checkPostCodeUnique(SysPostBo post);

    /**
     * 通过岗位ID查询岗位使用数量
     *
     * @param postId 岗位ID
     * @return 结果
     */
    long countUserPostById(Long postId);

    /**
     * 删除岗位信息
     *
     * @param postId 岗位ID
     * @return 结果
     */
    int deletePostById(Long postId);

    /**
     * 批量删除岗位信息
     *
     * @param postIds 需要删除的岗位ID
     * @return 结果
     */
    int deletePostByIds(Long[] postIds);

    /**
     * 新增保存岗位信息
     *
     * @param bo 岗位信息
     * @return 结果
     */
    int insertPost(SysPostBo bo);

    /**
     * 修改保存岗位信息
     *
     * @param bo 岗位信息
     * @return 结果
     */
    int updatePost(SysPostBo bo);
}
