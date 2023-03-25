import { SysDept } from '@/api/system/model/deptModel';
import { BaseEntity } from '@/api/model/resultModel';
import { SysRole } from '@/api/system/model/roleModel';
import { SysPost } from '@/api/system/model/postModel';

// 系统用户
export interface SysUser extends BaseEntity {
  /**
   * 用户ID
   */
  userId?: number;

  /**
   * 部门ID
   */
  deptId?: number;

  /**
   * 用户账号
   */
  userName?: string;

  /**
   * 用户昵称
   */
  nickName?: string;

  /**
   * 用户类型（sys_user系统用户）
   */
  userType?: string;

  /**
   * 用户邮箱
   */
  email?: string;

  /**
   * 手机号码
   */
  phonenumber?: string;

  /**
   * 用户性别
   */
  sex?: string;

  /**
   * 用户头像
   */
  avatar?: string;

  /**
   * 密码
   */
  password?: string;

  /**
   * 帐号状态（0正常 1停用）
   */
  status?: string;

  /**
   * 删除标志（0代表存在 2代表删除）
   */
  delFlag?: string;

  /**
   * 最后登录IP
   */
  loginIp?: string;

  /**
   * 最后登录时间
   */
  loginDate?: object;

  /**
   * 备注
   */
  remark?: string;

  /**
   * 部门对象
   */
  dept?: SysDept;

  /**
   * 角色对象
   */
  roles?: Array<SysRole>;

  /**
   * 角色组
   */
  roleIds?: Array<number>;

  /**
   * 岗位组
   */
  postIds?: Array<number>;

  /**
   * 数据权限 当前角色ID
   */
  roleId?: number;

  /**
   * 创建者
   */
  createBy?: string;

  /**
   * 创建时间
   */
  createTime?: object;

  /**
   * 更新者
   */
  updateBy?: string;

  /**
   * 更新时间
   */
  updateTime?: object;
}

export interface UserProfile {
  /**
   * 用户信息
   */
  user?: SysUser;
  /**
   * 用户所属角色组
   */
  roleGroup?: string;
  /**
   * 用户所属岗位组
   */
  postGroup?: string;
}

export interface UserAvatarResult {
  imgUrl?: string;
}

export interface UserAuthRole {
  user?: SysUser;
  roles?: Array<SysRole>;
}

export interface SysUserInfo {
  roles?: Array<SysRole>;
  posts?: Array<SysPost>;
  user?: SysUser;
  postIds?: Array<number>;
  roleIds?: Array<number>;
}
