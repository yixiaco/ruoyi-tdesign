package org.dromara.system.controller.system;

import cn.dev33.satoken.secure.BCrypt;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.io.FileUtil;
import org.dromara.common.core.domain.R;
import org.dromara.common.core.enums.UserType;
import org.dromara.common.core.exception.ServiceException;
import org.dromara.common.core.utils.StringUtils;
import org.dromara.common.core.utils.file.MimeTypeUtils;
import org.dromara.common.encrypt.annotation.ApiEncrypt;
import org.dromara.common.log.annotation.Log;
import org.dromara.common.log.enums.BusinessType;
import org.dromara.common.satoken.utils.LoginHelper;
import org.dromara.common.tenant.helper.TenantHelper;
import org.dromara.common.web.core.BaseController;
import org.dromara.system.domain.bo.SysOssBo;
import org.dromara.system.domain.bo.SysUserBo;
import org.dromara.system.domain.bo.SysUserPasswordBo;
import org.dromara.system.domain.bo.SysUserProfileBo;
import org.dromara.system.domain.vo.AvatarVo;
import org.dromara.system.domain.vo.ProfileVo;
import org.dromara.system.domain.vo.SysOssVo;
import org.dromara.system.domain.vo.SysUserVo;
import org.dromara.system.service.ISysOssService;
import org.dromara.system.service.ISysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;

/**
 * 个人信息 业务处理
 *
 * @author Lion Li
 */
@Validated
@RestController
@RequestMapping("/system/user/profile")
public class SysProfileController extends BaseController {

    @Autowired
    private ISysUserService userService;
    @Autowired
    private ISysOssService ossService;

    /**
     * 个人信息
     */
    @GetMapping
    public R<ProfileVo> profile() {
        SysUserVo user = userService.selectUserById(LoginHelper.getUserId());
        ProfileVo profileVo = new ProfileVo();
        profileVo.setUser(user);
        profileVo.setRoleGroup(userService.selectUserRoleGroup(user.getUserName()));
        profileVo.setPostGroup(userService.selectUserPostGroup(user.getUserName()));
        return R.ok(profileVo);
    }

    /**
     * 修改用户
     */
    @Log(title = "个人信息", businessType = BusinessType.UPDATE)
    @PutMapping
    public R<Void> updateProfile(@RequestBody SysUserProfileBo profile) {
        SysUserBo user = BeanUtil.toBean(profile, SysUserBo.class);
        String username = LoginHelper.getUsername();
        // 检查全局
        TenantHelper.ignore(() -> {
            if (StringUtils.isNotEmpty(user.getPhonenumber()) && !userService.checkPhoneUnique(user)) {
                throw new ServiceException("修改用户'" + username + "'失败，手机号码已存在");
            }
            if (StringUtils.isNotEmpty(user.getEmail()) && !userService.checkEmailUnique(user)) {
                throw new ServiceException("修改用户'" + username + "'失败，邮箱账号已存在");
            }
        });
        user.setUserId(LoginHelper.getUserId());
        if (userService.updateUserProfile(user)) {
            return R.ok();
        }
        return R.fail("修改个人信息异常，请联系管理员");
    }

    /**
     * 重置密码
     *
     * @param bo 新旧密码
     */
    @ApiEncrypt
    @Log(title = "个人信息", businessType = BusinessType.UPDATE)
    @PutMapping("/updatePwd")
    public R<Void> updatePwd(@Validated @RequestBody SysUserPasswordBo bo) {
        SysUserVo user = userService.selectUserById(LoginHelper.getUserId());
        String password = user.getPassword();
        if (!BCrypt.checkpw(bo.getOldPassword(), password)) {
            return R.fail("修改密码失败，旧密码错误");
        }
        if (BCrypt.checkpw(bo.getNewPassword(), password)) {
            return R.fail("新密码不能与旧密码相同");
        }

        if (userService.resetUserPwd(user.getUserId(), BCrypt.hashpw(bo.getNewPassword()))) {
            return R.ok();
        }
        return R.fail("修改密码异常，请联系管理员");
    }

    /**
     * 头像上传
     *
     * @param avatarfile 用户头像
     */
    @Log(title = "用户头像", businessType = BusinessType.UPDATE)
    @PostMapping(value = "/avatar", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public R<AvatarVo> avatar(@RequestPart("avatarfile") MultipartFile avatarfile) {
        if (!avatarfile.isEmpty()) {
            String extension = FileUtil.extName(avatarfile.getOriginalFilename());
            if (!StringUtils.equalsAnyIgnoreCase(extension, MimeTypeUtils.IMAGE_EXTENSION)) {
                return R.fail("文件格式不正确，请上传" + Arrays.toString(MimeTypeUtils.IMAGE_EXTENSION) + "格式");
            }
            SysOssBo bo = new SysOssBo();
            bo.setCreateBy(LoginHelper.getUserId());
            bo.setUserTypeEnum(UserType.SYS_USER);
            bo.setIsLock(1);
            bo.setOssCategoryId(0L);
            SysOssVo oss = ossService.upload(avatarfile, bo);
            String avatar = oss.getUrl();
            if (userService.updateUserAvatar(LoginHelper.getUserId(), oss.getOssId())) {
                AvatarVo avatarVo = new AvatarVo();
                avatarVo.setImgUrl(avatar);
                return R.ok(avatarVo);
            }
        }
        return R.fail("上传图片异常，请联系管理员");
    }
}
